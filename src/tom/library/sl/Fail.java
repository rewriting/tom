package tom.library.sl;

import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

/**
 * <code>x.accept(Fail)</code> always raises a VisitFailure exception. 
 * <p>
 * Basic visitor combinator without arguments, that always fails.
 * <p>
 * Test case documentation:
 * <a href="FailTest.java">FailTest</a>
 */

public class Fail extends AbstractMuStrategy {
  private String message;
    
  /** 
   * Construct Fail combinator with empty failure message.
   */
  public Fail() {
    initSubterm();
    this.message = ""; 
  }
  /**
   * Construct Fail combinator with a failure message to be passed to the
   * VisitFailure that it throws.
   */
  public Fail(String message) {
    this.message = message;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    throw new VisitFailure(message);
  }

  protected void visit() throws VisitFailure {
    throw new VisitFailure(message);
  }

}
