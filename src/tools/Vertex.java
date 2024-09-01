
package tools;

public record Vertex(Vec3 position, Vec3 normal, Vec2 uv, Color color) {

    public Vertex(Vec3 position, Vec3 normal, Vec2 uv) {
        this(position, normal, uv, Color.magenta);
    }

    public String toString() {
        return "(Vertex: " + position + " " + normal + " " + uv + " " + color + ")";
    }
}
