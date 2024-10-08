package tools;

/**
 * Utility class providing various mathematical and vector operations.
 */
public class Functions {

    /** Epsilon value for floating-point comparisons. */
    static public final double EPSILON = 1e-5;

    /**
     * Checks if a value is between two other values, inclusive.
     *
     * @param a The lower bound.
     * @param b The upper bound.
     * @param v The value to check.
     * @return true if v is between a and b (inclusive), false otherwise.
     */
    public static boolean in(double a, double b, double v) {
        return a <= v && v <= b;
    }

    /**
     * Compares two double values for near equality.
     *
     * @param a The first value.
     * @param b The second value.
     * @return true if the absolute difference between a and b is less than EPSILON,
     *         false otherwise.
     */
    public static boolean almostEqual(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * Creates a new Vec2 object.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return A new Vec2 object with the given coordinates.
     */
    public static Vec2 vec2(double x, double y) {
        return new Vec2(x, y);
    }

    /**
     * Calculates the sum of two or more Vec2 objects.
     *
     * @param a  The first vector.
     * @param b  The second vector.
     * @param vs Additional vectors (optional).
     * @return The sum of all input vectors.
     */
    public static Vec2 add(Vec2 a, Vec2 b, Vec2... vs) {
        Vec2 r = vec2(a.x() + b.x(), a.y() + b.y());
        for (var v : vs)
            r = vec2(r.x() + v.x(), r.y() + v.y());
        return r;
    }

    /**
     * Calculates the difference between two or more Vec2 objects.
     *
     * @param a  The first vector.
     * @param b  The second vector.
     * @param vs Additional vectors to subtract (optional).
     * @return The result of subtracting all subsequent vectors from the first.
     */
    public static Vec2 subtract(Vec2 a, Vec2 b, Vec2... vs) {
        Vec2 r = vec2(a.x() - b.x(), a.y() - b.y());
        for (var v : vs)
            r = vec2(r.x() - v.x(), r.y() - v.y());
        return r;
    }

    /**
     * Performs linear interpolation between two Vec2 objects.
     *
     * @param a The starting vector.
     * @param b The ending vector.
     * @param t The interpolation parameter (0.0 to 1.0).
     * @return The interpolated vector.
     */
    public static Vec2 interplolate(Vec2 a, Vec2 b, double t) {
        return add(multiply(a, 1 - t), multiply(b, t));
    }

    /**
     * Performs barycentric interpolation between three Vec2 objects.
     *
     * @param a   The first vector.
     * @param b   The second vector.
     * @param c   The third vector.
     * @param uvw The barycentric coordinates.
     * @return The interpolated vector.
     */
    public static Vec2 interplolate(Vec2 a, Vec2 b, Vec2 c, Vec3 uvw) {
        return add(multiply(a, uvw.u()), multiply(b, uvw.v()), multiply(c, uvw.w()));
    }

    /**
     * Multiplies a Vec2 by a scalar value.
     *
     * @param s The scalar value.
     * @param a The vector to multiply.
     * @return The resulting scaled vector.
     */
    public static Vec2 multiply(double s, Vec2 a) {
        return vec2(s * a.x(), s * a.y());
    }

    /**
     * Multiplies a Vec2 by a scalar value.
     *
     * @param a The vector to multiply.
     * @param s The scalar value.
     * @return The resulting scaled vector.
     */
    public static Vec2 multiply(Vec2 a, double s) {
        return vec2(s * a.x(), s * a.y());
    }

    /**
     * Multiplies two Vec2 objects component-wise.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The resulting vector with component-wise multiplication.
     */
    public static Vec2 multiply(Vec2 a, Vec2 b) {
        return vec2(a.x() * b.x(), a.y() * b.y());
    }

    /**
     * Negates a Vec2 object.
     *
     * @param a The vector to negate.
     * @return The negated vector.
     */
    public static Vec2 negate(Vec2 a) {
        return vec2(-a.x(), -a.y());
    }

    /**
     * Divides a Vec2 by a scalar value.
     *
     * @param a The vector to divide.
     * @param s The scalar value.
     * @return The resulting divided vector.
     */
    public static Vec2 divide(Vec2 a, double s) {
        return vec2(a.x() / s, a.y() / s);
    }

    /**
     * Calculates the dot product of two Vec2 objects.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The dot product of the two vectors.
     */
    public static double dot(Vec2 a, Vec2 b) {
        return a.x() * b.x() + a.y() * b.y();
    }

    /**
     * Calculates the "perpendicular cross" product of two Vec2 objects.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The magnitude of the cross product (signed area of the
     *         parallelogram).
     */
    public static double cross(Vec2 a, Vec2 b) {
        return a.x() * b.y() - a.y() * b.x();
    }

    /**
     * Calculates the length of a Vec2.
     *
     * @param a The vector.
     * @return The length of the vector.
     */
    public static double length(Vec2 a) {
        return Math.sqrt(a.x() * a.x() + a.y() * a.y());
    }

    /**
     * Calculates the squared length of a Vec2.
     *
     * @param a The vector.
     * @return The squared length of the vector.
     */
    public static double squaredLength(Vec2 a) {
        return a.x() * a.x() + a.y() * a.y();
    }

    /**
     * Normalizes a Vec2 to unit length.
     *
     * @param a The vector to normalize.
     * @return The normalized vector.
     */
    public static Vec2 normalize(Vec2 a) {
        return divide(a, length(a));
    }

    /**
     * Calculates the component-wise modulus of two Vec2 objects.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The resulting vector after component-wise modulus operation.
     */
    public static Vec2 mod(Vec2 a, Vec2 b) {
        return vec2(a.x() % b.x(), a.y() % b.y());
    }

    /**
     * Calculates the component-wise minimum of two Vec2 objects.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return A new Vec2 with the minimum values of each component.
     */
    public static Vec2 min(Vec2 a, Vec2 b) {
        return new Vec2(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()));
    }

