package lemu2.kernel.typecheckers;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.proofterms.types.fovarlist.fovarList;
import lemu2.kernel.*;
import lemu2.util.*;

import java.util.Collection;
import tom.library.freshgom.*;
import tom.library.sl.*;

import fj.data.List;
import fj.data.Option;
import fj.pre.*;
import fj.*;

public class LambdaMuTypeChecker {

  %include { kernel/proofterms/proofterms.tom } 
  %include { sl.tom } 

  public static Prop typeof(LTerm pt) {
    return `typeof(pt,seq(fovarList(),lctx(),rctx()));
  }

  public static Prop typeof(LTerm pt, Sequent se) {
    Prop res = _typeof(pt,se);
    if (res == null) {
      System.out.println(pt);
      System.out.println(Pretty.pretty(se));
      System.out.println();
    }
    return res;
  }

  public static Prop _typeof(LTerm pt, Sequent se) { 
    %match(se) {
      seq(free,gamma,delta) -> {
        %match(pt) {
          lvar(v) -> { return U.`lookup(gamma,v); }
          lam(Lam(x,tx,u)) -> { 
            Prop tu = `typeof(u,seq(free,lctx(nprop(x,tx),gamma*),delta));
            return `implies(tx,tu);
          }
          flam(FLam(x,u)) -> { 
            return `forall(Fa(x,typeof(u,seq(fovarList(x,free*),gamma,delta))));
          }
          activ(Act(x,tx,u)) -> {
            // only to check well-typedness
            Prop tu = `typeof(u,seq(free,gamma,rctx(cnprop(x,tx),delta*)));
            return `tx;
            //return tu == `bottom() ? `tx : null;
          }
          lapp(u,v) -> {
            Prop tu = `typeof(u,se);
            Prop tv = `typeof(v,se);
            %match(tu) {
              implies(A,B) -> { return U.`alpha(A,tv,free) ? `B : null; }
              _            -> { return null; }
            }
          }
          fapp(u,ft) -> {
            Prop tu = `typeof(u,se);
            %match(tu) {
              forall(Fa(x,A)) -> { return U.`substFoVar(A,x,ft); }
              _               -> { return null; }
            }
          }
          pair(u,v) -> { 
            Prop tu = `typeof(u,se);
            Prop tv = `typeof(v,se);
            return `and(tu,tv);
          }
          proj1(u) -> { 
            Prop tu = `typeof(u,se);
            %match(tu) {
              and(A,_) -> { return `A; }
              _        -> { return null; }
            }
          }
          proj2(u) -> { 
            Prop tu = `typeof(u,se);
            %match(tu) {
              and(_,B) -> { return `B; }
              _        -> { return null; }
            }
          }
          caseof(u,Alt(x,px,v),Alt(y,py,w)) -> { 
            Prop tu = `typeof(u,se);
            Prop tv = `typeof(v,seq(free,lctx(nprop(x,px),gamma*),delta));
            Prop tw = `typeof(w,seq(free,lctx(nprop(y,py),gamma*),delta));
            %match(tu) {
              or(A,B) -> { 
                if (U.`alpha(A,px,free) && U.`alpha(B,py,free) && U.`alpha(tv,tw,free))
                  return tv;
                else 
                  return null; 
              }
              _       -> { return null; }
            }
          }
          letin(Letin(fx,x,px,u,v)) -> {
            Prop tu = `typeof(u,se);
            Sequent se1 = `seq(fovarList(fx,free),lctx(nprop(x,px),gamma*),delta);
            Prop tv = `typeof(v,se1);
            %match(tu) {
              exists(Ex(fy,A)) -> { 
                if (U.`alpha(U.substFoVar(A,fy,var(fx)),px,fovarList(fy,free*))) return tv;
                else                                                             return null;
              }
              _ -> { return null; }
            }
          }
          witness(ft,u,p) -> {
            Prop tu = `typeof(u,se);
            %match(p) {
              exists(Ex(fx,A)) -> { 
                if (U.`alpha(U.substFoVar(A,fx,ft),tu,free)) return `p;
                else                                         return null; 
              }
              _                -> { return null; }
            }
          }
          left(u,p) -> {
            Prop tu = `typeof(u,se);
            return `or(tu,p);            
          }
          right(u,p) -> { 
            Prop tu = `typeof(u,se);
            return `or(p,tu);            
          }
          passiv(mv,u) -> {
            Prop tu = `typeof(u,se);
            Prop tv = U.`lookup(delta,mv);
            return U.`alpha(tu,tv,free) ? `bottom() : null;
          }
          unit() -> {
            return `top();
          }
        }
      }
    }
    return null;
  }
}
