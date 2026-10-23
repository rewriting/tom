package smoke;

// Tom+Java twin of ../ac_matching.t: compiled by legacy/, its output is
// ../expected.txt (TestJavaTwins).

import smoke.twin.term.types.*;
import java.util.*;

public class Twin {
  %include{ int.tom }
  %include{ intarray.tom }
  %gom {
    module Term
      imports
      abstract syntax
      T = a() | b() | c() | f(T*)
      f:AC{}
  }

  public static void main(String[] args) {
    // the AC helpers Tom generates are instance methods
    new Twin().run();
  }

  public void run() {
    System.out.println((`f(a(),b(),c()) == `f(c(),a(),b())) + " " + (`f(a(),f(b(),c())) == `f(b(),c(),a())));

    System.out.println((`f(c(),b(),a(),c(),b()) == `f(a(),b(),b(),c(),c())) + " " + (`f(f(c(),a()),f(b(),c(),f(a()))) == `f(a(),a(),b(),c(),c())) + " " + `f(c(),b(),a(),c(),b()).length());
    T t1 = `f(a(),b(),c());
    List<String> sols = new ArrayList<String>();
    %match(t1) {
      f??(X*,a(),Y*) -> {
        sols.add((`X == `f()) + " " + (`X == `b()) + " " + (`X == `c()) + " " + (`X == `f(b(),c())) + " " + (`Y == `f(b(),c())) + " " + (`Y == `f()));
      }
    }
    Collections.sort(sols);
    System.out.println(sols.size() + " [" + String.join(" ", sols) + "]");

    T t2 = `f(a(),b(),a());
    List<String> sols2 = new ArrayList<String>();
    %match(t2) {
      f??(X*,X*,Y*) -> {
        sols2.add("X=" + canon(`X) + " Y=" + canon(`Y));
      }
    }
    Collections.sort(sols2);
    System.out.println(sols2.size() + " [" + String.join(" ", sols2) + "]");
  }

  // canon prints t with the elements of an f list sorted: the order of
  // the elements of an AC term depends on the hash values.
  static String canon(T t) {
    if (!t.isConsf() && !t.isEmptyf()) {
      return t.toString();
    }
    List<String> elems = new ArrayList<String>();
    for (; t.isConsf(); t = t.getTailf()) {
      elems.add(t.getHeadf().toString());
    }
    Collections.sort(elems);
    return "f(" + String.join(",", elems) + ")";
  }
}
