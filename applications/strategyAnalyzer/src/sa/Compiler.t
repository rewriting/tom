package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;

public class Compiler {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/List.tom }

  %typeterm Compiler { implement { Compiler }}

  private static Compiler instance = null;

  // initial AST
  private Program program;

  // The extracted (concrete) signature
  private Signature extractedSignature;
  // The generated (concrete) signature
  private Signature generatedSignature;

  // strategy name -> strategy compiled into a TRS
  private Map<String,List<Rule>> generatedTRSs;

  private Map<Strategy,List<Rule>> storedTRSs;

  /**
   * initialize the TRS and set the (generated) symbol that should be
   * used to wrap the terms to reduce
   * 
   */
  private Compiler() {
    this.generatedTRSs = new HashMap<String,List<Rule>>();
  }

  /**
   * get the instance of the Singleton
   * 
   */
  public static Compiler getInstance() {
    if(instance == null) {
      instance = new Compiler();
    }
    return instance;
  }

  /**
   * get the names of the compiled strategies
   * @return the names of the compiled strategies
   */
  public List<String> getStrategyNames() {
    return new ArrayList(generatedTRSs.keySet());
  }

  public void setProgram(Program program) throws TypeMismatchException {
    this.extractedSignature = new Signature();
    this.extractedSignature.setSignature(program);
    this.generatedSignature = this.extractedSignature.expandSignature();

    this.program = program;
  }

  public Signature getExtractedSignature() {
    return this.extractedSignature;
  }

  public Signature getGeneratedSignature() {
    return this.generatedSignature;
  }


  /**
   * Compile the strategy strategyName into a rewrite system.
   * @param strategyName the name of the strategy to compile
   * @return the TRS for strategyName 
   */
  public List<Rule>  compileStrategy(String strategyName) {
    Expression expand = this.expandStrategy(strategyName);
    System.out.println("expanded version for "+strategyName+"  = " + expand);

    Strat strategy=null;
    %match(expand) {
      Strat(strat) -> { strategy = `strat;      }
    }
    System.out.println("STRATEGY "+strategyName+"  = " + strategy);

    this.generatedTRSs.put(strategyName,new ArrayList<Rule>());

    if(Main.options.metalevel) {
      this.compileStrat(strategyName,strategy,this.generatedTRSs.get(strategyName));
    } else {
      this.compileStrat(strategyName,strategy,this.generatedTRSs.get(strategyName));
      this.generateEquality(this.generatedTRSs.get(strategyName));
    }

    return new ArrayList(this.generatedTRSs.get(strategyName));
  }


  /*
   * Given a name, retrieve the corresponding StratDecl (which should not have parameter)
   * and return an expanded version of the body 
   * The resulting strategy is self-contained
   */
  public Expression expandStrategy(String name) {
    StratDecl sd = Tools.getStratDecl(name, this.program);
    Expression res = null;

    try {
      %match(sd) {
        StratDecl(name, ConcParam() , body) -> {
            res = `RepeatId(TopDown(ExpandStratAppl(this))).visit(`body);
        }
      }
    } catch(VisitFailure e) {
    }

    return res;
  }

  /*
   * used by expandStrategy
   * for each StratAppl:
   * retrieve the corresponding StratDecl
   * rename with fresh variables
   * apply the macro expansion 
   */
  %strategy ExpandStratAppl(compiler:Compiler) extends Identity() {
    visit Strat {
      StratAppl(name, args) -> {
        StratDecl sd = Tools.getStratDecl(`name, compiler.program);
        Map map = new HashMap();
        sd = `TopDown(FreshStratDecl(map)).visitLight(sd);
        Expression si = compiler.instantiateStrategy(sd, `args);
        return `StratExp(si);
      }
    }
  }

  /*
   * Rename the variables of a StratDecl into fresh names
   */
  %strategy FreshStratDecl(map:Map) extends Identity() {
    visit Param {
      Param(n) -> {
        String newName = (String) map.get(`n);
        if(newName == null) {
          newName = Tools.getName("_"+`n);
          map.put(`n,newName);
        }
        return `Param(newName);
      }
    }

    visit Strat {
      s@(StratName|StratMu)[name=n] -> {
        String newName = (String) map.get(`n);
        if(newName == null) {
          newName = Tools.getName("_"+`n);
          map.put(`n,newName);
        }
        return `s.setname(newName);
      }
    }
  }


  /*
   * Given a StratDecl (name, params, body)
   * Replace the parameters by their effective values (args) in body
   * i.e. apply the substitution [param_1 -> arg_1, ..., param_n -> arg_n]
   */
  public Expression instantiateStrategy(StratDecl sd, StratList args) {
    Expression res = null;

    try {
      %match(sd) {
        StratDecl(name, params, body) -> {
          if(`params.length() == args.length()) {
            res = `(TopDown(ReplaceParameters(params,args))).visit(`body);
          }
        }
      }
    } catch(VisitFailure e) {
    }

    return res;
  }

  /*
   * used by instantiateStrategy to apply the substitution
   */
  %strategy ReplaceParameters(params:ParamList, args:StratList) extends Identity() {
    visit Strat {
      StratName(n) -> {
          //System.out.println("stratname = " + `n); 
          //System.out.println("params = " + params); 
          //System.out.println("args = " + args); 
          ParamList plist = params;
          StratList slist = args;

        while(!plist.isEmptyConcParam() && !slist.isEmptyConcStrat()) {
          Param p = plist.getHeadConcParam();
          Strat s = slist.getHeadConcStrat();
          //System.out.println("param = " + p + " -- arg = " + s); 
          %match(p) {
            Param(name) && n==name -> {
              return s;
            }
          }
          plist = plist.getTailConcParam();
          slist = slist.getTailConcStrat();
        }
      }
    }
  }


  /**
   * compile a strategy in a classical way (without using meta-representation)
   * return the name of the top symbol (phi) introduced
   * @param stratName the name of the strategy to compile
   * @param strat the strategy to compile
   * @param generatedRules the list of rewrite rules generated for this strategy
   * @return the name of the last compiled strategy
   */
  private boolean generated_aux_functions = false;
  private String compileStrat(String stratName, Strat strat, List<Rule> generatedRules) {
  // by default, if strategy can't be compiled, a meaningless name
    // TODO: change to exception ?
    String strategySymbol = strat.toString();

    String X = Tools.getName("X");
    String Y = Tools.getName("Y");
    String Z = Tools.getName("Z");
    String XX = Tools.getName("XX");
    String YY = Tools.getName("YY");
    String Z0 = Tools.getName("Z0");
    String Z1 = Tools.getName("Z1");
    String Z2 = Tools.getName("Z2");
    String Z3 = Tools.getName("Z3");
    Term varX = Tools.encode(X,this.generatedSignature);
    Term botX = Tools.encode("Bottom("+X+")",this.generatedSignature);
    Term True = Tools.encode("True",this.generatedSignature);
    Term False = Tools.encode("False",this.generatedSignature);

    %match(strat) {
      StratExp((Set|List)(rulelist)) -> {
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

        String rule = (stratName!=null)?stratName:Tools.getName("rule");
        String cr = Tools.getName("crule");

        if(!Main.options.metalevel) {
          // if declared strategy (i.e. defined name) use its name; otherwise generate fresh name
          this.generatedSignature.addSymbol(rule,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          this.generatedSignature.addSymbol(cr,Arrays.asList(Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);

          %match(rulelist) {
            RuleList(_*,Rule(lhs,rhs),_*) -> {
              // use AST-syntax because lhs and rhs are already encoded

              // propagate failure; if the rule is applied to the result of a strategy that failed then the result is a failure
              generatedRules.add(`Rule(Appl(rule,TermList(botX)),botX));

              TermList result = this.linearize(`lhs);
              Term constraint = `Appl("True",TermList());

              %match(result) {
                // if already linear lhs
                TermList(_, Appl("True",TermList())) -> {
                  generatedRules.add(`Rule(Appl(rule,TermList(At(varX,lhs))),rhs));
                  generatedRules.add(`Rule(Appl(rule,TermList(At(varX,Anti(lhs)))),botX));
                }

                // if non-linear add rules for checking equality for corresponding arguments
                TermList(linearlhs, cond@!Appl("True",TermList())) -> {
                  generatedRules.add(`Rule(Appl(rule,TermList(At(varX,linearlhs))),Appl(cr, TermList(varX, cond))));
                  generatedRules.add(`Rule(Appl(rule,TermList(At(varX,Anti(linearlhs)))),botX));

                  generatedRules.add(`Rule(Appl(cr,TermList(linearlhs, True)), rhs));
                  generatedRules.add(`Rule(Appl(cr,TermList(At(varX,linearlhs), False)),botX));
                }
              }
            }
          }
        } else {
          // META-LEVEL
          this.generatedSignature.addSymbol(rule,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          this.generatedSignature.addSymbol(cr,Arrays.asList(Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);

          %match(rulelist) {
            RuleList(_*,Rule(lhs,rhs),_*) -> {
              // use AST-syntax because lhs and rhs are already encoded
              // propagate failure
              generatedRules.add(`Rule(Appl(rule,TermList(botX)),botX));

              TermList result = this.linearize(`lhs);
              Term constraint = `Appl("True",TermList());

              Term mlhs = Tools.metaEncodeConsNil(`lhs,generatedSignature);
              Term mrhs = Tools.metaEncodeConsNil(`rhs,generatedSignature);

              //System.out.println("RuleList lhs  = " + `lhs);
              //System.out.println("RuleList mlhs = " + mlhs);
              %match(result) {
                // if already linear lhs
                TermList(_, Appl("True",TermList())) -> {
                  generatedRules.add(`Rule(Appl(rule,TermList(At(varX,mlhs))), mrhs));
                  generatedRules.add(`Rule(Appl(rule,TermList(At(varX,Anti(mlhs)))),botX));
                }

                // if non-linear add rules for checking equality for corresponding arguments
                TermList(linearlhs, cond@!Appl("True",TermList())) -> {
                  Term mlinearlhs = Tools.metaEncodeConsNil(`linearlhs,generatedSignature);

                  generatedRules.add(`Rule(Appl(rule,TermList(At(varX,mlinearlhs))),Appl(cr, TermList(varX, cond))));
                  generatedRules.add(`Rule(Appl(rule,TermList(At(varX,Anti(mlinearlhs)))),botX));

                  generatedRules.add(`Rule(Appl(cr,TermList(mlinearlhs, True)), mrhs));
                  generatedRules.add(`Rule(Appl(cr,TermList(At(varX,mlinearlhs), False)),botX));
                }
              }

              // TODO: non-linear anti-pattern
              // generatedRules.add(`Rule(Appl(rule,TermList(At(varX,Anti(mlhs)))),botX));
            }
          }

          for(String name:this.extractedSignature.getSymbolNames()) {
            // add symb_a(), symb_b(), symb_f(), symb_g() in the signature
            this.generatedSignature.addSymbol("symb_"+name,new ArrayList<String>(),Signature.DUMMY);
          }
        }
        strategySymbol=rule;
      }

      /*
       * TODO: fix non confluence here
       */
      StratMu(name,s) -> {
        try {
          String mu = (stratName!=null)?stratName:Tools.getName("mu");
          this.generatedSignature.addSymbol(mu,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          Strat newStrat = `TopDown(ReplaceMuVar(name,mu)).visitLight(`s);
          String phi_s = compileStrat(null,newStrat,generatedRules);
          if(!Main.options.metalevel) {
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@mu@(at(@X@,anti(Bottom(@Y@)))), @phi_s@(@X@)),
                  rule(@mu@(Bottom(@X@)), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          } else {
            // META-LEVEL
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@mu@(Appl(@Y@,@Z@)), @phi_s@(Appl(@Y@,@Z@))),
                  rule(@mu@(Bottom(@X@)), Bottom(@X@)) 
                  ]]%,this.generatedSignature).getCollectionRuleList());
          }
          strategySymbol = phi_s;
        } catch(VisitFailure e) {
          System.out.println("failure in StratMu on: " + `s);
        }
      }

      // mu fix point: transform the stratame into a function call
      StratName(name) -> {
        strategySymbol = `name;
      }

      StratIdentity() -> {
        String id = (stratName!=null)?stratName:Tools.getName("id");
        if(!Main.options.metalevel) {
          this.generatedSignature.addSymbol(id,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          if( !Main.options.approx ) {
            // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
            // normally it will follow reduction in original TRS
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@id@(at(@X@,anti(Bottom(@Y@)))), @X@),
                  rule(@id@(Bottom(@X@)), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          } else { 
            // Bottom of Bottom is Bottom
            // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@id@(@X@), @X@),
                  rule(Bottom(Bottom(@X@)), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          }
        } else {
          // META-LEVEL
          this.generatedSignature.addSymbol(id,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          if( !Main.options.approx ) {
            // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
            // normally it will follow reduction in original TRS
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@id@(Appl(@X@,@Y@)), Appl(@X@,@Y@)),
                  rule(@id@(Bottom(@X@)), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          } else { 
            // Bottom of Bottom is Bottom
            // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@id@(@X@), @X@),
                  rule(Bottom(Bottom(@X@)), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          }
        }
        strategySymbol = id;
      }

      StratFail() -> {
        String fail = (stratName!=null)?stratName:Tools.getName("fail");
        this.generatedSignature.addSymbol(fail,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
        if( !Main.options.metalevel ) {
          if( !Main.options.approx ) {
            // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
            // normally it will follow reduction in original TRS
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@fail@(at(@X@,anti(Bottom(@Y@)))), Bottom(@X@)),
                  rule(@fail@(Bottom(@X@)), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          } else { 
            // Bottom of Bottom is Bottom
            // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@fail@(@X@), Bottom(@X@)),
                  rule(Bottom(Bottom(@X@)), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          }
        } else {
          // META-LEVEL
          if( !Main.options.approx ) {
            // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
            // normally it will follow reduction in original TRS
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@fail@(Appl(@X@,@Y@)), Bottom(Appl(@X@,@Y@))),
                  rule(@fail@(Bottom(@X@)), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          } else { 
            // Bottom of Bottom is Bottom
            // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@fail@(@X@), Bottom(@X@)),
                  rule(Bottom(Bottom(@X@)), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          }

        }
        strategySymbol = fail;
      }

      StratSequence(s1,s2) -> {
        String n1 = compileStrat(null,`s1,generatedRules);
        String n2 = compileStrat(null,`s2,generatedRules);
        String seq = (stratName!=null)?stratName:Tools.getName("seq");
        String seq2 = Tools.getName("seq2");
        if( !Main.options.metalevel ) {
          this.generatedSignature.addSymbol(seq,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          this.generatedSignature.addSymbol(seq2,Arrays.asList(Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);
          if( !Main.options.approx ) {
            // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
            // normally it will follow reduction in original TRS
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@seq@(at(@X@,anti(Bottom(@Y@)))), @seq2@(@n2@(@n1@(@X@)),@X@)),
                  rule(@seq@(Bottom(@X@)), Bottom(@X@)),
                  rule(@seq2@(at(@X@,anti(Bottom(@Y@))),@Z@), @X@),
                  rule(@seq2@(Bottom(@Y@),@X@), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          } else { 
            // Bottom of Bottom is Bottom
            // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@seq@(@X@), @seq2@(@n2@(@n1@(@X@)),@X@)),
                  rule(Bottom(Bottom(@X@)), Bottom(@X@)),
                  rule(@seq2@(at(@X@,anti(Bottom(@Y@))),@Z@), @X@),
                  rule(@seq2@(Bottom(@Y@),@X@), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          }
        } else {
          // META-LEVEL
          this.generatedSignature.addSymbol(seq,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          this.generatedSignature.addSymbol(seq2,Arrays.asList(Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);
          if( !Main.options.approx ) {
            // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
            // normally it will follow reduction in original TRS
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@seq@(Appl(@X@,@Y@)), @seq2@(@n2@(@n1@(Appl(@X@,@Y@))),Appl(@X@,@Y@))),
                  rule(@seq@(Bottom(@X@)), Bottom(@X@)),
                  rule(@seq2@(Appl(@X@,@Y@),@Z@), Appl(@X@,@Y@)),
                  rule(@seq2@(Bottom(@Y@),@X@), Bottom(@X@))
                  ]]%,this.generatedSignature).getCollectionRuleList());
          } else { 
            // CHECK HERE
            generatedRules.add(Tools.encodeRule(%[rule(@seq@(@X@), @n2@(@n1@(@X@)))]%,generatedSignature));
          }

        }
        strategySymbol = seq;
      }

      StratChoice(s1,s2) -> {
        String n1 = compileStrat(null,`s1,generatedRules);
        String n2 = compileStrat(null,`s2,generatedRules);
        String choice = (stratName!=null)?stratName:Tools.getName("choice");
        String choice2 = Tools.getName("choice");
        if( !Main.options.metalevel ) {
          this.generatedSignature.addSymbol(choice,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          this.generatedSignature.addSymbol(choice2,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          // TODO [20/01/2015]: see if not exact is interesting
          generatedRules.addAll(Tools.encodeRuleList(%[[
                rule(@choice@(at(@X@,anti(Bottom(@Y@)))),  @choice2@(@n1@(@X@)) ),
                rule(@choice@(Bottom(@X@)), Bottom(@X@)),
                rule(@choice2@(at(@X@,anti(Bottom(@Y@)))), @X@),
                rule(@choice2@(Bottom(@X@)), @n2@(@X@))
                ]]%,this.generatedSignature).getCollectionRuleList());
        } else {
          // META-LEVEL
          this.generatedSignature.addSymbol(choice,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          this.generatedSignature.addSymbol(choice2,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          if( !Main.options.approx ) {
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@choice@(Appl(@X@,@Y@)), @choice2@(@n1@(Appl(@X@,@Y@)))),
                  rule(@choice@(Bottom(@X@)), Bottom(@X@)),
                  rule(@choice2@(Appl(@X@,@Y@)), Appl(@X@,@Y@)),
                  rule(@choice2@(Bottom(@X@)), @n2@(@X@))
                  ]]%,generatedSignature).getCollectionRuleList());
          } else {
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@choice@(@X@), @choice2@(@n1@(@X@))),
                  rule(@choice2@(Appl(@X@,@Y@)), Appl(@X@,@Y@)),
                  rule(@choice2@(Bottom(@X@)), @n2@(@X@))
                  ]]%,generatedSignature).getCollectionRuleList());
          }
        }
        strategySymbol = choice;
      }

      StratAll(s) -> {
        String phi_s = compileStrat(null,`s,generatedRules);
        String all = (stratName!=null)?stratName:Tools.getName("all");
        if( !Main.options.metalevel ) {
          this.generatedSignature.addSymbol(all,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          for(String name : this.extractedSignature.getSymbolNames()) {
            int arity = this.generatedSignature.getArity(name);
            int arity_all = arity+1;
            if(arity==0) {
              generatedRules.add(Tools.encodeRule(%[rule(@all@(@name@), @name@)]%,this.generatedSignature));
            } else {
              String all_n = all+"_"+name;
              List<String> all_args = new ArrayList<String>();
              for(int i=0; i<arity_all; i++){
                all_args.add(Signature.DUMMY);
              }
              this.generatedSignature.addSymbol(all_n,all_args,Signature.DUMMY);
              //             generatedSignature.put(all_n,arity_all);
              {
                // main case
                // all(f(x1,...,xn)) -> all_n(phi_s(x1),phi_s(x2),...,phi_s(xn),f(x1,...,xn))
                String lx = "X1"; 
                String rx = phi_s+"(X1)"; 
                for(int i=2 ; i<=arity ; i++) {
                  lx += ",X"+i;
                  rx += ","+phi_s+"(X"+i+")";
                }
                generatedRules.add(Tools.encodeRule(%[rule(@all@(@name@(@lx@)), @all_n@(@rx@,@name@(@lx@)))]%,this.generatedSignature)); 
                // propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
                generatedRules.add(Tools.encodeRule(%[rule(@all@(Bottom(@X@)), Bottom(@X@) )]%,this.generatedSignature));               
              }

              // generate success rules
              // all_g(x1@!BOTTOM,...,xN@!BOTTOM,_) -> g(x1,...,xN)
              String lx = "at(X1,anti(Bottom(Y1)))";
              String rx = "X1"; 
              for(int j=2; j<=arity;j++) {
                lx += ",at(X"+j+",anti(Bottom(Y"+j+")))";
                rx += ",X"+j;
              }
              generatedRules.add(Tools.encodeRule(%[rule(@all_n@(@lx@,@Z@), @name@(@rx@))]%,this.generatedSignature));

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
                generatedRules.add(Tools.encodeRule(%[rule(@all_n@(@llx@,@Z@), Bottom(@Z@))]%,this.generatedSignature));
              }
            }
          }
        } else {
          // META-LEVEL
          this.generatedSignature.addSymbol(all,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          String all_1 = all+"_1";
          String all_2 = all+"_2";
          String all_3 = all+"_3";
          String append = "append";
          String reverse = "reverse";
          String rconcat = "rconcat";
          generatedSignature.addSymbol(all_1,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          generatedSignature.addSymbol(all_2,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          generatedSignature.addSymbol(all_3,Arrays.asList(Signature.DUMMY,Signature.DUMMY,Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);
          generatedSignature.addSymbol(append,Arrays.asList(Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);
          generatedSignature.addSymbol(reverse,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          generatedSignature.addSymbol(rconcat,Arrays.asList(Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);

          generatedRules.addAll(Tools.encodeRuleList(%[[
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
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@append@(Nil,@Z@), Cons(@Z@,Nil)),
                  rule(@append@(Cons(@X@,@Y@),@Z@), Cons(@X@,@append@(@Y@,@Z@))),

                  rule(@reverse@(Nil), Nil),
                  rule(@reverse@(Cons(@X@,@Y@)), @append@(@reverse@(@Y@),@X@)),

                  rule(@rconcat@(Nil,@Z@), @Z@),
                  rule(@rconcat@(Cons(@X@,@Y@),@Z@), @rconcat@(@Y@,Cons(@X@,@Z@)))
                  ]]%,generatedSignature).getCollectionRuleList());
          }

        }
        strategySymbol = all;
      }

      StratOne(s) -> {
        String phi_s = compileStrat(null,`s,generatedRules);
        String one = (stratName!=null)?stratName:Tools.getName("one");
        if( !Main.options.metalevel ) {
          this.generatedSignature.addSymbol(one,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          //         generatedSignature.put(one,1);
          for(String name : this.extractedSignature.getSymbolNames()) {
            int arity = this.extractedSignature.getArity(name);
            if(arity==0) {
              generatedRules.add(Tools.encodeRule(%[rule(@one@(@name@), Bottom(@name@))]%,this.generatedSignature));
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
                generatedRules.add(Tools.encodeRule(%[rule(@one@(@name@(@lx@)), @one_n@_1(@rx@))]%,this.generatedSignature));
                // propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
                generatedRules.add(Tools.encodeRule(%[rule(@one@(Bottom(@X@)), Bottom(@X@) )]%,this.generatedSignature));               
              }

              for(int i=1 ; i<=arity ; i++) {
                String one_n_i = one_n + "_"+i;
                List<String> one_n_args = new ArrayList<String>();
                for(int ni=0; ni<arity; ni++){
                  one_n_args.add(Signature.DUMMY);
                }
                this.generatedSignature.addSymbol(one_n_i,one_n_args,Signature.DUMMY);
                //               generatedSignature.put(one_n_i,arity);
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

                  List<String> one_n_ii_args = new ArrayList<String>();
                  for(int nii=0; nii<arity; nii++){
                    one_n_ii_args.add(Signature.DUMMY);
                  }
                  this.generatedSignature.addSymbol(one_n_ii,one_n_ii_args,Signature.DUMMY);
                  //                 generatedSignature.put(one_n_ii,arity);
                  generatedRules.add(Tools.encodeRule(%[rule(@one_n_i@(@lx@), @one_n_ii@(@rx@))]%,this.generatedSignature));
                } else {
                  // one_f_n(Bottom(x1),...,Bottom(xn)) -> Bottom(f(x1,...,xn))
                  String lx = "Bottom(X1)";
                  String rx = "X1";
                  for(int j=2; j<=arity;j++) {
                    lx += ",Bottom(X"+j+")";
                    rx += ",X"+j;
                  }
                  generatedRules.add(Tools.encodeRule(%[rule(@one_n_i@(@lx@), Bottom(@name@(@rx@)))]%,this.generatedSignature));
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
                generatedRules.add(Tools.encodeRule(%[rule(@one_n_i@(@lx@), @name@(@rx@))]%,this.generatedSignature));

              }
            }
          }
        } else {
          // META-LEVEL
          this.generatedSignature.addSymbol(one,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          String one_1 = one+"_1";
          String one_2 = one+"_2";
          String one_3 = one+"_3";
          String append = "append";
          String reverse = "reverse";
          String rconcat = "rconcat";
          generatedSignature.addSymbol(one_1,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          generatedSignature.addSymbol(one_2,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          generatedSignature.addSymbol(one_3,Arrays.asList(Signature.DUMMY,Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);
          generatedSignature.addSymbol(append,Arrays.asList(Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);
          generatedSignature.addSymbol(reverse,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
          generatedSignature.addSymbol(rconcat,Arrays.asList(Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);

          generatedRules.addAll(Tools.encodeRuleList(%[[
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
            generatedRules.addAll(Tools.encodeRuleList(%[[
                  rule(@append@(Nil,@Z@), Cons(@Z@,Nil)),
                  rule(@append@(Cons(@X@,@Y@),@Z@), Cons(@X@,@append@(@Y@,@Z@))),

                  rule(@reverse@(Nil), Nil),
                  rule(@reverse@(Cons(@X@,@Y@)), @append@(@reverse@(@Y@),@X@)),

                  rule(@rconcat@(Nil,@Z@), @Z@),
                  rule(@rconcat@(Cons(@X@,@Y@),@Z@), @rconcat@(@Y@,Cons(@X@,@Z@)))
                  ]]%,generatedSignature).getCollectionRuleList());
          }
        }
        strategySymbol = one;
      }

    }
    return strategySymbol; //strat.toString();
  }

  %strategy ReplaceMuVar(name:String, appl:String) extends Identity() {
    visit Strat {
      StratName(n) && n==name -> {
        return `StratName(appl);
      }
    }
  }

  /**
   * Transform lhs into linear-lhs + true ^ constraint on non linear variables
   * TODO: not really related to the Compiler but more to the Tools (for Terms)
   */
  private TermList linearize(Term lhs) {
    Map<String,Integer> map = this.collectMultiplicity(lhs);
    Map<String,String> mapToOldName = new HashMap<String,String>();

    for(String name:map.keySet()) {
      if(map.get(name) > 1) {
        try {
          HashMap copy = new HashMap(map);
          lhs = `TopDown(ReplaceWithFreshVar(this,name,copy,mapToOldName)).visitLight(lhs);
        } catch(VisitFailure e) {
          throw new RuntimeException("Should not be there");
        }
      }
    }

    Term constraint = `Appl("True",TermList());
    for(String name:mapToOldName.keySet()) {
      String oldName = mapToOldName.get(name);
      String argType = "T"; // TODO: we have to find the type of the variable here!!!
      String eq_arg = getExtractedSignature().disambiguateSymbol(Signature.EQ, Arrays.asList(argType,argType) );
      constraint = `Appl("and",TermList( Appl(eq_arg,TermList(Var(oldName),Var(name))), constraint));
    }
    return `TermList(lhs,constraint);

  }
  
  // for Main.options.metalevel we need the (generated)signature 
  //   -> in previous versions it was one of the parameters
  %strategy ReplaceWithFreshVar(compiler:Compiler,name:String, multiplicityMap:Map, map:Map) extends Identity() {
    visit Term {
      Var(n)  -> {
        int value = (Integer)multiplicityMap.get(`name);
        if(`n.compareTo(`name)==0 && value>1) {
          String z = Tools.getName("Z");
          map.put(z,`n);
          multiplicityMap.put(`name, value - 1);
          Term newt = `Var(z);
          if(Main.options.metalevel) {
            newt = Tools.metaEncodeConsNil(newt,compiler.generatedSignature);
          }
          return newt;
        }
      }
    }
  }

  /**
   * Returns a Map which associates to each variable name an integer
   * representing the number of occurences of the variable in the
   * (Visitable) Term
   */
  public Map<String,Integer> collectMultiplicity(tom.library.sl.Visitable subject) {
    // collect variables
    List<String> variableList = new LinkedList<String>();
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
        multiplicityMap.put(varName, 1 + value);
      } else {
        multiplicityMap.put(varName, 1);
      }
    }
    return multiplicityMap;
  }

  // search all Var and store their values
  %strategy CollectVars(bag:List) extends Identity() {
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
  private void generateEquality(List<Rule> generatedRules) {
    Signature gSig = getGeneratedSignature();
    Signature eSig = getExtractedSignature();
    generatedRules.add(Tools.encodeRule(%[rule(and(True,True), True)]%,gSig));
    generatedRules.add(Tools.encodeRule(%[rule(and(True,False), False)]%,gSig));
    generatedRules.add(Tools.encodeRule(%[rule(and(False,True), False)]%,gSig));
    generatedRules.add(Tools.encodeRule(%[rule(and(False,False), False)]%,gSig));

    String z1 = Tools.getName("Z");
    String z2 = Tools.getName("Z"); 
    //for(String type:eSig.getTypeNames()) {
      //List<String> symbolNames = eSig.getSymbolNames(type);
      List<String> symbolNames = eSig.getSymbolNames();
      for(String f:symbolNames) {
        for(String g:symbolNames) {
          int arf = eSig.getArity(f);
          int arg = eSig.getArity(g);
          String eq_type = "eq"; //eSig.disambiguateSymbol(Signature.EQ, Arrays.asList(type,type) );
          if(!f.equals(g)) {
            generatedRules.add(Tools.encodeRule(%[rule(@eq_type@(@Tools.genAbstractTerm(f,arf,z1)@,@Tools.genAbstractTerm(g,arg,z2)@), False)]%,gSig));
          } else {
            String t1 = Tools.genAbstractTerm(f,arf,z1);
            String t2 = Tools.genAbstractTerm(f,arf,z2);
            List<String> domain = eSig.getProfile(f);
            String scond = "True";
            for(int i=1 ; i<=arf ; i++) {
              String argType = domain.get(i-1);
              String eq_arg = "eq"; //eSig.disambiguateSymbol(Signature.EQ, Arrays.asList(argType,argType) );
              scond = %[and(@eq_arg@(@z1@_@i@,@z2@_@i@),@scond@)]%;
            }
            generatedRules.add(Tools.encodeRule(%[rule(@eq_type@(@t1@,@t2@),@scond@)]%,gSig));
          }
        }
      }
    //}
    //generatedRules.add(Tools.encodeRule(%[rule(@mu@(Bottom(X)), Bottom(X))]%));
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
      s@Anti(_) -> {
        list.add(0,getEnvironment().getPosition());
        return `s;
      }
    }
  }


  /*
   * Specializer
   * @param symbol the symbol whose type is enforce
   * @param the type of the symbol
   * @param trs the TRS to transform
   */
  public List<Rule> specialize(String symbol, String typeName, List<Rule> trs) {
    Signature gSig = getGeneratedSignature();
    String codomainName = gSig.getCodomain(symbol);

    for(Rule r: trs) {
      %match(r) {
        Rule(lhs@Appl(name,args),rhs) -> {
          if(`name.equals(symbol)) {
            Term newLhs = propagateType(`EmptyEnvironment(), `lhs, `GomType(typeName));
            System.out.println("newLhs = " + newLhs);
          }
        }
      }

    }
    return null;
  }

  private Term propagateType(TypeEnvironment env, Term t, GomType type) {
    Signature gSig = getGeneratedSignature();
    %match(t) {
      Appl(name,args) -> {
        String codomainName = gSig.getCodomain(`name);
        if(codomainName == Signature.DUMMY) {
          List<String> domain = gSig.getProfile(`name);
          String dname = gSig.disambiguateSymbol(`name, domain);
          gSig.addSymbol(dname,domain,codomainName);
          int i = 0;
          TermList args = `args;
          TermList newArgs = `TermList();
          while(!args.isEmptyTermList()) {
            Term arg = args.getHeadTermList();
            // compute GOOD type here!
            Term arg2 = propagateType(env, arg, `GomType(domain.get(i)));
            if(arg2!=null) {
              newArgs = `TermList(newArgs*, arg2);
            } else {
              // throw exception
              System.out.println("bad arg2: " + arg2);
              return null;
            }
            i++;
            args = args.getTailTermList();
          }
          return `Appl(dname,newArgs);
        } else if(type != `GomType(codomainName)) {
          // throw exception
          System.out.println("bad type: " + t + " : " + type);
          return null;
        }

      }

      Anti(t1) -> {
        return propagateType(env, `t1,type);
      }

      At(t1,t2) -> {
        env = checkType(env,`t1, type);
        if(env == null) {
          // throw exception
          System.out.println("bad type: " + `t1 + " : " + type);
          return null;
        }
        return propagateType(env, `t2,type);
      }

      s@BuiltinInt(i) -> {
        if(type != `GomType("int")) {
          // throw exception
          System.out.println("bad type: " + `s + " : " + type);
          return null;
        }
        return `s;
      }

      s@Var(name) -> {
        env = checkType(env,`s, type);
        if(env == null) {
          // throw exception
          System.out.println("bad type: " + `s + " : " + type);
          return null;
        } 
        return `s;
      }

    }
    System.out.println("propagateType should not be there: " + t);
    return null;
  }

  /*
   * check that t is a variable with a type compatible with the enviroment
   * @param env the environment
   * @param t the term to type check
   * @param type the type
   * @return null is t is not correctly typed, or the enviroment enriched with t:type
   */
  private TypeEnvironment checkType(TypeEnvironment env, Term t, GomType type) {
    %match(env, t) {
      EmptyEnvironment(), Var(x) -> {
        return `PushEnvironment(x, type, EmptyEnvironment());
      }
      
      PushEnvironment(x,xtype,tail), Var(x) -> {
        if(`xtype == type) {
          return env;
        } else {
          // type mismatch
          return null;
        }
      }
      
      PushEnvironment(y,ytype,tail), Var(x) -> {
        TypeEnvironment tmp = checkType(`tail, t, type);
        if(tmp == null) {
          return null;
        } else {
          return `PushEnvironment(y,type, tmp);
        }
      }

    }

    System.out.println("checkType should not be there: " + t);
    return null;
  }

   
}
