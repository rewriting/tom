package jjtraveler;

import jjtraveler.reflective.AbstractVisitableVisitor;

/**
 * <code>x.accept(Identity) = x</code>
 * <p>
 * Basic visitor combinator without arguments that does nothing.
 * <p>
 * See also <a href="IdentityTest.java">IdentityTest</a>.
 */

public class Identity extends AbstractVisitableVisitor {
  public Identity() {
    init();
  }

	public Visitable visit(Visitable x) {
		return x;
	}

}
