/*
 * Copyright (c) 2004-2006, INRIA
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
import tom.library.strategy.mutraveler.*;
import java.util.*;
import aterm.*;
import aterm.pure.PureFactory;

public class ReplaceVisitor extends aterm.ATermFwd {

  private static ATermFactory atermFactory = new PureFactory();

  // replaces in the term the placeholders with
  // their initialization from tds
  public ATerm modifyReplacement(ATerm term, Map tds){

    String termStr = term.toString();
    Iterator it = tds.keySet().iterator();
    while (it.hasNext()){

      ATerm key = (ATerm)it.next();
      String keyStr = key.toString();
      
      termStr = termStr.replaceAll(keyStr,((ATerm)tds.get(key)).toString());
    }
    return atermFactory.parse(termStr);
  }

  ATerm tomatch;
  ATerm replacement;

  public ReplaceVisitor(ATerm tomatch, ATerm replacement) {
    super(new Identity());
    this.tomatch = tomatch;
    this.replacement = replacement;
  }

  public aterm.Visitable visitATerm(ATerm arg) {
    Map tds = Ted.match(tomatch, arg);
    if (tds != null) 
      return modifyReplacement(replacement, tds);
    else 
      return arg;
  }
}

