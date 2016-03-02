package sa;

import sa.rule.types.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;
import com.google.common.collect.HashMultiset;

import static sa.Tools._appl;
import static sa.Tools.Var;
import static sa.Tools.At;
import static sa.Tools.Bottom;
import static sa.Tools.Bottom2;


public class RuleCompiler {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/List.tom }
  %include { java/util/types/ArrayList.tom }

  %typeterm Signature { implement { Signature } }
  %typeterm HashMultiset { implement { HashMultiset }}

  // The extracted (concrete) signature
  private Signature extractedSignature;
  // The generated (concrete) signature
  private Signature generatedSignature;
  // The generated (ordered) TRS
  private List<Rule> generatedRules;

  public RuleCompiler(Signature extractedSignature, Signature generatedSignature) {
    this.extractedSignature=extractedSignature;
    this.generatedSignature=generatedSignature;
  }

  /********************************************************************************
   *     Transform a list of LINEAR rules (with only one anti-pattern)
   *     in a list of rules with no anti-patterns. The order of rules
   *     is preserved.
   ********************************************************************************/
  /**
   * Expand an anti-pattern in each of the LINEAR rules (with only one anti-pattern) in the list
   * - the rules replacing the orginal are at the same position in the list
   * @param rules the rules to expand
   * @return the list of generatedRules (with no anti-paterns left)
   */
  public RuleList expandAntiPatterns(RuleList rules) {
    return expandAntiPatternsAux(rules, true);
  }
  
  /*
   * Remove nested anti-patterns
   * @param rules the list of rules to expand
   */
  public RuleList expandGeneralAntiPatterns(RuleList rules, String nextRuleSymbol) {
    rules = expandAntiPatternsAux(rules, false);
    if(nextRuleSymbol == null) { //if we don't know what to do with the Bottom2 (than change to Bottom)
      rules = eliminateBottom2(rules);
    }else{ // chain to the next call
      rules = changeBottom2(rules,nextRuleSymbol);      
    }
    return rules;
  }

  /*
   * Remove nested anti-patterns
   * @param rules the list of rules to expand
   * @param postTreatment true to remove only top-level anti-patterns
   * @returns a (ordered) list of rules
   */
  public RuleList expandAntiPatternsAux(RuleList rules, boolean postTreatment) {
    RuleList newRules = `ConcRule();
    for(Rule rule:rules.getCollectionConcRule()) {
      RuleList genRules = this.expandAntiPatternInRule(`rule, postTreatment);
      // add the generated rules for rule to the result (list of rule)
      newRules = `ConcRule(newRules*,genRules*);
    }
    return newRules;
  }  
  
  /**
   * Expand a rule (with anti-patterns) in list of LINEAR rules (with only one anti-pattern)
   * @param rule the rule to expand
   * @param postTreatment true to remove only top-level anti-pattern
   * @return the list of generated rules
   */
  private RuleList expandAntiPatternInRule(Rule rule, boolean postTreatment) {
    RuleList genRules = `ConcRule();
    try {
      // collect the anti-patterns in a multiset
      HashMultiset<Term> antiBag = HashMultiset.create();
      Term lhs = rule.getlhs();
      `TopDown(CollectAnti(antiBag)).visitLight(lhs);
      int nbOfAnti = antiBag.size();
      if(nbOfAnti == 0) {
        // add the rule since it contains no anti-pattern
        genRules = `ConcRule(genRules*,rule);
      } else {
        List<Rule> ruleList = new ArrayList<Rule>();
        if(postTreatment) {
          if(true || Property.isLinear(lhs)) {
            /*
             * case: rule is left-linear and there is only one negation
             * should only be done in post-treatment, not during compilation of strategies
             */
            `OnceTopDown(ExpandAntiPatternPostTreatment(ruleList,rule,this.extractedSignature, this.generatedSignature)).visit(rule);
          } else {
            System.out.println("NON LIN: " + Pretty.toString(rule) );
            throw new RuntimeException("Should not be there");
          }
        } else {
          /*
           * General case
           */
          `OnceTopDown(ExpandGeneralAntiPattern(ruleList,rule)).visit(rule);
        }

        // for each generated rule re-start the expansion
        for(Rule expandr:ruleList) {
          // add the list of rules generated for the expandr rule to the final result
          RuleList expandedRules = this.expandAntiPatternInRule(expandr,postTreatment);
          genRules = `ConcRule(genRules*,expandedRules*);
        }

      }
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }
    return genRules;
  }
 
  /**
   * Perform one-step expansion for a LINEAR Rule
   * @param ruleList the resulting list of rules (extended by side effect)
   * @param subject the rule to expand
   * @param extractedSignature the extracted signature
   * @param generatedSignature the generated signature
   */
  %strategy ExpandAntiPatternPostTreatment(ruleList:List,subject:Rule,extractedSignature:Signature, generatedSignature:Signature) extends Fail() {
    visit Term {
      Anti(Anti(t)) -> {
        Rule newr = (Rule) getEnvironment().getPosition().getReplace(`t).visit(subject);
        ruleList.add(newr);
        return `t;
      }

      Anti(t) -> {
        Term antiterm = (Main.options.metalevel)?Tools.metaDecodeConsNil(`t):`t;
        %match(antiterm) { 
          Appl(name,args)  -> {
            // add g(Z1,...) ... h(Z1,...)
            for(String otherName: extractedSignature.getConstructors()) {
              if(!`name.equals(otherName)) {
                int arity = extractedSignature.getArity(otherName);
                Term newt = Tools.genAbstractTerm(otherName,arity,Tools.getName("Z"));
                if(Main.options.metalevel) {
                  newt = Tools.metaEncodeConsNil(newt,generatedSignature);
                }
                Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);
                ruleList.add(newr);
              }
            }
            
            // add f(!a1,...) ... f(a1,...,!an)
            sa.rule.types.termlist.TermList tl = (sa.rule.types.termlist.TermList) `args;
            //int arity = extractedSignature.getArity(`name); // arity(Bottom)=-1 instead of 1 !!!
            int arity = tl.length();
            //System.out.println(`name + " -- " + arity);

            Term[] arrayOfVariable = new Term[arity];
            Term[] arrayOfTerm = new Term[arity];
            arrayOfTerm = tl.toArray(arrayOfTerm);
            String Z = Tools.getName("Z");
            for(int i=1 ; i<=arity ; i++) {
              arrayOfVariable[i-1] = Var(Z +"_"+ i);
            }
            for(int i=1 ; i<=arity ; i++) {
              Term variable = arrayOfVariable[i-1];
              Term ti = arrayOfTerm[i-1];
              arrayOfVariable[i-1] = `Anti(ti);
              Term newt = `Appl(name,sa.rule.types.termlist.TermList.fromArray(arrayOfVariable));
              arrayOfVariable[i-1] = variable;
              if(Main.options.metalevel) {
                newt = Tools.metaEncodeConsNil(newt,generatedSignature);
              }
              Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);
              ruleList.add(newr);
            }
          }
        }
        return `t;
      }
    }
  }

  /*
   * Perform one-step expansion
   *
   * @param orderedTRS the resulting list of rules
   * @param subject the rule to expand (may contain nested anti-pattern and be non-linear)
   */
  %strategy ExpandGeneralAntiPattern(orderedTRS:List,subject:Rule) extends Fail() {
    visit Term {
      s@Anti(Anti(t)) -> {
        Rule newr = (Rule) getEnvironment().getPosition().getReplace(`t).visit(subject);
        `orderedTRS.add(newr);
        return `s;
      }

      s@Anti(t) -> {
        /*
         * x@q[!q'] -> bot(x,r) becomes   q[q'] -> r
         *                              x@q[z]  -> bot(x,r)
         *
         *   q[!q'] -> r        becomes x@q[q'] -> bot(x,r)
         *                                q[z]  -> r
         */

        // here: t is q'
        System.out.println("EXPAND AP: " + Pretty.toString(subject));
        %match(subject) {
          Rule(lhs,Appl(bottom,TermList(x,r))) -> {
            if(`bottom.equals(Signature.BOTTOM2)) {
              // here we generate x@q[q'] but x will be eliminated later
              Rule r1 = (Rule) getEnvironment().getPosition().getReplace(`t).visit(subject);
              r1 = r1.setrhs(`r);

              Term Z = Var(Tools.getName("Z"));
              Rule r2 = (Rule) getEnvironment().getPosition().getReplace(Z).visit(subject);
              r2 = r2.setrhs(Bottom2(`x,`r));

              `orderedTRS.add(r1);
              `orderedTRS.add(r2);
              System.out.println("  case 1 ==> " + Pretty.toString(r1));
              System.out.println("  case 1 ==> " + Pretty.toString(r2));
              return `s;
            }
          }

          Rule(lhs,r) -> {
            Term X = Var(Tools.getName("X"));
            Rule r1 = (Rule) getEnvironment().getPosition().getReplace(`t).visit(subject);
            r1 = r1.setlhs(At(X,r1.getlhs()));
            r1 = r1.setrhs(Bottom2(X,r1.getrhs()));

            // here we generate x@q[z] but x will be eliminated later
            Term Z = Var(Tools.getName("Z"));
            Rule r2 = (Rule) getEnvironment().getPosition().getReplace(Z).visit(subject);

            `orderedTRS.add(r1);
            `orderedTRS.add(r2);
            System.out.println("  case 2 ==> " + Pretty.toString(r1));
            System.out.println("  case 2 ==> " + Pretty.toString(r2));
            return `s;
          }

        }
      }
    }
  }

  /*
   * replace Bottom2 by Bottom
   */
  public RuleList eliminateBottom2(RuleList subject) {
    RuleList res = subject;
    try {
      res = `TopDown(EliminateBottom2()).visitLight(subject);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }
    return res;
  }

  %strategy EliminateBottom2() extends Identity() {
    visit Term {
      Appl(bottom2,TermList(x,r)) -> {
        if(`bottom2.equals(Signature.BOTTOM2)) {
          return Bottom(`x);
        }
      }
    }
  }

  /*
   * replace Bottom2(x,_) by nextRuleSymbol(x)
   */
  public RuleList changeBottom2(RuleList subject, String nextRuleSymbol) {
    RuleList res = subject;
    try {
      res = `TopDown(ChangeBottom2(nextRuleSymbol)).visitLight(subject);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }
    return res;
  }

  %strategy ChangeBottom2(String nextRuleSymbol) extends Identity() {
    visit Term {
      Appl(bottom2,TermList(x,r)) -> {
        if(`bottom2.equals(Signature.BOTTOM2)) {
          return _appl(nextRuleSymbol,`x);
        }
      }
    }
  }

  /********************************************************************************
   *     Transform a list of rules with @ in a list of rules with the
   *     @ expanded accordingly. The order of rules is preserved.
   ********************************************************************************/
  /**
    * Transforms a list of rules that contain x@t into a list of rules without @ 
    * @param ruleList the list of rules to expand
    * @return a new list that contains the expanded rules
    */
  public RuleList expandAt(RuleList ruleList) {
    RuleList res = `ConcRule();
    ArrayList<Rule> list = new ArrayList<Rule>();
    for(Rule rule:ruleList.getCollectionConcRule()) {
      Map<String,Term> map = new HashMap<String,Term>();
      try {
        `TopDown(CollectAt(map)).visitLight(`rule); // add x->t into map for each x@t
      } catch(VisitFailure e) {
      }

      //System.out.println("AT MAP: " + map);
      //System.out.println("RULE: " + rule);

      if(map.keySet().isEmpty()) {
        // if no AT in the rule just add it to the result
        assert !Tools.containsAt(rule): rule;
        //res = `ConcRule(res*,rule);
        list.add(rule);
      } else {
        // if some AT in the rule then build a new one
        Rule newRule = `rule;
        for(String name:map.keySet()) {
          Term t = map.get(name);
          try {
            // replace the ATs with the corresponding expressions
            if(name != "_") { // special treatment for _@t: they will be removed by EliminateAt
              newRule = `BottomUp(ReplaceVariable(name,t)).visitLight(newRule);
            }
            // and remove the ATs
            newRule = `BottomUp(EliminateAt()).visitLight(newRule);
          } catch(VisitFailure e) {
          }
        }
        assert !Tools.containsAt(newRule): newRule;
        //res = `ConcRule(res*,newRule);
        list.add(newRule);
      }
    }
    for(Rule r:list) {
      res = `ConcRule(r,res*);
    }
    res = res.reverse();
    return res;
  }

  // search all AT and store their values
  %strategy CollectAt(map:Map) extends Identity() {
    visit Term {
      At(Var(name),t2)-> {
        // we remove the AT from the term we store in the map
        map.put(`name,removeAt(`t2));
      }
    }
  }
  
  private static tom.library.sl.Visitable removeAt(tom.library.sl.Visitable t) {
    try {
      return `TopDown(RemoveAt()).visitLight(t);
    } catch(VisitFailure e) {
      throw new RuntimeException("should not be there");
    }
  }

  %strategy RemoveAt() extends Identity() {
    visit Term {
      At(_,t) -> {
        return `t;
      }
    }
  }

  // search all Anti symbols
  %strategy CollectAnti(bag:HashMultiset) extends Identity() {
    visit Term {
      x@Anti(t)-> {
        bag.add(`x);
      }
    }
  }

  // replace x by t, and thus x@t by t@t
  %strategy ReplaceVariable(name:String, term:Term) extends Identity() {
    visit Term {
      Var(n) -> {
        if(`n.equals(name)) {
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
      At(Var("_"),t) -> {
        return `t;
      }
    }
  }
  /********************************************************************************
   *     END
   ********************************************************************************/


  public Signature getExtractedSignature() {
    return this.extractedSignature;
  }
  public Signature getGeneratedSignature() {
    return this.generatedSignature;
  }

}
