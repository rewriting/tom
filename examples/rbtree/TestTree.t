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

package rbtree;

import java.util.*;

import aterm.*;
import aterm.pure.*;
import rbtree.tree.*;
import rbtree.tree.types.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TestTree {

  private Tree1 test;

	public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestTree.class.getName());
	}

  @Before
  public void setUp() {
    test = new Tree1();
  }

  %include { tree/tree.tom }
 
  @Test
  public void testFistInsert() {
    Tree t = `emptyTree();
    t = test.insert(t,`e1());
    assertEquals(1,test.card(t));
    assertTrue(test.member(t,`e1()));
  }

  @Test
  public void testInsertTwo() {
    Tree t = `emptyTree();
    t = test.insert(t,`e1());
    t = test.insert(t,`e2());
    assertEquals(2,test.card(t));
    assertTrue(test.member(t,`e1()));
    assertTrue(test.member(t,`e2()));
  }

  @Test
  public void testInsert20() {
    assertEquals(7,depth(buildTree(20)));
  }

  @Test
  public void testInsert100() {
    assertEquals(11,depth(buildTree(100)));
  }

  @Test
  public void testInsert1000() {
    assertEquals(15,depth(buildTree(1000)));
  }

  public Tree buildTree(int nodes) {
    Tree t = `emptyTree();
    Element[] array = makeArray(nodes);
    for (int i = 0; i<array.length; i++) {
      t = test.insert(t, array[i]);
    }
    assertEquals(3*nodes,test.card(t));
    return t;
  }

  private Element[] makeArray(int n) {
    Element e1 = `e1();
    Element e2 = `e2();
    Element e3 = `e3();
    Element array[] = new Element[3*n];
    array[0] = e1;
    array[1] = e2;
    array[2] = e3;
    for(int i=1 ; i<n ; i++) {
      Element old_e1 = array[3*i+0-3];
      Element old_e2 = array[3*i+1-3];
      Element old_e3 = array[3*i+2-3];
      array[3*i+0] = `f(old_e1);
      array[3*i+1] = `f(old_e2);
      array[3*i+2] = `f(old_e3);
    }
    return array;
  }

  private int depth(Tree t) {
    %match(t) {
      emptyTree() -> { return 0; }
      node[lhs=l,rhs=r] -> {
        int ldepth = depth(`l);
        int rdepth = depth(`r);
        if (ldepth>rdepth) {
          return ldepth+1;
        } else {
          return rdepth+1;
        }
      }
    }
    return 0;
  }
}
