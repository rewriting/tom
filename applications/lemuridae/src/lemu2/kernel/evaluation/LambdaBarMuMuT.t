package lemu2.kernel.evaluation;

import tom.library.sl.*;

import lemu2.kernel.U;
import lemu2.kernel.proofterms.types.*;
import fj.*;
import fj.P;
import fj.data.Stream;
import java.util.Collection;

public class LambdaBarMuMuT {

  %include { kernel/proofterms/proofterms.tom } 
  %include { sl.tom }

  public static Stream<LMMContext> eval(final LMMContext e) {
    %match(e) {
      /* y */
      lmmCoVar(a) -> { return Stream.<LMMContext>`single(lmmCoVar(a)); }
      /* mu~ x:tyx . u */
      lmmMuT(LmmMuT(_x,_tyx,_c)) -> {
        /* do c_ <- eval c
              return $ mu~ x:tyx. c_ */
        final Name x = `_x;
        final Prop tyx = `_tyx;
        final LMMCommand c = `_c;
        return eval(c).bind(new F<LMMCommand,Stream<LMMContext>>() {
          public Stream<LMMContext> f(final LMMCommand c_) {
            return Stream.<LMMContext>`single(lmmMuT(LmmMuT(x,tyx,c_)));
          }
        });
      }
      /* v . e1 */
      lmmDot(_v,_e1) -> { 
        /* do v_ <- eval v
              e1_ <- eval e1
              return $ v_ `dot` e_1 */
        final LMMTerm v = `_v;
        final LMMContext e1 = `_e1;
        return eval(v).bind(new F<LMMTerm,Stream<LMMContext>>() {
          public Stream<LMMContext> f(final LMMTerm v_) {
            return eval(e1).bind(new F<LMMContext,Stream<LMMContext>>() {
              public Stream<LMMContext> f(final LMMContext e1_) { 
                return Stream.<LMMContext>`single(lmmDot(v_,e1_));
              }
            });
          }
        });
      }
      /* ft . e1 */
      lmmFDot(_ft,_e1) -> { 
        /* do e1_ <- eval e1
              return $ v_ `dot` e1_ */
        final Term ft = `_ft;
        final LMMContext e1 = `_e1;
          return eval(e1).bind(new F<LMMContext,Stream<LMMContext>>() {
            public Stream<LMMContext> f(final LMMContext e1_) { 
              return Stream.<LMMContext>`single(lmmFDot(ft,e1_));
            }
          });
      }
      /* (lfold[r] e1) */
      lmmFoldL(r,_e1) -> { 
        /* do e1_ <- eval e1
              return $ (lfold[r] e1_) */
        final LMMContext e1 = `_e1;
          return eval(e1).bind(new F<LMMContext,Stream<LMMContext>>() {
            public Stream<LMMContext> f(final LMMContext e1_) { 
              return Stream.<LMMContext>`single(lmmFoldL(r,e1_));
            }
          });
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static Stream<LMMTerm> eval(final LMMTerm v) {
    %match(v) {
      /* y */
      lmmVar(y) -> { return Stream.<LMMTerm>`single(lmmVar(y)); }
      /* mu a:tya . c */
      lmmMu(LmmMu(_a,_tya,_c)) -> {
        /* do c_ <- eval c
              return $ mu a:tya. c_ */
        final CoName a = `_a;
        final Prop tya = `_tya;
        final LMMCommand c = `_c;
        return eval(c).bind(new F<LMMCommand,Stream<LMMTerm>>() {
          public Stream<LMMTerm> f(final LMMCommand c_) {
            return Stream.<LMMTerm>`single(lmmMu(LmmMu(a,tya,c_)));
          }
        });
      }
      /* \y:tyy. u */
      lmmLam(LmmLam(_y,_tyy,_u)) -> { 
        /* do u_ <- eval u
              return $ \y:tyy . u_ */
        final Name y = `_y;
        final Prop tyy = `_tyy;
        final LMMTerm u = `_u;
        return eval(u).bind(new F<LMMTerm,Stream<LMMTerm>>() {
          public Stream<LMMTerm> f(final LMMTerm u_) {
            return Stream.<LMMTerm>`single(lmmLam(LmmLam(y,tyy,u_))); 
          }
        });
      }
      /* \fx . u */
      lmmFLam(LmmFLam(_fx,_u)) -> { 
        /* do u_ <- eval u
              return $ \fx: . u_ */
        final FoVar fx = `_fx;
        final LMMTerm u = `_u;
        return eval(u).bind(new F<LMMTerm,Stream<LMMTerm>>() {
          public Stream<LMMTerm> f(final LMMTerm u_) {
            return Stream.<LMMTerm>`single(lmmFLam(LmmFLam(fx,u_))); 
          }
        });
      }
      /* (rfold[r] u) */
      lmmFoldR(r,_u) -> { 
        /* do u_ <- eval u
              return $ (rfold[r] u_) */
        final LMMTerm u = `_u;
        return eval(u).bind(new F<LMMTerm,Stream<LMMTerm>>() {
          public Stream<LMMTerm> f(final LMMTerm u_) {
            return Stream.<LMMTerm>`single(lmmFoldR(r,u_)); 
          }
        });
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Stream<LMMCommand> eval(final LMMCommand c) {
    System.out.println("ici");
    %match(c) {
      /* < \x:tx.v1 | v2 . e > --> < v2 | mu~ x:tx.< v1 | e > > */
      lmmCommand(lmmLam(LmmLam(x,tx,v1)),lmmDot(v2,e)) -> { 
        return `eval(lmmCommand(v2,lmmMuT(LmmMuT(x,tx,lmmCommand(v1,e)))));
      }
      /* < \fx.v | ft . e > --> < v[fx:=ft] | e > */
      lmmCommand(lmmFLam(LmmFLam(fx,v)),lmmFDot(ft,e)) -> { 
        return `eval(lmmCommand(substFoVar(fx,ft,v),e));
      }
      /* < mu a:ta.c | e > */
      lmmCommand(v@lmmMu(LmmMu(a,_,c1)),e) -> {
        final Stream<LMMCommand> res1 = `eval(substCoVar(a,e,c1));
        %match(e) {
          /* mu~ x:tx.c2 */
          lmmMuT(LmmMuT(x,_,c2)) -> {
            final Stream<LMMCommand> res2 = `eval(substVar(x,v,c2));
            return res1.append(res2);
          }
          _ -> { return res1; }
        }
      }
      /* < (rfold[r] v) | (lfold[r] e) > --> <v | e> */
      lmmCommand(lmmFoldR(r,v),lmmFoldL(r,e)) -> {
        return `eval(lmmCommand(v,e));
      }
      /* < x | .. > or < .. | a >*/
      lmmCommand(_v,_e) -> {
        final LMMTerm v = `_v;
        final LMMContext e = `_e;
        return eval(v).bind(new F<LMMTerm,Stream<LMMCommand>>() {
          public Stream<LMMCommand> f(final LMMTerm v_) {
            return eval(e).bind(new F<LMMContext,Stream<LMMCommand>>() {
              public Stream<LMMCommand> f(final LMMContext e_) {
                return Stream.<LMMCommand>`single(lmmCommand(v_,e_));
              }
            });
          }
        });
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static LMMCommand substVar(Name x, LMMTerm t, LMMCommand c) {
    %match(c) {
      lmmCommand(v,e) -> { return `lmmCommand(substVar(x,t,v),substVar(x,t,e)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static LMMTerm substVar(Name x, LMMTerm u, LMMTerm t) {
    %match(t) {
      lmmVar(y)               -> { return `y.equals(x) ? u : t; }
      lmmMu(LmmMu(a,tya,c))   -> { return `lmmMu(LmmMu(a,tya,substVar(x,u,c))); }
      lmmLam(LmmLam(y,tyy,v)) -> { return `lmmLam(LmmLam(y,tyy,substVar(x,u,v))); }
      lmmFLam(LmmFLam(fx,v))  -> { return `lmmFLam(LmmFLam(fx,substVar(x,u,v))); }
      lmmFoldR(r,v)           -> { return `lmmFoldR(r,substVar(x,u,v)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static LMMContext substVar(Name x, LMMTerm u, LMMContext e) {
    %match(e) {
      lmmCoVar[]              -> { return e; }
      lmmMuT(LmmMuT(y,tyy,c)) -> { return `lmmMuT(LmmMuT(y,tyy,substVar(x,u,c))); }
      lmmDot(v,e1)            -> { return `lmmDot(substVar(x,u,v),substVar(x,u,e1)); }
      lmmFDot(ft,e1)          -> { return `lmmFDot(ft,substVar(x,u,e1)); }
      lmmFoldL(r,e1)          -> { return `lmmFoldL(r,substVar(x,u,e1)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static LMMCommand substCoVar(CoName a, LMMContext e, LMMCommand c) {
    %match(c) {
      lmmCommand(v,e1) -> { return `lmmCommand(substCoVar(a,e,v),substCoVar(a,e,e1)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static LMMTerm substCoVar(CoName a, LMMContext e, LMMTerm t) {
    %match(t) {
      lmmVar[]               -> { return t; }
      lmmMu(LmmMu(b,tyb,c))  -> { return `lmmMu(LmmMu(b,tyb,substCoVar(a,e,c))); }
      lmmLam(LmmLam(x,txx,v))-> { return `lmmLam(LmmLam(x,txx,substCoVar(a,e,v))); }
      lmmFLam(LmmFLam(fx,v)) -> { return `lmmFLam(LmmFLam(fx,substCoVar(a,e,v))); }
      lmmFoldR(r,v)          -> { return `lmmFoldR(r,substCoVar(a,e,v)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static LMMContext substCoVar(CoName a, LMMContext e, LMMContext e1) {
    %match(e1) {
      lmmCoVar(b)             -> { return `b.equals(a) ? e : e1; }
      lmmMuT(LmmMuT(x,txx,c)) -> { return `lmmMuT(LmmMuT(x,txx,substCoVar(a,e,c))); }
      lmmDot(v,e2)            -> { return `lmmDot(substCoVar(a,e,v),substCoVar(a,e,e2)); }
      lmmFDot(ft,e2)          -> { return `lmmFDot(ft,substCoVar(a,e,e2)); }
      lmmFoldL(r,e2)          -> { return `lmmFoldL(r,substCoVar(a,e,e2)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static LMMCommand substFoVar(FoVar fx, Term ft, LMMCommand c) {
    %match(c) {
      lmmCommand(v,e) -> { return `lmmCommand(substFoVar(fx,ft,v),substFoVar(fx,ft,e)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static LMMTerm substFoVar(FoVar fx, Term ft, LMMTerm t) {
    %match(t) {
      lmmVar[]               -> { return t; }
      lmmMu(LmmMu(b,tyb,c))  -> { return `lmmMu(LmmMu(b,U.substFoVar(tyb,fx,ft),substFoVar(fx,ft,c))); }
      lmmLam(LmmLam(x,txx,v))-> { return `lmmLam(LmmLam(x,U.substFoVar(txx,fx,ft),substFoVar(fx,ft,v))); }
      lmmFLam(LmmFLam(fy,v)) -> { return `lmmFLam(LmmFLam(fy,substFoVar(fx,ft,v))); }
      lmmFoldR(r,v)          -> { return `lmmFoldR(r,substFoVar(fx,ft,v)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static LMMContext substFoVar(FoVar fx, Term ft, LMMContext e) {
    %match(e) {
      lmmCoVar[]              -> { return e; }
      lmmMuT(LmmMuT(x,txx,c)) -> { return `lmmMuT(LmmMuT(x,U.substFoVar(txx,fx,ft),substFoVar(fx,ft,c))); }
      lmmDot(v,e1)            -> { return `lmmDot(substFoVar(fx,ft,v),substFoVar(fx,ft,e1)); }
      lmmFDot(ft1,e1)         -> { return `lmmFDot(U.substFoVar(ft1,fx,ft),substFoVar(fx,ft,e1)); }
      lmmFoldL(r,e1)          -> { return `lmmFoldL(r,substFoVar(fx,ft,e1)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static void main(String[] args) {
    RawProp pa = `RawrelApp("A",RawtermList());
    RawProp pb = `RawrelApp("B",RawtermList());
    RawProp pc = `RawrelApp("C",RawtermList());
    RawProp pd = `RawrelApp("D",RawtermList());
    RawLMMCommand c = `RawlmmCommand(
                         RawlmmMu(RawLmmMu("a",pc,
                           RawlmmCommand(RawlmmVar("y"),RawlmmCoVar("b")))),
                         RawlmmMuT(RawLmmMuT("x",pd,
                           RawlmmCommand(RawlmmVar("z"),RawlmmCoVar("g")))));
    RawLMMTerm t = `RawlmmLam(RawLmmLam("y",pa,
                      RawlmmMu(RawLmmMu("b",pa,
                         RawlmmCommand(
                           RawlmmLam(RawLmmLam("z",pb,
                             RawlmmMu(RawLmmMu("g",pb,c)))),
                           RawlmmCoVar("b"))))));
    System.out.println(lemu2.util.Pretty.pretty(t));
    Collection<LMMTerm> res = eval(t.convert()).toCollection();
    for (LMMTerm x: res) System.out.println(lemu2.util.Pretty.pretty(x.export()));
  }

  %strategy MuMuTEta() extends Fail() {
    visit LMMTerm {
      lmmMu(LmmMu(a,_,lmmCommand(V,lmmCoVar(a)))) -> {
        if (! U.`freeIn(a,V)) return `V;
      }
    }
    visit LMMContext {
      lmmMuT(LmmMuT(x,_,lmmCommand(lmmVar(x),E))) -> {
        if (! U.`freeIn(x,E)) return `E;
      }
    }
  }

  public static LMMTerm eta(LMMTerm t) {
    try { return `Innermost(MuMuTEta()).visit(t); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  public static Stream<LMMTerm> eta(Stream<LMMTerm> ts) {
    return ts.map(new F<LMMTerm,LMMTerm>() { 
        public LMMTerm f(LMMTerm t) { return eta(t); } 
      });
  }
}

