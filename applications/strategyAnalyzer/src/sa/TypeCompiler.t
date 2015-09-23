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
    this.generatedRules = new ArrayList<Rule>();
  }


  /**
   * Flattens all the types from the (extracted) signature into a unique (TERM) type
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
          // if a specific type in the signature than flatten it to TERM
          if(!ptype.equals(`GomType(Signature.TERM)) && !ptype.equals(`GomType(Signature.BOOLEAN))){
            newTypes.add(`GomType(Signature.TERM));
          }else{ // otherwise leave it as it is
            newTypes.add(`GomType(Signature.TERM));
          }
        }
        newProfiles.put(symbol,newTypes);
      }
      generatedSignature.addProfilesForType(`GomType(Signature.TERM),newProfiles);
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

        //         Rule(Appl(stratOp,TermList(Appl(fun,args),X*)), Appl(rightOp,A)) ->{
        Rule(Appl(stratOp,TermList(Appl(fun,args),X*)), rhs) ->{
          String opSymb = `stratOp;
          StrategyOperator op = Tools.getOperator(opSymb);

          // not an auxiliary symbol for ONE or ALL (i.e. one_..._f)  and thus with a strict propagation of Bottom
          //           if(Tools.getComposite(opSymb)==null && `stratOp != Signature.AND  && `stratOp != Signature.EQ ){
          if(`stratOp != Signature.AND  && `stratOp != Signature.EQ ){
            System.out.print("FUN : "+ `fun);
              for(GomType type: this.getTypes(`fun)){
                String typedSymbol = Tools.typeSymbol(opSymb,type.getName());
                //                 String typedRightOp = Tools.typeSymbol(`rightOp,type.getName());
                Term typedRhs = propagateType(`EmptyEnvironment(),`rhs,type);
                //                 Rule newRule = `Rule(Appl(typedSymbol,TermList(Appl(fun,args),X*)), Appl(typedRightOp,A));
                Rule newRule = `Rule(Appl(typedSymbol,TermList(Appl(fun,args),X*)), typedRhs);
                generatedRules.add(newRule);
                
                localSignature.addSymbol(typedSymbol,new ArrayList<String>(),type.toString());
              }
          }else{
            System.out.print("RULE symbol: "+ op);
            System.out.print(" for symbol "+ `fun);
            System.out.print(" of type " + extractedSignature.getCodomain(`fun));
            System.out.println(" -- FUN symbol for "+ opSymb + " :  "+ Tools.getComposite(opSymb));
          }
        }
      }
      
    }

    for(Rule rule: generatedRules){
      System.out.println(" RULE "+ Pretty.toString(rule));
    }

    System.out.println("GEN SIG = " + localSignature);
  }
  
  /********************************************************************************
   *     END
   ********************************************************************************/

  /** Get the potential types of symbol
   *  symbol can be a symbol from the estracted signature or Bottom
   */
  private List<GomType> getTypes(String symbol){
    List<GomType>  types = new ArrayList<GomType>();
    if(symbol == Signature.BOTTOM){
      for(GomType type: this.extractedSignature.getTypes()){
        types.add(type);
      }
    }else{
      if(extractedSignature.getCodomainType(symbol) != null){
        types.add(extractedSignature.getCodomainType(symbol));
      }
    }
    return types;
  }

  private Term propagateType(TypeEnvironment env, Term t, GomType type) {
    Term typedTerm = t;
    %match(t) {
      Appl(name,args) -> {
        // if term in the extracted signature then don't change it; change symbol names otherwise
        if(extractedSignature.getCodomainType(`name) == null){
          List<GomType> domain = new ArrayList<GomType>();
          String fun = Tools.getComposite(`name);
          if(fun != null){ // if a composite symbol (e.g. all_...-f)
            domain = extractedSignature.getProfileType(fun);
            // TODO : domain == null
            domain.add(domain.size(),type); // for all_f add the type of f(...) at the end; will be ignored for one_f
          }else{
            // at most 2 arguments; propagate the type
            // TODO: be more general ?
            domain.add(type);
            domain.add(type);
          }
          String dname = Tools.typeSymbol(`name,type.getName());
          int i = 0;
          TermList args = `args;
          TermList newArgs = `TermList();
          while(!args.isEmptyTermList()) {
            Term arg = args.getHeadTermList();
            //             Term arg2 = propagateType(env, arg, type);
            Term arg2 = propagateType(env, arg, domain.get(i));
            if(arg2!=null) {
              newArgs = `TermList(newArgs*, arg2);
            } else {
              // throw exception
              System.out.println("bad arg2: " + arg2);
              return null;
            }
            i++;
            args = args.getTailTermList();
          }
          typedTerm = `Appl(dname,newArgs);
        }
      }

      s@Var(name) -> {
        typedTerm = `s;
      }
    }
    return typedTerm;
  }


