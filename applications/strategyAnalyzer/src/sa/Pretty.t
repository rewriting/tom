package sa;

import sa.rule.types.*;
import tom.library.sl.*; 
import java.util.*;

public class Pretty {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/HashSet.tom }

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

  public static String toString(TermList t, boolean forAprove) {
    StringBuffer sb = new StringBuffer();
    %match(t) {
      Cons(x,end) -> {
        sb.append(Pretty.toString(`x,forAprove));
        if(!`end.isNil()) {
          sb.append(",");
          sb.append(Pretty.toString(`end,forAprove));
        }
      }

      TermList(_*,x,end*) -> {
        sb.append(Pretty.toString(`x,forAprove));
        if(!`end.isEmptyTermList()) {
          sb.append(",");
        }
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

      At(var,term) -> { 
        sb.append(Pretty.toString(`var,forAprove));
        sb.append("@"); 
        sb.append(Pretty.toString(`term,forAprove));
      }

      Appl(symb,args) -> { 
        sb.append(`symb); 
        if(!(`args.isEmptyTermList() && forAprove)) {
          sb.append("(");
          sb.append(Pretty.toString(`args,forAprove));
          sb.append(")");
        }
      }
    }
    return sb.toString();
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
            return `Var("BOBO"); // TO CHANGE
          }
        }
      }
    }
  }


  public static String generateAprove(List<Rule> bag, Map<String,Integer> extractedSignature, boolean innermost) 
    throws VisitFailure{

    // doesn't work when passed as parameter to ReplaceAnti
    //     HashMap<String,Term> varsig = new HashMap<String,Term>();

    // generate different variables for each symbol ()
    // use extractedSignature since normally anti-patterns are only on symbols in signature + Bottom
    int varn = 0;
    for(String name: extractedSignature.keySet()) {
      Term t = `Var("Z"+varn++);
      varsig.put(name,t);
    }
    //     System.out.println(varsig+"\n");

    //  anti-patterns replaced by variable
    StringBuffer rulesb = new StringBuffer();
    Strategy replaceAnti = `ReplaceAnti();

    HashSet<String> varSet = new HashSet<String>();
    Strategy collectVars = `CollectVars(varSet);

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
    if(innermost){
      varsb.append("(STRATEGY INNERMOST)\n");
    }
    varsb.append("(VAR ");
    for(String name: varSet) {
      varsb.append(name + " ");
    }
    varsb.deleteCharAt(varsb.length()-1);
    varsb.append(")");

    return varsb.toString()+rulesb.toString();
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
      T = Bottom(term:T)
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

