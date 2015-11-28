package sa;

import sa.rule.types.*;
import java.util.Set;
import java.util.HashSet;
import com.google.common.collect.HashBasedTable;

public class Signature {
  %include { rule/Rule.tom }

  // All literal strings and string-valued constant expressions are interned.
  public final static String TRUE = "True";
  public final static String FALSE = "False";
  public final static String AND = "and";
  public final static String BOTTOM = "Bottom";
  public final static String FAIL = "bottom";
  public final static String BOTTOM2 = "Bottom2";
  public final static String BOTTOMLIST = "BottomList";
  public final static String EQ = "eq";
  public final static String APPL = "Appl";
  public final static String CONS = "Cons";
  public final static String NIL = "Nil";
  
  public final static String ENCODE = "encode";
  public final static String DECODE = "decode";

  // Types
  public final static GomType TYPE_BOOLEAN = `GomType("Bool");
  public final static GomType TYPE_TERM = `GomType("Term");
  public final static GomType TYPE_METASYMBOL = `GomType("MetaSymbol");
  public final static GomType TYPE_METATERM = `GomType("MetaTerm");
  public final static GomType TYPE_METALIST = `GomType("MetaList");

  // R=Codomain x C=SymbolName -> V=Domain List of types
  private HashBasedTable<GomType,String,GomTypeList> table;

  // set of names that correspond to defined symbols (i.e. not constructors)
  private HashSet<String> functions;
  // set of names that correspond to internals symbols (i.e. bottom, meta-symbols)
  private HashSet<String> internals;

  public Signature() {
    this.table = HashBasedTable.create();
    this.functions = new HashSet<String>();
    this.internals = new HashSet<String>();
  }

  public Signature(Signature from) {
    this.table = HashBasedTable.create(from.table);
    this.functions = new HashSet<String>(from.functions);
    this.internals = new HashSet<String>(from.internals);
  }

  public boolean isBooleanOperator(String opname) {
    String s = opname.intern();
    return s == Signature.AND || s == Signature.TRUE || s == Signature.FALSE || s == Signature.EQ;
  }

  public boolean isBooleanOperatorExceptEQ(String opname) {
    String s = opname.intern();
    return s == Signature.AND || s == Signature.TRUE || s == Signature.FALSE;
  }

  public boolean isBooleanOperatorEQ(String opname) {
    String s = opname.intern();
    return s == Signature.EQ;
  }

  /**
   * Add the symbols defined for a given type in the corresponding
   * entry in the signature Map. 
   * @param program the AST of the program containing the signature
   */
  public void setSignature(Program program) {
    %match(program) {
      Program[productionList=ConcProduction(_*,SortType(codomain,ConcAlternative(_*,Alternative(name,args,codomain),_*)),_*),functionList=functionList] -> {
        GomTypeList domain = `ConcGomType();
        %match(args) {
          ConcField(_*,UnamedField(argType),_*) -> {
            domain = `ConcGomType(domain*,argType);
          }
        }
        addSymbol(`name,domain,`codomain);

        %match(functionList) {
          ConcProduction(_*,SortType(codomain2,ConcAlternative(_*,Alternative(name2,args2,codomain2),_*)),_*) -> {
            if(`name2 == `name && `codomain==`codomain2) {
              setFunction(`name);
            }
          }
        }
      }
    }
  }
  
  /**
   * Create an expanded signature containing the symbols in the
   * current one (set from a program) and builtin symbols used in the
   * translation of strategies. This signature is mono-sorted (TERM only)
   * @return the expanded signature
   */
  public Signature expandSignature()  {
    Signature expandedSignature = new Signature();
    // clone everything, but forget types (use TERM instead)
    for(GomType type: getCodomains()) {
      for(String symbol: getSymbols()) {
        GomTypeList domain = `ConcGomType();
        for(int i=0 ; i < getArity(symbol) ; i++) {
          domain = `ConcGomType(TYPE_TERM, domain*);
        }
        expandedSignature.addSymbol(symbol,domain,TYPE_TERM);
      }
    }

    if(Main.options.metalevel) {
      // add: Appl, Cons, Nil, BottomList
      expandedSignature.addSymbol(APPL,`ConcGomType(TYPE_METASYMBOL,TYPE_METALIST),TYPE_METATERM);
      expandedSignature.addSymbol(CONS,`ConcGomType(TYPE_METATERM,TYPE_METALIST),TYPE_METALIST);
      expandedSignature.addSymbol(NIL,`ConcGomType(),TYPE_METALIST);
      expandedSignature.addSymbol(BOTTOMLIST,`ConcGomType(TYPE_METALIST),TYPE_METALIST);
    }

    // add: True, False, and, eq
    expandedSignature.addSymbol(TRUE,`ConcGomType(),TYPE_BOOLEAN);
    expandedSignature.addSymbol(FALSE,`ConcGomType(),TYPE_BOOLEAN);
    expandedSignature.addFunctionSymbol(AND,`ConcGomType(TYPE_BOOLEAN,TYPE_BOOLEAN),TYPE_BOOLEAN);
    //     expandedSignature.addFunctionSymbol(EQ,`ConcGomType(TYPE_TERM,TYPE_TERM),TYPE_BOOLEAN);
    if(!Main.options.metalevel) {
      expandedSignature.addFunctionSymbol(EQ,`ConcGomType(TYPE_TERM,TYPE_TERM),TYPE_BOOLEAN);
    } else {
      expandedSignature.addFunctionSymbol(EQ,`ConcGomType(TYPE_METATERM,TYPE_METATERM),TYPE_BOOLEAN);
    }

    // add: bottom
    if(!Main.options.metalevel) {
      expandedSignature.addInternalSymbol(BOTTOM,`ConcGomType(TYPE_TERM),TYPE_TERM);
    } else {
      expandedSignature.addInternalSymbol(BOTTOM,`ConcGomType(TYPE_METATERM),TYPE_METATERM);
    }

    // for metalevel + Tom code
    if(Main.options.metalevel && Main.options.classname != null) {
      // add: encode, decode
      expandedSignature.addFunctionSymbol(ENCODE,`ConcGomType(TYPE_TERM),TYPE_METATERM);
      expandedSignature.addFunctionSymbol(DECODE,`ConcGomType(TYPE_METATERM),TYPE_TERM);
      // BOTTOM at the term level (at metalevel we already have Bottom)
      expandedSignature.addFunctionSymbol(FAIL,`ConcGomType(TYPE_TERM),TYPE_TERM);
    }
    return expandedSignature;
  }


  /** Get the arity of a symbol
   * @param symbol the name of the symbol
   * @return the arity of the symbol
   */
  public int getArity(String symbol) {
    GomTypeList domain = getDomain(symbol);
    if(domain != null) {
      return domain.length();
    }
    return -1;
  }

  /**
   * Add a symbol (with the corresponding profile).
   * @param name the name of the symbol
   * @param domain the types of its arguments
   * @param codomain the return type 
   */
  public void addSymbol(String name, GomTypeList domain, GomType codomain) {
    table.put(codomain, name.intern(), domain);
  }

  public void addFunctionSymbol(String name, GomTypeList domain, GomType codomain) {
    addSymbol(name, domain, codomain);
    setFunction(name);
  }

  public void addInternalSymbol(String name, GomTypeList domain, GomType codomain) {
    addSymbol(name, domain, codomain);
    setInternal(name);
  }

  public void setFunction(String name) {
    functions.add(name);
  }

  public boolean isFunction(String name) {
    return functions.contains(name);
  }

  public void setInternal(String name) {
    internals.add(name);
  }

  public boolean isInternal(String name) {
    return internals.contains(name);
  }

  /** Get the list of all codomains
   * @return the list of codomains in the table
   */
  public Set<GomType> getCodomains() {
    return table.rowKeySet();
  }

  /** 
   * Get the unique codomain for symbol
   * @return the codomain or null if symbol does not exist
   */
  public GomType getCodomain(String symbol) {
    for(GomType type: getCodomains()) {
      if(getDomain(type,symbol) != null) {
        return type;
      }
    }
    return null;
  }

  /** 
   * Get the unique domain for symbol
   * @return the domain or null if symbol does not exist
   */
  public GomTypeList getDomain(String symbol) {
    GomType codomain = getCodomain(symbol);
    if(codomain != null) {
      return getDomain(codomain, symbol);
    }
    return null;
  }

  /** 
   * Get the unique domain for symbol and a codomain
   * @return the domain or null if symbol does not exist
   */
  private GomTypeList getDomain(GomType codomain, String symbol) {
    return table.get(codomain,symbol);
  }

  /** Get the set of all symbols
   * @return the set of symbols in the signature
   */
  public Set<String> getSymbols() {
    return table.columnKeySet();
  }

  /** Get the set of symbols for a given codomain
   * @codomain the type of the codomain
   * @return the list of symbols in the type
   */
  public Set<String> getSymbols(GomType codomain) {
    return table.row(codomain).keySet();
  }
  
  /** Get the set of all constructors (symbols but not functions)
   * @return the set of constructors in the signature
   */
  public Set<String> getConstructors() {
    HashSet<String> res = new HashSet<String>();
    for(String e:getSymbols()) {
      if(!isFunction(e) && !isInternal(e)) {
        res.add(e);
      }
    }
    return res;
  }

  /** Get the set of constructors for a given codomain
   * @codomain the type of the codomain
   * @return the list of constructors in the type
   */
  public Set<String> getConstructors(GomType codomain) {
    HashSet<String> res = new HashSet<String>();
    for(String e:getSymbols(codomain)) {
      if(!isFunction(e) && !isInternal(e)) {
        res.add(e);
      }
    }
    return res;
  }

  public String toString() {
    return "" + this.table;
  }
}
