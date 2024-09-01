
package tools;

import static tools.Functions.*;

public final record Vec2(double x, double y) {

    public static final Vec2 zero = vec2(0, 0);
    public static final Vec2 xAxis = vec2(1, 0);
    public static final Vec2 yAxis = vec2(0, 1);
    public static final Vec2 nxAxis = vec2(-1, 0);
    public static final Vec2 nyAxis = vec2(0, -1);

    public double u() {
        return x;
    }

    public double v() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("(Vec2: %.2f %.2f)", x, y);
    }
}
