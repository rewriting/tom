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
      andR(AndRPrem1(a,pa,M),AndRPrem2(b,pb,N),c) -> {
        return `passiv(c,pair(activ(Act(a,pa,convert(M))),activ(Act(b,pb,convert(N)))));
      }
      andL(AndLPrem1(x,px,y,py,M),z) -> {
        return U.`substName(U.substName(convert(M),x,proj1(lvar(z))),y,proj2(lvar(z)));
      }
      forallR(ForallRPrem1(a,pa,fx,M),b) -> {
        return `passiv(b,flam(FLam(fx,activ(Act(a,pa,convert(M))))));
      }
      forallL(ForallLPrem1(x,px,M),t,y) -> {
        return U.`substName(convert(M),x,fapp(lvar(y),t));
      }
      orR(OrRPrem1(a,pa,b,pb,M),c) -> {
        return `passiv(c,
            left(activ(Act(a,pa,passiv(c,right(activ(Act(b,pb,convert(M)))))))));
      }
      orL(OrLPrem1(x,px,M),OrLPrem2(y,py,N),z) -> {
        return `caseof(lvar(z),Alt(x,px,convert(M)),Alt(y,py,convert(N)));
      }
      // cuts
      /*
      cut(CutPrem1(a,pa,M),
          CutPrem2(x,px,prem@implyL(ImplyLPrem1(y,py,N),ImplyLPrem2(b,pb,Q),x))) -> {
        System.out.println("ici");
        if (!U.`isImplicitContraction(prem)) 
          return U.`substName(convert(N),y,
              lapp(activ(Act(a,pa,convert(M))),activ(Act(b,pb,convert(Q)))));
      } */
      cut(CutPrem1(a,pa,M),CutPrem2(x,px,N)) -> {
        return U.`substName(convert(N),x,activ(Act(a,pa,convert(M))));
      }
    }
    return null;
  }
}
