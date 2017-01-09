/*
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
 * 	- Neither the name of the Inria nor the names of its
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
 * <p>
 * Partial implementation of the strategy interface that implements
 * most of the <code>visit</code> methods. This class is extended by
 * the two abstract classes <code>AbstractStrategyBasic</code> and
 * <code>AbstractStrategyCombinator</code> that can be used to
 * implement respectively new basic transformations (like a rewrite
 * rule system) and new strategy combinators (like the
 * <code>All</code> strategy).
 * <p>
 * It is not advised to implement directly this abstract class. 
 *
 */
public abstract class AbstractStrategy implements Strategy {

  /**
   * Environment of the strategy application
   */
  protected Environment environment;

  /** 
   * Executes the strategy in the given environment (on its current subject).
     This method can only be used inside user strategies to execute another
     strategy but with the current environment of the user strategy.
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
     This method can only be used inside user strategies to execute another
     strategy but with the current environment of the user strategy.  
   *
   * @param envt the environment where execute the strategy.
   * @param i the introspector
   * @throws VisitFailure if visit fails
   */
  public Object visit(Environment envt, Introspector i) throws VisitFailure {
    AbstractStrategy.init(this,envt);
    int status = visit(i);
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
   * @param i the introspector
   * @throws VisitFailure if visit fails
   */
  @SuppressWarnings("unchecked")
  public <T> T visit(T any, Introspector i) throws VisitFailure {
    init(i);
    setRoot(any);
    int status = visit(i);
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
    return getEnvironment().getRoot();
  }

  /** 
   * Set up the current root.
   *
   * @param any the current root
   */
  public void setRoot(Object any) {
    getEnvironment().setRoot(any);
  }

  /** 
   * Get the current subject
   *
   * @return the current subject
   */
  public Object getSubject() {
    return getEnvironment().getSubject();
  }

  /** 
   * Set up the current subject
   *
   * @param any the subject to set up
   */
  public void setSubject(Object any) {
    getEnvironment().setSubject(any);
  }

  /** 
   * Get the current position
   *
   * @return the current application position
   */
  public Position getPosition() {
    return getEnvironment().getPosition();
  }

  /** 
   * Get the current ancestor
   *
   * @return the current ancestor
   */
  public Object getAncestor() {
    return getEnvironment().getAncestor();
  }

  /** 
   * Initialize the Strategy
   */
  public void init(Introspector introspector) {
    init(this,new Environment(introspector));
  }

  /** 
   * Initialize the Strategy by providing the environment and the strategy
   *
   * @param s the strategy
   * @param env the environment
   */
  public static void init(Strategy s, Environment env) {
    /* to avoid infinite loop during initialization
     */
    if (((s instanceof AbstractStrategy) && ((AbstractStrategy)s).environment==env)) {
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
