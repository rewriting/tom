package lemu2.kernel.converters;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.*;
import lemu2.util.*;

public class LKtoLambdaMu {
  
  %include { kernel/proofterms/proofterms.tom } 


  public static LTerm convert(ProofTerm pt) {
    %match(pt) {
      rootR(RootRPrem1(a,pa,M)) -> { return `activ(Act(a,pa,convert(M))); }
      ax(x,a) -> { return `passiv(a,lvar(x)); }
      implyR(ImplyRPrem1(x,px,a,pa,M),b) -> { 
        return `passiv(b,lam(Lam(x,px,activ(Act(a,pa,convert(M))))));
      }
      implyL(ImplyLPrem1(x,px,M),ImplyLPrem2(a,pa,N),y) -> {
        return U.`substName(convert(M),x,lapp(lvar(y),activ(Act(a,pa,convert(N)))));
      }
      // cuts
      cut(CutPrem1(a,pa,M),
          CutPrem2(x,px,prem@implyL(ImplyLPrem1(y,py,N),ImplyLPrem2(b,pb,Q),x))) -> {
        if (!U.`isImplicitContraction(prem)) 
          return U.`substName(convert(N),y,
              lapp(activ(Act(a,pa,convert(M))),activ(Act(b,pb,convert(Q)))));
      }
      cut(CutPrem1(a,pa,M),CutPrem2(x,px,N)) -> {
        return U.`substName(convert(N),x,activ(Act(a,pa,convert(M))));
      }
    }
    return null;
  }
}
