package tools;

public interface Image {

    public void setPixel(int x, int y, Color color);

    public Color getPixel(int x, int y);

    public int width();

    public int height();
}
