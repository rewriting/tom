import aterm.*;
import aterm.pure.*;
import java.util.*;
import adt.*;

public class ArrayBeadSort {
  private TermFactory factory;

  private static int SIZE = 50;
  private boolean[][] space;
  private boolean[][] newSpace;

  %include { term.t }

  %op Bead beadNS(n:Bead, s:Bead) {
    fsym { }
    is_fsym(t) { (t!=null) && t.isBead() }
    make(n,s) { makeBead(n,s) }
    get_slot(n,t) { getNorthBead(t) }
    get_slot(s,t) { getSouthBead(t) }
  }

  %op Bead empty {
    fsym { }
    is_fsym(t) { t==null }
    make { null }
  }

  private static Integer one = new Integer(1);

    /*
     * create a bead such that "north"  is the immediate north of the bead
     * (Resp. "south")
     */
  private Bead makeBead(Bead beadNorth, Bead beadSouth) {
    Position north = (beadNorth!=null)?beadNorth.getPos():null;
    Position south = (beadSouth!=null)?beadSouth.getPos():null;
    
    if(north==null && south!=null) {
      return `bead(getNorthPosition(south),one);
    } else if(north!=null && south==null) {
      return `bead(getSouthPosition(north),one);
    } else if(north==null && south==null) {
      return `bead(makeOrigin(),one);
    } else if(north.getX().intValue()!=south.getX().intValue() ||
              (north.getY().intValue()-south.getY().intValue()) != 2) {
      System.out.println("north and south are not compatible: " + north + " <--> " + south);
    }
    return `bead(getSouthPosition(north),one);
  }

  private Position makeOrigin() {
    Integer zero = new Integer(0);
    return `pos(zero,zero);
  }
  
  private Position getNorthPosition(Position p) {
    Integer x = p.getX();
    Integer y = new Integer(p.getY().intValue() + 1);
    return `pos(x,y);
  }
  
  private Position getSouthPosition(Position p) {
    Integer x = p.getX();
    Integer y = new Integer(p.getY().intValue() - 1);
    return `pos(x,y);
  }

  private Bead getNorthBead(Bead b) {
    Position p = getNorthPosition(b.getPos());
    int x = p.getX().intValue();
    int y = p.getY().intValue();
    if(x>=0 && y>=0 && space[x][y]) {
      return (Bead) `bead(p,b.getValue());
    } else {
      return null;
    }
  }
  
  private Bead getSouthBead(Bead b) {
    Position p = getSouthPosition(b.getPos());
    int x = p.getX().intValue();
    int y = p.getY().intValue();
    if(x>=0 && y>=0 && space[x][y]) {
      return (Bead) `bead(p,b.getValue());
    } else {
      return null;
    }
  }

  private boolean onGround(Bead b) {
    return b.getPos().getY().intValue() <= 0;
  }
  
  public ArrayBeadSort(TermFactory factory) {
    this.factory = factory;
  }

  public TermFactory getTermFactory() {
    return factory;
  }


  public final static void main(String[] args) {
    ArrayBeadSort test = new ArrayBeadSort(new TermFactory(16));
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
          Integer X = new Integer(x);
          Integer Y = new Integer(y);
          Bead b = `bead(pos(X,Y),one);
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
          int x = newBead.getPos().getX().intValue();
          int y = newBead.getPos().getY().intValue();
          newSpace[x][y] =true;
          
          //System.out.println(b + " --> " + newBead);
          return true;
        }
      }

      bead[pos=pos(X,Y)] -> {
        newSpace[X.intValue()][Y.intValue()] = true;
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

