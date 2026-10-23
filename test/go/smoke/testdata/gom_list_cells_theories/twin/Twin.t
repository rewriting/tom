package smoke;

// Tom+Java twin of ../gom_list_cells_theories.t: compiled by legacy/,
// its output is ../expected.txt (TestJavaTwins).

import smoke.twin.th.types.*;
import java.util.*;

public class Twin {
  %gom {
    module th
    abstract syntax
    T = a() | b() | c() | d()
      | fl(T*)
      | au(T*)
      | nau(T*)
      | ac(T*)
      | acu(T*)
    au:AU() {}
    nau:AU() { `d() }
    ac:AC() {}
    acu:ACU() {}
  }

  public static void main(String[] args) {
    T f = `fl(a(), fl(b(), c()), fl());
    System.out.println("FL " + f + " " + f.length() + " " + (f.getTailfl() == `fl(b(), c())) + " " + (f.getTailfl().getTailfl().getTailfl() == `fl()) + " " + f.reverse());

    T u = `au(a(), au(b(), c()), au());
    System.out.println("AU " + u + " " + u.length() + " " + (u.getTailau().getTailau() == `c()) + " " + ((smoke.twin.th.types.t.au)u).toArray().length + " " + u.reverse() + " " + ((smoke.twin.th.types.t.au)u).append(`d()));
    System.out.println("AU small " + `au(a()) + " " + `au() + " " + `au(a(), b()).getTailau() + " " + `au(a(), b()).length());
    System.out.println("AU neutral " + `nau(a(), d(), b()) + " " + `nau(d()) + " " + `nau() + " " + `nau(d(), d()) + " " + `nau(a(), nau(b(), d())));

    T x = `ac(b(), ac(a(), c()), a(), ac());
    System.out.println("AC " + (x == `ac(a(), a(), b(), c())) + " " + (x == `ac(c(), ac(b(), a(), a()))) + " " + x.length() + " " + canon(x) + " " + sorted(x) + " " + (`ac(a()) == `a()));
    T y = `acu(c(), acu(a(), b()), acu());
    System.out.println("ACU " + (y == `acu(a(), b(), c())) + " " + y.length() + " " + canon(y) + " " + sorted(y) + " " + (`acu(a()) == `a()) + " " + `acu());
  }

  // canon prints a list with its elements sorted by their text.
  static String canon(T t) {
    List<String> elems = new ArrayList<String>();
    for (Object e : toArray(t)) {
      elems.add(e.toString());
    }
    Collections.sort(elems);
    return t.symbolName() + "[" + String.join(",", elems) + "]";
  }

  // sorted reports whether the elements of an AC/ACU list are in the
  // order of compareTo.
  static boolean sorted(T t) {
    Object[] elems = toArray(t);
    for (int i = 0; i + 1 < elems.length; i++) {
      if (((T)elems[i]).compareTo(elems[i + 1]) > 0) {
        return false;
      }
    }
    return true;
  }

  static Object[] toArray(T t) {
    if (t instanceof smoke.twin.th.types.t.ac) {
      return ((smoke.twin.th.types.t.ac)t).toArray();
    }
    return ((smoke.twin.th.types.t.acu)t).toArray();
  }
}
