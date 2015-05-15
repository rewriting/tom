package sa;

import sa.rule.types.*;
import java.util.*;
// import tom.library.sl.*;
// import aterm.*;
// import aterm.pure.*;

public class Signature {
  %include { rule/Rule.tom }
//   %include { sl.tom }
//   %include { java/util/types/Map.tom }
//   %include { java/util/types/List.tom }

  public static String BOOLEAN = "Boolean";
  public static String TRUE = "True";
  public static String FALSE = "False";
  public static String AND = "and";
  public static String BOTTOM = "Bottom";
  public static String EQ = "eq";

  public static final String DUMMY = "Dummy";


  // Codomain Type -> (SymbolName -> Domain List of types)
  private Map<GomType,Map<String,List<GomType>>> signature;

  public Signature() {
    this.signature = new HashMap<GomType,Map<String,List<GomType>>>(); 
  }


  public void setSignature(Program program) throws TypeMismatchException {
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

  /*
   * Create a new signature
   */
  public Signature expandSignature() throws TypeMismatchException {
    Signature expandedSignature = new Signature();
    for(GomType type: this.signature.keySet()) {
        Map<String,List<GomType>> symbols = new HashMap<String,List<GomType>>(); 
        for(String symbol: signature.get(type).keySet()) {
          List<GomType> args = new ArrayList<GomType>(signature.get(type).get(symbol));
          symbols.put(symbol,args);
        }
        expandedSignature.signature.put(type,symbols);
    }

    // add: True, False, and, eq
    expandedSignature.addSymbol(TRUE,new ArrayList<String>(),BOOLEAN);
    expandedSignature.addSymbol(FALSE,new ArrayList<String>(),BOOLEAN);
    expandedSignature.addSymbol(AND,Arrays.asList(BOOLEAN,BOOLEAN),BOOLEAN);
    String eq_boolean = desambiguateSymbol(EQ, Arrays.asList(BOOLEAN,BOOLEAN));
    expandedSignature.addSymbol(eq_boolean,Arrays.asList(BOOLEAN,BOOLEAN),BOOLEAN);

    // add: bottom(T), eq_T(T,T)
    for(GomType type: this.signature.keySet()) {
      String t = type.getName();
      expandedSignature.addSymbol(BOTTOM,Arrays.asList(t),t);
      String eq_t = desambiguateSymbol(EQ, Arrays.asList(t,t));
      expandedSignature.addSymbol(eq_t,Arrays.asList(t,t),BOOLEAN);
    }

    return expandedSignature;
  }

  private String desambiguateSymbol(String name, List<String> argTypes) {
    String res = name;
    // Just for TESTING
    // TODO: generate the correct eq_X_X in the rules (for the moment only "eq" generate)
//     for(String argType:argTypes) {
//       res += "_" + argType;
//     }
    return res;
  }

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
    List<GomType> oldDomain = symbols.put(name,args);
    if(oldDomain != null) {
      //System.out.println("redefinition: '" + name +"'" + oldDomain + " becomes '" + name + "'" + args);
      System.out.println(%[redefinition: '@name@'@oldDomain@ becomes '@name@'@args@]%);
      //throw new TypeMismatchException();
    }
    signature.put(`GomType(codomain),symbols); 
  }

  /** Get codomain for symbol
   */
  public GomType getCodomain(String symbol){
    GomType codomain=null;

    for(GomType type: this.signature.keySet()) {
      if(this.signature.get(type).get(symbol)!=null) {
        codomain = type;
        break;
      }
    }
    // pem: make a defensive copy of the list?
    return codomain;
  }

  /** Get the list of types of its arguments
   */
  public List<GomType> getProfile(String symbol){
    List<GomType> profile = null;

    for(GomType type: this.signature.keySet()) {
      if((profile=this.signature.get(type).get(symbol))!=null) {
        break;
      }
    }
    // pem: make a defensive copy of the list?
    return profile;
  }


  /** Get the arity of a symbol
   */
  public int getArity(String symbol){
    int arity = -1;
    if(this.getProfile(symbol)!=null) {
      arity = this.getProfile(symbol).size();
    }
    return arity;
  }


  /** Get the list of all symbols
   */
  public List<String> getSymbolNames() {
    List<String> symbols = new ArrayList<String>();

    for(GomType type: this.signature.keySet()) {
      symbols.addAll(signature.get(type).keySet());
      //for(String symbol: signature.get(type).keySet()) {
      //  symbols.add(symbol);
      //}
    }
    return symbols;
  }

  public String toString() {
    return "" + this.signature;
  }
}
