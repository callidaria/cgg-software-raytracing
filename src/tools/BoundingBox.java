//
// Author: Henrik Tramberend <tramberend@beuth-hochschule.de>
//

package tools;

import static tools.Functions.*;

/**
 * Represents a bounding box in 3D space defined by two points: minimum and maximum.
 */
public record BoundingBox(Vec3 min, Vec3 max) {
    /**
     * A bounding box that encompasses everything in the scene.
     */
    public static BoundingBox everything = new BoundingBox(Double.MAX_VALUE);

    /**
     * A bounding box that represents an empty space.
     */
    public static BoundingBox empty = new BoundingBox(-Double.MAX_VALUE);

    /**
     * Constructs a BoundingBox with a given size centered at the origin.
     *
     * @param size The size of the bounding box in each dimension.
     */
    public BoundingBox(double size) {
        this(vec3(-size, -size, -size), vec3(size, size, size));
    }

    /**
     * Constructs a default BoundingBox representing an empty space.
     * This is equivalent to calling the constructor with -Double.MAX_VALUE.
     */
    public BoundingBox() {
        this(-Double.MAX_VALUE);
    }

    /**
     * Creates a BoundingBox that encompasses all given points.
     *
     * @param points An array of Vec3 points to be enclosed by the bounding box.
     * @return A BoundingBox that contains all the given points.
     */
    public static BoundingBox around(Vec3... points) {
        var bounds = BoundingBox.empty;
        for (var p : points) bounds = bounds.extend(p);
        return bounds;
    }

    /**
     * Creates a BoundingBox that encompasses all given bounding boxes.
     *
     * @param boxes An array of BoundingBox objects to be enclosed by the new bounding box.
     * @return A BoundingBox that contains all the given bounding boxes.
     */
    public static BoundingBox around(BoundingBox... boxes) {
        var bounds = BoundingBox.empty;
        for (var box : boxes) bounds = bounds.extend(box);
        return bounds;
    }

    /**
     * Extends the current bounding box to include another bounding box.
     *
     * @param bb The bounding box to include.
     * @return A new BoundingBox that encompasses both the current box and the given box.
     */
    public BoundingBox extend(BoundingBox bb) {
        return new BoundingBox(
            vec3(
                Math.min(min.x(), bb.min.x()),
                Math.min(min.y(), bb.min.y()),
                Math.min(min.z(), bb.min.z())
            ),
            vec3(
                Math.max(max.x(), bb.max.x()),
                Math.max(max.y(), bb.max.y()),
                Math.max(max.z(), bb.max.z())
            )
        );
    }

    /**
     * Extends the current bounding box to include a given point.
     *
     * @param p The point to include in the bounding box.
     * @return A new BoundingBox that encompasses both the current box and the given point.
     */
    public BoundingBox extend(Vec3 p) {
        return new BoundingBox(
            vec3(
                Math.min(min.x(), p.x()),
                Math.min(min.y(), p.y()),
                Math.min(min.z(), p.z())
            ),
            vec3(
                Math.max(max.x(), p.x()),
                Math.max(max.y(), p.y()),
                Math.max(max.z(), p.z())
            )
        );
    }

    /**
     * Splits the bounding box along its longest axis and returns the left half.
     *
     * @return A new BoundingBox representing the left half of the current box.
     */
    public BoundingBox splitLeft() {
        Vec3 size2 = subtract(
            divide(subtract(max, Vec3.zero), 2),
            divide(subtract(min, Vec3.zero), 2)
        );
        if (size2.x() >= size2.y() && size2.x() >= size2.z()) {
            return new BoundingBox(
                min,
                vec3(min.x() + size2.x(), max.y(), max.z())
            );
        } else if (size2.y() >= size2.x() && size2.y() >= size2.z()) {
            return new BoundingBox(
                min,
                vec3(max.x(), min.y() + size2.y(), max.z())
            );
        } else {
            return new BoundingBox(
                min,
                vec3(max.x(), max.y(), min.z() + size2.z())
            );
        }
    }

    /**
     * Splits the bounding box along its longest axis and returns the right half.
     *
     * @return A new BoundingBox representing the right half of the current box.
     */
    public BoundingBox splitRight() {
        Vec3 size2 = subtract(
            divide(subtract(max, Vec3.zero), 2),
            divide(subtract(min, Vec3.zero), 2)
        );
        if (size2.x() >= size2.y() && size2.x() >= size2.z()) {
            return new BoundingBox(
                vec3(min.x() + size2.x(), min.y(), min.z()),
                max
            );
        } else if (size2.y() >= size2.x() && size2.y() >= size2.z()) {
            return new BoundingBox(
                vec3(min.x(), min.y() + size2.y(), min.z()),
                max
            );
        } else {
            return new BoundingBox(
                vec3(min.x(), min.y(), min.z() + size2.z()),
                max
            );
        }
    }

