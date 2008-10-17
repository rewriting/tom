package subtyping;

import subtyping.language.types.*;
import tom.library.sl.*;
import java.util.*;
import org.antlr.runtime.*;

public class TypeInference {
  %include{ language/Language.tom }
  %include{ sl.tom }

  %typeterm IntList { implement { ArrayList<Integer> } }
  %typeterm MappingList { implement { ArrayList<Mapping> } }

  private static int counter = 0;
  
  //---------------------------------
  // To generate a new TypeVar name
  //---------------------------------
  private static TomType freshTypeVar() {
    return `TypeVar(counter++);
  } 

  //---------------------------------------------------------------
  // To collect all TypeVar names existing in a specified subterm  
  //---------------------------------------------------------------
  %strategy collectTypeVars(listVars: IntList) extends Identity() {
    visit TomType {
      TypeVar(i) -> { listVars.add(`i); }
    }
  }
 
  //--------------------------------------------------
  // To collect all TypeVar names existing in a term
  //--------------------------------------------------
  private static ArrayList<Integer> getTypeVars(TomInstruction match) {
    ArrayList<Integer> listVars = new ArrayList<Integer>();
    try {
      `TopDown(collectTypeVars(listVars)).visitLight(match);
    } catch (VisitFailure e) {
      throw new RuntimeException("Error in getTypeVars function");
    }
    return listVars;
  }

  //----------------------------------------------------
  // To get the bigger "name" of all the TypeVar names
  //----------------------------------------------------
  private static int maxIndexTypeVars(TomInstruction match) {
     return Collections.max(getTypeVars(match));
  }

  //----------------------------------------------------
  // To select the lower type between two given types
  //----------------------------------------------------
  private static TomType minimalType(TomType t1, TomType t2, ConstraintList cl) {
    %match(cl) {
      CList(_*,Subtype(ty1,ty2),_*) &&
      ty1 << TomType t1 && ty2 << TomType t2
      -> { return t1; }
      CList(_*,Subtype(ty2,ty1),_*) &&
      ty2 << TomType t2 && ty1 << TomType t1
      -> { return t2; }
    }
    throw new RuntimeException("Error in minimalType function. t1 = " + t1+ " and t2 =" + t2 + ". List = " + cl);
  }

  //----------------------------------------------
  // To substitute the type variables of a term
  //----------------------------------------------
  %strategy subsTypeVars(i: int, type: TomType) extends Identity() {
    visit TomType {
      TypeVar(x) && x << int i -> { return type; }
    }
  } 

  private static ConstraintList applySubstitution(Mapping map, ConstraintList cl) {
    %match(map) {
      MapsTo(TypeVar(i),type)
      ->
      {
        try {
          return (ConstraintList) `TopDown(subsTypeVars(i,type)).visitLight(cl);
        } catch (VisitFailure e) {
          throw new RuntimeException("Error in applySubstitution function.");
        }
      }
    }
    throw new RuntimeException("Error in applySubstitution function.");
  }

  //-----------------------------------------------
  // To apply transitivity in the constraint list
  //-----------------------------------------------
  %strategy transitivity() extends Identity() {
    visit ConstraintList {
      cl@CList(_*,Subtype(t1@Type(_),t2@!t1),_*,Subtype(t2,t3@!t1),_*) &&
      !CList(_*,Subtype(t1,t3),_*) << cl &&
      (t2 != t3)
      -> { return `CList(Subtype(t1,t3),cl*); }
    }
  }

  private static ConstraintList applyTransitivity(ConstraintList cl) {
    %match(cl) {
      CList() -> { return `cl; }
      !CList()
      ->
      {
        try {
          return (ConstraintList) `RepeatId(transitivity()).visitLight(cl);
        } catch (VisitFailure e) {
          throw new RuntimeException("Error in applyTransitivity function");
        }
      }
    }
    throw new RuntimeException("Error in applyTransitivity function");
  }

  //---------------------------------------------------------------------
  // To know if a given TypeVar name already exists in a constraint list
  //---------------------------------------------------------------------
  private static ConstraintList findTypeVars(TomType tvar, ConstraintList cl) {
    %match(cl) {
      CList(_*,cons,_*)
      ->
      {
        %match(cons) {
          Equation(x,_) && x << TomType tvar -> { return `True(); }
          Equation(_,x) && x << TomType tvar -> { return `True(); }
          Subtype(x,_) && x << TomType tvar -> { return `True(); }
          Subtype(_,x) && x << TomType tvar -> { return `True(); }
        }
      }
    }
    return `False();
  }

  //----------------------------------------
  // To resolve the constraints of typing
  // ---------------------------------------
  %strategy constraintsResolution(ml: MappingList) extends Identity() {
    visit ConstraintList {
/*      // {type = type} U C -> resolution(C)
      CList(x*,Equation(type,type),y*)
      ->
      {
        System.out.println("{type = type} U C -> resolution(C)\n"); 
        return `CList(x*,y*);
      }

      // {type <: type} U C -> resolution(C)
      CList(x*,Subtype(type,type),y*)
      ->
      { 
        System.out.println("{type <: type} U C -> resolution(C)\n");
        return `CList(x*,y*); 
      }
*/
      // {tvar = type} U C -> {tvar |-> type} && resolution([type/tvar]C) si (tvar not(member) C) 
      cl@CList(x*,Equation(tvar@TypeVar(_),type),y*) && 
      (tvar != type)
      ->
      { 
        System.out.println("{tvar = type} U C -> {tvar |-> type} && resolution([type/tvar]C) " +
                           "si (tvar not(member) C)");
        Mapping map = `MapsTo(tvar,type);
        ml.add(`map);

        ConstraintList sublist = `CList(x*,y*);
        %match(findTypeVars(tvar,sublist)) {
          True()
          ->
          {
            ConstraintList clist = `applySubstitution(map,cl);
            //System.out.println("after apply substitution = " + clist);
            sublist = applyTransitivity(clist);
            return `CList(sublist*);
          }
        }
        // apply substituion only on the pair
        return `CList(Equation(type,type),x*,y*);
      }

      // {tprim = tvar} U C -> {tvar |-> tprim} && (resolution([tprim/tvar]C) si (tvar not(member) C))
      cl@CList(x*,Equation(tprim@Type(_),tvar@TypeVar(_)),y*)
      ->
      { 
        System.out.println("{tprim = tvar} U C -> {tvar |-> tprim} && (resolution([tprim/tvar]C) " +
                           "si (tvar not(member) C))");
        Mapping map = `MapsTo(tvar,tprim);
        ml.add(`map);

        ConstraintList sublist = `CList(x*,y*);
        %match(findTypeVars(tvar,sublist)) {
          True()
          ->
          {
            ConstraintList clist = `applySubstitution(map,cl);
            //System.out.println("after apply substitution = " + clist);
            sublist = applyTransitivity(clist);
            return `CList(sublist*);
          }
        }
      // apply substituion only on the pair
        return `CList(Equation(tprim,tprim),x*,y*);
      }

      // {tprim1 = tprim2} U C -> false
      CList(_*,Equation(tprim1@Type(_),tprim2@Type(_)),_*) &&
      (tprim1 != tprim2)
      -> { throw new RuntimeException("Bad subtyping declaration of types: " + `tprim1 + " = " + `tprim2); /*return `False();*/ }

      // {type1 <: type2, type2 <: type1} U C -> resolution({type1 = type2} U C) si ((type1 = type2) (not(member) C)
      cl@CList(_*,Subtype(type1,type2@!type1),_*,Subtype(type2,type1),_*) &&
      !CList(_*,Equation(type1,type2),_*) << cl     
      -> 
      {
        System.out.println("{type1 <: type2, type2 <: type1} U C -> resolution({type1 = type2} U C) " +
                           "si ((type1 = type2) (not(member) C)\n");
        return `CList(Equation(type1,type2),cl*);
      }

      // {tvar1 <: tprim2, tvar1 <: tprim3} U C -> resolution({tvar1 = min(type2,type3)} U C) si ((tvar1 = min(type2,type3)) (not(member)) C)
      cl@CList(_*,Subtype(tvar1@TypeVar(_),tprim2@Type(_)),_*,Subtype(tvar1,tprim3@Type(_)),_*) &&
      (tprim2 != tprim3)
      ->
      { 
        System.out.println("{tvar1 <: type2, tvar1 <: type3} U C -> resolution({tvar1 = min(type2,type3)} U C) " +
                           "si ((tvar1 = min(type2,type3)) (not(member)) C)\n");
        TomType min_t = `minimalType(tprim2,tprim3,cl);
        %match(cl) {
          !CList(_*,Equation(t1,t2),_*)  &&
          t1 << TomType tvar1 &&
          t2 << TomType min_t
          -> { return `CList(Equation(t1,t2),cl*); }
        }
      }

      // {tvar1 <: type2} U C -> resolution({tvar1 = type2} U C)
      // Default case: should be after {tvar1 <: type2, tvar1 <: t3} U C ...
      cl@CList(_*,Subtype(tvar1@TypeVar(_),type2),_*)
      -> 
      {
        System.out.println("{tvar1 <: type2} U C -> resolution({tvar1 = type2} U C)\n");
        return `CList(Equation(tvar1,type2),cl*);
      }
    }
  }

  //------------------------------------------
  // To put all constraints in an only list
  //------------------------------------------
  private static ConstraintList constraintsUnion(ReconResultList rrlist) {
    return getConstraintPair(rrlist,`CList());
  }

  private static ConstraintList getConstraintPair(ReconResultList rrlist, ConstraintList cl) {
    %match(rrlist) {
      RRList() -> { return cl; }
      RRList(rresult,rreslist*) && Pair(_,cons) << rresult
      -> { return `getConstraintPair(rreslist*,CList(cons*,cl*)); }
    }
    throw new RuntimeException("Error during the union of constraints.");
  }

  //--------------------------------------------------------
  // To call functions to reconstruct the types for each
  // kind of term of a pattern matching
  //--------------------------------------------------------
  public static SubstitutionList typeOfAll(Input in) {
    %match(in) {
      Input(ctx,clist,allMatchs) &&
      !Context() << ctx &&
      !TIList() << allMatchs
      -> { return `typeOf(ctx,clist,allMatchs,SList()); }
    }
    throw new RuntimeException("Error in declaration of Context and Matchs");
  }
  
  public static SubstitutionList typeOf(Context ctx, ConstraintList clist, TomInstructionList allMatchs, SubstitutionList allSubs) {
    %match(allMatchs) {
      TIList() -> { return allSubs; }
      TIList(match,matchs*)
      ->
      {
        counter = maxIndexTypeVars(`match) + 1;
        %match(reconTomInstruction(ctx,match,clist)) {
          cl@!CList()
          ->
          {
            try {
              ArrayList<Mapping> subs = new ArrayList<Mapping>();
              // original subtype relations defined by the programmer
              //System.out.println("-------- Initial CList = " + `cl + "\n");
              ConstraintList new_cl = `applyTransitivity(cl);
              //System.out.println("-------- Initial saturated CList = " + `new_cl + "\n");
              `RepeatId(constraintsResolution(subs)).visitLight(`new_cl);
              //System.out.println("-------- Final CList = " + `new_cl);
              Substitution res = `MList();
              for (Mapping m: subs) {
                res = `MList(m,res*);
              }
              return `typeOf(ctx,clist,matchs*,SList(res,allSubs*));
            } catch(VisitFailure e) {
              throw new RuntimeException("Error during resolution of constraints.");
            }
          }
        }
      }
    }
    throw new RuntimeException("Error in declaration of Context and Matchs");
  }

  //------------------------------------------------------
  // To reconstruct the type of a "match" instruction
  //------------------------------------------------------
  private static ConstraintList reconTomInstruction(Context ctx, TomInstruction match, ConstraintList clist) {
    %match(match) {
      Match(rule) -> { 
        ConstraintList cl = reconClause(ctx,`rule);
        return `CList(cl*,clist*);
      }
    }
    throw new RuntimeException("Type reconstruction failed to the \"match\" instruction.");
  }

  //-------------------------------------------------
  // To reconstruct the type of one of the rules
  //-------------------------------------------------
  private static ConstraintList reconClause(Context ctx, Clause rule) {
    %match(rule) {
      Rule(condition,result) &&
      CCPair(ctx_c,cl_c) << reconCondition(ctx,condition)
      -> 
      {
        %match(reconBlockList(Context(ctx*,ctx_c*),result)) {
          CList() -> { return `cl_c; }
          cl_bl@!CList() -> { return `CList(cl_c*,cl_bl*); }
        }
      }
    }
    throw new RuntimeException("Type reconstruction failed to the rules of the pattern matching.");
  }

  //----------------------------------------------------
  // To reconstruct the type of the left side of rule
  // in others words, the pattern matching
  //----------------------------------------------------
  private static ContextAndConstraints reconCondition(Context ctx, Condition cond) {
    %match {
      Matching(pattern,subject,type) << cond &&
      CRPair(ctx_p,pair_p) << reconPattern(ctx,pattern) &&
      Pair(type_s,constraints_s) << reconTerm(ctx,subject) &&
      Pair(type_p,cl_p) << pair_p
      ->
      {
        ConstraintList cl_s = `CList(Equation(type,type_s),Equation(type_s,type_p),constraints_s*);
        return `CCPair(ctx_p,CList(cl_p*,cl_s*));
      }

      (Conjunction(cond1,cond2) << cond || Disjunction(cond1,cond2) << cond) &&
      CCPair(ctx1,cl1) << reconCondition(ctx,cond1) &&
      CCPair(ctx2,cl2) << reconCondition(ctx1,cond2)
      -> { return `CCPair(ctx2,CList(cl1*,cl2*)); }

      (Equality(term1,term2) << cond ||
      Inequality(term1,term2) << cond ||
      Greater(term1,term2) << cond ||
      GreaterEq(term1,term2) << cond ||
      Less(term1,term2) << cond ||
      LessEq(term1,term2) << cond) &&
      Pair(type1,cons1) << reconTerm(ctx,term1) &&
      Pair(type2,cons2) << reconTerm(ctx,term2)
      ->
      {
        TomType newTypeVar = freshTypeVar();
        ConstraintList cl1 = `CList(Subtype(type1,newTypeVar),cons1*);
        ConstraintList cl2 = `CList(Subtype(type2,newTypeVar),cons2*);
        return `CCPair(ctx,CList(cl1*,cl2*));
      }
    }
    throw new RuntimeException("Type reconstruction failed to the left side of the rules of the pattern matching.");
  }

  //-----------------------------------------------------
  // To reconstruct the types of the right side of rule
  // in other words, the type of backquote variables
  //-----------------------------------------------------
  private static ConstraintList reconBlockList(Context ctx, TomTermList backquotes) {
    return reconBackquotes(ctx,backquotes,`CList());
  }

  private static ConstraintList reconBackquotes(Context ctx, TomTermList backquotes, ConstraintList cl) {
    %match(backquotes) {
      TTeList() -> { return cl; }
      TTeList(term,terms*) &&
      Pair(_,current_cl) << reconTerm(ctx,term)
      ->
      {
        return `reconBackquotes(ctx,terms,CList(current_cl*,cl*));
      }
    }
    throw new RuntimeException("Type reconstruction failed to the right side of the rules of the pattern matching.");
  }

  //----------------------------------------
  // To reconstruct the types of a pattern
  //----------------------------------------
  private static ContextAndResult reconPattern(Context ctx, TomTerm pattern) {
    %match(pattern) {
      Var(name,type) -> { return `CRPair(Context(Jugement(name,type),ctx*),Pair(type,CList())); }
      Fun(name,args) && Sig(dom,codom) << assocFun(ctx,name) &&
      CCPair(ctx_p,cons) << reconPatternArgsList(ctx,args,dom)
      -> 
      {
        return `CRPair(ctx_p,Pair(codom,cons));
      }
    }
    throw new RuntimeException("Type reconstruction failed to the pattern.");
  }

  //----------------------------------------
  // To reconstruct the types of arguments
  // of a pattern function
  //----------------------------------------
  private static ContextAndConstraints reconPatternArgsList(Context ctx, TomTermList pArgs, Domain dom) {
    return reconPatternArgs(ctx,pArgs,dom,`CList());  
  }

  private static ContextAndConstraints reconPatternArgs(Context ctx, TomTermList pArgs, Domain dom, ConstraintList cons) {
    %match(pArgs,dom) {
      TTeList(), Domain() -> { return `CCPair(ctx,cons); }
      TTeList(pterm,pterms*), Domain(pDom,pTypes*) &&
      CRPair(ctx_p,res_p) << reconPattern(ctx,pterm) &&
      Pair(type_p,cons_p) << res_p
      -> 
      {
        return `reconPatternArgs(ctx_p,pterms*,pTypes,CList(Subtype(type_p,pDom),cons_p*,cons*));
      }
    }
    throw new RuntimeException("Type reconstruction failed to the function arguments of a pattern.");
  }
  
  //---------------------------------------------
  // To reconstruct the types of the tom terms
  //---------------------------------------------
  private static ReconResult reconTerm(Context ctx, TomTerm term) {
    %match(term) {
      Var(name,type)
      -> 
      {
        TomType typeInContext = assocVar(ctx,`name);
        return `Pair(type,CList(Equation(typeInContext,type)));
      }
      Fun(name,args) && Sig(dom,codom) << assocFun(ctx,name)
      -> 
      {
        ConstraintList cl = `reconArgsList(ctx,args,dom);
        return `Pair(codom,cl);
      }
    }
    throw new RuntimeException("Type reconstruction failed to a term of a rule.");
  }

  //----------------------------------------
  // To reconstruct the types of arguments
  // of a function term
  //----------------------------------------
  private static ConstraintList reconArgsList(Context ctx, TomTermList args, Domain dom) {
    return reconArgs(ctx,args,dom,`CList());
  }

  private static ConstraintList reconArgs(Context ctx, TomTermList args, Domain dom, ConstraintList cons) {
    %match(args,dom) {
      TTeList(), Domain() -> { return cons; }
      TTeList(term,terms*), Domain(tDom,types*) && Pair(tArg,cons_t) << reconTerm(ctx,term)
      -> 
      {
        ConstraintList cl = `CList(Subtype(tArg,tDom),cons*,cons_t*);
        return `reconArgs(ctx,terms,types,cl);
      }
    }
    throw new RuntimeException("Type reconstruction failed to a function term.");
  }

  //--------------------------------------------------------------
  // To check if a variable is in the context and return his type
  //--------------------------------------------------------------
  private static TomType assocVar(Context ctx, String name) {
    %match(ctx) {
      Context(_*,Jugement(var,type),_*) && var << String name -> { return `type; }
    }
    throw new RuntimeException("Ill formed variable \"" + name + "\".");
  }

  //-------------------------------------------------------------------
  // To check if a function is in the context and return his signature
  //-------------------------------------------------------------------
  private static Signature assocFun(Context ctx, String name) {
    %match(ctx) {
      Context(_*,SigOf(fun,sig),_*) && fun << String name -> { return `sig; }
    }
    throw new RuntimeException("Function\"" + name + "\" was not declared.");
  }
}