//   /*
//    * Specializer
//    * @param symbol the symbol whose type is enforce
//    * @param the type of the symbol
//    * @param trs the TRS to transform
//    */
//   public List<Rule> specialize(String symbol, String typeName, List<Rule> trs) {
//     Signature gSig = getGeneratedSignature();
//     String codomainName = gSig.getCodomain(symbol);

//     for(Rule r: trs) {
//       %match(r) {
//         Rule(lhs@Appl(name,args),rhs) -> {
//           if(`name.equals(symbol)) {
//             Term newLhs = propagateType(`EmptyEnvironment(), `lhs, `GomType(typeName));
//             System.out.println("newLhs = " + newLhs);
//           }
//         }
//       }

//     }
//     return null;
//   }

//   private Term propagateType(TypeEnvironment env, Term t, GomType type) {
//     Signature gSig = getGeneratedSignature();
//     %match(t) {
//       Appl(name,args) -> {
//         String codomainName = gSig.getCodomain(`name);
//         if(codomainName == Signature.TERM) {
//           List<String> domain = gSig.getProfile(`name);
//           String dname = gSig.disambiguateSymbol(`name, domain);
//           gSig.addSymbol(dname,domain,codomainName);
//           int i = 0;
//           TermList args = `args;
//           TermList newArgs = `TermList();
//           while(!args.isEmptyTermList()) {
//             Term arg = args.getHeadTermList();
//             // compute GOOD type here!
//             Term arg2 = propagateType(env, arg, `GomType(domain.get(i)));
//             if(arg2!=null) {
//               newArgs = `TermList(newArgs*, arg2);
//             } else {
//               // throw exception
//               System.out.println("bad arg2: " + arg2);
//               return null;
//             }
//             i++;
//             args = args.getTailTermList();
//           }
//           return `Appl(dname,newArgs);
//         } else if(type != `GomType(codomainName)) {
//           // throw exception
//           System.out.println("bad type: " + t + " : " + type);
//           return null;
//         }

//       }

//       Anti(t1) -> {
//         return propagateType(env, `t1,type);
//       }

//       At(t1,t2) -> {
//         env = checkType(env,`t1, type);
//         if(env == null) {
//           // throw exception
//           System.out.println("bad type: " + `t1 + " : " + type);
//           return null;
//         }
//         return propagateType(env, `t2,type);
//       }

//       s@BuiltinInt(i) -> {
//         if(type != `GomType("int")) {
//           // throw exception
//           System.out.println("bad type: " + `s + " : " + type);
//           return null;
//         }
//         return `s;
//       }

//       s@Var(name) -> {
//         env = checkType(env,`s, type);
//         if(env == null) {
//           // throw exception
//           System.out.println("bad type: " + `s + " : " + type);
//           return null;
//         } 
//         return `s;
//       }

//     }
//     System.out.println("propagateType should not be there: " + t);
//     return null;
//   }

//   /*
//    * check that t is a variable with a type compatible with the enviroment
//    * @param env the environment
//    * @param t the term to type check
//    * @param type the type
//    * @return null is t is not correctly typed, or the enviroment enriched with t:type
//    */
//   private TypeEnvironment checkType(TypeEnvironment env, Term t, GomType type) {
//     %match(env, t) {
//       EmptyEnvironment(), Var(x) -> {
//         return `PushEnvironment(x, type, EmptyEnvironment());
//       }
      
//       PushEnvironment(x,xtype,tail), Var(x) -> {
//         if(`xtype == type) {
//           return env;
//         } else {
//           // type mismatch
//           return null;
//         }
//       }
      
//       PushEnvironment(y,ytype,tail), Var(x) -> {
//         TypeEnvironment tmp = checkType(`tail, t, type);
//         if(tmp == null) {
//           return null;
//         } else {
//           return `PushEnvironment(y,type, tmp);
//         }
//       }

//     }

//     System.out.println("checkType should not be there: " + t);
//     return null;
//   }


  /***********************************************************************************/

  public Signature getExtractedSignature(){
    return this.extractedSignature;
  }
  public Signature getGeneratedSignature(){
    return this.generatedSignature;
  }

}
