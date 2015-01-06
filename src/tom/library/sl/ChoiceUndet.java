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

/**
 * <code>ChoiceUndet(v1,..,vn) = vi</code> with probability 1/n
 * <p>
 * Visitor combinator with a list of n arguments, 
 * that select a argument according to the probability 1/n
 * The strategy fails if the selected strategy fails
 * <p>
 * Note that any side-effects of vi are not undone when it fails.
 */

public class ChoiceUndet extends AbstractStrategyCombinator {
  public final static int FIRST = 0;
  public final static int THEN = 1;
  int size;
  private static java.util.Random random = null;

  private ChoiceUndet(Strategy[] strategies) {
    initSubterm(strategies);
    this.size = strategies.length;
    if(random == null) {
      random = new java.util.Random();
    }
  }

  private ChoiceUndet(Strategy head, Strategy tail) {
    if (tail instanceof ChoiceUndet) {
      Strategy[] tailstrategies = ((ChoiceUndet)tail).arguments;
      Strategy[] strategies = new Strategy[tailstrategies.length+1];
      strategies[0] = head;
      for (int i=0; i<tailstrategies.length;i++) {
        strategies[i+1] = tailstrategies[i];
      }
      initSubterm(strategies);
      this.size = strategies.length;
    } else {
      initSubterm(new Strategy[] {head,tail});
      this.size = 2;
    }
    if(random == null) {
      random = new java.util.Random();
    }
  }

  public static Strategy make(Strategy first, Strategy then) {
    if(then==null) {
      return first;
    }
    return new ChoiceUndet(first,then);
  }

  public Strategy getHead() {
    return arguments[0];
  }

  public Strategy getTail() {
    if (size<=1) {
      return null;
    } else {
      Strategy[] tail = new Strategy[size-1];
      for (int i=0; i<size-1; i++) {
        tail[i] = arguments[size-1];
      }
      return new ChoiceUndet(tail);
    }
  }

  public <T> T visitLight(T subject, Introspector introspector) throws VisitFailure {
    int randomInt = random.nextInt(size);
    return arguments[randomInt].visitLight(subject,introspector);
  }

  public int visit(Introspector introspector) {
    int randomInt = random.nextInt(size);
    return arguments[randomInt].visit(introspector);
  }

}
