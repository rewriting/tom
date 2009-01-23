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

  private final static Term BOTTOM = `Appl("Bottom",TermList());
  private final static Term X = `Var("X");

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

  /**
   * encode: 
   * @param exp the string representation of the term to meta-encode
   * @return meta-encoding using Appl and TermList
   */
  private static Term encode(String stringterm) {
    //System.out.println("encode: " + stringterm);
    Term res = null;
    ATermFactory factory = SingletonFactory.getInstance();
    ATerm at = factory.parse(stringterm);
    res = encode(at);
    //System.out.println("encode: " + res);
    return res;
  }
  
  private static Rule encodeRule(String stringterm) {
    //System.out.println("encodeRule: " + stringterm);
    Rule res = null;
    ATermFactory factory = SingletonFactory.getInstance();
    ATerm at = factory.parse(stringterm);
    switch(at.getType()) {
	  	case ATerm.APPL:
	  		ATermAppl appl = (ATermAppl) at;
        String name = appl.getName();
        ATermList args = appl.getArguments();
        if(name.equals("rule") && args.getLength()==2) {
          Term lhs = encode(args.getFirst());
          Term rhs = encode(args.getNext().getFirst());
          res = `Rule(lhs,rhs);
        }
        break;
    }
    //System.out.println("encodeRule: " + res);
    return res;
  }

  private static boolean isVariableName(String name) {
    boolean res = true;
    for(int i=0 ; i<name.length(); i++) {
      res &= (Character.isUpperCase(name.charAt(i)) || Character.isDigit(name.charAt(i)));
    } 
    return res;
  }

  private static Term encode(ATerm at) {
    Term res = null;
    switch(at.getType()) {
	  	case ATerm.APPL:
	  		ATermAppl appl = (ATermAppl) at;
        String name = appl.getName();
        ATermList args = appl.getArguments();
        if(args.isEmpty() && isVariableName(name)) {
          res = `Var(name);
        } else if(name.equals("anti") && args.getLength()==1) {
          Term term = encode(args.getFirst());
          res = `Anti(term);
        } else if(name.equals("at") && args.getLength()==2) {
          Term var = encode(args.getFirst());
          Term term = encode(args.getNext().getFirst());
          res = `At(var,term);
        } else {
          res = `Appl(name,encodeList(args));
        }
       break; 
    }
    return res;
  }

  private static TermList encodeList(ATermList list) {
    if(list.isEmpty()) {
      return `TermList();
    } else {
      Term head = encode(list.getFirst());
      TermList tail = encodeList(list.getNext());
      return `TermList(head,tail*);
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
        String start = compileStrat(bag,extractedSignature,generatedSignature,`s);
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
      StratRule(Rule(lhs,rhs)) -> {
        String r = getName("rule");
        generatedSignature.put(r,1);
        // use AST-syntax because lhs and rhs are already encoded
        bag.add(`Rule(Appl(r,TermList(lhs)),rhs));
        bag.add(`Rule(Appl(r,TermList(At(X,Anti(lhs)))),encode("Bottom(X)")));
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
          bag.add(encodeRule(%[rule(@mu@(at(X,anti(Bottom(Y)))), @phi_s@(X))]%));
          bag.add(encodeRule(%[rule(@mu@(Bottom(X)), Bottom(X))]%));
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
        bag.add(encodeRule(%[rule(@id@(X), X)]%));
        return id;
      }

      StratFail() -> {
        String fail = getName("fail");
        generatedSignature.put(fail,1);
        bag.add(encodeRule(%[rule(@fail@(X), Bottom(X))]%));
        return fail;
      }

      StratSequence(s1,s2) -> {
        String n1 = compileStrat(bag,extractedSignature,generatedSignature,`s1);
        String n2 = compileStrat(bag,extractedSignature,generatedSignature,`s2);
        String seq = getName("seq");
        generatedSignature.put(seq,1);
        bag.add(encodeRule(%[rule(@seq@(X), @n2@(@n1@(X)))]%));
        return seq;
      }

      StratChoice(s1,s2) -> {
        String n1 = compileStrat(bag,extractedSignature,generatedSignature,`s1);
        String n2 = compileStrat(bag,extractedSignature,generatedSignature,`s2);
        String choice = getName("choice");
        String choice2 = getName("choice");
        generatedSignature.put(choice,1);
        generatedSignature.put(choice2,1);
        bag.add(encodeRule(%[rule(@choice@(X), @choice2@(@n1@(X)))]%));
        bag.add(encodeRule(%[rule(@choice2@(Bottom(X)), @n2@(X))]%));
        bag.add(encodeRule(%[rule(@choice2@(at(X,anti(Bottom(Y)))), X)]%));
        return choice;
      }

      StratAll(s) -> {
        String phi_s = compileStrat(bag,extractedSignature,generatedSignature,`s);
        String phi = getName("all");
        generatedSignature.put(phi,1);
        Map<Integer,String> mapphi = new HashMap<Integer,String>();

        Iterator<String> it = extractedSignature.keySet().iterator();
        while(it.hasNext()) {
          String name = it.next();
          int arity = generatedSignature.get(name);
          String phi_n = mapphi.get(arity);
          if(phi_n == null) {
            //phi_n = getName();
            phi_n = phi+"_"+arity;
            generatedSignature.put(phi_n,arity+1);
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
        String phi_s = compileStrat(bag,extractedSignature,generatedSignature,`s);
        String one = getName("one");
        generatedSignature.put(one,1);
        Iterator<String> it = extractedSignature.keySet().iterator();
        while(it.hasNext()) {
          String name = it.next();
          int arity = generatedSignature.get(name);
          if(arity==0) {
            bag.add(encodeRule(%[rule(@one@(@name@), Bottom(@name@))]%));
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
              bag.add(encodeRule(%[rule(@one@(@name@(@lx@)), @one_n@_1(@rx@))]%));
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
                bag.add(encodeRule(%[rule(@one_n_i@(@lx@), @one_n_ii@(@rx@))]%));
              } else {
                // one_f_n(Bottom(x1),...,Bottom(xn)) -> Bottom(f(x1,...,xn))
                String lx = "Bottom(X1)";
                String rx = "X1";
                for(int j=2; j<=arity;j++) {
                  lx += ",Bottom(X"+j+")";
                  rx += ",X"+j;
                }
                bag.add(encodeRule(%[rule(@one_n_i@(@lx@), Bottom(@name@(@rx@)))]%));

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
                bag.add(encodeRule(%[rule(@one_n_i@(@lx@), @name@(@rx@))]%));
              }

            }
          }
        }
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

  public static HashSet<Rule> expandAntiPatterns(List<Rule> bag, Map<String,Integer> extractedSignature) 
    throws VisitFailure {

    // generate depth 1 replacement terms for each symbol in the signature (+Bottom)
    // use extractedSignature since normally anti-patterns are only on symbols in signature + Bottom
      // expsig: associate f(Z1,...,ZN) to "f"
    Map<String,Term> expsig = new HashMap<String,Term>();
    extractedSignature.put("Bottom",1);
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

    HashSet<Rule> ruleSet = new HashSet<Rule>();
    for(Rule r:bag) {
      %match(r) {
        Rule(lhs,rhs) -> {
          // Generate LHSs without anti-pattern
          HashSet<Term> termSet = generateTermsWithoutAntiPatterns(`lhs,expsig);
          for(Term t: termSet) {
            // generate rule for each lhs generated 
            ruleSet.add(`Rule(t,rhs));
          }
        }
      }
    }

    return ruleSet;
  }
 
  public static HashSet<Term> generateTermsWithoutAntiPatterns(Term t, Map<String,Term> expsig) 
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
        %match(at) {
          Anti(Appl(name,_)) -> { symbolName = `name;}
        }
        if(symbolName.compareTo("doesntexist")==0) {
          System.out.println(" PROBLEM "); 
        } // TO CHANGE
        
        Set<String> gensig = new HashSet<String>(expsig.keySet());
        gensig.remove(symbolName); // all but current
        for(String sn: gensig) {
          Term t2p = (Term)pos.getReplace(expsig.get(sn)).visitLight(t);
          termSet.addAll(generateTermsWithoutAntiPatterns(t2p,expsig));
          //           System.out.println(" replace with  " + expsig.get(sn) + "  :  " + t2p);
        }
      }
    }
    
    return termSet;
  }

  %strategy CollectAntiPatterns(pos:HashSet) extends Identity() {
    visit Term {
      Anti(t)  -> {
        pos.add(getEnvironment().getPosition());
      }
    }
  }

}
