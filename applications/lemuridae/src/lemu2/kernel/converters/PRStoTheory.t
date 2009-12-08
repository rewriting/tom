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

  private static Theory convert(PropRewriteRule r) {
    %match(r) {
      proprrule(id,prule(vars,lhs,rhs)) -> {
        return `theory(namedAx(id + "_left",pivect(vars,implies(lhs,rhs))),
                       namedAx(id + "_right",pivect(vars,implies(rhs,lhs))));

      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static Theory convert(PropRewriteRules prs) {
    %match(prs) {
      proprrules() -> { return `theory(); }
      proprrules(r,rs*) -> {
        Theory ax = `convert(r);
        Theory axs = `convert(rs);
        return `theory(ax*,axs*);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

}
