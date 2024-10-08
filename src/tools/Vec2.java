package tools;

import static tools.Functions.*;

/**
 * Represents a 2D vector with x and y components.
 * This class provides constants for common vectors and methods for accessing components.
 */
public final record Vec2(double x, double y) {

    /** The zero vector (0, 0). */
    public static final Vec2 zero = vec2(0, 0);
    /** The unit vector along the x-axis (1, 0). */
    public static final Vec2 xAxis = vec2(1, 0);
    /** The unit vector along the y-axis (0, 1). */
    public static final Vec2 yAxis = vec2(0, 1);
    /** The negative unit vector along the x-axis (-1, 0). */
    public static final Vec2 nxAxis = vec2(-1, 0);
    /** The negative unit vector along the y-axis (0, -1). */
    public static final Vec2 nyAxis = vec2(0, -1);

    /**
     * Returns the x-coordinate of the vector.
     * This method is an alias for the x() method.
     *
     * @return The x-coordinate (u-component) of the vector.
     */
    public double u() {
        return x;
    }

    /**
     * Returns the y-coordinate of the vector.
     * This method is an alias for the y() method.
     *
     * @return The y-coordinate (v-component) of the vector.
     */
    public double v() {
        return y;
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return A string in the format "(Vec2: x.xx y.yy)".
     */
    @Override
    public String toString() {
        return String.format("(Vec2: %.2f %.2f)", x, y);
    }
}