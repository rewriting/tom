import formula.*;
import formula.types.*;
import java.util.*;

public class Pretty {

  %include { formula/formula.tom }

  /*
   * Pretty printer
   */
  public String pretty(Exp e) {
    %match(e) {
      CstInt(v) -> { return ""+`v; }
      Var(name) -> { return `name + "_"; }
      Plus(e1,e2) -> { return pretty(`e1) + "+" + pretty(`e2); }
      Mult(e1,e2) -> { return pretty(`e1) + "*" + pretty(`e2); }
      O(eps) -> { return "O(" + pretty(`eps) + ")"; }
      Index(body,indexname) -> { return pretty(`body) + "_" + `indexname; }
      Function(symbol,args) -> { return pretty(`symbol) + "(" + pretty(`args) + ")"; }
      Partial(body,var) -> { return "d." + pretty(`body) + "/" + "d." + pretty(`var); }
      Sum(domain,body,var) -> { return "(Sum " + pretty(`var) + " in " + pretty(`domain) + ". " + pretty(`body) + ")"; }
      Integral(domain,body,var) -> { return "(Integral " + pretty(`domain) + ". " + pretty(`body) + ". d" + pretty(`var) + ")"; }
    }
    return "Exp undefined case: " + e;
  }

  public String pretty(ExpList e) {
    %match(e) {
      concExp() -> { return ""; }
      concExp(head,tail*) -> { return pretty(`head) + "," + pretty(`tail*); }
    }

    return "ExpList undefined case: " + e;
  }

  public String pretty(Epsilon e) {
    %match(e) {
      Epsilon(v) -> { return "epsilon_"+`v; }
    }

    return "Epsilon undefined case: " + e;
  }

  public String pretty(Region e) {
    %match(e) {
      Interval(e1,e2) -> { return "[" + pretty(`e1) + "," + pretty(`e2) + "]"; }
      Domain(name) -> { return `name; }
    }

    return "Region undefined case: " + e;
  }

  public String pretty(Symbol e) {
    %match(e) {
      Symbol(name) -> { return `name; }
    }

    return "Symbol undefined case: " + e;
  }

}
