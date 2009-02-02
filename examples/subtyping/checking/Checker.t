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

  private static Type typeOf(Context ctx, ConstraintList cl, Term t, PathTree ptree) {
    %match{
      term@Var(x) << t  && Context(_*,SigOf(x,ty@Atom(_)),_*) << ctx
      ->
      { 
        tree = `SList(tree*,Statement("VAR",ptree,ctx,TypeOf(term,ty)));
        return `ty;
      }

      term@Fun(x,args) << t && Context(_*,SigOf(x,Composed(dom,codom@Atom(_))),_*) << ctx      
      ->
      {
        PathTree ptargs = `PTree(ptree*,Label(0));
        if (`checkList(ctx,cl,dom,args,ptargs)) {
          tree = `SList(tree*,Statement("FUN",ptree,ctx,TypeOf(term,codom)));
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
      new_ty1@!Atom("") << Type typeOf(ctx,cl,t1,ptree)
      ->
      { 
        if (`Leq(cl,new_ty1,ty1))
          return `checkList(ctx,cl,tys*,ts*,ptree);
        else 
          //Error when a tree derivation is not founded
          throw new RuntimeException("TYPE ERROR : The type infered to <<" + `t1 + ">> was <<" + `ty1 + ">> but the type expected was <<" + `new_ty1 + ">>.\n");
      }
    }
    return false;
  }

  public static Tree check(Context ctx, ConstraintList cl, Term t, Type ty) {
    Type new_ty = `typeOf(ctx,cl,t,PTree(Label(1)));
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
