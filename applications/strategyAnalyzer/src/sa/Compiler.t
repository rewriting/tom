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

  private Map<Strat,List<Rule>> storedTRSs;
  private Map<Strat,String> strategySymbols;

  /**
   * initialize the TRS and set the (generated) symbol that should be
   * used to wrap the terms to reduce
   * 
   */
  private Compiler() {
    this.generatedTRSs = new HashMap<String,List<Rule>>();
    this.storedTRSs = new HashMap<Strat,List<Rule>>();;
    this.strategySymbols = new HashMap<Strat,String>();;
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

    // if not generated yet
    if(this.generatedTRSs.get(strategyName)==null) {
      this.generatedTRSs.put(strategyName,new ArrayList<Rule>());
      
      String strategySymbol = "NONE";
      if(Main.options.metalevel) {
        strategySymbol=this.compileStrat(strategy,this.generatedTRSs.get(strategyName));
        this.generateTriggerRule(strategyName,strategySymbol,this.generatedTRSs.get(strategyName));
      } else {
        strategySymbol=this.compileStrat(strategy,this.generatedTRSs.get(strategyName));
        this.generateEquality(this.generatedTRSs.get(strategyName));
        this.generateTriggerRule(strategyName,strategySymbol,this.generatedTRSs.get(strategyName));
      }
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


  private Term Var(String name) { return `Var(name); }
  private Term Anti(Term t) { return `Anti(t); }
  private Term At(Term t1, Term t2) { return `At(t1,t2); }
  private Term Bottom(Term t) { return _appl(Signature.BOTTOM,t); }
  private Term BottomList(Term t) { return _appl(Signature.BOTTOMLIST,t); }
  private Term True() { return _appl(Signature.TRUE); }
  private Term False() { return _appl(Signature.FALSE); }
  private Term And(Term t1, Term t2) { return _appl(Signature.AND,t1,t2); }
  private Term Appl(Term t1, Term t2) { return _appl(Signature.APPL,t1,t2); }
  private Rule Rule(Term lhs, Term rhs) { return `Rule(lhs,rhs); }
  private Term Nil() { return _appl(Signature.NIL); }
  private Term Cons(Term t1, Term t2) { return _appl(Signature.CONS,t1,t2); }

  private Term _appl(String name, Term... args) {
    TermList tl = `TermList();
    for(Term t:args) {
      tl = `TermList(tl*,t);
    }
    return `Appl(name,tl);
  }

  /**
   * compile a strategy in a classical way (without using meta-representation)
   * return the name of the top symbol (phi) introduced
   * @param strat the strategy to compile
   * @param rules the list of rewrite rules generated for this strategy
   * @return the symbol to be used for the compiled strategy
   */
  private boolean generated_aux_functions = false;
  private String compileStrat(Strat strat, List<Rule> rules) {
    // by default, if strategy can't be compiled, a meaningless name
    // TODO: change to exception ?
    List<Rule> generatedRules = new ArrayList<Rule>();

    // if the stategy has been already compiled and a symbol generated
    String strategySymbol = this.strategySymbols.get(strat);
    if(strategySymbol!=null) {
      generatedRules = this.storedTRSs.get(strat);
      System.out.println("ALREADY EXISTING: "+strat);
    } else {
      Term X = Var(Tools.getName("X"));
      Term Y = Var(Tools.getName("Y"));
      Term XX = Var(Tools.getName("XX"));
      Term YY = Var(Tools.getName("YY"));
      Term Z = Var(Tools.getName("Z"));
      Term Z0 = Var(Tools.getName("Z0"));
      Term Z1 = Var(Tools.getName("Z1"));
      Term Z2 = Var(Tools.getName("Z2"));
      Term Z3 = Var(Tools.getName("Z3"));

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

          String rule = Tools.getName("rule");
          String cr = Tools.getName("crule");

          if(!Main.options.metalevel) {
            // if declared strategy (i.e. defined name) use its name; otherwise generate fresh name
            this.generatedSignature.addSymbol(rule,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
            this.generatedSignature.addSymbol(cr,Arrays.asList(Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);

            %match(rulelist) {
              RuleList(_*,Rule(lhs,rhs),_*) -> {
                // propagate failure; if the rule is applied to the result of a strategy that failed then the result is a failure
                // rule(Bot(X)) -> Bot(X)
                generatedRules.add(Rule(_appl(rule,Bottom(X)), Bottom(X)));

                TermList result = this.linearize(`lhs);

                %match(result) {
                  TermList(_, Appl("True",TermList())) -> {
                    // if already linear lhs
                    // rule(X@lhs) -> rhs
                    // rule(X@!lhs) -> Bot(X)
                    generatedRules.add(Rule(_appl(rule,At(X,`lhs)), `rhs));
                    generatedRules.add(Rule(_appl(rule,At(X,Anti(`lhs))), Bottom(X)));
                  }

                  TermList(linearlhs, cond@!Appl("True",TermList())) -> {
                    // if non-linear add rules for checking equality for corresponding arguments
                    // rule(X@linearlhs) -> cr(X,cond)
                    // rule(X@!linearlhs) -> Bot(X)
                    // cr(linearlhs, True) -> rhs
                    // cr(X@linearlhs, False) -> Bot(X)
                    generatedRules.add(Rule(_appl(rule,At(X,`linearlhs)), _appl(cr, X, `cond)));
                    generatedRules.add(Rule(_appl(rule,At(X,Anti(`linearlhs))), Bottom(X)));
                    generatedRules.add(Rule(_appl(cr,`linearlhs, True()), `rhs));
                    generatedRules.add(Rule(_appl(cr,At(X,`linearlhs),False()), Bottom(X)));
                  }
                }
              }
            }
          } else {
            // META-LEVEL
            this.generatedSignature.addSymbol(rule,Arrays.asList(Signature.METATERM),Signature.METATERM);
            this.generatedSignature.addSymbol(cr,Arrays.asList(Signature.METATERM,Signature.BOOLEAN),Signature.METATERM);

            %match(rulelist) {
              RuleList(_*,Rule(lhs,rhs),_*) -> {
                // propagate failure
                // rule(Bot(X)) -> Bot(X)
                generatedRules.add(Rule(_appl(rule,Bottom(X)), Bottom(X)));

                TermList result = this.linearize(`lhs);
                Term mlhs = Tools.metaEncodeConsNil(`lhs,generatedSignature);
                Term mrhs = Tools.metaEncodeConsNil(`rhs,generatedSignature);

                %match(result) {
                  TermList(_, Appl("True",TermList())) -> {
                    // if already linear lhs
                    // rule(X@mlhs) -> mrhs
                    // rule(X@!mlhs) -> Bot(X)
                    generatedRules.add(Rule(_appl(rule,At(X,mlhs)), mrhs));
                    generatedRules.add(Rule(_appl(rule,At(X,Anti(mlhs))), Bottom(X)));
                  }

                  TermList(linearlhs, cond@!Appl("True",TermList())) -> {
                    // if non-linear add rules for checking equality for corresponding arguments
                    Term mlinearlhs = Tools.metaEncodeConsNil(`linearlhs,generatedSignature);
                    // rule(X@mlinearlhs) -> cr(X,cond)
                    // rule(X@!mlinearlhs) -> Bot(X)
                    // cr(mlinearlhs, True) -> mrhs
                    // cr(X@mlinearlhs, False) -> Bot(X)
                    generatedRules.add(Rule(_appl(rule,At(X,mlinearlhs)), _appl(cr,X,`cond)));
                    generatedRules.add(Rule(_appl(rule,At(X,Anti(mlinearlhs))), Bottom(X)));
                    generatedRules.add(Rule(_appl(cr,mlinearlhs, True()), mrhs));
                    generatedRules.add(Rule(_appl(cr,At(X,mlinearlhs), False()), Bottom(X)));
                  }
                }

                // TODO: non-linear anti-pattern
                // generatedRules.add(`Rule(Appl(rule,TermList(At(X,Anti(mlhs)))),botX));
              }
            }

            for(String name:this.extractedSignature.getSymbolNames()) {
              // add symb_a(), symb_b(), symb_f(), symb_g() in the signature
              this.generatedSignature.addSymbol("symb_"+name,new ArrayList<String>(),Signature.METASYMBOL);
            }
          }
          strategySymbol=rule;
        }

        /*
         * TODO: fix non confluence here
         */
        StratMu(name,s) -> {
          try {
            String mu = Tools.getName("mu");
            Strat newStrat = `TopDown(ReplaceMuVar(name,mu)).visitLight(`s);
            String phi_s = compileStrat(newStrat,generatedRules);
            if(!Main.options.metalevel) {
              this.generatedSignature.addSymbol(mu,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
              // mu(X@!Bot(Y)) -> phi_s(X)
              // mu(Bot(X)) -> Bot(X)
              generatedRules.add(Rule(_appl(mu,At(X,Anti(Bottom(Y)))), _appl(phi_s,X)));
              generatedRules.add(Rule(_appl(mu,Bottom(X)), Bottom(X)));
            } else {
              // META-LEVEL
              this.generatedSignature.addSymbol(mu,Arrays.asList(Signature.METATERM),Signature.METATERM);
              // mu(Appl(Y,Z)) -> phi_s(Appl(Y,Z))
              // mu(Bot(X)) -> Bot(X)
              generatedRules.add(Rule(_appl(mu,Appl(Y,Z)), _appl(phi_s,Appl(Y,Z))));
              generatedRules.add(Rule(_appl(mu,Bottom(X)), Bottom(X)));
            }
            strategySymbol = phi_s;
          } catch(VisitFailure e) {
            System.out.println("failure in StratMu on: " + `s);
          }
        }

        // mu fix point: transform the stratname into a function call
        StratName(name) -> {
          strategySymbol = `name;
        }

        StratIdentity() -> {
          String id = Tools.getName("id");
          if(!Main.options.metalevel) {
            this.generatedSignature.addSymbol(id,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
            if( !Main.options.approx ) {
              // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
              // normally it will follow reduction in original TRS

              // id(X@!Bot(Y)) -> X
              // id(Bot(X)) -> Bot(X)
              generatedRules.add(Rule(_appl(id,At(X,Anti(Bottom(Y)))), X));
              generatedRules.add(Rule(_appl(id,Bottom(X)), Bottom(X)));
            } else { 
              // Bottom of Bottom is Bottom
              // this is not necessary if exact reduction - in this case Bottom is propagated immediately 
              // id(X) -> X
              // Bot(Bot(X)) -> Bot(X)
              generatedRules.add(Rule(_appl(id,X), X));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }
          } else {
            // META-LEVEL
            this.generatedSignature.addSymbol(id,Arrays.asList(Signature.METATERM),Signature.METATERM);
            if( !Main.options.approx ) {
              // id(Appl(X,Y)) -> Appl(X,Y)
              // id(Bot(X)) -> Bot(X)
              generatedRules.add(Rule(_appl(id,Appl(X,Y)), Appl(X,Y)));
              generatedRules.add(Rule(_appl(id,Bottom(X)), Bottom(X)));
            } else { 
              // id(X) -> X
              // Bot(Bot(X)) -> Bot(X)
              generatedRules.add(Rule(_appl(id,X), X));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }
          }
          strategySymbol = id;
        }

        StratFail() -> {
          String fail = Tools.getName("fail");
          if( !Main.options.metalevel ) {
            this.generatedSignature.addSymbol(fail,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
            if( !Main.options.approx ) {
              // fail(X@!Bot(Y)) -> Bot(X)
              // fail(Bot(X)) -> Bot(X)
              generatedRules.add(Rule(_appl(fail,At(X,Anti(Bottom(Y)))), Bottom(X)));
              generatedRules.add(Rule(_appl(fail,Bottom(X)), Bottom(X)));
            } else { 
              // fail(X) -> Bot(X)
              // Bot(Bot(X)) -> Bot(X)
              generatedRules.add(Rule(_appl(fail,X), Bottom(X)));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }
          } else {
            // META-LEVEL
            this.generatedSignature.addSymbol(fail,Arrays.asList(Signature.METATERM),Signature.METATERM);
            if( !Main.options.approx ) {
              // fail(Appl(X,Y)) -> Bot(Appl(X,Y))
              // fail(Bot(X)) -> Bot(X)
              generatedRules.add(Rule(_appl(fail,Appl(X,Y)), Bottom(Appl(X,Y))));
              generatedRules.add(Rule(_appl(fail,Bottom(X)), Bottom(X)));
            } else { 
              // fail(X) -> Bot(X)
              // Bot(Bot(X)) -> Bot(X)
              generatedRules.add(Rule(_appl(fail,X), Bottom(X)));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }

          }
          strategySymbol = fail;
        }

        StratSequence(s1,s2) -> {
          String n1 = compileStrat(`s1,generatedRules);
          String n2 = compileStrat(`s2,generatedRules);
          String seq = Tools.getName("seq");
          String seq2 = Tools.getName("seq2");
          if( !Main.options.metalevel ) {
            this.generatedSignature.addSymbol(seq,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
            this.generatedSignature.addSymbol(seq2,Arrays.asList(Signature.DUMMY,Signature.DUMMY),Signature.DUMMY);
            if( !Main.options.approx ) {
              // the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
              // normally it will follow reduction in original TRS
              // seq(X@!Bot(Y)) -> seq2(n2(n1(X)),X)
              // seq(Bot(X)) -> Bot(X)
              // seq2(X@!Bot(Y),Z) -> X
              // seq2(Bot(Y),X) -> Bot(X)
              generatedRules.add(Rule(_appl(seq,At(X,Anti(Bottom(Y)))), _appl(seq2,_appl(n2,_appl(n1,X)),X)));
              generatedRules.add(Rule(_appl(seq,Bottom(X)), Bottom(X)));
              generatedRules.add(Rule(_appl(seq2,At(X,Anti(Bottom(Y))),Z), X));
              generatedRules.add(Rule(_appl(seq2,Bottom(Y),X), Bottom(X)));

            } else { 
              // seq(X) -> seq2(n2(n1(X)),X)
              // Bot(Bot(X)) -> Bot(X)
              // seq2(X@!Bot(Y),Z) -> X
              // seq2(Bot(Y),X) -> Bot(X)
              generatedRules.add(Rule(_appl(seq,X), _appl(seq2,_appl(n2,_appl(n1,X)),X)));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
              generatedRules.add(Rule(_appl(seq2,At(X,Anti(Bottom(Y))),Z), X));
              generatedRules.add(Rule(_appl(seq2,Bottom(Y),X), Bottom(X)));
            }
          } else {
            // META-LEVEL
            this.generatedSignature.addSymbol(seq,Arrays.asList(Signature.METATERM),Signature.METATERM);
            this.generatedSignature.addSymbol(seq2,Arrays.asList(Signature.METATERM,Signature.METATERM),Signature.METATERM);
            if( !Main.options.approx ) {
              // seq(Appl(X,Y)) -> seq2(n2(n1(Appl(X,Y))),Appl(X,Y))
              // seq(Bot(X)) -> Bot(X)
              // seq2(Appl(X,Y),Z) -> Appl(X,Y)
              // seq2(Bot(Y),X) -> Bot(X)
              generatedRules.add(Rule(Appl(X,Y), _appl(seq2,_appl(n2,_appl(n1,Appl(X,Y))),Appl(X,Y))));
              generatedRules.add(Rule(_appl(seq,Bottom(X)), Bottom(X)));
              generatedRules.add(Rule(_appl(seq2,Appl(X,Y),Z), Appl(X,Y)));
              generatedRules.add(Rule(_appl(seq2,Bottom(Y),X), Bottom(X)));
            } else { 
              // CHECK HERE
              // seq(X) -> n2(n1(X))
              generatedRules.add(Rule(X, _appl(n2,_appl(n1,X))));
            }

          }
          strategySymbol = seq;
        }

        StratChoice(s1,s2) -> {
          String n1 = compileStrat(`s1,generatedRules);
          String n2 = compileStrat(`s2,generatedRules);
          String choice = Tools.getName("choice");
          String choice2 = Tools.getName("choice");
          if( !Main.options.metalevel ) {
            this.generatedSignature.addSymbol(choice,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
            this.generatedSignature.addSymbol(choice2,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
            // TODO [20/01/2015]: see if not exact is interesting
            // choice(X@!Bot(Y)) -> choice2(n1(X))
            // choice(Bot(X)) -> Bot(X)
            // choice2(X@!Bot(Y)) -> X
            // choice2(Bot(X)) -> n2(X)
            generatedRules.add(Rule(_appl(choice,At(X,Anti(Bottom(Y)))), _appl(choice2,_appl(n1,X))));
            generatedRules.add(Rule(_appl(choice,Bottom(X)), Bottom(X)));
            generatedRules.add(Rule(_appl(choice2,At(X,Anti(Bottom(Y)))), X));
            generatedRules.add(Rule(_appl(choice2,Bottom(X)), _appl(n2,X)));
          } else {
            // META-LEVEL
            this.generatedSignature.addSymbol(choice,Arrays.asList(Signature.METATERM),Signature.METATERM);
            this.generatedSignature.addSymbol(choice2,Arrays.asList(Signature.METATERM),Signature.METATERM);
            if( !Main.options.approx ) {
              // choice(Appl(X,Y)) -> choice2(n1(Appl(X,Y)))
              // choice(Bot(X)) -> Bot(X)
              // choice2(Appl(X,Y) -> Appl(X,Y)
              // choice2(Bot(X)) -> n2(X)
              generatedRules.add(Rule(_appl(choice,Appl(X,Y)), _appl(choice2,_appl(n1,Appl(X,Y)))));
              generatedRules.add(Rule(_appl(choice,Bottom(X)), Bottom(X)));
              generatedRules.add(Rule(_appl(choice2,Appl(X,Y)), Appl(X,Y)));
              generatedRules.add(Rule(_appl(choice2,Bottom(X)), _appl(n2,X)));
            } else {
              // choice(X) -> choice2(n1(X))
              // choice2(Appl(X,Y) -> Appl(X,Y)
              // choice2(Bot(X)) -> n2(X)
              generatedRules.add(Rule(_appl(choice,X), _appl(choice2,_appl(n1,X))));
              generatedRules.add(Rule(_appl(choice2,Appl(X,Y)), Appl(X,Y)));
              generatedRules.add(Rule(_appl(choice2,Bottom(X)), _appl(n2,X)));
            }
          }
          strategySymbol = choice;
        }

        StratAll(s) -> {
          String phi_s = compileStrat(`s,generatedRules);
          String all = Tools.getName("all");
          if( !Main.options.metalevel ) {
            this.generatedSignature.addSymbol(all,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
            for(String name : this.extractedSignature.getSymbolNames()) {
              int arity = this.generatedSignature.getArity(name);
              int arity_all = arity+1;
              if(arity==0) {
                // all(name) -> name
                generatedRules.add(Rule(_appl(all,_appl(name)), _appl(name)));
              } else {
                String all_n = all+"_"+name;
                List<String> all_args = new ArrayList<String>();
                for(int i=0; i<arity_all; i++){
                  all_args.add(Signature.DUMMY);
                }
                this.generatedSignature.addSymbol(all_n,all_args,Signature.DUMMY);

                // main case
                // all(f(x1,...,xn)) -> all_n(phi_s(x1),phi_s(x2),...,phi_s(xn),f(x1,...,xn))
                Term[] a_lx = new Term[arity];
                Term[] a_rx = new Term[arity+1];
                for(int i=0 ; i<arity ; i++) {
                  Term Xi = `Var("X" + i);
                  a_lx[i] = Xi;
                  a_rx[i] = _appl(phi_s, Xi);
                }
                a_rx[arity] = _appl(name, a_lx);
                generatedRules.add(Rule(_appl(all,_appl(name, a_lx)), _appl(all_n, a_rx)));

                // propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
                // all(Bot(X)) -> Bot(X)
                generatedRules.add(Rule(_appl(all,Bottom(X)), Bottom(X)));

                // generate success rules
                // all_g(X1@!Bottom(Y1),...,X_n@!Bottom(Y_n),_) -> g(X1,...,X_n)
                a_lx = new Term[arity+1];
                a_rx = new Term[arity];
                for(int i=0 ; i<arity ; i++) {
                  Term Xi = `Var("X" + i);
                  Term Yi = `Var("Y" + i);
                  a_lx[i] = At(Xi,Anti(Bottom(Yi)));
                  a_rx[i] = Xi;
                }
                a_lx[arity] = Z;
                generatedRules.add(Rule(_appl(all_n,a_lx), _appl(name, a_rx)));

                // generate failure rules
                // phi_n(Bottom(_),_,...,_,Z) -> Bottom(Z)
                // phi_n(...,Bottom(_),...,Z) -> Bottom(Z)
                // phi_n(_,...,_,Bottom(_),Z) -> Bottom(Z)
                Term[] a_llx = new Term[arity+1];
                for(int i=0 ; i<arity ; i++) {
                  Term X0 = `Var("X0");
                  a_llx[0] = (i==0)?Bottom(X0):X0;
                  for(int j=1 ; i<arity ; i++) {
                    Term Xj = `Var("X" + i);
                    if(j==i) {
                      a_llx[j] = Bottom(Xj);
                    } else {
                      a_llx[j] = Xj;
                    }
                  }
                  a_llx[arity] = Z;
                  generatedRules.add(Rule(_appl(all_n,a_llx), Bottom(Z)));
                }
              }
            }
          } else {
            // META-LEVEL
            this.generatedSignature.addSymbol(all,Arrays.asList(Signature.METATERM),Signature.METATERM);
            String all_1 = all+"_1";
            String all_2 = all+"_2";
            String all_3 = all+"_3";
            String append = "append";
            String reverse = "reverse";
            String rconcat = "rconcat";
            generatedSignature.addSymbol(all_1,Arrays.asList(Signature.METATERM),Signature.METATERM);
            generatedSignature.addSymbol(all_2,Arrays.asList(Signature.METALIST),Signature.METALIST);
            generatedSignature.addSymbol(all_3,Arrays.asList(Signature.METATERM,Signature.METALIST,Signature.METALIST,Signature.METALIST),Signature.METALIST);
            generatedSignature.addSymbol(append,Arrays.asList(Signature.METALIST,Signature.METATERM),Signature.METALIST);
            generatedSignature.addSymbol(reverse,Arrays.asList(Signature.METALIST),Signature.METALIST);
            generatedSignature.addSymbol(rconcat,Arrays.asList(Signature.METALIST,Signature.METALIST),Signature.METALIST);
            // all(Appl(Z0,Z1)) -> all_1(Appl(Z0,all_2(Z1)))
            // all_1(Appl(Z0, BottomList(Z))) -> Bottom(Appl(Z0,Z))
            // all_1(Appl(Z0, Cons(Z1,Z2))) -> Appl(Z0,Cons(Z1,Z2))
            // all_1(Appl(Z0, Nil)) -> Appl(Z0,Nil)
            // all_2(Nil) -> Nil
            // all_2(Cons(Z1,Z2)) -> all_3(phi_s(Z1),Z2,Cons(Z1,Nil),Nil)
            // all_3(Bottom(X),todo,rargs,rs_args) -> BottomList(rconcat(rargs,todo))
            // all_3(Appl(X,Y),Nil,rargs,rs_args) -> reverse(Cons(Appl(X,Y),rs_args))
            // all_3(Appl(X,Y), Cons(XX,YY), rargs, rs_args) -> 
            // all_3(phi_s(XX), YY, Cons(XX,rargs), Cons(Appl(X,Y),rs_args))
            generatedRules.add(Rule(_appl(all,Appl(Z0,Z1)), _appl(all_1,Appl(Z0,_appl(all_2,Z1)))));
            generatedRules.add(Rule(_appl(all_1,Appl(Z0,BottomList(Z))), Bottom(Appl(Z0,Z))));
            generatedRules.add(Rule(_appl(all_1,Appl(Z0,Cons(Z1,Z2))), Appl(Z0,Cons(Z1,Z2))));
            generatedRules.add(Rule(_appl(all_1,Nil()), Appl(Z0,Nil())));
            generatedRules.add(Rule(_appl(all_2,Nil()), Nil()));
            generatedRules.add(Rule(_appl(all_2,Cons(Z1,Z2)), _appl(all_3,_appl(phi_s,Z1),Z2,Cons(Z1,Nil()),Nil())));

            generatedRules.add(Rule(_appl(all_3,Bottom(X),Z1,Z2,Z3), BottomList(_appl(rconcat,Z2,Z1))));
            generatedRules.add(Rule(_appl(all_3,Appl(X,Y),Nil(),Z2,Z3), _appl(reverse,Cons(Appl(X,Y),Z3))));
            generatedRules.add(Rule(_appl(all_3,Appl(X,Y),Cons(XX,YY),Z2,Z3), 
                  _appl(all_3,_appl(phi_s,XX),YY,Cons(XX,Z2),Cons(Appl(X,Y),Z3))));

            if(!generated_aux_functions) {
              generated_aux_functions = true;
              // append(Nil,Z) -> Cons(Z,Nil)
              // append(Cons(X,Y),Z) -> Cons(X,append(Y,Z))
              // reverse(Nil) -> Nil
              // reverse(Cons(X,Y)) -> append(reverse(Y),X)
              // rconcat(Nil,Z) -> Z
              // rconcat(Cons(X,Y),Z) -> rconcat(Y,Cons(X,Z))

              generatedRules.add(Rule(_appl(append,Nil(),Z), Cons(Z,Nil())));
              generatedRules.add(Rule(_appl(append,Cons(X,Y),Z), Cons(X,_appl(append,Y,Z))));
              generatedRules.add(Rule(_appl(reverse,Nil()), Nil()));
              generatedRules.add(Rule(_appl(reverse,Cons(X,Y)), _appl(append,_appl(reverse,Y),X)));
              generatedRules.add(Rule(_appl(rconcat,Nil(),Z), Z));
              generatedRules.add(Rule(_appl(rconcat,Cons(X,Y),Z), _appl(rconcat,Y,Cons(X,Z))));

            }
          }
          strategySymbol = all;
        }

        StratOne(s) -> {
          String phi_s = compileStrat(`s,generatedRules);
          String one = Tools.getName("one");
          if( !Main.options.metalevel ) {
            this.generatedSignature.addSymbol(one,Arrays.asList(Signature.DUMMY),Signature.DUMMY);
            for(String name : this.extractedSignature.getSymbolNames()) {
              int arity = this.extractedSignature.getArity(name);
              if(arity==0) {
                // one(name) -> Bottom(name)
                generatedRules.add(Rule(_appl(one,_appl(name)), Bottom(_appl(name))));
              } else {
                String one_n = one+"_"+name;
                String one_n_1 = one_n + "_1";

                // main case
                // one(f(x1,...,xn)) -> one_n_1(phi_s(x1),x2,...,xn)
                Term[] a_lx = new Term[arity];
                Term[] a_rx = new Term[arity];
                for(int i=1 ; i<=arity ; i++) {
                  Term Xi = `Var("X" + i);
                  a_lx[i-1] = Xi;
                  a_rx[i-1] = (i==1)?_appl(phi_s, Xi):Xi;
                }
                generatedRules.add(Rule(_appl(one,_appl(name, a_lx)), _appl(one_n_1, a_rx)));
                
                // propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
                // one(Bottom(X)) -> Bottom(X)
                generatedRules.add(Rule(_appl(one,Bottom(X)), Bottom(X)));


                for(int i=1 ; i<=arity ; i++) {
                  String one_n_i = one_n + "_" + i;
                  String one_n_ii = one_n + "_"+(i+1);
                  List<String> one_n_args = new ArrayList<String>();
                  List<String> one_n_ii_args = new ArrayList<String>();
                  for(int ni=0; ni<arity; ni++){
                    one_n_args.add(Signature.DUMMY);
                    one_n_ii_args.add(Signature.DUMMY);
                  }
                  this.generatedSignature.addSymbol(one_n_i,one_n_args,Signature.DUMMY);
                  if(i<arity) {
                    this.generatedSignature.addSymbol(one_n_ii,one_n_ii_args,Signature.DUMMY);
                    // one_f_i(Bottom(x1),...,Bottom(xi),xj,...,xn)
                    // -> one_f_(i+1)(Bottom(x1),...,Bottom(xi),phi_s(x_i+1),...,xn)
                    for(int j=1 ; j<=i ; j++) {
                      Term Xj = Var("X"+j);
                      if(j<=i) {
                        a_lx[j-1] = Bottom(Xj);
                        a_rx[j-1] = Bottom(Xj);
                      } else {
                        a_lx[j-1] = Xj;
                        a_rx[j-1] = (j==i+1)?_appl(phi_s,Xj):Xj;
                      }
                    }

                    // one_n_i(lx) -> one_n_ii(rx)
                    generatedRules.add(Rule(_appl(one_n_i,a_lx), _appl(one_n_ii, a_rx)));
                  } else {
                    // one_f_n(Bottom(x1),...,Bottom(xn)) -> Bottom(f(x1,...,xn))
                    for(int j=1 ; j<=arity ; j++) {
                      Term Xj = Var("X"+j);
                      a_lx[j-1] = Bottom(Xj);
                      a_rx[j-1] = Xj;
                    }
                    generatedRules.add(Rule(_appl(one_n_i,a_lx), Bottom(_appl(name, a_rx))));
                  }

                  // one_f_i(Bottom(x1),...,xi@!Bottom(_),xj,...,xn)
                  // -> f(x1,...,xi,...,xn)
                  for(int j=1 ; j<=arity ; j++) {
                    Term Xj = Var("X"+j);
                    if(j<i) {
                      a_lx[j-1] = Bottom(Xj);
                    } else if(j==i) {
                      a_lx[j-1] = At(Xj,Anti(Bottom(Y)));
                    } else {
                      a_lx[j-1] = Xj;
                    }
                    a_rx[j-1] = Xj;
                  }

                  // one_n_i(lx) -> name(rx)
                  generatedRules.add(Rule(_appl(one_n_i,a_lx), _appl(name, a_rx)));

                }
              }
            }
          } else {
            // META-LEVEL
            this.generatedSignature.addSymbol(one,Arrays.asList(Signature.METATERM),Signature.METATERM);
            String one_1 = one+"_1";
            String one_2 = one+"_2";
            String one_3 = one+"_3";
            String append = "append";
            String reverse = "reverse";
            String rconcat = "rconcat";
            generatedSignature.addSymbol(one_1,Arrays.asList(Signature.METATERM),Signature.METATERM);
            generatedSignature.addSymbol(one_2,Arrays.asList(Signature.METALIST),Signature.METALIST);
            generatedSignature.addSymbol(one_3,Arrays.asList(Signature.METATERM,Signature.METALIST,Signature.METALIST),Signature.METALIST);
            generatedSignature.addSymbol(append,Arrays.asList(Signature.METALIST,Signature.METATERM),Signature.METALIST);
            generatedSignature.addSymbol(reverse,Arrays.asList(Signature.METALIST),Signature.METALIST);
            generatedSignature.addSymbol(rconcat,Arrays.asList(Signature.METALIST,Signature.METALIST),Signature.METALIST);
            // one(Appl(Z0,Z1)) -> one_1(Appl(Z0,one_2(Z1)))
            generatedRules.add(Rule(_appl(one,Appl(Z0,Z1)), _appl(one_1,Appl(Z0,_appl(one_2,Z1)))));
            // one_1(Appl(Z0,BottomList(Z))) -> Bottom(Appl(Z0,Z))
            // one_1(Appl(Z0,Cons(Z1,Z2))) -> Appl(Z0,Cons(Z1,Z2))
            generatedRules.add(Rule(_appl(one_1,Appl(Z0,BottomList(Z))), Bottom(Appl(Z0,Z))));
            generatedRules.add(Rule(_appl(one_1,Appl(Z0,Cons(Z1,Z2))), Appl(Z0,Cons(Z1,Z2))));
            // one_2(Nil) -> BottomList(Nil)
            // one_2(Cons(X,Y)) -> one_3(phi_s(X),Y,Cons(X,Nil))
            generatedRules.add(Rule(_appl(one_2,Nil()), BottomList(Nil())));
            generatedRules.add(Rule(_appl(one_2,Cons(Z1,Z2)), _appl(one_3,_appl(phi_s,Z1),Z2,Cons(Z1,Nil()))));
            // one_3(Bottom(X),Nil,rargs) -> BottomList(reverse(rargs))
            // one_3(Bottom(X),Cons(head,tail),rargs) -> ONE(head, tail, Cons(head,rargs))
            // one_3(Appl(X,Y),todo,Cons(last,rargs)) -> rconcat(rargs,Cons(Appl(X,Y),todo))
            generatedRules.add(Rule(_appl(one_3,Bottom(Z),Nil(),Z2), BottomList(_appl(reverse,Z2))));
            generatedRules.add(Rule(_appl(one_3,Bottom(Z),Cons(XX,YY),Z2), _appl(one_3,XX,YY,Cons(XX,Z2))));
            generatedRules.add(Rule(_appl(one_3,Appl(X,Y),Z1,Cons(Z2,Z3)), _appl(rconcat,Z3,Cons(Appl(X,Y),Z1))));

            if(!generated_aux_functions) { 
              generated_aux_functions = true;
              // append(Nil,Z) -> Cons(Z,Nil)
              // append(Cons(X,Y),Z) -> Cons(X,append(Y,Z))
              // reverse(Nil) -> Nil
              // reverse(Cons(X,Y)) -> append(reverse(Y),X)
              // rconcat(Nil,Z) -> Z
              // rconcat(Cons(X,Y),Z) -> rconcat(Y,Cons(X,Z))

              generatedRules.add(Rule(_appl(append,Nil(),Z), Cons(Z,Nil())));
              generatedRules.add(Rule(_appl(append,Cons(X,Y),Z), Cons(X,_appl(append,Y,Z))));
              generatedRules.add(Rule(_appl(reverse,Nil()), Nil()));
              generatedRules.add(Rule(_appl(reverse,Cons(X,Y)), _appl(append,_appl(reverse,Y),X)));
              generatedRules.add(Rule(_appl(rconcat,Nil(),Z), Z));
              generatedRules.add(Rule(_appl(rconcat,Cons(X,Y),Z), _appl(rconcat,Y,Cons(X,Z))));

            }
          }
          strategySymbol = one;
        }
      } // match
      this.strategySymbols.put(strat,strategySymbol);
      this.storedTRSs.put(strat,generatedRules);
    } // strategy not yet compiled
        
    // add the generated rules to the rules for the global strategy only if not already existing
    for(Rule rule:generatedRules) {
      if(!rules.contains(rule)) {
        rules.add(rule);
      }
    }
    return strategySymbol;
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

    generatedRules.add(Rule(And(True(),True()), True()));
    generatedRules.add(Rule(And(True(),False()), False()));
    generatedRules.add(Rule(And(False(),True()), False()));
    generatedRules.add(Rule(And(False(),False()), False()));

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


  /**
   * generates a rule of the form name(X) -> symbol(X)
   * name can be then see as an alias for symbol in terms of reduction 
   **/
  private void generateTriggerRule(String name, String symbol, List<Rule> generatedRules) {
    String codomain = this.generatedSignature.getCodomain(symbol);
    List<String> profile = this.generatedSignature.getProfile(symbol);
    this.generatedSignature.addSymbol(name,profile,codomain);

    Term X = Var(Tools.getName("X"));

    // X matches anything but morally matches only symbols from the extracted signature
    // name(X) -> symbol(X)
    generatedRules.add(Rule(_appl(name,X), _appl(symbol,X)));
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
