
package tools;

import static tools.Functions.*;

/**
 * A simple 3D vector. All relevant vector operations are static methods on the
 * tools.Function class.
 */
 public final record Vec3(double x, double y, double z) {

    public static final Vec3 zero = vec3(0, 0, 0);
    public static final Vec3 xAxis = vec3(1, 0, 0);
    public static final Vec3 yAxis = vec3(0, 1, 0);
    public static final Vec3 zAxis = vec3(0, 0, 1);
    public static final Vec3 nxAxis = vec3(-1, 0, 0);
    public static final Vec3 nyAxis = vec3(0, -1, 0);
    public static final Vec3 nzAxis = vec3(0, 0, -1);

    /** An alias for the x-coordinate. */
    public double u() {
        return x;
    }

    /** An alias for the y-coordinate. */
    public double v() {
        return y;
    }

    /** An alias for the z-coordinate. */
    public double w() {
        return z;
    }

    @Override
    public String toString() {
        return String.format("(Vec3: %.2f %.2f %.2f)", x, y, z);
    }
}
