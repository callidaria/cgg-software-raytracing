package tools;

public class StopWatch {
  private long mark;

  public StopWatch() {
    mark = System.currentTimeMillis();
  }

  public void stop(String name) {
    var time = System.currentTimeMillis() - mark;
    System.out.format("%s: time: %.2fs\n", name, time / 1e3);
  }
}
