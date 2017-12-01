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

  public static boolean isGeneratedVariableName(String name) {
    return name.contains("_");
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
   * @returns operatorName or null if there is no "-operatorName"
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
  public static Term Anti(Term t) { 
    %match(t){ 
      At(var,term) -> {
        Term antiterm = Anti(`term);
        return `At(var,antiterm);         
      }
    }
    return `Anti(t); 
  }
  public static Term At(Term t1, Term t2) { return `At(t1,t2); }
  public static Term Bottom(Term t) { return _appl(Signature.BOTTOM,t); }
  public static Term Bottom2(Term t1,Term t2) { return _appl(Signature.BOTTOM2,t1,t2); }
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
    * metaVars: add a "var_" to all variables
    * used to have the same name for variable in the conditions generated when linearing a term
    */
  public static Term metaEncodeVars(Term t, Signature signature) {
    if(Main.options.metalevel) {
      return encodeVars(t,signature);
    } else {
      throw new RuntimeException("metaEncodeVars can only be used with meta-level active");
    }
  }
  
  private static Term encodeVars(Term t, Signature signature) {
    %match(t) {
      Appl(symb,args) -> {
        return `Appl(symb, encodeVars(args,signature));
      }

      Var(name) -> {
        return Var("var_"+`name);
      }

      Anti(term) -> {
        return Anti(encodeVars(`term,signature));
      }
    }
    return null;
  }

  private static TermList encodeVars(TermList t, Signature signature) {
    %match(t) {
      TermList(head,tail*) -> {
        Term he = encodeVars(`head,signature);
        TermList ta = encodeVars(`tail,signature);
        return `TermList(he,ta*);
      }
      TermList() -> {
        return `TermList();
      }
    }
    return null;
  }

  /**
    * metaEncodeConsNil: transforms a Term representation into a generic term representation
    * for instance, the term f(b()) (implemented by Appl("f",TermList(Appl("b",TermList()))))
    * is transformed into the term Appl(symb_f,Cons(Appl(symb_b,Nil()),Nil()))
    * implemented by Appl("Appl",TermList(Appl("symb_f",TermList()),Appl("Cons",TermList(Appl("Appl",TermList(Appl("symb_a",TermList()),Appl("Nil",TermList()))),Appl("Nil",TermList())))))
    */
  public static Term metaEncodeConsNil(Term t, Signature signature) {
    if(Main.options.metalevel) {
      return encodeConsNil(t,signature);
    } else {
      throw new RuntimeException("metaEncodeConsNil can only be used with meta-level active");
    }
  }
  
  private static Term encodeConsNil(Term t, Signature signature) {
    %match(t) {
      Appl(symb,args) -> {
        String symbName = "symb_" + `symb;
        signature.addSymbol(symbName,`ConcGomType(),Signature.TYPE_METASYMBOL);
        return Appl(_appl(symbName), encodeConsNil(`args,signature));
      }

      Var(name) -> {
        return Var("var_"+`name);
      }

      Anti(term) -> {
        return Anti(encodeConsNil(`term,signature));
      }

      At(v@Var[],t2) -> {
        return At(encodeConsNil(`v,signature),encodeConsNil(`t2,signature));
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
  public static Term metaDecodeConsNil(Term t) {
    if(Main.options.metalevel) {
      return decodeConsNil(t);
    } else {
      throw new RuntimeException("metaDecodeConsNil can only be used with meta-level active");
    }
  }

  private static Term decodeConsNil(Term t) {
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

  private static TermList decodeConsNilList(Term t) {
    %match(t) {
      Appl("Cons",TermList(head,tail)) -> {
        TermList newTail = decodeConsNilList(`tail);
        return `TermList(decodeConsNil(head), newTail*);
      }

      Appl("Nil",TermList()) -> {
        return `TermList();
      }
    }
    throw new RuntimeException("should not be there");
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
      Program[stratList=ConcStratDecl(_*,decl@StratDecl(n,_,_),_*)] -> {
        if(`n.equals(name)) {
          return `decl;
        }
      }
    }
    return null;
  }

  /**
   * Transform lhs into linear-lhs + true ^ constraint on non linear variables
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

    Term constraint = `Appl(Signature.TRUE,TermList());
    for(String name:mapToOldName.keySet()) {
      String oldName = mapToOldName.get(name);
      constraint = `Appl(Signature.AND,TermList( Appl(Signature.EQ,TermList(Var(oldName),Var(name))), constraint));
    }
    return `TermList(lhs,constraint);

  }

  /*
   * Replace a named variable by an underscore
   */
  %strategy RemoveVar() extends Identity() {
    visit Term {
      Var[] -> {
        return `Var("_");
      }

      At(_,t) -> {
        return `t;
      }
    }
  }

  public static tom.library.sl.Visitable removeVar(tom.library.sl.Visitable t) {
    try {
      return `TopDown(RemoveVar()).visitLight(t);
    } catch(VisitFailure e) {
      throw new RuntimeException("should not be there");
    }
  }
  
  %strategy RemoveAt() extends Identity() {
    visit Term {
      At(_,t) -> {
        return `t;
      }
    }
  }
  
  public static tom.library.sl.Visitable removeAt(tom.library.sl.Visitable t) {
    try {
      return `InnermostId(RemoveAt()).visitLight(t);
    } catch(VisitFailure e) {
      throw new RuntimeException("should not be there");
    }
  }


  
  public static boolean containsNamedVar(tom.library.sl.Visitable t) {
    HashMultiset<String> bag = collectVariableMultiplicity(t);
    for(String name:bag.elementSet()) {
      if(name != "_") {
        return true;
      }
    }

    return false;
  }

  public static boolean containsAt(tom.library.sl.Visitable t) {
    HashMultiset<Term> bag = HashMultiset.create();
    try {
      `TopDown(CollectAt(bag)).visitLight(t);
    } catch(VisitFailure e) {
    }
     return !bag.isEmpty();
  }
  
  // search all At symbols
  %strategy CollectAt(bag:HashMultiset) extends Identity() {
    visit Term {
      x@At[]-> {
        bag.add(`x);
      }
    }
  }



  public static boolean containsEqAnd(tom.library.sl.Visitable t) {
    try {
      `TopDown(ContainsEqAnd()).visitLight(t);
      return false;
    } catch(VisitFailure e) {
      return true;
    }
  }

  %strategy ContainsEqAnd() extends Identity() {
    visit Term {
      t@Appl("eq",_) -> {
        `Fail().visitLight(`t);
      }

      t@Appl("and",_) -> {
        `Fail().visitLight(`t);
      }
    }
  }


  /**
   * Returns a Map which associates to each variable name an integer
   * representing the number of occurences of the variable in the
   * (Visitable) Term
   */
  public static HashMultiset<String> collectVariableMultiplicity(tom.library.sl.Visitable subject) {
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
  %strategy CollectVars(bag:Collection) extends Identity() {
    visit Term {
      Var(name)-> {
        bag.add(`name);
      }
    }
  }

  // for Main.options.metalevel we need the (generated)signature 
  %strategy ReplaceWithFreshVar(signature:Signature, bag:HashMultiset, map:Map) extends Identity() {
    visit Term {
      Var(n)  -> {
        if(bag.count(`n) > 1) {
          bag.remove(`n);
          String z = Tools.getName("Z");
          map.put(z,`n);
          Term newt = `Var(z);
          // HC: why should we encode it?
//           if(Main.options.metalevel) {
//             newt = Tools.metaEncodeConsNil(newt,signature);
//           }
          return newt;
        }
      }
    }
  }

  public static RuleList fromListOfRule(List<Rule> l) {
    RuleList res = `ConcRule();
    for(Rule r:l) {
      res = `ConcRule(r,res*);
    }
    return res.reverse();
  }

  /*
   * rename variables into x1,...,xn (using a topdown traversal)
   */
  public static Term normalizeVariable(Term subject) {
    ArrayList<String> list = new ArrayList<String>();
    Map<String,String> mapToNewName = new HashMap<String,String>();
    int cpt = 0;
    try {
      `TopDown(CollectVars(list)).visitLight(subject);
      for(String name:list) {
        
        if(mapToNewName.get(name) == null) {
          String newName = "x_" + (cpt++);
          mapToNewName.put(name,newName);
        }
      }
      subject = `TopDown(RenameVars(mapToNewName)).visitLight(subject);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }
    return subject;
  }
  
  %strategy RenameVars(map:Map) extends Identity() {
    visit Term {
      Var(n)  -> {
        if(isGeneratedVariableName(`n)) {
          String newName = (String) map.get(`n);
          if(newName != null) {
            return `Var(newName);
          }
        }
      }
    }
  }

}
