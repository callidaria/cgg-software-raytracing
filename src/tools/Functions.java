package tools;

public class Functions {

    static public final double EPSILON = 1e-5;

    // Functions on doubles
    public static boolean in(double a, double b, double v) {
        return a <= v && v <= b;
    }

    public static boolean almostEqual(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    // Static Vec2 functions
    public static Vec2 vec2(double x, double y) {
        return new Vec2(x, y);
    }

    public static Vec2 add(Vec2 a, Vec2 b, Vec2... vs) {
        Vec2 r = vec2(a.x() + b.x(), a.y() + b.y());
        for (var v : vs)
            r = vec2(r.x() + v.x(), r.y() + v.y());
        return r;
    }

    public static Vec2 subtract(Vec2 a, Vec2 b, Vec2... vs) {
        Vec2 r = vec2(a.x() - b.x(), a.y() - b.y());
        for (var v : vs)
            r = vec2(r.x() - v.x(), r.y() - v.y());
        return r;
    }

    // linear interpolation betweeen 2 points
    public static Vec2 interplolate(Vec2 a, Vec2 b, double t) {
        return add(multiply(a, 1 - t), multiply(b, t));
    }

    // barycentric interpolation betweeen 3 points
    public static Vec2 interplolate(Vec2 a, Vec2 b, Vec2 c, Vec3 uvw) {
        return add(multiply(a, uvw.u()), multiply(b, uvw.v()), multiply(c, uvw.w()));
    }

    public static Vec2 multiply(double s, Vec2 a) {
        return vec2(s * a.x(), s * a.y());
    }

    public static Vec2 multiply(Vec2 a, double s) {
        return vec2(s * a.x(), s * a.y());
    }

    public static Vec2 multiply(Vec2 a, Vec2 b) {
        return vec2(a.x() * b.x(), a.y() * b.y());
    }

    public static Vec2 negate(Vec2 a) {
        return vec2(-a.x(), -a.y());
    }

    public static Vec2 divide(Vec2 a, double s) {
        return vec2(a.x() / s, a.y() / s);
    }

    public static double dot(Vec2 a, Vec2 b) {
        return a.x() * b.x() + a.y() * b.y();
    }

    public static double cross(Vec2 a, Vec2 b) {
        return a.x() * b.y() - a.y() * b.x();
    }

    public static double length(Vec2 a) {
        return (double) Math.sqrt(a.x() * a.x() + a.y() * a.y());
    }

    public static double squaredLength(Vec2 a) {
        return a.x() * a.x() + a.y() * a.y();
    }

    public static Vec2 normalize(Vec2 a) {
        return divide(a, length(a));
    }

    public static Vec2 mod(Vec2 a, Vec2 b) {
        return vec2(a.x() % b.x(), a.y() % b.y());
    }

    public static Vec2 min(Vec2 a, Vec2 b) {
        return new Vec2(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()));
    }

