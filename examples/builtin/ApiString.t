/*
 * Copyright (c) 2004, INRIA
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
 */

package builtin;

import aterm.pure.*;
import aterm.*;
import builtin.term.*;
import builtin.term.types.*;

public class ApiString {

  private Factory factory;
  
  %include { term.tom }

  public ApiString(Factory factory) {
    this.factory = factory;
  }

  public Factory getTermFactory() {
    return factory;
  }

  public void run() {

    String n = "Roger";
    Term t = `Name("Albert");

    matchBlock1: {
      %match(Term t) {
        Name("Albert") -> { System.out.println("Albert"); break matchBlock1; }
        Name("Roger")  -> { System.out.println("Roger"); break matchBlock1;}
        _ -> { System.out.println("Unknonw"); }
      }
    }
    
    matchBlock2: {
      %match(String n) {
        "Albert" -> { System.out.println("Albert"); break matchBlock2; }
        "Roger"  -> { System.out.println("Roger"); break matchBlock2;}
        _ -> { System.out.println("Unknonw"); }
      }
    }

  }
  
  public final static void main(String[] args) {
    ApiString test = new ApiString(new Factory(new PureFactory()));
    test.run();
  }
  
}
