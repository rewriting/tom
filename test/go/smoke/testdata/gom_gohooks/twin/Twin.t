package smoke;

// Tom+Java twin of ../gom_gohooks.t: compiled by legacy/, its output is
// ../expected.txt (TestJavaTwins). The hooks are the Java code of the
// Go hooks.

import smoke.twin.jb.types.*;

public class Twin {
  %gom {
    module jb
    abstract syntax
    T = a() | b() | f(lhs:T, rhs:T)
    L = conc(T*)
    f:make(l, r) {
      if (l.isb() && r.isb()) {
        return `a();
      }
    }
    sort T:block() {
      public int size() {
        return isf() ? 1 + getlhs().size() + getrhs().size() : 1;
      }
    }
    sort L:block() {
      public int weight() {
        int w = 0;
        for (L c = this; c.isConsconc(); c = c.getTailconc()) {
          w += c.getHeadconc().size();
        }
        return w;
      }
    }
  }

  public static void main(String[] args) {
    T t = `f(a(), f(b(), a()));
    System.out.println(t + " " + t.size() + " " + `b().size() + " " + `f(b(), b()));
    L l = `conc(t, a());
    System.out.println(l + " " + l.weight() + " " + `conc().weight());
  }
}
