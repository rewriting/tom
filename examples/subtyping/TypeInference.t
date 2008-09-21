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

  //----------------------------------------
  // To resolve the constraints of typing
  // ---------------------------------------
  private static Substitution constraintsResolution(ConstraintList cl) {
    %match(cl) {
      CList() -> { return `MList(); }
      CList(x*,Equation(Type(n),Type(n)),y*) -> {
        return `constraintsResolution(CList(x*,y*));
      }
/*     CList(x*,Equation(TypeVar(i),Type(n)),y*) ||
     CList(x*,Equation(Type(n),TypeVar(i)),y*)
     ->
     {
       return `constraintsResolution(subs);
     }*/
    }
    throw new RuntimeException("Error during resolution of constraints.");
  }

  private static ConstraintList constraintsUnion(ReconResultList rrlist) {
    return getConstraintPair(rrlist,`CList());
  }

  private static ConstraintList getConstraintPair(ReconResultList rrlist, ConstraintList cl) {
    %match(rrlist) {
      RRList() -> { return cl; }
      RRList(rresult,rreslist*) && Pair(_,cons) << rresult
      -> { return `getConstraintPair(rreslist,CList(cons*,cl*)); }
    }
    throw new RuntimeException("Error during the union of constraints.");
  }

  //------------------------------------------------------------------
  // To call functions to do the reconstruction of types for which
  // kind of term of a pattern matching
  // TODO: to change to get the context in a gom grammar directly
  //------------------------------------------------------------------
  //public static TomType typeOf(TomInstruction match) {
  public static ReconResultList typeOf(TomInstruction match) {
    /* Grammar to test the context
    | Nat = zero()
    |     | suc(pred:Nat)
    |     | mult(fac1:Nat,fac2:Nat)
    |     | square(y:Int)
    |
    | Int = Nat
    |     | uminus(x:Nat)
    |     | plus(x1:Int,x2:Int)
    */

    Context ctx = `Context(SigOf("zero", Sig(Domain(),Type("Nat"))),
                           SigOf("suc", Sig(Domain(Type("Nat")), Type("Nat"))),
                           SigOf("mult", Sig(Domain(Type("Nat"),Type("Nat")), Type("Nat"))),
                           SigOf("square", Sig(Domain(Type("Int")), Type("Nat"))),
                           SigOf("uminus", Sig(Domain(Type("Nat")), Type("Int"))),
                           SigOf("plus", Sig(Domain(Type("Int"),Type("Int")),Type("Int"))));
    ReconResultList rrlist = `RRList(Pair(Type("Int"),CList(Subtype(Type("Nat"),Type("Int")))));

    counter = maxIndexTypeVars(match) + 1;
    return reconTomInstruction(ctx,match,rrlist);
    /*
    %match(reconTomInstruction(ctx,match,rrlist)) {
      //RRList() -> { return null;}
      pairList@!RRList() -> {
        //Substitution subst = `unify(pairList);
        return null;//`applySubstitution(type,constraints);
      }
    }
    return null;*/
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
      CRPair(ctx_c,pair_c) << reconLeft(ctx,condition)
      -> 
      {
        %match(reconRight(Context(ctx*,ctx_c*),result)) {
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
  private static ContextAndResult reconLeft(Context ctx, Condition cond) {
    %match(cond) {
      Matching(pattern,subject,type) &&
      CRPair(ctx_p,pair_p) << reconPattern(ctx,pattern) &&
      Pair(type_s,constraints_s) << reconTerm(ctx,subject) 
      -> 
      {
        %match(pair_p) {
          RRList(Pair(type_p,_)) -> {
            ConstraintList cl_s = `CList(Equation(type,type_s),Equation(type_s,type_p),constraints_s*);
            return `CRPair(ctx_p,RRList(pair_p,Pair(type_s,cl_s)));
          }
        }
      }
    }
    throw new RuntimeException("Type reconstruction failed to the left side of the rules of the pattern matching.");
  }

  //-----------------------------------------------------
  // To reconstruct the types of the right side of rule
  // in other words, the type of backquote variables
  //-----------------------------------------------------
  private static ReconResultList reconRight(Context ctx, TomTermList backquotes) {
    return reconBackquotes(ctx,backquotes,`RRList());
  }

  private static ReconResultList reconBackquotes(Context ctx, TomTermList backquotes, ReconResultList pair) {
    %match(backquotes) {
      TTeList() -> { return pair; }
      TTeList(term,terms*) -> {
        ReconResult currentPair = `reconTerm(ctx,term);
        return `reconBackquotes(ctx,terms,RRList(currentPair,pair*));
      }
    }
    throw new RuntimeException("Type reconstruction failed to the right side of the rules of the pattern matching.");
  }

  //----------------------------------------
  // To reconstruct the types of a pattern
  //----------------------------------------
  private static ContextAndResult reconPattern(Context ctx, Pattern pattern) {
    %match(pattern) {
      Simple(Var(name,type)) -> { return `CRPair(Context(Jugement(name,type),ctx*),RRList(Pair(type,CList()))); }
      Simple(Fun(name,args)) && Sig(dom,codom) << assocFun(ctx,name) &&
      CCPair(ctx_p,cons) << reconPatternArgsList(ctx,args,dom) -> {
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
      CRPair(ctx_p,res_p) << reconPattern(ctx,Simple(pterm)) &&
      RRList(Pair(type_p,cons_p)) << res_p -> {
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
      Var(name,type) -> {
        TomType typeInContext = assocVar(ctx,`name);
        return `Pair(type,CList(Equation(typeInContext,type)));
      }
      Fun(name,args) && Sig(dom,codom) << assocFun(ctx,name) -> {
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
      TTeList(term,terms*), Domain(tDom,types*) && Pair(tArg,cons_t) << reconTerm(ctx,term) -> {
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
