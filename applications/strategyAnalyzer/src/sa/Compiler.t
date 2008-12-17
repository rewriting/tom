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
  private static int phiNumber = 0;
  private static String getName() {
    return "phi" + (phiNumber++);
  }

  /*
   * Compile a strategy into a rewrite system
   */
  public static void compile(ExpressionList expl) {
    %match(expl) {
      ExpressionList(_*,x,_*) -> {
        Map<String,Integer> sig = new HashMap<String,Integer>();
        Collection<Rule> bag = new ArrayList<Rule>();
        compileExp(bag,sig,`x);
        for(Rule r:bag) {
          System.out.println(Pretty.toString(r));
        }
      }
    }
  }

  private static void compileExp(Collection<Rule> bag, Map<String,Integer> sig, Expression e) {
    //System.out.println("exp = " + e);
    %match(e) {
      Let(v,Signature(sl),body) -> {
        %match(sl) {
          SymbolList(_*,Symbol(name,arity),_*) -> {
            sig.put(`name,`arity);
          }
        }
        System.out.println("sig = " + sig);
        compileExp(bag,sig,`body);
      }

      Strat(s) -> {
        compileStrat(bag,sig,`s);
      }
    }
  }

  /*
   * compile a strategy
   * return the name of the top symbol (phi) introduced
   */
  private static String compileStrat(Collection<Rule> bag, Map<String,Integer> sig, Strat strat) {
    %match(strat) {
      StratRule(Rule(lhs,rhs)) -> {
        String phi = getName();
        bag.add(`Rule(Appl(phi,TermList(lhs)),rhs));
        return phi;
      }

      StratMu(name,s) -> {
        try {
          String phi_x = getName();
          Strat newStrat = `TopDown(ReplaceMuVar(name,phi_x)).visitLight(`s);
          String phi_s = compileStrat(bag,sig,newStrat);
          bag.add(`Rule(Appl(phi_x,TermList(Var("x"))),Appl(phi_s,TermList(Var("x")))));
          bag.add(`Rule(Appl(phi_x,TermList(BOTTOM)),Appl(phi_s,TermList(BOTTOM))));
          return phi_s;
        } catch(VisitFailure e) {
          System.out.println("failure in StratMu on: " + `s);
        }
      }

      StratIdentity() -> {
        String phi = getName();
        bag.add(`Rule(Appl(phi,TermList(Var("x"))),Var("x")));
        return phi;
      }

      StratFail() -> {
        String phi = getName();
        bag.add(`Rule(Appl(phi,TermList(Var("x"))),BOTTOM));
        return phi;
      }

      StratSequence(s1,s2) -> {
        String phi_s1 = compileStrat(bag,sig,`s1);
        String phi_s2 = compileStrat(bag,sig,`s2);
        String phi = getName();
        bag.add(`Rule(Appl(phi,TermList(Var("x"))),
              Appl(phi_s2,TermList(Appl(phi_s1,TermList(Var("x")))))));
        return phi;
      }

      StratChoice(s1,s2) -> {
        String phi_s1 = compileStrat(bag,sig,`s1);
        String phi_s2 = compileStrat(bag,sig,`s2);
        String phi = getName();
        String phi2 = getName();
        bag.add(`Rule(Appl(phi,TermList(Var("x"))),
              Appl(phi2,TermList(Appl(phi_s1,TermList(Var("x"))), Var("x")))));
        bag.add(`Rule(Appl(phi2,TermList(BOTTOM,Var("x"))),
              Appl(phi_s2,TermList(Var("x")))));
        bag.add(`Rule(Appl(phi2,TermList(Anti(BOTTOM),Var("x"))),
              Appl(phi_s1,TermList(Var("x")))));
        return phi;
      }

      StratAll(s) -> {
        String phi_s = compileStrat(bag,sig,`s);
        String phi = getName();
        Map<Integer,String> mapphi = new HashMap<Integer,String>();

        Iterator<String> it = sig.keySet().iterator();
        while(it.hasNext()) {
          String name = it.next();
          int arity = sig.get(name);
          String phi_n = mapphi.get(arity);
          if(phi_n == null) {
            //phi_n = getName();
            phi_n = phi+"_"+arity;
            mapphi.put(arity,phi_n);
            if(arity>0) {
              // generate success rules
              // phi_n(!BOTTOM,...,!BOTTOM,x) -> x
              TermList args = `TermList(Var("x"));
              for(int i=1 ; i<=arity ; i++) {
                args = `TermList(Anti(BOTTOM),args*);
              }
              bag.add(`Rule(Appl(phi_n,args),Var("x")));
              // generate failure rules
              // phi_n(BOTTOM,_,...,_,x) -> BOTTOM
              // phi_n(...,BOTTOM,...,x) -> BOTTOM
              // phi_n(_,...,_,BOTTOM,x) -> BOTTOM
              for(int i=1 ; i<=arity ; i++) {
                TermList diag = `TermList(Var("x"));
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
            // phi(f(x1,...,xn)) -> phi_n(phi(x1),...,phi(xn), 
            //                       f(phi_s(x1),...,phi_s(xn)))
            TermList args_x = `TermList();
            TermList args_phi = `TermList();
            TermList args_phi_s = `TermList();
            for(int i=arity ; i>0 ; i--) {
              args_x = `TermList(Var("x"+i),args_x*);
              args_phi = `TermList(Appl(phi,TermList(Var("x"+i))),args_phi*);
              args_phi_s = `TermList(Appl(phi_s,TermList(Var("x"+i))),args_phi_s*);
            }
            bag.add(`Rule(Appl(phi,TermList(Appl(name,args_x))),
                  Appl(phi_n,TermList(args_phi*,Appl(name,args_phi_s)))));
          }
        }

        return phi;
      }

      StratOne(s) -> {
        String phi_s = compileStrat(bag,sig,`s);
        String phi = getName();
        Map<Integer,String> mapphi = new HashMap<Integer,String>();

        Iterator<String> it = sig.keySet().iterator();
        while(it.hasNext()) {
          String name = it.next();
          int arity = sig.get(name);
          String phi_n = mapphi.get(arity);
          if(phi_n == null) {
            //phi_n = getName();
            phi_n = phi+"_"+arity;
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
          // phi(f(x1,...,xn)) -> phi_n(phi(x1),...,phi(xn),
          //                f(phi_s(x1),...,xn),...,f(x1,...,phi_s(xn)))
            TermList args_x = `TermList();
            TermList args_phi = `TermList();
            TermList args_f = `TermList();
            for(int i=arity ; i>0 ; i--) {
              args_x = `TermList(Var("x"+i),args_x*);
              args_phi = `TermList(Appl(phi,TermList(Var("x"+i))),args_phi*);
              TermList args_phi_s = `TermList();
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
                  Appl(phi_n,TermList(args_phi*,args_f*))));
          }
        }

        return phi;
      }


      // mu fix point: transform the startame into a function call
      StratName(name) -> {
        return `name;
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