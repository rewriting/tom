/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
package lsruntime;

import lsruntime.adt.lsystems.*;
import lsruntime.adt.lsystems.types.*;
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
    this(args,new Factory(new PureFactory()));
  }
  
  public LsystemsRuntime(String[] args, Factory factory) {
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
  
  public Factory factory;
  public Factory getLsystemsFactory() {
    return factory;
  }
  
  public Parameter n;
  public Parameter delta;
  public Parameter longueur;
  public Random ran;
  public boolean draw;
  public boolean verbose;
  private NodeList root;
  
  %include { ../lsystems.tom }
  
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
