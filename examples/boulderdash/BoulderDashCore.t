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

package boulderdash;

import aterm.pure.*;
import java.util.*;
import boulderdash.boulder.*;
import boulderdash.boulder.types.*;

public class BoulderDashCore {

  private HashMap<Position,Bead> space;
  private HashSet<Bead> marked;
  private HashMap<Position,Bead> newSpace;
  private static int SIZE = 20;

  %include { boulder/boulder.tom }
 
  %op Bead beadRock(n:Bead, s:Bead, e:Bead, w:Bead) {
    is_fsym(t) { ($t!=null) && $t.isbead() && $t.getvalue() == rock && !marked.contains($t) }
    get_slot(n,t) { getNorthBead($t) }
    get_slot(s,t) { getSouthBead($t) }
    get_slot(e,t) { getEastBead($t) }
    get_slot(w,t) { getWestBead($t) }
  }

  %op Bead empty() {
    is_fsym(t) { $t==null }
    make { null }
  }

  private static int undefined = 0;
  private static int rock   = 7; // Diamond
  private static int ground = 2; //Titanium Wall

  private Position getNorthPosition(Position p) {
    return `pos(p.getx(),p.gety()-1);
  }

  private Position getSouthPosition(Position p) {
    return `pos(p.getx(),p.gety()+1);
  }

  private Position getEastPosition(Position p) {
    return `pos(p.getx()+1,p.gety());
  }

  private Position getWestPosition(Position p) {
    return `pos(p.getx()-1,p.gety());
  }

  private Bead getNorthBead(Bead b) {
    return (Bead) space.get(getNorthPosition(b.getpos()));
  }

  private Bead getSouthBead(Bead b) {
    return (Bead) space.get(getSouthPosition(b.getpos()));
  }

  private Bead getEastBead(Bead b) {
    return (Bead) space.get(getEastPosition(b.getpos()));
  }

  private Bead getWestBead(Bead b) {
    return (Bead) space.get(getWestPosition(b.getpos()));
  }

  public final static void main(String[] args) {
    BoulderDashCore test = new BoulderDashCore();
    test.run();
  }
  
  public void run() {
    space = new HashMap<Position,Bead>();
    marked = new HashSet<Bead>();
    setRock(space);
    boolean fire = true;
    while(fire) {
      System.out.println(toMatrix(space));
      fire = oneStep();
    }
  }
  
  public String toMatrix(HashMap<Position,Bead> space) {
    int xmax=0;
    int ymax=0;

    Iterator it = space.values().iterator();
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      %match(Bead b) {
        bead(pos(x,y),_) -> {
          if(`x>xmax) xmax = `x;
          if(`y>ymax) ymax = `y;
        }
      }
    }

    int[][] array = new int[xmax+1][ymax+1];
    it = space.values().iterator();
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      %match(Bead b) {
        bead(pos(x,y),value) -> {
          array[`x][`y] = `value;
        }
      }
    }

    String s = "";
    for(int y=ymax+1 ; y<SIZE ; y++) {
      s += "\n";
    }

    for(int y=0 ; y<ymax+1 ; y++) {
      String line = "";
      for(int x=0 ; x<=xmax ; x++) {
        if(array[x][y] == undefined) {
          line += " ";
        } else
          if(array[x][y] == ground) {
            line += "X";
          } else if(array[x][y] == rock) {
            line += "O";
          } 
      }
      s += line + "\n";
    }
    return s;
  }

  public HashMap start() {
    space = new HashMap<Position,Bead>();
    marked = new HashSet<Bead>();
    setRock(space);
    return space;
  }

  public HashMap getStep() {
    if(oneStep()) {
      return space;
    } else {
      return new HashMap();
    }
  }
  
  public boolean oneStep() {
    boolean fire = false;
    newSpace = new HashMap<Position,Bead>();
    Iterator it = space.values().iterator();
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      boolean f = gravity(newSpace,b);
      fire = fire || f ;
    }
    space = newSpace;
    marked.clear();
    return fire;
  }

  // return true if fire a rule
  public boolean gravity(HashMap<Position,Bead> newSpace, Bead b) {
    %match(Bead b) {
      beadRock[s=empty()] -> {
        Bead newBead = `bead(getSouthPosition(b.getpos()),b.getvalue());
        marked.add(b);
        putBead(newSpace,newBead);
        return true;
      }
/*
      beadRock[s=s@beadRock[e=empty()],e=empty()] -> {
        Bead newBead = `bead(getEastPosition(getSouthPosition(b.getpos())),b.getvalue());
        putBead(newSpace,newBead);
        putBead(newSpace,`s);
        marked.add(b);
        marked.add(`s);
        return true;
      }

      beadRock[s=s@beadRock[w=empty()],w=empty()] -> {
        Bead newBead = `bead(getWestPosition(getSouthPosition(b.getpos())),b.getvalue());
        putBead(newSpace,newBead);
        putBead(newSpace,`s);
        marked.add(b);
        marked.add(`s);
        return true;
      }


      bead[pos=p] -> {
        newSpace.put(`p,b);
        return false;
      }*/
    }
    return false;
  }

  private void putBead(HashMap<Position,Bead> space, int x, int y, int beadType) {
    Position p = `pos(x,y);
    Bead b = `bead(p,beadType);
    space.put(p, b);
  }

  private void putBead(HashMap<Position,Bead> space, Bead b) {
    space.put(b.getpos(),b);
  }

  private void removeBead(HashMap<Position,Bead> space, Bead b) {
    space.remove(b.getpos());
  }


  public void setGround(HashMap<Position,Bead> space, int size) {
    for(int i=0 ; i<size ; i++) {
      putBead(space,i,0,ground);
    }
  }

  public void setRock(HashMap<Position,Bead> space) {
    for(int i=0 ; i<SIZE ; i++) {
      putBead(space,i,0,ground);
      putBead(space,i,SIZE-1,ground);
      putBead(space,0,i,ground);
      putBead(space,SIZE-1,i,ground);
    }
    int midlevel = SIZE/2-1;
    for(int i=0 ; i<SIZE/2-1 ; i++) {
      putBead(space,i,midlevel,ground);
    }
    for(int i=SIZE/2+1 ; i<SIZE ; i++) {
      putBead(space,i,midlevel,ground);
    }

    for(int j=1 ; j<SIZE/2-1 ; j++) {
      for(int i=1 ; i<SIZE-1 ; i++) {
        putBead(space,i,j,rock);
       }
    }
  }

}
