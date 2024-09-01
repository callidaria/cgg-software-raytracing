
package tools;

public final class Mat44 {

  public static final Mat44 identity;

  static {
    identity = new Mat44();
  }

  @Override
  public String toString() {
    String s = "";
    for (int r = 0; r < 4; r++) {
      s += String.format("(% .2f % .2f % .2f % .2f)\n", get(0, r), get(1, r), get(2, r), get(3, r));
    }
    return s;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Mat44))
      return false;
    if (o == this)
      return true;
    Mat44 m = (Mat44) o;
    for (int i = 0; i != 16; i++)
      if (values[i] != m.values[i])
        return false;
    return true;
  }

  Mat44() {
    makeIdentity();
  }

  double[] values() {
    return values;
  }

  double get(int c, int r) {
    return values[4 * c + r];
  }

  void set(int c, int r, double v) {
    values[4 * c + r] = v;
  }

  Mat44 makeIdentity() {
    values = new double[16];
    set(0, 0, 1.0f);
    set(1, 1, 1.0f);
    set(2, 2, 1.0f);
    set(3, 3, 1.0f);
    return this;
  }

  Mat44 multiply(Mat44 m) {
    Mat44 n = new Mat44();
    {
      {
        double v = 0;
        v += values[4 * 0 + 0] * m.values[4 * 0 + 0];
        v += values[4 * 1 + 0] * m.values[4 * 0 + 1];
        v += values[4 * 2 + 0] * m.values[4 * 0 + 2];
        v += values[4 * 3 + 0] * m.values[4 * 0 + 3];
        n.values[4 * 0 + 0] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 1] * m.values[4 * 0 + 0];
        v += values[4 * 1 + 1] * m.values[4 * 0 + 1];
        v += values[4 * 2 + 1] * m.values[4 * 0 + 2];
        v += values[4 * 3 + 1] * m.values[4 * 0 + 3];
        n.values[4 * 0 + 1] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 2] * m.values[4 * 0 + 0];
        v += values[4 * 1 + 2] * m.values[4 * 0 + 1];
        v += values[4 * 2 + 2] * m.values[4 * 0 + 2];
        v += values[4 * 3 + 2] * m.values[4 * 0 + 3];
        n.values[4 * 0 + 2] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 3] * m.values[4 * 0 + 0];
        v += values[4 * 1 + 3] * m.values[4 * 0 + 1];
        v += values[4 * 2 + 3] * m.values[4 * 0 + 2];
        v += values[4 * 3 + 3] * m.values[4 * 0 + 3];
        n.values[4 * 0 + 3] = v;
      }
    }
    {
      {
        double v = 0;
        v += values[4 * 0 + 0] * m.values[4 * 1 + 0];
        v += values[4 * 1 + 0] * m.values[4 * 1 + 1];
        v += values[4 * 2 + 0] * m.values[4 * 1 + 2];
        v += values[4 * 3 + 0] * m.values[4 * 1 + 3];
        n.values[4 * 1 + 0] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 1] * m.values[4 * 1 + 0];
        v += values[4 * 1 + 1] * m.values[4 * 1 + 1];
        v += values[4 * 2 + 1] * m.values[4 * 1 + 2];
        v += values[4 * 3 + 1] * m.values[4 * 1 + 3];
        n.values[4 * 1 + 1] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 2] * m.values[4 * 1 + 0];
        v += values[4 * 1 + 2] * m.values[4 * 1 + 1];
        v += values[4 * 2 + 2] * m.values[4 * 1 + 2];
        v += values[4 * 3 + 2] * m.values[4 * 1 + 3];
        n.values[4 * 1 + 2] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 3] * m.values[4 * 1 + 0];
        v += values[4 * 1 + 3] * m.values[4 * 1 + 1];
        v += values[4 * 2 + 3] * m.values[4 * 1 + 2];
        v += values[4 * 3 + 3] * m.values[4 * 1 + 3];
        n.values[4 * 1 + 3] = v;
      }
    }
    {
      {
        double v = 0;
        v += values[4 * 0 + 0] * m.values[4 * 2 + 0];
        v += values[4 * 1 + 0] * m.values[4 * 2 + 1];
        v += values[4 * 2 + 0] * m.values[4 * 2 + 2];
        v += values[4 * 3 + 0] * m.values[4 * 2 + 3];
        n.values[4 * 2 + 0] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 1] * m.values[4 * 2 + 0];
        v += values[4 * 1 + 1] * m.values[4 * 2 + 1];
        v += values[4 * 2 + 1] * m.values[4 * 2 + 2];
        v += values[4 * 3 + 1] * m.values[4 * 2 + 3];
        n.values[4 * 2 + 1] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 2] * m.values[4 * 2 + 0];
        v += values[4 * 1 + 2] * m.values[4 * 2 + 1];
        v += values[4 * 2 + 2] * m.values[4 * 2 + 2];
        v += values[4 * 3 + 2] * m.values[4 * 2 + 3];
        n.values[4 * 2 + 2] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 3] * m.values[4 * 2 + 0];
        v += values[4 * 1 + 3] * m.values[4 * 2 + 1];
        v += values[4 * 2 + 3] * m.values[4 * 2 + 2];
        v += values[4 * 3 + 3] * m.values[4 * 2 + 3];
        n.values[4 * 2 + 3] = v;
      }
    }
    {
      {
        double v = 0;
        v += values[4 * 0 + 0] * m.values[4 * 3 + 0];
        v += values[4 * 1 + 0] * m.values[4 * 3 + 1];
        v += values[4 * 2 + 0] * m.values[4 * 3 + 2];
        v += values[4 * 3 + 0] * m.values[4 * 3 + 3];
        n.values[4 * 3 + 0] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 1] * m.values[4 * 3 + 0];
        v += values[4 * 1 + 1] * m.values[4 * 3 + 1];
        v += values[4 * 2 + 1] * m.values[4 * 3 + 2];
        v += values[4 * 3 + 1] * m.values[4 * 3 + 3];
        n.values[4 * 3 + 1] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 2] * m.values[4 * 3 + 0];
        v += values[4 * 1 + 2] * m.values[4 * 3 + 1];
        v += values[4 * 2 + 2] * m.values[4 * 3 + 2];
        v += values[4 * 3 + 2] * m.values[4 * 3 + 3];
        n.values[4 * 3 + 2] = v;
      }
      {
        double v = 0;
        v += values[4 * 0 + 3] * m.values[4 * 3 + 0];
        v += values[4 * 1 + 3] * m.values[4 * 3 + 1];
        v += values[4 * 2 + 3] * m.values[4 * 3 + 2];
        v += values[4 * 3 + 3] * m.values[4 * 3 + 3];
        n.values[4 * 3 + 3] = v;
      }
    }
    return n;
  }

  private double[] values;
}
