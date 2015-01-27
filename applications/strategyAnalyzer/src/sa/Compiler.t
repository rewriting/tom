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
    %match(expl) {
      ExpressionList(_*,x,_*) -> {
        compileExp(bag,extractedSignature,generatedSignature,`x);
      }
    }
    generateEquality(bag, extractedSignature, generatedSignature);
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
        //System.out.println("Original generatedSignature= " + extractedSignature);
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

    %match(strat) {
      StratExp(Set(rulelist)) -> {
        String r = getName("rule");
        generatedSignature.put(r,1);
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
            String atname = "INTERNALX";
            Term atvar = tools.encode(atname);
            // use AST-syntax because lhs and rhs are already encoded
            //bag.add(`Rule(Appl(r,TermList(lhs)),rhs));
            //bag.add(`Rule(Appl(r,TermList(At(tools.encode("INTERNALX"),Anti(lhs)))),tools.encode("Bottom(INTERNALX)")));
            // propagate failure; if the rule is applied to the result of a strategy that failed then the result is a failure
            bag.add(`Rule(Appl(r,TermList(tools.encode("Bottom("+atname+")"))),tools.encode("Bottom("+atname+")")));

            TermList result = linearize(`lhs);
            %match(result) {
              TermList(linearlhs, cond) -> {
                bag.add(`Rule(Appl(r,TermList(At(atvar,linearlhs))),
                              Appl(cr, TermList(atvar, cond))));
                bag.add(`Rule(Appl(r,TermList(At(atvar,Anti(linearlhs)))),tools.encode("Bottom("+atname+")")));

                bag.add(`Rule(Appl(cr,TermList(linearlhs, tools.encode("True"))), rhs));
                bag.add(`Rule(Appl(cr,TermList(At(atvar,linearlhs), tools.encode("False"))),tools.encode("Bottom("+atname+")")));
              }
            }
            //System.out.println(cr);
          }
        }
        return r;
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
          bag.add(tools.encodeRule(%[rule(@mu@(at(INTERNALX,anti(Bottom(Y)))), @phi_s@(INTERNALX))]%));
          bag.add(tools.encodeRule(%[rule(@mu@(Bottom(X)), Bottom(X))]%));
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
        if( !Main.options.exact ){
          bag.add(tools.encodeRule(%[rule(@id@(X), X)]%));
          // Bottom of Bottom is Bottom
          // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
          bag.add(tools.encodeRule(%[rule(Bottom(Bottom(X)), Bottom(X))]%));
       } else { 
          // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
          // normally it will follow reduction in original TRS
          bag.add(tools.encodeRule(%[rule(@id@(at(X,anti(Dummy()))), X)]%));
          bag.add(tools.encodeRule(%[rule(@id@(Bottom(X)), Bottom(X))]%));
        }
        return id;
      }

      StratFail() -> {
        String fail = getName("fail");
        generatedSignature.put(fail,1);
        if( !Main.options.exact ){
          bag.add(tools.encodeRule(%[rule(@fail@(X), Bottom(X))]%));
          // Bottom of Bottom is Bottom
          // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
          bag.add(tools.encodeRule(%[rule(Bottom(Bottom(X)), Bottom(X))]%));
       } else { 
          // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
          // normally it will follow reduction in original TRS
          bag.add(tools.encodeRule(%[rule(@fail@(at(X,anti(Dummy()))), Bottom(X))]%));
          bag.add(tools.encodeRule(%[rule(@fail@(Bottom(X)), Bottom(X))]%));
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
        if( !Main.options.exact ){
          //           bag.add(tools.encodeRule(%[rule(@seq@(X), @n2@(@n1@(X)))]%)); // old version - doesn't keep track of input
          bag.add(tools.encodeRule(%[rule(@seq@(X), @seq2@(@n2@(@n1@(X)),X))]%));
          // Bottom of Bottom is Bottom
          // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
          bag.add(tools.encodeRule(%[rule(Bottom(Bottom(X)), Bottom(X))]%));
       } else { 
          // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
          // normally it will follow reduction in original TRS
          bag.add(tools.encodeRule(%[rule(@seq@(at(X,anti(Dummy()))), @seq2@(@n2@(@n1@(X)),X))]%));
          bag.add(tools.encodeRule(%[rule(@seq@(Bottom(X)), Bottom(X))]%));
        }
        bag.add(tools.encodeRule(%[rule(@seq2@(Bottom(Y),X), Bottom(X))]%));
        bag.add(tools.encodeRule(%[rule(@seq2@(at(X,anti(Bottom(Y))),Z), X)]%));
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
        //         bag.add(tools.encodeRule(%[rule(@choice@(X), @choice2@(@n1@(X)))]%)); // old version - Bottom not propagated directly
        bag.add(tools.encodeRule(%[rule(@choice@(at(X,anti(Dummy()))),  @choice2@(@n1@(X)) )]%));
        bag.add(tools.encodeRule(%[rule(@choice@(Bottom(X)), Bottom(X))]%));
        bag.add(tools.encodeRule(%[rule(@choice2@(Bottom(X)), @n2@(X))]%));
        bag.add(tools.encodeRule(%[rule(@choice2@(at(X,anti(Bottom(Y)))), X)]%));
        return choice;
      }

      StratAll(s) -> {
        String phi_s = compileStrat(bag,extractedSignature,generatedSignature,`s);
        String all = getName("all");
        generatedSignature.put(all,1);
        //         Iterator<String> it = extractedSignature.keySet().iterator();
        //         while(it.hasNext()) {
        //           String name = it.next();
        for(String name : extractedSignature.keySet()) {
          int arity = generatedSignature.get(name);
          int arity_all = arity+1;
          if(arity==0) {
            bag.add(tools.encodeRule(%[rule(@all@(@name@), @name@)]%));
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
              bag.add(tools.encodeRule(%[rule(@all@(@name@(@lx@)), @all_n@(@rx@,@name@(@lx@)))]%)); 
              // propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
              bag.add(tools.encodeRule(%[rule(@all@(Bottom(X)), Bottom(X) )]%));               
            }

            // generate success rules
            // all_g(x1@!BOTTOM,...,xN@!BOTTOM,_) -> g(x1,...,xN)
            String lx = "at(X1,anti(Bottom(Y1)))";
            String rx = "X1"; 
            for(int j=2; j<=arity;j++) {
              lx += ",at(X"+j+",anti(Bottom(Y"+j+")))";
              rx += ",X"+j;
            }
            bag.add(tools.encodeRule(%[rule(@all_n@(@lx@,Z), @name@(@rx@))]%));

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
              bag.add(tools.encodeRule(%[rule(@all_n@(@llx@,Z), Bottom(Z))]%));
            }
          }
        }        
        return all;
      }

      StratOne(s) -> {
        String phi_s = compileStrat(bag,extractedSignature,generatedSignature,`s);
        String one = getName("one");
        generatedSignature.put(one,1);
        //         Iterator<String> it = extractedSignature.keySet().iterator();
        //         while(it.hasNext()) {
        //           String name = it.next();
        for(String name : extractedSignature.keySet()) {
          int arity = generatedSignature.get(name);
          if(arity==0) {
            bag.add(tools.encodeRule(%[rule(@one@(@name@), Bottom(@name@))]%));
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
              bag.add(tools.encodeRule(%[rule(@one@(@name@(@lx@)), @one_n@_1(@rx@))]%));
              // propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
              bag.add(tools.encodeRule(%[rule(@one@(Bottom(X)), Bottom(X) )]%));               
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
                bag.add(tools.encodeRule(%[rule(@one_n_i@(@lx@), @one_n_ii@(@rx@))]%));
              } else {
                // one_f_n(Bottom(x1),...,Bottom(xn)) -> Bottom(f(x1,...,xn))
                String lx = "Bottom(X1)";
                String rx = "X1";
                for(int j=2; j<=arity;j++) {
                  lx += ",Bottom(X"+j+")";
                  rx += ",X"+j;
                }
                bag.add(tools.encodeRule(%[rule(@one_n_i@(@lx@), Bottom(@name@(@rx@)))]%));

              }

              {
                // one_f_i(Bottom(x1),...,xi@!Bottom(_),xj,...,xn)
                // -> f(x1,...,xi,...,xn)
                String lx = (i==1)?"at(X1,anti(Bottom(Y)))":"Bottom(X1)";
                String rx = "X1";
                for(int j=2; j<=arity;j++) {
                  if(j<i) {
                    lx += ",Bottom(X"+j+")";
                    rx += ",X"+j;
                  } else if(j==i) {
                    lx += ",at(X"+j+",anti(Bottom(Y)))";
                    rx += ",X"+j;
                  } else {
                    lx += ",X"+j;
                    rx += ",X"+j;
                  }
                }
                bag.add(tools.encodeRule(%[rule(@one_n_i@(@lx@), @name@(@rx@))]%));
              }

            }
          }
        }
        return one;
      }

    }
    return strat.toString();
  }

  /**
   * compile a strategy in a classical way (without using meta-representation)
   * return the name of the top symbol (phi) introduced
   * @param bag set of rule that is extended by compilation
   * @param extractedSignature associates arity to a name, for all constructor of the initial strategy
   * @param generatedSignature associates arity to a name, for all generated defined symbols
   * @param strat the strategy to compile
   * @return the name of the last compiled strategy
   */
  private static String compileGenericStrat(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, Strat strat) {

    %match(strat) {
      StratExp(Set(rulelist)) -> {
        String r = getName("rule");
        generatedSignature.put(r,1);
        %match(rulelist) {
          RuleList(_*,Rule(lhs,rhs),_*) -> {
            // use AST-syntax because lhs and rhs are already encoded
            //System.out.println("lhs = " + `lhs);
            //System.out.println("encode lhs = " + tools.encodeConsNil(`lhs));
            //System.out.println("rhs = " + `rhs);
            //System.out.println("decode(encode rhs) = " + tools.decodeConsNil(tools.metaEncodeConsNil(`rhs)));
            bag.add(`Rule(Appl(r,TermList(tools.metaEncodeConsNil(lhs))),tools.metaEncodeConsNil(rhs)));
            bag.add(`Rule(Appl(r,TermList(At(tools.encode("INTERNALX"),Anti(tools.metaEncodeConsNil(lhs))))),tools.encode("Bottom(INTERNALX)")));
          }
        }

        for(String name:extractedSignature.keySet()) {
          // add symb_a(), symb_b(), symb_f(), symb_g() in the signature
          generatedSignature.put("symb_"+name,0);
        }
        return r;
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
          bag.add(tools.encodeRule(%[rule(@mu@(Appl(Z0,Z1)), @phi_s@(Appl(Z0,Z1)))]%));
          bag.add(tools.encodeRule(%[rule(@mu@(Bottom(X)), Bottom(X))]%));
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
        bag.add(tools.encodeRule(%[rule(@id@(X), X)]%));
        return id;
      }

      StratFail() -> {
        String fail = getName("fail");
        generatedSignature.put(fail,1);
        bag.add(tools.encodeRule(%[rule(@fail@(X), Bottom(X))]%));
        return fail;
      }

      StratSequence(s1,s2) -> {
        String n1 = compileGenericStrat(bag,extractedSignature,generatedSignature,`s1);
        String n2 = compileGenericStrat(bag,extractedSignature,generatedSignature,`s2);
        String seq = getName("seq");
        generatedSignature.put(seq,1);
        bag.add(tools.encodeRule(%[rule(@seq@(X), @n2@(@n1@(X)))]%));
        return seq;
      }

      StratChoice(s1,s2) -> {
        String n1 = compileGenericStrat(bag,extractedSignature,generatedSignature,`s1);
        String n2 = compileGenericStrat(bag,extractedSignature,generatedSignature,`s2);
        String choice = getName("choice");
        String choice2 = getName("choice");
        generatedSignature.put(choice,1);
        generatedSignature.put(choice2,1);
        bag.add(tools.encodeRule(%[rule(@choice@(X), @choice2@(@n1@(X)))]%));
        bag.add(tools.encodeRule(%[rule(@choice2@(Bottom(X)), @n2@(X))]%));
        bag.add(tools.encodeRule(%[rule(@choice2@(Appl(Z0,Z1)), Appl(Z0,Z1))]%));
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
        bag.add(tools.encodeRule(%[rule(@all@(Appl(Z0,Z1)), @all_1@(Appl(Z0,@all_2@(Z1))))]%));
        // all_1
        bag.add(tools.encodeRule(%[rule(@all_1@(Appl(Z0,BottomList(Z))), Bottom(Appl(Z0,Z)))]%));
        bag.add(tools.encodeRule(%[rule(@all_1@(Appl(Z0,Cons(Z1,Z2))), Appl(Z0,Cons(Z1,Z2)))]%));
        bag.add(tools.encodeRule(%[rule(@all_1@(Appl(Z0,Nil)) , Appl(Z0,Nil))]%));
        
        bag.add(tools.encodeRule(%[rule(@all_2@(Nil()) , Nil())]%));
        bag.add(tools.encodeRule(%[rule(@all_2@(Cons(Z1,Z2)) , @all_3@(@phi_s@(Z1),Z2,Cons(Z1,Nil)))]%));
        
        bag.add(tools.encodeRule(%[rule(@all_3@(Appl(Z0,Z1),Nil,Y) , Cons(Appl(Z0,Z1),Nil))]%));
        bag.add(tools.encodeRule(%[rule(@all_3@(Appl(Z0,Z1),Cons(X1,X2),Y) , @all_3@(@phi_s@(X1),X2,Cons(Appl(Z0,Z1),Y)))]%));
        bag.add(tools.encodeRule(%[rule(@all_3@(Bottom(Z),X2,Y) , BottomList(@concat@(Y,X2)))]%));

        bag.add(tools.encodeRule(%[rule(@concat@(Nil,Z), Z)]%));
        bag.add(tools.encodeRule(%[rule(@concat@(Cons(X,Y),Z), Cons(X,@concat@(Y,Z)))]%));
    
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
        bag.add(tools.encodeRule(%[rule(@one@(Appl(Z0,Z1)), @one_1@(Appl(Z0,@one_2@(Z1))))]%));
        // one_1
        bag.add(tools.encodeRule(%[rule(@one_1@(Appl(Z0,BottomList(Z))), Bottom(Appl(Z0,Z)))]%));
        bag.add(tools.encodeRule(%[rule(@one_1@(Appl(Z0,Cons(X1,X2))), Appl(Z0,Cons(X1,X2)))]%));
        // one_2
        bag.add(tools.encodeRule(%[rule(@one_2@(Nil()), BottomList(Nil()))]%));
        bag.add(tools.encodeRule(%[rule(@one_2@(Cons(X1,X2)), @one_3@(@phi_s@(X1),X2))]%));
        // one_3
        bag.add(tools.encodeRule(%[rule(@one_3@(Appl(Z0,Z1),X2), Cons(Appl(Z0,Z1),X2))]%));
        bag.add(tools.encodeRule(%[rule(@one_3@(Bottom(Z),Nil()), BottomList(Cons(Z,Nil())))]%));
        bag.add(tools.encodeRule(%[rule(@one_3@(Bottom(Z),Cons(Z0,Z1)), @clean@(Z,@one_2@(Cons(Z0,Z1))))]%));
        // clean
        bag.add(tools.encodeRule(%[rule(@clean@(Z,BottomList(Nil())), BottomList(Cons(Z,Nil())))]%));
        bag.add(tools.encodeRule(%[rule(@clean@(Z,BottomList(Cons(Z1,Nil()))), BottomList(Cons(Z,Cons(Z1,Nil()))))]%));
        bag.add(tools.encodeRule(%[rule(@clean@(Z,Cons(Z0,Z1)), Cons(Z,Cons(Z0,Z1)))]%));

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
   * PEM's version
   * @param generatedRules initial set of rules
   * @param rule the rule to expand
   * @param extractedSignature the signature
   * @return nothing, but modifies generatedRules
   */
  public static void expandAntiPattern(Collection<Rule> generatedRules, Rule rule, Map<String,Integer> extractedSignature) {
    try {
      `OnceBottomUp(ContainsAntiPattern()).visitLight(rule); // check if the rule contains an anti-pattern (exception otherwise)
      generatedRules.remove(rule); // remove the rule since it will be expanded
      //       System.out.println("RULE: "+`rule);
      Collection<Rule> bag = new HashSet<Rule>();
      // perform one-step expansion
      `TopDown(ExpandAntiPattern(bag,rule,extractedSignature)).visit(rule);

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
        for(Rule r:generatedRules) {
          toAdd &= (!matchModuloAt(r.getlhs(), expandr.getlhs())); 
        }
        if(toAdd) {
          //System.out.println("YES");
          expandAntiPattern(generatedRules,expandr,extractedSignature);
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
      return name;
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
  %strategy ExpandAntiPattern(bag:Collection,subject:Rule,extractedSignature:Map) extends Identity() {
    visit Term {
      Anti(t) -> {
        Term antiterm = (Main.options.generic)?tools.decodeConsNil(`t):`t;
        %match(antiterm) { 
          Appl(name,args)  -> {
            Map<String,Integer> signature = (Map<String,Integer>)extractedSignature;
            // add g(Z1,...) ... h(Z1,...)
            for(String otherName:signature.keySet()) {
              if(!`name.equals(otherName)) {
                int arity = signature.get(otherName);
                Term newt = tools.encode(genAbstractTerm(otherName,arity, getName("Z")));
                if(Main.options.generic) {
                  newt = tools.metaEncodeConsNil(newt);
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
              array[i] = tools.encode(z+"_"+i);
            }
            for(int i=0 ; i<arity ; i++) {
              Term ti = tarray[i];
              array[i] = `Anti(ti);
              Term newt = `Appl(name,sa.rule.types.termlist.TermList.fromArray(array));
              //               System.out.println("NEWT:"+`newt);
              array[i] = tools.encode(z+"_"+i);
              if(Main.options.generic) {
                newt = tools.metaEncodeConsNil(newt);
              }
              Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);

              bag.add(newr);
            }
           
          }
        }
      }
    }
  }


  /*
   * Perform one-step expansion
   *
   * @param bag the resulted set of rules
   * @param rule the rule to expand
   * @param extractedSignature the signature
   */
  %strategy ExpandAntiPatternWithLevel(bag:Collection,subject:Rule,extractedSignature:Map,level:int) extends Identity() {
    visit Term {
      Anti(t) -> {
        Term antiterm = (Main.options.generic)?tools.decodeConsNil(`t):`t;
          %match(antiterm) { 
            Appl(name,args)  -> {
              if(`name.compareTo("Bottom")!=0 && level==0){ // if maximum level and not a Bottom
                String z = getName("Z");
                Term newt = tools.encode(z);
                if(Main.options.generic) {
                  newt = tools.metaEncodeConsNil(newt);
                }
                Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);
                bag.add(newr);
              } else {
                Map<String,Integer> signature = (Map<String,Integer>)extractedSignature;
                // add g(Z1,...) ... h(Z1,...)
                for(String otherName:signature.keySet()) {
                  if(!`name.equals(otherName)) {
                    int arity = signature.get(otherName);
                    Term newt = tools.encode(genAbstractTerm(otherName,arity, getName("Z")));
                    if(Main.options.generic) {
                      newt = tools.metaEncodeConsNil(newt);
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
                  array[i] = tools.encode(z+"_"+i);
                }
                for(int i=0 ; i<arity ; i++) {
                  Term ti = tarray[i];
                  array[i] = `Anti(ti);
                  Term newt = `Appl(name,sa.rule.types.termlist.TermList.fromArray(array));
                  array[i] = tools.encode(z+"_"+i);
                  if(Main.options.generic) {
                    newt = tools.metaEncodeConsNil(newt);
                  }
                  Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);
              
                  bag.add(newr);
                }              

                // non-linear patterns: f(x,g(x)) -> f(y,g(z))
                Term lint = `antiterm;
                boolean nonlinear = false;
                HashSet<String> nonlinVars = new HashSet<String>();
                HashSet<String> allVars = new HashSet<String>();
                for(Term elem:`args.getCollectionTermList()){
                  HashSet<String> listVars = new HashSet<String>();
                  `TopDown(CollectVars(listVars)).visitLight(elem);
                  for(String var:listVars){
                    for(String occ:allVars){
                      if(var.compareTo(occ) == 0){
                        nonlinVars.add(var);
                        nonlinear = true;
                      }
                    }
                  }
                  allVars.addAll(listVars);
                }
                if(nonlinear){
                  for(String v:nonlinVars){
                    // desactivate
                    //lint = `TopDown(ReplaceWithFreshVar(v)).visitLight(lint);
                  }
                  Term newt = lint;
                  if(Main.options.generic) {
                    newt = tools.metaEncodeConsNil(newt);
                  }
                  Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);
              
                  bag.add(newr);
                }
                // non-linear patterns

              }
            }
        }
      }
    }
  }

  public static void expandAntiPatternWithLevel(Collection<Rule> generatedRules, Rule rule, Map<String,Integer> extractedSignature, int level) {
    try {
      `OnceBottomUp(ContainsAntiPattern()).visitLight(rule); // check if the rule contains an anti-pattern (exception otherwise)
      generatedRules.remove(rule); // remove the rule since it will be expanded
      //       System.out.println("RULE: "+`rule);
      Collection<Rule> bag = new HashSet<Rule>();
      // perform one-step expansion
      `TopDown(ExpandAntiPatternWithLevel(bag,rule,extractedSignature,level)).visit(rule);

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
//         boolean toAdd = true;
//         for(Rule r:generatedRules) {
//           toAdd &= (!matchModuloAt(r.getlhs(), expandr.getlhs())); 
//         }
//         if(toAdd) {
          //System.out.println("YES");
          expandAntiPatternWithLevel(generatedRules,expandr,extractedSignature,level-1);
//         } else {
//           // do not add expandr
//           //System.out.println("NO");
//         }
      }
    } catch(VisitFailure e) {
      // add the rule since it contains no more anti-pattern
      generatedRules.add(rule);
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

  %strategy ReplaceWithFreshVar(name:String, multiplicityMap:Map, map:Map) extends Identity() {
    visit Term {
      Var(n)  -> {
        int value = (Integer)multiplicityMap.get(`name);
        if(`n.compareTo(`name)==0 && value>1) {
          String z = getName("Z");
          map.put(z,`n);
          multiplicityMap.put(`name, value - 1);
          Term newt = tools.encode(z);
          if(Main.options.generic) {
            newt = tools.metaEncodeConsNil(newt);
          }
          return newt;
        }
      }
    }
  }

  /*
   * Transform lhs into linear-lhs + true ^ constraint on non linear variables
   */
  private static TermList linearize(Term lhs) {
    //System.out.println("lhs = " + lhs);
    Map<String,Integer> map = collectMultiplicity(lhs);
    Map<String,String> mapToOldName = new HashMap<String,String>();

    for(String name:map.keySet()) {
      //System.out.println(name + " --> " + map.get(name));
      if(map.get(name) > 1) {
        try {
          HashMap copy = new HashMap(map);
          lhs = `TopDown(ReplaceWithFreshVar(name,copy,mapToOldName)).visitLight(lhs);
          //System.out.println("linear lhs = " + lhs);
        } catch(VisitFailure e) {
          throw new RuntimeException("Should not be there");
        }
      }
    }

    Term constraint = tools.encode("True");
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

  private static void generateEquality(Collection<Rule> bag, Map<String,Integer> signature, Map<String,Integer> generatedSignature) {
    generatedSignature.put("True",0);
    generatedSignature.put("False",0);
    generatedSignature.put("and",2);
    generatedSignature.put("eq",2);

    bag.add(tools.encodeRule(%[rule(and(True,True), True)]%));
    bag.add(tools.encodeRule(%[rule(and(True,False), False)]%));
    bag.add(tools.encodeRule(%[rule(and(False,True), False)]%));
    bag.add(tools.encodeRule(%[rule(and(False,False), False)]%));

    for(String f:signature.keySet()) {
      for(String g:signature.keySet()) {
        int arf = signature.get(f);
        int arg = signature.get(g);
        if(!f.equals(g)) {
          bag.add(tools.encodeRule(%[rule(eq(@genAbstractTerm(f,arf, getName("Z"))@,@genAbstractTerm(g,arg, getName("Z"))@), False)]%));
        } else {
          String z1 = getName("Z");
          String z2 = getName("Z");
          String t1 = genAbstractTerm(f,arf,z1);
          String t2 = genAbstractTerm(f,arf,z2);
          String scond = "True";
          for(int i=1 ; i<=arf ; i++) {
            scond = %[and(eq(@z1@_@i@,@z2@_@i@),@scond@)]%;
          }
          //System.out.println(t1 + " = " + t2 + " --> " + scond);
          bag.add(tools.encodeRule(%[rule(eq(@t1@,@t2@),@scond@)]%));
        }
      }
    }
    //bag.add(tools.encodeRule(%[rule(@mu@(Bottom(X)), Bottom(X))]%));
  }

}
