/*
 * Copyright (c) 2004-2005, INRIA
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

package tutorial;

public class HelloWorld {
  %include { string.tom }

  public final static void main(String[] args) {
    HelloWorld o = new HelloWorld();
    System.out.println("Hello " + o.getWord("World"));
  } 

  public String getWord(String t) {
    %match(String t) {
      "World"  -> { return "World";}
      _        -> { return "Unknown"; }
    }
  }

  public void revisited(String t) {
    %match(String t) {
      (_*,'e',_*) -> { System.out.println("we have found a 'e'"); }
    }

    %match(String t) {
      (before*,'e',after*) -> { 
        System.out.println("we have found a 'e'" +
                           " after " + `before* +
                           " but before " + `after*); 
      }
    }

    %match(String t) {
      (before*,'o') -> { /* ... */ }
    }

    %match(String t) {
      (before*,'e',_*,'o') -> { /* ... */ }
    }

    %match(String t) {
      (_*,'l','l',_*) -> { /* ... */ }
    }

    %match(String t) {
      (_*,'ll',_*) -> { /* ... */ }
    }

  }

  public void re_revisited(String t) {
    %match(String t) {
      (x,_*,'ll',_*,y) -> { /* ... */ }
    }

    %match(String t) {
      (x,_*,'ll',_*,y) -> { /* ... */ }
      (x,y,y,x)        -> { /* we have found a palindrome */ }
    }

    %match(String t) {
      (_*,'a',_*,'a',_*,'a',_*) -> { /* look for 3 'a' in a string */ }
      (_*,x@'a',_*,x,_*,x,_*)   -> { /* look for 3 'a' in a string */ }
    }
   
  }

}
