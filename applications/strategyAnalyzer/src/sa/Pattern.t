package sa;

import sa.rule.types.*;
import tom.library.sl.*;

public class Pattern {
  %include { rule/Rule.tom }
  %include { sl.tom }

  private static Signature eSig = new Signature();
  private static Signature gSig = new Signature();

  public static void main(String args[]) {
    Term V = `Var("_");
    Term X = `Var("x");
    Term Y = `Var("y");
    Term t = null;
    GomType type = null;

    // example 1
    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("h", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    gSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    gSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    gSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    gSig.addSymbol("f", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("h", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());
    Term ga =`Appl("g", TermList(a));
    Term gv =`Appl("g", TermList(V));

    Term fa =`Appl("f", TermList(a));
    Term fga =`Appl("f", TermList(ga));
    Term fgv =`Appl("f", TermList(gv));
    Term fv =`Appl("f", TermList(V));

    Term hxy =`Appl("h", TermList(X,Y));
    Term hab =`Appl("h", TermList(a,b));
    Term hba =`Appl("h", TermList(b,a));
    Term fhxy =`Appl("f", TermList(hxy));
    Term fhab =`Appl("f", TermList(hab));
    Term fhba =`Appl("f", TermList(hba));

    // f(g(x)) \ ( f(a) + f(g(a)) )
    //t = `Sub(fgv, Add(TermList(fa,fga)));
    //t = `Sub(fv, Add(TermList(fa,fga,fgv)));

    // X \ f(a + b)
    //     t = `Sub(V, Appl("f",TermList(Add(TermList(a,b)))));
    t = `Sub(V, Add(TermList(fhba,fhab)));

//  t = (_ \ (f(h(b(),a())) + f(h(a(),b()))))
// t1 = (_ \ f((h(b(),a()) + h(a(),b()))))


    type = `GomType("T");

    // example 2
    eSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    eSig.addSymbol("Cons", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );

    gSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    gSig.addSymbol("Cons", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );
    gSig.addSymbol("sep", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );

    Term y_ys = `Appl("Cons", TermList(V,V));
    Term x_y_ys = `Appl("Cons", TermList(V,y_ys));
    Term sep1 = `Appl("sep", TermList(V,x_y_ys));
    Term sep2 = `Appl("sep", TermList(V,V));

    //t = `Sub(sep2,sep1);
    //type = `GomType("List");

    // example 3
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    eSig.addSymbol("C", `ConcGomType(GomType("Nat")), `GomType("TT") );
    eSig.addSymbol("Bound", `ConcGomType(GomType("Nat")), `GomType("TT") );
    eSig.addSymbol("Neg", `ConcGomType(GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Add", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Sub", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Mul", `ConcGomType(GomType("Nat"),GomType("TT")), `GomType("TT") );


    gSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    gSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    gSig.addSymbol("C", `ConcGomType(GomType("Nat")), `GomType("TT") );
    gSig.addSymbol("Bound", `ConcGomType(GomType("Nat")), `GomType("TT") );
    gSig.addSymbol("Neg", `ConcGomType(GomType("TT")), `GomType("TT") );
    gSig.addSymbol("Add", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    gSig.addSymbol("Sub", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    gSig.addSymbol("Mul", `ConcGomType(GomType("Nat"),GomType("TT")), `GomType("TT") );
    gSig.addSymbol("numadd", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );

    Term pat = `Appl("Add",TermList(Appl("Mul",TermList(V,Appl("Bound",TermList(V)))),V));
    Term p1 = `Appl("numadd",TermList(pat,pat));
    Term p2 = `Appl("numadd",TermList(pat,V));
    Term p3 = `Appl("numadd",TermList(V,pat));
    Term p4 = `Appl("numadd",TermList(Appl("C",TermList(V)),Appl("C",TermList(V))));
    Term p5 = `Appl("numadd",TermList(V,V));

    //t = `Sub(p2,p1);
    //t = `Sub(p3,Add(TermList(p1,p2)));
    //t = `Sub(p4,Add(TermList(p1,p2,p3)));
    //t = `Sub(p5,Add(TermList(p1,p2,p3,p4)));
    //type = `GomType("TT");



    System.out.println("pretty t = " + Pretty.toString(t));

    try {
      t = `Repeat(OnceBottomUp(Choice(PropagateEmpty(),SimplifyAdd(),SimplifySub()))).visitLight(t);
      System.out.println("t1 = " + Pretty.toString(t));

      /*
      Term oldT = null;
      while(oldT != t) {
        oldT = t;
        t = `Repeat(OnceTopDown(SimplifySub())).visitLight(t);
        System.out.println("t1 = " + Pretty.toString(t));
        t = `Repeat(OnceTopDown(ExpandSub())).visitLight(t);
        System.out.println("t2 = " + Pretty.toString(t));
        t = `Repeat(OnceTopDown(SimplifyAdd())).visitLight(t);
        System.out.println("t3 = " + Pretty.toString(t));

        //GomType type = gSig.getCodomain(t.getsymbol());
        //System.out.println("symbol = " + t.getsymbol());
        //System.out.println("type = " + type);
        t = eliminateIllTyped(t, type);
      }
      */
      t = eliminateIllTyped(t, type);

    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }

    System.out.println("res = " + Pretty.toString(t));
  }

