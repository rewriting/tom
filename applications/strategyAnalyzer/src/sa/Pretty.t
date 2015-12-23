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

  /*
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
    }
    return sb.toString();
  }
  */

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

      TrueMatch() -> {
        return "TrueMatch";
      }
      
      Match(t1,t2) -> {
        return "(" + toString(`t1) + " << " + toString(`t2) + ")";
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

  public static String generateAprove(RuleList ruleList, boolean innermost) 
    throws VisitFailure {
    StringBuffer rulesb = new StringBuffer();
    Collection<String> varSet = new HashSet<String>();

    rulesb.append("\n(RULES\n");
    %match(ruleList) {
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
  
  public static String generateTimbuk(RuleList ruleList, Signature generatedSignature) throws VisitFailure {
    StringBuffer rulesb = new StringBuffer();
    StringBuffer opsb = new StringBuffer();
    StringBuffer varsb = new StringBuffer();
    Collection<String> varSet = new HashSet<String>();

    opsb.append("\nOps\n");
    for(String name: generatedSignature.getSymbols()) {
      opsb.append(generateJavaName(name)  + ":" + generatedSignature.getArity(name) + " ");
    }

    rulesb.append("\nTRS R\n");
    %match(ruleList) {
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

  public static String generateTom(Set<String> strategyNames, RuleList ruleList, Signature esig, Signature gsig) {
    String classname = Main.options.classname;
    boolean isTyped = Main.options.withType;
    System.out.println("--------- TOM ----------------------");
    //     System.out.println("RULEs: " + toString(ruleList));

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
    for(GomType codomain: gsig.getCodomains()) {
      sb.append("      " + codomain.getName() + " = \n");
      for(String name: gsig.getSymbols(codomain)) {
        int arity = gsig.getArity(name);
        GomTypeList domain = gsig.getDomain(name);
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
    %match(ruleList) {
      ConcRule(_*,r,_*) -> {
        sb.append("        " + toString(`r) + "\n");
      }
    }

    // generate main
    sb.append(%[
    }
  }
  
  public static void main(String[] args) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      Object input = @classname@.fromString(reader.readLine());
      long start = System.currentTimeMillis();
      Object t = @classname@.mainStrat(input);
      System.out.println(t);
      long stop = System.currentTimeMillis();
      System.out.println("time1 (ms): " + ((stop-start)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  ]%);

    for(String strategyName:strategyNames) {
      // generate strategyName
      sb.append(%[
  public static Object @strategyName@(Object t) {]%);

      if(isTyped) {
        for(GomType codomain: esig.getCodomains()) {
      sb.append(%[
    if(t instanceof @codomain.getName()@) {
      return `@strategyName@_@codomain.getName()@((@codomain.getName()@)t);
    }]%);
      }
      sb.append(%[
    throw new RuntimeException("cannot find a mainstrat for: " + t);]%);
      }

      if(isTyped) {
        // generate nothing
      } else if(Main.options.metalevel) {      
        sb.append(%[
  	return `decode(@strategyName@(encode((Term)t)));]%);
      } else {
        sb.append(%[
    return `@strategyName@((Term) t);]%);
      }
      sb.append(%[    
  }
  ]%); // end strategyName
    }

  // generate fromString
  sb.append(%[
  public static Object fromString(String s) {
  ]%);

  if(isTyped) {
    for(GomType codomain: esig.getCodomains()) {
      sb.append(%[
    try {
      return @codomain.getName()@.fromString(s);
    } catch(IllegalArgumentException e) {
    }
      ]%);
    }

    sb.append(%[
    throw new RuntimeException("cannot find a valid type for: " + s);
    ]%);

  } else {
    sb.append(%[
    return Term.fromString(s);
    ]%);

  }

  sb.append(%[    
  }
  ]%); // end fromString


  sb.append(%[    
}
  ]%); // end class

    return sb.toString();
  }

}
