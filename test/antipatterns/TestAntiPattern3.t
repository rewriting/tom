/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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

package antipatterns;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import antipatterns.testantipattern3.peano.types.*;

public class TestAntiPattern3 {
  %gom {
    module Peano
      abstract syntax
      Nat = zero()
      | suc(pred:Nat)
      | plus(x1:Nat, x2:Nat)
  }

  public Collection evaluate(Nat n) {
    Collection bag = new HashSet();
    %match(Nat n) {
      plus(!x,!y)               -> { bag.add(1); }
      zero()                    -> { bag.add(2); }
      plus(x,!suc(y))           -> { bag.add(3); }
      plus(x,!suc(x))           -> { bag.add(4); }
      plus(x,y)                 -> { bag.add(5); }
      plus(x,x)                 -> { bag.add(6); }
      plus(x,!x)                -> { bag.add(7); }
      !plus(x,!suc(y))          -> { bag.add(8); }
      !plus(x,!suc(suc(y)))     -> { bag.add(9); }
      plus(x,suc(suc(plus(y,!x))))    -> { bag.add(10); }
    }
    return bag;
  }

  @Test
  public void test1() {
    Collection bag = evaluate(`zero());
    assertTrue(!bag.contains(1));
    assertTrue(bag.contains(2));
    assertTrue(!bag.contains(3));
    assertTrue(!bag.contains(4));
    assertTrue(!bag.contains(5));
    assertTrue(!bag.contains(6));
    assertTrue(!bag.contains(7));
    assertTrue(bag.contains(8));
    assertTrue(bag.contains(9));
    assertTrue(!bag.contains(10));
  }

  @Test
  public void test2() {
    Collection bag = evaluate(`plus(zero(),zero()));
    assertTrue("1",!bag.contains(1));
    assertTrue("2",!bag.contains(2));
    assertTrue("3",bag.contains(3));
    assertTrue("4",bag.contains(4));
    assertTrue("5",bag.contains(5));
    assertTrue("6",bag.contains(6));
    assertTrue("7",!bag.contains(7));
    assertTrue("8",!bag.contains(8));
    assertTrue("9",!bag.contains(9));
    assertTrue("10",!bag.contains(10));
  }

  @Test
  public void test3() {
    Collection bag = evaluate(`plus(zero(),suc(zero())));
    assertTrue("1",!bag.contains(1));
    assertTrue("2",!bag.contains(2));
    assertTrue("3",!bag.contains(3));
    assertTrue("4",!bag.contains(4));
    assertTrue("5",bag.contains(5));
    assertTrue("6",!bag.contains(6));
    assertTrue("7",bag.contains(7));
    assertTrue("8",bag.contains(8));
    assertTrue("9",!bag.contains(9));
    assertTrue("10",!bag.contains(10));
  }

  @Test
  public void test4() {
    Collection bag = evaluate(`plus(suc(zero()),zero()));
    assertTrue("1",!bag.contains(1));
    assertTrue("2",!bag.contains(2));
    assertTrue("3",bag.contains(3));
    assertTrue("4",bag.contains(4));
    assertTrue("5",bag.contains(5));
    assertTrue("6",!bag.contains(6));
    assertTrue("7",bag.contains(7));
    assertTrue("8",!bag.contains(8));
    assertTrue("9",!bag.contains(9));
    assertTrue("10",!bag.contains(10));
  }

  @Test
  public void test5() {
    Collection bag = evaluate(`plus(suc(zero()),suc(suc(zero()))));
    assertTrue("1",!bag.contains(1));
    assertTrue("2",!bag.contains(2));
    assertTrue("3",!bag.contains(3));
    assertTrue("4",!bag.contains(4));
    assertTrue("5",bag.contains(5));
    assertTrue("6",!bag.contains(6));
    assertTrue("7",bag.contains(7));
    assertTrue("8",bag.contains(8));
    assertTrue("9",bag.contains(9));
    assertTrue("10",!bag.contains(10));
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestAntiPattern3.class.getName());
  }

}
