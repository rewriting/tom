package tom.library.strategy.mutraveler;
import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * <code>OmegaU(i,v)</code> 
 * <p>
 * Basic visitor combinator which applies Omega(i,v) with
	 * i in [1..arity] with a uniform probability
	 * i.e. with probability 1/getChildCount
 * <p>
*/

public class OmegaU extends AbstractMuStrategy {
  public final static int ARG = 0;
  protected VisitableVisitor defaultStrategy;
  private static java.util.Random random = null;
  
  public OmegaU(VisitableVisitor v,VisitableVisitor d) {
    initSubterm(v);
		this.defaultStrategy = d;
    if(random == null) {
      random = new java.util.Random();
    }
  }

  public VisitableVisitor getDefaultStrategy() {
    return defaultStrategy;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
		int arity = any.getChildCount();
    int randomInt = Math.abs(random.nextInt());
		if(arity==0) {
			return defaultStrategy.visit(any);
		} else {
			int selectedSubterm = (randomInt%arity)+1;
			return (new Omega(selectedSubterm,getArgument(ARG))).visit(any);
    }
  }
}

