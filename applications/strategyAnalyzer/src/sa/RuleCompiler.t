package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;

public class RuleCompiler {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/List.tom }
  %include { java/util/types/ArrayList.tom }

  %typeterm Signature { implement { Signature } }

  // The extracted (concrete) signature
  private Signature extractedSignature;
  // The generated (concrete) signature
  private Signature generatedSignature;
  // The generated (ordered) TRS
  private List<Rule> generatedRules;

  public RuleCompiler(Signature extractedSignature, Signature generatedSignature){
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
  public  List<Rule> expandAntiPatterns(List<Rule> rules) {
    List<Rule> newRules = new ArrayList<Rule>();
    for(Rule rule:rules) { 
      List<Rule> genRules=this.expandAntiPatternInRule(rule);
      // add the generated rules for rule to the result (list of rule)
      newRules.addAll(genRules);
    }
    return newRules;
  }  
  /**
   * Expand an anti-pattern in a LINEAR Rule (with only one anti-pattern)
   * @param generatedRules initial set of rules
   * @param rule the rule to expand
   * @return the list of generated rules
   */
  private List<Rule> expandAntiPatternInRule(Rule rule) {
    List<Rule> genRules = new ArrayList<Rule>();
    try {
      `OnceBottomUp(ContainsAntiPattern()).visitLight(rule); // check if the rule contains an anti-pattern (exception otherwise)
      List<Rule> bag = new ArrayList<Rule>();
      // perform one-step expansion
      `OnceTopDown(ExpandAntiPattern(bag,rule,this.extractedSignature, this.generatedSignature)).visit(rule);
      // for each generated rule restart the expansion
      for(Rule expandr:bag) {
        // add the list of rules generated for the expandr rule to the final result
        List<Rule> expandedRules = this.expandAntiPatternInRule(expandr);
        genRules.addAll(expandedRules);
      }
    } catch(VisitFailure e) {
      // add the rule since it contains no anti-pattern
      genRules.add(rule);
    }
    return genRules;
  }
  /**
   * Do nothing if it Term contains anti-pattern;
   * Fail (i.e. exception) otherwise
   */
  %strategy ContainsAntiPattern() extends Fail() {
    visit Term {
      t@Anti(_)  -> { return `t; }
    }
  }
  /**
   * Perform one-step expansion for a LINEAR Rule
   * @param bag the resulted list of rules
   * @param rule the rule to expand
   * @param extractedSignature the extracted signature
   * @param generatedSignature the generated signature
   */
  %strategy ExpandAntiPattern(bag:List,subject:Rule,extractedSignature:Signature, generatedSignature:Signature) extends Fail() {
    visit Term {
      Anti(Anti(t)) -> {
        Rule newr = (Rule) getEnvironment().getPosition().getReplace(`t).visit(subject);
        bag.add(newr);
        return `t;
      }

      Anti(t) -> {
        Term antiterm = (Main.options.metalevel)?Tools.decodeConsNil(`t):`t;
        %match(antiterm) { 
          Appl(name,args)  -> {
            // add g(Z1,...) ... h(Z1,...)
            //             Set<String> otherNames = new HashSet<String>( ((Map<String,Integer>)extractedSignature).keySet() );
            Set<String> otherNames = new HashSet<String>(extractedSignature.getSymbolNames() );
            for(String otherName:otherNames) {
              if(!`name.equals(otherName)) {
                int arity = extractedSignature.getArity(otherName);
                Term newt = Tools.encode(Tools.genAbstractTerm(otherName,arity,Tools.getName("Z")),generatedSignature);
                if(Main.options.metalevel) {
                  newt = Tools.metaEncodeConsNil(newt,generatedSignature);
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
            String z = Tools.getName("Z");
            for(int i=0 ; i<arity ; i++) {
              array[i] = Tools.encode(z+"_"+i,generatedSignature);
            }
            for(int i=0 ; i<arity ; i++) {
              Term ti = tarray[i];
              array[i] = `Anti(ti);
              Term newt = `Appl(name,sa.rule.types.termlist.TermList.fromArray(array));
              array[i] = Tools.encode(z+"_"+i,generatedSignature);
              if(Main.options.metalevel) {
                newt = Tools.metaEncodeConsNil(newt,generatedSignature);
              }
              Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);

              bag.add(newr);
            }
          }
        }
        return `t;
      }
    }
  }
  /********************************************************************************
   *     END
   ********************************************************************************/

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



  /********************************************************************************
   *     Transform a list of rules with @ in a list of rules with the
   *     @ expanded accordingly. The order of rules is preserved.
   ********************************************************************************/
  /**
    * Transforms a set of rules that contain x@t into a set of rules without @ 
    * @param bag the set of rules to expand
    * @return a new set that contains the expanded rules
    */
  public  List<Rule> expandAt(List<Rule> bag) throws VisitFailure {
    List<Rule> res = new ArrayList<Rule>();
    for(Rule rule:bag) {
      Map<String,Term> map = new HashMap<String,Term>();
      `TopDown(CollectAt(map)).visitLight(rule);
      if(map.keySet().isEmpty()) {
        // if no AT in the rule just add it to the result
        res.add(rule);
      }else{
        // if some AT in the rule then build a new one
        Rule newRule = rule;
        for(String name:map.keySet()) {
          Term t = map.get(name);
          // replace the ATs with the corresponding expressions
          newRule = `TopDown(ReplaceVariable(name,t)).visitLight(newRule);
          // and remove the ATs
          newRule = `TopDown(EliminateAt()).visitLight(newRule);
        }
        res.add(newRule);
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
  /********************************************************************************
   *     END
   ********************************************************************************/


  public Signature getExtractedSignature(){
    return this.extractedSignature;
  }
  public Signature getGeneratedSignature(){
    return this.generatedSignature;
  }

}
