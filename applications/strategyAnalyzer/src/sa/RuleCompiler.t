package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;

public class RuleCompiler {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Collection.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/HashSet.tom }
  %include { java/util/types/List.tom }
  %include { java/util/types/ArrayList.tom }

  // The extracted (concrete) signature
  private Map<String,Integer> extractedSignature;
  // The generated (concrete) signature
  private Map<String,Integer> generatedSignature;

  private Collection<Rule> generatedRules;


  //   private  Tools tools = new Tools();
  // pretty is used just for debugging
  // TODO: remove it
  private static  Pretty pretty = new Pretty();


  public RuleCompiler(Map<String,Integer> extractedSignature, Map<String,Integer> generatedSignature){
    this.extractedSignature=extractedSignature;
    this.generatedSignature=generatedSignature;
  }


  
  /**
   * Expand an anti-patterns in each of the rules in the list
   * - the rules replacing the orginal should be at the same position in the list
   * @param generatedRules initial set of rules
   * @param rule the rule to expand
   * @return the modified list of generatedRules (with no anti-paterns left)
   */
  public  List<Rule> expandAntiPatterns(List<Rule> rules) {
    List<Rule> newRules = new ArrayList<Rule>();
    for(Rule rule:rules) { 
      List<Rule> genRules=expandAntiPatternInRule(rule);
      // add the generated rules for rule to the result (list of rule)
      newRules.addAll(genRules);
    }
    return newRules;
  }
  
  /**
   * Expand an anti-pattern
   * @param generatedRules initial set of rules
   * @param rule the rule to expand
   * @return the list of generated rules
   */
  private List<Rule> expandAntiPatternInRule(Rule rule) {
    System.out.println("EXPAND:" + pretty.toString(rule));
    List<Rule> genRules = new ArrayList<Rule>();
    try {
      `OnceBottomUp(ContainsAntiPattern()).visitLight(rule); // check if the rule contains an anti-pattern (exception otherwise)
      List<Rule> bag = new ArrayList<Rule>();
      // perform one-step expansion
      `OnceTopDown(ExpandAntiPattern(bag,rule,this.extractedSignature, this.generatedSignature)).visit(rule);
      for(Rule expandr:bag) {
        // add the list of rules generated for the expandr rule to the final result
        System.out.println("RULE to expand:" + pretty.toString(expandr));
        List<Rule> expandedRules = expandAntiPatternInRule(expandr);
        genRules.addAll(expandedRules);
      }
    } catch(VisitFailure e) {
      // add the rule since it contains no anti-pattern
      genRules.add(rule);
    }
    return genRules;
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
  %strategy ExpandAntiPattern(bag:List,subject:Rule,extractedSignature:Map, generatedSignature:Map) extends Fail() {
    visit Term {
      Anti(Anti(t)) -> {
        Rule newr = (Rule) getEnvironment().getPosition().getReplace(`t).visit(subject);
        //System.out.println("add bag0:" + pretty.toString(newr));
        bag.add(newr);
        return `t;
      }

      Anti(t) -> {
        System.out.println("ExpandAntiPattern: " + `t);
        Term antiterm = (Main.options.generic)?Tools.decodeConsNil(`t):`t;
        System.out.println("ExpandAntiPattern antiterm: " + antiterm);
        %match(antiterm) { 
          Appl(name,args)  -> {
            // add g(Z1,...) ... h(Z1,...)
            Set<String> otherNames = new HashSet<String>( ((Map<String,Integer>)extractedSignature).keySet() );
            for(String otherName:otherNames) {
              if(!`name.equals(otherName)) {
                int arity = ((Map<String,Integer>)extractedSignature).get(otherName);
                Term newt = Tools.encode(Tools.genAbstractTerm(otherName,arity,Tools.getName("Z")),generatedSignature);
                if(Main.options.generic) {
                  newt = Tools.metaEncodeConsNil(newt,generatedSignature);
                }
                Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);
                System.out.println("add bag1:" + pretty.toString(newr));
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
              //               System.out.println("NEWT:"+`newt);
              array[i] = Tools.encode(z+"_"+i,generatedSignature);
              if(Main.options.generic) {
                newt = Tools.metaEncodeConsNil(newt,generatedSignature);
              }
              Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);

              System.out.println("add bag2:" + pretty.toString(newr));
              bag.add(newr);
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


  public   Map<String,Integer> getExtractedSignature(){
    return this.extractedSignature;
  }
  public Map<String,Integer> getGeneratedSignature(){
    return this.generatedSignature;
  }


}
