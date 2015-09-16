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
  private List<Rule> generatedRules;

  public TypeCompiler(Signature extractedSignature, Signature generatedSignature){
    this.extractedSignature=extractedSignature;
    this.generatedSignature=generatedSignature;
  }


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
