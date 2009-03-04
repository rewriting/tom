package lemu2.kernel.converters;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.*;
import lemu2.util.*;

public class PRStoTheory {
  
  %include { kernel/proofterms/proofterms.tom } 
  
  private static Prop pivect(FoBound vars, Prop p) {
    %match(vars) {
      foBound() -> { return p; }
      foBound(x,xs*) -> { return `forall(Fa(x,pivect(xs,p))); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static NamedAx convert1(PropRewriteRule r) {
    %match(r) {
      proprrule(id,prule(vars,lhs,rhs)) -> {
        return `namedAx(id,pivect(vars,implies(lhs,rhs)));
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static NamedAx convert2(PropRewriteRule r) {
    %match(r) {
      proprrule(id,prule(vars,lhs,rhs)) -> {
        return `namedAx(id,pivect(vars,implies(rhs,lhs)));
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static Theory convert(PropRewriteRules prs) {
    %match(prs) {
      proprrules() -> { return `theory(); }
      proprrules(r,rs*) -> {
        Theory axs = `convert(rs);
        return `theory(convert1(r),convert2(r),axs*);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

}
