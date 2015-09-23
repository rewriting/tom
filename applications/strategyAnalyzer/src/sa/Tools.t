package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;

public class Tools {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { aterm.tom }
  %include { java/util/types/Collection.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/HashSet.tom }
  %include { java/util/types/Set.tom }


  private static int phiNumber = 0;
  public static String getName(String name) {
    return name + "_" + (phiNumber++);
  }

  public static StrategyOperator getOperator(String symbol) {
//   public static String getOperator(String symbol) {
    int last = symbol.indexOf('_');
    if(last==-1){ last=symbol.length();}
//     return symbol.substring(0,last);
    return StrategyOperator.getStrategyOperator(symbol.substring(0,last));
  }

  public static String getTypeOfSymbol(String symbol) {
    int last = symbol.lastIndexOf('_');
    if(last==-1){ return Signature.BOOLEAN;} // TODO: change it when and becomes and_Bool
    return symbol.substring(last+1,symbol.length());
  }

//   public static boolean isAuxiliary(String symbol) {
//     int last = symbol.indexOf('_');
//     if(last==-1){ last=symbol.length();}
//     if(symbol.contains(Compiler.AUX) &&  symbol.substring(last-Compiler.AUX.length(), last).equals(Compiler.AUX)){
//           return true;
//     }
//     return false;
//   }
  
  public static String getCompositeName(String opName, String aux) {
    //     return opName+"_"+aux;
    return opName+"-"+aux;
  }

  public static String getComposite(String symbol) {
    String aux=null;
    int last = symbol.indexOf('-');
    if(last != -1){ // if containing a composite
      int  funLast = symbol.indexOf('_',last+1);
      if(funLast == -1){ //  if no other information after composite
        funLast = symbol.length();
      }
      aux = symbol.substring(last+1,funLast);
    }
    return aux;
  }

//   public static String getComposite(String symbol) {
//     String aux=null;
//     int last = symbol.indexOf('_');
//     if(last != -1){ // if generated symbol containing a seq number 
//       int numberLast = symbol.indexOf('_',last+1);
//       if(numberLast != -1){ // if composed
//         int funLast = symbol.indexOf('_',numberLast+1);
//         if(funLast == -1){ // if no extra symbols after
//           funLast = symbol.length();
//         }
//         aux = symbol.substring(numberLast+1,funLast);
//       }
//     }
//     return aux;
//   }

  public static String typeSymbol(String symbol, String type) {
    return symbol+"_"+type;
  }


