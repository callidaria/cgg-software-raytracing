package tools;

import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class Turnin {

    // private static final String turninUrl = "https://turnin.tramberend.de";
    // private static final String turninUrl = "https://tramberend.bht-berlin.de/turnin";
    private static final String turninUrl = "http://localhost:3003";
    private static final String course = "CGG";
    private static final int year = 2024;
    private static final int term = 2;
    private static final int track = 1;

    private record FileData(String path, String mime_type, String hash, String base64) {
        FileData copy(boolean includeData) {
            return new FileData(path, mime_type, hash, includeData ? base64 : null);
        }

        private JSONObject toJson() {
            var o = new JSONObject();
            o.put("path", path);
            o.put("mime_type", mime_type);
            o.put("hash", hash);
            o.put("base64", base64);
            return o;
        }
    }

    private static void upload(String login, String password, String tag) {
        try {
            // constructs a JSON object matching
            //
            // pub enum UploadAuthInfo {
            // Token { random: String },
            // Basic { login: String, password: String },
            // }
            //
            var creds = new JSONObject();
            var basic = new JSONObject();
            creds.put("Basic", basic);
            basic.put("login", login);
            basic.put("password", password);

            var authorization = Base64.getEncoder().encodeToString(creds.toString().getBytes());

            var files = globUploadFiles(tag);

            var fileData = files.stream()
                    .map(Turnin::fileData).toList();
            var hashes = fileData.stream()
                    .map(FileData::hash).toList();
            var json = new JSONArray(hashes);

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(turninUrl + "/hashes"))
                    .header("Content-Type", "application/json")
                    .header("x-auth", authorization)
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());
            handleError(response);

            System.out.println("DEBUG missing file hashes:\n" + response.body());

            Set<String> required = new TreeSet<>();
            new JSONArray(response.body()).forEach(e -> required.add(e.toString()));

            fileData = fileData.stream()
                    .map(fd -> fd.copy(required.contains(fd.hash)))
                    .toList();

            var jsonFileData = new JSONArray(fileData.stream().map(fd -> fd.toJson()).toList());
            var upload = new JSONObject();
            upload.put("acronym", course);
            upload.put("year", year);
            upload.put("term", term);
            upload.put("track", track);
            upload.put("tag", tag);
            upload.put("files", jsonFileData);

            var uploadData = upload.toString();
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(URI.create(turninUrl + "/uploads"))
                    .header("Content-Type", "application/json")
                    .header("x-auth", authorization)
                    .POST(HttpRequest.BodyPublishers.ofString(uploadData))
                    .build();

            System.out.format("Uploading ...\n");
            HttpResponse<String> response2 = httpClient
                    .send(request2, HttpResponse.BodyHandlers.ofString());
            handleError(response2);

            System.out.format("Upload successful (files: %d, bytes transferred: %d):\n", fileData.size(),
                    uploadData.length());
            for (var fd : fileData)
                System.out.format("  %s (%s)\n", fd.path, fd.base64 == null ? "cached" : fd.base64.length() + " bytes");
        } catch (Exception e) {
            panic(e);
        }
    }

    private static void handleError(HttpResponse<String> response) {
        if (response.statusCode() / 100 != 2) {
            System.out.format("Server says: %d %s.\n", response.statusCode(), response.body());
            System.out.format("Upload failed.\n");
            System.exit(1);
        }
    }

    private static List<Path> match(Path root, String pattern) throws IOException {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        return Files.walk(root, FileVisitOption.FOLLOW_LINKS).filter(matcher::matches).toList();
    }

    private static FileData fileData(Path file) {
        try {
            var bytes = Files.readAllBytes(file);
            var hash = hash(bytes);
            var base64 = Base64.getEncoder().encodeToString(bytes);
            var mime_type = URLConnection.guessContentTypeFromName(file.toString());
            return new FileData(file.toString(), mime_type, hash, base64);
        } catch (IOException e) {
            return panic(e);
        }
    }

    private static <T> T panic(Exception e) {
        System.out.format("An error occured: %s.\n", e);
        System.out.format("Cause: %s.\n", e.getCause());
        System.out.format("Upload failed.\n");
        // e.printStackTrace();
        System.exit(1);
        return null;
    }

    private static String hash(byte[] blob) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(blob);
            return HexFormat.of().formatHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return panic(e);
        }
    }

    static List<Path> globUploadFiles(String tag) throws IOException {
        var files = new ArrayList<Path>();
        files.addAll(match(Path.of("src"), "src/solutions/*.java"));
        files.addAll(match(Path.of("src"), "src/solutions/" + tag + "/*.java"));
        files.addAll(match(Path.of("images"), "images/" + tag + "-*.png"));
        return files;
    }

    // Interactive upload of submission to the Turnin server
    public static void upload() {
        System.out.format("Upload to %s\n", turninUrl);
        var tag = System.console().readLine("Tag:     ");
        if (tag.equals("")) {
            System.out.println("No tag given. Upload aborted.");
        } else {
            var login = System.console().readLine("Login:   ");
            var password = System.console().readPassword("Password: ");
            upload(login, String.valueOf(password).trim(), tag.trim());
        }
    }

    public static void main(String[] args) {
        upload();
    }
}
