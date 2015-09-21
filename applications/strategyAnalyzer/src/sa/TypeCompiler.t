package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;

public class TypeCompiler {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/List.tom }
  %include { java/util/types/ArrayList.tom }

  %typeterm Signature { implement { Signature } }

  // The extracted (concrete) signature
  private Signature extractedSignature;
  // The generated (concrete) signature
  private Signature generatedSignature;
  // The generated (ordered) TRS
  private List<Rule> untypedRules;
  private List<Rule> generatedRules;

  public TypeCompiler(Signature extractedSignature, Signature generatedSignature, List<Rule> untypedRules){
    this.extractedSignature=extractedSignature;
    this.generatedSignature=generatedSignature;
    this.untypedRules=untypedRules;
  }


  /**
   * Flattens all the types from the (extracted) signature into a unique (DUMMY) type
   */
  public  void flattenSignature() {
    Set<GomType> extractedTypes = extractedSignature.getTypes();
    for(GomType type: extractedTypes){
      System.out.println("Type:"+type.getName());
      Map<String,List<GomType>> newProfiles = new HashMap<String,List<GomType>>();
      Map<String,List<GomType>> profiles = generatedSignature.getSymbolsOfType(type);
      for(String symbol:profiles.keySet()){
        List<GomType> newTypes = new ArrayList<GomType>();
        List<GomType> types = profiles.get(symbol);
        for(GomType ptype: types){
          // if a specific type in the signature than flatten it to DUMMY
          if(!ptype.equals(`GomType(Signature.DUMMY)) && !ptype.equals(`GomType(Signature.BOOLEAN))){
            newTypes.add(`GomType(Signature.DUMMY));
          }else{ // otherwise leave it as it is
            newTypes.add(`GomType(Signature.DUMMY));
          }
        }
        newProfiles.put(symbol,newTypes);
      }
      generatedSignature.addProfilesForType(`GomType(Signature.DUMMY),newProfiles);
      generatedSignature.removeType(type);
    }
  }


  public  void typeRules() {
    Signature localSignature = new Signature();
    Set<GomType> extractedTypes = extractedSignature.getTypes();

    for(Rule rule: untypedRules){
      //       System.out.println(rule);
      // if BOTTOM propagation rule

      %match(rule){
        Rule(Appl(stratOp,TermList(Appl(fun,args),X*)), Appl(rightOp,A)) ->{
          //           System.out.println("RULE symbol: "+ Tools.getRuleOperator(rule) 
          //                              + " for symbol "+Tools.getArgumentSymbol(rule)
          //                              + " of type " + extractedSignature.getCodomain(Tools.getArgumentSymbol(rule)));
          
          String opSymb = `stratOp;
          StrategyOperator op = Tools.getOperator(opSymb);
          System.out.print("RULE symbol: "+ op);
          System.out.print(" for symbol "+ `fun);
          System.out.print(" of type " + extractedSignature.getCodomain(`fun));
          System.out.println(" -- FUN symbol for "+ opSymb + " :  "+ Tools.getComposite(opSymb));

          // not an AUXiliary symbol and thus with a strict propagation of Bottom
          if(`fun == Signature.BOTTOM && !Tools.isAuxiliary(opSymb)){
            for(GomType type: extractedTypes){
              String typedSymbol = Tools.typeSymbol(opSymb,type.getName());
              String typedRightOp = Tools.typeSymbol(`rightOp,type.getName());
              Rule newRule = `Rule(Appl(typedSymbol,TermList(Appl(fun,args),X*)), Appl(typedRightOp,A));
//               generatedRules.
  
              System.out.println(" RULE "+ newRule);

              localSignature.addSymbol(typedSymbol,new ArrayList<String>(),type.toString());
            }
          }
        }
      }
      
    }
  }
  
  /********************************************************************************
   *     END
   ********************************************************************************/


  public Signature getExtractedSignature(){
    return this.extractedSignature;
  }
  public Signature getGeneratedSignature(){
    return this.generatedSignature;
  }

}
