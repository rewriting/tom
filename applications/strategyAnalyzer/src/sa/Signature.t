package sa;

import sa.rule.types.*;
import java.util.*;

public class Signature {
  %include { rule/Rule.tom }

  // All literal strings and string-valued constant expressions are interned.
  public static String BOOLEAN = "Bool";
  public static String TRUE = "True";
  public static String FALSE = "False";
  public static String AND = "and";
  public static String BOTTOM = "Bottom";
  public static String BOTTOMLIST = "BottomList";
  public static String EQ = "eq";
  public static String APPL = "Appl";
  public static String CONS = "Cons";
  public static String NIL = "Nil";
  
  public static String ENCODE = "encode";
  public static String DECODE = "decode";

  // Types
  public static final String DUMMY = "Dummy";
  public static final String METASYMBOL = "MetaSymbol";
  public static final String METATERM = "MetaTerm";
  public static final String METALIST = "MetaList";


  // Codomain Type -> (SymbolName -> Domain List of types)
  private Map<GomType,Map<String,List<GomType>>> signature;

  public Signature() {
    this.signature = new HashMap<GomType,Map<String,List<GomType>>>(); 
  }


  /**
   * Add the symbols defined for a given type in the corresponding
   * entry in the signature Map. 
   * @param program the AST of the program containing the signature
   */
  public void setSignature(Program program) {
    %match(program) {
      Program(ConcProduction(_*,SortType(type,symb),_*),_) -> {
        Map<String,List<GomType>> symbols = new HashMap<String,List<GomType>>(); 
        %match(symb) {
          ConcAlternative(_*,Alternative(name,args,codomain),_*) -> {
            if(`codomain != `type) {
              throw new TypeMismatchException();
            }
            List<GomType> arguments = new ArrayList<GomType>(); 
            %match(args) {
              ConcField(_*,UnamedField(argType),_*) -> {
                arguments.add(`argType);
              }
            }
            symbols.put(`name,arguments);
          }
        }
        signature.put(`type,symbols);
      }
    }
  }

  /**
   * Create an expanded signature containing the symbols in the
   * current one (set from a program) and builtin symbols used in the
   * translation of strategies
   * @return the expanded signature
   */
  public Signature expandSignature()  {
    Signature expandedSignature = new Signature();
    // clone everything (lists could be just copied since normally not modified later on)
    for(GomType type: this.signature.keySet()) {
        Map<String,List<GomType>> symbols = new HashMap<String,List<GomType>>(); 
        for(String symbol: signature.get(type).keySet()) {
          List<GomType> args = new ArrayList<GomType>(signature.get(type).get(symbol));
          symbols.put(symbol.intern(),args);
        }
        expandedSignature.signature.put(type,symbols);
    }

    if(Main.options.metalevel) {
      // add: Appl, Cons, Nil, BottomList
      expandedSignature.addSymbol(APPL,Arrays.asList(METASYMBOL,METALIST),METATERM);
      expandedSignature.addSymbol(CONS,Arrays.asList(METATERM,METALIST),METALIST);
      expandedSignature.addSymbol(NIL,new ArrayList<String>(),METALIST);
      expandedSignature.addSymbol(BOTTOMLIST,Arrays.asList(METALIST),METALIST);
    }

    // add: True, False, and, eq
    expandedSignature.addSymbol(TRUE,new ArrayList<String>(),BOOLEAN);
    expandedSignature.addSymbol(FALSE,new ArrayList<String>(),BOOLEAN);
    expandedSignature.addSymbol(AND,Arrays.asList(BOOLEAN,BOOLEAN),BOOLEAN);
    String eq_boolean = this.disambiguateSymbol(EQ, Arrays.asList(BOOLEAN,BOOLEAN));
    expandedSignature.addSymbol(eq_boolean,Arrays.asList(BOOLEAN,BOOLEAN),BOOLEAN);

    // add: bottom(T), eq_T(T,T)
    /*
    for(GomType type: this.signature.keySet()) {
      String t = type.getName();
      expandedSignature.addSymbol(BOTTOM,Arrays.asList(t),t);
      String eq_t = this.disambiguateSymbol(EQ, Arrays.asList(t,t));
      expandedSignature.addSymbol(eq_t,Arrays.asList(t,t),BOOLEAN);
    }
    */
    // add: bottom(DUMMY), eq(DUMMY,DUMMY)
    if(!Main.options.metalevel) {
      expandedSignature.addSymbol(BOTTOM,Arrays.asList(DUMMY),DUMMY);
      String eq_t = this.disambiguateSymbol(EQ, Arrays.asList(DUMMY,DUMMY));
      expandedSignature.addSymbol(eq_t,Arrays.asList(DUMMY,DUMMY),BOOLEAN);
    } else {
      expandedSignature.addSymbol(BOTTOM,Arrays.asList(METATERM),METATERM);
      String eq_t = this.disambiguateSymbol(EQ, Arrays.asList(METATERM,METATERM));
      expandedSignature.addSymbol(eq_t,Arrays.asList(METATERM,METATERM),BOOLEAN);
    }

    // for metalevel + Tom code
    if(Main.options.metalevel && Main.options.classname != null) {
      // add: encode, decode
      expandedSignature.addSymbol(ENCODE,Arrays.asList(DUMMY),METATERM);
      expandedSignature.addSymbol(DECODE,Arrays.asList(METATERM),DUMMY);
    }
    return expandedSignature;
  }

  /**
   * Generate a new name for potentially overloaded symbols; use the
   * types of its arguments to disambiguate.
   * @param name the name of the symbol
   * @param argTypes the types of its arguments
   * @return the new name of the form name_T1_T2_..._Tn
   */
  public String disambiguateSymbol(String name, List<String> argTypes) {
    String res = name;
    // Just for TESTING
    // TODO: generate the correct eq_X_X in the rules (for the moment only "eq" generate)
    //for(String argType:argTypes) {
    //  res += "_" + argType;
    //}
    return res.intern();
  }


  /**
   * Add a symbol (with the corresponding profile).
   * @param name the name of the symbol
   * @param argTypes the types of its arguments
   * @param codomain the return type 
   */
  public void addSymbol(String name, List<String> argTypes, String codomain) {
    Map<String,List<GomType>> symbols = this.signature.get(`GomType(codomain));
    // if type of codomain doesn't exist then create it
    if(symbols==null) {
      symbols = new HashMap<String,List<GomType>>();
    }
    // create arguments' types list
    List<GomType> args = new ArrayList<GomType>();
    for(String argType:argTypes) {
      args.add(`GomType(argType));
    }
    // detect overloading
    List<GomType> oldDomain = symbols.put(name.intern(),args);
    if(oldDomain != null) {
      //System.out.println("redefinition: '" + name +"'" + oldDomain + " becomes '" + name + "'" + args);
      System.out.println(%[redefinition: '@name@'@oldDomain@ becomes '@name@'@args@]%);
      //       throw new TypeMismatchException();
    }
    signature.put(`GomType(codomain),symbols); 
  }

  /** Get codomain for symbol
   */
  public String getCodomain(String symbol) {
    for(GomType type: this.signature.keySet()) {
      if(this.signature.get(type).get(symbol)!=null) {
        return type.getName();
      }
    }
    return null;
  }

  /** Get the list of types of its arguments
   * @param symbol the name of the symbol
   * @return the list of types of its arguments
   */
  public List<String> getProfile(String symbol) {
    List<String> res = new ArrayList<String>();
    for(GomType type: this.signature.keySet()) {
      List<GomType> profile = this.signature.get(type).get(symbol);
      if(profile!=null) {
        // pem: make a defensive copy of the list?
        for(GomType argtype:profile) {
          res.add(argtype.getName());
        }
        return res;
      }
    }
    return null;
  }

  /** Get the arity of a symbol
   * @param symbol the name of the symbol
   * @return the arity of the symbol
   */
  public int getArity(String symbol) {
    int arity = -1;
    if(this.getProfile(symbol)!=null) {
      arity = this.getProfile(symbol).size();
    }
    return arity;
  }

  /** Get the list of all symbols
   * @return the list of symbols in the signature
   */
  public List<String> getTypeNames() {
    List<String> types = new ArrayList<String>();
    for(GomType type: this.signature.keySet()) {
      types.add(type.getName());
    }
    return types;
  }

  /** Get the list of all symbols
   * @return the list of symbols in the signature
   */
  public List<String> getSymbolNames() {
    List<String> symbols = new ArrayList<String>();

    for(GomType type: this.signature.keySet()) {
      symbols.addAll(signature.get(type).keySet());
    }
    return symbols;
  }

  /** Get the list of symbols for a given typeName
    * @typeName the name of the type
    * @return the list of symbols in the type
   */
  public List<String> getSymbolNames(String typeName) {
    List<String> symbols = new ArrayList<String>();
    symbols.addAll(signature.get(`GomType(typeName)).keySet());
    return symbols;
  }


  public String toString() {
    return "" + this.signature;
  }
}
