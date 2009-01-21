package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;

public class Compiler {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Collection.tom }
  %include { java/util/types/Map.tom }

  private final static Term BOTTOM = `Appl("Bottom",TermList());
  private final static Term X = `Var("x");

  private static int phiNumber = 0;
  private static String getName(String name) {
//     return "phi" + (phiNumber++);
    return name + (phiNumber++);
  }

  private static String topName = "";
  public static String getTopName() {
    return topName;
  }

  /*
   * Compile a strategy into a rewrite system
   */
  public static void compile(Collection<Rule> bag, Map<String,Integer> origsig, Map<String,Integer> sig, ExpressionList expl) {
    %match(expl) {
      ExpressionList(_*,x,_*) -> {
        compileExp(bag,origsig,sig,`x);
      }
    }
  }

  private static void compileExp(Collection<Rule> bag, Map<String,Integer> origsig, Map<String,Integer> sig, Expression e) {
    //System.out.println("exp = " + e);
    %match(e) {
      Let(v,Signature(sl),body) -> {
        %match(sl) {
          SymbolList(_*,Symbol(name,arity),_*) -> {
            origsig.put(`name,`arity);
            sig.put(`name,`arity);
          }
        }
        //System.out.println("Original sig= " + origsig);
        compileExp(bag,origsig,sig,`body);
      }

      Strat(s) -> {
        String start = compileStrat(bag,origsig,sig,`s);
        System.out.println("// start: " + start);
        topName = start;
      }
    }
  }

  /*
   * compile a strategy
   * return the name of the top symbol (phi) introduced
   */
  private static String compileStrat(Collection<Rule> bag, Map<String,Integer> origsig, Map<String,Integer> sig, Strat strat) {
//     System.out.println("sig = " + sig);
//     System.out.println("strat = " + strat);

    %match(strat) {
      StratRule(Rule(lhs,rhs)) -> {
        String phi = getName("rule");
        sig.put(phi,1);
        bag.add(`Rule(Appl(phi,TermList(lhs)),rhs));
        bag.add(`Rule(Appl(phi,TermList(Anti(lhs))),BOTTOM));
        return phi;
      }

      /*
       * TODO: fix non confluence here
       */
      StratMu(name,s) -> {
        try {
          String phi_x = getName("mu");
          String phi2_x = getName("mu");
          sig.put(phi_x,1);
          sig.put(phi2_x,2);
          Strat newStrat = `TopDown(ReplaceMuVar(name,phi_x)).visitLight(`s);
          String phi_s = compileStrat(bag,origsig,sig,newStrat);
          bag.add(`Rule(Appl(phi_x,TermList(X)),Appl(phi2_x,TermList(X,X))));
          bag.add(`Rule(Appl(phi2_x,TermList(Anti(BOTTOM),X)),Appl(phi_s,TermList(X))));
          bag.add(`Rule(Appl(phi2_x,TermList(BOTTOM,X)),BOTTOM));
          return phi_s;
        } catch(VisitFailure e) {
          System.out.println("failure in StratMu on: " + `s);
        }
      }

      // mu fix point: transform the startame into a function call
      StratName(name) -> {
        return `name;
      }

      StratIdentity() -> {
        String phi = getName("id");
        sig.put(phi,1);
        bag.add(`Rule(Appl(phi,TermList(X)),X));
        return phi;
      }

      StratFail() -> {
        String phi = getName("fail");
        sig.put(phi,1);
        bag.add(`Rule(Appl(phi,TermList(X)),BOTTOM));
        return phi;
      }

      StratSequence(s1,s2) -> {
        String phi_s1 = compileStrat(bag,origsig,sig,`s1);
        String phi_s2 = compileStrat(bag,origsig,sig,`s2);
        String phi = getName("seq");
        sig.put(phi,1);
        bag.add(`Rule(Appl(phi,TermList(X)),
              Appl(phi_s2,TermList(Appl(phi_s1,TermList(X))))));
        return phi;
      }

      StratChoice(s1,s2) -> {
        String phi_s1 = compileStrat(bag,origsig,sig,`s1);
        String phi_s2 = compileStrat(bag,origsig,sig,`s2);
        String phi = getName("choice");
        String phi2 = getName("choice");
        sig.put(phi,1);
        sig.put(phi2,2);
        bag.add(`Rule(Appl(phi,TermList(X)),
              Appl(phi2,TermList(Appl(phi_s1,TermList(X)), X))));
        bag.add(`Rule(Appl(phi2,TermList(BOTTOM,X)),
              Appl(phi_s2,TermList(X))));
        bag.add(`Rule(Appl(phi2,TermList(Anti(BOTTOM),X)),
              Appl(phi_s1,TermList(X))));
        return phi;
      }

      StratAll(s) -> {
        String phi_s = compileStrat(bag,origsig,sig,`s);
        String phi = getName("all");
        sig.put(phi,1);
        Map<Integer,String> mapphi = new HashMap<Integer,String>();

        Iterator<String> it = origsig.keySet().iterator();
        while(it.hasNext()) {
          String name = it.next();
          int arity = sig.get(name);
          String phi_n = mapphi.get(arity);
          if(phi_n == null) {
            //phi_n = getName();
            phi_n = phi+"_"+arity;
            sig.put(phi_n,arity+1);
            mapphi.put(arity,phi_n);
            if(arity>0) {
              // generate success rules
              // phi_n(!BOTTOM,...,!BOTTOM,x) -> x
              TermList args = `TermList(X);
              for(int i=1 ; i<=arity ; i++) {
                args = `TermList(Anti(BOTTOM),args*);
              }
              bag.add(`Rule(Appl(phi_n,args),X));
              // generate failure rules
              // phi_n(BOTTOM,_,...,_,x) -> BOTTOM
              // phi_n(...,BOTTOM,...,x) -> BOTTOM
              // phi_n(_,...,_,BOTTOM,x) -> BOTTOM
              for(int i=1 ; i<=arity ; i++) {
                TermList diag = `TermList(X);
                for(int j=arity ; j>0 ; j--) {
                  if(i==j) {
                    diag = `TermList(BOTTOM,diag*);
                  } else {
                    diag = `TermList(Var("y"+j),diag*);
                  }
                }
                bag.add(`Rule(Appl(phi_n,diag),BOTTOM));
              }
            }
          }
          // generate congruence rules
          if(arity==0) {
            bag.add(`Rule(Appl(phi,TermList(Appl(name,TermList()))),
                          Appl(name,TermList())));

          } else {
            // phi(f(x1,...,xn)) -> phi_n(phi_s(x1),...,phi_s(xn), 
            //                       f(phi_s(x1),...,phi_s(xn)))
            TermList args_x = `TermList();
            TermList args_phi_s = `TermList();
            for(int i=arity ; i>0 ; i--) {
              args_x = `TermList(Var("x"+i),args_x*);
              args_phi_s = `TermList(Appl(phi_s,TermList(Var("x"+i))),args_phi_s*);
            }
            bag.add(`Rule(Appl(phi,TermList(Appl(name,args_x))),
                  Appl(phi_n,TermList(args_phi_s*,Appl(name,args_phi_s)))));
          }
        }

        return phi;
      }

      StratOne(s) -> {
        String phi_s = compileStrat(bag,origsig,sig,`s);
        String phi = getName("one");
        sig.put(phi,1);
        Map<Integer,String> mapphi = new HashMap<Integer,String>();
        Iterator<String> it = origsig.keySet().iterator();
        while(it.hasNext()) {
          String name = it.next();
          int arity = sig.get(name);
          String phi_n = mapphi.get(arity);
          if(phi_n == null) {
            //phi_n = getName();
            phi_n = phi+"_"+arity;
            sig.put(phi_n,2*arity);
            mapphi.put(arity,phi_n);
            if(arity>0) {
              // generate failure rules
              // phi_n(BOTTOM,...,BOTTOM,x1,...,xn) -> BOTTOM
              TermList args = `TermList();
              for(int i=arity ; i>0 ; i--) {
                args = `TermList(Var("x"+i),args*);
              }
              for(int i=0 ; i<arity ; i++) {
                args = `TermList(BOTTOM,args*);
              }
              bag.add(`Rule(Appl(phi_n,args),BOTTOM));

              // generate success rules
              // phi_n(!BOTTOM,...,yn,x1,...,xn) -> x1
              // phi_n(y1,...,!BOTTOM,x1,...,xn) -> xn
              for(int i=1 ; i<=arity ; i++) {
                TermList diag = `TermList();
                for(int j=arity ; j>0 ; j--) {
                  diag = `TermList(Var("x"+j),diag*);
                }
                for(int j=arity ; j>0 ; j--) {
                  if(i==j) {
                    diag = `TermList(Anti(BOTTOM),diag*);
                  } else {
                    diag = `TermList(Var("y"+j),diag*);
                  }
                }
                bag.add(`Rule(Appl(phi_n,diag),Var("x"+i)));
              }
            }
          }
          // generate congruence rules
          if(arity==0) {
            bag.add(`Rule(Appl(phi,TermList(Appl(name,TermList()))),
                          BOTTOM));

          } else {
          // phi(f(x1,...,xn)) -> phi_n(phi_s(x1),...,phi_s(xn),
          //                f(phi_s(x1),...,xn),...,f(x1,...,phi_s(xn)))
            TermList args_x = `TermList(); // x1,...,xn
            TermList args_phi_s_xi = `TermList(); // phi_s(x1),...,phi_s(xn)
            TermList args_f = `TermList(); // f(...),...,f(...)
            for(int i=arity ; i>0 ; i--) {
              args_x = `TermList(Var("x"+i),args_x*);
              args_phi_s_xi = `TermList(Appl(phi_s,TermList(Var("x"+i))),args_phi_s_xi*);
              TermList args_phi_s = `TermList(); // phi_s(x1),x2,...,xn or x1,phi_s(x2),...,xn
              for(int j=arity ; j>0 ; j--) {
                if(i==j) {
                  args_phi_s = `TermList(Appl(phi_s,TermList(Var("x"+i))),args_phi_s*);
                } else {
                  args_phi_s = `TermList(Var("x"+j),args_phi_s*);
                }
              }
                args_f = `TermList(Appl(name,args_phi_s),args_f*);
            }
            bag.add(`Rule(Appl(phi,TermList(Appl(name,args_x))),
                  Appl(phi_n,TermList(args_phi_s_xi*,args_f*))));
          }
        }

        return phi;
      }



    }
    return strat.toString();
  }

  %strategy ReplaceMuVar(name:String, appl:String) extends Identity() {
    visit Strat {
      StratName(n) && n==name -> {
        return `StratName(appl);
      }
    }
  }

  /*
   * Transforms Let(name,exp,body) into body[name/exp]
   */
  public static ExpressionList expand(ExpressionList expl) {
    try {
      return `RepeatId(TopDown(Expand())).visitLight(expl);
    } catch(VisitFailure e) {
      System.out.println("failure on: " + e);
    }
    return expl;
  }

  %strategy Expand() extends Identity() {
    visit Expression {
      Let(name,exp@(Strat|Set)[],body) -> {
        return `TopDown(Replace(name,exp)).visitLight(`body);
      }
    }
  }

  %strategy Replace(name:String, exp:Expression) extends Identity() {
    visit Strat {
      StratName(n) && n==name -> {
        return `StratExp(exp);
      }
    }
  }

}
