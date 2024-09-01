//
// Author: Henrik Tramberend <tramberend@beuth-hochschule.de>
//

package tools;

import static tools.Functions.*;

public record BoundingBox(Vec3 min, Vec3 max) {

    public static BoundingBox everything = new BoundingBox(Double.MAX_VALUE);
    public static BoundingBox empty = new BoundingBox(-Double.MAX_VALUE);

    public BoundingBox(double size) {
        this(vec3(-size, -size, -size), vec3(size, size, size));
    }

    public BoundingBox() {
        this(-Double.MAX_VALUE);
    }

    public static BoundingBox around(Vec3... points) {
        var bounds = BoundingBox.empty;
        for (var p : points)
            bounds = bounds.extend(p);
        return bounds;
    }

    public static BoundingBox around(BoundingBox... boxes) {
        var bounds = BoundingBox.empty;
        for (var box : boxes)
            bounds = bounds.extend(box);
        return bounds;
    }

    public BoundingBox extend(BoundingBox bb) {
        return new BoundingBox(
                vec3(Math.min(min.x(), bb.min.x()), Math.min(min.y(), bb.min.y()), Math.min(min.z(), bb.min.z())),
                vec3(Math.max(max.x(), bb.max.x()), Math.max(max.y(), bb.max.y()), Math.max(max.z(), bb.max.z())));
    }

    public BoundingBox extend(Vec3 p) {
        return new BoundingBox(vec3(Math.min(min.x(), p.x()), Math.min(min.y(), p.y()), Math.min(min.z(), p.z())),
                vec3(Math.max(max.x(), p.x()), Math.max(max.y(), p.y()), Math.max(max.z(), p.z())));
    }

    public BoundingBox splitLeft() {
        Vec3 size2 = subtract(divide(subtract(max, Vec3.zero), 2), divide(subtract(min, Vec3.zero), 2));
        if (size2.x() >= size2.y() && size2.x() >= size2.z()) {
            return new BoundingBox(min, vec3(min.x() + size2.x(), max.y(), max.z()));
        } else if (size2.y() >= size2.x() && size2.y() >= size2.z()) {
            return new BoundingBox(min, vec3(max.x(), min.y() + size2.y(), max.z()));
        } else {
            return new BoundingBox(min, vec3(max.x(), max.y(), min.z() + size2.z()));
        }
    }

    public BoundingBox splitRight() {
        Vec3 size2 = subtract(divide(subtract(max, Vec3.zero), 2), divide(subtract(min, Vec3.zero), 2));
        if (size2.x() >= size2.y() && size2.x() >= size2.z()) {
            return new BoundingBox(vec3(min.x() + size2.x(), min.y(), min.z()), max);
        } else if (size2.y() >= size2.x() && size2.y() >= size2.z()) {
            return new BoundingBox(vec3(min.x(), min.y() + size2.y(), min.z()), max);
        } else {
            return new BoundingBox(vec3(min.x(), min.y(), min.z() + size2.z()), max);
        }
    }

    public BoundingBox transform(Mat44 xform) {
        BoundingBox result = BoundingBox.empty;

        result = result.extend(multiplyPoint(xform, min));
        result = result.extend(multiplyPoint(xform, vec3(min.x(), min.y(), max.z())));
        result = result.extend(multiplyPoint(xform, vec3(min.x(), max.y(), min.z())));
        result = result.extend(multiplyPoint(xform, vec3(min.x(), max.y(), max.z())));
        result = result.extend(multiplyPoint(xform, vec3(max.x(), min.y(), min.z())));
        result = result.extend(multiplyPoint(xform, vec3(max.x(), min.y(), max.z())));
        result = result.extend(multiplyPoint(xform, vec3(max.x(), max.y(), min.z())));
        result = result.extend(multiplyPoint(xform, max));

        return result;
    }

    public boolean contains(Vec3 v) {
        return min.x() <= v.x() && min.y() <= v.y() && min.z() <= v.z() && max.x() >= v.x() && max.y() >= v.y()
                && max.z() >= v.z();
    }

    public boolean contains(BoundingBox bb) {
        return min.x() <= bb.min.x() && min.y() <= bb.min.y() && min.z() <= bb.min.z() && max.x() >= bb.max.x()
                && max.y() >= bb.max.y()
                && max.z() >= bb.max.z();
    }

    //
    // Adapted from
    // https://tavianator.com/cgit/dimension.git/tree/libdimension/bvh/bvh.c
    //
    public boolean intersect(Vec3 origin, Vec3 direction, double tMin, double tMax) {
        if (this.equals(everything))
            return true;
        if (this.contains(add(origin, multiply(direction, tMin))))
            return true;
        if (this.contains(add(origin, multiply(direction, tMax))))
            return true;

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

    public Vec3 size() {
        return subtract(max, min);
    }

    public Vec3 center() {
        return interplolate(max, min, 0.5);
    }

    public BoundingBox scale(double factor) {
        var c = center();
        return new BoundingBox(add(
                multiply(factor, subtract(min, c)), c),
                add(multiply(factor, subtract(max, c)), c));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BoundingBox))
            return false;
        if (o == this)
            return true;
        BoundingBox v = (BoundingBox) o;
        return min.equals(v.min) && max.equals(v.max);
    }

    @Override
    public String toString() {
        return String.format("(BBox: %s %s)", min, max);
    }
}
