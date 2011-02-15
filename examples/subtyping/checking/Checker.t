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

import subtyping.definition.types.*;
import tom.library.sl.*;
import org.antlr.runtime.*;

public class Checker {
	%include { sl.tom }
  %include { definition/Definition.tom }

  private static Tree tree = `SList();

  private static boolean Leq(ConstraintList cl, Type ty1, Type ty2) {
    %match{
      CList(_*,Subtype(t1,t2),_*) << cl && Atom(t1) << ty1 && Atom(t2) << ty2 -> { return true; }
    }
    return false;
  }

  private static Type findType(Context ctx, ConstraintList cl, Term t, PathTree ptree) {
    %match{
      term@Var(x) << t  && Context(_*,SigOf(x,ty@Atom(_)),_*) << ctx
      ->
      { 
        tree = `SList(tree*,Statement1("VAR",ptree,ctx,TypeOf(term,ty)));
        return `ty;
      }

      term@Fun(x,args) << t && Context(_*,SigOf(x,Composed(dom,codom@Atom(_))),_*) << ctx      
      ->
      {
        PathTree ptargs = `PTree(ptree*,Label(0));
        if (`checkList(ctx,cl,dom,args,ptargs)) {
          tree = `SList(tree*,Statement1("FUN",ptree,ctx,TypeOf(term,codom)));
          return `codom;
        }
      }
    }
    return `Atom("");
  }

  private static boolean checkList(Context ctx, ConstraintList cl, TypeList dom, TermList args, PathTree ptree) {
    %match {
      PTree(lbls*,Label(i)) << ptree -> { ptree = `PTree(lbls*,Label(i+1)); }
    }

    %match {
      TyList() << dom && TeList() << args -> { return true; }
      
      TyList(ty1,tys*) << dom && TeList(t1,ts*) << args &&
      new_ty1@!Atom("") << Type findType(ctx,cl,t1,ptree)
      ->
      { 
        if (`Leq(cl,new_ty1,ty1)) {
          %match{
            (new_ty1 != ty1) && Atom(new_ty1name) << new_ty1 && Atom(ty1name) << ty1
            ->
            {
              tree = `SList(tree*,Statement1("SUB",ptree,ctx,TypeOf(t1,ty1)));
              PathTree new_ptree = `PTree(ptree*,Label(1));
              tree = `SList(tree*,Statement2("TO PROVE",new_ptree,ctx,Subtype(new_ty1name,ty1name))); 
              new_ptree = `PTree(ptree*,Label(2));
              return `checkList(ctx,cl,tys*,ts*,new_ptree);
            }
          }
          //if new_ty1 == ty1
          //new_ptree = `PTree(ptree*,Label(2));
          return `checkList(ctx,cl,tys*,ts*,ptree);
        }
        else 
          //Error when a tree derivation is not founded
          throw new RuntimeException("TYPE ERROR : The type infered to <<" + `t1 + ">> was <<" + `ty1 + ">> but the type expected was <<" + `new_ty1 + ">>.\n");
      }
    }
    return false;
  }

  public static Tree check(Context ctx, ConstraintList cl, Term t, Type ty) {
    Type new_ty = `findType(ctx,cl,t,PTree(Label(1)));
    %match {
      //Error when a tree derivation is founded but the inital term has not a good type.
      !Atom("") << new_ty 
      ->
      {
        if (Leq(cl,new_ty,ty))
          return tree;
        else
          throw new RuntimeException("TYPE ERROR : The type infered to <<" + t + ">> was <<" + ty + ">> but the type expected was <<" + new_ty + ">>.\n");
       }
    }
    throw new RuntimeException("TYPE ERROR!!");
  }
}
