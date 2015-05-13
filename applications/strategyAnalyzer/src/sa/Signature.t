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
  public static String BOT = "bot";
  public static String EQ = "eq";


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

  // pem: ne peut-on pas faire : expandedSignature.signature = new HashMap(this.signature) ?

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

    // add: bot(T), eq_T(T,T)
    for(GomType type: this.signature.keySet()) {
      String t = type.getName();
      expandedSignature.addSymbol(BOT,Arrays.asList(t),t);
      String eq_t = desambiguateSymbol(EQ, Arrays.asList(t,t));
      expandedSignature.addSymbol(eq_t,Arrays.asList(t,t),BOOLEAN);
    }

    return expandedSignature;
  }

  private String desambiguateSymbol(String name, List<String> argTypes) {
    String res = name;for(String argType:argTypes) {
      res += "_" + argType;
    }
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


  public String toString() {
    return "" + this.signature;
  }
}
