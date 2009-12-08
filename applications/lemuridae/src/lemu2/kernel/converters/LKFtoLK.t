package lemu2.kernel.converters;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.*;
import lemu2.util.*;
import fj.data.List;
import fj.data.Option;
import fj.pre.*;
import fj.*;


public class LKFtoLK {

  private static F2<List<P2<String,Name>>,String,Option<Name>> 
    strlookup = List.<String,Name>lookup(Equal.<String>anyEqual());

  %include { kernel/proofterms/proofterms.tom } 

  private static ProofTerm convertFold(
      ProofTerm pt,
      PropRewriteRules prs,
      Theory axioms,
      List<P2<String,Name>> map,
      Sequent se) {
    %match(se) {
      seq(free,gamma,delta) -> {
        %match(pt) {
          foldL(id,FoldLPrem1(n1,pn1,M),n) -> {
            Prop subject = U.`lookup(gamma,n);
            NamedAx ax = U.`lookup(axioms,id + "_left");
            PropRewriteRule prr = U.`lookup(prs,id);
            Name axn = strlookup.`f(map,id + "_left").some();
            %match(prr) {
              proprrule(_,prule(bound,lhs,_)) -> {
                FoSubst subst = Rewriting.`match(subject,lhs);
                Sequent se1 = `seq(free,lctx(nprop(n1,pn1),gamma*),delta);
                ProofTerm M1 = `convert(M,prs,axioms,map,se1);
                return `convertLeft(ax.getax(),bound,subst,axn,n,n1,M1);
              }
            }
          }
          foldR(id,FoldRPrem1(cn1,pcn1,M),cn) -> {
            Prop subject = U.`lookup(delta,cn);
            NamedAx ax = U.`lookup(axioms,id + "_right");
            PropRewriteRule prr = U.`lookup(prs,id);
            Name axn = strlookup.`f(map,id + "_right").some();
            %match(prr) {
              proprrule(_,prule(bound,lhs,_)) -> {
                FoSubst subst = Rewriting.`match(subject,lhs);
                Sequent se1 = `seq(free,gamma,rctx(cnprop(cn1,pcn1),delta*));
                ProofTerm M1 = `convert(M,prs,axioms,map,se1);
                return `convertRight(ax.getax(),bound,subst,axn,cn,cn1,M1);
              }
            }
          }
        }
      }
    }
    throw new RuntimeException("conversion exception");
  }

