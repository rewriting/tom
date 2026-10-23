package smoke;

// Tom+Java twin of ../gom_javablock.t: compiled by legacy/, its output
// is ../expected.txt (TestJavaTwins). The Gom grammar of the Java
// compiler has no `javablock` (an internal hook kind of Java Gom: a
// block without Tom code); its Java counterpart is a `block` whose body
// has no Tom code.

import smoke.twin.jb.types.*;

public class Twin {
  %gom {
    module jb
    abstract syntax
    T = a() | b() | f(lhs:T, rhs:T)
    L = conc(T*)
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
    System.out.println(t + " " + t.size() + " " + `b().size());
    L l = `conc(t, a());
    System.out.println(l + " " + l.weight() + " " + `conc().weight());
  }
}
