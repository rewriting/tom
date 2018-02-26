# Term-Graph Rewriting

Using the option `--termgraph`, it is possible to automatically generate from a signature the extended version for term-graphs. As the terms are implemented with maximal sharing, so are the term-graphs. Term-graphs can be specified using label constructors.

```java
import testlist.m.types.*;
import testlist.m.*;
import tom.library.sl.*;
import tom.library.utils.Viewer;

public class TestList {

  %include{sl.tom}

  %gom(--termgraph) {
    module m
      abstract syntax

      Term = a()
           | b()
           | c()
           | d()

      List = doublelinkedlist(previous:List,element:Term,next:List)
           | nil()
           | insert(element:Term,list:List)

      sort List: graphrules(Insert,Identity) {
       insert(x1,v:doublelinkedlist(p,x2,n)) -> l:doublelinkedlist(p,x1,v:doublelinkedlist(&l,x2,n))
      }
  }

  public static void main(String[] args) {

    List abcd = (List)
     `LabList("1", doublelinkedlist(nil(),a(),
       LabList("2",insert(b(),
         LabList("3",doublelinkedlist(RefList("1"),c(),
           doublelinkedlist(RefList("3"),d(),nil()))))))).expand();
    System.out.println("Original subject");
    Viewer.display(abcd);
    System.out.println("Insertion with term-graph rules from Gom");
    try {
      Viewer.display(`TopDown(List.Insert()).visit(abcd));
    } catch(VisitFailure e) {
      System.out.println("Unexcepted failure!");
    }
  }

}
```

Users can define a system of term-graph rules and it is automatically compiled in a basic strategy. These term-graph rewriting rules can then be integrated in a more complex strategy using strategy combinators. As a consequence, all features are available for term-graphs.
