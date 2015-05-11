package sa;

import sa.rule.types.*;
import tom.library.sl.*; 
import java.util.*;

public class Pretty {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Collection.tom }

  private HashMap<String,Term> varsig = new HashMap<String,Term>();

  public String toString(ExpressionList l) {
    StringBuffer sb = new StringBuffer();
    %match(l) {
      ExpressionList(_*,x,_*) -> {
        sb.append(toString(`x)); 
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  public String toString(Expression e) {
    StringBuffer sb = new StringBuffer();
    %match(e) {
      Let(x,v,t) -> {
        sb.append("let ");
        sb.append(`x);
        sb.append(" = ");
        sb.append(toString(`v));
        sb.append(" in ");
        sb.append(toString(`t));
      }

      Set(rulelist) -> {
        sb.append("{ ");
        %match(rulelist) {
          RuleList(_*,x,end*) -> {
            sb.append(toString(`x));
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
  
  public String toString(Rule r) {
    %match(r) {
      Rule(lhs,rhs) -> {
        return toString(`lhs) + " -> " + toString(`rhs);
      }
    }
    return "toString(Rule): error";
  }

  public String addBrace(String name) {
    if(Main.options.aprove) {
      return name;
    } else {
      return name + "()";
    }
  }

  public String toString(TermList t) {
    StringBuffer sb = new StringBuffer();
    %match(t) {
      TermList(_*,x,end*) -> {
        sb.append(toString(`x));
        if(!`end.isEmptyTermList()) {
          sb.append(",");
        }
      }
    }
    return sb.toString();
  }

  public String toString(Term t) {
    %match(t) {
      Var(n) -> { return `n; }

      BuiltinInt(n) -> { return ""+`n; }

      Anti(p) -> { return "!" + toString(`p); }

      At(var,term) -> { return toString(`var) + "@" + toString(`term); }

      Appl(symb,args) -> {
        if(`args.isEmptyTermList()) {
          return addBrace(`symb);
        } else {
          return `symb + "(" + toString(`args) + ")";
        }
      }
    }
    return "toString(Term): error";
  }

  // Collect the variables
  %strategy CollectVars(varSet:Collection) extends Identity() {
    visit Term {
      Var(v)  -> {
        varSet.add(`v);
      }
    }
  }

  public String generateAprove(List<Rule> bag, Map<String,Integer> extractedSignature, boolean innermost) 
    throws VisitFailure {
    StringBuffer rulesb = new StringBuffer();
    Collection<String> varSet = new HashSet<String>();

    rulesb.append("\n(RULES\n");
    for(Rule r:bag) {
      `BottomUp(CollectVars(varSet)).visit(r);
      rulesb.append("        " + toString(r) + "\n");
    }
    rulesb.append(")\n");

    StringBuffer varsb = new StringBuffer();
    if(innermost) {
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

  public String generateTom(List<Rule> bag, Map<String,Integer> sig, String classname) {
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
      T = 
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
      sb.append("        " + toString(r) + "\n");
    }
    sb.append(%[
    }
  }
  
  public static void main(String[] args) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      T input = T.fromString(reader.readLine());
      long start = System.currentTimeMillis();
      T t = `@Compiler.getInstance().getTopName()@(input);
      long stop = System.currentTimeMillis();
      System.out.println(t);
      System.out.println("time1 (ms): " + ((stop-start)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
]%);

    return sb.toString();
  }

}
