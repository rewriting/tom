package sa;

import sa.rule.types.*;
import tom.library.sl.*; 
import java.util.*;

public class Pretty {
  %include { rule/Rule.tom }
  %include { sl.tom }

  private static HashMap<String,Term> varsig = new HashMap<String,Term>();

  public static String toString(ExpressionList l, boolean forAprove) {
    StringBuffer sb = new StringBuffer();
    %match(l) {
      ExpressionList(_*,x,_*) -> {
        sb.append(Pretty.toString(`x,forAprove)); 
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  public static String toString(Expression e, boolean forAprove) {
    StringBuffer sb = new StringBuffer();
    %match(e) {
      Let(x,v,t) -> {
        sb.append("let ");
        sb.append(`x);
        sb.append(" = ");
        sb.append(Pretty.toString(`v,forAprove));
        sb.append(" in ");
        sb.append(Pretty.toString(`t,forAprove));
      }

      Set(rulelist) -> {
        sb.append("{ ");
        %match(rulelist) {
          RuleList(_*,x,end*) -> {
            sb.append(Pretty.toString(`x,forAprove));
            if(!`end.isEmptyRuleList()) {
              sb.append(", ");
            }
          }
        }
        sb.append(" }");
      }

      Strat(s) -> {
        sb.append(`s);
      }
      
      Signature(symbollist) -> {
        sb.append("signature { ");
        %match(symbollist) {
          SymbolList(_*,Symbol(name,arity),end*) -> {
            sb.append(`name + ":" + `arity);
            if(!`end.isEmptySymbolList()) {
              sb.append(", ");
            }
          }
        }
        sb.append(" }");
      }


    }
    return sb.toString();
  }
  
  public static String toString(Rule r, boolean forAprove) {
    StringBuffer sb = new StringBuffer();
    %match(r) {
    Rule(lhs,rhs) -> {
      sb.append(Pretty.toString(`lhs,forAprove));
      sb.append(" -> ");
      sb.append(Pretty.toString(`rhs,forAprove));
    }
    }
    return sb.toString();
  }

