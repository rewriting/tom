//package BoulderDashApplet;

import aterm.*;
import aterm.pure.*;
import java.util.*;
import adt.*;

public class BoulderDashCore {
  private TermFactory factory;

  private HashMap space;
  private HashSet marked;
  private HashMap newSpace;
  private static int SIZE = 20;

  %include { term.t }

  %op Bead beadRock(n:Bead, s:Bead, e:Bead, w:Bead) {
    fsym { }
    is_fsym(t) { (t!=null) && t.isBead() && t.getValue() == rock && !marked.contains(t) }
    get_slot(n,t) { getNorthBead(t) }
    get_slot(s,t) { getSouthBead(t) }
    get_slot(e,t) { getEastBead(t) }
    get_slot(w,t) { getWestBead(t) }
  }

  %op Bead empty {
    fsym { }
    is_fsym(t) { t==null }
    make { null }
  }

  private static int undefined = 0;
  private static int rock   = 7; // Diamond
  private static int ground = 2; //Titanium Wall

  private Position getNorthPosition(Position p) {
    return `pos(p.getX(),p.getY()+1);
  }

  private Position getSouthPosition(Position p) {
    return `pos(p.getX(),p.getY()-1);
  }

  private Position getEastPosition(Position p) {
    return `pos(p.getX()+1,p.getY());
  }

  private Position getWestPosition(Position p) {
    return `pos(p.getX()-1,p.getY());
  }

  private Bead getNorthBead(Bead b) {
    return (Bead) space.get(getNorthPosition(b.getPos()));
  }

  private Bead getSouthBead(Bead b) {
    return (Bead) space.get(getSouthPosition(b.getPos()));
  }

  private Bead getEastBead(Bead b) {
    return (Bead) space.get(getEastPosition(b.getPos()));
  }

  private Bead getWestBead(Bead b) {
    return (Bead) space.get(getWestPosition(b.getPos()));
  }


  public BoulderDashCore(TermFactory factory) {
    this.factory = factory;
  }

  public TermFactory getTermFactory() {
    return factory;
  }


  public final static void main(String[] args) {
    BoulderDashCore test = new BoulderDashCore(new TermFactory(16));
    test.run();
  }
  
  public void run() {
    space = new HashMap();
    marked = new HashSet();
    setRock(space);
    boolean fire = true;
    while(fire) {
      System.out.println(toMatrix(space));
      fire = oneStep();
    }
  }
  
  public String toMatrix(HashMap space) {
    int xmax=0;
    int ymax=0;

    Iterator it = space.values().iterator();
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      %match(Bead b) {
        bead(pos(x,y),_) -> {
          if(x>xmax) xmax = x;
          if(y>ymax) ymax = y;
        }
      }
    }

    int[][] array = new int[xmax+1][ymax+1];
    it = space.values().iterator();
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      %match(Bead b) {
        bead(pos(x,y),value) -> {
          array[x][y] = value;
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
    space = new HashMap();
    marked = new HashSet();
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
    newSpace = new HashMap();
    Iterator it = space.values().iterator();
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      boolean f = gravity(newSpace,b);
      fire = fire || f ;
    }
    space=newSpace;
    marked.clear();
    return fire;
  }

  // return true if fire a rule
  public boolean gravity(HashMap newSpace, Bead b) {
    %match(Bead b) {
      beadRock[s=empty()] -> {
        Bead newBead = `bead(getSouthPosition(b.getPos()),b.getValue());
        marked.add(b);
        putBead(newSpace,newBead);
        return true;
      }

      beadRock[s=s@beadRock[e=empty()],e=empty()] -> {
        Bead newBead = `bead(getEastPosition(getSouthPosition(b.getPos())),b.getValue());
        putBead(newSpace,newBead);
        putBead(newSpace,s);
        marked.add(b);
        marked.add(s);
        return true;
      }

      beadRock[s=s@beadRock[w=empty()],w=empty()] -> {
        Bead newBead = `bead(getWestPosition(getSouthPosition(b.getPos())),b.getValue());
        putBead(newSpace,newBead);
        putBead(newSpace,s);
        marked.add(b);
        marked.add(s);
        return true;
      }


      bead[pos=p] -> {
        newSpace.put(p,b);
        return false;
      }
    }
    return false;
  }

  private void putBead(HashMap space, int x, int y, int beadType) {
    Position p = `pos(x,y);
    space.put(p, `bead(p,beadType));
  }

  private void putBead(HashMap space, Bead b) {
    space.put(b.getPos(),b);
  }

  private void removeBead(HashMap space, Bead b) {
    space.remove(b.getPos());
  }


  public void setGround(HashMap space, int size) {
    for(int i=0 ; i<size ; i++) {
      putBead(space,i,0,ground);
    }
  }

  public void setRock(HashMap space) {
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

