package jjtraveler;

/**
 * The VisitFailure exception is used to model success and failure
 * of visitor combinators. On failure, the exception is raised. At
 * choice points, the try and catch constructs are used to recover
 * from failed visits.
 */

public class VisitFailure extends Exception {

  public VisitFailure() {
    super();
  }

  public VisitFailure(String msg) {
    super(msg);
  }

}
