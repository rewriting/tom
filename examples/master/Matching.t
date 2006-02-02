package master;

import aterm.pure.SingletonFactory;
import master.matching.term.*;
import master.matching.term.types.*;

class Matching {
  private TermFactory factory;

  %vas {
    // ...
    module Term
    imports 
    public
    sorts Term TermList
      
    abstract syntax
    Variable(name:String)               -> Term
    Appl(name:String, args:TermList)    -> Term
    cons(head:Term,tail:TermList )      -> TermList
    nil                                 -> TermList
    True 																-> Term
    False																-> Term
    Match(pattern:Term, subject:Term) 	-> Term
    And(a1:Term, a2:Term) 							-> Term
    decomposeList(l1:TermList, l2:TermList) -> Term
  }

  %rule {
    decomposeList(nil(),nil()) -> True()
    decomposeList(cons(h1,t1),cons(h2,t2)) -> And(Match(h1,h2),decomposeList(t1,t2))
  } 

  %rule {
    // Delete
    Match(Appl(name,nil()),Appl(name,nil())) -> True()
        
    // Decompose
    Match(Appl(name,a1),Appl(name,a2)) -> decomposeList(a1,a2)
        
    // SymbolClash
    Match(Appl(name1,args1),Appl(name2,args2)) -> False() //if name1 != name2
  }

  %rule {
    // PropagateClash
    And(False(),_) -> False()
    And(_,False()) -> False()

    // PropagateSuccess
    And(True(),x) -> x
    And(x,True()) -> x
    And(x,x) -> x
  }

  //-------------------------------------------------------
  public Matching() {
    this.factory = TermFactory.getInstance(SingletonFactory.getInstance());
  } 

  public TermFactory getTermFactory() {
    return factory;
  }

  public void run() {
    Term p1 = `Appl("f",cons(Variable("x"),nil));
    Term s1 = `Appl("f",cons(Appl("a",nil),nil));
    
    Term p2 = `Appl("f",cons(Variable("x"),cons(Appl("g",cons(Variable("y"),nil)),nil)));
    Term s2 = `Appl("f",cons(Appl("a",nil),cons(Appl("g",cons(Appl("b",nil),nil)),nil)));

    Term p3 = `Appl("f",cons(Appl("a",nil),cons(Appl("g",cons(Variable("y"),nil)),nil)));
    Term s3 = `Appl("f",cons(Appl("b",nil),cons(Appl("g",cons(Appl("b",nil),nil)),nil)));

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
    Matching test = new Matching();
    test.run();
  }


}
