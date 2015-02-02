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
  %include { java/util/types/Set.tom }

  /**
   * encode: transforms a string "f(a,X)" into its Term representation
   * e.g. `Appl("f",TermList(Appl("a",TermList()),Var("X")))
   * variables are in capital letters
   * anti-pattern are introduced by anti(t)
   * alias are introduced by at(t1,t2)
   * rules are introduced by rule(lhs,rhs)
   * @param exp the string representation of the term to meta-encode
   * @return meta-encoding using Appl and TermList
   */
  public static Term encode(String stringterm, Map<String,Integer> signature) {
    //System.out.println("encode: " + stringterm);
    Term res = null;
    ATermFactory factory = SingletonFactory.getInstance();
    ATerm at = factory.parse(stringterm);
    res = encode(at,signature);
    //System.out.println("encode: " + res);
    return res;
  }
  
  private static Term encode(ATerm at, Map<String,Integer> signature) {
    Term res = null;
    switch(at.getType()) {
	  	case ATerm.APPL:
	  		ATermAppl appl = (ATermAppl) at;
        String name = appl.getName();
        ATermList args = appl.getArguments();
        if(args.isEmpty() && isVariableName(name,signature)) {
          res = `Var(name);
        } else if(name.equals("anti") && args.getLength()==1) {
          Term term = encode(args.getFirst(),signature);
          res = `Anti(term);
        } else if(name.equals("at") && args.getLength()==2) {
          Term var = encode(args.getFirst(),signature);
          Term term = encode(args.getNext().getFirst(),signature);
          res = `At(var,term);
        } else {
          res = `Appl(name,encodeList(args,signature));
        }
       break; 
    }
    return res;
  }

  public static Rule encodeRule(String stringterm, Map<String,Integer> signature) {
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
          Term lhs = encode(args.getFirst(),signature);
          Term rhs = encode(args.getNext().getFirst(),signature);
          res = `Rule(lhs,rhs);
        }
        break;
    }
    // System.out.println("encodeRule: " + res);
    return res;
  }

  private static boolean isVariableName(String name, Map<String,Integer> signature) {
    return !signature.keySet().contains(name);

    /*
    boolean res = true;
    for(int i=0 ; i<name.length(); i++) {
      res &= (Character.isUpperCase(name.charAt(i)) || Character.isDigit(name.charAt(i)) || name.charAt(i)=='_');
    }
    return name.startsWith("var_") || res;
    */

  }

  private static TermList encodeList(ATermList list, Map<String,Integer> signature) {
    if(list.isEmpty()) {
      return `TermList();
    } else {
      Term head = encode(list.getFirst(),signature);
      TermList tail = encodeList(list.getNext(),signature);
      return `TermList(head,tail*);
    }
  }

  /**
    * metaEncodeConsNil: transforms a Term representation into a generic term representation
    * for instance, Appl("f",TermList(Appl("b",TermList()))) is transformed into
    * the string "Appl(symb_f,Cons(Appl(symb_b,Nil()),Nil()))"
    * this string can be encoded into a Term, using the "encode" method
    */
  public static Term metaEncodeConsNil(Term t, Map<String,Integer> signature) {
    return encode(encodeConsNil(t,signature),signature);
  }

  private static String encodeConsNil(Term t, Map<String,Integer> signature) {
    %match(t) {
      Appl(symb,args) -> {
        String symbName = "symb_" + `symb;
        signature.put(symbName,0);
        return "Appl(" + symbName + "," + encodeConsNil(`args,signature) + ")";
      }

      Var(name) -> {
        return "var_" + `name;
      }

      Anti(term) -> {
        //System.out.println("ENCODE ANTI: " + `term);
        return "anti(" + encodeConsNil(`term,signature) + ")";
      }
    }
    return null;
  }

  private static String encodeConsNil(TermList t, Map<String,Integer> signature) {
    %match(t) {
      TermList(head,tail*) -> {
        return "Cons(" + encodeConsNil(`head,signature) + "," + encodeConsNil(`tail,signature) + ")";
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

//     System.out.println("IN DECODE = "+ `t);
    %match(t) {
      Appl("Appl",TermList(Appl(symb_name,TermList()),args)) -> {
          String name = `symb_name.substring("symb_".length());
          return `Appl(name,decodeConsNilList(args));
      }

      Var(symb_name) -> {
        String name = `symb_name.substring("var_".length());
        return `Var(name);
      }
    }
    // identity to not decode an already decoded term
    return t;
  }

  public static TermList decodeConsNilList(Term t) {
    %match(t) {
      Appl("Cons",TermList(head,tail)) -> {
//         System.out.println("HEAD = "+ `head);
//         System.out.println("TAIL = "+ `tail);
        TermList newTail = decodeConsNilList(`tail);
        return `TermList(decodeConsNil(head), newTail*);
      }

      Appl("Nil",TermList()) -> {
        return `TermList();
      }

    }
    return null;
  }

  public static TermList generalDecodeConsNilList(TermList t) {
    %match(t) {
      TermList(head,tail*) -> { 
        TermList newTail = generalDecodeConsNilList(`tail);
        return `TermList(generalDecodeConsNil(head),newTail*);
      }
    }
    return t; // liste vide
  }

  /** 
   * Decodes (the) encoded (part of) terms
   * The term can contain Anti and At - only the encoded parts are decoded
   */
  public static Term generalDecodeConsNil(Term t) {
    %match(t) {
      Appl("Appl",TermList(Appl(symb_name,TermList()),args)) -> {
          String name = `symb_name.substring("symb_".length());
          return `Appl(name,generalDecodeConsNilList(args));
      }
      Appl(rule_name,args) -> {
          return `Appl(rule_name,generalDecodeConsNilList(args));
      }
      Var(symb_name) -> {
        String name = `symb_name.startsWith("var_")?`symb_name.substring("var_".length()):`symb_name;
        return `Var(name);
      }
      At(var_name,term) -> {
          return `At(var_name,generalDecodeConsNil(term));
      }
      Anti(term) -> {
          return `Anti(generalDecodeConsNil(term));
      }      
    }
    // never
    return t;
  }

  public static TermList generalDecodeConsNilList(Term t) {
    %match(t) {
      Appl("Cons",TermList(head,tail)) -> {
        TermList newTail = generalDecodeConsNilList(`tail);
        return `TermList(generalDecodeConsNil(head), newTail*);
      }

      Appl("Nil",TermList()) -> {
        return `TermList();
      }

    }
    // never
    return `TermList();
  }


  /**
    * generalMetaEncodeConsNil: transforms a Term representation into a generic term representation
    * - the term can contain Anti and At - only the terms in the original signature are encoded
    */
/*
  public static Term generalMetaEncodeConsNil(Term t, Set<String> extractedSignature, Map<String,Integer> signature) {
    %match(t) {
      Appl(symb,args) -> {
        if(extractedSignature.contains(`symb)){
          return encode("Appl(symb_" + `symb + "," + encodeConsNil(`args,signature) + ")",signature);
        } else {
          return `Appl(symb,generalMetaEncodeConsNil(args,extractedSignature,signature));
        }
      }
      // could be default
      Var(name) -> {
        return `Var(name);
      }
      Anti(term) -> {
        return `Anti(generalMetaEncodeConsNil(term,extractedSignature,signature));
      }
      At(t1,t2) -> {
        return `At(generalMetaEncodeConsNil(t1,extractedSignature,signature),generalMetaEncodeConsNil(t2,extractedSignature,signature));
      }
    }
    return t;
  }

  private static TermList generalMetaEncodeConsNil(TermList t, Set<String> extractedSignature, Map<String,Integer> signature) {
    %match(t) {
      TermList(head,tail*) -> {
        Term headEnc = generalMetaEncodeConsNil(`head,extractedSignature,signature);
        TermList tailEnc = generalMetaEncodeConsNil(`tail,extractedSignature,signature);
        return `TermList(headEnc,tailEnc*);
      }
      // could be default
      TermList() -> {
        return `TermList();
      }
    }
    return t;
  }
*/




}
