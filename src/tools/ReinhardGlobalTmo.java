package tools;

import static tools.Functions.*;

// inspired by https://64.github.io/tonemapping/

public class ReinhardGlobalTmo {
    double a;
    Double white;

    public ReinhardGlobalTmo(double a) {
        this.a = a;
        this.white = null;
    }

    public ReinhardGlobalTmo(double a, double white) {
        this.a = a;
        this.white = white;
    }

    public void toneMap(Image img) {
        var average = averageLuminance(img);
        var max = maxLuminance(img);
        System.out.format("maximum luminance before: %02f", max);
        if (white == null)
            white = max;
        for (int x = 0; x != img.width(); x++) {
            for (int y = 0; y != img.height(); y++) {
                var luminance = luminance(img.getPixel(x, y));
                var scaled = (a / average) * luminance;
                var display = (scaled * (1 + (scaled / (white * white)))) / (1 + scaled);
                img.setPixel(x, y, changeLuminance(img.getPixel(x, y), display));
            }
        }
        System.out.format("maximum luminance after : %02f", maxLuminance(img));
    }

    private double luminance(Color c) {
        return dot(vec3(c), vec3(0.299, 0.587, 0.144));
    }

    private Color changeLuminance(Color c, double out) {
        return multiply(out / luminance(c), c);
    }

    private double averageLuminance(Image img) {
        var sum = 0;
        var n = img.width() * img.height();
        for (int x = 0; x != img.width(); x++) {
            for (int y = 0; y != img.height(); y++) {
                sum += Math.log(1e-5 + luminance(img.getPixel(x, y)));
            }
        }
        return Math.exp(sum / n);
    }

    private double maxLuminance(Image img) {
        var max = 0.0;
        for (int x = 0; x != img.width(); x++) {
            for (int y = 0; y != img.height(); y++) {
                max = Math.max(max, luminance(img.getPixel(x, y)));
            }
        }
        return max;
    }
}
