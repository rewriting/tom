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

  public static VisitableVisitor mu(VisitableVisitor var, VisitableVisitor v) {
    try {
      VisitableVisitor muExpander = new BottomUp(new MuExpander(var,v));
      return (VisitableVisitor) muExpander.visit((Visitable)v);
    } catch (VisitFailure e) {
      System.out.println("mu reduction failed");
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

class MuExpander implements VisitableVisitor {
  VisitableVisitor variable;
  VisitableVisitor instance;
  public MuExpander(VisitableVisitor variable, VisitableVisitor instance) {
    this.variable = variable;
    this.instance = instance;
  }
    
  public Visitable visit(Visitable v) throws VisitFailure { 
    if(v instanceof MuVar) {
      MuVar muV = (MuVar)v;
      MuVar muVariable = (MuVar)variable;
      if(muV.equals(muVariable)) {
        muV.setInstance(instance);
        muV.setName(null);
      } 
    }
    return v;
  }

  public int getChildCount() {
    return 2;
  }

  public Visitable getChildAt(int i) {
    switch (i) {
    case 0: return variable;
    case 1: return instance;
    default: throw new IndexOutOfBoundsException();
    }
  }

  public Visitable setChildAt(int i, Visitable child) {
    switch (i) {
    case 0: variable = (VisitableVisitor)child; return this;
    case 1: instance = (VisitableVisitor)child; return this;
    default: throw new IndexOutOfBoundsException();
    }
  }

}

/**
 * <code>BottomUp(v) = Sequence(All(BottomUp(v)),v)</code>
 * <p>
 * Visitor combinator with one visitor argument that applies this
 * visitor exactly once to the current visitable and each of its
 * descendants, following the bottomup (post-order) traversal
 * strategy.
 */

class BottomUp extends Sequence {

    /*
     * Since it is not allowed to reference `this' before the
     * super type constructor has been called, we can not
     * write `super(All(this),v)'
     * Instead, we set the first argument first to `null', and
     * set it to its proper value afterwards.
     */
    public BottomUp(VisitableVisitor v) {
      super(null,v);
      setArgument(FIRST,new All(this));
    }
    
}
