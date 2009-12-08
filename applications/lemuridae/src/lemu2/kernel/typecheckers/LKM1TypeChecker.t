package lemu2.kernel.typecheckers;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.proofterms.types.fovarlist.fovarList;
import lemu2.kernel.*;
import lemu2.util.*;

import java.util.Collection;
import tom.library.freshgom.*;
import tom.library.sl.*;

public class LKM1TypeChecker {

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
              return U.`alpha(U.norm(U.lookup(gamma,n),termrrules(),prs),U.norm(U.lookup(delta,cn),termrrules(),prs),free);
            }
            cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
              return U.`alpha(pa,px,free) 
                && `typecheck(M1,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs)
                && `typecheck(M2,seq(free,lctx(nprop(x,px),gamma*),delta),prs);
            }
            // left rules
            falseL(n) -> {
              Prop q = U.`lookup(gamma,n); 
              %match(q) {
                bottom() -> { return true; }
                relApp[] -> { return Rewriting.rewrite(q,prs) == `bottom(); }
              }
              return false;
            }
            andL(AndLPrem1(x,px,y,py,M),n) -> {
              Prop q = U.`lookup(gamma,n); 
              %match(q) {
                and[] -> {
                  return U.`alpha(q,and(px,py),free)
                    && `typecheck(M,seq(free,lctx(nprop(x,px),nprop(y,py),gamma*),delta),prs);
                }
                relApp[] -> {
                  return U.`alpha(Rewriting.rewrite(q,prs),and(px,py),free)
                    && `typecheck(M,seq(free,lctx(nprop(x,px),nprop(y,py),gamma*),delta),prs);
                }
              }
              return false;
            }
            orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
              Prop q = U.`lookup(gamma,n); 
              %match(q) {
                or[] -> {
                  return U.`alpha(q,or(px,py),free)
                    && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),prs)
                    && `typecheck(M2,seq(free,lctx(nprop(y,py),gamma*),delta),prs);
                }
                relApp[] -> {
                  return U.`alpha(Rewriting.rewrite(q,prs),or(px,py),free)
                    && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),prs)
                    && `typecheck(M2,seq(free,lctx(nprop(y,py),gamma*),delta),prs);
                }
              }
              return false;
            }
            implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
              Prop q = U.`lookup(gamma,n); 
              %match(q) {
                implies[] -> {
                  return U.`alpha(q,implies(px,pa),free)
                    && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),prs)
                    && `typecheck(M2,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs);
                }
                relApp[] -> {
                  return U.`alpha(Rewriting.rewrite(q,prs),implies(px,pa),free)
                    && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),prs)
                    && `typecheck(M2,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs);
                }
              }
              return false;
            }
            forallL(ForallLPrem1(x,px,M),t,n) -> {
              Prop q = U.`lookup(gamma,n); 
              %match(q) {
                forall(Fa(fx,p)) -> {
                  return U.`alpha(px,U.substFoVar(p,fx,t),free)
                    && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),prs);
                }
                relApp[] -> {
                  Prop qq = Rewriting.rewrite(q,prs);
                  %match(qq) {
                    forall(Fa(fx,p)) -> {
                      return U.`alpha(px,U.substFoVar(p,fx,t),free)
                        && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),prs);
                    }
                  }
                  return false;
                }
              }
              return false;
            }
            existsL(ExistsLPrem1(x,px,fx,M),n) -> {
              Prop q = U.`lookup(gamma,n); 
              %match(q) {
                exists[] -> {
                  return U.`alpha(q,exists(Ex(fx,px)),free)
                    /* && !((fovarList)`free).contains(`fx) //useless */
                    && `typecheck(M,seq(fovarList(fx,free*),lctx(nprop(x,px),gamma*),delta),prs); 
                }
                relApp[] -> {
                  return U.`alpha(Rewriting.rewrite(q,prs),exists(Ex(fx,px)),free)
                    /* && !((fovarList)`free).contains(`fx) //useless */
                    && `typecheck(M,seq(fovarList(fx,free*),lctx(nprop(x,px),gamma*),delta),prs); 
                }
              }
              return false;
            }
            rootL(RootLPrem1(x,px,M)) -> {
              return `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),prs); 
            }
            // right rules
            trueR(cn) -> {
              Prop q = U.`lookup(delta,cn); 
              %match(q) {
                top()    -> { return true; }
                relApp[] -> { return Rewriting.rewrite(q,prs) == `top(); }
              }
              return false;
            }
            orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
              Prop q = U.`lookup(delta,cn); 
              %match(q) {
                or[]     -> {
                  return U.`alpha(q,or(pa,pb),free)
                    && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),cnprop(b,pb),delta*)),prs);
                }
                relApp[] -> { 
                  return U.`alpha(Rewriting.rewrite(q,prs),or(pa,pb),free)
                    && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),cnprop(b,pb),delta*)),prs);
                }
              }
              return false;
            }
            andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
              Prop q = U.`lookup(delta,cn); 
              %match(q) {
                and[]    -> {
                  return U.`alpha(q,and(pa,pb),free)
                    && `typecheck(M1,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs)
                    && `typecheck(M2,seq(free,gamma,rctx(cnprop(b,pb),delta*)),prs);
                }
                relApp[] -> { 
                  return U.`alpha(Rewriting.rewrite(q,prs),and(pa,pb),free)
                    && `typecheck(M1,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs)
                    && `typecheck(M2,seq(free,gamma,rctx(cnprop(b,pb),delta*)),prs);
                }
              }
              return false;
            }
            implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
              Prop q = U.`lookup(delta,cn); 
              %match(q) {
                implies[]    -> {
                  return U.`alpha(q,implies(px,pa),free)
                    && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),rctx(cnprop(a,pa),delta*)),prs);
                }
                relApp[] -> { 
                  return U.`alpha(Rewriting.rewrite(q,prs),implies(px,pa),free)
                    && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),rctx(cnprop(a,pa),delta*)),prs);
                }
              }
              return false;
            }
            existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
              Prop q = U.`lookup(delta,cn); 
              %match(q) {
                exists(Ex(fx,p)) -> {
                  return U.`alpha(pa,U.substFoVar(p,fx,t),free) 
                    && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs);
                }
                relApp[] -> { 
                  Prop qq = Rewriting.rewrite(q,prs); 
                  %match(qq) {
                    exists(Ex(fx,p)) -> {
                      return U.`alpha(pa,U.substFoVar(p,fx,t),free) 
                        && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs);
                    }
                  } return false;
                }
              } return false;
            }
            forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
              Prop q = U.`lookup(delta,cn); 
              %match(q) {
                forall[]    -> {
                  return U.`alpha(q,forall(Fa(fx,pa)),free)
                    /* && !((fovarList)`free).contains(`fx) //useless */
                    && `typecheck(M,seq(fovarList(fx,free*),gamma,rctx(cnprop(a,pa),delta*)),prs);
                }
                relApp[] -> { 
                  return U.`alpha(Rewriting.rewrite(q,prs),forall(Fa(fx,pa)),free)
                    /* && !((fovarList)`free).contains(`fx) //useless */
                    && `typecheck(M,seq(fovarList(fx,free*),gamma,rctx(cnprop(a,pa),delta*)),prs);
                }
              }
              return false;
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
