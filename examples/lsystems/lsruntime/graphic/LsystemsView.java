package lsruntime.graphic;

import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import aterm.*;

import lsruntime.adt.*;

public class LsystemsView extends JFrame {
  
  private LsystemsDraw myDraw;
  private JScrollPane js;
  
  public LsystemsView(LsystemsFactory factory, String titre, double angle, int longueur, NodeList root ) {
    super(titre);
    myDraw = new LsystemsDraw(factory,angle,longueur,root);
  }
  
  public LsystemsView(LsystemsFactory factory, String titre, double angle, NodeList root ) {
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
