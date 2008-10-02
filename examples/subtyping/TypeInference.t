package subtyping;

import subtyping.language.types.*;
import tom.library.sl.*;
import java.util.*;
import org.antlr.runtime.*;

public class TypeInference {
  %include{ language/Language.tom }
  %include{ sl.tom }

  %typeterm IntList { implement { ArrayList<Integer> } }

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

  //----------------------------------------
  // To resolve the constraints of typing
  // ---------------------------------------
  private static Substitution applyConstraintsResolution(ConstraintList cl) {
    return constraintsResolution(cl,`MList(),`CList());
  }

  private static Substitution constraintsResolution(ConstraintList cl, Substitution ml, ConstraintList subcl) {
    //System.out.println("ConstraintList = " +cl);
    
    %match(cl) {
      CList()
      -> { return ml;}

      CList(cons,consl*)
      -> {
        %match(cons) {
          // {A = A} U C -> resolution(C) || {X = X} U C -> resolution(C)
          Equation(type,type)
          -> { return `constraintsResolution(consl*,ml,subcl); }

          // {A <: A} U C -> resolution(C) || {X <: X} U C -> resolution(C)
          Subtype(type,type)
          -> { return `constraintsResolution(consl*,ml,subcl); }

          // {X = A} U C -> {X |-> A} && resolution([A/X]C) 
          Equation(tvar@TypeVar(_),type@Type(_))
          ->
          { 
            Mapping map = `MapsTo(tvar,type);
            Substitution rec = `constraintsResolution(applySubstitution(map,consl*),ml,applySubstitution(map,subcl*)); 
            return `MList(map,rec*);
          }

          // {A = X} U C -> {X |-> A} && resolution([A/X]C) 
          Equation(type@Type(_),tvar@TypeVar(_))
          ->
          { 
            Mapping map = `MapsTo(tvar,type);
            Substitution rec = `constraintsResolution(applySubstitution(map,consl*),ml,applySubstitution(map,subcl*)); 
            return `MList(map,rec*);
          }

          // {X = Y} U C -> {X |-> Y} && resolution([Y/X]C) 
          Equation(tvar1@TypeVar(_),tvar2@TypeVar(_)) &&
          (tvar1 != tvar2)
          ->
          { 
            Mapping map = `MapsTo(tvar1,tvar2);
            Substitution rec = `constraintsResolution(applySubstitution(map,consl*),ml,applySubstitution(map,subcl*)); 
            return `MList(map,rec*);
          }

          // {A = B} U C -> false
          Equation(t1@Type(_),t2@Type(_)) &&
          (t1 != t2)
          -> { throw new RuntimeException("Constraint resolution error: " + `t1 + " = " + `t2); }

          // {X <: Y,Y <: X} U C -> resolution({X = Y} U C)
          Subtype(tvar1@TypeVar(i1),tvar2@TypeVar(i2)) &&
          CList(x*,Subtype(TypeVar(i2),TypeVar(i1)),y*) << consl &&
          (tvar1 != tvar2)
          -> { return `constraintsResolution(CList(Equation(tvar1,tvar2),x*,y*),ml,subcl*); }

          Subtype(t1@TypeVar(_),t2)
          ->
          {
            // {X <: Y,X <: Z} U C -> resolution({X = min(Y,Z)} U C)
            %match(consl) {
              CList(x*,Subtype(ty1,t3),y*) &&
              (t1 == ty1) && (t2 != t3)
              ->
              { 
                TomType min_t = `minimalType(t2,t3,subcl*);
                return `constraintsResolution(CList(Equation(t1,min_t),x*,y*),ml,subcl*);
              }
            }
            // {X <: Y} -> {X = Y}
            return `constraintsResolution(CList(Equation(t1,t2),consl*),ml,subcl*);
          }

          //{A <: B} U C -> (insert({A <: B},SubList) && resolution(C))
          Subtype(t1@Type(_),t2) &&
          (t1 != t2)
          -> { return `constraintsResolution(consl*,ml,CList(cons,subcl*)); }
 
/*
          Subtype(t1,t2) && (t1 != t2)
          -> {
            %match {
              // {X <: Y,Y <: X} U C -> resolution({X = Y} U C)
              TypeVar(i1) << t1 &&
              TypeVar(i2) << t2 &&
              CList(x*,Subtype(TypeVar(i2),TypeVar(i1)),y*) << consl
              -> { return `constraintsResolution(CList(Equation(t1,t2),x*,y*),ml,subcl*); }
              // {X <: Y,X <: Z} U C -> resolution({X = min(Y,Z)} U C)

	            TypeVar(i) << t1 &&
              CList(x*,Subtype(TypeVar(i),t3),y*) << consl
              ->
              { 
                TomType min_t = `minimalType(t2,t3,subcl*);
                return `constraintsResolution(CList(Equation(t1,min_t),x*,y*),ml,subcl*);
              }
            }
            //{A <: B} U C -> (insert({A <: B},SubList) && resolution(C))
            return `constraintsResolution(consl*,ml,CList(cons,subcl*));
          }
*/
        }
        //return constraintsResolution(`consl*,ml);
      }
    }
    throw new RuntimeException("Error during resolution of constraints.");
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
      Input(ctx,rrlist,allMatchs) &&
      !Context() << ctx &&
      !TIList() << allMatchs
      -> { return `typeOf(ctx,rrlist,allMatchs,SList()); }
    }
    throw new RuntimeException("Error in declaration of Context and Matchs");
  }
  
  public static SubstitutionList typeOf(Context ctx, ReconResultList rrlist, TomInstructionList allMatchs, SubstitutionList allSubs) {
    %match(allMatchs) {
      TIList() -> { return allSubs; }
      TIList(match,matchs*)
      ->
      {
        counter = maxIndexTypeVars(`match) + 1;
        %match(reconTomInstruction(ctx,match,rrlist)) {
          pairList@!RRList()
          ->
          {
            ConstraintList cl = constraintsUnion(`pairList);
            Substitution subs = applyConstraintsResolution(cl);
            return `typeOf(ctx,rrlist,matchs*,SList(subs,allSubs*));
          }
        }
      }
    }
    throw new RuntimeException("Error in declaration of Context and Matchs");
  }

  //------------------------------------------------------
  // To reconstruct the type of a "match" instruction
  //------------------------------------------------------
  private static ReconResultList reconTomInstruction(Context ctx, TomInstruction match, ReconResultList rrlist) {
    %match(match) {
      Match(rule) -> { 
        ReconResultList rl = `reconClause(ctx,rule);
        return `RRList(rl*,rrlist*);
      }
    }
    throw new RuntimeException("Type reconstruction failed to the \"match\" instruction.");
  }

  //-------------------------------------------------
  // To reconstruct the type of one of the rules
  //-------------------------------------------------
  private static ReconResultList reconClause(Context ctx, Clause rule) {
    %match(rule) {
      Rule(condition,result) &&
      CRPair(ctx_c,pair_c) << reconCondition(ctx,condition)
      -> 
      {
        %match(reconBlockList(Context(ctx*,ctx_c*),result)) {
          RRList() -> { return `RRList(pair_c*); }
          pairList@!RRList() -> { return `RRList(pair_c*,pairList*); }
        }
      }
    }
    throw new RuntimeException("Type reconstruction failed to the rules of the pattern matching.");
  }

  //----------------------------------------------------
  // To reconstruct the type of the left side of rule
  // in others words, the pattern matching
  //----------------------------------------------------
  private static ContextAndResult reconCondition(Context ctx, Condition cond) {
    %match {
      Matching(pattern,subject,type) << cond &&
      CRPair(ctx_p,pair_p) << reconPattern(ctx,pattern) &&
      Pair(type_s,constraints_s) << reconTerm(ctx,subject) &&
      RRList(Pair(type_p,_)) << pair_p
      ->
      {
        ConstraintList cl_s = `CList(Equation(type,type_s),Equation(type_s,type_p),constraints_s*);
        return `CRPair(ctx_p,RRList(pair_p,Pair(type_s,cl_s)));
      }

      (Conjunction(cond1,cond2) << cond || Disjunction(cond1,cond2) << cond) &&
      CRPair(ctx1,res1) << reconCondition(ctx,cond1) &&
      CRPair(ctx2,res2) << reconCondition(ctx1,cond2)
      -> { return `CRPair(ctx2,RRList(res1*,res2*)); }

      (Equality(term1,term2) << cond ||
      Inequality(term1,term2) << cond ||
      Greater(term1,term2) << cond ||
      GreaterEq(term1,term2) << cond ||
      Less(term1,term2) << cond ||
      LessEq(term1,term2) << cond) &&
      Pair(type1,cons1) << reconTerm(ctx,term1) &&
      pair2@Pair(type2,_) << reconTerm(ctx,term2)
      ->
      {
        ConstraintList cl = `CList(Equation(type1,type2),cons1*);
        return `CRPair(ctx,RRList(Pair(type1,cl),pair2));
      }
    }
    throw new RuntimeException("Type reconstruction failed to the left side of the rules of the pattern matching.");
  }

  //-----------------------------------------------------
  // To reconstruct the types of the right side of rule
  // in other words, the type of backquote variables
  //-----------------------------------------------------
  private static ReconResultList reconBlockList(Context ctx, TomTermList backquotes) {
    return reconBackquotes(ctx,backquotes,`RRList());
  }

  private static ReconResultList reconBackquotes(Context ctx, TomTermList backquotes, ReconResultList pair) {
    %match(backquotes) {
      TTeList() -> { return pair; }
      TTeList(term,terms*)
      ->
      {
        ReconResult currentPair = `reconTerm(ctx,term);
        return `reconBackquotes(ctx,terms,RRList(currentPair,pair*));
      }
    }
    throw new RuntimeException("Type reconstruction failed to the right side of the rules of the pattern matching.");
  }

  //----------------------------------------
  // To reconstruct the types of a pattern
  //----------------------------------------
  private static ContextAndResult reconPattern(Context ctx, TomTerm pattern) {
    %match(pattern) {
      Var(name,type) -> { return `CRPair(Context(Jugement(name,type),ctx*),RRList(Pair(type,CList()))); }
      Fun(name,args) && Sig(dom,codom) << assocFun(ctx,name) &&
      CCPair(ctx_p,cons) << reconPatternArgsList(ctx,args,dom)
      -> 
      {
        return `CRPair(ctx_p,RRList(Pair(codom,cons)));
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
      RRList(Pair(type_p,cons_p)) << res_p
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
