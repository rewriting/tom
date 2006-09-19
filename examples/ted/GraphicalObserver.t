package ted;

import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import tom.library.strategy.mutraveler.*;
import jjtraveler.VisitFailure;
import java.io.*;

// -------------------- SVG -----------------------------
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.*;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.w3c.dom.svg.SVGDocument;
import org.apache.batik.util.XMLResourceDescriptor;


import javax.xml.parsers.*;
import org.xml.sax.*;
// ----------------------------------------------------

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
    javax.swing.SwingUtilities.invokeLater(
        new Runnable() { public void run() { createAndShowGUI();} });
  }

  
  // GUI
  JSVGCanvas svgCanvas1 = new JSVGCanvas();
  JSVGCanvas svgCanvas2 = new JSVGCanvas();

  private void createAndShowGUI() {
    JFrame.setDefaultLookAndFeelDecorated(true);
    JFrame.setDefaultLookAndFeelDecorated(true);
    JFrame frame = new JFrame("Strategy Debugger");
    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    

    JButton button = new JButton("Next");
    button.addActionListener(
        new ActionListener () {
          public void actionPerformed(ActionEvent e) {
          synchronized (GraphicalObserver.this) {
            GraphicalObserver.this.notify();
            }
          }
        }
    );

    
    JPanel pan = new JPanel(new GridLayout(1,2));
    pan.add(svgCanvas2);
    pan.add(svgCanvas1);

    frame.getContentPane().add(pan, BorderLayout.CENTER);
    frame.getContentPane().add(button, BorderLayout.SOUTH);
    frame.pack();
    frame.setVisible(true);
  }

  private static  SVGDocument streamToSVGDom(InputStream stream) {
    try {
      String parser = XMLResourceDescriptor.getXMLParserClassName();
      SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
      String uri = "http://bidon";
      return f.createSVGDocument(uri,stream);
    } catch (IOException ex) { 
      ex.printStackTrace();
      return null;
    }
  }

  public void before(DebugStrategy s)  {
    String[] names = s.getStrat().getClass().getName().split("[\\.\\$]");
    String name = names[names.length-1];
    System.out.println("[" + (scope++) + "] applying " + name + " at " + s.getPosition());

    try {
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec("dot -Tsvg");
      Writer out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
      VisitableViewer.visitableToDot(term, out, s.getPosition(), "#66FF66");
      out.close();
      //pr.waitFor();
      SVGDocument dom = streamToSVGDom(pr.getInputStream());
      svgCanvas1.setSVGDocument(dom);


      pr = rt.exec("dot -Tsvg");
      out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
      StrategyViewer.stratToDot(init_strat, out, (MuStrategy) s.getStrat(),"#6666FF");
      out.close();
      //pr.waitFor();
      dom = streamToSVGDom(pr.getInputStream());
      svgCanvas2.setSVGDocument(dom);

      //System.in.read();
      synchronized (this) {
        this.wait();
      }

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
      VisitableViewer.visitableToDot(term, out, s.getPosition(), "#FF6666");
      out.close();
      //pr.waitFor();
      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      SVGDocument dom = streamToSVGDom(pr.getInputStream());
      svgCanvas1.setSVGDocument(dom);

      pr = rt.exec("dot -Tsvg");
      out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
      StrategyViewer.stratToDot(init_strat, out, (MuStrategy) s.getStrat(),"#666666");
      out.close();
      //pr.waitFor();
      dom = streamToSVGDom(pr.getInputStream());
      svgCanvas2.setSVGDocument(dom);

      //System.in.read();
      synchronized (this) {
        this.wait();
      }


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}


