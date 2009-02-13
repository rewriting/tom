package lemu2;

import lemu2.proofterms.types.*;
import lemu2.proofterms.types.fovarlist.fovarList;
import java.util.Collection;
import tom.library.freshgom.*;
import tom.library.sl.*;

public class LKM1TypeChecker {

  %include { proofterms/proofterms.tom } 
  %include { sl.tom } 

  private static Prop lookup(LCtx ctx, Name n) {
    %match(ctx) {
      lctx(_*,nprop(x,p),_*) && x == n -> { return `p; }
    }
    throw new RuntimeException("name " + n + " not in scope");
  }

  private static Prop lookup(RCtx ctx, CoName cn) {
    %match(ctx) {
      rctx(_*,cnprop(x,p),_*) && x == cn -> { return `p; }
    }
    throw new RuntimeException("coname " + cn + " not in scope");
  }

  private static PropRewriteRule lookup(PropRewriteRules pfrules, String id) {
    %match(pfrules) {
      proprrules(_*,r@proprrule(n,_),_*) && n == id -> { return `r; }
    }
    throw new RuntimeException("rule " + id + " not in scope");
  }

  private static fovarList freeFoVars(TermList tl, fovarList ctx) {
    %match(tl) {
      termList() -> { return `fovarList(); }
      termList(t,ts*) -> {
        fovarList l1 = `freeFoVars(t,ctx);
        fovarList l2 = `freeFoVars(ts,ctx);
        return (fovarList) `fovarList(l1*,l2*);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static fovarList freeFoVars(Term t, fovarList ctx) {
    %match(t) {
      var(v) -> { if (!ctx.contains(`v)) return `fovarList(v); }
      funApp(_,tl) -> { return `freeFoVars(tl,ctx); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static fovarList freeFoVars(Prop p, fovarList ctx) {
    %match(p) {
      relApp(_,tl) -> { return `freeFoVars(tl,ctx); }
      (and|or|implies)(p1,p2) -> { 
        FoVarList l1 = `freeFoVars(p1,ctx);
        FoVarList l2 = `freeFoVars(p2,ctx);
        return (fovarList) `fovarList(l1*,l2*);
      }
      forall(Fa(x,p1)) -> { return `freeFoVars(p1,fovarList(x,ctx*)); }
      exists(Ex(x,p1)) -> { return `freeFoVars(p1,fovarList(x,ctx*)); }
      (bottom|top)() -> { return `fovarList(); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  %strategy SubstFoVar(FoVar x, Term u) extends Identity() {
    visit Term {
      var(y) && y == x -> { return `u; }
    }
  }

  private static Prop substFoVar(Prop p, FoVar x, Term u) {
    try { return (Prop) `TopDown(SubstFoVar(x,u)).visit(p); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  private static Prop norm(Prop p, PropRewriteRules prs) {
    return Rewriting.normalize(p,`termrrules(),prs);
  }

  /* checks p1 and p2 alpha-equivalence with free the set of free vars */
  private static boolean alpha(Prop p1, Prop p2, FoVarList free) {
    if (p1 == null || p2 == null) return false;
    AlphaMap<FoVar> map = new AlphaMap<FoVar>();
    %match(free) {
      fovarList(_*,v,_*) -> { map.`put(v,v,FoVar.freshFoVar("free")); }
    }
    try { p1.alpha(p2,map); return true; } 
    catch (AlphaMap.AlphaException e) { return false; }
  }

  public static boolean 
    typecheck(ProofTerm pt, PropRewriteRules prs, PropRewriteRules pfrs) {
      return typecheck(pt,`seq(fovarList(),lctx(),rctx()),prs,pfrs);
    }

  private static boolean 
    typecheck(ProofTerm pt, Sequent se, PropRewriteRules prs, PropRewriteRules pfrs) {
      if(_typecheck(pt,se,prs,pfrs))
        return true;
      else {
        System.out.println(Pretty.pretty(pt) + "\n\n" + Pretty.pretty(se) + "\n"); 
        return false;
      }
    }

  private static boolean 
    _typecheck(ProofTerm pt, Sequent se, PropRewriteRules prs, PropRewriteRules pfrs) {
      %match(se) {
        seq(free,gamma,delta) -> {
          %match(pt) {
            ax(n,cn) -> {
              return `alpha(norm(lookup(gamma,n),prs),norm(lookup(delta,cn),prs),free);
            }
            cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
              return `alpha(pa,px,free) 
                && `typecheck(M1,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs,pfrs)
                && `typecheck(M2,seq(free,lctx(nprop(x,px),gamma*),delta),prs,pfrs);
            }
            // left rules
            falseL(n) -> {
              Prop q = `lookup(gamma,n); 
              %match(q) {
                bottom() -> { return true; }
                relApp[] -> { return Rewriting.rewrite(q,prs) == `bottom(); }
              }
              return false;
            }
            andL(AndLPrem1(x,px,y,py,M),n) -> {
              Prop q = `lookup(gamma,n); 
              %match(q) {
                and[] -> {
                  return `alpha(q,and(px,py),free)
                    && `typecheck(M,seq(free,lctx(nprop(x,px),nprop(y,py),gamma*),delta),prs,pfrs);
                }
                relApp[] -> {
                  return `alpha(Rewriting.rewrite(q,prs),and(px,py),free)
                    && `typecheck(M,seq(free,lctx(nprop(x,px),nprop(y,py),gamma*),delta),prs,pfrs);
                }
              }
              return false;
            }
            orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
              Prop q = `lookup(gamma,n); 
              %match(q) {
                or[] -> {
                  return `alpha(q,or(px,py),free)
                    && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),prs,pfrs)
                    && `typecheck(M2,seq(free,lctx(nprop(y,py),gamma*),delta),prs,pfrs);
                }
                relApp[] -> {
                  return `alpha(Rewriting.rewrite(q,prs),or(px,py),free)
                    && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),prs,pfrs)
                    && `typecheck(M2,seq(free,lctx(nprop(y,py),gamma*),delta),prs,pfrs);
                }
              }
              return false;
            }
            implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
              Prop q = `lookup(gamma,n); 
              %match(q) {
                implies[] -> {
                  return `alpha(q,implies(px,pa),free)
                    && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),prs,pfrs)
                    && `typecheck(M2,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs,pfrs);
                }
                relApp[] -> {
                  return `alpha(Rewriting.rewrite(q,prs),implies(px,pa),free)
                    && `typecheck(M1,seq(free,lctx(nprop(x,px),gamma*),delta),prs,pfrs)
                    && `typecheck(M2,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs,pfrs);
                }
              }
              return false;
            }
            forallL(ForallLPrem1(x,px,M),t,n) -> {
              Prop q = `lookup(gamma,n); 
              %match(q) {
                forall(Fa(fx,p)) -> {
                  return `alpha(px,substFoVar(p,fx,t),free)
                    && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),prs,pfrs);
                }
                relApp[] -> {
                  Prop qq = Rewriting.rewrite(q,prs);
                  %match(qq) {
                    forall(Fa(fx,p)) -> {
                      return `alpha(px,substFoVar(p,fx,t),free)
                        && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),prs,pfrs);
                    }
                  }
                  return false;
                }
              }
              return false;
            }
            existsL(ExistsLPrem1(x,px,fx,M),n) -> {
              Prop q = `lookup(gamma,n); 
              %match(q) {
                exists[] -> {
                  return `alpha(q,exists(Ex(fx,px)),free)
                    && `typecheck(M,seq(fovarList(fx,free*),lctx(nprop(x,px),gamma*),delta),prs,pfrs); 
                }
                relApp[] -> {
                  return `alpha(Rewriting.rewrite(q,prs),exists(Ex(fx,px)),free)
                    && `typecheck(M,seq(fovarList(fx,free*),lctx(nprop(x,px),gamma*),delta),prs,pfrs); 
                }
              }
              return false;
            }
            rootL(RootLPrem1(x,px,M)) -> {
              return `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),delta),prs,pfrs); 
            }
            // right rules
            trueR(cn) -> {
              Prop q = `lookup(delta,cn); 
              %match(q) {
                top()    -> { return true; }
                relApp[] -> { return Rewriting.rewrite(q,prs) == `top(); }
              }
              return false;
            }
            orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
              Prop q = `lookup(delta,cn); 
              %match(q) {
                or[]     -> {
                  return `alpha(q,or(pa,pb),free)
                    && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),cnprop(b,pb),delta*)),prs,pfrs);
                }
                relApp[] -> { 
                  return `alpha(Rewriting.rewrite(q,prs),or(pa,pb),free)
                    && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),cnprop(b,pb),delta*)),prs,pfrs);
                }
              }
              return false;
            }
            andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
              Prop q = `lookup(delta,cn); 
              %match(q) {
                and[]    -> {
                  return `alpha(q,and(pa,pb),free)
                    && `typecheck(M1,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs,pfrs)
                    && `typecheck(M2,seq(free,gamma,rctx(cnprop(b,pb),delta*)),prs,pfrs);
                }
                relApp[] -> { 
                  return `alpha(Rewriting.rewrite(q,prs),and(pa,pb),free)
                    && `typecheck(M1,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs,pfrs)
                    && `typecheck(M2,seq(free,gamma,rctx(cnprop(b,pb),delta*)),prs,pfrs);
                }
              }
              return false;
            }
            implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
              Prop q = `lookup(delta,cn); 
              %match(q) {
                implies[]    -> {
                  return `alpha(q,implies(px,pa),free)
                    && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),rctx(cnprop(a,pa),delta*)),prs,pfrs);
                }
                relApp[] -> { 
                  return `alpha(Rewriting.rewrite(q,prs),implies(px,pa),free)
                    && `typecheck(M,seq(free,lctx(nprop(x,px),gamma*),rctx(cnprop(a,pa),delta*)),prs,pfrs);
                }
              }
              return false;
            }
            existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
              Prop q = `lookup(delta,cn); 
              %match(q) {
                exists(Ex(fx,p)) -> {
                  return `alpha(pa,substFoVar(p,fx,t),free) 
                    && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs,pfrs);
                }
                relApp[] -> { 
                  Prop qq = Rewriting.rewrite(q,prs); 
                  %match(qq) {
                    exists(Ex(fx,p)) -> {
                      return `alpha(pa,substFoVar(p,fx,t),free) 
                        && `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs,pfrs);
                    }
                  } return false;
                }
              } return false;
            }
            forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
              Prop q = `lookup(delta,cn); 
              %match(q) {
                forall[]    -> {
                  return `alpha(q,forall(Fa(fx,pa)),free)
                    && `typecheck(M,seq(fovarList(fx,free*),gamma,rctx(cnprop(a,pa),delta*)),prs,pfrs);
                }
                relApp[] -> { 
                  return `alpha(Rewriting.rewrite(q,prs),forall(Fa(fx,pa)),free)
                    && `typecheck(M,seq(fovarList(fx,free*),gamma,rctx(cnprop(a,pa),delta*)),prs,pfrs);
                }
              }
              return false;
            }
            rootR(RootRPrem1(a,pa,M)) -> {
              return `typecheck(M,seq(free,gamma,rctx(cnprop(a,pa),delta*)),prs,pfrs);
            }
          }
        }
      }
      return false;
    }
}
