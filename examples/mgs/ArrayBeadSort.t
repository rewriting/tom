/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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
package mgs;

import mgs.term.*;
import mgs.term.types.*;
import aterm.*;
import aterm.pure.*;

public class ArrayBeadSort {

  private static int SIZE = 50;
  private boolean[][] space;
  private boolean[][] newSpace;

  %include { term/term.tom }

  %op Bead beadNS(n:Bead, s:Bead) {
    is_fsym(t) { ($t!=null) && $t.isbead() }
    make(n,s) { makeBead($n,$s) }
    get_slot(n,t) { getNorthBead($t) }
    get_slot(s,t) { getSouthBead($t) }
  }

  %op Bead empty() {
    is_fsym(t) { $t==null }
    make { null }
  }

    /*
     * create a bead such that "north"  is the immediate north of the bead
     * (Resp. "south")
     */
  private Bead makeBead(Bead beadNorth, Bead beadSouth) {
    Position north = (beadNorth!=null)?beadNorth.getpos():null;
    Position south = (beadSouth!=null)?beadSouth.getpos():null;
    
    if(north==null && south!=null) {
      return `bead(getNorthPosition(south),1);
    } else if(north!=null && south==null) {
      return `bead(getSouthPosition(north),1);
    } else if(north==null && south==null) {
      return `bead(makeOrigin(),1);
    } else if(north.getx()!=south.getx() || (north.gety()-south.gety()) != 2) {
      System.out.println("north and south are not compatible: " + north + " <--> " + south);
    }
    return `bead(getSouthPosition(north),1);
  }

  private Position makeOrigin() {
    return `pos(0,0);
  }
  
  private Position getNorthPosition(Position p) {
    return `pos(p.getx(),p.gety()+1);
  }
  
  private Position getSouthPosition(Position p) {
    return `pos(p.getx(),p.gety()-1);
  }

  private Bead getNorthBead(Bead b) {
    Position p = getNorthPosition(b.getpos());
    int x = p.getx();
    int y = p.gety();
    if(x>=0 && y>=0 && space[x][y]) {
      return (Bead) `bead(p,b.getvalue());
    } else {
      return null;
    }
  }
  
  private Bead getSouthBead(Bead b) {
    Position p = getSouthPosition(b.getpos());
    int x = p.getx();
    int y = p.gety();
    if(x>=0 && y>=0 && space[x][y]) {
      return (Bead) `bead(p,b.getvalue());
    } else {
      return null;
    }
  }

  private boolean onGround(Bead b) {
    return b.getpos().gety() <= 0;
  }

  public final static void main(String[] args) {
    ArrayBeadSort test = new ArrayBeadSort();
    test.run();
  }

  public String toString() {
    String s = "";
    for(int y=SIZE-1 ; y>=0 ; y--) {
      String line = "";
      for(int x=0 ; x<SIZE ; x++) {
        line += (space[x][y])?"X":" ";
      }
      if(line.trim().length()>0) {
        s += line + "\n";
      }
    }
    return s;
  }

  public void run() {
    space = new boolean[SIZE][SIZE];
    generateNumber(space,20);

    boolean fire = true;
    while(fire) {
      System.out.println("\nspace = \n" + this);
      fire = oneStep();
    }
    
    System.out.println("\nstable space = \n" + this);
  }

  public boolean oneStep() {
    boolean fire = false;
    newSpace = new boolean[SIZE][SIZE];
    for(int x=0 ; x<SIZE ; x++) {
      for(int y=0 ; y<SIZE ; y++) {
        if(space[x][y]) {
          Bead b = `bead(pos(x,y),1);
          boolean f = gravity(newSpace,b);
          fire = fire || f ;
        }
      }
    }

    space=newSpace;
    return fire;
  }

    // return true if fire a rule
  public boolean gravity(boolean newSpace[][], Bead b) {
    %match(Bead b) {
      beadNS[s=empty()] -> {
        if(!onGround(b)) {
          Bead newBead = `beadNS(b,empty());
          int x = newBead.getpos().getx();
          int y = newBead.getpos().gety();
          newSpace[x][y] =true;
          
          //System.out.println(b + " --> " + newBead);
          return true;
        }
      }

      bead[pos=pos(X,Y)] -> {
        newSpace[`X][`Y] = true;
        return false;
      }
    }
    return false;
  }

  public void addInteger(boolean space[][], int n, int y) {
    for(int i=0 ; i<n ; i++) {
      space[i][y] = true;
    }
  }

  public void generateNumber(boolean space[][], int max) {
    int y=0;
   for(int i=1 ; i<=max/2 ; i++) {
     addInteger(space,i*2,y);
     y++;
   } 
   for(int i=1 ; i<=max/2 ; i++) {
     addInteger(space,1+i*2,y);
     y++;
   }
   
  }
  
}

