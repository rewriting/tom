package sa;

import sa.rule.types.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;
import com.google.common.collect.HashMultiset;

import static sa.Tools.Var;
import static sa.Tools.Anti;
import static sa.Tools.At;
import static sa.Tools.Bottom;
import static sa.Tools.BottomList;
import static sa.Tools.True;
import static sa.Tools.False;
import static sa.Tools.And;
import static sa.Tools.Eq;
import static sa.Tools.Appl;
import static sa.Tools.Rule;
import static sa.Tools.Nil;
import static sa.Tools.Cons;
import static sa.Tools._appl;

public class Compiler {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/List.tom }

  %typeterm Compiler { implement { Compiler }}
  %typeterm HashMultiset { implement { HashMultiset }}

  private static Compiler instance = null;

  // initial AST
  private Program program;

  // The extracted (concrete) signature
  private Signature extractedSignature;
  // The generated (concrete) signature
  private Signature generatedSignature;

  // strategy name -> strategy compiled into a TRS
  private Map<String,RuleList> generatedTRSs;

  private Map<Strat,List<Rule>> storedTRSs;
  private Map<Strat,String> strategySymbols;

  /**
   * initialize the TRS and set the (generated) symbol that should be
   * used to wrap the terms to reduce
   * 
   */
  private Compiler() {
    this.generatedTRSs = new HashMap<String,RuleList>();
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
  public RuleList compileStrategy(String strategyName) {
    Expression expand = this.expandStrategy(strategyName);

    Strat strategy=null;
    %match(expand) {
      Strat(strat) -> { strategy = `strat; }
    }

    // if not generated yet
    if(generatedTRSs.get(strategyName) == null) {
      
      String strategySymbol = "NONE";
      List<Rule> mutableList = new ArrayList<Rule>();
      strategySymbol = this.compileStrat(strategy,mutableList);
      RuleList ruleList = Tools.fromListOfRule(mutableList);

      if(Main.options.metalevel) {
        ruleList = generateTriggerRule(strategyName,strategySymbol,ruleList);
        ruleList = generateEncodeDecode(ruleList);
      } else {
        ruleList = generateEquality(ruleList);
        ruleList = generateTriggerRule(strategyName,strategySymbol,ruleList);
      }
      generatedTRSs.put(strategyName,ruleList);
    }
    return generatedTRSs.get(strategyName);
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
        StratDecl(_, ConcParam() , body) -> {
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
        StratDecl(_, params, body) -> {
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
   * compile a (ordered) list of rules
   * return the name of the top symbol (phi) introduced
   * @param ruleList the list of rules to compile
   * @param generatedRules the list of rewrite rules generated 
   * @return the symbol to be used for the compiled strategy
   */
  private String compileRuleList(RuleList ruleList, List<Rule> generatedRules) {
    Signature gSig = getGeneratedSignature();
    Signature eSig = getExtractedSignature();

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
    
    Term X = Var(Tools.getName("X"));
    String rule = Tools.getName(StrategyOperator.RULE.getName());
    String cr = Tools.getName(Tools.addAuxExtension(StrategyOperator.RULE.getName()));

    // used just to have in generatedRules the packages of rules in the order they are called 
    List<Rule> localRules = new ArrayList<Rule>();

    if(Main.options.pattern) {
      RuleList res = Pattern.trsRule(ruleList,eSig,gSig);
    }

    if(!Main.options.metalevel) {
      // if declared strategy (i.e. defined name) use its name; otherwise generate fresh name
      gSig.addSymbol(rule,`ConcGomType(Signature.TYPE_TERM),Signature.TYPE_TERM);
      gSig.addSymbol(cr,`ConcGomType(Signature.TYPE_TERM,Signature.TYPE_BOOLEAN),Signature.TYPE_TERM);

      %match(ruleList) {
        ConcRule(Rule(lhs,rhs),A*) -> {
          String nextRule = compileRuleList(`A*,generatedRules);
          TermList result = Tools.linearize(`lhs, this.generatedSignature );

          /*
           * propagate failure; if the rule is applied to the result of a strategy that failed then the result is a failure
           * rule(Bot(X)) -> Bot(X)
           */
          localRules.add(Rule(_appl(rule,Bottom(X)), Bottom(X)));

          %match(result) {
            TermList(_, Appl("True",TermList())) -> {
              /*
               * if already linear lhs
               * rule(X@lhs) -> rhs
               * rule(X@!lhs) -> nextRule(X)       // could be Bot(X) if only one rule, i.e. non next rule
               */
              localRules.add(Rule(_appl(rule,At(X,`lhs)), `rhs));
              Term lhs = Main.options.ordered ? _appl(rule,X) : _appl(rule,At(X,Anti(`lhs)));
              localRules.add(Rule(lhs, _appl(nextRule,X)) );
