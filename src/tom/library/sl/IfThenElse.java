/*
 *
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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

public class IfThenElse extends AbstractStrategyCombinator {

  public final static int CONDITION = 0;
  public final static int TRUE_CASE = 1;
  public final static int FALSE_CASE = 2;

  public IfThenElse(Strategy c, Strategy t, Strategy f) {
    initSubterm(c,t,f);
  }

  public IfThenElse(Strategy c, Strategy t) {
    initSubterm(c,t, new Identity());
  }


  public <T> T visitLight(T x, Introspector introspector) throws VisitFailure {
    boolean success;
    T result;
    try {
      arguments[CONDITION].visitLight(x, introspector);
      success = true;
    } catch (VisitFailure vf) {
      success = false;
    }
    if (success) {
      result = arguments[TRUE_CASE].visitLight(x, introspector);
    } else {
      result = arguments[FALSE_CASE].visitLight(x, introspector);
    }
    return result;
  }

  public int visit(Introspector introspector) {
    Object subject = environment.getSubject();
    int status = arguments[CONDITION].visit(introspector);
    /* reset modifications from CONDITION */
    /* we are just interested in the status */
    environment.setSubject(subject);
    if(status == Environment.SUCCESS) {
      return arguments[TRUE_CASE].visit(introspector);
    } else {
      //setStatus(Environment.SUCCESS);
      return arguments[FALSE_CASE].visit(introspector);
    }
  }
}
