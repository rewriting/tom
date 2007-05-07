/*
 * Copyright (c) 2004-2007, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
 */
package ted;

import java.io.*;
import aterm.*;
import tom.library.sl.*;
import aterm.pure.PureFactory;

public class GrepVisitor extends aterm.ATermFwdVoid implements Strategy {

  private static ATermFactory atermFactory = new PureFactory();
  ATerm tomatch;
  ATermList res = atermFactory.makeList();

  public GrepVisitor(ATerm tomatch) {
    super();
    this.tomatch = tomatch;
  }

  public void voidVisitATerm(ATerm arg) throws VisitFailure {
    if(Ted.match(tomatch, arg) != null) {
      res = res.append(arg);
      throw new VisitFailure();
      //TODO throw this instance and use mu x.(try(sequence(s,all(x)))
    }
  }

  public ATermList getList() {
    return res;
  }

  /* for the Strategy compatibility */
  public  Strategy setChildAt(int index, Strategy v) { throw new IndexOutOfBoundsException(); }
  public  Strategy getChildAt(int index) { throw new IndexOutOfBoundsException(); }
  public int getChildCount() { return 0; }
}

