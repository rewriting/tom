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
  }

  private static void compileExp(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, Expression e) {
    //System.out.println("exp = " + e);
    %match(e) {
      Let(v,Signature(sl),body) -> {
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
   * @param extractedSignature associates arity to a name, for all constructor of the initial strategy
   * @param generatedSignature associates arity to a name, for all generated defined symbols
   * @param strat the strategy to compile
   * @return the name of the last compiled strategy
   */
  private static String compileStrat(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, Strat strat) {

    %match(strat) {
      StratExp(Set(rulelist)) -> {
        String r = getName("rule");
        generatedSignature.put(r,1);
        %match(rulelist) {
          RuleList(_*,Rule(lhs,rhs),_*) -> {
            // use AST-syntax because lhs and rhs are already encoded
            bag.add(`Rule(Appl(r,TermList(lhs)),rhs));
            bag.add(`Rule(Appl(r,TermList(At(tools.encode("INTERNALX"),Anti(lhs)))),tools.encode("Bottom(INTERNALX)")));
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
        String n1 = compileStrat(bag,extractedSignature,generatedSignature,`s1);
        String n2 = compileStrat(bag,extractedSignature,generatedSignature,`s2);
        String seq = getName("seq");
        generatedSignature.put(seq,1);
        bag.add(tools.encodeRule(%[rule(@seq@(X), @n2@(@n1@(X)))]%));
        return seq;
      }

      StratChoice(s1,s2) -> {
        String n1 = compileStrat(bag,extractedSignature,generatedSignature,`s1);
        String n2 = compileStrat(bag,extractedSignature,generatedSignature,`s2);
        String choice = getName("choice");
        String choice2 = getName("choice");
        generatedSignature.put(choice,1);
        generatedSignature.put(choice2,1);
        bag.add(tools.encodeRule(%[rule(@choice@(X), @choice2@(@n1@(X)))]%));
        bag.add(tools.encodeRule(%[rule(@choice2@(Bottom(X)), @n2@(X))]%));
        bag.add(tools.encodeRule(%[rule(@choice2@(at(X,anti(Bottom(Y)))), X)]%));
        return choice;
      }

      StratAll(s) -> {
        String phi_s = compileStrat(bag,extractedSignature,generatedSignature,`s);
        String all = getName("all");
        generatedSignature.put(all,1);
        Iterator<String> it = extractedSignature.keySet().iterator();
        while(it.hasNext()) {
          String name = it.next();
          int arity = generatedSignature.get(name);
          if(arity==0) {
            bag.add(tools.encodeRule(%[rule(@all@(@name@), @name@)]%));
          } else {
            String all_n = all+"_"+name;
            generatedSignature.put(all_n,1);
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
        Iterator<String> it = extractedSignature.keySet().iterator();
        while(it.hasNext()) {
          String name = it.next();
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
        String concat = getName("concat");
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
        String clean = getName("clean");
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

  // transforms a set of rule that contains x@t into a set of rules without @ 
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

  /*
   * Expand an anti-pattern
   * HC's version
   */
  public static Collection<Rule> expandAntiPatterns(Collection<Rule> bag, Map<String,Integer> extractedSignature) 
    throws VisitFailure {

    // generate depth 1 replacement terms for each symbol in the signature
    // use extractedSignature since normally anti-patterns are only on symbols in signature 
      // expsig: associate f(Z1,...,ZN) to "f"
    Map<String,Term> expsig = new HashMap<String,Term>();
    for(String name: extractedSignature.keySet()) {
      int arity = extractedSignature.get(name);
      TermList tl = `TermList();
      for(int i=1 ; i<=arity ; i++) {
        tl = `TermList(tl*,Var("Z"+i));
      }
      Term t = `Appl(name,tl);
      expsig.put(name,t);
    }
    //     System.out.println(expsig+"\n");

    Collection<Rule> ruleSet = new HashSet<Rule>();
    for(Rule r:bag) {
      %match(r) {
        Rule(lhs,rhs) -> {
          // if generic version than decode LHS
          boolean generic = Main.options.generic;
          Term decodedLHS = (generic)?tools.generalDecodeConsNil(`lhs):`lhs;

          // Generate LHSs without anti-pattern
          Collection<Term> termSet = generateTermsWithoutAntiPatterns(decodedLHS,expsig);

          // remove the starting term if it contained an anti-pattern
          HashSet<Position> posSet = new HashSet<Position>();
          // Collect anti-patterns
          `BottomUp(CollectAntiPatterns(posSet)).visit(decodedLHS);
          for(Position pos: posSet) {
            Term at = (Term)pos.getSubterm().visitLight(decodedLHS);
            %match(at) {
              Anti(term) -> { 
                Term newTerm = (Term)pos.getReplace(`term).visitLight(decodedLHS);
                termSet.remove(newTerm);    
              }
            }
          }

          for(Term t: termSet) {
            // generate rule for each lhs generated 
            // if generic version than encode the generated lhs
            Term newLHS = (generic)?tools.generalMetaEncodeConsNil(t,extractedSignature.keySet()):`t;
            ruleSet.add(`Rule(newLHS,rhs));
            //             System.out.println(" PROBLEM = "+ pp.toString(`Rule(newLHS,rhs))); 
          }
        }
      }
    }

    return ruleSet;
  }
 
  public static Collection<Term> generateTermsWithoutAntiPatterns(Term t, Map<String,Term> expsig) 
    throws VisitFailure {

    HashSet<Term> termSet = new HashSet<Term>();
    HashSet<Position> posSet = new HashSet<Position>();

    // Collect anti-patterns
    `BottomUp(CollectAntiPatterns(posSet)).visit(t);
 
    if(posSet.isEmpty()) {
      termSet.add(t);
    } else {
      // Generate rules without anti-pattern
      for(Position pos: posSet) {
        Term at = (Term)pos.getSubterm().visitLight(t);
        String symbolName = "DoesntExist";
        Term below_at = at;
        %match(at) {
          Anti(bat@Appl(name,_)) -> { 
            symbolName = `name;
            below_at = `bat;
          }
        }
        if(symbolName.compareTo("doesntexist")==0) {
          System.out.println(" PROBLEM "); 
        } // TO CHANGE

        // Generate all terms corresponding to a pattern
        HashSet<Term> antiTerms = propagateAntiSmart(below_at,expsig);
        for(Term allt : antiTerms){
          Term t2p = (Term)pos.getReplace(allt).visitLight(t);
          termSet.addAll(generateTermsWithoutAntiPatterns(t2p,expsig));
        }
      }
    }

    return termSet;
  }


  public static HashSet<Term> propagateAntiSmart(Term t, Map<String,Term> expsig) 
    throws VisitFailure {

    HashSet<Term> termSet = new HashSet<Term>();

    %match(t) {
      Appl(name,args) -> {
        // First level corresponding anti-terms (a,b,f(x),... for !g(...))
        Set<String> gensig = new HashSet<String>(expsig.keySet());
        gensig.remove(`name); // all but current
        for(String sn : gensig) {
          termSet.add(expsig.get(sn));
        }
        
        %match(args){ 
          TermList(a*,tl,b*) -> {
            // go down into the arguments
            HashSet<Term> antiArgs = propagateAntiSmart(`tl,expsig);
            for(Term curr_arg:antiArgs){
              //           System.out.println("  ADD of " + pp.toString(`Appl(name,curr_arg)) );
              TermList na = generateVarArgs(`a);
              TermList nb = generateVarArgs(`b);
              termSet.add(`Appl(name,TermList(na*,ConsTermList(curr_arg,nb))));
              //                     termSet.add(`Appl(name,TermList(curr_arg)));
            }
          }
        }
      }
    }
    return termSet; 
  }

  public static TermList generateVarArgs(TermList a){
    TermList args = `TermList();
    %match(a){
      TermList(head,tail*) -> {
        Term nh = `Var(getName("Z"));
        TermList nt = generateVarArgs(`tail);
        return `ConsTermList(nh,nt);
        //         return `ConsTermList(Var(getName("Z")),generateVarArgs(tail));
      }
    }
    return args;
  }


  public static HashSet<Term> propagateAnti(Term t, Map<String,Term> expsig) 
    throws VisitFailure {

    HashSet<Term> termSet = new HashSet<Term>();

    %match(t) {
      Appl(name,tl) -> {
        // First level corresponding anti-terms (a,b,f(x),... for !g(...))
        Set<String> gensig = new HashSet<String>(expsig.keySet());
        gensig.remove(`name); // all but current
        //         gensig.remove("Bottom"); //  no Bottom when generating
        for(String sn : gensig) {
          termSet.add(expsig.get(sn));
        }
        
        // go down into the arguments
        HashSet<TermList> antiArgs = propagateAntiList(`tl,expsig);
        for(TermList curr_arg:antiArgs){
          //           System.out.println("  ADD of " + pp.toString(`Appl(name,curr_arg)) );
          termSet.add(`Appl(name,curr_arg));
        }
      }
    }
    return termSet; 
  }


  public static HashSet<TermList> propagateAntiList(TermList tl, Map<String,Term> expsig) 
    throws VisitFailure {

    HashSet<TermList> termListSet = new HashSet<TermList>();

    %match(tl) {
      //       TermList() -> {
      //         return termListSet;
      //       }

      // singleton
      TermList(h) -> {
        HashSet<Term> termSet = propagateAnti(`h,expsig);
        for(Term arg:termSet){
          termListSet.add(`TermList(arg));
        }
        termListSet.add(`TermList(h));
      }

      TermList(h,tail*) -> {
        HashSet<Term> termSet = propagateAnti(`h,expsig);
        //         System.out.println("  TERM SET of " + pp.toString(`h) + "  ==  "+ termSet);
        HashSet<TermList> tailSet = propagateAntiList(`tail,expsig);
        //         System.out.println("  TAIL SET of " + pp.toString(`tail) + "  ==  "+ tailSet);

        for(TermList rest:tailSet){
          for(Term arg:termSet){
            termListSet.add(`TermList(arg,rest*));
            //             System.out.println("  ADD " + pp.toString(`TermList(arg,rest*))+"\n      SET="+termListSet );
          }
          termListSet.add(`TermList(h,rest*));
          //           System.out.println("  ADD " + pp.toString(`TermList(h,rest*))+"\n      SET="+termListSet );
        }
      }
    }

    return termListSet; 
  }


  %strategy CollectAntiPatterns(pos:HashSet) extends Identity() {
    visit Term {
      Anti(t)  -> {
        pos.add(getEnvironment().getPosition());
      }
    }
  }
 
  
  /**
   * Expand an anti-pattern
   * PEM's version
   * @param generatedRules initial set of rules
   * @param rule the rule to expand
   * @param extractedSignature the signature
   * @return nothing, but modifies generatedRules
   */
  public static void expandAntiPattern2(Collection<Rule> generatedRules, Rule rule, Map<String,Integer> extractedSignature) {
    try {
      `OnceBottomUp(ContainsAntiPattern()).visitLight(rule); // check if the rule contains an anti-pattern (exception otherwise)
      generatedRules.remove(rule); // remove the rule since it will be expanded
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
          expandAntiPattern2(generatedRules,expandr,extractedSignature);
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
      At(t1,t2)  -> { return `t2; }
      Var(_)  -> { return `Var("_"); }
    }
  }

  /*
   * generate a term for the form f(Z1,...,Zn)
   * @param name the symbol name 
   * @param arity the arity of the symbol
   * @return the string that represents the term
   */
  private static String genAbstractTerm(String name, int arity) {
    if(arity==0) {
      return name;
    } else {
      String z = getName("Z");
      String args = z+"_"+"1";
      for(int i=2 ; i<=arity ; i++) {
        args += ", " + z+"_"+i;
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
                Term newt = tools.encode(genAbstractTerm(otherName,arity));
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
              array[i] = `Anti(tarray[i]);
              Term newt = `Appl(name,sa.rule.types.termlist.TermList.fromArray(array));
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

}
