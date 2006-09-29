package tom.library.sl;

/**
 * <code>x.accept(Identity) = x</code>
 * <p>
 * Basic visitor combinator without arguments that does nothing.
 * <p>
 * See also <a href="IdentityTest.java">IdentityTest</a>.
 */

public class Identity extends AbstractMuStrategy {
  public Identity() {
    initSubterm();
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable x) {
    return x;
  }

  protected void visit() {
    // do nothing
  }
}
