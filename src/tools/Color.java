
package tools;

import static tools.Functions.*;

// See
// https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Record.html
// for details on the record data type.
public record Color(double r, double g, double b) {

    public static final Color black = color(0, 0, 0);
    public static final Color gray = color(0.5, 0.5, 0.5);
    public static final Color white = color(1, 1, 1);
    public static final Color red = color(1, 0, 0);
    public static final Color green = color(0, 1, 0);
    public static final Color blue = color(0, 0, 1);
    public static final Color cyan = color(0, 1, 1);
    public static final Color magenta = color(1, 0, 1);
    public static final Color yellow = color(1, 1, 0);

    @Override
    public String toString() {
        return String.format("(Color: %.2f %.2f %.2f)", r, g, b);
    }
}