  private static Term _appl(String name, Term... args) {
    TermList tl = `TermList();
    for(Term t:args) {
      tl = `TermList(tl*,t);
    }
    return `Appl(name,tl);
  }

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
  /*
   * REMOVE */
   public static Term encode(String stringterm, Signature signature) {
    //System.out.println("encode: " + stringterm);
    Term res = null;
    ATermFactory factory = SingletonFactory.getInstance();
    ATerm at = factory.parse(stringterm);
    res = encode(at,signature);
    //System.out.println("encode: " + res);
    return res;
  }
  
  private static Term encode(ATerm at, Signature signature) {
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

  private static TermList encodeList(ATermList list, Signature signature) {
    if(list.isEmpty()) {
      return `TermList();
    } else {
      Term head = encode(list.getFirst(),signature);
      TermList tail = encodeList(list.getNext(),signature);
      return `TermList(head,tail*);
    }
  }

  public static RuleList encodeRuleList(String stringterm, Signature signature) {
    System.out.println("encodeRuleList: " + stringterm);
    RuleList res = `RuleList();
    ATermFactory factory = SingletonFactory.getInstance();
    ATerm at = factory.parse(stringterm);
    %match(at) {
      ATermList(_*,ATermAppl(AFun("rule",2,_),concATerm(atlhs,atrhs)), _*) -> {
        Term lhs = encode(`atlhs,signature);
        Term rhs = encode(`atrhs,signature);
        res = `RuleList(res*, Rule(lhs,rhs));
      }
    }
    System.out.println("encodeRuleList: " + res);
    return res;
  }

  public static Rule encodeRule(String stringterm, Signature signature) {
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
  /* REMOVE */

  private static boolean isVariableName(String name, Signature signature) {
    return signature.getCodomain(name) == null;

    /*
    boolean res = true;
    for(int i=0 ; i<name.length(); i++) {
      res &= (Character.isUpperCase(name.charAt(i)) || Character.isDigit(name.charAt(i)) || name.charAt(i)=='_');
    }
    return name.startsWith("var_") || res;
    */

  }



  /**
    * metaEncodeConsNil: transforms a Term representation into a generic term representation
    * for instance, Appl("f",TermList(Appl("b",TermList()))) is transformed into
    * the string "Appl(symb_f,Cons(Appl(symb_b,Nil()),Nil()))"
    * this string can be encoded into a Term, using the "encode" method
    */
  /*   REMOVE */ 
  public static Term metaEncodeConsNil(Term t, Signature signature) {
    return encode(encodeConsNil(t,signature),signature);
  }
  /*  REMOVE */

  private static String encodeConsNil(Term t, Signature signature) {
    %match(t) {
      Appl(symb,args) -> {
        String symbName = "symb_" + `symb;
        if(!Main.options.metalevel) {
          signature.addSymbol(symbName,new ArrayList<String>(),Signature.TERM);
        } else {
          signature.addSymbol(symbName,new ArrayList<String>(),Signature.METASYMBOL);
        }
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

  private static String encodeConsNil(TermList t, Signature signature) {
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

    //System.out.println("IN DECODE = "+ `t);
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
     //System.out.println("IN DECODE LIST = "+ `t);
    %match(t) {
      Appl("Cons",TermList(head,tail)) -> {
         //System.out.println("HEAD = "+ `head);
         //System.out.println("TAIL = "+ `tail);
        TermList newTail = decodeConsNilList(`tail);
        return `TermList(decodeConsNil(head), newTail*);
      }

      Appl("Nil",TermList()) -> {
         //System.out.println("NIL");
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



  // TODO: doesn't take into account non-linear patterns
  // f(x,x) matches f(x,y) because name variables are ignored
  private  boolean matchModuloAt(Term pattern, Term subject) {
    try {
      Term p = `TopDown(RemoveAtAndRenameVariables()).visitLight(pattern);
      Term s = `TopDown(RemoveAtAndRenameVariables()).visitLight(subject);
      //System.out.println(pattern + " <--> " + subject + " ==> " + (p==s));
      return p==s;
    } catch(VisitFailure e) {}
    throw new RuntimeException("should not be there");
  }
  /*
   * put terms in normal form to detect redundant patterns
   */
  %strategy RemoveAtAndRenameVariables() extends Identity() {
    visit Term {
      At(_,t2)  -> { return `t2; }
      Var(_)  -> { return `Var("_"); }
    }
  }


  /*
   * generate a term for the form f(Z1,...,Zn)
   * @param name the symbol name 
   * @param arity the arity of the symbol
   * @return the string that represents the term
   */
  public static Term genAbstractTerm(String name, int arity, String varname) {
    TermList args = `TermList();
    for(int i=arity ; i>= 1 ; i--) {
      Term var = `Var(varname+"_"+i);
      args = `TermList(var, args*);
    }
    return `Appl(name, args);
  }
 
  /*
   * REMOVE */ 
  public static String genStringAbstractTerm(String name, int arity, String varname) {
    if(arity==0) {
      return name + "()";
    } else {
      String args = varname+"_"+"1";
      for(int i=2 ; i<=arity ; i++) {
        args += ", " + varname+"_"+i;
      }
      return name + "(" + args + ")";
    }
  }
  /*    REMOVE */

  /*
   * tools for manipulating Program
   */
  public static StratDecl getStratDecl(String name, Program program) {
    %match(program) {
      Program(_, ConcStratDecl(_*,decl@StratDecl(n, args,body),_*)) -> {
        if(`n.equals(name)) {
          return `decl;
        }
      }
    }
    return null;
  }

  public static List<String> gomTypeListToStringList(GomTypeList argTypes) {
    List<String> res = new ArrayList<String>();
    while(!argTypes.isEmptyConcGomType()) {
      String name = argTypes.getHeadConcGomType().getName();
      res.add(name);
      argTypes = argTypes.getTailConcGomType();
    }
    return res;
  }

//   public static StrategyOperator getRuleOperator(Rule rule) {
//     StrategyOperator op = StrategyOperator.IDENTITY;
//     %match(rule){
//       Rule(Appl(symbol,args),_) ->{
//         String opSymb = `symbol;
//         op = Tools.getOperator(opSymb);
//       }
//     }
//     return op;
//   }


//   public static String getArgumentSymbol(Rule rule) {
//     String funSymb = null;
//     %match(rule){
//       Rule(Appl(symbol,TermList(Appl(fun,args),_*)),_) ->{
//         funSymb = `fun;
//       }
//     }
//     return funSymb;
//   }


}
