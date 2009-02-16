package lemu2.kernel.typecheckers;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.proofterms.types.fovarlist.fovarList;
import lemu2.kernel.*;
import lemu2.util.*;

import java.util.Collection;
import tom.library.freshgom.*;
import tom.library.sl.*;

public class LKFTypeChecker {

  %include { kernel/proofterms/proofterms.tom } 
  %include { sl.tom } 

  public static boolean 
    typecheck(ProofTerm pt, PropRewriteRules prs) {
      return typecheck(pt,`seq(fovarList(),lctx(),rctx()),prs);
    }

  private static boolean 
    typecheck(ProofTerm pt, Sequent se, PropRewriteRules prs) {
      if(_typecheck(pt,se,prs))
        return true;
      else {
        //System.out.println(Pretty.pretty(pt) + "\n\n" + Pretty.pretty(se) + "\n"); 
        return false;
      }
    }

  private static boolean 
    _typecheck(ProofTerm pt, Sequent se, PropRewriteRules prs) {
      %match(se) {
        seq(free,gamma,delta) -> {
          %match(pt) {
            ax(n,cn) -> { 
              return U.`alpha(U.lookup(gamma,n),U.lookup(delta,cn),free); 
            }
            cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
              return U.`alpha(pa,px,free) 
                && `typecheck(M1,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs)
                && `typecheck(M2,seq(free,lctx(nprop(x,px),gamma*),delta),prs);
            }
            // left rules
            falseL(n) -> { 
              return U.`alpha(U.lookup(gamma,n),bottom(),free);
            }
            andL(AndLPrem1(x,px,y,py,M),n) -> {
              return U.`alpha(U.lookup(gamma,n),and(px,py),free);
            }
            orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
              return U.`alpha(U.lookup(gamma,n),or(px,py),free)
                && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),prs)
                && `typecheck(M2,seq(free,lctx(nprop(y,py),gamma*),delta),prs);
            }
            implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
              return U.`alpha(U.lookup(gamma,n),implies(pa,px),free)
                && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),prs)
                && `typecheck(M2,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs);
            }
            forallL(ForallLPrem1(x,px,M),t,n) -> {
              %match(U.lookup(gamma,n)) {
                forall(Fa(fx,p)) -> {
                  return U.`alpha(px,U.substFoVar(p,fx,t),free) 
                    && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),prs);
                }
              } return false;
            }
            existsL(ExistsLPrem1(x,px,fx,M),n) -> {
              return U.`alpha(U.lookup(gamma,n),exists(Ex(fx,px)),free)
                && !((fovarList)`free).contains(`fx)
                && `typecheck(M,seq(fovarList(fx,free*),lctx(nprop(x,px),gamma*),delta),prs); 
            }
            foldL(id,FoldLPrem1(x,px,M),n) -> {
              Prop prem = U.`unfold(U.lookup(gamma,n),U.lookup(prs,id));
              return prem == null ? false : 
                U.`alpha(prem,px,free) 
                && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),prs);
            }
            rootL(RootLPrem1(x,px,M)) -> {
              return `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),prs); 
            }
            // right rules
            trueR(cn) -> {
              return U.`alpha(U.lookup(delta,cn),top(),free);
            }
            orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
              return U.`alpha(U.lookup(delta,cn),or(pa,pb),free)
                && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),cnprop(b,pb),delta*)),prs);
            }
            andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
              return U.`alpha(U.lookup(delta,cn),and(pa,pb),free)
                && `typecheck(M1,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs)
                && `typecheck(M2,seq(free,gamma,rctx(cnprop(b,pb),delta*)),prs);
            }
            implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
              return U.`alpha(U.lookup(delta,cn),implies(px,pa),free)
                && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),rctx(cnprop(a,pa),delta*)),prs);
            }
            existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
              %match(U.lookup(delta,cn)) {
                exists(Ex(fx,p)) -> {
                  return U.`alpha(pa,U.substFoVar(p,fx,t),free) 
                    && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs);
                }
              } return false;
            }
            forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
              return U.`alpha(U.lookup(delta,cn),forall(Fa(fx,pa)),free)
                && !((fovarList)`free).contains(`fx)
                && `typecheck(M,seq(fovarList(fx,free*),gamma,rctx(cnprop(a,pa),delta*)),prs);
            }
            foldR(id,FoldRPrem1(a,pa,M),cn) -> {
              Prop prem = U.`unfold(U.lookup(delta,cn),U.lookup(prs,id));
              return prem == null ? false : 
                U.`alpha(prem,pa,free) 
                && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs);
            }
            rootR(RootRPrem1(a,pa,M)) -> {
              return `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs);
            }
          }
        }
      }
      return false;
    }
}
