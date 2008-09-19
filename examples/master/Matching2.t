package master;
import master.matching2.term.types.*;

class Matching2 {
  %gom {
    module Term
    imports String
    abstract syntax
    Term = Variable(name:String)
         | Appl(name:String, args:TermList)
         | True()
         | False()
         | Match(pattern:Term, subject:Term)
         | And(Term*)
         | decomposeList(l1:TermList, l2:TermList)

    TermList = conc( Term* )
 
    And:Free() {}
    module Term:rules() {
     // Delete
     Match(Appl(name,conc()),Appl(name,conc())) -> True()

     // Decompose
     Match(Appl(name,arg1),Appl(name,arg2)) -> decomposeList(arg1,arg2)

     // SymbolClash
     Match(Appl(name1,arg1),Appl(name2,arg2)) -> False()

     // PropagateClash
     And(_*,False(),_*) -> False()
     // PropagateSuccess
     And(True(),Y*) -> Y*
     And(c,Y*,c,Z*) -> And(c,Y*,Z*)
     //And(X*,True(),Y*) -> And(X*,Y*)
     //And(X*,c,Y*,c,Z*) -> And(X*,c,Y*,Z*)
     And(x) -> x
     //And() -> True()

     decomposeList(conc(),conc()) -> True()
     decomposeList(conc(h1,t1*),conc(h2,t2*)) -> And(Match(h1,h2),decomposeList(t1,t2))
    }
  }

  //-------------------------------------------------------

  public final static void main(String[] args) {
    Term p1 = `Appl("f",conc(Variable("x")));
    Term s1 = `Appl("f",conc(Appl("a",conc())));

    Term p2 = `Appl("f",conc(Variable("x"),Appl("g",conc(Variable("y")))));
    Term s2 = `Appl("f",conc(Appl("a",conc()),Appl("g",conc(Appl("b",conc())))));

    Term p3 = `Appl("f",conc(Appl("a",conc()),Appl("g",conc(Variable("y")))));
    Term s3 = `Appl("f",conc(Appl("a",conc()),Appl("g",conc(Appl("b",conc())))));

    System.out.println("running...");
    System.out.println("p1 = " + p1);
    System.out.println("s1 = " + s1);
    System.out.println("match(p1,s1) = " + `Match(p1,s1));
    System.out.println("p2 = " + p2);
    System.out.println("s2 = " + s2);
    System.out.println("match(p2,s2) = " + `Match(p2,s2));
    System.out.println("match(p3,s3) = " + `Match(p3,s3));
  }

}

