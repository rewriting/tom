/*
 * Lsystems implementation for jtom
 * Copyright (C) 2003, LORIA-INRIA
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */
package lsruntime.graphic;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import aterm.*;

import lsruntime.adt.*;

public class LsystemsDraw  extends JComponent {

  %include { ../adt/lsystems.tom }
  %include { ../extras.tom }
  
  private LsystemsFactory factory;
  public LsystemsFactory getLsystemsFactory() {
    return factory;
  }
  
  public int MAX_WIDTH = 1024;
  public int MAX_HEIGHT = 768;
  public double BORDER = 2.0;
  
  private double angle;
  private NodeList root;
  private double longueur;
  private Point3D point;
  private Vector liste;
  public Rectangle taille;
  
  private boolean activeDraw;
  
  public LsystemsDraw(LsystemsFactory factory,double angle, int longueur, NodeList root ) {
    super();
    this.factory = factory;
    this.angle = angle;
    this.root = root;
    this.longueur = (double) longueur;
    taille = new Rectangle();
    
    point = new Point3D();
    point.setDirection(-90.0);
    Polygon poly = new Polygon();
    addPoint(point,poly);
    
    liste = new Vector();
    liste.add(paint(root,poly));
    setSize(taille.getSize());
    repaint();
    taille.setRect(taille.getX()-BORDER,taille.getY()-BORDER,taille.getWidth()+BORDER*2+BORDER*10,taille.getHeight()+BORDER*2+BORDER*10);
    if (taille.width > MAX_WIDTH) 
      taille.setSize(MAX_WIDTH,taille.height);
    if (taille.height > MAX_HEIGHT) 
      taille.setSize(taille.width,MAX_HEIGHT);
    setSize(taille.getSize());
  }
  
  
  public void update(Graphics g) {
    repaint();
  }
  
  
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    
    //Rectangle rec = g2.getClipBounds();
    g2.translate(-taille.x,-taille.y);
    
    for (int j = liste.size() - 1 ; j >= 0 ; j-- ) {
      g2.draw((Shape)liste.get(j));
    }
    
  }



  public Polygon paint(NodeList root, Polygon poly) {
    while(!root.isEmpty()) {
      Node node = root.getHead();
      matchBlock: {
        %match(Node node) {
          SubList(sublist) -> {
            Point3D old_point = new Point3D(point);
            Polygon new_poly = new Polygon();
            addPoint(point,new_poly);
            liste.add(paint(sublist,new_poly));
            point = old_point;
            break matchBlock;
          }
          
          _ -> {
            paint(node,poly);
            break matchBlock;
          }
        }
      }
      root = root.getTail();
    }
    return poly;
  }
  
  public void paint(Node n, Polygon poly) {
    %match(Node n) {
      Cell("F",para) -> {
        %match(ParamList para) {
          concParam() -> { point.avance(longueur); }
          concParam(head,tail*) -> { point.avance(head.getArg()); }
        }
        addPoint(point,poly);
      }
      Cell("f",para) -> {
        %match(ParamList para) {
          concParam() -> { point.avance(longueur); }
          concParam(head,tail*) -> { point.avance(head.getArg()); }
        }
        taille.add(point);
      }
      Right -> { point.tourne(angle); }
      
      Left -> { point.tourne(-angle); }
    }
  }
  
  public void addPoint(Point3D p, Polygon poly) {
    poly.addPoint((int) Math.round(p.getX()),(int) Math.round(p.getY()));
    taille.add(p);
  }
}
