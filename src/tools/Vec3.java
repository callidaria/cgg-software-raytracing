
package tools;

import static tools.Functions.*;

public final record Vec3(double x, double y, double z) {

    public static final Vec3 zero = vec3(0, 0, 0);
    public static final Vec3 xAxis = vec3(1, 0, 0);
    public static final Vec3 yAxis = vec3(0, 1, 0);
    public static final Vec3 zAxis = vec3(0, 0, 1);
    public static final Vec3 nxAxis = vec3(-1, 0, 0);
    public static final Vec3 nyAxis = vec3(0, -1, 0);
    public static final Vec3 nzAxis = vec3(0, 0, -1);

    public double u() {
        return x;
    }

    public double v() {
        return y;
    }

    public double w() {
        return z;
    }

    @Override
    public String toString() {
        return String.format("(Vec3: %.2f %.2f %.2f)", x, y, z);
    }
}
