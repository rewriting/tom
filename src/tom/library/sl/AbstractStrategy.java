/*
 *
 * Copyright (c) 2000-2007, Pierre-Etienne Moreau
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

public abstract class AbstractStrategy implements Strategy {
  protected Strategy[] visitors;
  protected Environment environment;

  protected void initSubterm() {
    visitors = new Strategy[] {};
  }
  protected void initSubterm(Strategy v1) {
    visitors = new Strategy[] {v1};
  }
  protected void initSubterm(Strategy v1, Strategy v2) {
    visitors = new Strategy[] {v1,v2};
  }
  protected void initSubterm(Strategy v1, Strategy v2, Strategy v3) {
    visitors = new Strategy[] {v1,v2,v3};
  }
  protected void initSubterm(Strategy[] v) {
    visitors = v;
  }

  public int getChildCount() {
    return visitors.length;
  }

  public Visitable getChildAt(int i) {
    return visitors[i];
  }

  public Visitable[] getChildren() {
    return (Visitable[]) visitors.clone();
  }

  public Visitable setChildAt(int i, Visitable child) {
    visitors[i] = (Strategy) child;
    return this;
  }

  public Visitable setChildren(Visitable[] children) {
    Strategy[] newVisitors = new Strategy[children.length];
    for(int i = 0; i < children.length; i++) {
      newVisitors[i] = (Strategy) children[i];
    }
    this.visitors = newVisitors;
    return this;
  }

  public Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws VisitFailure {
    return v.visit_Strategy(this);
  }

  public Visitable visit(Environment envt) throws VisitFailure {
    AbstractStrategy.init(this,envt);
    int status = visit();
    if(status == Environment.SUCCESS) {
      return getRoot();
    } else {
      throw new VisitFailure();
    }
  }

  public Visitable visit(Visitable any) throws VisitFailure{
    init();
    setRoot(any);
    int status = visit();
    if(status == Environment.SUCCESS) {
      return getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new RuntimeException("environment not initialized");
    }
  }

  public void setEnvironment(Environment env) {
    this.environment = env;
  }

  public Visitable getRoot() {
    return environment.getRoot();
  }

  public void setRoot(Visitable any) {
    environment.setRoot(any);
  }

  public Visitable getSubject() {
    return environment.getSubject();
  }

  public void setSubject(Visitable any) {
    environment.setSubject(any);
  }

  public void init() {
    init(this,new Environment());
  }

  public static void init(Strategy s, Environment env) {
    /* to avoid infinite loop during initialization
     * TODO: use static typing
     */
    if((s instanceof AbstractStrategy) && ((AbstractStrategy)s).environment==env) {
      return;
    }
    s.setEnvironment(env);
    for(int i=0 ; i<s.getChildCount() ; i++) {
      Visitable child = s.getChildAt(i);
      if(child instanceof Strategy) {
        init((Strategy)child,env);
      }
    }
  }

}
