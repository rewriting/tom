import aterm.*;
import aterm.pure.*;
import java.util.*;
import adt.*;

public class BoulderDash {
  private TermFactory factory;

  private HashMap space;
  private HashSet marked;
  private HashMap newSpace;

  %include { term.t }

  %op Bead beadRock(n:Bead, s:Bead, e:Bead, w:Bead) {
    fsym { }
    is_fsym(t) { (t!=null) && t.isBead() && t.getValue().equals(rock) && !marked.contains(t) }
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


  private static Integer rock   = new Integer(1);
  private static Integer ground = new Integer(2);

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

  private Position getEastPosition(Position p) {
    Integer x = new Integer(p.getX().intValue() + 1);
    Integer y = p.getY();
    return `pos(x,y);
  }

  private Position getWestPosition(Position p) {
    Integer x = new Integer(p.getX().intValue() - 1);
    Integer y = p.getY();
    return `pos(x,y);
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


  public BoulderDash(TermFactory factory) {
    this.factory = factory;
  }

  public TermFactory getTermFactory() {
    return factory;
  }


  public final static void main(String[] args) {
    BoulderDash test = new BoulderDash(new TermFactory(16));
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

  public String toMatrix(HashMap space) {
    int xmax=0;
    int ymax=0;

    Iterator it = space.values().iterator();
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      int x = b.getPos().getX().intValue();
      int y = b.getPos().getY().intValue();
      if(x>xmax) xmax = x;
      if(y>ymax) ymax = y;
    }
    Integer array[][] = new Integer[xmax+1][ymax+1];

    it = space.values().iterator();
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      int x = b.getPos().getX().intValue();
      int y = b.getPos().getY().intValue();
      array[x][y] = b.getValue();
    }

    String s = "";
    for(int y=ymax ; y>=0 ; y--) {
      String line = "";
      for(int x=0 ; x<=xmax ; x++) {
        if(array[x][y] == null) {
          line += " ";
        } else
          if(array[x][y].equals(ground)) {
            line += "X";
          } else if(array[x][y].equals(rock)) {
            line += "O";
          } 
      }
      s += line + "\n";
    }
    return s;
  }


  public void run() {
    space = new HashMap();
    marked = new HashSet();
    setGround(space,20);
    putBead(space,10,5,rock);
    putBead(space,10,6,rock);
    putBead(space,10,7,rock);
    putBead(space,10,8,rock);
    putBead(space,10,9,rock);

    boolean fire = true;
    while(fire) {
      System.out.println(toMatrix(space));
      fire = oneStep();
    }

    //System.out.println("\nstable space = \n" + this);
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

  private void putBead(HashMap space, int x, int y, Integer beadType) {
    Integer X = new Integer(x);
    Integer Y = new Integer(y);
    Position p = `pos(X,Y);
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


}

