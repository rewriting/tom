package sa;

import sa.rule.types.*;
import tom.library.sl.*;
import java.util.HashSet;
import java.util.Set;

public class Pattern {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/HashSet.tom }
  %typeterm Signature { implement { Signature } }

  public static void main(String args[]) {
//     example1();
    // example2();
//     example3(); // numadd
//       example4(); // interp
//     example5(); // balance
//     example6(); // and-or
//    example7(); // simplest reduce
//     example7bis(); // simplest reduce with one type
//    example8(); // reduce deeper
//   example9(); // nested anti-pattern
//   example10(); // nested anti-pattern
   example11(); // exact cover
  }

  /*
   * examples
   */
  private static void example1() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    Term X = `Var("x");
    Term Y = `Var("y");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("h", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());
    Term ga =`Appl("g", TermList(a));
    Term gv =`Appl("g", TermList(V));

    Term fa =`Appl("f", TermList(a));
    Term fga =`Appl("f", TermList(ga));
    Term fgv =`Appl("f", TermList(gv));
    Term fv =`Appl("f", TermList(V));

    Term hxy =`Appl("h", TermList(V,V));
    Term hab =`Appl("h", TermList(a,b));
    Term hba =`Appl("h", TermList(b,a));
    Term fhxy =`Appl("f", TermList(hxy));
    Term fhab =`Appl("f", TermList(hab));
    Term fhba =`Appl("f", TermList(hba));

    // f(g(x)) \ ( f(a) + f(g(a)) )
    //t = `Sub(fgv, Add(ConcAdd(fa,fga)));
    //t = `Sub(fv, Add(ConcAdd(fa,fga,fgv)));

    // X \ f(a + b)
    //     t = `Sub(V, Appl("f",TermList(Add(ConcAdd(a,b)))));
    //t = `Sub(V, Add(ConcAdd(fhba,fhab)));

