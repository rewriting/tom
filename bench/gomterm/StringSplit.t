/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
package gomterm;

public class StringSplit {
  %include {string.tom}

  public static final void main(String[] args) {
    int maxcount = 0;
    try {
      maxcount = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.err.println("Usage: java gom.StringSplit <maxcount>");
      return;
    }
    for(int i = 1; i<=maxcount; i++) {
      System.out.print(i+"\t");
      System.out.println(benchnew(i));
    }
    System.out.println();
    for(int i = 1; i<=maxcount; i++) {
      System.out.print(i+"\t");
      System.out.println(benchold(i));
    }
    System.out.println();

    for(int i = 200; i<=(200+(maxcount*50)); i+=50) {
      System.out.print(i+"\t");
      System.out.println(benchfast(i));
    }
    System.out.println();
    for(int i = 200; i<=(200+(maxcount*50)); i+=50) {
      System.out.print(i+"\t");
      System.out.println(benchnew(i));
    }
    System.out.println();
  }

  public static double benchfast(int i) {
    String test = new String(makeInput(0));
    String mark = "";
		long startChrono = System.currentTimeMillis();
    for(int j = 0; j < i; j++) {
    %match(String test) {
      concString(login*,':',pass*,':',uid*,':',gid*,':',fname*,':',path*,':',shell*) -> {
        //mark = `login* + `pass* + `rest*;
      }
    }
    }
		long stopChrono = System.currentTimeMillis();
    return ((stopChrono-startChrono)/1000.);
  }

  public static double benchnew(int i) {
    String test = new String(makeInput(0));
    String mark = "";
		long startChrono = System.currentTimeMillis();
    for(int j = 0; j < i; j++) {
    %match(String test) {
      concString(login*,x,pass*,x,uid*,x,gid*,x,fname*,x,path*,x,shell*) -> {
        //mark = `login* + `pass* + `rest*;
      }
    }
    }
		long stopChrono = System.currentTimeMillis();
    return ((stopChrono-startChrono)/1000.);
  }

  public static double benchold(int i) {
    String test = new String(makeInput(0));
    String mark = "";
		long startChrono = System.currentTimeMillis();
    for(int j = 0; j < i; j++) {
    %match(String test) {
      concString(login*,a,pass*,b,uid*,c,gid*,d,fname*,e,path*,f,shell*) -> {
        if(`a == `b && `b == `c && `c == `d && `d == `e && `e == `f && `a == ':') {
          //mark = `login* + `pass* + `rest*;
        }
      }
    }
    }
		long stopChrono = System.currentTimeMillis();
    return ((stopChrono-startChrono)/1000.);
  }

  public static String makeInput(int i) {
    String tmpres = "postfix:*:27:27:Postfix User:/var/spool/postfix:/usr/bin/false";
    String res = tmpres;
    for (int j = 0; j < i; j++)
      res = res + "\n" + tmpres;
    return res;
  }

}
