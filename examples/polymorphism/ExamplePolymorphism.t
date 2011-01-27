/*
 * Copyright (c) 2004-2010, INPL, INRIA
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
package polymorphism;

//import typeinference.examplepolymorphism.example.types.*;
public class ExamplePolymorphism{
  /*
  %gom {
    module Example
      abstract syntax
      Type<T> = f(nType:T)

      First = a()
  }
   */

  public static class Type<T> {
    public Type() {}
  }

  public static class First {
    public First() {}
  }

  public static class f<T> extends Type<T> {
    public T nType;
    public f(T t) { nType = t; }
    public T getnType() { return nType; }
    public String symbolName() { return "f"; }
    public String toString() { return "f(" + nType + ")"; }
  }

  public static class a extends First { 
    public a() { } 
   
    public String toString() { return "a()"; }
  }

  %typeterm Type<T> {
    implement { Type }
    is_sort(t) { ($t instanceof Type) }
    equals(t1,t2) { ($t1==$t2) }
  }
  %typeterm First {
    implement { First }
    is_sort(t) { ($t instanceof First) }
    equals(t1,t2) { ($t1==$t2) }
  }
  %op Type<T> f(nType:T) {
    is_fsym(t) { ($t instanceof f) }
    get_slot(nType, t) { ((f<$T>)$t).getnType() }
    make(t) { new f($t) }
  }
  %op First a() {
    is_fsym(t) { ($t instanceof f) }
    make() { new a() }
  }

  public static void main(String[] args) {
    Type<First> tt = `f(a());
    %match{
      f(_) << Type tt -> { System.out.println(`tt); }
    }
  }
}
