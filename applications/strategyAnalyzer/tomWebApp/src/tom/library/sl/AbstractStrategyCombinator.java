/*
 *
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
import java.util.Arrays;

/**
 * <p>
 * Partial implementation of the strategy interface that can be used to
 * implement new strategy combinators (like the <code>All</code> strategy).
 * This class has an attribute of type Strategy array which corresponds to the
 * combinator arguments. These strategy arguments are also considered as
 * children when visiting this combinator. Note that this abstract class is
 * used to define all the <code>sl</code> strategy combinators.
 *
 */

public abstract class AbstractStrategyCombinator extends AbstractStrategy {

  /** Strategy arguments of the combinator that can be used to define
   * <code>visit</code> methods
   */
  protected Strategy[] arguments;

  /**
   * Initializes subterm
   */
  protected void initSubterm() {
    arguments = new Strategy[] {};
  }

  /**
   * Initializes subterm by using and adding one Strategy
   *
   * @param v1 first Strategy to add to the array
   */
  protected void initSubterm(Strategy v1) {
    arguments = new Strategy[] {v1};
  }

  /**
   * Initializes subterm by using and adding two Strategy
   *
   * @param v1 first Strategy to add to the array
   * @param v2 second Strategy to add to the array
   */
  protected void initSubterm(Strategy v1, Strategy v2) {
    arguments = new Strategy[] {v1,v2};
  }

  /**
   * Initializes subterm by using abd adding three Strategy
   *
   * @param v1 first Strategy to add to the array
   * @param v2 second Strategy to add to the array
   * @param v3 third Strategy to add to the array
   */
  protected void initSubterm(Strategy v1, Strategy v2, Strategy v3) {
    arguments = new Strategy[] {v1,v2,v3};
  }

  /**
   * Initializes subterm by using an array of Strategy
   *
   * @param v array used to initialize the subterm
   */
  protected void initSubterm(Strategy[] v) {
    this.arguments = (Strategy[])Arrays.asList(v).toArray();
  }

  /**
   * Returns arguments
   *
   * @return arguments
   */
  public Strategy[] getVisitors() {
    return (Strategy[]) Arrays.asList(arguments).toArray();
  }

  /**
   * Returns the Strategy at the specified position in arguments
   *
   * @param i index of the Strategy to return
   * @return the Strategy at the specified position in arguments
   */
  public Strategy getVisitor(int i) {
    return arguments[i];
  }

  /**
   * Returns the length of arguments
   *
   * @return the length of arguments
   */
  public int getChildCount() {
    return arguments.length;
  }

  /**
   * Returns the Visitable at the specified position in arguments
   *
   * @param i index of the Visitable to return
   * @return the Visitable at the specified position in arguments
   */
  public Visitable getChildAt(int i) {
    return arguments[i];
  }

  /**
   * Returns the list of (non builtin) Visitable children
   *
   * @return an array of Visitable
   */
  public Visitable[] getChildren() {
    return (Visitable[]) Arrays.asList(arguments).toArray();
  }

  /**
   * Replaces a child at the specified position
   *
   * @param i index of Visitable to set
   * @param child Visitable so set at the specified position
   * @return an array of Visitable
   */
  public Visitable setChildAt(int i, Visitable child) {
    arguments[i] = (Strategy) child;
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
    this.arguments = newVisitors;
    return this;
  }

}
