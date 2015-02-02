package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;

public class Compiler {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Collection.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/HashSet.tom }

  private static Tools tools = new Tools();
  private static Pretty pp = new Pretty();

  private static int phiNumber = 0;
  private static String getName(String name) {
    return name + (phiNumber++);
  }

  /**
   * getTopName
   * @return the name of the strategy which starts the computation
   */
  private static String topName = "";
  public static String getTopName() {
    return topName;
  }

  /*
   * Compile a strategy into a rewrite system
   */
  public static void compile(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, ExpressionList expl) {
    generatedSignature.put("True",0);
    generatedSignature.put("False",0);
    generatedSignature.put("and",2);
    generatedSignature.put("eq",2);

    generatedSignature.put("Bottom",1);
    if(Main.options.generic) {
      generatedSignature.put("BottomList",1);
      generatedSignature.put("Appl",2);
      generatedSignature.put("Cons",2);
      generatedSignature.put("Nil",0);
    }

    %match(expl) {
      ExpressionList(_*,x,_*) -> {
        compileExp(bag,extractedSignature,generatedSignature,`x);
      }
    }

    if(Main.options.generic) {
      // do nothing
    } else {
      generateEquality(bag, extractedSignature, generatedSignature);
    }
  }


  private static void compileExp(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, Expression e) {
    //System.out.println("exp = " + e);
    %match(e) {
      Let(_,Signature(sl),body) -> {
        %match(sl) {
          SymbolList(_*,Symbol(name,arity),_*) -> {
            extractedSignature.put(`name,`arity);
            generatedSignature.put(`name,`arity);
          }
        }
        //System.out.println("Original generatedSignature= " + generatedSignature);
        compileExp(bag,extractedSignature,generatedSignature,`body);
      }

      Strat(s) -> {
        String start = "";
        if(Main.options.generic) {
          start = compileGenericStrat(bag,extractedSignature,generatedSignature,`s);
        } else {
          start = compileStrat(bag,extractedSignature,generatedSignature,`s);
        }
        topName = start;
      }
    }
  }

  /**
   * compile a strategy in a classical way (without using meta-representation)
   * return the name of the top symbol (phi) introduced
   * @param bag set of rule that is extended by compilation
   * @param extractedSignature associates arity to a name, for all constructors of the initial strategy
   * @param generatedSignature associates arity to a name, for all generated defined symbols
   * @param strat the strategy to compile
   * @return the name of the last compiled strategy
   */
  private static String compileStrat(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, Strat strat) {
    String X = getName("X");
    String Y = getName("Y");
    String Z = getName("Z");
    Term varX = tools.encode(X,generatedSignature);
    Term botX = tools.encode("Bottom("+X+")",generatedSignature);
    Term True = tools.encode("True",generatedSignature);
    Term False = tools.encode("False",generatedSignature);

    %match(strat) {
      StratExp(Set(rulelist)) -> {
        String rule = getName("rule");
        generatedSignature.put(rule,1);
        String cr = getName("crule");
        generatedSignature.put(cr,2);
        /*
         * lhs -> rhs becomes
         * in the linear case:
         *   rule(lhs) -> rhs
         *   rule(X@!lhs) -> Bottom(X)
         * in the non-linear case:
         *   rule(X@linear-lhs) -> rule'(X, true ^ constraint on non linear variables)
         *   rule(X@!linear-lhs) -> Bottom(X)
         *   rule'(linear-lhs, true) -> rhs
         *   rule'(X@linear-lhs, false) -> Bottom(x)
         */
        %match(rulelist) {
          RuleList(_*,Rule(lhs,rhs),_*) -> {
            // use AST-syntax because lhs and rhs are already encoded
            // propagate failure; if the rule is applied to the result of a strategy that failed then the result is a failure
            bag.add(`Rule(Appl(rule,TermList(botX)),botX));

            TermList result = linearize(`lhs,generatedSignature);
            %match(result) {
              TermList(linearlhs, cond) -> {
                bag.add(`Rule(Appl(rule,TermList(At(varX,linearlhs))),
                              Appl(cr, TermList(varX, cond))));
                bag.add(`Rule(Appl(rule,TermList(At(varX,Anti(linearlhs)))),botX));

                bag.add(`Rule(Appl(cr,TermList(linearlhs, True)), rhs));
                bag.add(`Rule(Appl(cr,TermList(At(varX,linearlhs), False)),botX));
              }
            }
            //System.out.println(cr);
          }
        }
        return rule;
      }

      /*
       * TODO: fix non confluence here
       */
      StratMu(name,s) -> {
        try {
          String mu = getName("mu");
          generatedSignature.put(mu,1);
          Strat newStrat = `TopDown(ReplaceMuVar(name,mu)).visitLight(`s);
          String phi_s = compileStrat(bag,extractedSignature,generatedSignature,newStrat);
          bag.add(tools.encodeRule(%[rule(@mu@(at(@X@,anti(Bottom(@Y@)))), @phi_s@(@X@))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@mu@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
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
        String id = getName("id");
        generatedSignature.put(id,1);
        if( !Main.options.approx ) {
          // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
          // normally it will follow reduction in original TRS
          bag.add(tools.encodeRule(%[rule(@id@(at(@X@,anti(Bottom(@Y@)))), @X@)]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@id@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        } else { 
          bag.add(tools.encodeRule(%[rule(@id@(@X@), @X@)]%,generatedSignature));
          // Bottom of Bottom is Bottom
          // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
          bag.add(tools.encodeRule(%[rule(Bottom(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        }
        return id;
      }

      StratFail() -> {
        String fail = getName("fail");
        generatedSignature.put(fail,1);
        if( !Main.options.approx ) {
          // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
          // normally it will follow reduction in original TRS
          bag.add(tools.encodeRule(%[rule(@fail@(at(X,anti(Bottom(@Y@)))), Bottom(X))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@fail@(Bottom(X)), Bottom(X))]%,generatedSignature));
        } else { 
          bag.add(tools.encodeRule(%[rule(@fail@(X), Bottom(X))]%,generatedSignature));
          // Bottom of Bottom is Bottom
          // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
          bag.add(tools.encodeRule(%[rule(Bottom(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        }
        return fail;
      }

      StratSequence(s1,s2) -> {
        String n1 = compileStrat(bag,extractedSignature,generatedSignature,`s1);
        String n2 = compileStrat(bag,extractedSignature,generatedSignature,`s2);
        String seq = getName("seq");
        String seq2 = getName("seq2");
        generatedSignature.put(seq,1);
        generatedSignature.put(seq2,2);
        if( !Main.options.approx ) {
          // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
          // normally it will follow reduction in original TRS
          bag.add(tools.encodeRule(%[rule(@seq@(at(@X@,anti(Bottom(@Y@)))), @seq2@(@n2@(@n1@(@X@)),@X@))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@seq@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        } else { 
          bag.add(tools.encodeRule(%[rule(@seq@(@X@), @seq2@(@n2@(@n1@(@X@)),@X@))]%,generatedSignature));
          // Bottom of Bottom is Bottom
          // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
          bag.add(tools.encodeRule(%[rule(Bottom(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        }
        bag.add(tools.encodeRule(%[rule(@seq2@(at(@X@,anti(Bottom(@Y@))),@Z@), @X@)]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@seq2@(Bottom(@Y@),@X@), Bottom(@X@))]%,generatedSignature));
        return seq;
      }

      // TODO [20/01/2015]: see if not exact is interesting
      StratChoice(s1,s2) -> {
        String n1 = compileStrat(bag,extractedSignature,generatedSignature,`s1);
        String n2 = compileStrat(bag,extractedSignature,generatedSignature,`s2);
        String choice = getName("choice");
        String choice2 = getName("choice");
        generatedSignature.put(choice,1);
        generatedSignature.put(choice2,1);
        bag.add(tools.encodeRule(%[rule(@choice@(at(@X@,anti(Bottom(@Y@)))),  @choice2@(@n1@(@X@)) )]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@choice@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@choice2@(at(@X@,anti(Bottom(@Y@)))), @X@)]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@choice2@(Bottom(@X@)), @n2@(@X@))]%,generatedSignature));
        return choice;
      }

      StratAll(s) -> {
        String phi_s = compileStrat(bag,extractedSignature,generatedSignature,`s);
        String all = getName("all");
        generatedSignature.put(all,1);
        for(String name : extractedSignature.keySet()) {
          int arity = generatedSignature.get(name);
          int arity_all = arity+1;
          if(arity==0) {
            bag.add(tools.encodeRule(%[rule(@all@(@name@), @name@)]%,generatedSignature));
          } else {
            String all_n = all+"_"+name;
            generatedSignature.put(all_n,arity_all);
            {
              // main case
              // all(f(x1,...,xn)) -> all_n(phi_s(x1),phi_s(x2),...,phi_s(xn),f(x1,...,xn))
              String lx = "X1"; 
              String rx = phi_s+"(X1)"; 
              for(int i=2 ; i<=arity ; i++) {
                lx += ",X"+i;
                rx += ","+phi_s+"(X"+i+")";
              }
              bag.add(tools.encodeRule(%[rule(@all@(@name@(@lx@)), @all_n@(@rx@,@name@(@lx@)))]%,generatedSignature)); 
              // propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
              bag.add(tools.encodeRule(%[rule(@all@(Bottom(@X@)), Bottom(@X@) )]%,generatedSignature));               
            }

            // generate success rules
            // all_g(x1@!BOTTOM,...,xN@!BOTTOM,_) -> g(x1,...,xN)
            String lx = "at(X1,anti(Bottom(Y1)))";
            String rx = "X1"; 
            for(int j=2; j<=arity;j++) {
              lx += ",at(X"+j+",anti(Bottom(Y"+j+")))";
              rx += ",X"+j;
            }
            bag.add(tools.encodeRule(%[rule(@all_n@(@lx@,@Z@), @name@(@rx@))]%,generatedSignature));

            // generate failure rules
            // phi_n(BOTTOM,_,...,_,x) -> BOTTOM
            // phi_n(...,BOTTOM,...,x) -> BOTTOM
            // phi_n(_,...,_,BOTTOM,x) -> BOTTOM
            for(int i=1 ; i<=arity ; i++) {
              String llx = (i==1)?"Bottom(X1)":"X1";
              for(int j=2; j<=arity;j++) {
                if(j==i) {
                  llx += ",Bottom(X"+j+")";
                } else {
                  llx += ",X"+j;
                }
              }
              bag.add(tools.encodeRule(%[rule(@all_n@(@llx@,@Z@), Bottom(@Z@))]%,generatedSignature));
            }
          }
        }        
        return all;
      }

      StratOne(s) -> {
        String phi_s = compileStrat(bag,extractedSignature,generatedSignature,`s);
        String one = getName("one");
        generatedSignature.put(one,1);
        for(String name : extractedSignature.keySet()) {
          int arity = generatedSignature.get(name);
          if(arity==0) {
            bag.add(tools.encodeRule(%[rule(@one@(@name@), Bottom(@name@))]%,generatedSignature));
          } else {
            String one_n = one+"_"+name;

            {
              // main case
              // one(f(x1,...,xn)) -> one_n_1(phi_s(x1),x2,...,xn)
              String lx = "X1"; 
              String rx = phi_s+"(X1)"; 
              for(int i=2 ; i<=arity ; i++) {
                lx += ",X"+i;
                rx += ",X"+i;
              }
              bag.add(tools.encodeRule(%[rule(@one@(@name@(@lx@)), @one_n@_1(@rx@))]%,generatedSignature));
              // propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
              bag.add(tools.encodeRule(%[rule(@one@(Bottom(@X@)), Bottom(@X@) )]%,generatedSignature));               
            }

            for(int i=1 ; i<=arity ; i++) {
              String one_n_i = one_n + "_"+i;
              generatedSignature.put(one_n_i,arity);
              if(i<arity) {
                // one_f_i(Bottom(x1),...,Bottom(xi),xj,...,xn)
                // -> one_f_(i+1)(Bottom(x1),...,Bottom(xi),phi_s(x_i+1),...,xn)
                String lx = "Bottom(X1)";
                String rx = "Bottom(X1)";
                for(int j=2; j<=arity;j++) {
                  if(j<=i) {
                    lx += ",Bottom(X"+j+")";
                    rx += ",Bottom(X"+j+")";
                  } else if(j==i+1) {
                    lx += ",X"+j;
                    rx += ","+phi_s+"(X"+j+")";
                  } else {
                    lx += ",X"+j;
                    rx += ",X"+j;
                  }
                }
                String one_n_ii = one_n + "_"+(i+1);
                generatedSignature.put(one_n_ii,arity);
                bag.add(tools.encodeRule(%[rule(@one_n_i@(@lx@), @one_n_ii@(@rx@))]%,generatedSignature));
              } else {
                // one_f_n(Bottom(x1),...,Bottom(xn)) -> Bottom(f(x1,...,xn))
                String lx = "Bottom(X1)";
                String rx = "X1";
                for(int j=2; j<=arity;j++) {
                  lx += ",Bottom(X"+j+")";
                  rx += ",X"+j;
                }
                bag.add(tools.encodeRule(%[rule(@one_n_i@(@lx@), Bottom(@name@(@rx@)))]%,generatedSignature));
              }

              // one_f_i(Bottom(x1),...,xi@!Bottom(_),xj,...,xn)
              // -> f(x1,...,xi,...,xn)
              String lx = (i==1)?%[at(X1,anti(Bottom(@Y@)))]%:%[Bottom(X1)]%;
              String rx = "X1";
              for(int j=2; j<=arity;j++) {
                if(j<i) {
                  lx += ",Bottom(X"+j+")";
                  rx += ",X"+j;
                } else if(j==i) {
                  lx += ",at(X"+j+",anti(Bottom("+Y+")))";
                  rx += ",X"+j;
                } else {
                  lx += ",X"+j;
                  rx += ",X"+j;
                }
              }
              bag.add(tools.encodeRule(%[rule(@one_n_i@(@lx@), @name@(@rx@))]%,generatedSignature));

            }
          }
        }
        return one;
      }

    }
    return strat.toString();
  }

  /**
   * compile a strategy in a generic way (using a meta-representation)
   * return the name of the top symbol (phi) introduced
   * @param bag set of rule that is extended by compilation
   * @param extractedSignature associates arity to a name, for all constructor of the initial strategy
   * @param generatedSignature associates arity to a name, for all generated defined symbols
   * @param strat the strategy to compile
   * @return the name of the last compiled strategy
   */
  private static String compileGenericStrat(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, Strat strat) {
    String X = getName("X");
    String Y = getName("Y");
    String Z = getName("Z");
    String Z0 = getName("Z0");
    String Z1 = getName("Z1");
    String Z2 = getName("Z2");
    Term varX = tools.encode(X,generatedSignature);
    Term botX = tools.encode("Bottom("+X+")",generatedSignature);
    Term True = tools.encode("True",generatedSignature);
    Term False = tools.encode("False",generatedSignature);

    %match(strat) {
      StratExp(Set(rulelist)) -> {
        String rule = getName("rule");
        generatedSignature.put(rule,1);
        %match(rulelist) {
          RuleList(_*,Rule(lhs,rhs),_*) -> {
            // use AST-syntax because lhs and rhs are already encoded
            //System.out.println("lhs = " + `lhs);
            //System.out.println("encode lhs = " + tools.metaEncodeConsNil(`lhs,generatedSignature));
            //System.out.println("rhs = " + `rhs);
            //System.out.println("encode rhs = " + tools.metaEncodeConsNil(`rhs,generatedSignature));
            //System.out.println("decode(encode rhs) = " + tools.decodeConsNil(tools.metaEncodeConsNil(`rhs,generatedSignature)));
            bag.add(`Rule(Appl(rule,TermList(tools.metaEncodeConsNil(lhs,generatedSignature))),tools.metaEncodeConsNil(rhs,generatedSignature)));

            // TODO: non-linear anti-pattern
            bag.add(`Rule(Appl(rule,TermList(At(varX,Anti(tools.metaEncodeConsNil(lhs,generatedSignature))))),botX));
          }
        }

        for(String name:extractedSignature.keySet()) {
          // add symb_a(), symb_b(), symb_f(), symb_g() in the signature
          generatedSignature.put("symb_"+name,0);
        }
        return rule;
      }

      /*
       * TODO: fix non confluence here
       */
      StratMu(name,s) -> {
        try {
          String mu = getName("mu");
          generatedSignature.put(mu,1);
          Strat newStrat = `TopDown(ReplaceMuVar(name,mu)).visitLight(`s);
          String phi_s = compileGenericStrat(bag,extractedSignature,generatedSignature,newStrat);
          bag.add(tools.encodeRule(%[rule(@mu@(Appl(@Y@,@Z@)), @phi_s@(Appl(@Y@,@Z@)))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@mu@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
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
        String id = getName("id");
        generatedSignature.put(id,1);
        if( !Main.options.approx ) {
          bag.add(tools.encodeRule(%[rule(@id@(Appl(@X@,@Y@)), Appl(@X@,@Y@))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@id@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        } else {
          bag.add(tools.encodeRule(%[rule(@id@(@X@), @X@)]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(Bottom(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        }
        return id;
      }

      StratFail() -> {
        String fail = getName("fail");
        generatedSignature.put(fail,1);
        if( !Main.options.approx ) {
          bag.add(tools.encodeRule(%[rule(@fail@(Appl(@X@,@Y@)), Bottom(Appl(@X@,@Y@)))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@fail@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        } else { 
          bag.add(tools.encodeRule(%[rule(@fail@(X), Bottom(X))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(Bottom(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        }
        return fail;
      }

      StratSequence(s1,s2) -> {
        String n1 = compileGenericStrat(bag,extractedSignature,generatedSignature,`s1);
        String n2 = compileGenericStrat(bag,extractedSignature,generatedSignature,`s2);
        String seq = getName("seq");
        String seq2 = getName("seq2");
        generatedSignature.put(seq,1);
        generatedSignature.put(seq2,2);
        if( !Main.options.approx ) {
          bag.add(tools.encodeRule(%[rule(@seq@(Appl(@X@,@Y@)), @seq2@(@n2@(@n1@(Appl(@X@,@Y@))),Appl(@X@,@Y@)))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@seq@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@seq2@(Appl(@X@,@Y@),@Z@), Appl(@X@,@Y@))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@seq2@(Bottom(@Y@),@X@), Bottom(@X@))]%,generatedSignature));
        } else { 
          bag.add(tools.encodeRule(%[rule(@seq@(@X@), @n2@(@n1@(@X@)))]%,generatedSignature));
        }
        return seq;
      }

      StratChoice(s1,s2) -> {
        String n1 = compileGenericStrat(bag,extractedSignature,generatedSignature,`s1);
        String n2 = compileGenericStrat(bag,extractedSignature,generatedSignature,`s2);
        String choice = getName("choice");
        String choice2 = getName("choice");
        generatedSignature.put(choice,1);
        generatedSignature.put(choice2,1);
        if( !Main.options.approx ) {
          bag.add(tools.encodeRule(%[rule(@choice@(Appl(@X@,@Y@)), @choice2@(@n1@(Appl(@X@,@Y@))))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@choice@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@choice2@(Appl(@X@,@Y@)), Appl(@X@,@Y@))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@choice2@(Bottom(@X@)), @n2@(@X@))]%,generatedSignature));
        } else {
          bag.add(tools.encodeRule(%[rule(@choice@(@X@), @choice2@(@n1@(@X@)))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@choice2@(Appl(@X@,@Y@)), Appl(@X@,@Y@))]%,generatedSignature));
          bag.add(tools.encodeRule(%[rule(@choice2@(Bottom(@X@)), @n2@(@X@))]%,generatedSignature));
        }
        return choice;
      }

      StratAll(s) -> {
        String phi_s = compileGenericStrat(bag,extractedSignature,generatedSignature,`s);
        String all = getName("all");
        generatedSignature.put(all,1);
        String all_1 = all+"_1";
        generatedSignature.put(all_1,1);
        String all_2 = all+"_2";
        generatedSignature.put(all_2,1);
        String all_3 = all+"_3";
        generatedSignature.put(all_3,3);
        //         String concat = getName("concat");
        String concat = "concat";
        generatedSignature.put(concat,2); 
 
        // all
        bag.add(tools.encodeRule(%[rule(@all@(Appl(@Z0@,@Z1@)), @all_1@(Appl(@Z0@,@all_2@(@Z1@))))]%,generatedSignature));
        // all_1
        bag.add(tools.encodeRule(%[rule(@all_1@(Appl(@Z0@,BottomList(Z))), Bottom(Appl(@Z0@,@Z@)))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@all_1@(Appl(@Z0@,Cons(@Z1@,@Z2@))), Appl(@Z0@,Cons(@Z1@,@Z2@)))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@all_1@(Appl(@Z0@,Nil)) , Appl(@Z0@,Nil))]%,generatedSignature));
        
        bag.add(tools.encodeRule(%[rule(@all_2@(Nil()) , Nil())]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@all_2@(Cons(@Z1@,@Z2@)) , @all_3@(@phi_s@(@Z1@),@Z2@,Cons(@Z1@,Nil)))]%,generatedSignature));
        
        bag.add(tools.encodeRule(%[rule(@all_3@(Appl(@Z0@,@Z1@),Nil,@Y@) , Cons(Appl(@Z0@,@Z1@),Nil))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@all_3@(Appl(@Z0@,@Z1@),Cons(@X@,@Z@),@Y@) , @all_3@(@phi_s@(@X@),@Z@,Cons(Appl(@Z0@,@Z1@),@Y@)))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@all_3@(Bottom(@Z@),@X@,@Y@) , BottomList(@concat@(@Y@,@X@)))]%,generatedSignature));

        bag.add(tools.encodeRule(%[rule(@concat@(Nil,@Z@), @Z@)]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@concat@(Cons(@X@,@Y@),@Z@), Cons(@X@,@concat@(@Y@,@Z@)))]%,generatedSignature));
    
        return all;
      }

      StratOne(s) -> {
        String phi_s = compileGenericStrat(bag,extractedSignature,generatedSignature,`s);
        String one = getName("one");
        generatedSignature.put(one,1);
        String one_1 = one+"_1";
        generatedSignature.put(one_1,1);
        String one_2 = one+"_2";
        generatedSignature.put(one_2,1);
        String one_3 = one+"_3";
        generatedSignature.put(one_3,2);
        //         String clean = getName("clean");
        String clean = "clean";
        generatedSignature.put(clean,2); 
        // one
        bag.add(tools.encodeRule(%[rule(@one@(Appl(@Z0@,@Z1@)), @one_1@(Appl(@Z0@,@one_2@(@Z1@))))]%,generatedSignature));
        // one_1
        bag.add(tools.encodeRule(%[rule(@one_1@(Appl(@Z0@,BottomList(@Z@))), Bottom(Appl(@Z0@,@Z@)))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@one_1@(Appl(@Z0@,Cons(@X@,@Y@))), Appl(@Z0@,Cons(@X@,@Y@)))]%,generatedSignature));
        // one_2
        bag.add(tools.encodeRule(%[rule(@one_2@(Nil()), BottomList(Nil()))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@one_2@(Cons(@X@,@Y@)), @one_3@(@phi_s@(@X@),@Y@))]%,generatedSignature));
        // one_3
        bag.add(tools.encodeRule(%[rule(@one_3@(Appl(@Z0@,@Z1@),@Y@), Cons(Appl(@Z0@,@Z1@),@Y@))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@one_3@(Bottom(@Z@),Nil()), BottomList(Cons(@Z@,Nil())))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@one_3@(Bottom(@Z@),Cons(@Z0@,@Z1@)), @clean@(@Z@,@one_2@(Cons(@Z0@,@Z1@))))]%,generatedSignature));
        // clean
        bag.add(tools.encodeRule(%[rule(@clean@(@Z@,BottomList(Nil())), BottomList(Cons(@Z@,Nil())))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@clean@(@Z@,BottomList(Cons(@Z1@,Nil()))), BottomList(Cons(@Z@,Cons(@Z1@,Nil()))))]%,generatedSignature));
        bag.add(tools.encodeRule(%[rule(@clean@(@Z@,Cons(@Z0@,@Z1@)), Cons(@Z@,Cons(@Z0@,@Z1@)))]%,generatedSignature));

        return one;
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
 
  // search all At and store their values
  %strategy CollectAt(map:Map) extends Identity() {
    visit Term {
      At(Var(name),t2)-> {
        map.put(`name,`t2);
      }
    }
  }

  // replace x by t, and thus x@t by t@t
  %strategy ReplaceVariable(name:String, term:Term) extends Identity() {
    visit Term {
      Var(n) -> {
        if(`n==name) {
          return `term;
        }
      }
    }
  }
  
    // replace t@t by t
  %strategy EliminateAt() extends Identity() {
    visit Term {
      At(t,t) -> {
        return `t;
      }
    }
  }

  /**
    * transforms a set of rule that contains x@t into a set of rules without @ 
    * @param bag the set of rules to expand
    * @return a new set that contains the expanded rules
    */
  public static Collection<Rule> expandAt(Collection<Rule> bag) throws VisitFailure {
    Collection<Rule> res = new HashSet<Rule>();
    for(Rule rule:bag) {
      //System.out.println("expand at rule: " + rule);
      Map<String,Term> map = new HashMap<String,Term>();
      `TopDown(CollectAt(map)).visitLight(rule);
      if(map.keySet().isEmpty()) {
        res.add(rule);
      }
      //System.out.println("at-map: " + map);
      Rule newRule = rule;
      for(String name:map.keySet()) {
        Term t = map.get(name);
        //System.out.println("replace variable: " + name);
        newRule = `TopDown(ReplaceVariable(name,t)).visitLight(newRule);
        //System.out.println("new rule (instantiate): " + newRule);
        newRule = `TopDown(EliminateAt()).visitLight(newRule);
        //System.out.println("new rule (elimAt): " + newRule);
      }
      res.add(newRule);
    }
    return res;
  }

 
  
  /**
   * Expand an anti-pattern
   * @param generatedRules initial set of rules
   * @param rule the rule to expand
   * @param extractedSignature the signature
   * @return nothing, but modifies generatedRules
   */
  public static void expandAntiPattern(Collection<Rule> generatedRules, Rule rule, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature) {
    try {
      `OnceBottomUp(ContainsAntiPattern()).visitLight(rule); // check if the rule contains an anti-pattern (exception otherwise)
      generatedRules.remove(rule); // remove the rule since it will be expanded
      //       System.out.println("RULE: "+`rule);
      Collection<Rule> bag = new HashSet<Rule>();
      // perform one-step expansion
      `TopDown(ExpandAntiPattern(bag,rule,extractedSignature, generatedSignature)).visit(rule);

      /*
       * add rules from bag into generatedRules only if
       * they do not overlap with previous rules (from generatedRules)
       * 
       * In fact, more complicated: we should do unification and generate new rules 
       * if they unify
       * Example: a->c, f(a,x)->c
       * -- we generate for !a->c the rule f(x,y)->BOTTOM but we should generate f(!a,x)->BOTTOM
       */
      for(Rule expandr:bag) {
        boolean toAdd = true;
        //for(Rule r:generatedRules) {
        //  toAdd &= (!matchModuloAt(r.getlhs(), expandr.getlhs())); 
        //}
        if(toAdd) {
          //System.out.println("YES");
          expandAntiPattern(generatedRules,expandr,extractedSignature,generatedSignature);
        } else {
          // do not add expandr
          //System.out.println("NO");
        }
      }
    } catch(VisitFailure e) {
      // add the rule since it contains no more anti-pattern
      generatedRules.add(rule);
    }
  }

  private static boolean matchModuloAt(Term pattern, Term subject) {
    try {
      Term p = `TopDown(RemoveAtAndRenameVariables()).visitLight(pattern);
      Term s = `TopDown(RemoveAtAndRenameVariables()).visitLight(subject);
      //System.out.println(pattern + " <--> " + subject + " ==> " + (p==s));
      return p==s;
    } catch(VisitFailure e) {}
    throw new RuntimeException("should not be there");
  }
 
  /*
   * put terms in normal form to detect redundant patterns
   */
  %strategy RemoveAtAndRenameVariables() extends Identity() {
    visit Term {
      At(_,t2)  -> { return `t2; }
      Var(_)  -> { return `Var("_"); }
    }
  }

  /*
   * generate a term for the form f(Z1,...,Zn)
   * @param name the symbol name 
   * @param arity the arity of the symbol
   * @return the string that represents the term
   */
  private static String genAbstractTerm(String name, int arity, String varname) {
    if(arity==0) {
      return name + "()";
    } else {
      String args = varname+"_"+"1";
      for(int i=2 ; i<=arity ; i++) {
        args += ", " + varname+"_"+i;
      }
      return name + "(" + args + ")";
    }
  }

  %strategy ContainsAntiPattern() extends Fail() {
    visit Term {
      t@Anti(_)  -> { return `t; }
    }
  }

  /*
   * Perform one-step expansion
   *
   * @param bag the resulted set of rules
   * @param rule the rule to expand
   * @param extractedSignature the signature
   */
  %strategy ExpandAntiPattern(bag:Collection,subject:Rule,extractedSignature:Map, generatedSignature:Map) extends Identity() {
    visit Term {
      Anti(t) -> {
        //System.out.println("ExpandAntiPattern: " + `t);
        Term antiterm = (Main.options.generic)?tools.decodeConsNil(`t):`t;
        //System.out.println("ExpandAntiPattern antiterm: " + antiterm);
        %match(antiterm) { 
          Appl(name,args)  -> {
            // add g(Z1,...) ... h(Z1,...)
            Set<String> otherNames = new HashSet<String>( ((Map<String,Integer>)extractedSignature).keySet() );
            for(String otherName:otherNames) {
              if(!`name.equals(otherName)) {
                int arity = ((Map<String,Integer>)extractedSignature).get(otherName);
                Term newt = tools.encode(genAbstractTerm(otherName,arity, getName("Z")),generatedSignature);
                if(Main.options.generic) {
                  newt = tools.metaEncodeConsNil(newt,generatedSignature);
                }
                Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);
                bag.add(newr);
              }
            }
            
            // add f(!a1,...) ... f(a1,...,!an)
            sa.rule.types.termlist.TermList tl = (sa.rule.types.termlist.TermList) `args;
            int arity = tl.length();
            Term[] array = new Term[arity];
            Term[] tarray = new Term[arity];
            tarray = tl.toArray(tarray);
            String z = getName("Z");
            for(int i=0 ; i<arity ; i++) {
              array[i] = tools.encode(z+"_"+i,generatedSignature);
            }
            for(int i=0 ; i<arity ; i++) {
              Term ti = tarray[i];
              array[i] = `Anti(ti);
              Term newt = `Appl(name,sa.rule.types.termlist.TermList.fromArray(array));
              //               System.out.println("NEWT:"+`newt);
              array[i] = tools.encode(z+"_"+i,generatedSignature);
              if(Main.options.generic) {
                newt = tools.metaEncodeConsNil(newt,generatedSignature);
              }
              Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);

              bag.add(newr);
            }
           
          }
        }
      }
    }
  }


  // search all Var and store their values
  %strategy CollectVars(bag:Collection) extends Identity() {
    visit Term {
      Var(name)-> {
        bag.add(`name);
      }
    }
  }

  %strategy ReplaceWithFreshVar(name:String, multiplicityMap:Map, map:Map, signature:Map) extends Identity() {
    visit Term {
      Var(n)  -> {
        int value = (Integer)multiplicityMap.get(`name);
        if(`n.compareTo(`name)==0 && value>1) {
          String z = getName("Z");
          map.put(z,`n);
          multiplicityMap.put(`name, value - 1);
          Term newt = `Var(z);
          if(Main.options.generic) {
            newt = tools.metaEncodeConsNil(newt,signature);
          }
          return newt;
        }
      }
    }
  }

  /*
   * Transform lhs into linear-lhs + true ^ constraint on non linear variables
   */
  private static TermList linearize(Term lhs, Map<String,Integer> signature) {
    //System.out.println("lhs = " + lhs);
    Map<String,Integer> map = collectMultiplicity(lhs);
    Map<String,String> mapToOldName = new HashMap<String,String>();

    for(String name:map.keySet()) {
      //System.out.println(name + " --> " + map.get(name));
      if(map.get(name) > 1) {
        try {
          HashMap copy = new HashMap(map);
          lhs = `TopDown(ReplaceWithFreshVar(name,copy,mapToOldName,signature)).visitLight(lhs);
          //System.out.println("linear lhs = " + lhs);
        } catch(VisitFailure e) {
          throw new RuntimeException("Should not be there");
        }
      }
    }

    Term constraint = `Appl("True",TermList());
    for(String name:mapToOldName.keySet()) {
      String oldName = mapToOldName.get(name);
      constraint = `Appl("and",TermList( Appl("eq",TermList(Var(oldName),Var(name))), constraint));
    }

/*
    Condition constraint = `CondTrue();
    for(String name:mapToOldName.keySet()) {
      String oldName = mapToOldName.get(name);
      constraint = `CondAnd(CondEquals(Var(oldName),Var(name)), constraint);
    }
    */
    //System.out.println("constraint = " + constraint);
    return `TermList(lhs,constraint);

  }
  
  /**
   * Returns a Map which associates an integer to each variable name
   */
  public static Map<String,Integer> collectMultiplicity(tom.library.sl.Visitable subject) {
    // collect variables
    Collection<String> variableList = new LinkedList<String>();
    try {
      `TopDown(CollectVars(variableList)).visitLight(subject);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }

    // compute multiplicities
    HashMap<String,Integer> multiplicityMap = new HashMap<String,Integer>();
    for(String varName:variableList) {
      if(multiplicityMap.containsKey(varName)) {
        int value = multiplicityMap.get(varName);
        multiplicityMap.put(varName, 1+value);
      } else {
        multiplicityMap.put(varName, 1);
      }
    }
    return multiplicityMap;
  }

  /*
   * Generate equality rules:
   * f(x_1,...,x_n) = g(y_1,...,y_m) -> False
   * f(x_1,...,x_n) = f(y_1,...,y_n) -> x_1=y1 ^ ... ^ x_n=y_n ^ true
   *
   */
  private static void generateEquality(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature) {

    bag.add(tools.encodeRule(%[rule(and(True,True), True)]%,generatedSignature));
    bag.add(tools.encodeRule(%[rule(and(True,False), False)]%,generatedSignature));
    bag.add(tools.encodeRule(%[rule(and(False,True), False)]%,generatedSignature));
    bag.add(tools.encodeRule(%[rule(and(False,False), False)]%,generatedSignature));

    String z1 = getName("Z");
    String z2 = getName("Z");
    for(String f:extractedSignature.keySet()) {
      for(String g:extractedSignature.keySet()) {
        int arf = extractedSignature.get(f);
        int arg = extractedSignature.get(g);
        if(!f.equals(g)) {
          bag.add(tools.encodeRule(%[rule(eq(@genAbstractTerm(f,arf,z1)@,@genAbstractTerm(g,arg,z2)@), False)]%,generatedSignature));
        } else {
          String t1 = genAbstractTerm(f,arf,z1);
          String t2 = genAbstractTerm(f,arf,z2);
          String scond = "True";
          for(int i=1 ; i<=arf ; i++) {
            scond = %[and(eq(@z1@_@i@,@z2@_@i@),@scond@)]%;
          }
          //System.out.println(t1 + " = " + t2 + " --> " + scond);
          bag.add(tools.encodeRule(%[rule(eq(@t1@,@t2@),@scond@)]%,generatedSignature));
        }
      }
    }
    //bag.add(tools.encodeRule(%[rule(@mu@(Bottom(X)), Bottom(X))]%));
  }

}
