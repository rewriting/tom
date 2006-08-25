package fib;

public class Main {
  public final static void main(String[] args) {
    int max = 0;
    try {
      max = Integer.parseInt(args[0]);
    } catch (Exception e) {
      //System.out.println("Usage: java fib.Main <max>");
      //return;
      max = 14;
    }
    // Generate something for metapost
    GomFib gomtest = new GomFib();
    for (int i = 0; i < max; i++) {
      gomtest.run(10+i);
    }
    System.out.println();
    ApiFib apitest = new ApiFib();
    for (int i = 0; i < max; i++) {
      apitest.run(10+i);
    }
    System.out.println();
    ATermFib atermtest = new ATermFib();
    for (int i = 0; i < max; i++) {
      atermtest.run(10+i);
    }
    System.out.println();
    HandFib handtest = new HandFib();
    for (int i = 0; i < max; i++) {
      handtest.run(10+i);
    }
  }
}
