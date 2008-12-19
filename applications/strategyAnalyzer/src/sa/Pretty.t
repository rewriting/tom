package sa;

import sa.rule.types.*;
import java.util.*;

public class Pretty {
  %include { rule/Rule.tom }

  public static String toString(ExpressionList l) {
    StringBuffer sb = new StringBuffer();
    %match(l) {
      ExpressionList(_*,x,_*) -> {
        sb.append(Pretty.toString(`x)); 
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  public static String toString(Expression e) {
    StringBuffer sb = new StringBuffer();
    %match(e) {
      Let(x,v,t) -> {
        sb.append("let ");
        sb.append(`x);
        sb.append(" = ");
        sb.append(Pretty.toString(`v));
        sb.append(" in ");
        sb.append(Pretty.toString(`t));
      }

      Set(rulelist) -> {
        sb.append("{ ");
        %match(rulelist) {
          RuleList(_*,x,end*) -> {
            sb.append(Pretty.toString(`x));
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
  
  public static String toString(Rule r) {
    StringBuffer sb = new StringBuffer();
    %match(r) {
    Rule(lhs,rhs) -> {
      sb.append(Pretty.toString(`lhs));
      sb.append(" -> ");
      sb.append(Pretty.toString(`rhs));
    }
    }
    return sb.toString();
  }

  public static String toString(Term t) {
    StringBuffer sb = new StringBuffer();
    %match(t) {
      Var(n) -> { sb.append(`n); }

      BuiltinInt(n) -> { sb.append(`n); }

      Anti(p) -> { 
        sb.append("!"); 
        sb.append(Pretty.toString(`p));
      }

      Appl(symb,args) -> { 
        sb.append(`symb); 
        sb.append("(");
        %match(args) {
          TermList(_*,x,end*) -> {
            sb.append(Pretty.toString(`x));
            if(!`end.isEmptyTermList()) {
              sb.append(",");
            }
          }
        }
        sb.append(")");
      }
    }
    return sb.toString();
  }

  public static String generate(Collection<Rule> bag, Map<String,Integer> sig, String classname) {
    StringBuffer sb = new StringBuffer();

    System.out.println(sig);

    for(Rule r:bag) {
      System.out.println(Pretty.toString(r));
    }
    return sb.toString();
  }

}