//               if(!ordered){
//                 localRules.add(Rule(_appl(rule,At(X,Anti(`lhs))), _appl(nextRule,X)) );
//               }else{
//                 localRules.add(Rule(_appl(rule,X), _appl(nextRule,X)) );
//               }
            }

            TermList(linearlhs, cond@!Appl("True",TermList())) -> {
              /*
               * if non-linear add rules for checking equality for corresponding arguments
               * rule(X@linearlhs) -> cr(X,cond)
               * rule(X@!linearlhs) -> nextRule(X)       // could be Bot(X) if only one rule, i.e. non next rule
               * cr(linearlhs, True) -> rhs
               * cr(X@linearlhs, False) -> nextRule(X)       // could be Bot(X) if only one rule, i.e. non next rule
               */
              localRules.add(Rule(_appl(rule,At(X,`linearlhs)), _appl(cr, X, `cond)));
              Term lhs = Main.options.ordered ? _appl(rule,X) : _appl(rule,At(X,Anti(`linearlhs)));
              localRules.add(Rule(lhs, _appl(nextRule,X)) );
//               if(!ordered){
//                 localRules.add(Rule(_appl(rule,At(X,Anti(`linearlhs))), _appl(nextRule,X) ));
//               }else{
//                 localRules.add(Rule(_appl(rule,X), _appl(nextRule,X) ));
//               }
              localRules.add(Rule(_appl(cr,`linearlhs, True()), `rhs));
              localRules.add(Rule(_appl(cr,At(X,`linearlhs),False()), _appl(nextRule,X) ) );
            }
          }
        }

        ConcRule() -> {
          localRules.add(Rule(_appl(rule,X), Bottom(X))); // X should be a term of the signature; morally X@!Bottom(_)
        }
      }
    } else { 
      // META-LEVEL
      gSig.addSymbol(rule,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
      gSig.addSymbol(cr,`ConcGomType(Signature.TYPE_METATERM,Signature.TYPE_BOOLEAN),Signature.TYPE_METATERM);

      %match(ruleList) {
        ConcRule(Rule(lhs,rhs),A*) -> {

          String nextRule = compileRuleList(`A*,generatedRules);

          TermList result = Tools.linearize(`lhs, this.generatedSignature);
          Term mlhs = Tools.metaEncodeConsNil(`lhs,generatedSignature);
          Term mrhs = Tools.metaEncodeConsNil(`rhs,generatedSignature);

          /*
           * propagate failure
           * rule(Bot(X)) -> Bot(X)
           */
          localRules.add(Rule(_appl(rule,Bottom(X)), Bottom(X)));

          %match(result) {
            TermList(_, Appl("True",TermList())) -> {
              /*
               * if already linear lhs
               * rule(X@mlhs) -> mrhs
               * rule(X@!mlhs) -> nextRule(X)       // could be Bot(X) if only one rule, i.e. non next rule
               */
              localRules.add(Rule(_appl(rule,At(X,mlhs)), mrhs));
              Term lhs = Main.options.ordered ? _appl(rule,X) : _appl(rule,At(X,Anti(mlhs)));
              localRules.add(Rule(lhs, _appl(nextRule,X)) );
//               if(!ordered){
//                 localRules.add(Rule(_appl(rule,At(X,Anti(mlhs))), _appl(nextRule,X) ) );
//               }else{
//                 localRules.add(Rule(_appl(rule,X), _appl(nextRule,X) ) );
//               }
            }

            TermList(linearlhs, cond@!Appl("True",TermList())) -> {
              // if non-linear add rules for checking equality for corresponding arguments
              Term mlinearlhs = Tools.metaEncodeConsNil(`linearlhs,generatedSignature);
              /*
               * rule(X@mlinearlhs) -> cr(X,cond)
               * rule(X@!mlinearlhs) -> nextRule(X)       // could be Bot(X) if only one rule, i.e. non next rule
               * cr(mlinearlhs, True) -> mrhs
               * cr(X@mlinearlhs, False) -> nextRule(X)       // could be Bot(X) if only one rule, i.e. non next rule
               */
              localRules.add(Rule(_appl(rule,At(X,mlinearlhs)), _appl(cr,X,`cond)));
              Term lhs = Main.options.ordered ? _appl(rule,X) : _appl(rule,At(X,Anti(mlinearlhs)));
              localRules.add(Rule(lhs, _appl(nextRule,X)) );
//               if(!ordered){
//                 localRules.add(Rule(_appl(rule,At(X,Anti(mlinearlhs))), _appl(nextRule,X)));
//               }else{
//                 localRules.add(Rule(_appl(rule,X), _appl(nextRule,X)));
//               }
              localRules.add(Rule(_appl(cr,mlinearlhs, True()), mrhs));
              localRules.add(Rule(_appl(cr,At(X,mlinearlhs), False()), _appl(nextRule,X)));
            }
          }

          // TODO: non-linear anti-pattern
          // localRules.add(`Rule(Appl(rule,TermList(At(X,Anti(mlhs)))),botX));
        }

        ConcRule() -> {
          localRules.add(Rule(_appl(rule,X), Bottom(X))); // X should be a term of the signature; morally X@!Bottom(_)
        }
      }

      for(String name:eSig.getSymbols()) {
        // add symb_a(), symb_b(), symb_f(), symb_g() in the signature
        gSig.addSymbol("symb_"+name,`ConcGomType(),Signature.TYPE_METASYMBOL);
      }
    }
    
    // put the locally generated rules at the beginning
    // efficient for LinkedLists but not for ArrayLists
    generatedRules.addAll(0,localRules);

    return rule;
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
    Signature gSig = getGeneratedSignature();
    Signature eSig = getExtractedSignature();

    // by default, if strategy can't be compiled, a meaningless name
    // TODO: change to exception ?
    List<Rule> generatedRules = null;

    // if the stategy has been already compiled and a symbol generated
    String strategySymbol = this.strategySymbols.get(strat);
    if(strategySymbol != null) {
      generatedRules = this.storedTRSs.get(strat);
      System.out.println("ALREADY EXISTING: "+strat);
    } else {
      generatedRules = new ArrayList<Rule>();
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
        // TODO: handle Set without an order
        StratExp(Set(_)) -> {
          throw new RuntimeException("Not Yet Implemented");
        }

        StratExp(List(rulelist)) -> {
          /*
           * Pre-treatment: remove anti-patterns (expandGeneralAntiPatterns)
           * move into compileStrategy ?
           */
          RuleList rList = `rulelist;
          if(Main.options.withAP == false) {
            RuleCompiler ruleCompiler = new RuleCompiler(eSig,gSig);
            rList = ruleCompiler.expandGeneralAntiPatterns(rList);

            for(Rule rule: rList.getCollectionConcRule()) {
              System.out.println("EXPANDED AP RULE: " + Pretty.toString(rule) );
            }
          }

          strategySymbol = this.compileRuleList(rList,generatedRules);
        }

        /*
         * TODO: fix non confluence here
         */
        StratMu(name,s) -> {
          try {
            String mu = Tools.getName(StrategyOperator.MU.getName());
            Strat newStrat = `TopDown(ReplaceMuVar(name,mu)).visitLight(`s);
            String phi_s = compileStrat(newStrat,generatedRules);
            if(!Main.options.metalevel) {
              gSig.addSymbol(mu,`ConcGomType(Signature.TYPE_TERM),Signature.TYPE_TERM);
              /*
               * mu(Bot(X)) -> Bot(X)
               * mu(X@!Bot(Y)) -> phi_s(X)
               */
              generatedRules.add(Rule(_appl(mu,Bottom(X)), Bottom(X)));
              Term lhs = Main.options.ordered ? _appl(mu,X) : _appl(mu,At(X,Anti(Bottom(Y))));
              generatedRules.add(Rule(lhs, _appl(phi_s,X)));
            } else {
              // META-LEVEL
              gSig.addSymbol(mu,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
              /*
               * mu(Bot(X)) -> Bot(X)
               * mu(Appl(Y,Z)) -> phi_s(Appl(Y,Z))
               */
              generatedRules.add(Rule(_appl(mu,Bottom(X)), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(mu,Appl(Y,Z)), _appl(phi_s,Appl(Y,Z))));
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
          String id = Tools.getName(StrategyOperator.IDENTITY.getName());
          if(!Main.options.metalevel) {
            gSig.addSymbol(id,`ConcGomType(Signature.TYPE_TERM),Signature.TYPE_TERM);
            if( !Main.options.approx ) {
              /*
               * the rule cannot be applied on arguments containing fresh
               * variables but only on terms from the signature or Bottom
               * normally it will follow reduction in original TRS
               * id(Bot(X)) -> Bot(X)
               * id(X@!Bot(Y)) -> X
               */
              generatedRules.add(Rule(_appl(id,Bottom(X)), Bottom(X)));
              Term lhs = Main.options.ordered ? _appl(id,X) : _appl(id,At(X,Anti(Bottom(Y))));
              generatedRules.add(Rule(lhs, X));
            } else { // TODO: remove APPROX branch?
              /*
               * Bottom of Bottom is Bottom
               * this is not necessary if exact reduction - in this case Bottom is propagated immediately 
               * id(X) -> X
               * Bot(Bot(X)) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(id,X), X));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }
          } else { 
            // Meta-LEVEL
            gSig.addSymbol(id,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
            if( !Main.options.approx ) {
              /*
               * id(Bot(X)) -> Bot(X)
               * id(Appl(X,Y)) -> Appl(X,Y)
               */
              generatedRules.add(Rule(_appl(id,Bottom(X)), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(id,Appl(X,Y)), Appl(X,Y)));
            } else {  // TODO: remove APPROX branch?
              /*
               * id(X) -> X
               * Bot(Bot(X)) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(id,X), X));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }
          }
          strategySymbol = id;
        }

        StratFail() -> {
          String fail = Tools.getName(StrategyOperator.FAIL.getName());
          if( !Main.options.metalevel ) {
            gSig.addSymbol(fail,`ConcGomType(Signature.TYPE_TERM),Signature.TYPE_TERM);
            if( !Main.options.approx ) {
              /*
               * fail(Bot(X)) -> Bot(X)
               * fail(X@!Bot(Y)) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(fail,Bottom(X)), Bottom(X)));
              Term lhs = Main.options.ordered ? _appl(fail,X) : _appl(fail,At(X,Anti(Bottom(Y))));
              generatedRules.add(Rule(lhs, X));
            } else { // TODO: remove APPROX branch?
              /*
               * fail(X) -> Bot(X)
               * Bot(Bot(X)) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(fail,X), Bottom(X)));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }
          } else {
            // META-LEVEL
            gSig.addSymbol(fail,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
            if( !Main.options.approx ) {
              /*
               * fail(Bot(X)) -> Bot(X)
               * fail(Appl(X,Y)) -> Bot(Appl(X,Y))
               */
              generatedRules.add(Rule(_appl(fail,Bottom(X)), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(fail,Appl(X,Y)), Bottom(Appl(X,Y))));
            } else { // TODO: remove APPROX branch?
              /*
               * fail(X) -> Bot(X)
               * Bot(Bot(X)) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(fail,X), Bottom(X)));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }

          }
          strategySymbol = fail;
        }

        StratSequence(s1,s2) -> {
          String n1 = compileStrat(`s1,generatedRules);
          String n2 = compileStrat(`s2,generatedRules);
          String seq = Tools.getName(StrategyOperator.SEQ.getName());
          String seq2 = Tools.getName(Tools.addAuxExtension(StrategyOperator.SEQ.getName()));
          if( !Main.options.metalevel ) {
            gSig.addSymbol(seq,`ConcGomType(Signature.TYPE_TERM),Signature.TYPE_TERM);
            gSig.addSymbol(seq2,`ConcGomType(Signature.TYPE_TERM,Signature.TYPE_TERM),Signature.TYPE_TERM);
            if( !Main.options.approx ) {
              /*
               * the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
               * normally it will follow reduction in original TRS
               * seq(Bot(X)) -> Bot(X)
               * seq(X@!Bot(Y)) -> seq2(n2(n1(X)),X)
               * seq2(Bot(Y),X) -> Bot(X)
               * seq2(X@!Bot(Y),Z) -> X
               */
              generatedRules.add(Rule(_appl(seq,Bottom(X)), Bottom(X)));
              Term lhs = Main.options.ordered ? _appl(seq,X) : _appl(seq,At(X,Anti(Bottom(Y))));
              generatedRules.add(Rule(lhs, _appl(seq2,_appl(n2,_appl(n1,X)),X)));
              generatedRules.add(Rule(_appl(seq2,Bottom(Y),X), Bottom(X)));
              Term nlhs = Main.options.ordered ? _appl(seq2,X,Z) : _appl(seq2,At(X,Anti(Bottom(Y))),Z);
              generatedRules.add(Rule(nlhs,X));
            } else { // TODO: remove APPROX branch?
              /*
               * seq(X) -> seq2(n2(n1(X)),X)
               * Bot(Bot(X)) -> Bot(X)
               * seq2(X@!Bot(Y),Z) -> X
               * seq2(Bot(Y),X) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(seq,X), _appl(seq2,_appl(n2,_appl(n1,X)),X)));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
              generatedRules.add(Rule(_appl(seq2,At(X,Anti(Bottom(Y))),Z), X));
              generatedRules.add(Rule(_appl(seq2,Bottom(Y),X), Bottom(X)));
            }
          } else { 
            // META-LEVEL
            gSig.addSymbol(seq,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
            gSig.addSymbol(seq2,`ConcGomType(Signature.TYPE_METATERM,Signature.TYPE_METATERM),Signature.TYPE_METATERM);
            if( !Main.options.approx ) {
              /*
               * seq(Bot(X)) -> Bot(X)
               * seq(Appl(X,Y)) -> seq2(n2(n1(Appl(X,Y))),Appl(X,Y))
               * seq2(Bot(Y),X) -> Bot(X)
               * seq2(Appl(X,Y),Z) -> Appl(X,Y)
               */
              generatedRules.add(Rule(_appl(seq,Bottom(X)), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(seq,Appl(X,Y)), _appl(seq2,_appl(n2,_appl(n1,Appl(X,Y))),Appl(X,Y))));
              generatedRules.add(Rule(_appl(seq2,Bottom(Y),X), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(seq2,Appl(X,Y),Z), Appl(X,Y)));
            } else { // TODO: remove APPROX branch?
              /*
               * CHECK HERE
               * seq(X) -> n2(n1(X))
               */
              generatedRules.add(Rule(X, _appl(n2,_appl(n1,X))));
            }

          }
          strategySymbol = seq;
        }

        StratChoice(s1,s2) -> {
          String n1 = compileStrat(`s1,generatedRules);
          String n2 = compileStrat(`s2,generatedRules);
          String choice = Tools.getName(StrategyOperator.CHOICE.getName());
          String choice2 = Tools.getName(Tools.addAuxExtension(StrategyOperator.CHOICE.getName()));
          if( !Main.options.metalevel ) {
            gSig.addSymbol(choice,`ConcGomType(Signature.TYPE_TERM),Signature.TYPE_TERM);
            gSig.addSymbol(choice2,`ConcGomType(Signature.TYPE_TERM),Signature.TYPE_TERM);
            /*
             * TODO [20/01/2015]: see if not exact is interesting
             * choice(Bot(X)) -> Bot(X)
             * choice(X@!Bot(Y)) -> choice2(n1(X))
             * choice2(Bot(X)) -> n2(X)
             * choice2(X@!Bot(Y)) -> X
             */
            generatedRules.add(Rule(_appl(choice,Bottom(X)), Bottom(X)));
            Term lhs = Main.options.ordered ? _appl(choice,X) : _appl(choice,At(X,Anti(Bottom(Y))));
            generatedRules.add(Rule(lhs, _appl(choice2,_appl(n1,X))));
            generatedRules.add(Rule(_appl(choice2,Bottom(X)), _appl(n2,X)));
            Term nlhs = Main.options.ordered ? _appl(choice2,X) : _appl(choice2,At(X,Anti(Bottom(Y))));
            generatedRules.add(Rule(nlhs, X));
          } else {
            // META-LEVEL
            gSig.addSymbol(choice,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
            gSig.addSymbol(choice2,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
            if( !Main.options.approx ) {
              /*
               * choice(Bot(X)) -> Bot(X)
               * choice(Appl(X,Y)) -> choice2(n1(Appl(X,Y)))
               * choice2(Bot(X)) -> n2(X)
               * choice2(Appl(X,Y) -> Appl(X,Y)
               */
              generatedRules.add(Rule(_appl(choice,Bottom(X)), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(choice,Appl(X,Y)), _appl(choice2,_appl(n1,Appl(X,Y)))));
              generatedRules.add(Rule(_appl(choice2,Bottom(X)), _appl(n2,X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(choice2,Appl(X,Y)), Appl(X,Y)));
            }
          }
          strategySymbol = choice;
        }

        StratAll(s) -> {
          String phi_s = compileStrat(`s,generatedRules);
          String all = Tools.getName(StrategyOperator.ALL.getName());
          if( !Main.options.metalevel ) {
            gSig.addSymbol(all,`ConcGomType(Signature.TYPE_TERM),Signature.TYPE_TERM);

            /*
             * propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
             * all(Bot(X)) -> Bot(X)
             */
            generatedRules.add(Rule(_appl(all,Bottom(X)), Bottom(X)));

            for(String name : eSig.getSymbols()) {
              int arity = gSig.getArity(name);
              int arity_all = arity+1;
              if(arity==0) {
                /*
                 * all(name) -> name
                 */
                generatedRules.add(Rule(_appl(all,_appl(name)), _appl(name)));
              } else {
                String all_n = Tools.addOperatorName(all,name);
                GomTypeList all_args = `ConcGomType();
                for(int i=0; i<arity_all; i++){
                  all_args = `ConcGomType(Signature.TYPE_TERM,all_args*);
                }
                gSig.addSymbol(all_n,all_args,Signature.TYPE_TERM);
                /*
                 * main case
                 * all(f(x1,...,xn)) -> all_n(phi_s(x1),phi_s(x2),...,phi_s(xn),f(x1,...,xn))
                 */
                Term[] a_lx = new Term[arity];
                Term[] a_rx = new Term[arity+1];
                for(int i=0 ; i<arity ; i++) {
                  Term Xi = `Var("X_" + i);
                  a_lx[i] = Xi;
                  a_rx[i] = _appl(phi_s, Xi);
                }
                a_rx[arity] = _appl(name, a_lx);
                generatedRules.add(Rule(_appl(all,_appl(name, a_lx)), _appl(all_n, a_rx)));
                /*
                 * generate failure rules
                 * phi_n(Bottom(_),_,...,_,Z) -> Bottom(Z)
                 * phi_n(...,Bottom(_),...,Z) -> Bottom(Z)
                 * phi_n(_,...,_,Bottom(_),Z) -> Bottom(Z)
                 */
                Term[] a_llx = new Term[arity+1];
                for(int i=0 ; i<arity ; i++) {
                  Term X0 = `Var("X0");
                  a_llx[0] = (i==0)?Bottom(X0):X0;
                  for(int j=1 ; i<arity ; i++) {
                    Term Xj = `Var("X_" + i);
                    if(j==i) {
                      a_llx[j] = Bottom(Xj);
                    } else {
                      a_llx[j] = Xj;
                    }
                  }
                  a_llx[arity] = Z;
                  generatedRules.add(Rule(_appl(all_n,a_llx), Bottom(Z)));
                }
                /*
                 * generate success rules
                 * all_g(X1@!Bottom(Y1),...,X_n@!Bottom(Y_n),_) -> g(X1,...,X_n)
                 */
                a_lx = new Term[arity+1];
                a_rx = new Term[arity];
                for(int i=0 ; i<arity ; i++) {
                  Term Xi = `Var("X_" + i);
                  Term Yi = `Var("Y" + i);
                  a_lx[i] = Main.options.ordered ? Xi : At(Xi,Anti(Bottom(Yi)));
                  a_rx[i] = Xi;
                }
                a_lx[arity] = Z;
                generatedRules.add(Rule(_appl(all_n,a_lx), _appl(name, a_rx)));
              }
            }
          } else {
            // META-LEVEL
            gSig.addSymbol(all,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
            String all_1 = all+"_1";
            String all_2 = all+"_2";
            String all_3 = all+"_3";
            String append = "append";
            String reverse = "reverse";
            String rconcat = "rconcat";
            generatedSignature.addSymbol(all_1,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
            generatedSignature.addSymbol(all_2,`ConcGomType(Signature.TYPE_METALIST),Signature.TYPE_METALIST);
            generatedSignature.addSymbol(all_3,`ConcGomType(Signature.TYPE_METATERM,Signature.TYPE_METALIST,Signature.TYPE_METALIST,Signature.TYPE_METALIST),Signature.TYPE_METALIST);
            generatedSignature.addSymbol(append,`ConcGomType(Signature.TYPE_METALIST,Signature.TYPE_METATERM),Signature.TYPE_METALIST);
            generatedSignature.addSymbol(reverse,`ConcGomType(Signature.TYPE_METALIST),Signature.TYPE_METALIST);
            generatedSignature.addSymbol(rconcat,`ConcGomType(Signature.TYPE_METALIST,Signature.TYPE_METALIST),Signature.TYPE_METALIST);

            /*
             * propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
             * all(Bot(X)) -> Bot(X)
             */
            generatedRules.add(Rule(_appl(all,Bottom(X)), Bottom(X)));

            /*
             * all(Appl(Z0,Z1)) -> all_1(Appl(Z0,all_2(Z1)))
             * all_1(Appl(Z0, BottomList(Z))) -> Bottom(Appl(Z0,Z))
             * all_1(Appl(Z0, Cons(Z1,Z2))) -> Appl(Z0,Cons(Z1,Z2))
             * all_1(Appl(Z0, Nil)) -> Appl(Z0,Nil)
             * all_2(Nil) -> Nil
             * all_2(Cons(Z1,Z2)) -> all_3(phi_s(Z1),Z2,Cons(Z1,Nil),Nil)
             * all_3(Bottom(X),todo,rargs,rs_args) -> BottomList(rconcat(rargs,todo))
             * all_3(Appl(X,Y),Nil,rargs,rs_args) -> reverse(Cons(Appl(X,Y),rs_args))
             * all_3(Appl(X,Y), Cons(XX,YY), rargs, rs_args) -> 
             * all_3(phi_s(XX), YY, Cons(XX,rargs), Cons(Appl(X,Y),rs_args))
             */
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
              /*
               * append(Nil,Z) -> Cons(Z,Nil)
               * append(Cons(X,Y),Z) -> Cons(X,append(Y,Z))
               * reverse(Nil) -> Nil
               * reverse(Cons(X,Y)) -> append(reverse(Y),X)
               * rconcat(Nil,Z) -> Z
               * rconcat(Cons(X,Y),Z) -> rconcat(Y,Cons(X,Z))
               */
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
          String one = Tools.getName(StrategyOperator.ONE.getName());
          if( !Main.options.metalevel ) {
            gSig.addSymbol(one,`ConcGomType(Signature.TYPE_TERM),Signature.TYPE_TERM);

            /*
             * propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
             * one(Bottom(X)) -> Bottom(X)
             */
            generatedRules.add(Rule(_appl(one,Bottom(X)), Bottom(X)));

            for(String name : eSig.getSymbols()) {
              int arity = eSig.getArity(name);
              if(arity==0) {
                /*
                 * one(name) -> Bottom(name)
                 */
                generatedRules.add(Rule(_appl(one,_appl(name)), Bottom(_appl(name))));
              } else {
                String one_n = Tools.addOperatorName(one,name);
                String one_n_1 = one_n + "_1";
                /*
                 * main case
                 * one(f(x1,...,xn)) -> one_n_1(phi_s(x1),x2,...,xn)
                 */
                Term[] a_lx = new Term[arity];
                Term[] a_rx = new Term[arity];
                for(int i=1 ; i<=arity ; i++) {
                  Term Xi = `Var("X_" + i);
                  a_lx[i-1] = Xi;
                  a_rx[i-1] = (i==1)?_appl(phi_s, Xi):Xi;
                }
                generatedRules.add(Rule(_appl(one,_appl(name, a_lx)), _appl(one_n_1, a_rx)));

                for(int i=1 ; i<=arity ; i++) {
                  String one_n_i = one_n + "_" + i;
                  String one_n_ii = one_n + "_"+(i+1);
                  GomTypeList one_n_args = `ConcGomType();
                  GomTypeList one_n_ii_args = `ConcGomType();
                  for(int ni=0; ni<arity; ni++) {
                    one_n_args = `ConcGomType(Signature.TYPE_TERM, one_n_args*);
                    one_n_ii_args = `ConcGomType(Signature.TYPE_TERM, one_n_ii_args*);
                  }
                  gSig.addSymbol(one_n_i,one_n_args,Signature.TYPE_TERM);
                  if(i<arity) {
                    gSig.addSymbol(one_n_ii,one_n_ii_args,Signature.TYPE_TERM);
                    /*
                     * one_f_i(Bottom(x1),...,Bottom(xi),xj,...,xn)
                     * -> one_f_(i+1)(Bottom(x1),...,Bottom(xi),phi_s(x_i+1),...,xn)
                     */
                    for(int j=1 ; j<=arity ; j++) {
                      Term Xj = Var("X_"+j);
                      if(j<=i) {
                        a_lx[j-1] = Bottom(Xj);
                        a_rx[j-1] = Bottom(Xj);
                      } else {
                        a_lx[j-1] = Xj;
                        a_rx[j-1] = (j==i+1)?_appl(phi_s,Xj):Xj;
                      }
                    }
                    /*
                     * one_n_i(lx) -> one_n_ii(rx)
                     */
                    generatedRules.add(Rule(_appl(one_n_i,a_lx), _appl(one_n_ii, a_rx)));
                  } else {
                    /*
                     * one_f_n(Bottom(x1),...,Bottom(xn)) -> Bottom(f(x1,...,xn))
                     */
                    for(int j=1 ; j<=arity ; j++) {
                      Term Xj = Var("X_"+j);
                      a_lx[j-1] = Bottom(Xj);
                      a_rx[j-1] = Xj;
                    }
                    generatedRules.add(Rule(_appl(one_n_i,a_lx), Bottom(_appl(name, a_rx))));
                  }
                  /*
                   * one_f_i(Bottom(x1),...,xi@!Bottom(_),xj,...,xn)
                   * -> f(x1,...,xi,...,xn)
                   */
                  for(int j=1 ; j<=arity ; j++) {
                    Term Xj = Var("X_"+j);
                    if(j<i) {
                      a_lx[j-1] = Bottom(Xj);
                    } else if(j==i) {
                      a_lx[j-1] = Main.options.ordered ? Xj : At(Xj,Anti(Bottom(Y)));
                    } else {
                      a_lx[j-1] = Xj;
                    }
                    a_rx[j-1] = Xj;
                  }
                  /*
                   * one_n_i(lx) -> name(rx)
                   */
                  generatedRules.add(Rule(_appl(one_n_i,a_lx), _appl(name, a_rx)));

                }
              }
            }
          } else {
            // META-LEVEL
            gSig.addSymbol(one,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
            String one_1 = one+"_1";
            String one_2 = one+"_2";
            String one_3 = one+"_3";
            String append = "append";
            String reverse = "reverse";
            String rconcat = "rconcat";
            generatedSignature.addSymbol(one_1,`ConcGomType(Signature.TYPE_METATERM),Signature.TYPE_METATERM);
            generatedSignature.addSymbol(one_2,`ConcGomType(Signature.TYPE_METALIST),Signature.TYPE_METALIST);
            generatedSignature.addSymbol(one_3,`ConcGomType(Signature.TYPE_METATERM,Signature.TYPE_METALIST,Signature.TYPE_METALIST),Signature.TYPE_METALIST);
            generatedSignature.addSymbol(append,`ConcGomType(Signature.TYPE_METALIST,Signature.TYPE_METATERM),Signature.TYPE_METALIST);
            generatedSignature.addSymbol(reverse,`ConcGomType(Signature.TYPE_METALIST),Signature.TYPE_METALIST);
            generatedSignature.addSymbol(rconcat,`ConcGomType(Signature.TYPE_METALIST,Signature.TYPE_METALIST),Signature.TYPE_METALIST);
            /*
             * one(Appl(Z0,Z1)) -> one_1(Appl(Z0,one_2(Z1)))
             */
            generatedRules.add(Rule(_appl(one,Appl(Z0,Z1)), _appl(one_1,Appl(Z0,_appl(one_2,Z1)))));
            /*
             * one_1(Appl(Z0,BottomList(Z))) -> Bottom(Appl(Z0,Z))
             * one_1(Appl(Z0,Cons(Z1,Z2))) -> Appl(Z0,Cons(Z1,Z2))
             */
            generatedRules.add(Rule(_appl(one_1,Appl(Z0,BottomList(Z))), Bottom(Appl(Z0,Z))));
            generatedRules.add(Rule(_appl(one_1,Appl(Z0,Cons(Z1,Z2))), Appl(Z0,Cons(Z1,Z2))));
            /*
             * one_2(Nil) -> BottomList(Nil)
             * one_2(Cons(X,Y)) -> one_3(phi_s(X),Y,Cons(X,Nil))
             */
            generatedRules.add(Rule(_appl(one_2,Nil()), BottomList(Nil())));
            generatedRules.add(Rule(_appl(one_2,Cons(Z1,Z2)), _appl(one_3,_appl(phi_s,Z1),Z2,Cons(Z1,Nil()))));
            /*
             * one_3(Bottom(X),Nil,rargs) -> BottomList(reverse(rargs))
             * one_3(Bottom(X),Cons(head,tail),rargs) -> one_3(phi_s(head), tail, Cons(head,rargs))
             * one_3(Appl(X,Y),todo,Cons(last,rargs)) -> rconcat(rargs,Cons(Appl(X,Y),todo))
             */
            generatedRules.add(Rule(_appl(one_3,Bottom(Z),Nil(),Z2), BottomList(_appl(reverse,Z2))));
            generatedRules.add(Rule(_appl(one_3,Bottom(Z),Cons(XX,YY),Z2), _appl(one_3,_appl(phi_s,XX),YY,Cons(XX,Z2))));
            generatedRules.add(Rule(_appl(one_3,Appl(X,Y),Z1,Cons(Z2,Z3)), _appl(rconcat,Z3,Cons(Appl(X,Y),Z1))));

            if(!generated_aux_functions) { 
              generated_aux_functions = true;
              /*
               * append(Nil,Z) -> Cons(Z,Nil)
               * append(Cons(X,Y),Z) -> Cons(X,append(Y,Z))
               * reverse(Nil) -> Nil
               * reverse(Cons(X,Y)) -> append(reverse(Y),X)
               * rconcat(Nil,Z) -> Z
               * rconcat(Cons(X,Y),Z) -> rconcat(Y,Cons(X,Z))
               */
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


  /*
   * Generate equality rules:
   * f(x_1,...,x_n) = g(y_1,...,y_m) -> False
   * f(x_1,...,x_n) = f(y_1,...,y_n) -> x_1=y1 ^ ... ^ x_n=y_n ^ true
   */
  private RuleList generateEquality(RuleList generatedRules) {
    Signature eSig = getExtractedSignature();

    generatedRules = `ConcRule(generatedRules*,Rule(And(True(),True()), True()));
    generatedRules = `ConcRule(generatedRules*,Rule(And(True(),False()), False()));
    generatedRules = `ConcRule(generatedRules*,Rule(And(False(),True()), False()));
    generatedRules = `ConcRule(generatedRules*,Rule(And(False(),False()), False()));

    Set<String> symbolNames = eSig.getSymbols();
    for(String f:symbolNames) {
      for(String g:symbolNames) {
        int arf = eSig.getArity(f);
        int arg = eSig.getArity(g);
        Term[] a_lx = new Term[arf];
        Term[] a_rx = new Term[arg];
        for(int i=1 ; i<=arf ; i++) {
          a_lx[i-1] = Var("X_" + i);
        }
        for(int i=1 ; i<=arg ; i++) {
          a_rx[i-1] = Var("Y" + i);
        }
        if(!f.equals(g)) {
          /*
           * eq(f(x1,...,xn),g(y1,...,ym)) -> False
           */
          generatedRules = `ConcRule(generatedRules*,Rule(Eq(_appl(f,a_lx),_appl(g,a_rx)), False()));
        } else {
          /*
           * eq(f(x1,...,xn),f(y1,...,yn)) -> True ^ eq(x1,y1) ^ ... ^ eq(xn,yn)
           */
          Term scond = True();
          for(int i=1 ; i<=arf ; i++) {
            scond = And(Eq(Var("X_" + i),Var("Y" + i)),scond);
          }
          generatedRules = `ConcRule(generatedRules*,Rule(Eq(_appl(f,a_lx),_appl(g,a_rx)), scond));
        }
      }
    }
    return generatedRules;
  }

  /**
   * generates encode/decode functions for metalevel
   * encode(f(x1,...,xn)) -> Appl(symb_f, Cons(encode(x1), ..., Cons(encode(xn),Nil())))
   **/
  private RuleList generateEncodeDecode(RuleList generatedRules) {
    Signature eSig = getExtractedSignature();
    String x = Tools.getName("X");
    Set<String> symbolNames = eSig.getSymbols();
    for(String f:symbolNames) {
      int arf = eSig.getArity(f);

      Term args_encode = Nil();
      Term largs_decode = Nil();
      TermList rargs_decode = `TermList();

      for(int i=arf ; i>=1 ; i--) {
        Term var = Var(x+"_"+i);
        args_encode = Cons(_appl(Signature.ENCODE,var),args_encode);
        largs_decode = Cons(var,largs_decode);
        rargs_decode = `TermList(_appl(Signature.DECODE,var),rargs_decode*);
      }

      Term lhs_encode = _appl(Signature.ENCODE, Tools.genAbstractTerm(f,arf,x));
      Term rhs_encode = Appl(_appl("symb_" + f), args_encode);

      Term lhs_decode = _appl(Signature.DECODE, Appl(_appl("symb_" + f), largs_decode));
      Term rhs_decode = `Appl(f,rargs_decode); 

      generatedRules = `ConcRule(generatedRules*, Rule(lhs_encode,rhs_encode));
      generatedRules = `ConcRule(generatedRules*, Rule(lhs_decode,rhs_decode));
    }
    return generatedRules;
  }


  /**
   * generates a rule of the form name(X) -> symbol(X)
   * name can be then see as an alias for symbol in terms of reduction 
   **/
  private RuleList generateTriggerRule(String name, String symbol, RuleList generatedRules) {
    Signature gSig = getGeneratedSignature();
    GomType codomain = gSig.getCodomain(symbol);
    GomTypeList domain = gSig.getDomain(symbol);
    gSig.addSymbol(name,domain,codomain);

    Term X = Var(Tools.getName("X"));
    /*
     * X matches anything but morally matches only symbols from the extracted signature
     * name(X) -> symbol(X)
     */
    return `ConcRule(generatedRules*, Rule(_appl(name,X), _appl(symbol,X)));
  }


}


