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

public class IfThenElse extends AbstractStrategy {

  public final static int CONDITION = 0;
  public final static int TRUE_CASE = 1;
  public final static int FALSE_CASE = 2;

  public IfThenElse(Strategy c, Strategy t, Strategy f) {
    initSubterm(c,t,f);
  }

  public IfThenElse(Strategy c, Strategy t) {
    initSubterm(c,t, new Identity());
  }


  public jjtraveler.Visitable visit(jjtraveler.Visitable x) throws jjtraveler.VisitFailure {
    boolean success;
    jjtraveler.Visitable result;
    try {
      visitors[CONDITION].visit(x);
      success = true;
    } catch (jjtraveler.VisitFailure vf) {
      success = false;
    }
    if (success) {
      result = visitors[TRUE_CASE].visit(x);
    } else {
      result = visitors[FALSE_CASE].visit(x);
    }
    return result;
  }

  public void visit() {
    boolean success;
    Visitable result;
    Visitable subject = environment.getSubject();
    visitors[CONDITION].visit();
    /* reset modifications from CONDITION */
    /* we are just interested in the status */
    environment.setSubject(subject);
    if (environment.getStatus() == Environment.SUCCESS) {
      success = true;
    } else {
      success = false;
      environment.setStatus(Environment.SUCCESS);
    }
    if (success) {
      visitors[TRUE_CASE].visit();
    } else {
      visitors[FALSE_CASE].visit();
    }
    return;
  }
}
