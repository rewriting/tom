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
 * <p>                                                 
 *                                                  
 * <code></code>
 * <p> 
 * <p>
 * <p>
 * 
 */

public class GenericForward extends AbstractStrategy {

  private tom.library.sl.Environment environment;

  protected Strategy any;

  public GenericForward(Strategy v) {
    this.any = v;
  }

  public int getChildCount() {
    return 1;
  }

  public Visitable getChildAt(int i) {
    return null;
  }

  public Visitable setChildAt(int i, Visitable child) {
    return null;
  }

  public Visitable[] getChildren() {
    return null;
  }

  public Visitable setChildren(Visitable[] children) {
    return null;
  }

  public Strategy accept(tom.library.sl.reflective.StrategyFwd v) {
    return null;
  }

  public tom.library.sl.Environment getEnvironment() {
    if (environment != null) {
      return environment;
    } else {
      throw new java.lang.RuntimeException("environment not initialized");
    }
  }

  public void setEnvironment(tom.library.sl.Environment env) {
    this.environment = env;
  }

  public Visitable visit(Environment envt) throws VisitFailure {
    setEnvironment(envt);
    int status = visit();
    if (status == Environment.SUCCESS) {
      return environment.getSubject();
    } else {
      throw new VisitFailure();
    }
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    AbstractStrategy.init(this, new tom.library.sl.Environment());
    environment.setRoot(any);
    int status = visit();
    if (status == tom.library.sl.Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new VisitFailure();
    }
  }

  public int visit() {
    try {
      environment.setSubject((tom.library.sl.Visitable) this
          .visitLight(environment.getSubject()));
      return tom.library.sl.Environment.SUCCESS;
    } catch (VisitFailure f) {
      return tom.library.sl.Environment.FAILURE;
    }
  }

  public Visitable visitLight(Visitable v) throws VisitFailure {
    return this.visit_generic(v);
  }

  public Visitable visit_generic(Visitable arg) throws VisitFailure {
    if (environment != null) {
      return any.visit(environment);
    } else {
      return any.visitLight(arg);
    }
  }
}