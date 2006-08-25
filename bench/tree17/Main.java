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
    for (int i=1; i <= max; i++) {
      gomtest.run_evalsym17(i);
    }
    System.out.println();
    for (int i=1; i <= max; i++) {
      gomtest.run_evalexp17(i);
    }
    System.out.println();
    for (int i=1; i <= max; i++) {
      gomtest.run_evaltree17(i);
    }
  }
}
