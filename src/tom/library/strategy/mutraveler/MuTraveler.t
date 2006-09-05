package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.util.*;

public class MuTraveler {
  %include { mustrategy.tom }

  private static Map positionMap = new WeakHashMap();

  public static VisitableVisitor init(VisitableVisitor v) {
    try {
      MuInitializer muInit = new MuInitializer(new Position());
      VisitableVisitor muInitializer = `BottomUp(muInit);
      return (VisitableVisitor) muInitializer.visit((Visitable)v);
    } catch (VisitFailure e) {
      System.out.println("initialization failed");
    }
    return v;
  }

	/**
	 * Basic function which build Omega(i,v) such that
	 * i in [1..arity] with a uniform probability
	 * i.e. with probability 1/getChildCount
	 * <p>
	 */
/*	
  public static VisitableVisitor lawUniform(VisitableVisitor v, VisitableVisitor defaultStrategy, Visitable subject) {
		int arity = subject.getchildCount();
    int randomInt = Math.abs(random.nextInt());
		if(arity==0) {
			return defaultStrategy;
		} else {
			int selectedSubterm = (randomInt%arity)+1;
			return new Omega(selectedSubterm,v);
    }
  }
*/
  public static Position getPosition(VisitableVisitor v) {
    return (Position) ((Position) positionMap.get(v)).clone();
  }

  protected static void setPosition(VisitableVisitor v, Position position) {
    positionMap.put(v,position);
  }

	public static VisitableVisitor getReplace(VisitableVisitor v, final Visitable t) {
    return ((Position) positionMap.get(v)).getReplace(t);
	}
	
	public static VisitableVisitor getSubterm(VisitableVisitor v) {
    return ((Position) positionMap.get(v)).getSubterm();
	}

}

class MuInitializer extends Identity {
  private Position initialPosition; 

  public MuInitializer(Position initialPosition) {
    super();
    this.initialPosition = initialPosition;
  }

  public Visitable visit(Visitable v) {
    if(v instanceof MuStrategy) {
      MuStrategy avv = (MuStrategy) v;
      avv.setPosition(initialPosition);
    } else if(v instanceof VisitableVisitor) {
      MuTraveler.setPosition((VisitableVisitor)v,initialPosition);
    } else {
      throw new RuntimeException("cannot initialize: " + v);
    }
    return v;
  }
}

