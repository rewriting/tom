/*
 *
 * Copyright (c) 2000-2009, INRIA
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
 
  /** 
   * Initializes subterm
   */
  protected void initSubterm() {
    visitors = new Strategy[] {};
  }

  /** 
   * Initializes subterm by using and adding one Strategy
   *
   * @param v1 first Strategy to add to the array
   */
  protected void initSubterm(Strategy v1) {
    visitors = new Strategy[] {v1};
  }
  
  /** 
   * Initializes subterm by using and adding two Strategy
   *
   * @param v1 first Strategy to add to the array
   * @param v2 second Strategy to add to the array
   */
  protected void initSubterm(Strategy v1, Strategy v2) {
    visitors = new Strategy[] {v1,v2};
  }
 
  /** 
   * Initializes subterm by using abd adding three Strategy
   *
   * @param v1 first Strategy to add to the array
   * @param v2 second Strategy to add to the array
   * @param v3 third Strategy to add to the array
   */
  protected void initSubterm(Strategy v1, Strategy v2, Strategy v3) {
    visitors = new Strategy[] {v1,v2,v3};
  }
 
  /** 
   * Initializes subterm by using an array of Strategy
   *
   * @param v array used to initialize the subterm
   */
  protected void initSubterm(Strategy[] v) {
    visitors = v;
  }

  /** 
   * Returns visitors
   *
   * @return visitors
   */
  public Strategy[] getVisitors() {
    return visitors;
  }

  /** 
   * Returns the Strategy at the specified position in visitors
   *
   * @param i index of the Strategy to return
   * @return the Strategy at the specified position in visitors
   */
  public Strategy getVisitor(int i) {
    return visitors[i];
  }

  //visitable
  /** 
   * Returns the length of visitors
   *
   * @return the length of visitors
   */
  public int getChildCount() {
    return visitors.length;
  }

  /** 
   * Returns the Visitable at the specified position in visitors
   *
   * @param i index of the Visitable to return
   * @return the Visitable at the specified position in visitors
   */
  public Visitable getChildAt(int i) {
    return visitors[i];
  }

  /** 
   * Returns the list of (non builtin) Visitable children
   *
   * @return an array of Visitable
   */
  public Visitable[] getChildren() {
    return (Visitable[]) visitors.clone();
  }

  /** 
   * Replaces a child at the specified position
   *
   * @param i index of Visitable to set
   * @param child Visitable so set at the specified position
   * @return an array of Visitable
   */
  public Visitable setChildAt(int i, Visitable child) {
    visitors[i] = (Strategy) child;
    return this;
  }

  /** 
   * Replaces all children of any visitable at once, and returns this
   * visitable.
   *
   * @param children array of Visitable
   * @return this Visitable
   */
  public Visitable setChildren(Visitable[] children) {
    Strategy[] newVisitors = new Strategy[children.length];
    for(int i = 0; i < children.length; i++) {
      newVisitors[i] = (Strategy) children[i];
    }
    this.visitors = newVisitors;
    return this;
  }

  /** 
   * Executes the strategy in the given environment (on its current subject).
     This method can only be used inside user strategies to execute another strategy but with the current environment of the user strategy.
   *
   * @param envt the environment where execute the strategy. 
   * @throws VisitFailure if visit fails
   */
  public <T extends Visitable> T visit(Environment envt) throws VisitFailure {
    return (T) visit(envt,VisitableIntrospector.getInstance());
  }

  /** 
   * Visits the subject any by providing the environment 
   *
   * @param any the subject to visit. 
   * @throws VisitFailure if visit fails
   */
  public <T extends Visitable> T visit(T any) throws VisitFailure {
    return visit(any,VisitableIntrospector.getInstance());
  }

  /** 
   * Executes the strategy in the given environment (on its current subject).
     This method can only be used inside user strategies to execute another strategy but with the current environment of the user strategy.  
   *
   * @param envt the environment where execute the strategy.
   * @param m the introspector
   * @throws VisitFailure if visit fails
   */
  public Object visit(Environment envt, Introspector m) throws VisitFailure {
    AbstractStrategy.init(this,envt);
    int status = visit(m);
    if(status == Environment.SUCCESS) {
      return getSubject();
    } else {
      throw new VisitFailure();
    }
  }

  /** 
   * Visits the subject any in a light way (without environment)  
   *
   * @param any the subject to visit
   * @throws VisitFailure if visitLight fails
   */
  public <T extends Visitable> T visitLight(T any) throws VisitFailure {
    return visitLight(any,VisitableIntrospector.getInstance());
  }

  /** 
   * Visits the subject any by providing the introspector 
   *
   * @param any the subject to visit. 
   * @param m the introspector
   * @throws VisitFailure if visit fails
   */
  public <T> T visit(T any, Introspector m) throws VisitFailure{
    init();
    setRoot(any);
    int status = visit(m);
    if(status == Environment.SUCCESS) {
      return (T) getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  /** 
   * Get a reference to the current environment.
   *
   * @return the current environment
   * @throws RuntimeException if the environment is not initialized
   */
  public Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new RuntimeException("environment not initialized");
    }
  }

  /** 
   * Set up a new environment.
   *
   * @param env the environment to set up.
   */
  public void setEnvironment(Environment env) {
    this.environment = env;
  }

  /** 
   * Get the current root.
   *
   * @return the current root
   */
  public Object getRoot() {
    return environment.getRoot();
  }

  /** 
   * Set up the current root.
   *
   * @param any the current root
   */
  public void setRoot(Object any) {
    environment.setRoot(any);
  }

  /** 
   * Get the current subject
   *
   * @return the current subject
   */
  public Object getSubject() {
    return environment.getSubject();
  }

  /** 
   * Set up the current subject
   *
   * @param any the subject to set up
   */
  public void setSubject(Object any) {
    environment.setSubject(any);
  }

  /** 
   * Initialize the Strategy
   */
  public void init() {
    init(this,new Environment());
  }

  /** 
   * Initialize the Strategy by providing the environment and the strategy
   *
   * @param s the strategy
   * @param env the environment
   */
  public static void init(Strategy s, Environment env) {
    /* to avoid infinite loop during initialization
     * TODO: use static typing
     */
    if (((s instanceof AbstractStrategy) && ((AbstractStrategy)s).environment==env) || ((s instanceof BasicStrategy) && ((BasicStrategy)s).environment==env)) {
      return;
    }
    s.setEnvironment(env);
    for(int i=0 ; i<s.getChildCount() ; i++) {
      Strategy child = (Strategy) s.getChildAt(i);
      if(child instanceof Strategy) {
        init(child,env);
      }
    }
  }

}
