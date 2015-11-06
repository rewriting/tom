package sa;

import sa.rule.types.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;
import com.google.common.collect.HashMultiset;

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
   */
  public RuleList expandGeneralAntiPatterns(RuleList rules) {
    rules = expandAntiPatternsAux(rules, false);
    rules = eliminateBottom2(rules);
    return rules;
  }

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
   * Expand an anti-pattern in a LINEAR Rule (with only one anti-pattern)
   * @param generatedRules initial set of rules
   * @param rule the rule to expand
   * @return the list of generated rules
   */
  private RuleList expandAntiPatternInRule(Rule rule, boolean postTreatment) {
    RuleList genRules = `ConcRule();
    try {
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
          if(true || Tools.isLinear(lhs)) {
            /*
             * case: rule is left-linear and there is only one negation
             * should only be done in post-treatment, not during compilation of strategies
             */
            `OnceTopDown(ExpandAntiPatternPostTreatment(ruleList,rule,this.extractedSignature, this.generatedSignature)).visit(rule);
          } else {
            System.out.println("NON LIN: " + Pretty.toString(rule) );
            throw new RuntimeException("Should not be there EXP ");
          }
        } else {
          /*
           * General case
           */
          `OnceTopDown(ExpandGeneralAntiPattern(ruleList,rule)).visit(rule);
        }

        // for each generated rule restart the expansion
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
   * @param ruleList the resulted list of rules
   * @param rule the rule to expand
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
        Term antiterm = (Main.options.metalevel)?Tools.decodeConsNil(`t):`t;
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
   * @param ordered list of rules
   * @param rule the rule to expand (may contain nested anti-pattern and be non-linear)
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
          Rule(lhs,Appl(bottom,TermList(x,r))) && bottom == Signature.BOTTOM2 -> {
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
      Appl(bottom2,TermList(x,r)) && bottom2 == Signature.BOTTOM2 -> {
        return Bottom(`x);
      }
    }
  }

  /********************************************************************************
   *     Transform a list of rules with @ in a list of rules with the
   *     @ expanded accordingly. The order of rules is preserved.
   ********************************************************************************/
  /**
    * Transforms a set of rules that contain x@t into a set of rules without @ 
    * @param ruleList the set of rules to expand
    * @return a new set that contains the expanded rules
    */
  public  RuleList expandAt(RuleList ruleList) throws VisitFailure {
    RuleList res = `ConcRule();
    for(Rule rule:ruleList.getCollectionConcRule()) {
      Map<String,Term> map = new HashMap<String,Term>();
      `TopDown(CollectAt(map)).visitLight(`rule); // add x->t into map for each x@t

//       System.out.println("AT MAP: " + map);

      if(map.keySet().isEmpty()) {
        // if no AT in the rule just add it to the result
        res = `ConcRule(res*,rule);
      } else {
        // if some AT in the rule then build a new one
        Rule newRule = `rule;
        for(String name:map.keySet()) {
          Term t = map.get(name);
          // replace the ATs with the corresponding expressions
          newRule = `TopDown(ReplaceVariable(name,t)).visitLight(newRule);
          // and remove the ATs
          newRule = `TopDown(EliminateAt()).visitLight(newRule);
        }
        res = `ConcRule(res*,newRule);
      }
    }
    return res;
  }

  // search all At and store their values
  %strategy CollectAt(map:Map) extends Identity() {
    visit Term {
      At(Var(name),t2)-> {
        map.put(`name,`t2);
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
        if(`n == name) {
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
