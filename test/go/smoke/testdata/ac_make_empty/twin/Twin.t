package smoke;

// Tom+Java twin of ../ac_make_empty.t: compiled by legacy/, its output
// is ../expected.txt (TestJavaTwins).

import smoke.twin.term.types.*;

public class Twin {
  public static int emptyCalls;

  %gom {
    module Term
      abstract syntax
      T = a() | b() | f(T*)
      f:AC{}
      f:make_empty() {
        smoke.Twin.emptyCalls++;
      }
  }

  public static void main(String[] args) {
    T lx = `f(a());
    T ly = `f(b());
    emptyCalls = 0;
    T t1 = `f(b(), lx*);
    T t2 = `f(a(), ly*);
    System.out.println(emptyCalls + " " + (t1 == t2) + " " + t1.length());
  }
}
