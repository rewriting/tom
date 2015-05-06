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
  %include { java/util/types/List.tom }

//   private  Tools tools = new Tools();
  private  Pretty pretty = new Pretty();

  private static int phiNumber = 0;
  private static String getName(String name) {
    return name + (phiNumber++);
  }

  /**
   * getTopName
   * @return the name of the strategy which starts the computation
   */
  private  String topName = "";
  public  String getTopName() {
    return topName;
  }


  /*
   * Extract all signatures and strategies
   * We suppose we always have let(_,signature,strategy)
   * For the moment a set of rules is not a strategy and not handled (see compileExp and Replace)
   */
  public  Map<String,Integer> extractSignaturesAndStrategies(List<Expression> signatures, List<Strat> strategies, ExpressionList expl) throws SymbolAlredyExistsException {
    %match(expl) {
      ExpressionList(_*,Let(_,sig@Signature(_),Strat(strat)),_*) -> {
            signatures.add(`sig);
            strategies.add(`strat);
      }
    }
    return expandSignature(signatures);
  }

  // Merge all signatures and test if compatible
  private  Map<String,Integer>  expandSignature(List<Expression> signatures) throws SymbolAlredyExistsException {
      Map<String,Integer> extractedSignature = new HashMap<String,Integer>();
      Integer previous;
      for(Expression signature:signatures){
        %match(signature) {
          Signature(SymbolList(_*,Symbol(name,arity),_*)) -> {
            // if symbol already exists
            if((previous=extractedSignature.put(`name,`arity)) != null){
              if(! previous.equals(`arity)){
                throw new SymbolAlredyExistsException();
              }
            }
          }
        }
      }
      return extractedSignature;
  }

  /*
   * Initialize the generated signature: add built-in symbols
   */
  public  Map<String,Integer>  generateSignature(Map<String,Integer> extractedSignature) throws SymbolAlredyExistsException {
    Integer previous;
    Map<String,Integer> generatedSignature = new HashMap<String,Integer>();

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

    for(String symbolName: extractedSignature.keySet()){
      Integer arity = extractedSignature.get(symbolName);
      // if built-in symbols used in the declared signatures
      if((previous=generatedSignature.put(symbolName,arity)) != null){
                throw new SymbolAlredyExistsException();
      }
    }
    return generatedSignature;
  }

  /*
   * Compile a (list of) strategy into a rewrite system
   */
  public  void compile(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, List<Strat> strategies) {
    // the (name of the last) strategy is used 
    for(Strat strategy:strategies){
        if(Main.options.generic) {
          topName = compileGenericStrat(bag,extractedSignature,generatedSignature,strategy);
        } else {
          topName = compileStrat(bag,extractedSignature,generatedSignature,strategy);
        }
    }

    if(Main.options.generic) {
      // do nothing
    } else {
      generateEquality(bag, extractedSignature, generatedSignature);
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
  private  String compileStrat(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, Strat strat) {
    String X = getName("X");
    String Y = getName("Y");
    String Z = getName("Z");
    Term varX = Tools.encode(X,generatedSignature);
    Term botX = Tools.encode("Bottom("+X+")",generatedSignature);
    Term True = Tools.encode("True",generatedSignature);
    Term False = Tools.encode("False",generatedSignature);

    %match(strat) {
      StratExp(Set(rulelist)) -> {
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
        String rule = getName("rule");
        generatedSignature.put(rule,1);
        String cr = getName("crule");
        generatedSignature.put(cr,2);
        %match(rulelist) {
          RuleList(_*,Rule(lhs,rhs),_*) -> {
            // use AST-syntax because lhs and rhs are already encoded
            // propagate failure; if the rule is applied to the result of a strategy that failed then the result is a failure
            bag.add(`Rule(Appl(rule,TermList(botX)),botX));

            TermList result = linearize(`lhs,generatedSignature);

            System.out.println("linear lhs = " + pretty.toString(result));

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
          bag.addAll(Tools.encodeRuleList(%[[
                rule(@mu@(at(@X@,anti(Bottom(@Y@)))), @phi_s@(@X@)),
                rule(@mu@(Bottom(@X@)), Bottom(@X@))
                ]]%,generatedSignature).getCollectionRuleList());
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
          bag.addAll(Tools.encodeRuleList(%[[
                rule(@id@(at(@X@,anti(Bottom(@Y@)))), @X@),
                rule(@id@(Bottom(@X@)), Bottom(@X@))
                ]]%,generatedSignature).getCollectionRuleList());
        } else { 
          // Bottom of Bottom is Bottom
          // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
          bag.addAll(Tools.encodeRuleList(%[[
                rule(@id@(@X@), @X@),
                rule(Bottom(Bottom(@X@)), Bottom(@X@))
                ]]%,generatedSignature).getCollectionRuleList());
        }
        return id;
      }

      StratFail() -> {
        String fail = getName("fail");
        generatedSignature.put(fail,1);
        if( !Main.options.approx ) {
          // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
          // normally it will follow reduction in original TRS
          bag.addAll(Tools.encodeRuleList(%[[
                rule(@fail@(at(X,anti(Bottom(@Y@)))), Bottom(X)),
                rule(@fail@(Bottom(X)), Bottom(X))
                ]]%,generatedSignature).getCollectionRuleList());
        } else { 
          // Bottom of Bottom is Bottom
          // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
          bag.addAll(Tools.encodeRuleList(%[[
                rule(@fail@(X), Bottom(X)),
                rule(Bottom(Bottom(@X@)), Bottom(@X@))
                ]]%,generatedSignature).getCollectionRuleList());
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
          bag.addAll(Tools.encodeRuleList(%[[
                rule(@seq@(at(@X@,anti(Bottom(@Y@)))), @seq2@(@n2@(@n1@(@X@)),@X@)),
                rule(@seq@(Bottom(@X@)), Bottom(@X@)),
                rule(@seq2@(at(@X@,anti(Bottom(@Y@))),@Z@), @X@),
                rule(@seq2@(Bottom(@Y@),@X@), Bottom(@X@))
                ]]%,generatedSignature).getCollectionRuleList());
        } else { 
          // Bottom of Bottom is Bottom
          // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
          bag.addAll(Tools.encodeRuleList(%[[
                rule(@seq@(@X@), @seq2@(@n2@(@n1@(@X@)),@X@)),
                rule(Bottom(Bottom(@X@)), Bottom(@X@)),
                rule(@seq2@(at(@X@,anti(Bottom(@Y@))),@Z@), @X@),
                rule(@seq2@(Bottom(@Y@),@X@), Bottom(@X@))
                ]]%,generatedSignature).getCollectionRuleList());
        }
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
        bag.addAll(Tools.encodeRuleList(%[[
              rule(@choice@(at(@X@,anti(Bottom(@Y@)))),  @choice2@(@n1@(@X@)) ),
              rule(@choice@(Bottom(@X@)), Bottom(@X@)),
              rule(@choice2@(at(@X@,anti(Bottom(@Y@)))), @X@),
              rule(@choice2@(Bottom(@X@)), @n2@(@X@))
              ]]%,generatedSignature).getCollectionRuleList());
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
            bag.add(Tools.encodeRule(%[rule(@all@(@name@), @name@)]%,generatedSignature));
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
              bag.add(Tools.encodeRule(%[rule(@all@(@name@(@lx@)), @all_n@(@rx@,@name@(@lx@)))]%,generatedSignature)); 
              // propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
              bag.add(Tools.encodeRule(%[rule(@all@(Bottom(@X@)), Bottom(@X@) )]%,generatedSignature));               
            }

            // generate success rules
            // all_g(x1@!BOTTOM,...,xN@!BOTTOM,_) -> g(x1,...,xN)
            String lx = "at(X1,anti(Bottom(Y1)))";
            String rx = "X1"; 
            for(int j=2; j<=arity;j++) {
              lx += ",at(X"+j+",anti(Bottom(Y"+j+")))";
              rx += ",X"+j;
            }
            bag.add(Tools.encodeRule(%[rule(@all_n@(@lx@,@Z@), @name@(@rx@))]%,generatedSignature));

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
              bag.add(Tools.encodeRule(%[rule(@all_n@(@llx@,@Z@), Bottom(@Z@))]%,generatedSignature));
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
            bag.add(Tools.encodeRule(%[rule(@one@(@name@), Bottom(@name@))]%,generatedSignature));
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
              bag.add(Tools.encodeRule(%[rule(@one@(@name@(@lx@)), @one_n@_1(@rx@))]%,generatedSignature));
              // propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
              bag.add(Tools.encodeRule(%[rule(@one@(Bottom(@X@)), Bottom(@X@) )]%,generatedSignature));               
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
                bag.add(Tools.encodeRule(%[rule(@one_n_i@(@lx@), @one_n_ii@(@rx@))]%,generatedSignature));
              } else {
                // one_f_n(Bottom(x1),...,Bottom(xn)) -> Bottom(f(x1,...,xn))
                String lx = "Bottom(X1)";
                String rx = "X1";
                for(int j=2; j<=arity;j++) {
                  lx += ",Bottom(X"+j+")";
                  rx += ",X"+j;
                }
                bag.add(Tools.encodeRule(%[rule(@one_n_i@(@lx@), Bottom(@name@(@rx@)))]%,generatedSignature));
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
              bag.add(Tools.encodeRule(%[rule(@one_n_i@(@lx@), @name@(@rx@))]%,generatedSignature));

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
  private  boolean generated_aux_functions = false;
  private  String compileGenericStrat(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, Strat strat) {
    String X = getName("X");
    String Y = getName("Y");
    String Z = getName("Z");
    String XX = getName("XX");
    String YY = getName("YY");
    String Z0 = getName("Z0");
    String Z1 = getName("Z1");
    String Z2 = getName("Z2");
    String Z3 = getName("Z3");
    Term varX = Tools.encode(X,generatedSignature);
    Term botX = Tools.encode("Bottom("+X+")",generatedSignature);
    Term True = Tools.encode("True",generatedSignature);
    Term False = Tools.encode("False",generatedSignature);

    %match(strat) {
      StratExp(List(rulelist)) -> {
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
        String rule = getName("rule");
        generatedSignature.put(rule,1);
        String cr = getName("crule");
        generatedSignature.put(cr,2);
        %match(rulelist) {
          RuleList(_*,Rule(lhs,rhs),_*) -> {
            Term mlhs = Tools.metaEncodeConsNil(`lhs,generatedSignature);
            Term mrhs = Tools.metaEncodeConsNil(`rhs,generatedSignature);

            // use AST-syntax because lhs and rhs are already encoded
            //System.out.println("lhs = " + `lhs);
            //System.out.println("encode lhs = " + mlhs);
            //System.out.println("rhs = " + `rhs);
            //System.out.println("encode rhs = " + mrhs);
            //System.out.println("decode(encode rhs) = " + Tools.decodeConsNil(Tools.metaEncodeConsNil(`rhs,generatedSignature)));

            bag.add(`Rule(Appl(rule,TermList(botX)),botX));
            //bag.add(`Rule(Appl(rule,TermList(mlhs)),mrhs));

            TermList result = linearize(`lhs,generatedSignature);
            %match(result) {
              TermList(linearlhs, cond) -> {
                Term mlinearlhs = Tools.metaEncodeConsNil(`linearlhs,generatedSignature);

                bag.add(`Rule(Appl(rule,TermList(At(varX,mlinearlhs))),
                              Appl(cr, TermList(varX, cond))));
                bag.add(`Rule(Appl(rule,TermList(At(varX,Anti(mlinearlhs)))),botX));

                bag.add(`Rule(Appl(cr,TermList(mlinearlhs, True)), rhs));
                bag.add(`Rule(Appl(cr,TermList(At(varX,mlinearlhs), False)),botX));
              }
            }

            // TODO: non-linear anti-pattern
            bag.add(`Rule(Appl(rule,TermList(At(varX,Anti(Tools.metaEncodeConsNil(lhs,generatedSignature))))),botX));
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
          bag.add(Tools.encodeRule(%[rule(@mu@(Appl(@Y@,@Z@)), @phi_s@(Appl(@Y@,@Z@)))]%,generatedSignature));
          bag.add(Tools.encodeRule(%[rule(@mu@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
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
          bag.add(Tools.encodeRule(%[rule(@id@(Appl(@X@,@Y@)), Appl(@X@,@Y@))]%,generatedSignature));
          bag.add(Tools.encodeRule(%[rule(@id@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        } else {
          bag.add(Tools.encodeRule(%[rule(@id@(@X@), @X@)]%,generatedSignature));
          bag.add(Tools.encodeRule(%[rule(Bottom(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        }
        return id;
      }

      StratFail() -> {
        String fail = getName("fail");
        generatedSignature.put(fail,1);
        if( !Main.options.approx ) {
          bag.add(Tools.encodeRule(%[rule(@fail@(Appl(@X@,@Y@)), Bottom(Appl(@X@,@Y@)))]%,generatedSignature));
          bag.add(Tools.encodeRule(%[rule(@fail@(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
        } else { 
          bag.add(Tools.encodeRule(%[rule(@fail@(X), Bottom(X))]%,generatedSignature));
          bag.add(Tools.encodeRule(%[rule(Bottom(Bottom(@X@)), Bottom(@X@))]%,generatedSignature));
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
          bag.addAll(Tools.encodeRuleList(%[[
                rule(@seq@(Appl(@X@,@Y@)), @seq2@(@n2@(@n1@(Appl(@X@,@Y@))),Appl(@X@,@Y@))),
                rule(@seq@(Bottom(@X@)), Bottom(@X@)),
                rule(@seq2@(Appl(@X@,@Y@),@Z@), Appl(@X@,@Y@)),
                rule(@seq2@(Bottom(@Y@),@X@), Bottom(@X@))
                ]]%,generatedSignature).getCollectionRuleList());
        } else { 
          bag.add(Tools.encodeRule(%[rule(@seq@(@X@), @n2@(@n1@(@X@)))]%,generatedSignature));
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
          bag.addAll(Tools.encodeRuleList(%[[
                rule(@choice@(Appl(@X@,@Y@)), @choice2@(@n1@(Appl(@X@,@Y@)))),
                rule(@choice@(Bottom(@X@)), Bottom(@X@)),
                rule(@choice2@(Appl(@X@,@Y@)), Appl(@X@,@Y@)),
                rule(@choice2@(Bottom(@X@)), @n2@(@X@))
                ]]%,generatedSignature).getCollectionRuleList());
        } else {
          bag.addAll(Tools.encodeRuleList(%[[
                rule(@choice@(@X@), @choice2@(@n1@(@X@))),
                rule(@choice2@(Appl(@X@,@Y@)), Appl(@X@,@Y@)),
                rule(@choice2@(Bottom(@X@)), @n2@(@X@))
                ]]%,generatedSignature).getCollectionRuleList());
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
        generatedSignature.put(all_3,4);
        
	String append = "append";
        generatedSignature.put(append,2); 
	String reverse = "reverse";
        generatedSignature.put(reverse,1); 
	String rconcat = "rconcat";
        generatedSignature.put(rconcat,2); 
	
        bag.addAll(Tools.encodeRuleList(%[[
              // all
              rule(@all@(Appl(@Z0@,@Z1@)), @all_1@(Appl(@Z0@,@all_2@(@Z1@)))),
              // all_1
              rule(@all_1@(Appl(@Z0@,BottomList(@Z@))), Bottom(Appl(@Z0@,@Z@))),
              rule(@all_1@(Appl(@Z0@,Cons(@Z1@,@Z2@))), Appl(@Z0@,Cons(@Z1@,@Z2@))),
              rule(@all_1@(Appl(@Z0@,Nil)) , Appl(@Z0@,Nil)),

              rule(@all_2@(Nil()) , Nil()),
              rule(@all_2@(Cons(@Z1@,@Z2@)) , @all_3@(@phi_s@(@Z1@),@Z2@,Cons(@Z1@,Nil),Nil)),
              /*
               * all_3(Bottom(X),todo,rargs,rs_args) -> BottomList(rconcat(rargs,todo))
               * all_3(Appl(X,Y),Nil,rargs,rs_args) -> reverse(Cons(Appl(X,Y),rs_args))
               * all_3(Appl(X,Y), Cons(XX,YY), rargs, rs_args) -> 
               * all_3(rule47(XX), YY, Cons(XX,rargs), Cons(Appl(X,Y),rs_args))
               */ 
              rule(@all_3@(Bottom(@X@),@Z1@,@Z2@,@Z3@) , BottomList(@rconcat@(@Z2@,@Z1@))),
              rule(@all_3@(Appl(@X@,@Y@),Nil,@Z2@,@Z3@) , @reverse@(Cons(Appl(@X@,@Y@),@Z3@))),
              rule(@all_3@(Appl(@X@,@Y@),Cons(@XX@,@YY@),@Z2@,@Z3@) , @all_3@(@phi_s@(@XX@),@YY@,Cons(@XX@,@Z2@), Cons(Appl(@X@,@Y@),@Z3@)))
              ]]%,generatedSignature).getCollectionRuleList());

	if(!generated_aux_functions) { 
	  generated_aux_functions = true;
    bag.addAll(Tools.encodeRuleList(%[[
          rule(@append@(Nil,@Z@), Cons(@Z@,Nil)),
          rule(@append@(Cons(@X@,@Y@),@Z@), Cons(@X@,@append@(@Y@,@Z@))),

          rule(@reverse@(Nil), Nil),
          rule(@reverse@(Cons(@X@,@Y@)), @append@(@reverse@(@Y@),@X@)),

          rule(@rconcat@(Nil,@Z@), @Z@),
          rule(@rconcat@(Cons(@X@,@Y@),@Z@), @rconcat@(@Y@,Cons(@X@,@Z@)))
          ]]%,generatedSignature).getCollectionRuleList());
	}
    
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
        generatedSignature.put(one_3,3);

	String append = "append";
        generatedSignature.put(append,2); 
	String reverse = "reverse";
        generatedSignature.put(reverse,1); 
	String rconcat = "rconcat";
        generatedSignature.put(rconcat,2); 

        bag.addAll(Tools.encodeRuleList(%[[
              // one
              rule(@one@(Appl(@Z0@,@Z1@)), @one_1@(Appl(@Z0@,@one_2@(@Z1@)))),
              // one_1
              rule(@one_1@(Appl(@Z0@,BottomList(@Z@))), Bottom(Appl(@Z0@,@Z@))),
              rule(@one_1@(Appl(@Z0@,Cons(@X@,@Y@))), Appl(@Z0@,Cons(@X@,@Y@))),
              // one_2
              rule(@one_2@(Nil()), BottomList(Nil())),
              rule(@one_2@(Cons(@X@,@Y@)), @one_3@(@phi_s@(@X@),@Y@,Cons(@X@,Nil))),
              // one_3
              /*
               * one_3(Bottom(X),Nil,rargs) -> BottomList(reverse(rargs))
               * one_3(Bottom(X),Cons(head,tail),rargs) -> ONE(head, tail, Cons(head,rargs))
               * one_3(Appl(X,Y),todo,Cons(last,rargs)) -> rconcat(rargs,Cons(Appl(X,Y),todo))
               */
              rule(@one_3@(Bottom(@Z@),Nil, @Z2@), BottomList(@reverse@(@Z2@))),
              rule(@one_3@(Bottom(@Z@),Cons(@XX@,@YY@),@Z2@), @one_3@(@XX@, @YY@, Cons(@XX@,@Z2@))),
              rule(@one_3@(Appl(@X@,@Y@),@Z1@, Cons(@Z2@,@Z3@)), @rconcat@(@Z3@, Cons(Appl(@X@,@Y@),@Z1@)))
              ]]%,generatedSignature).getCollectionRuleList());

	if(!generated_aux_functions) { 
	  generated_aux_functions = true;
    bag.addAll(Tools.encodeRuleList(%[[
          rule(@append@(Nil,@Z@), Cons(@Z@,Nil)),
          rule(@append@(Cons(@X@,@Y@),@Z@), Cons(@X@,@append@(@Y@,@Z@))),

          rule(@reverse@(Nil), Nil),
          rule(@reverse@(Cons(@X@,@Y@)), @append@(@reverse@(@Y@),@X@)),

          rule(@rconcat@(Nil,@Z@), @Z@),
          rule(@rconcat@(Cons(@X@,@Y@),@Z@), @rconcat@(@Y@,Cons(@X@,@Z@)))
          ]]%,generatedSignature).getCollectionRuleList());
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
  public  ExpressionList expand(ExpressionList expl) {
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
  public  Collection<Rule> expandAt(Collection<Rule> bag) throws VisitFailure {
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
  public  void expandAntiPattern(Collection<Rule> generatedRules, Rule rule, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature) {
    try {
      `OnceBottomUp(ContainsAntiPattern()).visitLight(rule); // check if the rule contains an anti-pattern (exception otherwise)
      generatedRules.remove(rule); // remove the rule since it will be expanded
      //       System.out.println("RULE: "+`rule);
      Collection<Rule> bag = new HashSet<Rule>();
      // perform one-step expansion
      `OnceTopDown(ExpandAntiPattern(bag,rule,extractedSignature, generatedSignature)).visit(rule);
      for(Rule expandr:bag) {
        expandAntiPattern(generatedRules,expandr,extractedSignature,generatedSignature);
      }
    } catch(VisitFailure e) {
      // add the rule since it contains no more anti-pattern
      generatedRules.add(rule);
    }
  }

  private  boolean matchModuloAt(Term pattern, Term subject) {
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
  // MOVE to Tools ???
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
  %strategy ExpandAntiPattern(bag:Collection,subject:Rule,extractedSignature:Map, generatedSignature:Map) extends Fail() {
    visit Term {
      Anti(Anti(t)) -> {
        Rule newr = (Rule) getEnvironment().getPosition().getReplace(`t).visit(subject);
        //System.out.println("add bag0:" + pretty.toString(newr));
        bag.add(newr);
        return `t;
      }

      Anti(t) -> {
        //System.out.println("ExpandAntiPattern: " + `t);
        Term antiterm = (Main.options.generic)?Tools.decodeConsNil(`t):`t;
        //System.out.println("ExpandAntiPattern antiterm: " + antiterm);
        %match(antiterm) { 
          Appl(name,args)  -> {
            // add g(Z1,...) ... h(Z1,...)
            Set<String> otherNames = new HashSet<String>( ((Map<String,Integer>)extractedSignature).keySet() );
            for(String otherName:otherNames) {
              if(!`name.equals(otherName)) {
                int arity = ((Map<String,Integer>)extractedSignature).get(otherName);
                Term newt = Tools.encode(genAbstractTerm(otherName,arity,Compiler.getName("Z")),generatedSignature);
                if(Main.options.generic) {
                  newt = Tools.metaEncodeConsNil(newt,generatedSignature);
                }
                Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);
                //System.out.println("add bag1:" + pretty.toString(newr));
                bag.add(newr);
              }
            }
            
            // add f(!a1,...) ... f(a1,...,!an)
            sa.rule.types.termlist.TermList tl = (sa.rule.types.termlist.TermList) `args;
            int arity = tl.length();
            Term[] array = new Term[arity];
            Term[] tarray = new Term[arity];
            tarray = tl.toArray(tarray);
            String z = Compiler.getName("Z");
            for(int i=0 ; i<arity ; i++) {
              array[i] = Tools.encode(z+"_"+i,generatedSignature);
            }
            for(int i=0 ; i<arity ; i++) {
              Term ti = tarray[i];
              array[i] = `Anti(ti);
              Term newt = `Appl(name,sa.rule.types.termlist.TermList.fromArray(array));
              //               System.out.println("NEWT:"+`newt);
              array[i] = Tools.encode(z+"_"+i,generatedSignature);
              if(Main.options.generic) {
                newt = Tools.metaEncodeConsNil(newt,generatedSignature);
              }
              Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);

              //System.out.println("add bag2:" + pretty.toString(newr));
              bag.add(newr);
            }
          }
        }
        return `t;
      }
    }
  }


  /*
   * Transform lhs into linear-lhs + true ^ constraint on non linear variables
   */
  private  TermList linearize(Term lhs, Map<String,Integer> signature) {
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

    return `TermList(lhs,constraint);

  }
  
  %strategy ReplaceWithFreshVar(name:String, multiplicityMap:Map, map:Map, signature:Map) extends Identity() {
    visit Term {
      Var(n)  -> {
        int value = (Integer)multiplicityMap.get(`name);
        if(`n.compareTo(`name)==0 && value>1) {
          String z = Compiler.getName("Z");
          map.put(z,`n);
          multiplicityMap.put(`name, value - 1);
          Term newt = `Var(z);
          if(Main.options.generic) {
            newt = Tools.metaEncodeConsNil(newt,signature);
          }
          return newt;
        }
      }
    }
  }

  
  /**
   * Returns a Map which associates an integer to each variable name
   */
  public  Map<String,Integer> collectMultiplicity(tom.library.sl.Visitable subject) {
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

  // search all Var and store their values
  %strategy CollectVars(bag:Collection) extends Identity() {
    visit Term {
      Var(name)-> {
        bag.add(`name);
      }
    }
  }

  /*
   * Generate equality rules:
   * f(x_1,...,x_n) = g(y_1,...,y_m) -> False
   * f(x_1,...,x_n) = f(y_1,...,y_n) -> x_1=y1 ^ ... ^ x_n=y_n ^ true
   *
   */
  private  void generateEquality(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature) {

    bag.add(Tools.encodeRule(%[rule(and(True,True), True)]%,generatedSignature));
    bag.add(Tools.encodeRule(%[rule(and(True,False), False)]%,generatedSignature));
    bag.add(Tools.encodeRule(%[rule(and(False,True), False)]%,generatedSignature));
    bag.add(Tools.encodeRule(%[rule(and(False,False), False)]%,generatedSignature));

    String z1 = getName("Z");
    String z2 = getName("Z");
    for(String f:extractedSignature.keySet()) {
      for(String g:extractedSignature.keySet()) {
        int arf = extractedSignature.get(f);
        int arg = extractedSignature.get(g);
        if(!f.equals(g)) {
          bag.add(Tools.encodeRule(%[rule(eq(@genAbstractTerm(f,arf,z1)@,@genAbstractTerm(g,arg,z2)@), False)]%,generatedSignature));
        } else {
          String t1 = genAbstractTerm(f,arf,z1);
          String t2 = genAbstractTerm(f,arf,z2);
          String scond = "True";
          for(int i=1 ; i<=arf ; i++) {
            scond = %[and(eq(@z1@_@i@,@z2@_@i@),@scond@)]%;
          }
          //System.out.println(t1 + " = " + t2 + " --> " + scond);
          bag.add(Tools.encodeRule(%[rule(eq(@t1@,@t2@),@scond@)]%,generatedSignature));
        }
      }
    }
    //bag.add(Tools.encodeRule(%[rule(@mu@(Bottom(X)), Bottom(X))]%));
  }




  private  Position getAntiPatternPosition(Term t) {
    List<Position> list = new ArrayList<Position>();
    try {
      `SearchAntiPattern(list).visit(t);
      return list.get(0);
    } catch(VisitFailure e) {
      // no anti pattern found
      return null;
    }
  }

  %strategy SearchAntiPattern(list:List) extends Fail() {
    visit Term {
      s@Anti(t) -> {
        list.add(0,getEnvironment().getPosition());
        return `s;
      }
    }
  }

  /*
   * Perform one-step expansion
   *
   * @param ordered list of rules
   * @param rule the rule to expand (may contain nested anti-pattern and be non-linear)
   * @param extractedSignature the signature
   */
  %strategy ExpandGeneralAntiPattern(orderedTRS:List,subject:Rule,extractedSignature:Map, generatedSignature:Map) extends Fail() {
    visit Term {
      Anti(Anti(t)) -> {
        Rule newr = (Rule) getEnvironment().getPosition().getReplace(`t).visit(subject);
        //System.out.println("add list0:" + pretty.toString(newr));
        `orderedTRS.add(`orderedTRS.size(),newr);
        return `t;
      }

      Anti(t) -> {
        /*
         * x@q[!q'] -> r      becomes x@q[q'] -> bot(x)
         *                              q[z]  -> r
         *
         * x@q[!q'] -> bot(_) becomes   q[q'] -> r
         *                            x@q[z]  -> bot(x)
         */
        return `t;
      }
    }
  }



// OLD version


  /*
   * Compile a strategy into a rewrite system
   */
  public  void compileOLD(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, ExpressionList expl) {

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


  private  void compileExp(Collection<Rule> bag, Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature, Expression e) {
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



}
