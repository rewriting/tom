/*
   *
   * Copyright (c) 2004-2011, INPL, INRIA
   * All rights reserved.
   *
   * Redistribution and use in source and binary forms, with or without
   * modification, are permitted provided that the following conditions are
   * met:
   *  - Redistributions of source code must retain the above copyright
   *  notice, this list of conditions and the following disclaimer.
   *  - Redistributions in binary form must reproduce the above copyright
   *  notice, this list of conditions and the following disclaimer in the
   *  documentation and/or other materials provided with the distribution.
   *  - Neither the name of the INRIA nor the names of its
   *  contributors may be used to endorse or promote products derived from
   *  this software without specific prior written permission.
   * 
   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
   * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
   * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
   * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
   * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
   * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
   * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
   * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
   */

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
  %typeterm MatchingList { implement { ArrayList<Condition> } }

  private static int counter = 0;

/* Type inference doesn't work to "ComposedInstruction2.in" file example,
 * because it has constraints
 * {...,tvar4 <: NatP, tvar6 <: NatN, tvar4 = tvar6,...} and the algorithm maps
 * tvar4 to tvar6 succeded by tvar6 to NatP and it doesn't check that this
 * generate a constraint "NatP <: NatN" that has no solution */

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
      // C = {tvar = type} U C' -> {tvar |-> type} &&
      //                      (if (tvar in C') then (resolution([type/tvar]C))
      //                       else (resolution({type = type} U C'))) 
      cl@CList(x*,Equation(tvar@TypeVar(_),type@!tvar),y*) //&& 
      //(tvar != type)
      ->
      { 
/*        System.out.println("C = {tvar = type} U C' -> {tvar |-> type} && (if (tvar in C') " +
                           "then (resolution([type/tvar]C)) " +
                           "else (resolution({type = type} U C')))\n");
*/
        //System.out.println("tvar: " + `tvar + ".   type: " + `type + "\n");

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
/*        System.out.println("C = {tprim = tvar} U C' -> {tvar |-> tprim} && (if (tvar in C') " +
                           "then (resolution([tprim/tvar]C)) " +
                           "else (resolution({tprim = tprim} U C')))\n");
*/
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

      // C = {type1 <: type2, type2 <: type1} U C' -> resolution({type1 = type2} U C) if ((type1 = type2) notin C')
      cl@CList(_*,Subtype(type1,type2@!type1),_*,Subtype(type2,type1),_*) &&
      !CList(_*,Equation(type1,type2),_*) << cl     
      -> 
      {
/*        System.out.println("C = {type1 <: type2, type2 <: type1} U C' -> resolution({type1 = type2} U C) if ((type1 = type2) notin C) \n"); 
*/          
          return `CList(Equation(type1,type2),cl*);
      }

      // C = {tvar1 <: tprim2, tvar1 <: tprim3} U C' -> resolution({tvar1 = min(tprim2,tprim3)} U C) 
      //                                           if ((tvar1 = min(tprim2,tprim3)) notin C')
      cl@CList(_*,Subtype(tvar1@TypeVar(_),tprim2@Type(_)),_*,Subtype(tvar1,tprim3@Type(_)),_*) &&
      (tprim2 != tprim3)
      ->
      { 
/*        System.out.println("C = {tvar1 <: tprim2, tvar1 <: tprim3} U C' -> resolution({tvar1 = min(tprim2,tprim3)} U C) " +
                           "if ((tvar1 = min(tprim2,tprim3)) notin C')\n");
*/
        TomType min_t = `minimalType(tprim2,tprim3,cl);
        %match(cl) {
          !CList(_*,Equation(t1,t2),_*)  &&
          t1 << TomType tvar1 &&
          t2 << TomType min_t
          -> { return `CList(Equation(t1,t2),cl*); }
        }
      }

      // C = {tvar1 <: type2} U C' -> resolution({tvar1 = type2} U C)
      //                              if ((tvar1 = type2) notin C')
      // Default case: should be after {tvar1 <: tprim2, tvar1 <: tprim3} U C ...
      cl@CList(_*,Subtype(tvar1@TypeVar(_),type2@!tvar1),_*) && !CList(_*,Equation(tvar1,type2),_*) << cl
      -> 
      {
/*        System.out.println("C = {tvar1 <: type2} U C' -> resolution({tvar1 = type2} U C)" +
                           "if ((tvar1 = type2) notin C')\n");
*/        
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
  
  private static SubstitutionList typeOf(Context ctx, ConstraintList clist, TomInstructionList allMatchs, SubstitutionList allSubs) {
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
              //System.out.println("-------- Initial CList = " + `cl + "\n");
              ConstraintList new_cl = `applyTransitivity(cl);
              System.out.println("-------- Initial saturated CList = " + `new_cl + "\n");
              `RepeatId(constraintsResolution(subs)).visitLight(`new_cl);
              System.out.println("-------- Final CList = " + `new_cl);
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
      Match(rule) 
      -> 
      { 
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
      
      Conjunction(conditions) << cond || Disjunction(conditions) << cond -> { return `reconConjDisj(ctx,conditions,cl); }
      
      (Equality(term1,term2) << cond ||
      Inequality(term1,term2) << cond ||
      Greater(term1,term2) << cond ||
      GreaterEq(term1,term2) << cond ||
      Less(term1,term2) << cond ||
      LessEq(term1,term2) << cond) &&
      Pair(type1,cons1) << reconTerm(ctx,term1) &&
      Pair(type2,cons2) << reconTerm(ctx,term2)
      -> { return `reconRelation(ctx,cond,cl); }
    }
    throw new RuntimeException("Error in reconCondition function.");
  }

  //----------------------------------------------------------
  // To reconstruct the type of a single matching condition
  //----------------------------------------------------------
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

  //-----------------------------------------------------------
  // To reconstruct the type of a single relation condition
  //-----------------------------------------------------------
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
        return `CCPair(ctx,CList(CommSubtype(type1,type2),cons1*,cons2*,cl*));
      }
    }
    throw new RuntimeException("Error in reconRelation function.");
  }

  //----------------------------------------------------------------------------
  // To reconstruct the types of each terms of a conjunction or a disjunction
  // It is necessary to collect all local context for each pattern before to
  // try to reconstruct types of theirs related subjects
  //---------------------------------------------------------------------------- 
  %strategy unfoldCondList() extends Identity() {
    visit ConditionList {
      CondList(x*,Conjunction(CondList(sublist*)),y*) -> { return `CondList(x*,sublist*,y*); }
      CondList(x*,Disjunction(CondList(sublist*)),y*) -> { return `CondList(x*,sublist*,y*); }
    }
  }

  %strategy separeMatchings(ml : MatchingList) extends Identity() {
    visit ConditionList {
      CondList(x*,match@Matching(_,_,_),y*) 
      ->
      {
        //System.out.println("Testing strategy... :\n" + `test +"\n");
        ml.add(`match);
        return `CondList(x*,y*);
      }
    } 
  }

  private static ContextAndConstraints reconConjDisj(Context ctx, ConditionList conditions, ConstraintList cl) {
    //System.out.println("CondList before unfolding:\n" + `conditions + "\n");
    // 1 - unfold the condition list
    ConditionList unfold_conditions = `conditions;
    try {
      unfold_conditions = (ConditionList) `RepeatId(unfoldCondList()).visitLight(conditions);
    } catch (VisitFailure e) {
      throw new RuntimeException("Error in reconCondition function: collecting matching in a composed condition.");
    }
  
    //System.out.println("CondList after unfolding:\n" + unfold_conditions + "\n");

    // 2 - separe matching condition from the other conditons
    ConditionList withoutMatchings = `CondList();
    ArrayList<Condition> onlyMatchings = new ArrayList<Condition>();
    try {
      withoutMatchings = (ConditionList) `RepeatId(separeMatchings(onlyMatchings)).visitLight(unfold_conditions);
    } catch (VisitFailure e) {
      throw new RuntimeException("Error in reconCondition function: collecting matching in a composed condition.");
    }

    ConditionList matchings_list = `CondList();
    for (int i=0; i < onlyMatchings.size(); i++) {
      matchings_list = `CondList(onlyMatchings.get(i),matchings_list*);
    }

    // 3 - collect local contexts and contraints of patterns and reconstruct type of each one 
    ContextAndResultList local_crlist = collectLocalContexts(ctx,`RRList(),matchings_list);

    // 4 - reconstruct types of subjects
    ContextAndConstraints whole_cc = `reconMatchingList(local_crlist,matchings_list,cl);

    %match(whole_cc){
      CCPair(whole_ctx,whole_cl)
      ->
      {
        ctx = `whole_ctx;
        cl = `whole_cl*;
      }
    }

    // 5 - reconstruct the rest of the condition list (without matchings)
    return `reconConditionList(ctx,withoutMatchings,cl);
  }

  //---------------------------------------------------------------------------
  // To collect the local context of each pattern and its type and constraints
  // related to this type. This data will be used to reconstruct types of
  // the subjects and the other conditions that are not matchings (like
  // a relation condition)
  //---------------------------------------------------------------------------
  private static ContextAndResultList collectLocalContexts(Context ctx, ReconResultList rrl, ConditionList ml) {
    %match {
      CondList() << ml -> { return `CRLPair(ctx,rrl); }

      CondList(match,matchs*) << ml &&
      Matching(pattern,_,_) << match &&
      CRPair(ctx_p,pair_p) << reconPattern(ctx,pattern)      
      ->
      {
        Context whole_ctx = `Context(ctx_p*,ctx*);

        //the order of insertion in RRList is important because it needs to be the same of matchings_list
        ReconResultList whole_rrl = `RRList(rrl*,pair_p);
        //ConstraintList whole_cl = `CList(cons_p*,cl*);
        //System.out.println("\'conds\' in first case os collectLocalContexts: \n" + `CondList(x*,y*) +"\n");
        //System.out.println("\'wholeMatchings\' in first case os collectLocalContexts: \n" + whole_listMatchings +"\n");
        return `collectLocalContexts(whole_ctx,whole_rrl,matchs);
      }
    }
    throw new RuntimeException("Type reconstruction failed to collect local contexts in a composed condition.");
  }

  //----------------------------------------------------------------------------
  // To reconstruct types of a set of subjects by global and all local contexts
  //----------------------------------------------------------------------------
  private static ContextAndConstraints reconMatchingList(ContextAndResultList crlist, ConditionList listMatchings, ConstraintList cl) {
    //System.out.println("CRLPair in reconMatchingList: \n" + crlist + "\n");
    //System.out.println("CondList in reconMatchingList: \n" + listMatchings + "\n");
    //System.out.println("CList in reconMatchingList: \n" + cl + "\n");

    %match {
      CondList() << listMatchings &&
      CRLPair(ctx,_) << crlist
      -> { return `CCPair(ctx,cl); }

      CondList(match,matchs*) << listMatchings &&
      Matching(_,subject,type) << match &&
      CRLPair(ctx,rrlist) << crlist &&
      RRList(pair,pairs*) << rrlist &&
      Pair(type_p,cons_p) << pair &&
      Pair(type_s,cons_s) << reconTerm(ctx,subject)
      -> 
      {
        //System.out.println("Pair: \n" + `pair + "\n");
        //System.out.println("Match: \n" + `match + "\n");
        ConstraintList cl_s = `CList(Equation(type,type_s),Subtype(type_p,type_s),cons_p*,cons_s*,cl*);
        return `reconMatchingList(CRLPair(ctx,pairs),matchs,cl_s); 
      }
    }
    throw new RuntimeException("Error in reconMatchingList function.");
  }

  //------------------------------------------------------------------
  // To reconstruct types of teh other conditions by global and all 
  // local contexts
  //------------------------------------------------------------------
  private static ContextAndConstraints reconConditionList(Context ctx, ConditionList condlist, ConstraintList cl) {
    %match {
      CondList() << condlist -> { return `CCPair(ctx,cl); }

      CondList(cond,conds*) << condlist &&
      CCPair(whole_ctx,whole_cl)  << reconEachCondition(ctx,cond,cl)
      -> { return `reconConditionList(whole_ctx,conds,whole_cl); }
    }
    throw new RuntimeException("Error in reconConditionList function.");
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
      Var(name,type)
      -> 
      { 
        ConstraintList cl = `CList();
        if (isAssocVar(ctx,`name)) {
          TomType existent_type = assocVar(ctx,`name);
          cl = `CList(Equation(type,existent_type));
        }
        else
          ctx = `Context(Jugement(name,type),ctx*);
        return `CRPair(ctx,Pair(type,cl)); 
      }

      Fun(name,args) && Sig(dom,codom) << assocFun(ctx,name) &&
      CCPair(ctx_p,cl) << reconPatternFunArgs(ctx,args,dom)
      -> { return `CRPair(ctx_p,Pair(codom,cl)); }

      List(name,args) &&
      Sig(dom,codom) << assocList(ctx,name) &&
      VariadicDomain(type) << dom &&
      CCPair(ctx_p,cl) << reconPatternListArgs(ctx,args,name,type,codom)
      -> { return `CRPair(ctx_p,Pair(codom,cl)); }

      // it is not allow to have a list variable like a pattern
    }
    throw new RuntimeException("Type reconstruction failed to the pattern.");
  }

  //----------------------------------------
  // To reconstruct the types of arguments
  // of a function in a pattern
  //----------------------------------------
  private static ContextAndConstraints reconPatternFunArgs(Context ctx, TomTermList pArgs, Domain dom) {
    return reconPatternFArgs(ctx,pArgs,dom,`CList());  
  }

  private static ContextAndConstraints reconPatternFArgs(Context ctx, TomTermList pArgs, Domain dom, ConstraintList cons) {
    %match(pArgs,dom) {
      TTeList(), Domain() -> { return `CCPair(ctx,cons); }
      TTeList(pterm,pterms*), Domain(pDom,pTypes*) &&
      CRPair(ctx_p,res_p) << reconPattern(ctx,pterm) &&
      Pair(type_p,cons_p) << res_p
      -> 
      {
        return `reconPatternFArgs(ctx_p,pterms*,pTypes,CList(Subtype(type_p,pDom),cons_p*,cons*));
      }
    }
    throw new RuntimeException("Type reconstruction failed to the function arguments of a pattern.");
  }

  //----------------------------------------
  // To reconstruct the types of arguments
  // of a list in a pattern
  //----------------------------------------
  private static ContextAndConstraints reconPatternListArgs(Context ctx, TomTermList pArgs, String name, TomType dom, TomType codom) {
    return reconPatternLArgs(ctx,pArgs,name,dom,codom,`CList());  
  }

  private static ContextAndConstraints reconPatternLArgs(Context ctx, TomTermList pArgs, String name, TomType dom, TomType codom, ConstraintList cons) {
    %match {
      TTeList() << pArgs -> { return `CCPair(ctx,cons); }

      TTeList(pterm,pterms*) << pArgs &&
      ListVar(_,type) << pterm &&
      CRPair(ctx_p,res_p) << reconPatternListVar(ctx,pterm) &&
      Pair(_,cons_p) << res_p
      -> { return `reconPatternLArgs(ctx_p,pterms*,name,dom,codom,CList(Equation(type,codom),cons_p*,cons*)); }
        
      TTeList(pterm,pterms*) << pArgs &&
      !List(_,_) << pterm &&
      CRPair(ctx_p,res_p) << reconPattern(ctx,pterm) &&
      Pair(type_p,cons_p) << res_p
      -> { return `reconPatternLArgs(ctx_p,pterms*,name,dom,codom,CList(Subtype(type_p,dom),cons_p*,cons*)); }

      TTeList(pterm,pterms*) << pArgs &&
      List(tName,_) << pterm
      -> 
      {
        %match {
          tName << String name &&
          CRPair(ctx_p,res_p) << reconPattern(ctx,pterm) &&
          Pair(_,cons_p) << res_p
          // it is not necessary to test if the type of "term" and the time of the original list
          // are equals because it is forbidden to declare 2 lists with the same constructor
          // name so, it is not possible to have 2 lists with same name and different types
          -> { return `reconPatternLArgs(ctx_p,pterms*,name,dom,codom,CList(cons_p*,cons*)); }
        }
        throw new RuntimeException("Ill formed list\"" + name + "\".");
      }
    }
    throw new RuntimeException("Type reconstruction failed to the function arguments of a pattern.");
  }

  //--------------------------------------------------------------------
  // To reconstruct the type of a list variable in a list of a pattern
  //--------------------------------------------------------------------
  private static ContextAndResult reconPatternListVar(Context ctx, TomTerm pattern) {
    %match(pattern) {
      ListVar(name,type)
      -> 
      { 
        ConstraintList cl = `CList();
        if (isAssocVar(ctx,`name)) {
          TomType existent_type = assocVar(ctx,`name);
          cl = `CList(Equation(type,existent_type));
        }
        else
          ctx = `Context(Jugement(name,type),ctx*);
        return `CRPair(ctx,Pair(type,cl)); 
      }
    }
    throw new RuntimeException("Type reconstruction failed to the list variable in a list of a pattern.");
  }

  //---------------------------------------------
  // To reconstruct the types of the tom terms
  //---------------------------------------------
  private static ReconResult reconTerm(Context ctx, TomTerm term) {
    //System.out.println("Term to reconstruct: " + term + "\n");
    %match {
      Var(name,type) << term || ListVar(name,type) << term
      -> 
      {
        TomType typeInContext = assocVar(ctx,`name);
        return `Pair(type,CList(Equation(typeInContext,type)));
      }
/*
      ListVar(name,type) << term 
      -> 
      {
        TomType typeInContext = assocVar(ctx,`name);
        return `Pair(type,CList(Equation(typeInContext,type)));
      }
*/
      Fun(name,args) << term && Sig(dom,codom) << assocFun(ctx,name)
      -> 
      {
        ConstraintList cl = `reconFunArgs(ctx,args,dom);
        return `Pair(codom,cl);
      }

      List(name,args) << term &&
      Sig(dom,codom) << assocList(ctx,name) &&
      VariadicDomain(type) << dom
      ->
      {
        ConstraintList cl = `reconListArgs(ctx,args,name,type);
        return `Pair(codom,cl);
      }

      //ListVar(name,_) -> { throw new RuntimeException("List variable \"" + `name + "\" is not allowed in subject!"); }
    }
    throw new RuntimeException("Type reconstruction failed to a term of a rule.");
  }

  //----------------------------------------
  // To reconstruct the types of arguments
  // of a function term
  //----------------------------------------
  private static ConstraintList reconFunArgs(Context ctx, TomTermList args, Domain dom) {
    return reconFArgs(ctx,args,dom,`CList());
  }

  private static ConstraintList reconFArgs(Context ctx, TomTermList args, Domain dom, ConstraintList cons) {
    %match(args,dom) {
      TTeList(), Domain() -> { return cons; }
      TTeList(term,terms*), Domain(tDom,types*) && Pair(tArg,cons_t) << reconTerm(ctx,term)
      -> 
      {
        ConstraintList cl = `CList(Subtype(tArg,tDom),cons*,cons_t*);
        return `reconFArgs(ctx,terms,types,cl);
      }
    }
    throw new RuntimeException("Type reconstruction failed to a function term.");
  }

  //-------------------------------------------
  // To reconstruct the types of arguments
  // of a list term ("star" and "merge" rules
  //-------------------------------------------
  private static ConstraintList reconListArgs(Context ctx, TomTermList args, String name, TomType dom) {
    return reconLArgs(ctx,args,name,dom,`CList());
  }

  private static ConstraintList reconLArgs(Context ctx, TomTermList args, String name, TomType dom, ConstraintList cons) {
    %match {
      TTeList() << args -> { return cons; }

      TTeList(term,terms*) << args &&
      !List(_,_) << term &&
      Pair(tArg,cons_t) << reconTerm(ctx,term)
      -> 
      {
        ConstraintList cl = `CList(Subtype(tArg,dom),cons*,cons_t*);
        return `reconLArgs(ctx,terms,name,dom,cl);
      }

      TTeList(term,terms*) << args &&
      List(tName,_) << term
      -> 
      {
        %match {
          tName << String name &&
          Pair(_,cons_t) << reconTerm(ctx,term)
          // it is not necessary to test if the type of "term" and the time of the original list
          // are equals because it is forbidden to declare 2 lists with the same constructor
          // name so, it is not possible to have 2 lists with same name and different types
          ->
          {
            ConstraintList cl = `CList(cons*,cons_t*);
            return `reconLArgs(ctx,terms,name,dom,cl);
          }
        }
        throw new RuntimeException("Ill formed list\"" + name + "\".");
      }
    }
    throw new RuntimeException("Type reconstruction failed to a function term.");
  }

  //--------------------------------------------------------------
  // To check if a variable is in the context
  //--------------------------------------------------------------
  private static boolean isAssocVar(Context ctx, String name) {
    %match(ctx) {
      Context(_*,Jugement(var,type),_*) && var << String name -> { return true; }
    }
    return false;
  }

  //--------------------------------------------------------------
  // To check if a variable is in the context and return its type
  //--------------------------------------------------------------
  private static TomType assocVar(Context ctx, String name) {
    %match(ctx) {
      Context(_*,Jugement(var,type),_*) && var << String name -> { return `type; }
    }
    throw new RuntimeException("Ill formed variable \"" + name + "\".");
  }

  //-------------------------------------------------------------------
  // To check if a function is in the context and return its signature
  //-------------------------------------------------------------------
  private static Signature assocFun(Context ctx, String name) {
    %match(ctx) {
      Context(_*,SigOf(fun,sig),_*) && fun << String name
      -> 
      { 
        %match(sig) {
          Sig(dom,codom) && Domain(_*) << dom -> { return `sig; }
        }  
        throw new RuntimeException("Ill formed function\"" + name + "\"."); 
      }
    }
    throw new RuntimeException("Function\"" + name + "\" was not declared.");
  }

  //-------------------------------------------------------------------
  // To check if a list is in the context and return its signature
  //-------------------------------------------------------------------
  private static Signature assocList(Context ctx, String name) {
    %match(ctx) {
      Context(_*,SigOf(list,sig),_*) && list << String name
      -> 
      { 
        %match(sig) {
          Sig(dom,codom) && VariadicDomain(_) << dom -> { return `sig; }
        }  
        throw new RuntimeException("Ill formed list\"" + name + "\"."); 
      }
    }
    throw new RuntimeException("List\"" + name + "\" was not declared.");
  }
}
