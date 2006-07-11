package ted;

import java.io.*;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.MuVar;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.*;

public class StrategyViewer {

  %include{mustrategy.tom}
  %typeterm Writer { implement {java.io.Writer} }
  
  static private String clean(String s) {
    s = s.replaceAll("\\.","");
    return s.replaceAll("@","");
  }
  
  %strategy toDot(out : Writer) extends `Identity() {
    visit Strategy {
      y@MuVar[] -> { return `y; }

      x@Mu[] -> { `x.apply(`Identity()); } // to mu-expand

      x -> {
        String[] tab = `x.getClass().getName().split("\\.");
        String name = tab[tab.length-1];
        String id = `clean(x.toString());
       
        try {
          out.write(%[@`id@ [label="@`name@"];]%);
          out.write("\n");  

          int n = `x.getChildCount();
          for(int i=0; i<n; i++) {
            Visitable s = `x.getChildAt(i);
            %match(Strategy s) {
              y@MuVar[] -> {
                MuStrategy pointer = (MuStrategy) `((MuVar)y).getInstance();
                if (pointer.getChildCount() > 0 && pointer.getChildAt(0) != `y) {
                  out.write(%[@`id@ -> @`clean(pointer.toString())@;]%);
                  out.write("\n");
                }
                continue;
              }
              y -> {
                out.write(%[@`id@ -> @`clean(y.toString())@;]%);
                out.write("\n");
              }
            }
          }
          out.flush();
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void stratToDot(MuStrategy s) {
    //s.apply(`Identity()); // to expand
    Writer w = new BufferedWriter(new OutputStreamWriter(System.out)); 
    System.out.println("digraph G {");
    `TopDown(toDot(w)).apply(s);
     System.out.println("}\n");
  }
  
  public static void main(String[] args) {
    //MuStrategy strat = `mu(MuVar("x"),Sequence(All(MuVar("x")),Identity()));
    MuStrategy strat = `Sequence(InnermostId(ChoiceId(RepeatId(Fail()),Fail())), InnermostId( ChoiceId( Sequence(RepeatId(Fail()), RepeatId(SequenceId(ChoiceId(Fail(),Fail()),OnceTopDownId(Fail())))), SequenceId(Fail(),OnceTopDownId(RepeatId(Fail()))))));

   //MuStrategy strat = `SequenceId(Fail(),OnceTopDownId(RepeatId(Fail())));

    stratToDot(strat);
  }
}
