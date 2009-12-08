package lemu2.kernel.converters;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.*;
import lemu2.util.*;

public class LKtoLambdaMu {
  
  %include { kernel/proofterms/proofterms.tom } 

  public static LTerm convert(ProofTerm pt) {
    return `convert(pt,seq(fovarList(),lctx(),rctx()));
  }

  private static LTerm convert(ProofTerm pt, Sequent se) {
    %match(se) {
      seq(free,gamma,delta) -> {
        %match(pt) {
          rootR(RootRPrem1(a,pa,M)) -> {
            Sequent se1 = `seq(free,gamma,rctx(cnprop(a,pa),delta*));
            return `activ(Act(a,pa,convert(M,se1))); 
          }
          ax(x,a) -> { return `passiv(a,lvar(x)); }
          trueR(a) -> { return `passiv(a,unit()); }
          implyR(ImplyRPrem1(x,px,a,pa,M),b) -> { 
            Sequent se1 = `seq(free,lctx(nprop(x,px),gamma*),rctx(cnprop(a,pa),delta*));
            return `passiv(b,lam(Lam(x,px,activ(Act(a,pa,convert(M,se1))))));
          }
          implyL(ImplyLPrem1(x,px,M),ImplyLPrem2(a,pa,N),y) -> {
            Sequent se1 = `seq(free,lctx(nprop(x,px),gamma*),delta);
            Sequent se2 = `seq(free,gamma,rctx(cnprop(a,pa),delta*));
            return U.`substName(convert(M,se1),x,
                        lapp(lvar(y),activ(Act(a,pa,convert(N,se2)))));
          }
          andR(AndRPrem1(a,pa,M),AndRPrem2(b,pb,N),c) -> {
            Sequent se1 = `seq(free,gamma,rctx(cnprop(a,pa),delta*));
            Sequent se2 = `seq(free,gamma,rctx(cnprop(b,pb),delta*));
            return `passiv(c,pair(activ(Act(a,pa,
                      convert(M,se1))),activ(Act(b,pb,convert(N,se2)))));
          }
          andL(AndLPrem1(x,px,y,py,M),z) -> {
            Sequent se1 = `seq(free,lctx(nprop(x,px),nprop(y,py),gamma*),delta);
            return U.`substName(
                     U.substName(convert(M,se1),x,proj1(lvar(z))),y,proj2(lvar(z)));
          }
          forallR(ForallRPrem1(a,pa,fx,M),b) -> {
            Sequent se1 = `seq(fovarList(fx,free),gamma,rctx(cnprop(a,pa),delta*));
            return `passiv(b,flam(FLam(fx,activ(Act(a,pa,convert(M,se1))))));
          }
          forallL(ForallLPrem1(x,px,M),t,y) -> {
            Sequent se1 = `seq(free,lctx(nprop(x,px),gamma*),delta);
            return U.`substName(convert(M,se1),x,fapp(lvar(y),t));
          }
          orR(OrRPrem1(a,pa,b,pb,M),c) -> {
            Sequent se1 = `seq(free,gamma,rctx(cnprop(a,pa),cnprop(b,pb),delta*));
            return `passiv(c,
                      left(activ(Act(a,pa,
                        passiv(c,right(activ(Act(b,pb,convert(M,se1))),pa)))),pb));
          }
          orL(OrLPrem1(x,px,M),OrLPrem2(y,py,N),z) -> {
            Sequent se1 = `seq(free,lctx(nprop(x,px),gamma*),delta);
            Sequent se2 = `seq(free,lctx(nprop(y,py),gamma*),delta);
            return `caseof(lvar(z),Alt(x,px,convert(M,se1)),Alt(y,py,convert(N,se2)));
          }
          existsR(ExistsRPrem1(a,pa,M),ft,b) -> {
            Sequent se1 = `seq(free,gamma,rctx(cnprop(a,pa),delta*));
            Prop xp = U.`lookup(delta,b);
            return `passiv(b,witness(ft,activ(Act(a,pa,convert(M,se1))),xp));
          }

          // cuts
          cut(CutPrem1(a,pa,M),CutPrem2(x,px,N)) -> {
            Sequent se1 = `seq(free,gamma,rctx(cnprop(a,pa),delta*));
            Sequent se2 = `seq(free,lctx(nprop(x,px),gamma*),delta);
            return U.`substName(convert(N,se2),x,activ(Act(a,pa,convert(M,se1))));
          }
        }
      }
    }
      return null;
  }
}