  public static String toString(Term t, boolean forAprove) {
    StringBuffer sb = new StringBuffer();
    %match(t) {
      Var(n) -> { sb.append(`n); }

      BuiltinInt(n) -> { sb.append(`n); }

      Anti(p) -> { 
        sb.append("!"); 
        sb.append(Pretty.toString(`p,forAprove));
      }

      Appl(symb,args) -> { 
        sb.append(`symb); 

        if(!(`args.isEmptyTermList() && forAprove)){
          sb.append("(");
          %match(args) {
            TermList(_*,x,end*) -> {
              sb.append(Pretty.toString(`x,forAprove));
              if(!`end.isEmptyTermList()) {
                sb.append(",");
              }
            }
          }
          sb.append(")");
        }
      }
    }
    return sb.toString();
  }

//   %typeterm TreeSet {
//           implement      { java.util.TreeSet }
//   }

  %typeterm HashSet {
          implement      { java.util.HashSet }
  }

  %typeterm HashMap {
          implement      { java.util.HashMap }
  }

  // Collect the variables
  %strategy CollectVars(varSet:HashSet) extends Identity() {
    visit Term {
      Var(v)  -> {
        varSet.add(`v);
      }
    }
  }

  // Collect anti-patterns
  %strategy CollectAntiPatterns(pos:HashSet) extends Identity() {
    visit Term {
      Anti(t)  -> {
        pos.add(getEnvironment().getPosition());
      }
    }
  }

  // Replace the !t by variable
  %strategy ReplaceAnti() extends Identity() {
    visit Term {
      Anti(t)  -> {
        %match(t) {
          Appl(name,_) -> {
            //             String n = `name;
            return varsig.get(`name);
          }
          _ -> {
            System.out.println("TTTT=" +`t);
            return null; // TO CHANGE
          }
        }
      }
    }
  }

  public static HashSet<Term> generateTermsWithoutAntiPatterns(Term t, Map<String,Term> expsig) 
    throws VisitFailure{

    HashSet<Term> termSet = new HashSet<Term>();

    HashSet<Position> posSet = new HashSet<Position>();
    Strategy collectAntiPatternPositions = `CollectAntiPatterns(posSet);

    // Collect anti-patterns
    `BottomUp(collectAntiPatternPositions).visit(t);

    if(posSet.isEmpty()){
      termSet.add(t);
    } else {
      // Generate rules without anti-pattern
      for(Position pos: posSet){
        Term at = (Term)pos.getSubterm().visitLight(t);
        String symbolName = "DoesntExist";
        %match(at){
          Anti(Appl(name,_)) -> { symbolName = `name;}
        }
        if(symbolName.compareTo("doesntexist")==0){
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

  public static String generateAprove(List<Rule> bag, Map<String,Integer> sig, Map<String,Integer> origsig) 
    throws VisitFailure{

    // generate depth 1 replacement terms for each symbol in the signature (+Bottom)
    // use origsig since normally anti-patterns are only on symbols in signature + Bottom
    Map<String,Term> expsig = new HashMap<String,Term>();
    origsig.put("Bottom",0);
    for(String name: origsig.keySet()) {
      int arity = origsig.get(name);
      TermList tl = `TermList();
      for(int i=0 ; i<arity ; i++) {
        tl = `TermList(Var("Z"+i),tl*);
      }
      Term t = `Appl(name,tl);
      expsig.put(name,t);
    }
    System.out.println(expsig+"\n");

    // doesn't work when passed as parameter to ReplaceAnti
    //     HashMap<String,Term> varsig = new HashMap<String,Term>();

    // generate different variables for each symbol ()
    // use origsig since normally anti-patterns are only on symbols in signature + Bottom
    int varn = 0;
    for(String name: origsig.keySet()) {
      Term t = `Var("Z"+varn++);
      varsig.put(name,t);
    }
    System.out.println(varsig+"\n");

    // 1) RULES with anti-patterns replaced by variable
    StringBuffer rulesb = new StringBuffer();
    Strategy replaceAnti = `ReplaceAnti();

    HashSet<String> varSet = new HashSet<String>();
    Strategy collectVars = `CollectVars(varSet);

    Collections.sort(bag, new MyRuleComparator());

    rulesb.append("\n(RULES\n");
    for(Rule r:bag) {
      Rule newRULE = null;
      %match(r){  
        Rule(lhs,rhs) -> {
          // replace !t by Var
          Term newLHS = `BottomUp(replaceAnti).visit(`lhs);
          newRULE = `Rule(newLHS,rhs);
          // Collect the variables when replacing anti-pattern by var
          `BottomUp(collectVars).visit(newLHS);
        }
      }
      rulesb.append("        " + Pretty.toString(newRULE,true) + "\n");
    }
    rulesb.append(")\n");

    StringBuffer varsb = new StringBuffer();
    varsb.append("(VAR ");
    for(String name: varSet) {
      varsb.append(name + ",");
    }
    varsb.deleteCharAt(varsb.length()-1);
    varsb.append(")");

    //2) RULES with anti-pattern replaced by corresponding terms
    StringBuffer ruleallsb = new StringBuffer();

    HashSet<String> allVarSet = new HashSet<String>();
    Strategy collectAllVars = `CollectVars(allVarSet);

    //     TreeSet<Rule> ruleSet = new TreeSet<Rule>();
    HashSet<Rule> ruleSet = new HashSet<Rule>();

    for(Rule r:bag) {
      %match(r){  
        Rule(lhs,rhs) -> {
          // Generate LHSs without anti-pattern
          HashSet<Term> termSet = generateTermsWithoutAntiPatterns(`lhs,expsig);
          for(Term t: termSet){
            // generate rule for each lhs generated 
            ruleSet.add(`Rule(t,rhs));
            // Collect the variables when replacing anti-pattern by term
            `BottomUp(collectAllVars).visit(t);
          }
        }
      }
    }


    List<Rule> ruleList = new ArrayList<Rule>(ruleSet);
    Collections.sort(ruleList, new MyRuleComparator());

    ruleallsb.append("\n(RULES\n");
    for(Rule r:ruleList) {
      ruleallsb.append("        " + Pretty.toString(r,true) + "\n");
    }
    ruleallsb.append(")\n");

    StringBuffer allvarsb = new StringBuffer();
    allvarsb.append("\n(STRATEGY INNERMOST)\n");
    allvarsb.append("(VAR ");
    for(String name: allVarSet) {
      allvarsb.append(name + ",");
    }
    allvarsb.deleteCharAt(allvarsb.length()-1);
    allvarsb.append(")");


    return varsb.toString()+rulesb.toString()+allvarsb.toString()+ruleallsb.toString();
  }  



  public static String generate(List<Rule> bag, Map<String,Integer> sig, String classname) {
    StringBuffer sb = new StringBuffer();
    String lowercaseClassname = classname.toLowerCase();
    sb.append(
        %[
import @lowercaseClassname@.m.types.*;
import java.io.*;
public class @classname@ {
  %gom {
    module m
      abstract syntax
      T = Bottom()
]%);
    for(String name: sig.keySet()) {
      int arity = sig.get(name);
      String args = "";
      for(int i=0 ; i<arity ; i++) {
        args += "kid"+i+":T";
        if(i+1<arity) { args += ","; }
      }
      sb.append("        | " + name + "(" + args + ")\n");
    }

    Collections.sort(bag, new MyRuleComparator());

    sb.append("      module m:rules() {\n");
    for(Rule r:bag) {
      sb.append("        " + Pretty.toString(r,false) + "\n");
    }
    sb.append(%[
    }
  }
  

  public static void main(String[] args) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      T input = T.fromString(reader.readLine());
      T t = `@Compiler.getTopName()@(input);
      System.out.println(t);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
    
    ]%);

    return sb.toString();
  }

}

