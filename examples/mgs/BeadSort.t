import aterm.*;
import java.util.*;
import adt.*;

public class BeadSort {
  private MgsADTFactory factory;

  private HashMap space;
  private HashMap newSpace;

  %include { MgsADT.t }

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

    /*
     * create a bead such that "north"  is the immediate north of the bead
     * (Resp. "south")
     */
  private Bead makeBead(Bead beadNorth, Bead beadSouth) {
    Position north = (beadNorth!=null)?beadNorth.getPos():null;
    Position south = (beadSouth!=null)?beadSouth.getPos():null;
    
    if(north==null && south!=null) {
      return `bead(getNorthPosition(south),1);
    } else if(north!=null && south==null) {
      return `bead(getSouthPosition(north),1);
    } else if(north==null && south==null) {
      return `bead(makeOrigin(),1);
    } else if(north.getX()!=south.getX() || (north.getY()-south.getY()) != 2) {
      System.out.println("north and south are not compatible: " + north + " <--> " + south);
    }
    return `bead(getSouthPosition(north),1);
  }

  private Position makeOrigin() {
    return `pos(0,0);
  }
  
  private Position getNorthPosition(Position p) {
    int x = p.getX();
    int y = p.getY()+ 1;
    return `pos(x,y);
  }
  
  private Position getSouthPosition(Position p) {
    int x = p.getX();
    int y = p.getY() - 1;
    return `pos(x,y);
  }

  private Bead getNorthBead(Bead b) {
    return (Bead) space.get(getNorthPosition(b.getPos()));
  }
  
  private Bead getSouthBead(Bead b) {
    return (Bead) space.get(getSouthPosition(b.getPos()));
  }

  private boolean onGround(Bead b) {
    return b.getPos().getY() <= 0;
  }
  
  public BeadSort(MgsADTFactory factory) {
    this.factory = factory;
  }

  public MgsADTFactory getMgsADTFactory() {
    return factory;
  }


  public final static void main(String[] args) {
    BeadSort test = new BeadSort(new MgsADTFactory(16));
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
          newSpace.put(newBead.getPos(),newBead);
          System.out.println(b + " --> " + newBead);
          return true;
        }
      }

      bead[pos=p] -> {
        newSpace.put(p,b);
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

