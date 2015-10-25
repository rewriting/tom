package sa;

import sa.rule.types.*;
import tom.library.sl.*; 
import java.util.*;

public class Pretty {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Collection.tom }

  private static String generateJavaName(String name) {
    name = name.replace('-','_');
    return name;
  }

  public static String toString(ExpressionList l) {
    StringBuffer sb = new StringBuffer();
    %match(l) {
      ExpressionList(_*,x,_*) -> {
        sb.append(toString(`x)); 
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  public static String toString(Expression e) {
    StringBuffer sb = new StringBuffer();
    %match(e) {
      Set(rulelist) -> {
        sb.append("{ ");
        %match(rulelist) {
          ConcRule(_*,x,end*) -> {
            sb.append(toString(`x));
            if(!`end.isEmptyConcRule()) {
              sb.append(", ");
            }
          }
        }
        sb.append(" }");
      }

      Strat(s) -> {
        sb.append(`s);
      }
     /* 
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
      */

    }
    return sb.toString();
  }
  
  public static String toString(Rule r) {
    %match(r) {
      Rule(lhs,rhs) -> {
        return toString(`lhs) + " -> " + toString(`rhs);
      }
    }
    return "toString(Rule): error";
  }

  public static String toString(RuleList rules) {
    String res = "";
    for(Rule r: rules.getCollectionConcRule()) {
      res += toString(r) + "\n";
    }
    return res;
  }

  public static String addBrace(String name) {
    if(Main.options.aprove || Main.options.timbuk) {
      return name;
    } else {
      return name + "()";
    }
  }

  public static String toString(TermList t) {
    return toStringAux(t, ",");
  }

  private static String toStringAux(TermList t, String sep) {
    StringBuffer sb = new StringBuffer();
    %match(t) {
      TermList(_*,x,end*) -> {
        sb.append(toString(`x));
        if(!`end.isEmptyTermList()) {
          sb.append(sep);
        }
      }
    }
    return sb.toString();
  }

  private static String toString(AddList t) {
    StringBuffer sb = new StringBuffer();
    %match(t) {
      ConcAdd(_*,x,end*) -> {
        sb.append(toString(`x));
        if(!`end.isEmptyConcAdd()) {
          sb.append("+");
        }
      }
    }
    return sb.toString();
  }

  public static String toString(Term t) {
    %match(t) {
      Var(n) -> { return `n; }

      BuiltinInt(n) -> { return ""+`n; }

      Anti(p) -> { return "!" + toString(`p); }

      At(var,term) -> { return toString(`var) + "@" + toString(`term); }

      Appl(symb,args) -> {
        String name = generateJavaName(`symb);
        if(`args.isEmptyTermList()) {
          return addBrace(name);
        } else {
          return name + "(" + toString(`args) + ")";
        }
      }

      Add(ConcAdd(l*)) -> {
        return "(" + toString(`l) + ")";
      }

      Sub(t1,t2) -> {
        return "(" + toString(`t1) + " \\ " + toString(`t2) + ")";
      }

      Empty() -> {
        return "empty";
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

  public static String generateAprove(RuleList bag, boolean innermost) 
    throws VisitFailure {
    StringBuffer rulesb = new StringBuffer();
    Collection<String> varSet = new HashSet<String>();

    rulesb.append("\n(RULES\n");
    //     for(Rule r:bag) {
    %match(bag) {
      ConcRule(_*,r,_*) -> {
        `BottomUp(CollectVars(varSet)).visit(`r);
        rulesb.append("        " + toString(`r) + "\n");
      }
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
  
  public static String generateTimbuk(RuleList bag, Signature generatedSignature) throws VisitFailure {
    StringBuffer rulesb = new StringBuffer();
    StringBuffer opsb = new StringBuffer();
    StringBuffer varsb = new StringBuffer();
    Collection<String> varSet = new HashSet<String>();

    opsb.append("\nOps\n");
    for(String name: generatedSignature.getSymbols()) {
      opsb.append(generateJavaName(name)  + ":" + generatedSignature.getArity(name) + " ");
    }

    rulesb.append("\nTRS R\n");
    //     for(Rule r:bag) {
    %match(bag) {
      ConcRule(_*,r,_*) -> {
        `BottomUp(CollectVars(varSet)).visit(`r);
        rulesb.append("        " + toString(`r) + "\n");
      }
    }

    varsb.append("\nVars\n");
    for(String name: varSet) {
      varsb.append(name + " ");
    }
    varsb.deleteCharAt(varsb.length()-1);

    return opsb.toString() + "\n" + varsb.toString() + "\n" + rulesb.toString();
  }  

  public static String generateTom(String strategyName, String typeName, RuleList bag, Signature sig, String classname) {
    System.out.println("--------- TOM ----------------------");
    //     System.out.println("RULEs: " + toString(bag));

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
]%);
    // generate signature
    for(GomType codomain: sig.getCodomains()) {
      sb.append("      " + codomain.getName() + " = \n");
      for(String name: sig.getSymbols(codomain)) {
        int arity = sig.getArity(name);
        GomTypeList domain = sig.getDomain(name);
        String args = "";
        int i = 0;
        while(!domain.isEmptyConcGomType()) {
          String argTypeName = domain.getHeadConcGomType().getName();
          args += "kid" + i + "_" + argTypeName + ":" + argTypeName;
          if(i+1<arity) { args += ","; }
          domain = domain.getTailConcGomType();
          i++;
        }


        sb.append("        | " + generateJavaName(name) + "(" + args + ")\n");
      }

    }

    // generate rules
    sb.append("      module m:rules() {\n");
    //     for(Rule r:bag) {
    %match(bag) {
      ConcRule(_*,r,_*) -> {
        sb.append("        " + toString(`r) + "\n");
      }
    }

    String inputTypeName = Signature.TYPE_TERM.getName();
    if(typeName != null) { // if typed compilation and initial term of type typeName
      inputTypeName = typeName;
    }

    sb.append(%[
    }
  }
  
  public static void main(String[] args) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      @inputTypeName@ input = @inputTypeName@.fromString(reader.readLine());
      long start = System.currentTimeMillis();
      ]%);

  // by default use the last compiled strategy
  String name = "no_strategy_name";
  if(Compiler.getInstance().getStrategyNames().contains(strategyName)) {
     name = strategyName;
  }
  if(typeName != null) { // if typed compilation and initial term of type typeName
      name += "_" + typeName;
  }

  if(Main.options.metalevel) {
    sb.append(%[
      Term t = `decode(@name@(encode(input)));
      ]%);
  } else {
      sb.append(%[
      @inputTypeName@ t = `@name@(input);
      ]%);
  }

  sb.append(%[
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