    /**
     * Calculates the component-wise maximum of two Vec2 objects.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return A new Vec2 with the maximum values of each component.
     */
    public static Vec2 max(Vec2 a, Vec2 b) {
        return new Vec2(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()));
    }

    /**
     * Creates a new Vec3 object.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     * @return A new Vec3 object with the given coordinates.
     */
    public static Vec3 vec3(double x, double y, double z) {
        return new Vec3(x, y, z);
    }

    /**
     * Creates a new Vec3 object with all components set to the same value.
     *
     * @param x The value for all components.
     * @return A new Vec3 object with all components set to x.
     */
    public static Vec3 vec3(double x) {
        return new Vec3(x, x, x);
    }

    /**
     * Creates a new Vec3 object from a Vec2 object, setting z to 0.
     *
     * @param v The Vec2 object.
     * @return A new Vec3 object with x and y from the Vec2 and z set to 0.
     */
    public static Vec3 vec3(Vec2 v) {
        return new Vec3(v.x(), v.y(), 0);
    }

    /**
     * Creates a new Vec3 object from a Vec2 object and a z-coordinate.
     *
     * @param v The Vec2 object.
     * @param z The z-coordinate.
     * @return A new Vec3 object with x and y from the Vec2 and the given z.
     */
    public static Vec3 vec3(Vec2 v, double z) {
        return new Vec3(v.x(), v.y(), z);
    }

    /**
     * Creates a new Vec3 object from a Color object.
     *
     * @param c The Color object.
     * @return A new Vec3 object with r, g, and b components from the Color.
     */
    public static Vec3 vec3(Color c) {
        return new Vec3(c.r(), c.g(), c.b());
    }

    /**
     * Calculates the sum of two or more Vec3 objects.
     *
     * @param a  The first vector.
     * @param b  The second vector.
     * @param vs Additional vectors (optional).
     * @return The sum of all input vectors.
     */
    public static Vec3 add(Vec3 a, Vec3 b, Vec3... vs) {
        Vec3 r = vec3(a.x() + b.x(), a.y() + b.y(), a.z() + b.z());
        for (var v : vs)
            r = vec3(r.x() + v.x(), r.y() + v.y(), r.z() + v.z());
        return r;
    }

    /**
     * Calculates the difference between two or more Vec3 objects.
     *
     * @param a  The first vector.
     * @param b  The second vector.
     * @param vs Additional vectors to subtract (optional).
     * @return The result of subtracting all subsequent vectors from the first.
     */
    public static Vec3 subtract(Vec3 a, Vec3 b, Vec3... vs) {
        Vec3 r = vec3(a.x() - b.x(), a.y() - b.y(), a.z() - b.z());
        for (var v : vs)
            r = vec3(r.x() - v.x(), r.y() - v.y(), r.z() - v.z());
        return r;
    }

    /**
     * Multiplies a Vec3 by a scalar value.
     *
     * @param s The scalar value.
     * @param a The vector to multiply.
     * @return The resulting scaled vector.
     */
    public static Vec3 multiply(double s, Vec3 a) {
        return vec3(s * a.x(), s * a.y(), s * a.z());
    }

    /**
     * Multiplies a Vec3 by a scalar value.
     *
     * @param a The vector to multiply.
     * @param s The scalar value.
     * @return The resulting scaled vector.
     */
    public static Vec3 multiply(Vec3 a, double s) {
        return vec3(s * a.x(), s * a.y(), s * a.z());
    }

    /**
     * Multiplies two Vec3 objects component-wise.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The resulting vector with component-wise multiplication.
     */
    public static Vec3 multiply(Vec3 a, Vec3 b) {
        return vec3(a.x() * b.x(), a.y() * b.y(), a.z() * b.z());
    }

    /**
     * Performs linear interpolation between two Vec3 objects.
     *
     * @param a The starting vector.
     * @param b The ending vector.
     * @param t The interpolation parameter (0.0 to 1.0).
     * @return The interpolated vector.
     */
    public static Vec3 interplolate(Vec3 a, Vec3 b, double t) {
        return add(multiply(a, 1 - t), multiply(b, t));
    }

    /**
     * Performs barycentric interpolation between three Vec3 objects.
     *
     * @param a   The first vector.
     * @param b   The second vector.
     * @param c   The third vector.
     * @param uvw The barycentric coordinates.
     * @return The interpolated vector.
     */
    public static Vec3 interplolate(Vec3 a, Vec3 b, Vec3 c, Vec3 uvw) {
        return add(multiply(a, uvw.u()), multiply(b, uvw.v()), multiply(c, uvw.w()));
    }

    /**
     * Negates a Vec3 object.
     *
     * @param a The vector to negate.
     * @return The negated vector.
     */
    public static Vec3 negate(Vec3 a) {
        return vec3(-a.x(), -a.y(), -a.z());
    }

    /**
     * Divides a Vec3 by a scalar value.
     *
     * @param a The vector to divide.
     * @param s The scalar value.
     * @return The resulting divided vector.
     */
    public static Vec3 divide(Vec3 a, double s) {
        return vec3(a.x() / s, a.y() / s, a.z() / s);
    }

    /**
     * Calculates the dot product of two Vec3 objects.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The dot product of the two vectors.
     */
    public static double dot(Vec3 a, Vec3 b) {
        return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
    }

    /**
     * Calculates the cross product of two Vec3 objects.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The cross product of the two vectors.
     */
    public static Vec3 cross(Vec3 a, Vec3 b) {
        return vec3(a.y() * b.z() - a.z() * b.y(), a.z() * b.x() - a.x() * b.z(),
                a.x() * b.y() - a.y() * b.x());
    }

    /**
     * Calculates the length of a Vec3.
     *
     * @param a The vector.
     * @return The length of the vector.
     */
    public static double length(Vec3 a) {
        return Math.sqrt(a.x() * a.x() + a.y() * a.y() + a.z() * a.z());
    }

    /**
     * Calculates the squared length of a Vec3.
     *
     * @param a The vector.
     * @return The squared length of the vector.
     */
    public static double squaredLength(Vec3 a) {
        return a.x() * a.x() + a.y() * a.y() + a.z() * a.z();
    }

    /**
     * Calculates the sum of all components of a Vec3.
     *
     * @param v The vector.
     * @return The sum of x, y, and z components.
     */
    public static double coordSum(Vec3 v) {
        return v.x() + v.y() + v.z();
    }

    /**
     * Checks if all components of a Vec3 are within a specified range.
     *
     * @param a The lower bound of the range.
     * @param b The upper bound of the range.
     * @param v The vector to check.
     * @return true if all components of v are between a and b (inclusive), false
     *         otherwise.
     */
    public static boolean in(double a, double b, Vec3 v) {
        return in(a, b, v.x()) && in(a, b, v.y()) && in(a, b, v.w());
    }

    /**
     * Normalizes a Vec3 to unit length.
     *
     * @param a The vector to normalize.
     * @return The normalized vector.
     */
    public static Vec3 normalize(Vec3 a) {
        return divide(a, length(a));
    }

    /**
     * Calculates the component-wise modulus of two Vec3 objects.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The resulting vector after component-wise modulus operation.
     */
    public static Vec3 mod(Vec3 a, Vec3 b) {
        return vec3(a.x() % b.x(), a.y() % b.y(), a.z() % b.z());
    }

    /**
     * Calculates the component-wise minimum of two Vec3 objects.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return A new Vec3 with the minimum values of each component.
     */
    public static Vec3 min(Vec3 a, Vec3 b) {
        return new Vec3(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()),
                Math.min(a.z(), b.z()));
    }

    /**
     * Calculates the component-wise maximum of two Vec3 objects.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return A new Vec3 with the maximum values of each component.
     */
    public static Vec3 max(Vec3 a, Vec3 b) {
        return new Vec3(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()),
                Math.max(a.z(), b.z()));
    }

    /**
     * Creates a new Color object.
     *
     * @param r The red component (0.0 to 1.0).
     * @param g The green component (0.0 to 1.0).
     * @param b The blue component (0.0 to 1.0).
     * @return A new Color object with the given RGB values.
     */
    public static Color color(double r, double g, double b) {
        return new Color(r, g, b);
    }

    /**
     * Creates a new grayscale Color object.
     *
     * @param i The intensity for all components (0.0 to 1.0).
     * @return A new Color object with all components set to the given intensity.
     */
    public static Color color(double i) {
        return color(i, i, i);
    }

    /**
     * Creates a new Color object from a Vec2, setting blue to 0.
     *
     * @param v The Vec2 object representing red and green components.
     * @return A new Color object with red and green from the Vec2 and blue set to
     *         0.
     */
    public static Color color(Vec2 v) {
        return color(v.x(), v.y(), 0);
    }

    /**
     * Creates a new Color object from a Vec3.
     *
     * @param v The Vec3 object representing RGB components.
     * @return A new Color object with RGB values from the Vec3.
     */
    public static Color color(Vec3 v) {
        return color(v.x(), v.y(), v.z());
    }

    /**
     * Adds two or more Color objects.
     *
     * @param a  The first color.
     * @param vs Additional colors to add (varargs).
     * @return The resulting Color after adding all components.
     */
    public static Color add(Color a, Color... vs) {
        for (Color v : vs) {
            a = color(a.r() + v.r(), a.g() + v.g(), a.b() + v.b());
        }
        return a;
    }

    /**
     * Subtracts two or more Color objects from the first one.
     *
     * @param a  The base color.
     * @param b  The first color to subtract.
     * @param vs Additional colors to subtract (varargs).
     * @return The resulting Color after subtracting all subsequent colors from the
     *         first.
     */
    public static Color subtract(Color a, Color b, Color... vs) {
        Color r = color(a.r() - b.r(), a.g() - b.g(), a.b() - b.b());
        for (Color v : vs) {
            r = color(r.r() - v.r(), r.g() - v.g(), r.b() - v.b());
        }
        return r;
    }

    /**
     * Multiplies a Color by a scalar value.
     *
     * @param s The scalar value.
     * @param a The color to multiply.
     * @return The resulting scaled color.
     */
    public static Color multiply(double s, Color a) {
        return color(s * a.r(), s * a.g(), s * a.b());
    }

    /**
     * Multiplies a Color by a scalar value.
     *
     * @param a The color to multiply.
     * @param s The scalar value.
     * @return The resulting scaled color.
     */
    public static Color multiply(Color a, double s) {
        return multiply(s, a);
    }

    /**
     * Multiplies two Color objects component-wise.
     *
     * @param a The first color.
     * @param b The second color.
     * @return The resulting color after component-wise multiplication.
     */
    public static Color multiply(Color a, Color b) {
        return color(a.r() * b.r(), a.g() * b.g(), a.b() * b.b());
    }

    /**
     * Divides a Color by a scalar value.
     *
     * @param a The color to divide.
     * @param s The scalar value.
     * @return The resulting divided color.
     */
    public static Color divide(Color a, double s) {
        return color(a.r() / s, a.g() / s, a.b() / s);
    }

    /**
     * Clamps the components of a Color to the range [0, 1].
     *
     * @param v The color to clamp.
     * @return A new Color with all components clamped between 0 and 1.
     */
    public static Color clamp(Color v) {
        return color(Math.min(1, Math.max(v.r(), 0)),
                Math.min(1, Math.max(v.g(), 0)),
                Math.min(1, Math.max(v.b(), 0)));
    }

    /**
     * Converts HSV (Hue, Saturation, Value) to RGB color.
     *
     * @param h Hue component (0.0 to 1.0).
     * @param s Saturation component (0.0 to 1.0).
     * @param v Value component (0.0 to 1.0).
     * @return The resulting RGB Color.
     */
    public static Color hsvToRgb(double h, double s, double v) {
        return multiply(v, add(multiply(s, subtract(hue(h), Color.white)), Color.white));
    }

    /**
     * Converts a Color object interpreted as HSV to RGB.
     *
     * @param c The Color object interpreted as HSV (h=r, s=g, v=b).
     * @return The resulting RGB Color.
     */
    public static Color hsvToRgb(Color c) {
        return hsvToRgb(c.r(), c.g(), c.b());
    }

    /**
     * Performs linear interpolation between two Color objects.
     *
     * @param a The starting color.
     * @param b The ending color.
     * @param t The interpolation parameter (0.0 to 1.0).
     * @return The interpolated color.
     */
    public static Color interpolate(Color a, Color b, double t) {
        return color(a.r() * (1 - t) + b.r() * t, a.g() * (1 - t) + b.g() * t, a.b() * (1 - t) + b.b() * t);
    }

    /**
     * Performs barycentric interpolation between three Color objects.
     *
     * @param a   The first color.
     * @param b   The second color.
     * @param c   The third color.
     * @param uvw The barycentric coordinates.
     * @return The interpolated color.
     */
    public static Color interpolate(Color a, Color b, Color c, Vec3 uvw) {
        return add(multiply(a, uvw.u()), multiply(b, uvw.v()), multiply(c, uvw.w()));
    }

    // ... (previous methods remain unchanged)

    /**
     * Creates a Mat44 (4x4 matrix) from three Vec3 objects representing the basis
     * vectors.
     *
     * @param b0 The first basis vector.
     * @param b1 The second basis vector.
     * @param b2 The third basis vector.
     * @return A new Mat44 object with the given basis vectors.
     */
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

    /**
     * Creates a Mat44 (4x4 matrix) from four Vec3 objects representing the basis
     * vectors and translation.
     *
     * @param b0 The first basis vector.
     * @param b1 The second basis vector.
     * @param b2 The third basis vector.
     * @param b3 The translation vector.
     * @return A new Mat44 object with the given basis vectors and translation.
     */
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

    /**
     * Returns the identity matrix.
     *
     * @return The 4x4 identity matrix.
     */
    public static Mat44 identity() {
        return Mat44.identity;
    }

    /**
     * Creates a translation matrix.
     *
     * @param t The translation vector.
     * @return A new Mat44 representing the translation.
     */
    public static Mat44 move(Vec3 t) {
        Mat44 m = new Mat44();
        m.set(3, 0, t.x());
        m.set(3, 1, t.y());
        m.set(3, 2, t.z());
        return m;
    }

    /**
     * Creates a translation matrix.
     *
     * @param x The x-component of the translation.
     * @param y The y-component of the translation.
     * @param z The z-component of the translation.
     * @return A new Mat44 representing the translation.
     */
    public static Mat44 move(double x, double y, double z) {
        Mat44 m = new Mat44();
        m.set(3, 0, x);
        m.set(3, 1, y);
        m.set(3, 2, z);
        return m;
    }

    /**
     * Creates a rotation matrix around an arbitrary axis.
     *
     * @param axis  The axis of rotation.
     * @param angle The angle of rotation in degrees.
     * @return A new Mat44 representing the rotation.
     */
    public static Mat44 rotate(Vec3 axis, double angle) {
        final Mat44 m = new Mat44();
        final double rad = Math.toRadians(angle);
        final double cosa = Math.cos(rad);
        final double sina = Math.sin(rad);
        final double l = Math.sqrt(axis.x() * axis.x() + axis.y() * axis.y() + axis.z() * axis.z());
        final double rx = axis.x() / l;
        final double ry = axis.y() / l;
        final double rz = axis.z() / l;
        final double icosa = 1 - cosa;

        m.set(0, 0, icosa * rx * rx + cosa);
        m.set(0, 1, icosa * rx * ry + rz * sina);
        m.set(0, 2, icosa * rx * rz - ry * sina);

        m.set(1, 0, icosa * rx * ry - rz * sina);
        m.set(1, 1, icosa * ry * ry + cosa);
        m.set(1, 2, icosa * ry * rz + rx * sina);

        m.set(2, 0, icosa * rx * rz + ry * sina);
        m.set(2, 1, icosa * ry * rz - rx * sina);
        m.set(2, 2, icosa * rz * rz + cosa);
        return m;
    }

    /**
     * Creates a rotation matrix around an arbitrary axis defined by its components.
     *
     * @param ax    The x-component of the rotation axis.
     * @param ay    The y-component of the rotation axis.
     * @param az    The z-component of the rotation axis.
     * @param angle The angle of rotation in degrees.
     * @return A new Mat44 representing the rotation.
     */
    public static Mat44 rotate(double ax, double ay, double az, double angle) {
        return rotate(vec3(ax, ay, az), angle);
    }

    /**
     * Creates a scaling matrix.
     *
     * @param x The x-scale factor.
     * @param y The y-scale factor.
     * @param z The z-scale factor.
     * @return A new Mat44 representing the scaling transformation.
     */
    public static Mat44 scale(double x, double y, double z) {
        Mat44 m = new Mat44();
        m.set(0, 0, x);
        m.set(1, 1, y);
        m.set(2, 2, z);
        return m;
    }

    /**
     * Creates a scaling matrix.
     *
     * @param d The Vec3 containing scale factors for x, y, and z.
     * @return A new Mat44 representing the scaling transformation.
     */
    public static Mat44 scale(Vec3 d) {
        Mat44 m = new Mat44();
        m.set(0, 0, d.x());
        m.set(1, 1, d.y());
        m.set(2, 2, d.z());
        return m;
    }

    /**
     * Creates a perspective projection matrix.
     *
     * @param fovy The field of view angle in the y direction, in degrees.
     * @param a    The aspect ratio (width / height).
     * @param n    The distance to the near clipping plane.
     * @param f    The distance to the far clipping plane.
     * @return A new Mat44 representing the perspective projection.
     */
    public static Mat44 perspective(double fovy, double a, double n, double f) {
        final double fov = (fovy / 181.0) * Math.PI;
        final double d = 1 / Math.tan(fov / 2);
        Mat44 m = new Mat44();
        m.set(0, 0, d / a);
        m.set(1, 1, d);
        m.set(2, 2, (n + f) / (n - f));
        m.set(3, 2, (2 * n * f) / (n - f));
        m.set(2, 3, -1);
        m.set(3, 3, 0);
        return m;
    }

    /**
     * Creates a viewport transformation matrix.
     *
     * @param sx The x-coordinate of the viewport's lower-left corner.
     * @param sy The y-coordinate of the viewport's lower-left corner.
     * @param w  The width of the viewport.
     * @param h  The height of the viewport.
     * @param n  The near depth range.
     * @param f  The far depth range.
     * @return A new Mat44 representing the viewport transformation.
     */
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

    /**
     * Multiplies two or more Mat44 matrices.
     *
     * @param a  The first matrix.
     * @param b  The second matrix.
     * @param ms Additional matrices (varargs).
     * @return The result of multiplying all matrices in order.
     */
    public static Mat44 multiply(Mat44 a, Mat44 b, Mat44... ms) {
        Mat44 r = a.multiply(b);
        for (Mat44 m : ms)
            r = r.multiply(m);
        return r;
    }

    /**
     * Multiplies a Mat44 matrix with a Vec3 point, treating the Vec3 as a point
     * (w=1).
     *
     * @param m The matrix.
     * @param p The point.
     * @return The transformed point.
     */
    public static Vec3 multiplyPoint(Mat44 m, Vec3 p) {
        final double x = m.get(0, 0) * p.x() + m.get(1, 0) * p.y() + m.get(2, 0) * p.z() + m.get(3, 0);
        final double y = m.get(0, 1) * p.x() + m.get(1, 1) * p.y() + m.get(2, 1) * p.z() + m.get(3, 1);
        final double z = m.get(0, 2) * p.x() + m.get(1, 2) * p.y() + m.get(2, 2) * p.z() + m.get(3, 2);
        return vec3(x, y, z);
    }

    /**
     * Multiplies a Mat44 matrix with a Vec3 direction, treating the Vec3 as a
     * direction (w=0).
     *
     * @param m The matrix.
     * @param d The direction.
     * @return The transformed direction.
     */
    public static Vec3 multiplyDirection(Mat44 m, Vec3 d) {
        double x = m.get(0, 0) * d.x() + m.get(1, 0) * d.y() + m.get(2, 0) * d.z();
        double y = m.get(0, 1) * d.x() + m.get(1, 1) * d.y() + m.get(2, 1) * d.z();
        double z = m.get(0, 2) * d.x() + m.get(1, 2) * d.y() + m.get(2, 2) * d.z();
        return vec3(x, y, z);
    }

    /**
     * Transposes a Mat44 matrix.
     *
     * @param m The matrix to transpose.
     * @return The transposed matrix.
     */
    public static Mat44 transpose(Mat44 m) {
        Mat44 n = new Mat44();
        for (int c = 0; c != 4; c++) {
            for (int r = 0; r != 4; r++) {
                n.set(c, r, m.get(r, c));
            }
        }
        return n;
    }

    /**
     * Inverts a Mat44 matrix.
     *
     * @param m The matrix to invert.
     * @return The inverted matrix.
     * @throws RuntimeException if the matrix is singular and not invertible.
     */
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

    /**
     * Generates a random double between 0 and 1.
     *
     * @return A random double between 0 and 1.
     */
    public static double random() {
        return Random.random();
    }

    /**
     * Generates a random Vec2 with components between -1 and 1.
     *
     * @return A random Vec2.
     */
    public static Vec2 randomVec2() {
        return vec2(Random.random() * 2 - 1, Random.random() * 2 - 1);
    }

    /**
     * Generates a random 2D direction vector.
     *
     * @return A random Vec2 representing a direction.
     */
    public static Vec2 randomDirection2D() {
        return vec2(Random.random() * 2 - 1, Random.random() * 2 - 1);
    }

    /**
     * Generates a random 3D direction vector.
     *
     * @return A random Vec3 representing a direction.
     */
    public static Vec3 randomDirection() {
        return vec3(Random.random() * 2 - 1, Random.random() * 2 - 1, Random.random() * 2 - 1);
    }

    /**
     * Generates a random Color with components between 0 and 1.
     *
     * @return A random Color.
     */
    public static Color randomColor() {
        return color(Random.random(), Random.random(), Random.random());
    }

    /**
     * Generates a random color with specified saturation and value (brightness).
     *
     * @param s The saturation component (0.0 to 1.0).
     * @param v The value (brightness) component (0.0 to 1.0).
     * @return A random Color with the specified saturation and value.
     */
    public static Color randomHue(double s, double v) {
        return hsvToRgb(Random.random(), s, v);
    }

    /**
     * Generates a random fully saturated and bright color.
     *
     * @return A random Color with full saturation and brightness.
     */
    public static Color randomHue() {
        return randomHue(1.0, 1.0);
    }

    /**
     * Generates a random gray color.
     *
     * @return A random Color in the gray scale.
     */
    public static Color randomGray() {
        return color(random() * 0.4 + 0.3);
    }

    /**
     * Sets the seed for the random number generator.
     *
     * @param s The seed value.
     */
    public static void seed(int s) {
        Random.seed(s);
    }

    /**
     * Helper method for HSV to RGB conversion.
     * Calculates the RGB color for a given hue value.
     *
     * @param h The hue value (0.0 to 1.0).
     * @return A Color object representing the RGB values for the given hue.
     */
    private static Color hue(double h) {
        double r = Math.abs(h * 6 - 3) - 1;
        double g = 2 - Math.abs(h * 6 - 2);
        double b = 2 - Math.abs(h * 6 - 4);
        return clamp(color(r, g, b));
    }
}
