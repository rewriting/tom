package pico;

public class Main {
  public final static void main(String[] args) {
    int criblemax = 0;
    try {
      criblemax = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java pico.Main <criblemax>");
      return;
    }
    // Generate something for metapost
    GomPico gomtest = new GomPico();
    for (int i = 100; i <= (100*criblemax); i+=100) {
      gomtest.run(i);
    }
    System.out.println();
    ApiPico apitest = new ApiPico();
    for (int i = 100; i <= (100*criblemax); i+=100) {
      apitest.run(i);
    }
  }
}
