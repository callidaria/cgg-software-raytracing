
package cgg.a01;

import tools.*;

// Represents the contents of an image. Provides the same color for all pixels.
public record ConstantColor(Color color) implements Sampler {

    // Returns the color for the given position.
    public Color getColor(Vec2 point) {
        return color;
    }
}
