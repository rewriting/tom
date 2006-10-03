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
package bytecode;

public class Subject {
  static protected int i = 42;
  static private String s = "hello";
  private String myAttr = "attr";

  public static void main(String[] args) {
    Subject subject = new Subject();

    gets();
    subject.switchfct(3);
    subject.stratKiller();
  }

  public static String gets() {
    String sUp = s.toUpperCase();
    ++i;
    return sUp;
  }

  public void f() {
    for(int i = 0; i < 4; ++i) {
      System.out.println(gets());
    }
  }

  public void u() throws Exception {
    try {
      int i[] = {1, 2, 3};
      i[4] = i[2] + 1;
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new Exception();
    }
  }

  public class SubSubject {
  }

  public void fct() throws Exception {
      while(true) {
      System.out.println(i);
      if(i > 5) {
        throw new Exception();
      }
    }
  }

  public void stratKiller() {
    int i = 0;
    int j = 0;
    while(true) {
      i = 1;
      i = 2;
      j = i;
    }
  }

  public void endingStore() {
    int i = 0;
  }

  public void switchfct(int value) {
    switch(value) {
      case 3:
        value += 3;
        break;
      case 4:
        value += 4;
        break;
      case 7:
        value += 7;
        break;
      case 8:
        value += 8;
        break;
      default:
        value -= 1;
    }

    System.out.println(value);

    switch(value) {
      case -4:
        value = -4;
        break;
      case 30:
        value = 30;
        break;
    }

    System.out.println(value);
  }

  public int d() {
    String [] array = new String[3];
    int [] array2 = new int[4];
    return array.length + array2.length;
  }

  public void e() {
    int p = 1;
    p = d();
  }

  public void l() {
    Object o = new String();
    String s = (String)o;
    System.out.println(o instanceof String);
  }

  public String substring(String s, int from, int to) {
    return s.substring(from, to);
  }
}

