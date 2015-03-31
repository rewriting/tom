/*
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
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
 *
 * Antoine Reilles   e-mail: Antoine.Reilles@loria.fr
 */
package gom;

import static org.junit.Assert.*;
import org.junit.Test;
import gom.testsorthook.m.types.*;

public class TestSortHook {

  %gom {
    module m
    abstract syntax
    Sort1 = a() | b()
    Sort2 = c() | d()
    sort Sort1:block() {
      public String hookSort1() {
        return "1"+symbolName();
      }

      /* Just to make sure the import did work */
      public Map getEmptyMap() {
        return new HashMap();
      }
    }

    sort Sort1:import() {
      import java.util.*;
    }

    sort Sort2:block() {
      public String hookSort2() {
        %match(this) {
          c() -> { return "2c"; }
          d() -> { return "2d"; }
        }
        return "plonk";
      }
    }
  }

  @Test
  public void testHook1a() {
    Sort1 my = `a();
    String res = my.hookSort1();
    assertEquals("1a",res);
  }

  @Test
  public void testHook1b() {
    Sort1 my = `b();
    String res = my.hookSort1();
    assertEquals("1b",res);
  }

  @Test
  public void testHook2c() {
    Sort2 my = `c();
    String res = my.hookSort2();
    assertEquals("2c",res);
  }

  @Test
  public void testHook2d() {
    Sort2 my = `d();
    String res = my.hookSort2();
    assertEquals("2d",res);
  }

  public final static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestSortHook.class.getName());
  }
}
