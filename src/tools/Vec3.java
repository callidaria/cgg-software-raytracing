package tools;

import static tools.Functions.*;

/**
 * Represents a 3D vector with x, y, and z components.
 * This class provides constants for common vectors and methods for accessing components.
 */
public final record Vec3(double x, double y, double z) {

    /** The zero vector (0, 0, 0). */
    public static final Vec3 zero = vec3(0, 0, 0);
    /** The unit vector along the x-axis (1, 0, 0). */
    public static final Vec3 xAxis = vec3(1, 0, 0);
    /** The unit vector along the y-axis (0, 1, 0). */
    public static final Vec3 yAxis = vec3(0, 1, 0);
    /** The unit vector along the z-axis (0, 0, 1). */
    public static final Vec3 zAxis = vec3(0, 0, 1);
    /** The negative unit vector along the x-axis (-1, 0, 0). */
    public static final Vec3 nxAxis = vec3(-1, 0, 0);
    /** The negative unit vector along the y-axis (0, -1, 0). */
    public static final Vec3 nyAxis = vec3(0, -1, 0);
    /** The negative unit vector along the z-axis (0, 0, -1). */
    public static final Vec3 nzAxis = vec3(0, 0, -1);

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
     * Returns the z-coordinate of the vector.
     * This method is an alias for the z() method.
     *
     * @return The z-coordinate (w-component) of the vector.
     */
    public double w() {
        return z;
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return A string in the format "(Vec3: x.xx y.yy z.zz)".
     */
    @Override
    public String toString() {
        return String.format("(Vec3: %.2f %.2f %.2f)", x, y, z);
    }
}