//  t = (_ \ (f(h(b(),a())) + f(h(a(),b()))))
// t1 = (_ \ f((h(b(),a()) + h(a(),b()))))

    //type = `GomType("T");
    Term r0 = `Appl("rhs0",TermList());
    Term r1 = `Appl("rhr1",TermList());
    Term r2 = `Appl("rhs2",TermList());

    Trs res = RewriteSystem.trsRule(`Otrs(ConcRule(Rule(fhba,r0), Rule(fhab,r1), Rule(V,r2))), eSig);
  }

  private static void example2() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    eSig.addSymbol("Cons", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );
    eSig.addSymbol("sep", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );
    eSig.setFunction("sep");

    Term y_ys = `Appl("Cons", TermList(V,V));
    Term x_y_ys = `Appl("Cons", TermList(V,y_ys));
    Term p0 = `Appl("sep", TermList(V,x_y_ys));
    Term p1 = `Appl("sep", TermList(V,V));

    Term r0 = `Appl("rhs0",TermList());
    Term r1 = `Appl("rhs1",TermList());

    Trs res = RewriteSystem.trsRule(`Otrs(ConcRule(Rule(p0,r0), Rule(p1,r1))), eSig);

  }

  private static void example3() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    eSig.addSymbol("C", `ConcGomType(GomType("Nat")), `GomType("TT") );
    eSig.addSymbol("Bound", `ConcGomType(GomType("Nat")), `GomType("TT") );
    eSig.addSymbol("Neg", `ConcGomType(GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Add", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Sub", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Mul", `ConcGomType(GomType("Nat"),GomType("TT")), `GomType("TT") );
    eSig.addSymbol("numadd", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    eSig.setFunction("numadd");

    Term pat = `Appl("Add",TermList(Appl("Mul",TermList(V,Appl("Bound",TermList(V)))),V));
    Term p1 = `Appl("numadd",TermList(pat,pat));
    Term p2 = `Appl("numadd",TermList(pat,V));
    Term p3 = `Appl("numadd",TermList(V,pat));
    Term p4 = `Appl("numadd",TermList(Appl("C",TermList(V)),Appl("C",TermList(V))));
    Term p5 = `Appl("numadd",TermList(V,V));

    Term r1 = `Appl("rhs1",TermList());
    Term r2 = `Appl("rhs2",TermList());
    Term r3 = `Appl("rhs3",TermList());
    Term r4 = `Appl("rhs4",TermList());
    Term r5 = `Appl("rhs5",TermList());

    Trs res = RewriteSystem.trsRule(`Otrs(ConcRule(Rule(p1,r1), Rule(p2,r2), Rule(p3,r3), Rule(p4,r4), Rule(p5,r5))), eSig);

  }

  private static void example4() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("True", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("False", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    eSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    eSig.addSymbol("Cons", `ConcGomType(GomType("Val"),GomType("List")), `GomType("List") );
    eSig.addSymbol("Nv", `ConcGomType(GomType("Nat")), `GomType("Val") );
    eSig.addSymbol("Bv", `ConcGomType(GomType("Bool")), `GomType("Val") );
    eSig.addSymbol("Undef", `ConcGomType(), `GomType("Val") );

    eSig.addSymbol("interp", `ConcGomType(GomType("Nat"),GomType("List")), `GomType("Val") );
    eSig.setFunction("interp");

    Term nat0 = `Appl("Z",TermList());
    Term nat1 = `Appl("S",TermList(nat0));
    Term nat2 = `Appl("S",TermList(nat1));
    Term nat3 = `Appl("S",TermList(nat2));
    Term nat4 = `Appl("S",TermList(nat3));
    Term nat5 = `Appl("S",TermList(nat4));
    Term nat6 = `Appl("S",TermList(nat5));
    Term nv = `Appl("Nv",TermList(V));
    Term bv = `Appl("Bv",TermList(V));
    Term nil = `Appl("Nil",TermList());

    Term p0 = `Appl("interp",TermList(nat0,nil));
    Term p1 = `Appl("interp",TermList(nat1,Appl("Cons",TermList(nv,nil))));
    Term p2 = `Appl("interp",TermList(nat2,Appl("Cons",TermList(nv,Appl("Cons",TermList(nv,nil))))));
    Term p3 = `Appl("interp",TermList(nat3,nil));
    Term p4 = `Appl("interp",TermList(nat4,Appl("Cons",TermList(bv,nil))));
    Term p5 = `Appl("interp",TermList(nat5,Appl("Cons",TermList(bv,Appl("Cons",TermList(bv,nil))))));
    Term p6 = `Appl("interp",TermList(nat6,Appl("Cons",TermList(nv,Appl("Cons",TermList(nv,nil))))));
    Term p7 = `Appl("interp",TermList(V,V));

    Term r0 = `Appl("rhs0",TermList());
    Term r1 = `Appl("rhs1",TermList());
    Term r2 = `Appl("rhs2",TermList());
    Term r3 = `Appl("rhs3",TermList());
    Term r4 = `Appl("rhs4",TermList());
    Term r5 = `Appl("rhs5",TermList());
    Term r6 = `Appl("rhs6",TermList());
    Term r7 = `Appl("rhs7",TermList());
    Trs res4 = RewriteSystem.trsRule(`Otrs(ConcRule(Rule(p0,r0), Rule(p1,r1), Rule(p2,r2),
                                      Rule(p3,r3), Rule(p4,r4), Rule(p5,r5),
                                      Rule(p6,r6),Rule(p7,r7))),eSig);
    //Trs res4 = `trsRule(ConcRule(Rule(p0,r0),Rule(p1,r1), Rule(p7,r7)),eSig);

//     //interp(S(Z()),Cons(Undef(),Cons(_,_)))
//     //interp(S(Z()),Cons(Undef(),Nil()))
//     //interp(S(Z()),Cons(Undef(),_))
//     Term t1 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Appl("Cons",TermList(Var("_"),Var("_")))))));
//       Term t2 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Appl("Nil",TermList())))));
//       Term t3 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Var("_")))));

//     //`reduce(Add(ConcAdd(t1,t2,t3)),eSig);

  }

  private static Term T(Term t1, Term t2, Term t3, Term t4) {
    return `Appl("T",TermList(t1,t2,t3,t4));
  }

  private static void example5() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    eSig.addSymbol("R", `ConcGomType(), `GomType("Color") );
    eSig.addSymbol("B", `ConcGomType(), `GomType("Color") );
    eSig.addSymbol("E", `ConcGomType(), `GomType("Tree") );
    eSig.addSymbol("T", `ConcGomType(GomType("Color"), GomType("Tree"), GomType("Nat"), GomType("Tree")), `GomType("Tree"));
    eSig.addSymbol("balance", `ConcGomType(GomType("Tree")), `GomType("Tree") );
    eSig.setFunction("balance");

    Term B = `Appl("B",TermList());
    Term R = `Appl("R",TermList());

    Term p0 = `Appl("balance", TermList(T(B,T(R,T(R,V,V,V),V,V),V,V)));
    Term p1 = `Appl("balance", TermList(T(B,T(R,V,V,T(R,V,V,V)),V,V)));
    Term p2 = `Appl("balance", TermList(T(B,V,V,T(R,T(R,V,V,V),V,V))));
    Term p3 = `Appl("balance", TermList(T(B,V,V,T(R,V,V,T(R,V,V,V)))));
    Term p4 = `Appl("balance", TermList(V));

    Term r0 = `Appl("rhs0",TermList());
    Term r1 = `Appl("rhs1",TermList());
    Term r2 = `Appl("rhs2",TermList());
    Term r3 = `Appl("rhs3",TermList());
    Term r4 = `Appl("rhs4",TermList());

    Trs res = RewriteSystem.trsRule(`Otrs(ConcRule(Rule(p0,r0), Rule(p1,r1), Rule(p2,r2),
                                      Rule(p3,r3), Rule(p4,r4))), eSig);
  }

  private static void example6() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("True", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("False", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("and", `ConcGomType(GomType("Bool"),GomType("Bool")), `GomType("Bool") );
    eSig.addSymbol("or",  `ConcGomType(GomType("Bool"),GomType("Bool")), `GomType("Bool") );
    eSig.setFunction("and");
    eSig.setFunction("or");

    Term True = `Appl("True",TermList());
    Term False = `Appl("False",TermList());

    Rule r0 = `Rule(Appl("and", TermList(True,True)), True);
    Rule r1 = `Rule(Appl("and", TermList(V,V)), False);

    Rule r2 = `Rule(Appl("or", TermList(False,False)), False);
    //Rule r3 = `Rule(Appl("or", TermList(V,V)), True);
    Rule r3 = `Rule(Add(ConcAdd(
            Appl("or", TermList(False,True)),
            Appl("or", TermList(True,False)),
            Appl("or", TermList(True,True)))), True);

    Trs res = RewriteSystem.trsRule(`Otrs(ConcRule(r0,r1,r2,r3)), eSig);
  }

  private static void example7() {
    Signature eSig = new Signature();

    Term V = `Var("_");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("U") );
    eSig.setFunction("f");

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());

    Term fav =`Appl("f", TermList(a,V));
    Term fbv =`Appl("f", TermList(b,V));
    Term fva =`Appl("f", TermList(V,a));

    Term r0 = `Appl("rhs0",TermList());

    Trs res = RewriteSystem.trsRule(`Otrs(ConcRule(Rule(fav,r0), Rule(fbv,r0), Rule(fva,r0))), eSig);
  }

  private static void example7bis() {
    Signature eSig = new Signature();

    Term V = `Var("_");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());

    Term fvv =`Appl("f", TermList(V,V));
    Term fav =`Appl("f", TermList(a,V));
    Term fbv =`Appl("f", TermList(b,V));
    Term ffv =`Appl("f", TermList(fvv,V));
    Term fva =`Appl("f", TermList(V,a));

    Term r0 = `Appl("rhs0",TermList());

    Trs res = RewriteSystem.trsRule(`Otrs(ConcRule(Rule(fav,r0), Rule(ffv,r0), Rule(fbv,r0), Rule(fva,r0))), eSig);
  }


  private static void example8() {
    Signature eSig = new Signature();

    Term V = `Var("_");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("U") );

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());

    Term gv =`Appl("g", TermList(V));
    Term ga =`Appl("g", TermList(a));
    Term gb =`Appl("g", TermList(b));
    Term gg =`Appl("g", TermList(gv));

    Term fgaa =`Appl("f", TermList(ga,a));
    Term fgba =`Appl("f", TermList(gb,a));
    Term fgga =`Appl("f", TermList(gg,a));
    Term faa =`Appl("f", TermList(a,a));
    Term fba =`Appl("f", TermList(b,a));
    Term fva =`Appl("f", TermList(V,a));

    Term r0 = `Appl("rhs0",TermList());

    Trs res = RewriteSystem.trsRule(`Otrs(ConcRule(Rule(fgba,r0), Rule(fgaa,r0), Rule(fgga,r0), Rule(faa,r0), Rule(fba,r0), Rule(fva,r0))), eSig);

  }

  private static void example9() {
    Signature eSig = new Signature();
    // !f(a,!g(y)) ==> Z \ f(a, Z' \ g(y) )

    Term V = `Var("_");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());
    Term gv =`Appl("g", TermList(V));

    Term r0 = `Appl("rhs0",TermList());

    Term pattern = `Sub(V, Appl("f",TermList(a,Sub(V,gv))));
    Trs res = RewriteSystem.trsRule(`Otrs(ConcRule(Rule(pattern,r0))), eSig);
  }

  private static void example10() {
    Signature eSig = new Signature();
    // !f(x,x) ==> Z \ f(x,x)

    Term V = `Var("_");
    Term X = `Var("x");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());
    Term gv =`Appl("g", TermList(V));

    Term r0 = `Appl("rhs0",TermList());

    Term pattern = `Sub(V, Appl("f",TermList(X,X)));
    Trs res = RewriteSystem.trsRule(`Otrs(ConcRule(Rule(pattern,r0))), eSig);
  }

  private static void example11() {
    Signature eSig = new Signature();

    Term V = `Var("_");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    Term a =`Appl("a", TermList());

    Term fav =`Appl("f", TermList(a,V));
    Term fva =`Appl("f", TermList(V,a));
    Term fvv =`Appl("f", TermList(V,V));
    Term fff =`Appl("f", TermList(fvv,fvv));

    boolean b = RewriteSystem.canBeRemoved3(`Rule(fvv,a), `ConcRule(Rule(fav,a),Rule(fva,a),Rule(fff,a)), eSig);
    System.out.println("b = " + b);

  }
  
  /*
   * Reduce a list of rules
   */
  public static RuleList reduceRules(RuleList ruleList, Signature eSig) {

    // test subsumtion idea
    %match(ruleList) {
      ConcRule(C1*,rule,C2*) -> {
        //RewriteSystem.canBeRemoved1(`rule, `ConcRule(C1*,C2*), eSig);
        RewriteSystem.canBeRemoved2(`rule, `ConcRule(C1*,C2*), eSig);
      }
    }

    return ruleList;
  }
/*
  private static Term nat(int n) {
    if(n==0) {
      return `Appl("Z",TermList());
    } else {
      Term t = nat(n-1);
      return `Appl("S",TermList(t));
    }
    //return `Empty();
  }

  private static void example11() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );

    Term A = `Add(ConcAdd(nat(1),nat(4),nat(7)));

    Term r = `Appl("rhs",TermList());

    RuleList candidates = `ConcRule(Rule(A,r));
    RuleList kernel = `ConcRule();
    RuleList res = RewriteSystem.removeRedundantRule(candidates,kernel,eSig);

    for(Rule rule:`res.getCollectionConcRule()) {
      System.out.println(Pretty.toString(rule));
    }
    System.out.println("size = " + `res.length());

  }
  */
}
