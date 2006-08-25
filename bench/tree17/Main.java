package tree17;

public class Main {
  public final static void main(String[] args) {
    int max = 0;
    try {
      max = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java tree17.Main <max>");
      return;
    }
    // Generate something for metapost
    GomTree17 gomtest = new GomTree17();
    for (int i = 0; i < max; i++) {
      gomtest.run(i);
    }
  }
}
