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

  private Map<GomType,Map<String,List<GomType>>> signature;

  public Signature(){
    this.signature = new HashMap<GomType,Map<String,List<GomType>>>(); 
  }


  public Signature setSignature(Program program) throws TypeMismatchException{
    %match(program) {
      Program(ConcProduction(_*,SortType(type,symb),_*),_) -> {
        Map<String,List<GomType>> symbols = new HashMap<String,List<GomType>>(); 
        %match(symb) {
          ConcAlternative(_*,Alternative(name,args,codomain),_*) -> {
            if(`codomain != `type){
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
    return this;
  }


  public Signature expandSignature() throws TypeMismatchException{
    Signature expandedSignature = new Signature();
    for(GomType type: this.signature.keySet()){
        Map<String,List<GomType>> symbols = new HashMap<String,List<GomType>>(); 
        for(String symbol: signature.get(type).keySet()){
          List<GomType> args = new ArrayList<GomType>(signature.get(type).get(symbol));
          symbols.put(symbol,args);
        }
        expandedSignature.signature.put(type,symbols);
    }
    expandedSignature.addSymbol("True",new ArrayList<String>(),"Boolean");
    expandedSignature.addSymbol("False",new ArrayList<String>(),"Boolean");
    expandedSignature.addSymbol("and",new ArrayList<String>(Arrays.asList("Boolean","Boolean")),"Boolean");

    // TODO: add generic operations
    // generatedSignature.put("eq",2);
    // generatedSignature.put("Bottom",1);

    return expandedSignature;
  }

  public void addSymbol(String name, List<String> argTypes, String codomain){
    Map<String,List<GomType>> typeSig = this.signature.get(`GomType(codomain));
    // if type of codomain doesn't exist then create it
    if(typeSig==null){
      typeSig = new HashMap<String,List<GomType>>();
    }
    // create arguments' types list
    List<GomType> args = new ArrayList<GomType>();
    for(String argType:argTypes){
      args.add(`GomType(argType));
    }
    typeSig.put(name,args);
    signature.put(`GomType(codomain),typeSig); 
  }


  public String toString(){
    return ""+this.signature;
  }
}
