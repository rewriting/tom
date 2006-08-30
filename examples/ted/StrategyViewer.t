package ted;

import java.io.*;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.MuVar;
import tom.library.strategy.mutraveler.Mu;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.*;

public class StrategyViewer {

  %include{mustrategy.tom}
  %typeterm Writer { implement {java.io.Writer} }

  private static int counter = 0;  
  static private String clean(String s) {
    s = s.replace('.','_');
    s = s.replace('$','_');
    s = s.replace('@','_');
    return s;
  }

  %strategy toDot(out : Writer) extends `Identity() {
    visit Strategy {
      y@MuVar[] -> { return `y; }

      x@Mu[] -> { ((Mu)`x).expand(); } // to mu-expand

      x -> {
        String[] tab = `x.getClass().getName().split("\\.");
        String name = tab[tab.length-1];
        tab = name.split("\\$");
        name = tab[tab.length-1];
        String idNode = `clean(x.toString());

        try {
          out.write(%[@idNode@ [label="@name@"];]%);
          out.write("\n");  

          int n = `x.getChildCount();
          for(int i=0; i<n; i++) {
            Visitable s = `x.getChildAt(i);
            %match(Strategy s) {
              y@MuVar[var=varName] -> {
                MuStrategy pointer = (MuStrategy) `((MuVar)y).getInstance();
                String idMu = clean(pointer.toString());
                if (pointer.getChildCount() > 0 && pointer.getChildAt(0) != `y) {
                  String idMuVar = idMu + "_" + (counter++);
                  out.write(%[
                      @idMuVar@ [label="@`varName@"]; 
                      @idNode@ -> @idMuVar@;
                      @idMuVar@ -> @idMu@;
                      ]%);
                }
                continue;
              }

              y -> {
                out.write(%[@idNode@ -> @clean(`y.toString())@;]%);
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
    System.out.println("digraph G { graph [ordering=out];");
    `TopDown(toDot(w)).apply(s);
    System.out.println("}\n");
  }

  public static void main(String[] args) {
    //MuStrategy strat = `mu(MuVar("x"),Sequence(All(MuVar("x")),Identity()));
    //MuStrategy strat = `Sequence(InnermostId(ChoiceId(RepeatId(R()),R())), InnermostId( ChoiceId( Sequence(RepeatId(R()), RepeatId(SequenceId(ChoiceId(R(),R()),OnceTopDownId(R())))), SequenceId(R(),OnceTopDownId(RepeatId(R()))))));

    MuStrategy strat = `Choice(
	mu(MuVar("x"),TopDownCollect(S(MuVar("x")))),
	mu(MuVar("y"),TopDownCollect(S(MuVar("y")))));

    stratToDot(strat);
  }

  %strategy R() extends Identity() {
    visit Strategy {
      x -> { return `x; }
    }
  }
  %strategy S(s:Strategy) extends Identity() {
    visit Strategy {
      x -> { return `x; }
    }
  }
}
