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
 * <code>x.fire(SequenceId (v1,v2)) = x.fire(v1) ; x.fire(v2) if x.fire(v1)!=x</code>
 * <p>
 * Strategy combinator with two arguments, that applies
 * these arguments one after the other (sequential composition), if the first
 * one is not the identity.
 */

public class SequenceId   extends AbstractStrategyCombinator {
  public final static int FIRST = 0;
  public final static int THEN = 1;
  private SequenceId(Strategy first, Strategy then) {
    initSubterm(first,then);
  }

  public static Strategy make(Strategy first, Strategy then) {
    if(then==null) {
      return first;
    }
    return new SequenceId(first,then);
  }

  public <T> T visitLight(T subject, Introspector introspector) throws VisitFailure {
    T v = arguments[FIRST].visitLight(subject, introspector);
    if(v != subject) {
      return arguments[THEN].visitLight(v, introspector);
    } else {
      return v;
    }
  }

  public int visit(Introspector introspector) {
    Object subject = environment.getSubject();
    int status = arguments[FIRST].visit(introspector);
    if(status == Environment.SUCCESS && environment.getSubject() != subject) {
      return arguments[THEN].visit(introspector);
    }
    return status;
  }

}
