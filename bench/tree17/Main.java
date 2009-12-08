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
    ApiTree17 apitest = new ApiTree17();
    ATermTree17 atermtest = new ATermTree17();
    for (int i=1; i <= max; i++) {
      gomtest.run_evalsym17(i);
    }
    System.out.println();
    for (int i=1; i <= max; i++) {
      apitest.run_evalsym17(i);
    }
    System.out.println();
    for (int i=1; i <= max; i++) {
      atermtest.run_evalsym17(i);
    }
    System.out.println();

    for (int i=1; i <= max; i++) {
      gomtest.run_evalexp17(i);
    }
    System.out.println();
    for (int i=1; i <= max; i++) {
      apitest.run_evalexp17(i);
    }
    System.out.println();
    for (int i=1; i <= max; i++) {
      atermtest.run_evalexp17(i);
    }
    System.out.println();

    for (int i=1; i <= max; i++) {
      gomtest.run_evaltree17(i);
    }
    System.out.println();
    for (int i=1; i <= max; i++) {
      apitest.run_evaltree17(i);
    }
    System.out.println();
    for (int i=1; i <= max; i++) {
      atermtest.run_evaltree17(i);
    }
  }
}
