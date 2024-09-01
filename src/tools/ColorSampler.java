package tools;

public record ColorSampler(Color color) implements Sampler {

  @Override
  public Color getColor(Vec2 at) {
    return color;
  }
}
