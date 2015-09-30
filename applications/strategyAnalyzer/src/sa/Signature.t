package sa;

import sa.rule.types.*;
import java.util.*;

public class Signature {
  %include { rule/Rule.tom }

  // All literal strings and string-valued constant expressions are interned.
  public final static String TRUE = "True";
  public final static String FALSE = "False";
  public final static String AND = "and";
  public final static String BOTTOM = "Bottom";
  public final static String BOTTOMLIST = "BottomList";
  public final static String EQ = "eq";
  public final static String APPL = "Appl";
  public final static String CONS = "Cons";
  public final static String NIL = "Nil";
  
  public final static String ENCODE = "encode";
  public final static String DECODE = "decode";

  // Types
  public final static String BOOLEAN = "Bool";
  public final static String TERM = "Term";
  public final static String METASYMBOL = "MetaSymbol";
  public final static String METATERM = "MetaTerm";
  public final static String METALIST = "MetaList";



  // Codomain Type -> (SymbolName -> Domain List of types)
  private Map<GomType,Map<String,List<GomType>>> signature;

  public Signature() {
    this.signature = new HashMap<GomType,Map<String,List<GomType>>>(); 
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
  
  public Signature cloneSignature()  {
    Signature res = new Signature();
    // clone everything (lists could be just copied since normally not modified later on)
    for(GomType type: this.signature.keySet()) {
      for(String symbol: signature.get(type).keySet()) {
        List<GomType> domain = this.getProfileType(symbol);
        res.addSymbolType(symbol,domain,type);
      }
    }
    return res;
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
      for(String symbol: signature.get(type).keySet()) {
        List<String> domain = new ArrayList<String>();
        for(int i=0 ; i < getArity(symbol) ; i++) {
          domain.add(TERM);
        }
        expandedSignature.addSymbol(symbol,domain,TERM);
      }
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
    expandedSignature.addSymbol(EQ,Arrays.asList(TERM,TERM),BOOLEAN);

    // add: bottom
    if(!Main.options.metalevel) {
      expandedSignature.addSymbol(BOTTOM,Arrays.asList(TERM),TERM);
    } else {
      expandedSignature.addSymbol(BOTTOM,Arrays.asList(METATERM),METATERM);
    }

    // for metalevel + Tom code
    if(Main.options.metalevel && Main.options.classname != null) {
      // add: encode, decode
      expandedSignature.addSymbol(ENCODE,Arrays.asList(TERM),METATERM);
      expandedSignature.addSymbol(DECODE,Arrays.asList(METATERM),TERM);
    }
    return expandedSignature;
  }

  /**
   * Add a symbol (with the corresponding profile).
   * @param name the name of the symbol
   * @param argTypes the types of its arguments
   * @param codomain the return type 
   */
  public void addSymbol(String name, List<String> argTypes, String codomain) {
    List<GomType> domain = new ArrayList<GomType>();
    for(String s: argTypes) {
      domain.add(`GomType(s));
    }
    addSymbolType(name,domain,`GomType(codomain));
  }

  public void addSymbolType(String name, List<GomType> argTypes, GomType codomain) {
    Map<String,List<GomType>> symbols = this.signature.get(codomain);
    // if type of codomain doesn't exist then create it
    if(symbols==null) {
      symbols = new HashMap<String,List<GomType>>();
    }
    // create arguments' types list
    List<GomType> args = new ArrayList<GomType>();
    for(GomType argType:argTypes) {
      args.add(argType);
    }
    // detect overloading
    List<GomType> oldDomain = symbols.put(name.intern(),args);
    if(oldDomain != null) {
      //       System.out.println(%[redefinition: '@name@'@oldDomain@ becomes '@name@'@args@]%);
    }
    signature.put(codomain,symbols); 
  }

  /** Get codomain for symbol
   */
  public GomType getCodomainType(String symbol) {
    for(GomType type: this.signature.keySet()) {
      if(this.signature.get(type).get(symbol) != null) {
        return type;
      }
    }
    return null;
  }

  public String getCodomain(String symbol) {
    if(this.getCodomainType(symbol) != null) {
        return this.getCodomainType(symbol).getName();
    }
    return null;
  }

  /** Get the list of types of its arguments
   * @param symbol the name of the symbol
   * @return the list of types of its arguments
   */
  public List<GomType> getProfileType(String symbol) {
    List<GomType> res = null;
    for(GomType type: this.signature.keySet()) {
      List<GomType> profile = this.signature.get(type).get(symbol);
      if(profile!=null) {
        res = new ArrayList<GomType>();
        // pem: make a defensive copy of the list?
        for(GomType argtype:profile) {
          res.add(argtype);
        }
        return res;
      }
    }
    return res;
  }

  public List<String> getProfile(String symbol) {
    List<GomType> profile = getProfileType(symbol);
    List<String> res = null;
    if(profile != null){
      res = new ArrayList<String>();
      for(GomType type: profile) {
          res.add(type.getName());
      }
    }
    return res;
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

  /** Get the list of all types
   * @return the list of types in the signature
   */
  public Set<GomType> getTypes() {
    return signature.keySet();
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

  /** Get the profiles for the symbols of a given type
   * @return the map of profiles for the symbols of a given type
   */
  public Map<String,List<GomType>> getSymbolsOfType(GomType type) {
    return signature.get(type);
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


  /** Set the profiles of symbols for a given type
    * @type the type to be enhanced
    * @newProfiles the profiles of the symbols to be added
   */
  public void addProfilesForType(GomType type, Map<String,List<GomType>> newProfiles) {
    Map<String,List<GomType>> profiles = signature.get(type);
    if(profiles == null){
      signature.put(type, newProfiles);
    }else{
      for(String symbol: newProfiles.keySet()){
        // TODO: 
        if(profiles.get(symbol)!=null){
          System.out.println("WARNING: Symbol already in the generated signature");
        }
        profiles.put(symbol, newProfiles.get(symbol));
      }
    }
  }

  /** Remove the profiles of symbols for a given type
    * @type the type
   */
  public void removeType(GomType type) {
    signature.remove(type);
  }


  public String toString() {
    return "" + this.signature;
  }
}
