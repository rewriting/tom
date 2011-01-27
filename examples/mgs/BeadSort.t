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

import java.util.*;
import aterm.*;
import aterm.pure.*;
import mgs.term.*;
import mgs.term.types.*;

public class BeadSort {
  private HashMap space;
  private HashMap newSpace;

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
    return (Bead) space.get(getNorthPosition(b.getpos()));
  }
  
  private Bead getSouthBead(Bead b) {
    return (Bead) space.get(getSouthPosition(b.getpos()));
  }

  private boolean onGround(Bead b) {
    return b.getpos().gety() <= 0;
  }

  public final static void main(String[] args) {
    BeadSort test = new BeadSort();
    test.run();
  }

  public String toString() {
    String s = "";
    Iterator it = space.values().iterator();
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      s += b + "\n";
    }
    return s;
  }

  public void run() {
    space = new HashMap();
    generateNumber(space,3 );

    boolean fire = true;
    while(fire) {
      System.out.println("\nspace = \n" + this);
      fire = oneStep();
    }
    
    System.out.println("\nstable space = \n" + this);
  }

  public boolean oneStep() {
    boolean fire = false;
    newSpace = new HashMap();
    Iterator it = space.values().iterator();
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      boolean f = gravity(newSpace,b);
      fire = fire || f ;
    }
    space=newSpace;
    return fire;
  }

    // return true if fire a rule
  public boolean gravity(HashMap newSpace, Bead b) {
    %match(Bead b) {
      beadNS[s=empty()] -> {
        if(!onGround(b)) {
          Bead newBead = `beadNS(b,empty());
          newSpace.put(newBead.getpos(),newBead);
          System.out.println(b + " --> " + newBead);
          return true;
        }
      }

      bead[pos=p] -> {
        newSpace.put(`p,b);
        return false;
      }
    }
    return false;
  }

  public void addInteger(HashMap space, int n, int y) {
    for(int i=0 ; i<n ; i++) {
      Position p = `pos(i,y);
      space.put(p, `bead(p,1));
    }
  }

  public void generateNumber(HashMap space, int max) {
    int y=0;
    for(int i=1 ; i<=max ; i++) {
      addInteger(space,i,y);
      y++;
    }
  }
  
}

