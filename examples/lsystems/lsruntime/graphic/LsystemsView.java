package lsruntime.graphic;

import javax.swing.*;
import java.awt.*;

import lsruntime.adt.*;

public class LsystemsView extends JFrame {
  
  private static String titre = "Lsystems";
  
  private LsystemsDraw myDraw;
  private JScrollPane js;
  
  public LsystemsView(LsystemsFactory factory, double angle, int longueur, NodeList root ) {
    super(titre);
    myDraw = new LsystemsDraw(factory,angle,longueur,root);
  }
  
  public LsystemsView(LsystemsFactory factory, double angle, NodeList root ) {
    super(titre);
    myDraw = new LsystemsDraw(factory,angle,10,root);
  }
  
  public void print() {  
    Container container = getContentPane();
    container.setLayout(new BorderLayout());
    container.add(myDraw);
    setSize(myDraw.getSize());
    setVisible(true);
  }
  
}
