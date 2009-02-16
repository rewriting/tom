package lemu2.kernel.typecheckers;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.proofterms.types.fovarlist.fovarList;
import lemu2.kernel.*;
import lemu2.util.*;

import java.util.Collection;
import tom.library.freshgom.*;
import tom.library.sl.*;

public class TypeChecker {

  %include { kernel/proofterms/proofterms.tom } 
  %include { sl.tom } 

  public static boolean 
    typecheck(ProofTerm pt, TermRewriteRules trs, PropRewriteRules prs, PropRewriteRules pfrs) {
      return typecheck(pt,`seq(fovarList(),lctx(),rctx()),trs,prs,pfrs);
    }

  private static boolean 
    typecheck(ProofTerm pt, Sequent se, TermRewriteRules trs, PropRewriteRules prs, PropRewriteRules pfrs) {
      if(_typecheck(pt,se,trs,prs,pfrs))
        return true;
      else {
        System.out.println(Pretty.pretty(pt) + "\n\n" + Pretty.pretty(se) + "\n"); 
        return false;
      }
    }

  private static boolean 
    _typecheck(ProofTerm pt, Sequent se, TermRewriteRules trs, PropRewriteRules prs, PropRewriteRules pfrs) {
      %match(se) {
        seq(free,gamma,delta) -> {
          %match(pt) {
            ax(n,cn) -> {
              return U.`alpha(U.norm(U.lookup(gamma,n),trs,prs),U.norm(U.lookup(delta,cn),trs,prs),free);
            }
            cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
              return U.`alpha(U.norm(pa,trs,prs),U.norm(px,trs,prs),free) 
                && `typecheck(M1,seq(free,gamma,rctx(cnprop(a,pa),delta*)),trs,prs,pfrs)
                && `typecheck(M2,seq(free,lctx(nprop(x,px),gamma*),delta),trs,prs,pfrs);
            }
            // left rules
            falseL(n) -> {
              return U.`alpha(U.norm(U.lookup(gamma,n),trs,prs),bottom(),free);
            }
            andL(AndLPrem1(x,px,y,py,M),n) -> {
              return U.`alpha(U.norm(U.lookup(gamma,n),trs,prs),U.norm(and(px,py),trs,prs),free)
                && `typecheck(M,seq(free,lctx(nprop(x,px),nprop(y,py),gamma*),delta),trs,prs,pfrs);
            }
            orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
              return U.`alpha(U.norm(U.lookup(gamma,n),trs,prs),U.norm(or(px,py),trs,prs),free)
                && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),trs,prs,pfrs)
                && `typecheck(M2,seq(free,lctx(nprop(y,py),gamma*),delta),trs,prs,pfrs);
            }
            implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
              return U.`alpha(U.norm(U.lookup(gamma,n),trs,prs),U.norm(implies(pa,px),trs,prs),free)
                && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),trs,prs,pfrs)
                && `typecheck(M2,seq(free,gamma,rctx(cnprop(a,pa),delta*)),trs,prs,pfrs);
            }
            forallL(ForallLPrem1(x,px,M),t,n) -> {
              %match(U.norm(U.lookup(gamma,n),trs,prs)) {
                forall(Fa(fx,p)) -> {
                  return U.`alpha(U.norm(px,trs,prs),U.norm(U.substFoVar(p,fx,t),trs,prs),free) 
                    && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),trs,prs,pfrs);
                }
              } return false;
            }
            existsL(ExistsLPrem1(x,px,fx,M),n) -> {
              return U.`alpha(U.norm(U.lookup(gamma,n),trs,prs),U.norm(exists(Ex(fx,px)),trs,prs),free)
                && !((fovarList)`free).contains(`fx)
                && `typecheck(M,seq(fovarList(fx,free*),lctx(nprop(x,px),gamma*),delta),trs,prs,pfrs); 
            }
            rootL(RootLPrem1(x,px,M)) -> {
              return `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),trs,prs,pfrs); 
            }
            foldL(id,FoldLPrem1(x,px,M),n) -> {
              Prop prem = U.`unfold(U.norm(U.lookup(gamma,n),trs,prs),U.lookup(pfrs,id));
              return prem == null ? false : 
                U.`alpha(U.norm(prem,trs,prs),U.norm(px,trs,prs),free) 
                && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),trs,prs,pfrs);
            }
            // right rules
            trueR(cn) -> {
              return U.`alpha(U.norm(U.lookup(delta,cn),trs,prs),top(),free);
            }
            orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
              return U.`alpha(U.norm(U.lookup(delta,cn),trs,prs),U.norm(or(pa,pb),trs,prs),free)
                && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),cnprop(b,pb),delta*)),trs,prs,pfrs);
            }
            andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
              return U.`alpha(U.norm(U.lookup(delta,cn),trs,prs),U.norm(and(pa,pb),trs,prs),free)
                && `typecheck(M1,seq(free,gamma,rctx(cnprop(a,pa),delta*)),trs,prs,pfrs)
                && `typecheck(M2,seq(free,gamma,rctx(cnprop(b,pb),delta*)),trs,prs,pfrs);
            }
            implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
              return U.`alpha(U.norm(U.lookup(delta,cn),trs,prs),U.norm(implies(px,pa),trs,prs),free)
                && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),rctx(cnprop(a,pa),delta*)),trs,prs,pfrs);
            }
            existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
              %match(U.norm(U.lookup(delta,cn),trs,prs)) {
                exists(Ex(fx,p)) -> {
                  return U.`alpha(U.norm(pa,trs,prs),U.norm(U.substFoVar(p,fx,t),trs,prs),free) 
                    && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),trs,prs,pfrs);
                }
              } return false;
            }
            forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
              return U.`alpha(U.norm(U.lookup(delta,cn),trs,prs),U.norm(forall(Fa(fx,pa)),trs,prs),free)
                && !((fovarList)`free).contains(`fx)
                && `typecheck(M,seq(fovarList(fx,free*),gamma,rctx(cnprop(a,pa),delta*)),trs,prs,pfrs);
            }
            rootR(RootRPrem1(a,pa,M)) -> {
              return `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),trs,prs,pfrs);
            }
            foldR(id,FoldRPrem1(a,pa,M),cn) -> {
              Prop prem = U.`unfold(U.norm(U.lookup(delta,cn),trs,prs),U.lookup(pfrs,id));
              return prem == null ? false : 
                U.`alpha(U.norm(prem,trs,prs),U.norm(pa,trs,prs),free) 
                && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),trs,prs,pfrs);
            }
          }
        }
      }
      return false;
    }
}
