package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;
import com.google.common.collect.HashMultiset;

public class Tools {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { aterm.tom }
  %include { java/util/types/Collection.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/HashSet.tom }
  %include { java/util/types/Set.tom }

  %typeterm Signature { implement { Signature }}
  %typeterm HashMultiset { implement { HashMultiset }}

  private static final String AUX = "Aux";   // extension for auxiliary symbols
  private static int phiNumber = 0;          // sequence number for symbol (names)

  /*** helpers to build and decompose symbol names ***/
  
  /**
   * Builds a unique symbol (name)
   */
  public static String getName(String name) {
    return name + "_" + (phiNumber++);
  }

  /**
   * Given symbolName and operatorName
   * returns symbolName-operatorName
   */
  public static String addOperatorName(String symbolName, String operatorName) {
    return symbolName + "-" + operatorName;
  }

  /**
   * given symbolName and typeName
   * returns symbolName_typeName
   */
  public static String addTypeName(String symbol, String typeName) {
    return symbol + "_" + typeName;
  }

  /**
   * Given symbolName
   * returns symbolNameAUX
   */
  public static String addAuxExtension(String symbol) {
    return symbol + AUX;
  }

  /**
   * A symbol is of the form:
   * - symbolName[AUX]
   * - symbolName[AUX][_<number>]
   * - symbolName[AUX][_<number>]_typeName
   * - symbolName[AUX][_<number>]-operatorName[_<number>]_typeName
   * @returns symbolName[AUX]
   */
  public static String getSymbolName(String symbol) {
    int last = symbol.indexOf('_');
    if(last == -1) {
      return symbol; //last=symbol.length();
    }
    return symbol.substring(0,last);
  }

  /**
   * Determines if auxiliary symbol
   * @returns true if of the form symbolNameAUX; false otherwise
   */
  public static boolean isSymbolNameAux(String symbol) {
    //boolean res = false;
    //String name = getSymbolName(symbol);
    //if(name.length() > AUX.length()) {
    //  res = name.substring(name.length()-AUX.length(),name.length()).equals(AUX);
    //}
    //return res;
    return getSymbolName(symbol).endsWith(AUX);
  }

  /**
   * Retuns the name of the strategy it has been generated for (strips of the AUX)
   * @returns symbolName
   */
  public static String getSymbolNameMain(String symbol) {
    String name = getSymbolName(symbol);
    if(isSymbolNameAux(symbol)) {
      name = name.substring(0,name.length()-AUX.length());
    }
    return name;
  }

  /**
   * A symbol is expected to be of the form:
   * - symbolName[AUX][_<number>]_typeName
   * - symbolName[AUX][_<number>]-operatorName[_<number>]_typeName
   * @returns symbolName[AUX]
   */
  public static String getTypeName(String symbol) {
    int last = symbol.lastIndexOf('_');
    if(last == -1) { // nothing if type not specified in the symbol name 
      return ""; 
    }
    return symbol.substring(last+1,symbol.length());
  }

  /**
   * Given a symbol name of the form 
   * - symbolName[AUX][_<number>]-operatorName[_<number>][_typeName]
   * @returns operatorName
   */
  public static String getOperatorName(String symbol) {
    String aux = null;
    int last = symbol.indexOf('-');
    if(last != -1) { // if containing a composite
      int funLast = symbol.indexOf('_',last+1);
      if(funLast == -1) { //  if no other information after composite
        funLast = symbol.length();
      }
      aux = symbol.substring(last+1,funLast);
    }
    return aux;
  }

  private static boolean isVariableName(String name, Signature signature) {
    return signature.getCodomain(name) == null;
  }