  private static ProofTerm convert(
      ProofTerm pt,
      PropRewriteRules prs,
      Theory axioms,
      List<P2<String,Name>> map,
      Sequent se) {
    //System.out.println(Pretty.pretty(pt));
    %match(se) {
      seq(free,gamma,delta) -> {
        %match(pt) {
          rootR(RootRPrem1(a,pa,M)) -> {
            Sequent se1 = `seq(free,gamma,rctx(cnprop(a,pa),delta*));
            return `rootR(RootRPrem1(a,pa,convert(M,prs,axioms,map,se1)));
          }
          trueR(a) -> { return `trueR(a); }
          falseL(x) -> { return `falseL(x); }
          ax(x,a) -> { return `ax(x,a); }
          implyR(ImplyRPrem1(x,px,a,pa,M),b) -> { 
            Sequent se1 = `seq(free,lctx(nprop(x,px),gamma*),rctx(cnprop(a,pa),delta*));
            return `implyR(ImplyRPrem1(x,px,a,pa,convert(M,prs,axioms,map,se1)),b); 
          }
          implyL(ImplyLPrem1(x,px,M),ImplyLPrem2(a,pa,N),y) -> {
            Sequent se1 = `seq(free,lctx(nprop(x,px),gamma*),delta);
            Sequent se2 = `seq(free,gamma,rctx(cnprop(a,pa),delta*));
            return `implyL(
                ImplyLPrem1(x,px,convert(M,prs,axioms,map,se1)),
                ImplyLPrem2(a,pa,convert(N,prs,axioms,map,se2)),y);
          }
          andR(AndRPrem1(a,pa,M),AndRPrem2(b,pb,N),c) -> {
            Sequent se1 = `seq(free,gamma,rctx(cnprop(a,pa),delta*));
            Sequent se2 = `seq(free,gamma,rctx(cnprop(b,pb),delta*));
            return `andR(
                AndRPrem1(a,pa,convert(M,prs,axioms,map,se1)),
                AndRPrem2(b,pb,convert(N,prs,axioms,map,se2)),c);
          }
          andL(AndLPrem1(x,px,y,py,M),z) -> {
            Sequent se1 = `seq(free,lctx(nprop(x,px),nprop(y,py),gamma*),delta);
            return `andL(AndLPrem1(x,px,y,py,convert(M,prs,axioms,map,se1)),z);
          }
          forallR(ForallRPrem1(a,pa,fx,M),b) -> {
            Sequent se1 = `seq(fovarList(fx,free),gamma,rctx(cnprop(a,pa),delta*));
            return `forallR(ForallRPrem1(a,pa,fx,convert(M,prs,axioms,map,se1)),b);
          }
          forallL(ForallLPrem1(x,px,M),t,y) -> {
            Sequent se1 = `seq(free,lctx(nprop(x,px),gamma*),delta);
            return `forallL(ForallLPrem1(x,px,convert(M,prs,axioms,map,se1)),t,y);
          }
          orR(OrRPrem1(a,pa,b,pb,M),c) -> {
            Sequent se1 = `seq(free,gamma,rctx(cnprop(a,pa),cnprop(b,pb),delta*));
            return `orR(OrRPrem1(a,pa,b,pb,convert(M,prs,axioms,map,se1)),c);
          }
          orL(OrLPrem1(x,px,M),OrLPrem2(y,py,N),z) -> {
            Sequent se1 = `seq(free,lctx(nprop(x,px),gamma*),delta);
            Sequent se2 = `seq(free,lctx(nprop(y,py),gamma*),delta);
            return `orL(
                OrLPrem1(x,px,convert(M,prs,axioms,map,se1)),
                OrLPrem2(y,py,convert(N,prs,axioms,map,se2)),z);
          }
          existsR(ExistsRPrem1(a,pa,M),ft,b) -> {
            Sequent se1 = `seq(free,gamma,rctx(cnprop(a,pa),delta*));
            return `existsR(ExistsRPrem1(a,pa,convert(M,prs,axioms,map,se1)),ft,b);
          }
          cut(CutPrem1(a,pa,M),CutPrem2(x,px,N)) -> {
            Sequent se1 = `seq(free,gamma,rctx(cnprop(a,pa),delta*));
            Sequent se2 = `seq(free,lctx(nprop(x,px),gamma*),delta);
            return `cut(
                CutPrem1(a,pa,convert(M,prs,axioms,map,se1)),
                CutPrem2(x,px,convert(N,prs,axioms,map,se2)));
          }
          (foldR|foldL)[] -> {
            return `convertFold(pt,prs,axioms,map,se);
          }
        }
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static ProofTerm convertLeft
    (Prop ax, FoBound vect, FoSubst subst, Name axn, Name n, Name n1, ProofTerm prem) {
    %match(ax, vect) {
      forall(Fa(fx,ax1)), foBound(v,vs*) -> {
        Term ft = U.`lookup(subst,v); 
        Prop ax11 = U.`substFoVar(ax1,fx,ft);
        Name freshaxn = Name.freshName(axn);
        ProofTerm M = `convertLeft(ax11,vs,subst,freshaxn,n,n1,prem);
        return `forallL(ForallLPrem1(freshaxn,ax11,M),ft,axn);
      }
      implies(p,phi), _ -> {
        CoName ap = CoName.freshCoName("lhs");
        Name xphi = Name.freshName("rhs");
        return `implyL(
                  ImplyLPrem1(xphi,phi,U.rename(prem,n1,xphi)),
                  ImplyLPrem2(ap,p,ax(n,ap)),
                  axn);
      }
    }
    throw new RuntimeException("conversion exception");
  }

  private static ProofTerm convertRight
    (Prop ax, FoBound vect, FoSubst subst, Name axn, CoName cn, CoName cn1, ProofTerm prem) {
    %match(ax, vect) {
      forall(Fa(fx,ax1)), foBound(v,vs*) -> {
        Term ft = U.`lookup(subst,v); 
        Prop ax11 = U.`substFoVar(ax1,fx,ft);
        Name freshaxn = Name.freshName(axn);
        ProofTerm M = `convertRight(ax11,vs,subst,freshaxn,cn,cn1,prem);
        return `forallL(ForallLPrem1(freshaxn,ax11,M),ft,axn);
      }
      implies(phi,p), _ -> {
        Name xp = Name.freshName("lhs");
        CoName aphi = CoName.freshCoName("rhs");
        return `implyL(
            ImplyLPrem1(xp,p,ax(xp,cn)),
            ImplyLPrem2(aphi,phi,U.reconame(prem,cn1,aphi)),
            axn);
      }
    }
    throw new RuntimeException("conversion exception");
  }

  private static ProofTerm 
    convert(ProofTerm pt, Theory th, List<P2<String,Name>> map) {
      %match(th) {
        theory() -> { return pt; }
        theory(namedAx(id,ax),axs*) -> {
          %match(pt) {
            rootR(RootRPrem1(a,pa,M)) -> {
              Name idn = strlookup.`f(map,id).some();
              CoName fresha = CoName.`freshCoName(a);
              return `convert(
                rootR(RootRPrem1(fresha,implies(ax,pa),implyR(ImplyRPrem1(idn,ax,a,pa,M),fresha))),
                axs,
                map);
            }
          }
        }
      }
      throw new RuntimeException("conversion exception");
    }

  public static ProofTerm convert(ProofTerm pt, PropRewriteRules pfrs) {
    Theory th = PRStoTheory.convert(pfrs);
    List<P2<String,Name>> map = List.<P2<String,Name>>nil();
    %match(th) {
      theory(_*,namedAx(n,_),_*) -> { map = map.`cons(P.p(n,Name.freshName(n))); }
    }
    pt = `convert(pt,pfrs,th,map,seq(fovarList(),lctx(),rctx()));
    return `convert(pt,th,map);
  }

}
