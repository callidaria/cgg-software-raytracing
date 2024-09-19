package tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mokiat.data.front.parser.OBJDataReference;
import com.mokiat.data.front.parser.MTLParser;
import com.mokiat.data.front.parser.MTLColor;
import com.mokiat.data.front.parser.OBJParser;

import static tools.Functions.*;

/**
 * A more gentle front for the OBJParser. Geometry datatypes are mapped and
 * textures are provided as Sampler objects.
 */
public class Wavefront {

  public record TriangleData(Vertex v0, Vertex v1, Vertex v2) {
  }

  public record MaterialData(
      Color ka,
      Sampler kaMap,
      Color kd,
      Sampler kdMap,
      Color ks,
      Sampler ksMap,
      double ns) {
  }

  public record MeshData(
      List<TriangleData> triangles,
      MaterialData material,
      String materialName) {
  }

  public record ObjectData(String name, List<MeshData> meshes) {
  }

  public static List<TriangleData> loadTriangleData(String filename) {
    return loadMeshData(filename)
        .stream()
        .flatMap(m -> m.triangles.stream())
        .toList();
  }

  public static List<MeshData> loadMeshData(String filename) {
    return loadObjectData(filename).values()
        .stream()
        .flatMap(o -> o.meshes.stream())
        .toList();
  }

  public static Map<String, ObjectData> loadObjectData(String filename) {
    String dirname = new File(filename).getParent();
    Map<String, ObjectData> objects = new TreeMap<>();

    try (var input = new FileInputStream(filename)) {
      int meshCount = 0;
      int triangleCount = 0;
      int materialCount = 0;

      BoundingBox bounds = BoundingBox.empty;
      final var parser = new OBJParser();
      final var model = parser.parse(input);

      final List<Vec3> vertices = new ArrayList<>();
      for (var v : model.getVertices())
        vertices.add(vec3(v.x, v.y, v.z));

      final List<Vec3> normals = new ArrayList<>();
      for (var n : model.getNormals())
        normals.add(vec3(n.x, n.y, n.z));

      final List<Vec2> uvs = new ArrayList<>();
      for (var t : model.getTexCoords())
        uvs.add(vec2(t.u, t.v));

      var materials = new TreeMap<String, MaterialData>();
      for (var mtllib : model.getMaterialLibraries()) {
        // System.out.println("mtllib: " + mtllib);
        try (var min = new FileInputStream(dirname + "/" + mtllib)) {
          var mtlParser = new MTLParser();
          var lib = mtlParser.parse(min);
          for (var material : lib.getMaterials()) {
            // System.out.println(" material: " + material.getName());
            materials.put(
                material.getName(),
                new MaterialData(
                    toColor(material.getAmbientColor()),
                    toSampler(dirname,
                        material.getAmbientColor(),
                        material.getAmbientTexture()),
                    toColor(material.getDiffuseColor()),
                    toSampler(dirname,
                        material.getDiffuseColor(),
                        material.getDiffuseTexture()),
                    toColor(material.getSpecularColor()),
                    toSampler(dirname,
                        material.getSpecularColor(),
                        material.getSpecularTexture()),
                    material.getSpecularExponent()));
            materialCount += 1;
          }
        } catch (Exception e) {
          System.err.println("ERROR: " + e);
        }
      }

      for (var object : model.getObjects()) {
        List<MeshData> meshes = new ArrayList<>();
        for (var mesh : object.getMeshes()) {
          List<TriangleData> triangles = new ArrayList<>();
          for (var face : mesh.getFaces()) {

            var refs = face.getReferences();
            var p0 = getVec3OrZero(vertices, refs.get(0).vertexIndex);
            var n0 = getVec3OrZero(normals, refs.get(0).normalIndex);
            var t0 = getVec2OrZero(uvs, refs.get(0).texCoordIndex);
            for (int i = 1; i < refs.size() - 1; i++) {
              var p1 = getVec3OrZero(vertices, refs.get(i).vertexIndex);
              var n1 = getVec3OrZero(normals, refs.get(i).normalIndex);
              var t1 = getVec2OrZero(uvs, refs.get(i).texCoordIndex);
              var p2 = getVec3OrZero(vertices, refs.get(i + 1).vertexIndex);
              var n2 = getVec3OrZero(normals, refs.get(i + 1).normalIndex);
              var t2 = getVec2OrZero(uvs, refs.get(i + 1).texCoordIndex);
              triangles.add(new TriangleData(new Vertex(p0, n0, t0),
                  new Vertex(p1, n1, t1),
                  new Vertex(p2, n2, t2)));
              bounds = bounds.extend(p0).extend(p1).extend(p2);
            }
          }

          var materialName = mesh.getMaterialName();
          var material = materialName != null
              ? materials.get(materialName)
              : defaultMaterialData;

          meshes.add(new MeshData(triangles, material, materialName));
          triangleCount += triangles.size();
        }
        var name = object.getName();
        objects.put(name, new ObjectData(name, meshes));
        meshCount += meshes.size();
      }
      System.out.format("%s: %d materials, %d meshes, %d triangles\n",
          filename, materialCount, meshCount, triangleCount);

    } catch (Exception e) {
      System.err.println("ERROR: cannot load " + filename + ": " + e);
      e.printStackTrace();
    }

    return objects;
  }

  private static Vec2 getVec2OrZero(List<Vec2> list, int index) {
    if (index == OBJDataReference.UNDEFINED_INDEX)
      return Vec2.zero;
    else
      return list.get(index);
  }

  private static Vec3 getVec3OrZero(List<Vec3> list, int index) {
    if (index == OBJDataReference.UNDEFINED_INDEX)
      return Vec3.zero;
    else
      return list.get(index);
  }

  private static Sampler toSampler(String dirname, MTLColor mtlc, String filename) {
    if (filename != null) {
      var pathname = dirname + "/" + filename;
      System.out.println("    texture: " + pathname);
      return new ImageTexture(pathname);
    } else {
      return new ColorSampler(color(mtlc.r, mtlc.g, mtlc.b));
    }
  }

  private static Color toColor(MTLColor mtlc) {
    return color(mtlc.r, mtlc.g, mtlc.b);
  }

  private static MaterialData defaultMaterialData = new MaterialData(
      color(0.1, 0.1, 0.1), new ColorSampler(color(0.1, 0.1, 0.1)),
      Color.gray, new ColorSampler(Color.gray),
      Color.gray, new ColorSampler(Color.gray), 0);
}
