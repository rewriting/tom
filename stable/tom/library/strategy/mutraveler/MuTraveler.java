package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.util.*;

public class MuTraveler {

  private static Map positionMap = new HashMap();

  public static VisitableVisitor init(VisitableVisitor v) {
    try {
      VisitableVisitor muInitializer = new BottomUp(new MuInitializer(new Position()));
      return (VisitableVisitor) muInitializer.visit((Visitable)v);
    } catch (VisitFailure e) {
      System.out.println("initialization failed");
    }
    return v;
  }

  public static void setPosition(VisitableVisitor v, Position position) {
    positionMap.put(v,position);
  }

  public static Position getPosition(VisitableVisitor v) {
    return (Position) positionMap.get(v);
  }

}

class MuInitializer extends Identity {
  private Position position; 

  public MuInitializer(Position position) {
    super();
    this.position = position;
  }

  public Visitable visit(Visitable v) {
    if(v instanceof AbstractVisitableVisitor) {
      AbstractVisitableVisitor avv = (AbstractVisitableVisitor) v;
      avv.setPosition(position);
    } else if(v instanceof VisitableVisitor) {
      MuTraveler.setPosition((VisitableVisitor)v,position);
    } else {
      throw new RuntimeException("cannot initialize: " + v);
    }
    return v;
  }
}
