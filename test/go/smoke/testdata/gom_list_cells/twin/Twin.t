package smoke;

// Tom+Java twin of ../gom_list_cells.t: compiled by legacy/, its output
// is ../expected.txt (TestJavaTwins).

import smoke.twin.cells.types.*;

public class Twin {
  %gom {
    module cells
    abstract syntax
    E = a() | b() | c()
    L = conc(E*) | nil()
    M = lm(E*) | m0()
    lm:make_empty() {
      return `m0();
    }
  }

  public static void main(String[] args) {
    // sharing of tails
    L l = `conc(b(), a());
    L m = `conc(a(), l*);
    System.out.println("shared " + (m.getTailconc() == l) + " " + (m == `conc(a(), b(), a())) + " " + (m.getTailconc().getTailconc() == `conc(a())));

    // a tail that is not a list
    L x = `Consconc(a(), nil());
    System.out.println("x " + x + " " + x.length() + " " + (x.getTailconc() == `nil()) + " " + x.isConsconc() + " " + x.isEmptyconc());
    L y = `Consconc(b(), x);
    System.out.println("y " + y + " " + y.length() + " " + y.reverse() + " " + ((smoke.twin.cells.types.l.conc)y).append(`c()) + " " + ((smoke.twin.cells.types.l.conc)y).toArray().length + " " + (y.getTailconc() == x));
    %match(y) {
      Consconc(h, Consconc(h2, t)) -> { System.out.println("match " + `h + " " + `h2 + " " + `t); }
    }

    // the make_empty hook ends the lists with m0()
    M p = `lm(a(), b());
    System.out.println("lm " + p + " " + p.length() + " " + (p.getTaillm().getTaillm() == `m0()) + " " + `lm() + " " + p.reverse() + " " + ((smoke.twin.cells.types.m.lm)p).append(`c()));
  }
}