    /**
     * Transforms the bounding box using a 4x4 transformation matrix.
     *
     * This method applies the given transformation to all 8 corners of the bounding box
     * and then creates a new bounding box that encompasses all the transformed points.
     * This approach ensures that the resulting bounding box correctly encloses the
     * transformed object, regardless of rotation, scaling, or translation.
     *
     * The algorithm works as follows:
     * 1. Start with an empty bounding box.
     * 2. Transform each of the 8 corners of the current bounding box using the given matrix.
     * 3. Extend the empty bounding box to include each transformed point.
     * 4. The result is a new bounding box that encompasses all transformed points.
     *
     * @param xform The 4x4 transformation matrix to apply.
     * @return A new BoundingBox that encompasses the transformed original box.
     */
    public BoundingBox transform(Mat44 xform) {
        BoundingBox result = BoundingBox.empty;

        result = result.extend(multiplyPoint(xform, min));
        result = result.extend(
            multiplyPoint(xform, vec3(min.x(), min.y(), max.z()))
        );
        result = result.extend(
            multiplyPoint(xform, vec3(min.x(), max.y(), min.z()))
        );
        result = result.extend(
            multiplyPoint(xform, vec3(min.x(), max.y(), max.z()))
        );
        result = result.extend(
            multiplyPoint(xform, vec3(max.x(), min.y(), min.z()))
        );
        result = result.extend(
            multiplyPoint(xform, vec3(max.x(), min.y(), max.z()))
        );
        result = result.extend(
            multiplyPoint(xform, vec3(max.x(), max.y(), min.z()))
        );
        result = result.extend(multiplyPoint(xform, max));

        return result;
    }

    /**
     * Checks if this bounding box contains a given point.
     *
     * @param v The point to check.
     * @return true if the point is inside or on the boundary of the bounding box, false otherwise.
     */
    public boolean contains(Vec3 v) {
        return (
            min.x() <= v.x() &&
            min.y() <= v.y() &&
            min.z() <= v.z() &&
            max.x() >= v.x() &&
            max.y() >= v.y() &&
            max.z() >= v.z()
        );
    }

    /**
     * Checks if this bounding box contains another bounding box.
     *
     * @param bb The bounding box to check.
     * @return true if the given bounding box is entirely inside or on the boundary of this bounding box, false otherwise.
     */
    public boolean contains(BoundingBox bb) {
        return (
            min.x() <= bb.min.x() &&
            min.y() <= bb.min.y() &&
            min.z() <= bb.min.z() &&
            max.x() >= bb.max.x() &&
            max.y() >= bb.max.y() &&
            max.z() >= bb.max.z()
        );
    }

    /**
     * Checks if a ray intersects with this bounding box.
     *
     * This method is adapted from:
     * https://tavianator.com/cgit/dimension.git/tree/libdimension/bvh/bvh.c
     *
     * @param origin The origin point of the ray.
     * @param direction The direction vector of the ray.
     * @param tMin The minimum distance along the ray to check for intersection.
     * @param tMax The maximum distance along the ray to check for intersection.
     * @return true if the ray intersects the bounding box, false otherwise.
     */
    public boolean intersect(
        Vec3 origin,
        Vec3 direction,
        double tMin,
        double tMax
    ) {
        if (this.equals(everything)) return true;
        if (this.contains(add(origin, multiply(direction, tMin)))) return true;
        if (this.contains(add(origin, multiply(direction, tMax)))) return true;

        double diy = 1.0 / direction.y();
        double diz = 1.0 / direction.z();
        double dix = 1.0 / direction.x();

        double tx1 = (min.x() - origin.x()) * dix;
        double tx2 = (max.x() - origin.x()) * dix;

        double tmin = Math.min(tx1, tx2);
        double tmax = Math.max(tx1, tx2);

        double ty1 = (min.y() - origin.y()) * diy;
        double ty2 = (max.y() - origin.y()) * diy;

        tmin = Math.max(tmin, Math.min(ty1, ty2));
        tmax = Math.min(tmax, Math.max(ty1, ty2));

        double tz1 = (min.z() - origin.z()) * diz;
        double tz2 = (max.z() - origin.z()) * diz;

        tmin = Math.max(tmin, Math.min(tz1, tz2));
        tmax = Math.min(tmax, Math.max(tz1, tz2));

        return (tmax >= tmin && tMin <= tmin && tmin <= tMax);
    }

    /**
     * Calculates the size of the bounding box.
     *
     * @return A Vec3 representing the dimensions of the bounding box.
     */
    public Vec3 size() {
        return subtract(max, min);
    }

    /**
     * Calculates the center point of the bounding box.
     *
     * @return A Vec3 representing the center point of the bounding box.
     */
    public Vec3 center() {
        return interplolate(max, min, 0.5);
    }

    /**
     * Scales the bounding box by a given factor around its center.
     *
     * @param factor The scaling factor to apply.
     * @return A new BoundingBox that is scaled version of the current box.
     */
    public BoundingBox scale(double factor) {
        var c = center();
        return new BoundingBox(
            add(multiply(factor, subtract(min, c)), c),
            add(multiply(factor, subtract(max, c)), c)
        );
    }

    /**
     * Overrides the default hashCode method.
     *
     * @return An integer hash code for this BoundingBox.
     */
    @Override
    public int hashCode() {
        return 31 * min.hashCode() + max.hashCode();
    }

    /**
     * Overrides the default equals method.
     *
     * @param o The object to compare with this BoundingBox.
     * @return true if the object is a BoundingBox with the same min and max values, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BoundingBox)) return false;
        if (o == this) return true;
        BoundingBox v = (BoundingBox) o;
        return min.equals(v.min) && max.equals(v.max);
    }

    /**
     * Overrides the default toString method.
     *
     * @return A string representation of this BoundingBox.
     */
    @Override
    public String toString() {
        return String.format("(BBox: %s %s)", min, max);
    }
}
