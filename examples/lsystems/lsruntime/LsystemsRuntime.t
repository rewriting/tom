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
package lsruntime;

import lsruntime.adt.*;
import lsruntime.graphic.LsystemsView;
import aterm.*;
import aterm.pure.*;
import java.util.*;

/**
 * Classe commune
 * 
 * @author Alexis Saettler (alexis@saettler.org)
 */
public class LsystemsRuntime {
  
  public LsystemsRuntime(String[] args) {
    this(args,new LsystemsFactory(new PureFactory()));
  }
  
  public LsystemsRuntime(String[] args,LsystemsFactory factory) {
    this.factory = factory;
    ran = new Random();
    
    if(args.length > 0) {
      for(int i=0; i < args.length; i++) { 
        if(args[i].charAt(0) == '-') {
          if(args[i].equals("--verbose")) {
            this.verbose = true;
          }
          if(args[i].equals("--draw")) {
            this.draw = true;
          }
        }
      }
    }

  }
  
  public LsystemsFactory factory;
  public LsystemsFactory getLsystemsFactory() {
    return factory;
  }
  
  public Parameter n;
  public Parameter delta;
  public Parameter longueur;
  public Random ran;
  public boolean draw;
  public boolean verbose;
  private NodeList root;
  
  %include { adt/lsystems.tom }
  %include { extras.tom }
  
  public void run(MatchLsystems mls,int nbr_it, int angle) {
    run(mls,nbr_it,angle,10);
  }
  
  public void run(MatchLsystems mls,int nbr_it, int angle, int longu) {
    this.matchls = mls;
    root = matchls.init();
    
    if (verbose) System.out.println("Init: " + root);
    
    n = `n(nbr_it);
    delta = `Angle(angle);
    longueur = `defLong(longu);
    
    for ( int i=1 ; i <= nbr_it ; i++ ) {
      // boucle autant de fois que d'itérations
      long startChrono = System.currentTimeMillis();

      root = go_derecursive(root,`concNode());
      
      long endChrono = System.currentTimeMillis();
      if (verbose) {
        System.out.println("itération " + i + "...(" + (endChrono-startChrono) + " ms)");
        System.out.println(root);
      }
    }
    if (verbose) System.out.println("\n\n\n");
    if (draw) draw();
    
  }

  public NodeList go_derecursive(NodeList root,NodeList last_prev_rev) {
    NodeList prev_rev = (NodeList) root.reverse();
    prev_rev = `concNode(prev_rev*,last_prev_rev*);
    
    NodeList result = `concNode();
    NodeList next = `concNode();
    
    boolean fin = false;
    do {
      %match(NodeList prev_rev) {
        concNode(token,tail*) -> {
          prev_rev = tail;
          
          NodeList last = `concNode();
          matchlab_derecursive: {
            %match(Node token) {
              SubList(sousliste) -> {
                Node new_token = `SubList(go_derecursive(sousliste,prev_rev));
                last = `concNode(new_token);
                break matchlab_derecursive;
              }
              _ -> {
                last = matchls.apply(prev_rev,token,next);
              }
            }
          }
          result = `concNode(last*,result*);
          next = `concNode(token,next*);
        }
        concNode() -> { fin = true; }
      }
      if (prev_rev == last_prev_rev) {
        fin = true;
      }
    } while(!fin);
    
    return result;
  }
  
  public int iter;
  
  /**
   * Used by the stochatsics Lsystems
   *
   * @return an integer between 0 and 100
   */
  public int random() {
    return ran.nextInt(100);
  }
  
  public void draw() {
    LsystemsView view = new LsystemsView(getLsystemsFactory(),delta.getValue(),longueur.getValue(),root);
    view.print();
  }
  
  
  public boolean UCD(ATermList I, Ignore P) {
    boolean result = true;
    for (; result && !I.isEmpty() ; I = I.getNext()) {
      result = P.apply(I.getFirst());
    }
    return result;
  }
  
  public MatchLsystems matchls;
  public Ignore ign;
  public Ignore extign;
  
}
