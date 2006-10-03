/*
 *
 * Copyright (c) 2000-2006, Pierre-Etienne Moreau
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 **/
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

