package tools;

import static tools.Functions.*;

/**
 * Represents a color with red, green, and blue components.
 * This class provides constants for common colors.
 */
public record Color(double r, double g, double b) {

    /** Black color (0, 0, 0). */
    public static final Color black = color(0, 0, 0);
    /** Gray color (0.5, 0.5, 0.5). */
    public static final Color gray = color(0.5, 0.5, 0.5);
    /** White color (1, 1, 1). */
    public static final Color white = color(1, 1, 1);
    /** Red color (1, 0, 0). */
    public static final Color red = color(1, 0, 0);
    /** Green color (0, 1, 0). */
    public static final Color green = color(0, 1, 0);
    /** Blue color (0, 0, 1). */
    public static final Color blue = color(0, 0, 1);
    /** Cyan color (0, 1, 1). */
    public static final Color cyan = color(0, 1, 1);
    /** Magenta color (1, 0, 1). */
    public static final Color magenta = color(1, 0, 1);
    /** Yellow color (1, 1, 0). */
    public static final Color yellow = color(1, 1, 0);

    /**
     * Returns a string representation of the color.
     *
     * @return A string in the format "(Color: r.rr g.gg b.bb)".
     */
    @Override
    public String toString() {
        return String.format("(Color: %.2f %.2f %.2f)", r, g, b);
    }
}