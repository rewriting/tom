package tom.library.sl;

/**
 * A visitor that it iself visitable with a VisitorVisitor needs
 * to implement the MuStrategy interface. The visitor's arguments
 * should play the role of children.
 */

public abstract class AbstractMuStrategy implements GraphStrategy {
  protected jjtraveler.reflective.VisitableVisitor[] visitors;
  protected tom.library.strategy.mutraveler.Position position;

  public void setPosition(tom.library.strategy.mutraveler.Position pos) {
    this.position = pos;
  }

  public tom.library.strategy.mutraveler.Position getPosition() {
    if(position!=null) {
      return position;
    } else {
      throw new RuntimeException("position not initialized");
    }
  }

  public final boolean hasPosition() {
    return position!=null;
  }

  protected void initSubterm() {
    visitors = new jjtraveler.reflective.VisitableVisitor[] {};
  }
  protected void initSubterm(jjtraveler.reflective.VisitableVisitor v1) {
    visitors = new jjtraveler.reflective.VisitableVisitor[] {v1};
  }
  protected void initSubterm(jjtraveler.reflective.VisitableVisitor v1, jjtraveler.reflective.VisitableVisitor v2) {
    visitors = new jjtraveler.reflective.VisitableVisitor[] {v1,v2};
  }
  protected void initSubterm(jjtraveler.reflective.VisitableVisitor v1, jjtraveler.reflective.VisitableVisitor v2, jjtraveler.reflective.VisitableVisitor v3) {
    visitors = new jjtraveler.reflective.VisitableVisitor[] {v1,v2,v3};
  }
  protected void initSubterm(jjtraveler.reflective.VisitableVisitor[] v) {
    visitors = v;
  }

  public final jjtraveler.reflective.VisitableVisitor getArgument(int i) {
    return visitors[i];
  }

  public void setArgument(int i, jjtraveler.reflective.VisitableVisitor child) {
    visitors[i]= child;
  }

  public int getChildCount() {
    return visitors.length;
  }

  public jjtraveler.Visitable getChildAt(int i) {
      return visitors[i];
  }
  
  public jjtraveler.Visitable setChildAt(int i, jjtraveler.Visitable child) {
    visitors[i]= (jjtraveler.reflective.VisitableVisitor) child;
    return this;
  }

  /*
   * Apply the strategy, and returns the subject in case of VisitFailure
   */
  public jjtraveler.Visitable apply(jjtraveler.Visitable any) {
    try {
      return tom.library.strategy.mutraveler.MuTraveler.init(this).visit(any);
    } catch (jjtraveler.VisitFailure f) {
      return any;
    }
  }

  public tom.library.strategy.mutraveler.MuStrategy accept(tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd v) throws jjtraveler.VisitFailure {
    return v.visit_Strategy(this);
  }

 /*
  * For graphs
  */

  protected Environment environment;
  public void setEnvironment(Environment env) {
    this.environment = env;
  }

  public Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new RuntimeException("environment not initialized");
    }
  }

  /**
   * getter en setter for the root term (i.e. top position)
   */
  public jjtraveler.Visitable getRoot() {
    return getEnvironment().getRoot();
  }

  public void setRoot(jjtraveler.Visitable any) {
    getEnvironment().setRoot(any);
  }

  /**
   * getter en setter for the term of the current position
   */
  public jjtraveler.Visitable getSubject() {
    return getEnvironment().getSubject();
  }

  public void setSubject(jjtraveler.Visitable any) {
    getEnvironment().setSubject(any);
  }

  /*
   * Apply the strategy, and returns the subject in case of VisitFailure
   */

  public jjtraveler.Visitable gapply(jjtraveler.Visitable any) {
    try {
      init();
      setRoot(any);
      visit();
      return getRoot();
    } catch (jjtraveler.VisitFailure f) {
      return any;
    }
  }

  public void init() {
    init(new Environment());
  }

  protected abstract void visit() throws jjtraveler.VisitFailure;

  private void init(Environment env) {
    setEnvironment(env);
    for(int i=0 ; i<getChildCount() ; i++) {
      jjtraveler.Visitable child = getChildAt(i);
      if(child instanceof AbstractMuStrategy) {
        ((AbstractMuStrategy)child).init(env);
      }
    }
  }

}

