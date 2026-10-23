package smoke;

// Tom+Java twin of ../action_else.t: compiled by legacy/, its output is
// ../expected.txt (TestJavaTwins).

public class Twin {
  %include { int.tom }

  static class pair {
    int a, b;
    pair() {}
    pair(int a, int b) { this.a = a; this.b = b; }
    int sum() { return a + b; }
  }

  static int add(pair p) { return p.a + p.b; }

  interface F { int call(); }

  static int f(int i) {
    %match(int i) {
      x -> {
          if (`x == 0) {
            return 10;
          } else if (`x == 1) {
            return `x + 20;
          } else if (`x == 2) {
            return add(new pair(`x, 1)) + new pair().sum() + new F() { public int call() { return 100; } }.call();
          } /* c */ else if (`x == 3) {
            pair[] ps = new pair[] { new pair(`x, 1), new pair(2, 3) };
            pair o = new pair(1, 2);
            int v = new int[] { `x, 1 }[0];
            boolean eq = true;
            if (eq) { return ps[1].b + o.b + v + 200; }
            return 0;
          } else {
            if (i > 5) { return 50; } else { return 30; }
          }
      }
    }
    return 0;
  }

  public static void main(String[] args) { System.out.println(f(0) + " " + f(1) + " " + f(2) + " " + f(3) + " " + f(4) + " " + f(6)); }
}
