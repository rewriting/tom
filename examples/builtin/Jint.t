package builtin;

public class Jint {

  %include {int.tom}

  public final static void main(String[] args) {
    Jint test = new Jint();
    int res = test.fib(10);

    System.out.println("res = " + res);
  }

  public int fib(int t) {
    %match(int t) {
      0 -> { return 1; }
      1 -> { return 1; }
      n -> { return fib(n-1) + fib(n-2); }
    }
  }

}