    public static Vec2 max(Vec2 a, Vec2 b) {
        return new Vec2(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()));
    }

    // Static Vec3 functions
    public static Vec3 vec3(double x, double y, double z) {
        return new Vec3(x, y, z);
    }

    public static Vec3 vec3(double x) {
        return new Vec3(x, x, x);
    }

    public static Vec3 vec3(Vec2 v) {
        return new Vec3(v.x(), v.y(), 0);
    }

    public static Vec3 vec3(Vec2 v, double z) {
        return new Vec3(v.x(), v.y(), z);
    }

    public static Vec3 vec3(Color c) {
        return new Vec3(c.r(), c.g(), c.b());
    }

    public static Vec3 add(Vec3 a, Vec3 b, Vec3... vs) {
        Vec3 r = vec3(a.x() + b.x(), a.y() + b.y(), a.z() + b.z());
        for (var v : vs)
            r = vec3(r.x() + v.x(), r.y() + v.y(), r.z() + v.z());
        return r;
    }

    public static Vec3 subtract(Vec3 a, Vec3 b, Vec3... vs) {
        Vec3 r = vec3(a.x() - b.x(), a.y() - b.y(), a.z() - b.z());
        for (var v : vs)
            r = vec3(r.x() - v.x(), r.y() - v.y(), r.z() - v.z());
        return r;
    }

    public static Vec3 multiply(double s, Vec3 a) {
        return vec3(s * a.x(), s * a.y(), s * a.z());
    }

    public static Vec3 multiply(Vec3 a, double s) {
        return vec3(s * a.x(), s * a.y(), s * a.z());
    }

    public static Vec3 multiply(Vec3 a, Vec3 b) {
        return vec3(a.x() * b.x(), a.y() * b.y(), a.z() * b.z());
    }

    // linear interpolation betweeen 2 points
    public static Vec3 interplolate(Vec3 a, Vec3 b, double t) {
        return add(multiply(a, 1 - t), multiply(b, t));
    }

    // barycentric interpolation betweeen 3 points
    public static Vec3 interplolate(Vec3 a, Vec3 b, Vec3 c, Vec3 uvw) {
        return add(multiply(a, uvw.u()), multiply(b, uvw.v()), multiply(c, uvw.w()));
    }

    public static Vec3 negate(Vec3 a) {
        return vec3(-a.x(), -a.y(), -a.z());
    }

    public static Vec3 divide(Vec3 a, double s) {
        return vec3(a.x() / s, a.y() / s, a.z() / s);
    }

    public static double dot(Vec3 a, Vec3 b) {
        return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
    }

    public static Vec3 cross(Vec3 a, Vec3 b) {
        return vec3(a.y() * b.z() - a.z() * b.y(), a.z() * b.x() - a.x() * b.z(),
                a.x() * b.y() - a.y() * b.x());
    }

    public static double length(Vec3 a) {
        return (double) Math.sqrt(a.x() * a.x() + a.y() * a.y() + a.z() * a.z());
    }

    public static double squaredLength(Vec3 a) {
        return a.x() * a.x() + a.y() * a.y() + a.z() * a.z();
    }

    public static double coordSum(Vec3 v) {
        return v.x() + v.y() + v.z();
    }

    public static boolean in(double a, double b, Vec3 v) {
        return in(a, b, v.x()) && in(a, b, v.y()) && in(a, b, v.w());
    }

    public static Vec3 normalize(Vec3 a) {
        return divide(a, length(a));
    }

    public static Vec3 mod(Vec3 a, Vec3 b) {
        return vec3(a.x() % b.x(), a.y() % b.y(), a.z() % b.z());
    }

    public static Vec3 min(Vec3 a, Vec3 b) {
        return new Vec3(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()),
                Math.min(a.z(), b.z()));
    }

    public static Vec3 max(Vec3 a, Vec3 b) {
        return new Vec3(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()),
                Math.max(a.z(), b.z()));
    }

    // Static Color functions
    public static Color color(double r, double g, double b) {
        return new Color(r, g, b);
    }

    public static Color color(double i) {
        return color(i, i, i);
    }

    public static Color color(Vec2 v) {
        return color(v.x(), v.y(), 0);
    }

    public static Color color(Vec3 v) {
        return color(v.x(), v.y(), v.z());
    }

    public static Color add(Color a, Color... vs) {
        for (Color v : vs) {
            a = color(a.r() + v.r(), a.g() + v.g(), a.b() + v.b());
        }
        return a;
    }

    public static Color subtract(Color a, Color b, Color... vs) {
        Color r = color(a.r() - b.r(), a.g() - b.g(), a.b() - b.b());
        for (Color v : vs) {
            r = color(r.r() - v.r(), r.g() - v.g(), r.b() - v.b());
        }
        return r;
    }

    public static Color multiply(double s, Color a) {
        return color(s * a.r(), s * a.g(), s * a.b());
    }

    public static Color multiply(Color a, double s) {
        return multiply(s, a);
    }

    public static Color multiply(Color a, Color b) {
        return color(a.r() * b.r(), a.g() * b.g(), a.b() * b.b());
    }

    public static Color divide(Color a, double s) {
        return color(a.r() / s, a.g() / s, a.b() / s);
    }

    public static Color clamp(Color v) {
        return color(Math.min(1, Math.max(v.r(), 0)),
                Math.min(1, Math.max(v.g(), 0)),
                Math.min(1, Math.max(v.b(), 0)));
    }

    private static Color hue(double h) {
        double r = Math.abs(h * 6 - 3) - 1;
        double g = 2 - Math.abs(h * 6 - 2);
        double b = 2 - Math.abs(h * 6 - 4);
        return clamp(color(r, g, b));
    }

    public static Color hsvToRgb(double h, double s, double v) {
        return multiply(v, add(multiply(s, subtract(hue(h), Color.white)), Color.white));
    }

    public static Color hsvToRgb(Color c) {
        return hsvToRgb(c.r(), c.g(), c.b());
    }

    // linear interpolation betweeen 2 colors
    public static Color interpolate(Color a, Color b, double t) {
        return color(a.r() * (1 - t) + b.r() * t, a.g() * (1 - t) + b.g() * t, a.b() * (1 - t) + b.b() * t);
    }

    // barycentric interpolation betweeen 3 colors
    public static Color interpolate(Color a, Color b, Color c, Vec3 uvw) {
        return add(multiply(a, uvw.u()), multiply(b, uvw.v()), multiply(c, uvw.w()));
    }

    // Static matrix functions
    public static Mat44 matrix(Vec3 b0, Vec3 b1, Vec3 b2) {
        Mat44 m = new Mat44();
        m.set(0, 0, b0.x());
        m.set(1, 0, b0.y());
        m.set(2, 0, b0.z());
        m.set(0, 1, b1.x());
        m.set(1, 1, b1.y());
        m.set(2, 1, b1.z());
        m.set(0, 2, b2.x());
        m.set(1, 2, b2.y());
        m.set(2, 2, b2.z());
        return m;
    }

    public static Mat44 matrix(Vec3 b0, Vec3 b1, Vec3 b2, Vec3 b3) {
        Mat44 m = new Mat44();
        m.set(0, 0, b0.x());
        m.set(1, 0, b0.y());
        m.set(2, 0, b0.z());
        m.set(0, 1, b1.x());
        m.set(1, 1, b1.y());
        m.set(2, 1, b1.z());
        m.set(0, 2, b2.x());
        m.set(1, 2, b2.y());
        m.set(2, 2, b2.z());
        m.set(0, 3, b3.x());
        m.set(1, 3, b3.y());
        m.set(2, 3, b3.z());
        return m;
    }

    public static Mat44 identity() {
        return Mat44.identity;
    }

    public static Mat44 move(Vec3 t) {
        Mat44 m = new Mat44();
        m.set(3, 0, t.x());
        m.set(3, 1, t.y());
        m.set(3, 2, t.z());
        return m;
    }

    public static Mat44 move(double x, double y, double z) {
        Mat44 m = new Mat44();
        m.set(3, 0, x);
        m.set(3, 1, y);
        m.set(3, 2, z);
        return m;
    }

    public static Mat44 rotate(Vec3 axis, double angle) {
        final Mat44 m = new Mat44();
        final double rad = Math.toRadians(angle);
        final double cosa = (double) Math.cos(rad);
        final double sina = (double) Math.sin(rad);
        final double l = Math.sqrt(axis.x() * axis.x() + axis.y() * axis.y() + axis.z() * axis.z());
        final double rx = axis.x() / l;
        final double ry = axis.y() / l;
        final double rz = axis.z() / l;
        final double icosa = 1 - cosa;

        m.set(0, 0, (double) (icosa * rx * rx + cosa));
        m.set(0, 1, (double) (icosa * rx * ry + rz * sina));
        m.set(0, 2, (double) (icosa * rx * rz - ry * sina));

        m.set(1, 0, (double) (icosa * rx * ry - rz * sina));
        m.set(1, 1, (double) (icosa * ry * ry + cosa));
        m.set(1, 2, (double) (icosa * ry * rz + rx * sina));

        m.set(2, 0, (double) (icosa * rx * rz + ry * sina));
        m.set(2, 1, (double) (icosa * ry * rz - rx * sina));
        m.set(2, 2, (double) (icosa * rz * rz + cosa));
        return m;
    }

    public static Mat44 rotate(double ax, double ay, double az, double angle) {
        return rotate(vec3(ax, ay, az), angle);
    }

    public static Mat44 scale(double x, double y, double z) {
        Mat44 m = new Mat44();
        m.set(0, 0, x);
        m.set(1, 1, y);
        m.set(2, 2, z);
        return m;
    }

    public static Mat44 scale(Vec3 d) {
        Mat44 m = new Mat44();
        m.set(0, 0, d.x());
        m.set(1, 1, d.y());
        m.set(2, 2, d.z());
        return m;
    }

    // https://en.m.wikibooks.org/wiki/GLSL_Programming/Vertex_Transformations
    //
    // This is not your fathers perspective projection matrix, Luke.
    // Here, z is mapped to [-1,1] and does *not* need perspective division.
    public static Mat44 perspective(double fovy, double a, double n, double f) {
        final double fov = (fovy / 181.0) * Math.PI;
        final double d = 1 / Math.tan(fov / 2);
        Mat44 m = new Mat44();
        m.set(0, 0, d / a);
        m.set(1, 1, d);

        // From ortho
        // m.set(2, 2, -2 / (f - n));
        // m.set(3, 2, -(f + n) / (f - n));

        // Simple
        // m.set(2, 2, -1/(f-n));
        // m.set(3, 2, -n/(f-n));

        // From perspective. Yields 1/z after division, which can be interpolated
        // linearily.
        m.set(2, 2, (n + f) / (n - f));
        m.set(3, 2, (2 * n * f) / (n - f));

        m.set(2, 3, -1);
        m.set(3, 3, 0);
        return m;
    }

    // https://en.m.wikibooks.org/wiki/GLSL_Programming/Vertex_Transformations
    public static Mat44 viewport(double sx, double sy, double w, double h, double n, double f) {
        Mat44 m = new Mat44();
        m.set(0, 0, w / 2);
        m.set(3, 0, sx + w / 2);
        m.set(1, 1, -h / 2); // Flip y
        m.set(3, 1, sy + h / 2);
        m.set(2, 2, (f - n) / 2);
        m.set(3, 2, (n + f) / 2);
        return m;
    }

    public static Mat44 multiply(Mat44 a, Mat44 b, Mat44... ms) {
        Mat44 r = a.multiply(b);
        for (Mat44 m : ms)
            r = r.multiply(m);
        return r;
    }

    public static Vec3 multiplyPoint(Mat44 m, Vec3 p) {
        final double x = m.get(0, 0) * p.x() + m.get(1, 0) * p.y() + m.get(2, 0) * p.z() + m.get(3, 0);
        final double y = m.get(0, 1) * p.x() + m.get(1, 1) * p.y() + m.get(2, 1) * p.z() + m.get(3, 1);
        final double z = m.get(0, 2) * p.x() + m.get(1, 2) * p.y() + m.get(2, 2) * p.z() + m.get(3, 2);
        return vec3(x, y, z);
    }

    public static Vec3 multiplyDirection(Mat44 m, Vec3 d) {
        double x = m.get(0, 0) * d.x() + m.get(1, 0) * d.y() + m.get(2, 0) * d.z();
        double y = m.get(0, 1) * d.x() + m.get(1, 1) * d.y() + m.get(2, 1) * d.z();
        double z = m.get(0, 2) * d.x() + m.get(1, 2) * d.y() + m.get(2, 2) * d.z();
        return vec3(x, y, z);
    }

    public static Mat44 transpose(Mat44 m) {
        Mat44 n = new Mat44();
        for (int c = 0; c != 4; c++) {
            for (int r = 0; r != 4; r++) {
                n.set(c, r, m.get(r, c));
            }
        }
        return n;
    }

    public static Mat44 invert(Mat44 m) {
        Mat44 ret = new Mat44();
        double[] mat = m.values();
        double[] dst = ret.values();
        double[] tmp = new double[12];

        /* temparray for pairs */
        double src[] = new double[16];

        /* array of transpose source matrix */
        double det;

        /* determinant */
        /*
         * transpose matrix
         */
        for (int i = 0; i < 4; i++) {
            src[i] = mat[i * 4];
            src[i + 4] = mat[i * 4 + 1];
            src[i + 8] = mat[i * 4 + 2];
            src[i + 12] = mat[i * 4 + 3];
        }

        /* calculate pairs for first 8 elements (cofactors) */
        tmp[0] = src[10] * src[15];
        tmp[1] = src[11] * src[14];
        tmp[2] = src[9] * src[15];
        tmp[3] = src[11] * src[13];
        tmp[4] = src[9] * src[14];
        tmp[5] = src[10] * src[13];
        tmp[6] = src[8] * src[15];
        tmp[7] = src[11] * src[12];
        tmp[8] = src[8] * src[14];
        tmp[9] = src[10] * src[12];
        tmp[10] = src[8] * src[13];
        tmp[11] = src[9] * src[12];

        /* calculate first 8 elements (cofactors) */
        dst[0] = tmp[0] * src[5] + tmp[3] * src[6] + tmp[4] * src[7];
        dst[0] -= tmp[1] * src[5] + tmp[2] * src[6] + tmp[5] * src[7];
        dst[1] = tmp[1] * src[4] + tmp[6] * src[6] + tmp[9] * src[7];
        dst[1] -= tmp[0] * src[4] + tmp[7] * src[6] + tmp[8] * src[7];
        dst[2] = tmp[2] * src[4] + tmp[7] * src[5] + tmp[10] * src[7];
        dst[2] -= tmp[3] * src[4] + tmp[6] * src[5] + tmp[11] * src[7];
        dst[3] = tmp[5] * src[4] + tmp[8] * src[5] + tmp[11] * src[6];
        dst[3] -= tmp[4] * src[4] + tmp[9] * src[5] + tmp[10] * src[6];
        dst[4] = tmp[1] * src[1] + tmp[2] * src[2] + tmp[5] * src[3];
        dst[4] -= tmp[0] * src[1] + tmp[3] * src[2] + tmp[4] * src[3];
        dst[5] = tmp[0] * src[0] + tmp[7] * src[2] + tmp[8] * src[3];
        dst[5] -= tmp[1] * src[0] + tmp[6] * src[2] + tmp[9] * src[3];
        dst[6] = tmp[3] * src[0] + tmp[6] * src[1] + tmp[11] * src[3];
        dst[6] -= tmp[2] * src[0] + tmp[7] * src[1] + tmp[10] * src[3];
        dst[7] = tmp[4] * src[0] + tmp[9] * src[1] + tmp[10] * src[2];
        dst[7] -= tmp[5] * src[0] + tmp[8] * src[1] + tmp[11] * src[2];

        /* calculate pairs for second 8 elements (cofactors) */
        tmp[0] = src[2] * src[7];
        tmp[1] = src[3] * src[6];
        tmp[2] = src[1] * src[7];
        tmp[3] = src[3] * src[5];
        tmp[4] = src[1] * src[6];
        tmp[5] = src[2] * src[5];
        tmp[6] = src[0] * src[7];
        tmp[7] = src[3] * src[4];
        tmp[8] = src[0] * src[6];
        tmp[9] = src[2] * src[4];
        tmp[10] = src[0] * src[5];
        tmp[11] = src[1] * src[4];

        /* calculate second 8 elements (cofactors) */
        dst[8] = tmp[0] * src[13] + tmp[3] * src[14] + tmp[4] * src[15];
        dst[8] -= tmp[1] * src[13] + tmp[2] * src[14] + tmp[5] * src[15];
        dst[9] = tmp[1] * src[12] + tmp[6] * src[14] + tmp[9] * src[15];
        dst[9] -= tmp[0] * src[12] + tmp[7] * src[14] + tmp[8] * src[15];
        dst[10] = tmp[2] * src[12] + tmp[7] * src[13] + tmp[10] * src[15];
        dst[10] -= tmp[3] * src[12] + tmp[6] * src[13] + tmp[11] * src[15];
        dst[11] = tmp[5] * src[12] + tmp[8] * src[13] + tmp[11] * src[14];
        dst[11] -= tmp[4] * src[12] + tmp[9] * src[13] + tmp[10] * src[14];
        dst[12] = tmp[2] * src[10] + tmp[5] * src[11] + tmp[1] * src[9];
        dst[12] -= tmp[4] * src[11] + tmp[0] * src[9] + tmp[3] * src[10];
        dst[13] = tmp[8] * src[11] + tmp[0] * src[8] + tmp[7] * src[10];
        dst[13] -= tmp[6] * src[10] + tmp[9] * src[11] + tmp[1] * src[8];
        dst[14] = tmp[6] * src[9] + tmp[11] * src[11] + tmp[3] * src[8];
        dst[14] -= tmp[10] * src[11] + tmp[2] * src[8] + tmp[7] * src[9];
        dst[15] = tmp[10] * src[10] + tmp[4] * src[8] + tmp[9] * src[9];
        dst[15] -= tmp[8] * src[9] + tmp[11] * src[10] + tmp[5] * src[8];

        /* calculate determinant */
        det = src[0] * dst[0] + src[1] * dst[1] + src[2] * dst[2] + src[3] * dst[3];

        if (det == 0.0f) {
            throw new RuntimeException("singular matrix is not invertible");
        }

        /* calculate matrix inverse */
        det = 1 / det;

        for (int j = 0; j < 16; j++) {
            dst[j] *= det;
        }

        return ret;
    }

    // Static Random functions
    public static double random() {
        return Random.random();
    }

    public static Vec2 randomVec2() {
        return vec2(Random.random() * 2 - 1, Random.random() * 2 - 1);
    }

    public static Vec2 randomDirection2D() {
        return vec2(Random.random() * 2 - 1, Random.random() * 2 - 1);
    }

    public static Vec3 randomDirection() {
        return vec3(Random.random() * 2 - 1, Random.random() * 2 - 1, Random.random() * 2 - 1);
    }

    public static Color randomColor() {
        return color(Random.random(), Random.random(), Random.random());
    }

    public static Color randomHue(double s, double v) {
        return hsvToRgb(Random.random(), s, v);
    }

    public static Color randomHue() {
        return randomHue(1.0, 1.0);
    }

    public static Color randomGray() {
        return color(random() * 0.4 + 0.3);
    }

    public static void seed(int s) {
        Random.seed(s);
    }
}