  /*** helpers to build AST ***/
  public static Term Var(String name) { return `Var(name); }
  public static Term Anti(Term t) { return `Anti(t); }
  public static Term At(Term t1, Term t2) { return `At(t1,t2); }
  public static Term Bottom(Term t) { return _appl(Signature.BOTTOM,t); }
  public static Term Bottom2(Term t1,Term t2) { return _appl(Signature.BOTTOM,t1,t2); }
  public static Term BottomList(Term t) { return _appl(Signature.BOTTOMLIST,t); }
  public static Term True() { return _appl(Signature.TRUE); }
  public static Term False() { return _appl(Signature.FALSE); }
  public static Term And(Term t1, Term t2) { return _appl(Signature.AND,t1,t2); }
  public static Term Eq(Term t1, Term t2) { return _appl(Signature.EQ,t1,t2); }
  public static Term Appl(Term t1, Term t2) { return _appl(Signature.APPL,t1,t2); }
  public static Rule Rule(Term lhs, Term rhs) { return `Rule(lhs,rhs); }
  public static Term Nil() { return _appl(Signature.NIL); }
  public static Term Cons(Term t1, Term t2) { return _appl(Signature.CONS,t1,t2); }
  public static Term _appl(String name, Term... args) {
    TermList tl = `TermList();
    for(Term t:args) {
      tl = `TermList(tl*,t);
    }
    return `Appl(name,tl);
  }

  /**
    * metaEncodeConsNil: transforms a Term representation into a generic term representation
    * for instance, the term f(b()) (implemented by Appl("f",TermList(Appl("b",TermList()))))
    * is transformed into the term Appl(symb_f,Cons(Appl(symb_b,Nil()),Nil()))
    * implemented by Appl("Appl",TermList(Appl("symb_f",TermList()),Appl("Cons",TermList(Appl("Appl",TermList(Appl("symb_a",TermList()),Appl("Nil",TermList()))),Appl("Nil",TermList())))))
    */
  public static Term metaEncodeConsNil(Term t, Signature signature) {
    //return encode(encodeConsNil(t,signature),signature);
    return encodeConsNil(t,signature);
  }
  
  private static Term encodeConsNil(Term t, Signature signature) {
    %match(t) {
      Appl(symb,args) -> {
        String symbName = "symb_" + `symb;
        if(!Main.options.metalevel) {
          signature.addSymbol(symbName,`ConcGomType(),Signature.TYPE_TERM);
        } else {
          signature.addSymbol(symbName,`ConcGomType(),Signature.TYPE_METASYMBOL);
        }
        //return "Appl(" + symbName + "," + encodeConsNil(`args,signature) + ")";
        return Appl(_appl(symbName), encodeConsNil(`args,signature));
      }

      Var(name) -> {
        //return "var_" + `name;
        return Var("var_"+`name);
      }

      Anti(term) -> {
        //System.out.println("ENCODE ANTI: " + `term);
        //return "anti(" + encodeConsNil(`term,signature) + ")";
        return Anti(encodeConsNil(`term,signature));
      }
    }
    return null;
  }

  private static Term encodeConsNil(TermList t, Signature signature) {
    %match(t) {
      TermList(head,tail*) -> {
        return Cons(encodeConsNil(`head,signature),encodeConsNil(`tail,signature));
      }
      TermList() -> {
        return Nil();
      }
    }
    return null;
  }

  /*
   * go from meta-level to term level
   * Appl("Appl",TermList(Appl("symb_f",TermList()),Appl("Cons",TermList(Appl("Appl",TermList(Appl("symb_a",TermList()),Appl("Nil",TermList()))),Appl("Nil",TermList())))))
    * is decoded to Appl("f",TermList(Appl("b",TermList())))
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

  /*
   * generate a term for the form f(Z1,...,Zn)
   * @param name the symbol name 
   * @param arity the arity of the symbol
   * @return the Term that represents the term
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
   * tools for manipulating Program
   */
  public static StratDecl getStratDecl(String name, Program program) {
    %match(program) {
      Program(_,ConcStratDecl(_*,decl@StratDecl(n,_,_),_*)) -> {
        if(`n.equals(name)) {
          return `decl;
        }
      }
    }
    return null;
  }

  /**
   * Transform lhs into linear-lhs + true ^ constraint on non linear variables
   * TODO: not really related to the Compiler but more to the Tools (for Terms)
   */
  public static TermList linearize(Term lhs, Signature signature) {
    Map<String,String> mapToOldName = new HashMap<String,String>();
    HashMultiset<String> bag = collectVariableMultiplicity(lhs);

    Set<String> elements = new HashSet<String>(bag.elementSet());

    for(String name:elements) {
      if(bag.count(name) == 1) {
        bag.remove(name);
      }
    }

    try {
      lhs = `TopDown(ReplaceWithFreshVar(signature,bag,mapToOldName)).visitLight(lhs);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }

    Term constraint = `Appl("True",TermList());
    for(String name:mapToOldName.keySet()) {
      String oldName = mapToOldName.get(name);
      constraint = `Appl("and",TermList( Appl(Signature.EQ,TermList(Var(oldName),Var(name))), constraint));
    }
    return `TermList(lhs,constraint);

  }

  public static boolean isLinear(Term t) {
    HashMultiset<String> bag = collectVariableMultiplicity(t);
    for(String name:bag.elementSet()) {
      if(bag.count(name) > 1) {
        return false;
      }
    }
    return true;
  }
  
  // for Main.options.metalevel we need the (generated)signature 
  //   -> in previous versions it was one of the parameters
  // TODO: use HashMultiset
  %strategy ReplaceWithFreshVar(signature:Signature, bag:HashMultiset, map:Map) extends Identity() {
    visit Term {
      Var(n)  -> {
        if(bag.count(`n) > 1) {
          bag.remove(`n);
          String z = Tools.getName("Z");
          map.put(z,`n);
          Term newt = `Var(z);
          if(Main.options.metalevel) {
            newt = Tools.metaEncodeConsNil(newt,signature);
          }
          return newt;
        }
      }
    }
  }

  /**
   * Returns a Map which associates to each variable name an integer
   * representing the number of occurences of the variable in the
   * (Visitable) Term
   */
  private static HashMultiset<String> collectVariableMultiplicity(tom.library.sl.Visitable subject) {
    // collect variables
    HashMultiset<String> bag = HashMultiset.create();
    try {
      `TopDown(CollectVars(bag)).visitLight(subject);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }

    return bag;
  }

  // search all Var and store their values
  %strategy CollectVars(bag:HashMultiset) extends Identity() {
    visit Term {
      Var(name)-> {
        bag.add(`name);
      }
    }
  }
}
