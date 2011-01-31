/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

public class PathFinder {

  //%include {charlist.tom}
  %include { string.tom }

  public final static void main(String[] args) {
    PathFinder test = new PathFinder();
    String s1 = "aaaabaaaabaaaabaaaabaaaabaaaabaaaabaaabaa";
    System.out.println(test.f2(s1));

    String s2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    System.out.println(test.f2(s2));

    String s3 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaa";
    test.f3(s1);
    test.f4(s3);
  }

  /*
  public String f(String s) {
    %match(String s) {
      ("h","e",X1*,x@"l",y@"o",_*) -> {
        System.out.println("X1   = " + `X1*);
        System.out.println("char = " + `x);
        System.out.println("char = " + `y);
        return s;
      }
      _        -> { return "unknown"; }
    }
  }
  */
 
  public int doubleB(String s) {
    int count = 0;
    %match(String s) {
      concString(_*,'b',_*,'b',_*) -> {
        count++; 
      }
    }
    return count;
  }
  
  public int doubleBwhen(String s) {
    int count = 0;
    %match(String s) {
      concString(_*,x,_*,x,_*)  /*when equalsChar(x)*/ -> {
        count++; 
      }
    }
    return count;
  }
  
  int cpt=0;
  public String f2(String s) {
    String res = "";
    %match(String s) {
      //(X1*,"ab",X2*,"b",X3*,"b",X4*,"b",X5*,"b",X6*,"b",X7*) -> {
      concString(X1*,'b',X2*,'b',X3*,'b',X4*,'b',X5*,'b',X6*,'b',X7*) -> {
        res += `X1 + " " + `X2 + " " + `X3 + " " + `X4 + " " + `X5 + " " + `X6 + " " + `X7;
        res += "\n";
      }
    }
    return res;
  }

  public String f3(String s) {
    System.out.println("Testing with string "+s);
    long startChrono;
    startChrono = System.currentTimeMillis();
    %match(String s) {
      concString(X1*,x,X2*,x,X3*,x,X4*,x,X5*,x,X6*,x,X7*) -> {
        if(equalsChar(`x)) {
          //System.out.println(`X1 + " " + `X2 + " " + `X3 + " " + `X4 + " " + `X5 + " " + `X6 + " " + `X7);
        }
      }
    }
    System.out.println("Using java if : "+(System.currentTimeMillis()-startChrono)+ " ms)");
    startChrono = System.currentTimeMillis();
    %match(String s) {
      concString(X1*,'b',X2*,'b',X3*,'b',X4*,'b',X5*,'b',X6*,'b',X7*) -> {
        //System.out.println(`X1 + " " + `X2 + " " + `X3 + " " + `X4 + " " + `X5 + " " + `X6 + " " + `X7);
      }
    }
    System.out.println("Using 'b' : "+(System.currentTimeMillis()-startChrono)+ " ms)");
    startChrono = System.currentTimeMillis();
    %match(String s) {
      concString(X1*,x,X2*,x,X3*,x,X4*,x,X5*,x,X6*,x,X7*) /*when equalsChar(x)*/ -> {
        //System.out.println(`X1 + " " + `X2 + " " + `X3 + " " + `X4 + " " + `X5 + " " + `X6 + " " + `X7);
      }
    }
    System.out.println("Using when equalsChar : "+(System.currentTimeMillis()-startChrono)+ " ms)");
    startChrono = System.currentTimeMillis();
    %match(String s) {
      concString(X1*,a,X2*,b,X3*,c,X4*,d,X5*,e,X6*,f,X7*) /*when equalsChar(a),equalsVar(a,b),equalsVar(b,c),equalsVar(c,d),equalsVar(d,e),equalsVar(e,f)*/ -> {
        //System.out.println(`X1 + " " + `X2 + " " + `X3 + " " + `X4 + " " + `X5 + " " + `X6 + " " + `X7);
      }
    }
    System.out.println("Using when equalsChar and equalsVar : "+(System.currentTimeMillis()-startChrono)+ " ms)");
    return s;
  }

  public String f4(String s) {
    System.out.println("Testing with string "+s);
    long startChrono;
    startChrono = System.currentTimeMillis();
    %match(String s) {
      concString(X1*,'b',X2*,'b',X3*,'b',X4*,'b',X5*,'b',X6*,'b',X7*) -> {
        //System.out.println(`X1 + " " + `X2 + " " + `X3 + " " + `X4 + " " + `X5 + " " + `X6 + " " + `X7);
      }
    }
    System.out.println("Using 'b' : "+(System.currentTimeMillis()-startChrono)+ " ms)");
    startChrono = System.currentTimeMillis();
    %match(String s) {
      concString(X1*,x,X2*,x,X3*,x,X4*,x,X5*,x,X6*,x,X7*) /*when equalsChar(x)*/ -> {
        //System.out.println(`X1 + " " + `X2 + " " + `X3 + " " + `X4 + " " + `X5 + " " + `X6 + " " + `X7);
      }
    }
    System.out.println("Using when equalsChar : "+(System.currentTimeMillis()-startChrono)+ " ms)");
    startChrono = System.currentTimeMillis();
    %match(String s) {
      concString(X1*,a,X2*,b,X3*,c,X4*,d,X5*,e,X6*,f,X7*) /*when equalsChar(a),equalsVar(a,b),equalsVar(b,c),equalsVar(c,d),equalsVar(d,e),equalsVar(e,f)*/ -> {
        //System.out.println(`X1 + " " + `X2 + " " + `X3 + " " + `X4 + " " + `X5 + " " + `X6 + " " + `X7);
      }
    }
    System.out.println("Using when equalsChar and equalsVar : "+(System.currentTimeMillis()-startChrono)+ " ms)");
    return s;
  }

  private boolean equalsChar(char pattern) {
    return pattern == 'b';
  }

  private boolean equalsVar(char pattern,char c) {
    return (pattern == c);
  }
}
