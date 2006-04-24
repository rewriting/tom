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
         | And(l:TermList)
    TermList = conc( Term* )
    //decomposeList(l1:TermList, l2:TermList) -> TermList
  }

  private TermList decomposeList(TermList l1, TermList l2) {
    %match(TermList l1, TermList l2) {
      conc(),conc() -> { return `conc(True()); }
      conc(h1,t1*),conc(h2,t2*) -> {
        TermList t3 = `decomposeList(t1,t2);
        return `conc(Match(h1,h2),t3*);
      }
    }
    return `conc();
  }
/*
  %rule {
    decomposeList(conc(),conc()) -> conc()
    decomposeList(conc(h1,t1*),conc(h2,t2*)) -> conc(Match(h1,h2),t3*) where t3:=decomposeList(t1,t2)
  }
*/
  %rule {
    // Delete
    Match(Appl(name,conc()),Appl(name,conc())) -> True()

    // Decompose
    Match(Appl(name,a1),Appl(name,a2)) -> And(decomposeList(a1,a2))

    // SymbolClash
    Match(Appl(name1,args1),Appl(name2,args2)) -> False()
  }

  %rule {
    // PropagateClash
    And(conc(_*,False(),_*)) -> False()
    // PropagateSuccess
    And(conc(X*,True(),Y*)) -> And(conc(X*,Y*))
    And(conc(X*,True(),Y*)) -> And(conc(X*,Y*))
    And(conc(X*,c,Y*,c,Z*)) -> And(conc(X*,c,Y*,Z*))
    And(conc()) -> True()
  }

  //-------------------------------------------------------

  public void run() {
    Term p1 = `Appl("f",conc(Variable("x")));
    Term s1 = `Appl("f",conc(Appl("a",conc())));

    Term p2 = `Appl("f",conc(Variable("x"),Appl("g",conc(Variable("y")))));
    Term s2 = `Appl("f",conc(Appl("a",conc()),Appl("g",conc(Appl("b",conc())))));

    Term p3 = `Appl("f",conc(Appl("a",conc()),Appl("g",conc(Variable("y")))));
    Term s3 = `Appl("f",conc(Appl("b",conc()),Appl("g",conc(Appl("b",conc())))));

    System.out.println("running...");
    System.out.println("p1 = " + p1);
    System.out.println("s1 = " + s1);
    System.out.println("match(p1,s1) = " + `Match(p1,s1));
    System.out.println("p2 = " + p2);
    System.out.println("s2 = " + s2);
    System.out.println("match(p2,s2) = " + `Match(p2,s2));
    System.out.println("match(p3,s3) = " + `Match(p3,s3));
  }

  public final static void main(String[] args) {
    Matching2 test = new Matching2();
    test.run();
  }

}
