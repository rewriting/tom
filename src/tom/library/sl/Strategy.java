/*
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

public interface Strategy extends Visitable {

  /**
   * Set up a new environment
   *
   * @param p the environment to set up
   */
  public void setEnvironment(Environment p);

  /**
   * Get a reference to the current environment.
   *
   * @return the current environment
   */
  public Environment getEnvironment();

  /**
   *  Visit the subject any by providing the environment
   *  
   *  @param any the subject to visit.
   *  @throws VisitFailure in case of failure.
   */  
  public <T extends Visitable> T visit(T any) throws VisitFailure;

  /**
   * Visit the subject any in a light way (without environment)
   *
   * @param any the subject to visit.
   * @throws VisitFailure in case of failure.
   */
  public <T extends Visitable> T visitLight(T any) throws VisitFailure;

  /**
   * Execute the strategy in the given environment (on its current subject).
   * This method can only be used inside user strategies to execute another
   * strategy but with the current environment of the user strategy.
   *
   * @param envt the environment where execute the strategy.
   * @throws VisitFailure in case of failure.
   */
  public <T extends Visitable> T visit(Environment envt) throws VisitFailure;

  /**
   * Visit the subject any by providing the introspector
   *
   * @param any the subject to visit.
   * @throws VisitFailure in case of failure.
   */
  public <T> T visit(T any, Introspector i) throws VisitFailure;

  /**
   * Visit the subject any in a light way (without environment)
   *
   * @param any the subject to visit.
   * @throws VisitFailure in case of failure.
   */
  public <T> T visitLight(T any, Introspector i) throws VisitFailure;

  /**
   * Execute the strategy in the given environment (on its current subject).
   * This method can only be used inside user strategies to execute another
   * strategy but with the current environment of the user strategy.
   *
   * @param envt the environment where execute the strategy.
   * @param i the instrospector
   * @throws VisitFailure in case of failure.
   */
  public Object visit(Environment envt, Introspector i) throws VisitFailure;

  /**
   *  Visit the current subject (found in the environment)
   *  and place its result in the environment.
   *  Sets the environment flag to Environment.FAILURE in case of failure
   * 
   *  @param i the introspector
   */
  public int visit(Introspector i);

}