  %strategy PropagateEmpty() extends Fail() {
    visit Term {
      Appl(f,TermList(_*,Empty(),_*)) -> {
        return `Empty();
      }
    }
  }

  %strategy SimplifyAdd() extends Fail() {
    visit Term {

      // flatten: (a + (b + c) + d) -> (a + b + c + d)
      s@Add(TermList(C1*, Add(TermList(tl*)), C2*)) -> {
        Term res = `Add(TermList(C1*,tl*,C2*));
        System.out.println("flatten1: " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }

      // Add(t) -> t
      s@Add(TermList(t)) -> {
        Term res = `t;
        System.out.println("flatten2: " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }

      s@Add(TermList()) -> {
        Term res = `Empty();
        System.out.println("elim () : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }

      // a + x + b -> x
      s@Add(TermList(_*, Var("_"), _*)) -> {
        Term res = `Var("_");
        System.out.println("a + x + b -> x : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }
      
      // t + empty -> t
      s@Add(TermList(C1*,Empty(),C2*)) -> {
        Term res = `Add(TermList(C1*,C2*));
        System.out.println("t + empty -> t : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }
      
      // t + t -> t
      //s@Add(TermList(C1*, t, C2*, t, C3*)) -> {
      //  Term res = `Add(TermList(C1*,t,C2*,C3*));
      //  System.out.println("t + t -> t : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
      //  return res;
     // }

      // f(t1,...,tn) + f(t1',...,tn') -> f(t1,..., ti + ti',...,tn)
      s@Add(TermList(C1*, Appl(f,tl1), C2*, Appl(f, tl2), C3*)) -> {
        TermList tl = `addUniqueTi(tl1,tl2);
        if(tl != null) {
          Term res = `Add(TermList(C1*, Appl(f,tl), C2*, C3*));
          System.out.println("add merge: " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
          return res;
        } else {
          System.out.println("add merge failed");
        }
      }

    }
  }

  private static boolean isPlainTerm(Term t) {
    try {
      `TopDown(PlainTerm()).visitLight(t);
    } catch(VisitFailure e) {
      return false;
    }
    return true;
  }

  %strategy PlainTerm() extends Identity() {
    visit Term {
      t@Add(_) -> {
        `Fail().visitLight(`t);
      }
    }

  }


  %strategy SimplifySub() extends Fail() {
    visit Term {
      //s@Add(TermList()) -> {
      //  Term res = `Empty();
      //  System.out.println("sub elim Add() : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
      //  return res;
     // }
      // Add(t) -> t
      //s@Add(TermList(t)) -> {
      //  Term res = `t;
      //  System.out.println("sub Add(t) -> t : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
      //  return res;
     // }

      // t - x -> empty
      Sub(t, Var("_")) -> {
        System.out.println("t - x -> empty");
        return `Empty();
      }

      // t - empty -> t
      Sub(t, Empty()) -> {
        return `t;
      }

      // t - (a1 + ... + an) -> (t - a) - (a2 + ... + an))
      s@Sub(t, Add(TermList(head,tail*))) -> {
        Term res = `Sub(Sub(t,head), Add(TermList(tail*)));
        System.out.println("sub distrib1 : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }
     
      // X - t -> expand AP
      Sub(Var("_"),t@Appl(f,args_f)) -> {
        if(isPlainTerm(`t)) {
          RuleCompiler ruleCompiler = new RuleCompiler(Pattern.eSig, Pattern.gSig);
          RuleList rl = ruleCompiler.expandAntiPatterns(`ConcRule(Rule(Anti(t),Var("_"))));
          //System.out.println("rl = " + Pretty.toString(rl));
          TermList tl = `TermList();
          %match(rl) {
            ConcRule(_*,Rule(lhs@Appl(g,args_g),rhs),_*) -> {
              Term newLhs = `TopDown(RemoveVar()).visitLight(`lhs);
              tl = `TermList(tl*,newLhs);
            }
          }
          System.out.println("expand AP");
          return `Add(tl);
        }
      }

      // empty - t -> empty
      //Sub(Empty(),t) -> {
      //  return `Empty();
      //}

      // empty - f(t1,...,tn) -> empty
      Sub(Empty(),Appl(f,tl)) -> {
        System.out.println("empty - f(...)");
        return `Empty();
      }


      // t - t -> empty
      //Sub(t, t) -> {
      //  return `Empty();
      //}

      // (a + t + b) - t -> (a + b)
      //Sub(Add(TermList(C1*,t,C2*)),t) -> {
      //  return `Add(TermList(C1*,C2*));
      //}


      // (a1 + ... + an) - b -> (a1 - b) + ( (a2 + ... + an) - b )
      //s@Sub(Add(TermList(head,tail*)), t) -> {
      //  Term res = `Add(TermList(Sub(head,t), Sub(Add(TermList(tail*)),t)));
      //  System.out.println("sub distrib2 : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
      //  return res;
      //}

      // (a1 + ... + an) - t@f(t1,...,tn) -> (a1 - t) + ( (a2 + ... + an) - t )
      s@Sub(Add(TermList(head,tail*)), t@Appl(f,tl)) -> {
        Term res = `Add(TermList(Sub(head,t), Sub(Add(TermList(tail*)),t)));
        System.out.println("sub distrib2 : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }

      // f(t1,...,tn) - g(t1',...,tm') -> f(t1,...,tn)
      s@Sub(t@Appl(f,tl1), Appl(g, tl2)) && f!=g -> {
        Term res = `t;
        System.out.println("sub elim1 : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }

      // f(t1,...,tn) - (a + g(t1',...,tm') + b) -> f(t1,...,tn) - (a + b)
      //s@Sub(t@Appl(f,tl1),Add(TermList(C1*, Appl(g, tl2), C2*))) && f!=g -> {
      //  Term res = `Sub(t, Add(TermList(C1*,C2*)));
      //  System.out.println("sub elim2 : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
      //  return res;
      // }


      // f(t1,t2) - f(t1',t2') -> f(t1, t2-t2') + f(t1-t1', t2)
      // f(t1,...,tn) - f(t1',...,tn') -> f(t1-t1',t2,...,tn) + f(t1, t2-t2',...,tn) + ... + f(t1,...,tn-tn')
      s@Sub(t1@Appl(f,tl1), t2@Appl(f, tl2)) -> {
        Term res = `sub(t1,t2);
        System.out.println("sub1 : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }

      //s@Sub(t1@Appl(f,tl1),Add(TermList(C1*, t2@Appl(f, tl2), C2*))) -> {
      //  Term res = `Sub(sub(t1,t2), Add(TermList(C1*,C2*)));
      //  System.out.println("sub2 : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
      //  return res;
      // }

    }
  }

  // f(a1,...,an) - f(b1,...,bn) -> f(a1-b1,..., an) + ... + f(a1,...,an-bn)
  private static Term sub(Term t1,Term t2) {
    %match(t1,t2) {
      Appl(f,args1), Appl(f,args2) -> {
        TermList tl1 = `args1;
        TermList tl2 = `args2;
        int len = `tl1.length();
        TermList args[] = new TermList[len];
        for(int i=0 ; i<len ; i++) {
          args[i] = `TermList();
        }
        int cpt = 0;
        while(!tl1.isEmptyTermList()) {
          Term h1 = tl1.getHeadTermList();
          Term h2 = tl2.getHeadTermList();

          for(int i=0 ; i<len ; i++) {
            TermList tl = args[i]; // cannot use [] in `
            if(i==cpt) {
              tl = `TermList(tl*, Sub(h1,h2));
            } else {
              tl = `TermList(tl*, h1);
            }
            args[i] = tl;
          }

          tl1 = tl1.getTailTermList();
          tl2 = tl2.getTailTermList();
          cpt++;
        }

        TermList sum = `TermList();
        for(int i=0 ; i<len ; i++) {
          TermList tl = args[i]; // cannot use [] in `
          sum = `TermList(sum*,Appl(f,tl));;
        }

        return `Add(sum);
      }
    }
    System.out.println("cannot sub " + t1 + " and " + t2);
    return null;
  }

  // (a1,...,an) + (b1,...,bn) -> (a1,..., ai+bi,..., an)
  // for the unique i such that ai != bi
  private static TermList addUniqueTi(TermList l1,TermList l2) {
    TermList tl1=l1;
    TermList tl2=l2;
    TermList tl = `TermList();
    int cpt = 0;
    while(!tl1.isEmptyTermList()) {
      Term h1 = tl1.getHeadTermList();
      Term h2 = tl2.getHeadTermList();
      if(h1==h2) {
        tl = `TermList(tl*, h1);
      } else {
        tl = `TermList(tl*, Add(TermList(h1,h2)));
        cpt++;
      }

      tl1 = tl1.getTailTermList();
      tl2 = tl2.getTailTermList();
    }
    if(cpt <= 1) {
      return tl;
    } else {
      System.out.println("cannot add " + l1 + " and " + l2);
      return null;
    }
  }

  %strategy ExpandSub() extends Fail() {
    visit Term {
      // x - a -> expand AP
      Sub(Var("_"),t@Appl(f,args_f)) -> {
        //GomType codomain = gSig.getCodomain(`f);
        RuleCompiler ruleCompiler = new RuleCompiler(Pattern.eSig, Pattern.gSig);
        RuleList rl = ruleCompiler.expandAntiPatterns(`ConcRule(Rule(Anti(t),Var("_"))));
        //System.out.println("rl = " + Pretty.toString(rl));
        TermList tl = `TermList();
        %match(rl) {
          ConcRule(_*,Rule(lhs@Appl(g,args_g),rhs),_*) -> {
            //if(gSig.getCodomain(`g) == codomain) {
              Term newLhs = `TopDown(RemoveVar()).visitLight(`lhs);
              tl = `TermList(tl*,newLhs);
            //}
          }
        }
        return `Add(tl);
      }
    }
  }

  %strategy RemoveVar() extends Identity() {
    visit Term {
      Var(_) -> {
        return `Var("_");
      }
    }
  }

  private static Term eliminateIllTyped(Term t, GomType type) {
    //System.out.println(Pretty.toString(t) + ":" + type.getName());
    %match(t) {
      Var("_") -> {
        return t;
      }

      Appl(f,args) -> {
        if(gSig.getCodomain(`f) == type) {
          GomTypeList types = gSig.getDomain(`f);
          TermList tail = `args;
          TermList res = `TermList();
          while(!tail.isEmptyTermList()) {
            Term head = tail.getHeadTermList();
            if(head == `Empty()) {
              return `Empty();
            }
            GomType arg_type = types.getHeadConcGomType();
            res = `TermList(res*, eliminateIllTyped(head,arg_type));
            tail = tail.getTailTermList();
            types = types.getTailConcGomType();
          }
          return `Appl(f,res);
        } else {
          return `Empty();
        }
      }

      Sub(t1,t2) -> {
        return `Sub(eliminateIllTyped(t1,type),eliminateIllTyped(t2,type));
      }

      Add(tl) -> {
        TermList tail = `tl;
        TermList res = `TermList();
        while(!tail.isEmptyTermList()) {
          Term head = tail.getHeadTermList();
          res = `TermList(res*, eliminateIllTyped(head,type));
          tail = tail.getTailTermList();
        }
        return `Add(res);
      }

    }

    System.out.println("eliminateIllTyped Should not be there: " + `t);
    return t;
  }

}
