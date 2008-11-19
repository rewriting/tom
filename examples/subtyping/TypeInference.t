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

      cl@CList(_*,Subtype(t2,t3@!t1),_*,Subtype(t1@Type(_),t2@!t1)) &&
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

  //----------------------------------------------------
  // To find a subtype declaration in a constraint list
  //----------------------------------------------------
  private static ConstraintList whoIsSubtype(TomType type1, TomType type2, ConstraintList cl) {
    %match(cl) {
      CList(_*,cons@Subtype(t1,t2),_*) &&
      t1 << TomType type1 &&
      t2 << TomType type2
      -> { return `CList(cons); }

      CList(_*,cons@Subtype(t2,t1),_*) &&
      t1 << TomType type1 &&
      t2 << TomType type2
      -> { return `CList(cons); }
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

      // C = {tvar = type} U C' -> {tvar |-> type} &&
      //                      (if (tvar in C') then (resolution([type/tvar]C))
      //                       else (resolution({type = type} U C'))) 
      cl@CList(x*,Equation(tvar@TypeVar(_),type@!tvar),y*) //&& 
      //(tvar != type)
      ->
      { 
        System.out.println("C = {tvar = type} U C' -> {tvar |-> type} && (if (tvar in C') " +
                           "then (resolution([type/tvar]C)) " +
                           "else (resolution({type = type} U C')))\n");
        Mapping map = `MapsTo(tvar,type);
        ml.add(map);

        ConstraintList sublist = `CList(x*,y*);
        %match(findTypeVars(tvar,sublist)) {
          True()
          ->
          {
            ConstraintList clist = `applySubstitution(map,cl);
            //System.out.println("after apply substitution = " + clist);
            sublist = applyTransitivity(clist);
	    return sublist;
          }
        }
        // apply substituion only on the pair
        return `CList(Equation(type,type),sublist*);
      }

      // C = {tprim = tvar} U C' -> {tvar |-> tprim} &&
      //                      (if (tvar in C') then (resolution([tprim/tvar]C))
      //                       else (resolution({tprim = tprim} U C')))
      cl@CList(x*,Equation(tprim@Type(_),tvar@TypeVar(_)),y*)
      ->
      { 
        System.out.println("C = {tprim = tvar} U C' -> {tvar |-> tprim} && (if (tvar in C') " +
                           "then (resolution([tprim/tvar]C)) " +
                           "else (resolution({tprim = tprim} U C')))\n");
        Mapping map = `MapsTo(tvar,tprim);
        ml.add(map);

        ConstraintList sublist = `CList(x*,y*);
        %match(findTypeVars(tvar,sublist)) {
          True()
          ->
          {
            ConstraintList clist = `applySubstitution(map,cl);
            //System.out.println("after apply substitution = " + clist);
            sublist = applyTransitivity(clist);
            return sublist;
          }
        }
      // apply substituion only on the pair
        return `CList(Equation(tprim,tprim),sublist*);
      }

      // C = {tprim1 = tprim2} U C' -> false
      CList(_*,Equation(tprim1@Type(_),tprim2@Type(_)),_*) &&
      (tprim1 != tprim2)
      -> { throw new RuntimeException("Bad subtyping declaration of types: " + `tprim1 + " = " + `tprim2); /*return `False();*/ }

      // C = {type1 <: type2, type2 <: type1} U C' -> resolution({type1 = type2} U C) si ((type1 = type2) notin C')
      cl@CList(_*,Subtype(type1,type2@!type1),_*,Subtype(type2,type1),_*) &&
      !CList(_*,Equation(type1,type2),_*) << cl     
      -> 
      {
        System.out.println("C = {type1 <: type2, type2 <: type1} U C' -> resolution({type1 = type2} U C) si ((type1 = type2) notin C) \n");        
        return `CList(Equation(type1,type2),cl*);
      }

      // C = {tvar1 <: tprim2, tvar1 <: tprim3} U C' -> resolution({tvar1 = min(tprim2,tprim3)} U C) 
      //                                           si ((tvar1 = min(tprim2,tprim3)) notin C')
      cl@CList(_*,Subtype(tvar1@TypeVar(_),tprim2@Type(_)),_*,Subtype(tvar1,tprim3@Type(_)),_*) &&
      (tprim2 != tprim3)
      ->
      { 
        System.out.println("C = {tvar1 <: tprim2, tvar1 <: tprim3} U C' -> resolution({tvar1 = min(tprim2,tprim3)} U C) " +
                           "si ((tvar1 = min(tprim2,tprim3)) notin C')\n");
        TomType min_t = `minimalType(tprim2,tprim3,cl);
        %match(cl) {
          !CList(_*,Equation(t1,t2),_*)  &&
          t1 << TomType tvar1 &&
          t2 << TomType min_t
          -> { return `CList(Equation(t1,t2),cl*); }
        }
      }

      // C = {tvar1 <: type2} U C' -> resolution({tvar1 = type2} U C)
      // Default case: should be after {tvar1 <: type2, tvar1 <: t3} U C ...
      cl@CList(_*,Subtype(tvar1@TypeVar(_),type2@!tvar1),_*)
      -> 
      {
        System.out.println("C = {tvar1 <: type2} U C' -> resolution({tvar1 = type2} U C)\n");
        return `CList(Equation(tvar1,type2),cl*);
      }

      // C = {type1 <:> type2} U C' -> (if ((type1 <: type2) in C') then (resolution({type1 <: type2} U C))
      //                           elseif ((type2 <: type1) in C') then (resolution({type2 <: type1} U C)))
      cl@CList(_*,CommSubtype(type1,type2@!type1),_*)
      ->
      {
        %match(whoIsSubtype(type1,type2,cl)) {
          False() -> { throw new RuntimeException("Bad subtyping declaration of types: " + `type1 + " <:> " + `type2);/*return `False();*/ }
          cons@!False() -> { return `CList(cons,cl*); }
        }
      }
    }
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
    return `reconEachCondition(ctx,cond,CList());
  }

  private static ContextAndConstraints reconEachCondition(Context ctx, Condition cond, ConstraintList cl) {
    %match {
      Matching(_,_,_) << cond -> { return `reconMatching(ctx,cond,cl); }
      
      Conjunction(conditions) << cond || Disjunction(conditions) << cond -> { return `reconConditionList(ctx,conditions,cl); }
      
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
        return `reconRelation(ctx,cond,cl);
      }
    }
    throw new RuntimeException("Error in reconCondition function.");
  }

  private static ContextAndConstraints reconMatching(Context ctx, Condition cond, ConstraintList cl) {
    %match {
      Matching(pattern,subject,type) << cond &&
      CRPair(ctx_p,pair_p) << reconPattern(ctx,pattern) &&
      Pair(type_s,constraints_s) << reconTerm(ctx_p,subject) &&
      Pair(type_p,cl_p) << pair_p
      ->
      {
        ConstraintList cl_s = `CList(Equation(type,type_s),Subtype(type_p,type_s),constraints_s*);
        return `CCPair(ctx_p,CList(cl_p*,cl_s*,cl*));
      }
    }
    throw new RuntimeException("Error in reconMatching function.");
  }

  private static ContextAndConstraints reconConditionList(Context ctx, ConditionList conditions, ConstraintList cl) {
    /*
      (Conjunction(cond1,cond2) << cond || Disjunction(cond1,cond2) << cond) &&
      CCPair(ctx1,cl1) << reconCondition(ctx,cond1) &&
      CCPair(ctx2,cl2) << reconCondition(ctx1,cond2)
      -> { return `CCPair(ctx2,CList(cl1*,cl2*)); }
    */
    %match {
      CondList() << conditions -> { return `CCPair(ctx,cl); }
      //(Conjunction(conds) << conditions || Disjunction(conds) << conditions) &&
      CondList(c1,cs*) << conditions
      ->
      {
        ContextAndConstraints cc_matchings = `collectLocalContexts(ctx,RRList(),CondList(),conditions,cl);
        //System.out.println("CCPair after collected local contexts:\n" + cc_matchings +"\n");

      /*      
        ConditionList withoutMatching = `CondList();
        ArrayList<Jugement> local_ctx = ArrayList<Jugement>();
        local_ctx.add(ctx);
        ArrayList<Constraint> local_cons = ArrayList<Constraint>();
        ArrayList<Condition> subjects = ArrayList<Condition>();
        try {
          withoutMatching = (ConditionList) `RepeatId(treatMatchings(local_ctx,local_cons,subjects)).visitLight(conds);
        } catch (VisitFailure e) {
          throw new RuntimeException("Error in reconCondition function: collecting matching in a composed condition.");
        }

        Context whole_ctx = `Context();
        for(int i=0; i<local_ctx.size(); i++) {
          whole_ctx = `Context(local_ctx[i],whole_ctx*);
        }
        
        ConstraintList whole_cons = `CList();
        for(int i=0; i<local_cons.size(); i++) {
          whole_cons = `CList(local_cons[i],whole_cons*);
        }

        ConditionList all_subjects = `CondList();
        for(int i=0; i<subjects.size(); i++) {i
          all_subjects = `CondList(subjects[i],all_subjects*);
        }
    */

        Condition first_cond = `c1;
        ConditionList rest_cond = `cs;
        %match(cc_matchings) {
          //the condition list constains Matchings instructions: we need to remove them
          CCPair(whole_ctx,whole_cons)
          ->
          {
            //System.out.println("CondList before strategy applications:\n" + `conditions +"\n");

            ConditionList withoutMatching = `conditions;
            try {
              withoutMatching = (ConditionList) `RepeatId(deleteMatchings()).visitLight(conditions);
            } catch (VisitFailure e) {
              throw new RuntimeException("Error in reconCondition function: collecting matching in a composed condition.");
            }
            ctx = `whole_ctx;
            cl = `whole_cons;

            //System.out.println("CondList after strategy applications:\n" + withoutMatching +"\n");
            //System.out.println("CList after remove matchings of condition list:\n" + cl +"\n");

            %match(withoutMatching) {
              CondList() -> { return `cc_matchings; }
              CondList(sub_c1,sub_cs*)
              -> 
              { 
                first_cond = `sub_c1; 
                rest_cond = `sub_cs;
              }
            }
            //return `reconEachCondition(Context(ctx*,whole_ctx*),withoutMatching,CList(whole_cons*,cl*)); 
          }
        }

        %match {
          CCPair(whole_ctx,whole_cons) << reconEachCondition(ctx,first_cond,cl) -> { return `reconConditionList(whole_ctx,rest_cond,whole_cons); }
        }
      }
    }
    throw new RuntimeException("Error in reconConditionList function.");
  }
  
  private static ContextAndConstraints reconRelation(Context ctx, Condition cond, ConstraintList cl) {
    %match {
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
        /* ConstraintList cl1 = `CList(Subtype(type1,newTypeVar),cons1*);
           ConstraintList cl2 = `CList(Subtype(type2,newTypeVar),cons2*); */
        return `CCPair(ctx,CList(CommSubtype(type1,type2),cons1*,cons2*,cl*));
      }
    }
    throw new RuntimeException("Error in reconRelation function.");
  }

  %strategy deleteMatchings() extends Identity() {
    visit ConditionList {
      test@CondList(x*,Matching(_,_,_),y*) 
      ->
      {
        //System.out.println("Testing strategy... :\n" + `test +"\n");
        return `CondList(x*,y*);
      }
    } 
  }
  //---------------------------------------------------------------------------
  // To reconstruct the types of each terms of a conjunction or a disjunction
  // It is necessary to collect all local context for each pattern before to
  // try to reconstruct types of theirs related subjects
  //---------------------------------------------------------------------------
  private static ContextAndConstraints collectLocalContexts(Context ctx, ReconResultList listRes, ConditionList listMatchings, ConditionList conds, ConstraintList cl) {
    %match {
      CondList(x*,match@Matching(pattern,_,_),y*) << conds &&
      CRPair(ctx_p,pair_p) << reconPattern(ctx,pattern) &&
      Pair(_,cons_p) << pair_p
      ->
      {
        Context whole_ctx = `Context(ctx_p*,ctx*);
        ReconResultList whole_listRes = `RRList(pair_p,listRes*);
        ConditionList whole_listMatchings = `CondList(match,listMatchings*);
        //System.out.println("\'conds\' in first case os collectLocalContexts: \n" + `CondList(x*,y*) +"\n");
        //System.out.println("\'wholeMatchings\' in first case os collectLocalContexts: \n" + whole_listMatchings +"\n");
        return `collectLocalContexts(whole_ctx,whole_listRes,whole_listMatchings,CondList(x*,y*),CList(cons_p*,cl*));
      }

      !CondList(_*,Matching(_,_,_),_*) << conds &&
      !CondList() << listMatchings
      ->
      {
        //System.out.println("CondList before call reconMatchingList:\n" + cl +"\n");
        ConstraintList cons_res = `reconMatchingList(ctx,listRes,listMatchings,cl);
        //System.out.println("CondList after call reconMatchingList:\n" + cons_res +"\n");
        return `CCPair(ctx,cons_res);
      }
      
      !CondList(_*,Matching(_,_,_),_*) << conds &&
      CondList() << listMatchings
      ->
      { 
        return `CCPair(ctx,cl); 
      }
    }
    throw new RuntimeException("Type reconstruction failed to collect matchings in a composed condition.");
  }

  private static ConstraintList reconMatchingList(Context ctx, ReconResultList listRes, ConditionList listMatchings, ConstraintList cl) {
    %match {
      CondList() << listMatchings -> { return cl; }

      CondList(cond,conds*) << listMatchings &&
      Matching(_,subject,type) << cond &&
      RRList(pair,pairs*) << listRes &&
      Pair(type_p,cons_p) << pair &&
      Pair(type_s,cons_s) << reconTerm(ctx,subject)
      -> 
      {
        ConstraintList cl_s = `CList(Equation(type,type_s),Subtype(type_p,type_s),cons_s*,cl*);
        return `reconMatchingList(ctx,pairs,conds,cl_s); 
      }
    }
    throw new RuntimeException("Error in reconMatchingList function.");
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
