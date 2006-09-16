package ted;

//import java.io.*;
import aterm.*;
import aterm.pure.PureFactory;
//import java.util.*;
//import java.lang.reflect.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.*;
import tom.library.strategy.mutraveler.MuStrategy;
 

public class VisitableViewer {

  %include { atermmapping.tom }
  %include { mutraveler.tom }

  // for parsing
  private static ATermFactory atermFactory = new PureFactory();

  public static void VisitableToDot(jjtraveler.Visitable v) {
    ATerm at = atermFactory.parse(v.toString());
    ATermToDot(at);
  }

  public static void ATermToDot(ATerm term) {
    System.out.println("digraph g { graph [ordering=out];");
    ATermToDotRec("pos_", 0, term);
    System.out.println("}");
  }

  private static void ATermToDotRec(String pos, int n, ATerm term) {
    pos = pos + n;
    %match(ATerm term) {
      a@ATermAppl(AFun[name=name],list) -> {
        System.out.println(%[@pos@ [label="@`name@"]; ]%);
        int i=0;
        for(; !`list.isEmpty(); `list = `list.getNext()) {
          ATerm t = `list.getFirst(); i++;
          System.out.println(%[@pos@ -> @pos+i@;]%);
          ATermToDotRec(pos,i,t);
        }
      }
    }
  }
}
