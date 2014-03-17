package acmatching;
/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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
 * 	- Neither the name of the Inria nor the names of its
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

import acmatching.testac.term.types.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.matchers.JUnitMatchers;
import static org.hamcrest.CoreMatchers.*;
import org.hamcrest.Matcher;
import org.junit.Test;

public class TestAC {

  %include{ int.tom }
  %include{ intarray.tom }
  %gom {
    module Term
      imports
      abstract syntax

      T =
      | a()
      | b()
      | c()
      | f(T*)
      | g(T*)

      f:AC{}
      g:AC{}

  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestAC.class.getName());
  }

  @Test
  public void test1() {
    T t1 = `f(a(),b(),c());
    Set<T> bagX = new HashSet<T>();
    Set<T> bagY = new HashSet<T>();

    %match(t1) {
      f??(X*,a(),Y*) -> {
        bagX.add(`X);
        bagY.add(`Y);
      }
    }

    assertThat("f() is solution", bagX, hasItem(`f()));
    assertThat("b() is solution", bagX, hasItem(`b()));
    assertThat("c() is solution", bagX, hasItem(`c()));
    assertThat("f(b(),c()) is solution", bagX, hasItem(`f(b(),c())));
    assertThat("f(c(),b()) is solution", bagX, hasItem(`f(c(),b())));
    assertEquals("bagX==bagY", bagX, bagY);
    assertEquals("bagX.size==4", 4, bagX.size());
  }

  @Test
    public void test2() {
      T t1 = `f(a(),b(),a());
      Set<T> bagX = new HashSet<T>();
      Set<T> bagY = new HashSet<T>();

      %match(t1) {
        f??(X*,X*,Y*) -> {
          bagX.add(`X);
          bagY.add(`Y);
        }
      }

      assertThat("f() is solution of X", bagX, hasItem(`f()));
      assertThat("f(a(),a()) is solution of X", bagX, hasItem(`f(a(),a())));
      assertThat("t1 is solution ofYX", bagY, hasItem(t1));
      assertThat("b() is solution of Y", bagY, hasItem(`b()));
      assertEquals("bagX.size==2", 2, bagX.size());
      assertEquals("bagY.size==2", 2, bagY.size());

      Set<T> bagXX = new HashSet<T>();
      Set<T> bagYY = new HashSet<T>();
      %match(t1) {
        f??(XX*,YY*,YY*) -> {
          bagXX.add(`XX);
          bagYY.add(`YY);
        }
      }
      assertEquals("bagX==bagYY",bagX,bagYY);
      assertEquals("bagY==bagXX",bagY,bagXX);

    }

  @Test
    public void test3() {
      T t1 = `f(a(),b(),a());
      Set<T> bagX = new HashSet<T>();
      Set<T> bagY = new HashSet<T>();

      %match(t1) {
        f??(C*,X*,X*,Y*,Y*) -> {
          bagX.add(`X);
          bagY.add(`Y);
        }
      }

      assertThat("f() is solution of X", bagX, hasItem(`f()));
      assertThat("f() is solution of Y", bagY, hasItem(`f()));
      assertThat("f(a(),a()) is solution of X", bagX, hasItem(`f(a(),a())));
      assertThat("f(a(),a()) is solution of Y", bagY, hasItem(`f(a(),a())));
      assertEquals("bagX.size==2", 2, bagX.size());
      assertEquals("bagY.size==2", 2, bagY.size());
    }

  @Test
  public void test4() {
    T t1 = `f(a(),b(),b(),a());
    Set<T> bagX = new HashSet<T>();

    %match(t1) {
      f??(X*,Y*) && X==Y -> {
        bagX.add(`X);
      }
    }

    assertThat("f(a(),b()) is solution of X", bagX, hasItem(`f(a(),b())));
    assertThat("f(b(),a()) is solution of X", bagX, hasItem(`f(b(),a())));
    assertEquals("bagX.size==1", 1, bagX.size());
  }

  private static Matcher<java.lang.Iterable<T>> hasItem(T t) {
    return JUnitMatchers.hasItem(t);
  }
}
