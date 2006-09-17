package ted;

import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import tom.library.strategy.mutraveler.*;
import jjtraveler.VisitFailure;
import java.io.*;

class VisitableVisitorFwd extends AbstractMuStrategy {
  public final static int ARG = 0;

  public VisitableVisitorFwd(VisitableVisitor v) {
    initSubterm(v);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    return visit_Visitable(any);
  }

  public Visitable visit_Visitable(Visitable any) throws VisitFailure {
    return  getArgument(ARG).visit(any);
  }
}


class GraphicalObserver implements DebugStrategyObserver {

  %include { mutraveler.tom }

  %typeterm Visitable {
    implement { jjtraveler.Visitable }
    visitor_fwd { VisitableVisitorFwd }
  }

  %strategy Replace(v:Visitable) extends `Identity() {
    visit Visitable {
      _ -> {  return v; }
    }
  }

  protected int scope = 0;
  protected Visitable term;
  protected MuStrategy init_strat;

  public GraphicalObserver(Visitable initialTerm, MuStrategy initialStrategy)  {
    term = initialTerm;
    init_strat = initialStrategy;
  }

  public void before(DebugStrategy s)  {
    String[] names = s.getStrat().getClass().getName().split("[\\.\\$]");
    String name = names[names.length-1];
    System.out.println("[" + (scope++) + "] applying " + name + " at " + s.getPosition());

    try {
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec("dot -Tsvg");
      Writer out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
      //VisitableViewer.visitableToDot(term, out, s.getPosition(), "#66FF66");
      StrategyViewer.stratToDot(init_strat, out, (MuStrategy) s.getStrat(),"#6666FF");
      out.close();
      pr.waitFor();
      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      pr = rt.exec("display -");
      out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
      int c;
      while ((c = in.read()) != -1) {
        out.write((char)c);
      }
      in.close();
      out.close();
      pr.waitFor();
    } catch (Exception e) {
      e.printStackTrace();
    }
  
  }
  public void after(DebugStrategy s, Visitable res) {
    term = ((MuStrategy) s.getPosition().getOmega(`Replace(res))).apply(term);
    System.out.println("[" + (--scope) + "] new tree : " + term);

    try {
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec("dot -Tsvg");
      Writer out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
      //VisitableViewer.visitableToDot(term, out, s.getPosition(), "#FF6666");
      //StrategyViewer.stratToDot(init_strat, out, (MuStrategy) s.getStrat(), "#6666FF");
      out.close();
      pr.waitFor();
      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      pr = rt.exec("display -");
      out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
      int c;
      while ((c = in.read()) != -1) {
        out.write((char)c);
      }
      in.close();
      out.close();
      pr.waitFor();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}


