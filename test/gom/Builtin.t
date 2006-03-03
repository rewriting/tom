/*
 * Copyright (c) 2006, INRIA
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
 * Antoine Reilles   e-mail: Antoine.Reilles@loria.fr
 */

import builtin.types.*;

public class Builtin {

  %include { builtin/Builtin.tom }

  public String run() {
    String res = "";
    int n = 32;
    Wrapper t = `Int(10);
    String a = "Germain";
    String b = new String("Germain");
    System.out.println("a == b: "+ (a == b));
    Wrapper nm = `Name("Germain");
    Wrapper na = `Name(a);
    Wrapper nb = `Name(b);
    System.out.println("na == nb: "+ (na == nb));

    matchBlock1: {
      %match(Wrapper t) {
        Int(10) -> { res += "10"; break matchBlock1; }
        Int(32) -> { res += "32"; break matchBlock1;}
        _       -> { res += "Unknown"; }
      }
    }
    res += "\n";
    if (t == `Int(10)) {
      res += "true\n";
    }

    matchBlock2: {
      %match(int n) {
        10 -> { res += "10"; break matchBlock2; }
        32 -> { res += "32"; break matchBlock2;}
        _  -> { res += "Unknown"; }
      }
    }

    matchBlock3: {
      %match(Wrapper nm) {
        Name("Pem") -> { res += " pem"; break matchBlock3; }
        Name("Germain") -> { res += " G"; break matchBlock3;}
        _  -> { res += "Unknown"; }
      }
    }
    return res;
  }
  
  public final static void main(String[] args) {
    Builtin test = new Builtin();
    System.out.println(test.run());
  }
  
}
