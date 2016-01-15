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

/**
 * <code>Pselect(p,q,v1,v2) = v1</code> with probability p/q
 * <p>
 * <code>Pselect(p,q,v1,v2) = v2</code> with probability 1-(p/q)
 * <p>
 * Visitor combinator with a probability and two arguments, 
 * that select a argument according to the probability p/q
 * The strategy fails if the selected strategy fails
 * <p>
 * Note that any side-effects of v1 are not undone when it fails.
 */

public class Pselect extends AbstractStrategyCombinator {
  public final static int FIRST = 0;
  public final static int THEN = 1;
  private int p;
  private int q;
  private static java.util.Random random = null;

  public Pselect(int p, int q, Strategy first, Strategy then) {
    initSubterm(first,then);
    this.p = p;
    this.q = q;
    if(random == null) {
      random = new java.util.Random();
    }
  }

  public int getP() {
    return p;
  }
  public int getQ() {
    return q;
  }

  public <T> T visitLight(T subject, Introspector introspector) throws VisitFailure {
    int randomInt = random.nextInt(q);
    if(randomInt < p) {
      return arguments[FIRST].visitLight(subject,introspector);
    } else {
      return arguments[THEN].visitLight(subject,introspector);
    }
  }

  public int visit(Introspector introspector) {
    int randomInt = random.nextInt(q);
    Object subject = environment.getSubject();
    if(randomInt < p) {
      return arguments[FIRST].visit(introspector);
    } else {
      return arguments[THEN].visit(introspector);
    }
  }
}
