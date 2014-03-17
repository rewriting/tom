/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package lsruntime.graphic;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import aterm.*;

import lsruntime.adt.lsystems.types.*;
import lsruntime.adt.lsystems.*;

public class LsystemsDraw  extends JComponent {

  %include { ../../lsystems.tom }
  
  private Factory factory;
  public Factory getLsystemsFactory() {
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
  
  public LsystemsDraw(Factory factory,double angle, int longueur, NodeList root ) {
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
      Right() -> { point.tourne(angle); }
      
      Left() -> { point.tourne(-angle); }
    }
  }
  
  public void addPoint(Point3D p, Polygon poly) {
    poly.addPoint((int) Math.round(p.getX()),(int) Math.round(p.getY()));
    taille.add(p);
  }
}
