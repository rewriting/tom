package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;

public class Tools {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Collection.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/HashSet.tom }

  /**
   * encode: transform a string "f(a,X)" into its Term representation
   * e.g. `Appl("f",TermList(Appl("a",TermList()),Var("X")))
   * variables are in capitel letters
   * anti-pattern are introduced by anti(t)
   * alias are introduced by at(t1,t2)
   * rules are introduced by rule(lhs,rhs)
   * @param exp the string representation of the term to meta-encode
   * @return meta-encoding using Appl and TermList
   */
  public static Term encode(String stringterm) {
    //System.out.println("encode: " + stringterm);
    Term res = null;
    ATermFactory factory = SingletonFactory.getInstance();
    ATerm at = factory.parse(stringterm);
    res = encode(at);
    //System.out.println("encode: " + res);
    return res;
  }
  
  private static Term encode(ATerm at) {
    Term res = null;
    switch(at.getType()) {
	  	case ATerm.APPL:
	  		ATermAppl appl = (ATermAppl) at;
        String name = appl.getName();
        ATermList args = appl.getArguments();
        if(args.isEmpty() && isVariableName(name)) {
          res = `Var(name);
        } else if(name.equals("anti") && args.getLength()==1) {
          Term term = encode(args.getFirst());
          res = `Anti(term);
        } else if(name.equals("at") && args.getLength()==2) {
          Term var = encode(args.getFirst());
          Term term = encode(args.getNext().getFirst());
          res = `At(var,term);
        } else {
          res = `Appl(name,encodeList(args));
        }
       break; 
    }
    return res;
  }

  public static Rule encodeRule(String stringterm) {
    //System.out.println("encodeRule: " + stringterm);
    Rule res = null;
    ATermFactory factory = SingletonFactory.getInstance();
    ATerm at = factory.parse(stringterm);
    switch(at.getType()) {
	  	case ATerm.APPL:
	  		ATermAppl appl = (ATermAppl) at;
        String name = appl.getName();
        ATermList args = appl.getArguments();
        if(name.equals("rule") && args.getLength()==2) {
          Term lhs = encode(args.getFirst());
          Term rhs = encode(args.getNext().getFirst());
          res = `Rule(lhs,rhs);
        }
        break;
    }
    //System.out.println("encodeRule: " + res);
    return res;
  }

  private static boolean isVariableName(String name) {
    boolean res = true;
    for(int i=0 ; i<name.length(); i++) {
      res &= (Character.isUpperCase(name.charAt(i)) || Character.isDigit(name.charAt(i)));
    } 
    return res;
  }

  private static TermList encodeList(ATermList list) {
    if(list.isEmpty()) {
      return `TermList();
    } else {
      Term head = encode(list.getFirst());
      TermList tail = encodeList(list.getNext());
      return `TermList(head,tail*);
    }
  }

  /**
    * metaEncodeConsNil: transforms a Term representation into a generic term representation
    * for instance, Appl("f",TermList(Appl("b",TermList()))) is transformed into
    * the string "Appl(symb_f,Cons(Appl(symb_b,Nil()),Nil()))"
    * this string can be encoded into a Term, using the "encode" method
    */
  public static Term metaEncodeConsNil(Term t) {
    return encode(encodeConsNil(t));
  }

  private static String encodeConsNil(Term t) {
    %match(t) {
      Appl(symb,args) -> {
        return "Appl(symb_" + `symb + "," + encodeConsNil(`args) + ")";
      }

      Var(name) -> {
        return `name;
      }

      Anti(term) -> {
        //System.out.println("ENCODE ANTI: " + `term);
        return "anti(" + encodeConsNil(`term) + ")";
      }
    }
    return null;
  }

  private static String encodeConsNil(TermList t) {
    %match(t) {
      TermList(head,tail*) -> {
        return "Cons(" + encodeConsNil(`head) + "," + encodeConsNil(`tail) + ")";
      }
      TermList() -> {
        return "Nil()";
      }
    }
    return "null";
  }

  /*
   * go from
   * Appl("Appl",TermList(Appl("symb_f",TermList()),Appl("Cons",TermList(Appl("Appl",TermList(Appl("symb_a",TermList()),Appl("Nil",TermList()))),Appl("Nil",TermList())))))
    * to Appl("f",TermList(Appl("b",TermList())))
     *
     */
  public static Term decodeConsNil(Term t) {
    %match(t) {
      Appl("Appl",TermList(Appl(symb_name,TermList()),args)) -> {
        String name = `symb_name.substring("symb_".length());
        return `Appl(name,decodeConsNilList(args));
      }
      
    }
    // identity to not decode an already decoded term
    return t;
  }

  public static TermList decodeConsNilList(Term t) {
    %match(t) {
      Appl("Cons",TermList(head,tail)) -> {
        TermList newTail = decodeConsNilList(`tail);
        return `TermList(decodeConsNil(head), newTail*);
      }

      Appl("Nil",TermList()) -> {
        return `TermList();
      }

    }
    return null;
  }

}
