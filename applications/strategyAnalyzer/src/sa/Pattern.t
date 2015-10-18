package sa;

import sa.rule.types.*;
import tom.library.sl.*;
import java.util.HashSet;

public class Pattern {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/HashSet.tom }
  %typeterm Signature { implement { Signature } }



  public static void main(String args[]) {
    //example1();
    //example2();
    //example3();
    example4();
  }

  /*
   * Transform a list of ordered patterns into a TRS
   */
  private static TermList trs(TermList orderedPatterns, Signature eSig, Signature gSig) {
    TermList tl = `TermList();
    %match(orderedPatterns) {
      TermList(C1*,p,C2*) -> {
        tl = `TermList(tl*, Sub(p,Add(TermList(C1*))));
      }
    }
    Term t = `Add(tl);

    try {
      Strategy S1 = `Choice(EmptyAdd2Empty(),PropagateEmpty(),ElimEmpty(),DistributeAdd(),SimplifySub(eSig,gSig));
      Strategy S2 = `Choice(EmptyAdd2Empty(),PropagateEmpty(),SimplifyAdd());

      //t =  `Repeat(OnceBottomUp(S1)).visitLight(t);
      t =  `Innermost(S1).visitLight(t);
      System.out.println("NO SUBs = " + Pretty.toString(t));

      //t = `Repeat(OnceBottomUp(S2)).visitLight(t);
      t = `Innermost(S2).visitLight(t);
      System.out.println("NO ADD = " + Pretty.toString(t));

    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }


    HashSet<Term> c = new HashSet<Term>();
    expandAdd(c,t);

    tl = `TermList();
    for(Term e:c) {
      tl = `TermList(e,tl*);
      System.out.println(Pretty.toString(e));
    }

    //System.out.println("res = " + Pretty.toString(tl));

    System.out.println("size = " + c.size());

    return tl;
  }



  %strategy PropagateEmpty() extends Fail() {
    visit Term {
      Appl(f,TermList(_*,Empty(),_*)) -> {
        return `Empty();
      }
    }
  }


  %strategy EmptyAdd2Empty() extends Fail() {
    visit Term {
      s@Add(TermList()) -> {
        Term res = `Empty();
        System.out.println("elim () : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }
    }
  }

  %strategy ElimEmpty() extends Fail() {
    visit Term {
      // t + empty -> t
      s@Add(TermList(C1*,Empty(),C2*)) -> {
        Term res = `Add(TermList(C1*,C2*));
        System.out.println("t + empty -> t : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
      }
    }
  }

  %strategy DistributeAdd() extends Fail() {
    visit Term {
      // f(t1,..., ti + ti',...,tn)  ->  f(t1,...,tn) + f(t1',...,tn')
      s@Appl(f, TermList(C1*, Add(TermList(u,v)), C2*)) -> {
        Term res = `Add(TermList(Appl(f, TermList(C1*,u,C2*)), Appl(f,TermList(C1*,v,C2*))));
        System.out.println("distribute add: " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
        return res;
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

//       s@Add(TermList()) -> {
//         Term res = `Empty();
//         System.out.println("elim () : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
//         return res;
//       }

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
      // all but one ti, ti' should be identical
      s@Add(TermList(C1*, Appl(f,tl1), C2*, Appl(f, tl2), C3*)) -> {
        TermList tl = `addUniqueTi(tl1,tl2);
        if(tl != null) {
          Term res = `Add(TermList(C1*, Appl(f,tl), C2*, C3*));
          System.out.println("add merge: " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
          return res;
        } else {
          //System.out.println("add merge failed");
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


  %strategy SimplifySub(eSig:Signature,gSig:Signature) extends Fail() {
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
          RuleCompiler ruleCompiler = new RuleCompiler(`eSig, `gSig);
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
          Term res = `Add(tl);
          GomType codomain = gSig.getCodomain(`f);
          res = eliminateIllTyped(res, codomain, `gSig);
          return res;
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
      //System.out.println("cannot add " + l1 + " and " + l2);
      return null;
    }
  }

  %strategy ExpandSub(eSig:Signature,gSig:Signature) extends Fail() {
    visit Term {
      // x - a -> expand AP
      Sub(Var("_"),t@Appl(f,args_f)) -> {
        //GomType codomain = gSig.getCodomain(`f);
        RuleCompiler ruleCompiler = new RuleCompiler(`eSig, `gSig);
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

  private static Term eliminateIllTyped(Term t, GomType type, Signature gSig) {
    //System.out.println(Pretty.toString(t) + ":" + type.getName());
    %match(t) {
      Var("_") -> {
        return t;
      }

      Appl(f,args) -> {
        if(gSig.getCodomain(`f) == type) {
          GomTypeList domain = gSig.getDomain(`f);
          TermList tail = `args;
          TermList new_args = `TermList();
          while(!tail.isEmptyTermList()) {
            Term head = tail.getHeadTermList();
            GomType arg_type = domain.getHeadConcGomType();
            if(head == `Empty()) {
              // propagate Empty for any term which contains Empty
              return `Empty();
            }
            new_args = `TermList(new_args*, eliminateIllTyped(head,arg_type,gSig));
            tail = tail.getTailTermList();
            domain = domain.getTailConcGomType();
          }
          return `Appl(f,new_args);
        } else {
          return `Empty();
        }
      }

      Sub(t1,t2) -> {
        return `Sub(eliminateIllTyped(t1,type,gSig),eliminateIllTyped(t2,type,gSig));
      }

      Add(tl) -> {
        TermList tail = `tl;
        TermList res = `TermList();
        while(!tail.isEmptyTermList()) {
          Term head = tail.getHeadTermList();
          res = `TermList(res*, eliminateIllTyped(head,type,gSig));
          tail = tail.getTailTermList();
        }
        return `Add(res);
      }

    }

    System.out.println("eliminateIllTyped Should not be there: " + `t);
    return t;
  }

  /*
   * Transform a term which contains Add into a set of plain terms
   */
  private static void expandAdd(HashSet<Term> c, Term subject) {
    HashSet<Term> todo = new HashSet<Term>();
    todo.add(subject);

    while(todo.size() > 0) {
      HashSet<Term> todo2 = new HashSet<Term>();
      for(Term t:todo) {
        HashSet<Term> tmpC = new HashSet<Term>();
        try {
          /*
           * TopDownCollect: apply s1 in a top-down way, s should extends the identity
           * a failure stops the top-down process under this current node
           */
          `TopDownCollect(ExpandAdd(tmpC,t)).visit(t);
        } catch(VisitFailure e) {
        }
        for(Term e:tmpC) {
          if(isPlainTerm(e)) {
            c.add(e);
          } else {
            todo2.add(e);
          }
        }
      }
      todo = todo2;
      //System.out.println("size(c) = " + c.size());
    }

  }

  %strategy ExpandAdd(c:HashSet, subject:Term) extends Identity() {
    visit Term {
      Add(TermList(_*,t,_*)) -> {
        Term newt = (Term) getEnvironment().getPosition().getReplace(`t).visit(subject);
        c.add(newt);
      }
      
      Add(TermList(head,tail*)) -> {
        // remove the term which contains Add(TermList(...))
        c.remove(`subject);
        // fails to stop the TopDownCollect, and thus do not expand deeper terms
        `Fail().visit(`subject);
      }
    }
  }

  /*
   * examples
   */
  private static void example1() {
    Signature eSig = new Signature();
    Signature gSig = new Signature();

    Term V = `Var("_");
    Term X = `Var("x");
    Term Y = `Var("y");

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
    //t = `Sub(V, Add(TermList(fhba,fhab)));

//  t = (_ \ (f(h(b(),a())) + f(h(a(),b()))))
// t1 = (_ \ f((h(b(),a()) + h(a(),b()))))

    //type = `GomType("T");
    TermList res1 = `trs(TermList(fhba,fhab,V),eSig,gSig);

  }
  
  private static void example2() {
    Signature eSig = new Signature();
    Signature gSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    eSig.addSymbol("Cons", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );

    gSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    gSig.addSymbol("Cons", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );
    gSig.addSymbol("sep", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );

    Term y_ys = `Appl("Cons", TermList(V,V));
    Term x_y_ys = `Appl("Cons", TermList(V,y_ys));
    Term sep1 = `Appl("sep", TermList(V,x_y_ys));
    Term sep2 = `Appl("sep", TermList(V,V));

    TermList res2 = `trs(TermList(sep1,sep2),eSig,gSig);

  }

  private static void example3() {
    Signature eSig = new Signature();
    Signature gSig = new Signature();

    Term V = `Var("_");
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

    TermList res3 = `trs(TermList(p1,p2,p3,p4,p5),eSig,gSig);

  }

  private static void example4() {
    Signature eSig = new Signature();
    Signature gSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("True", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("False", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    eSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    eSig.addSymbol("Cons", `ConcGomType(GomType("Val"),GomType("List")), `GomType("List") );
    eSig.addSymbol("Nv", `ConcGomType(GomType("Nat")), `GomType("Val") );
    eSig.addSymbol("Nb", `ConcGomType(GomType("Bool")), `GomType("Val") );
    eSig.addSymbol("Undef", `ConcGomType(), `GomType("Val") );


    gSig.addSymbol("True", `ConcGomType(), `GomType("Bool") );
    gSig.addSymbol("False", `ConcGomType(), `GomType("Bool") );
    gSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    gSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    gSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    gSig.addSymbol("Cons", `ConcGomType(GomType("Val"),GomType("List")), `GomType("List") );
    gSig.addSymbol("Nv", `ConcGomType(GomType("Nat")), `GomType("Val") );
    gSig.addSymbol("Nb", `ConcGomType(GomType("Bool")), `GomType("Val") );
    gSig.addSymbol("Undef", `ConcGomType(), `GomType("Val") );

    gSig.addSymbol("interp", `ConcGomType(GomType("Nat"),GomType("List")), `GomType("Val") );

    Term nat0 = `Appl("Z",TermList());
    Term nat1 = `Appl("S",TermList(nat0));
    Term nat2 = `Appl("S",TermList(nat1));
    Term nat3 = `Appl("S",TermList(nat2));
    Term nat4 = `Appl("S",TermList(nat3));
    Term nat5 = `Appl("S",TermList(nat4));
    Term nat6 = `Appl("S",TermList(nat5));
    Term nv = `Appl("Nv",TermList(V));
    Term bv = `Appl("Bv",TermList(V));
    Term nil = `Appl("Nil",TermList(V));

    Term p0 = `Appl("interp",TermList(nat0,nil));
    Term p1 = `Appl("interp",TermList(nat1,Appl("Cons",TermList(nv,nil))));
    Term p2 = `Appl("interp",TermList(nat2,Appl("Cons",TermList(nv,Appl("Cons",TermList(nv,nil))))));
    Term p3 = `Appl("interp",TermList(nat3,nil));
    Term p4 = `Appl("interp",TermList(nat4,Appl("Cons",TermList(bv,nil))));
    Term p5 = `Appl("interp",TermList(nat5,Appl("Cons",TermList(bv,Appl("Cons",TermList(bv,nil))))));
    Term p6 = `Appl("interp",TermList(nat6,Appl("Cons",TermList(nv,Appl("Cons",TermList(nv,nil))))));
    Term p7 = `Appl("interp",TermList(V,V));

    TermList res4 = `trs(TermList(p0,p1,p2,p3,p4,p5,p6,p7),eSig,gSig);

  }


}
