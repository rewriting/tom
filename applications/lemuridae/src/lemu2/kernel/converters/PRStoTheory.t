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

  private static NamedAx convert(PropRewriteRule r) {
    %match(r) {
      proprrule(id,prule(vars,lhs,rhs)) -> {
        return `namedAx(id,pivect(vars,and(implies(lhs,rhs),implies(rhs,lhs))));
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Prop convert2(PropRewriteRule r) {
    %match(r) {
      proprrule(id,prule(vars,lhs,rhs)) -> {
        return `pivect(vars,implies(rhs,lhs));
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static Theory convert(PropRewriteRules prs) {
    %match(prs) {
      proprrules() -> { return `theory(); }
      proprrules(r@proprrule(id,_),rs*) -> {
        Theory axs = `convert(rs);
        return `theory(convert(r),axs*);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

}
