/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2004, Antoine Reilles
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
 *
 * INRIA, Nancy, France 
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package jtom.runtime.strategies;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class StratList {
  protected LinkedList stratlist;

  public StratList() {
    stratlist = new LinkedList();
  }

  public StratList(List l) {
    this.stratlist = new LinkedList(l);
  }

  //{{{ public Strat getFirst()
  
  public Strat getFirst() {
    Strat first = null;
    if (!stratlist.isEmpty()) {
      first = (Strat)stratlist.get(0);
    } else {
      System.out.println("c'est étrange: " + stratlist);
    }
    return first;
  }
  // }}}

  //{{{ public StratList getNext()
  
  public StratList getNext() {
    StratList next = null;
    if (stratlist.size() > 1) {
      next = new StratList(stratlist.subList(1,stratlist.size()));
    }
    else {
      next = new StratList();
    }
    return next; 
  }
  // }}}

  public boolean isEmpty() {
    return stratlist.isEmpty();
  }

  public StratList insert(Strat strat) {
    stratlist.addFirst(strat);
    return new StratList(stratlist);
  }

  public void add(Strat strat) {
    stratlist.add(strat);
  }

  public int length() {
    return stratlist.size();
  }

  public Strat get(int i) {
    return (Strat)stratlist.get(i);
  }

  //{{{ public StratList getClone()

  public StratList getClone() {
    StratList clone = new StratList();
    ListIterator it = stratlist.listIterator();
    while (it.hasNext()) {
      clone.add(((Strat)it.next()).getClone());
    }
    return clone;
  }
  // }}}

  //{{{ public boolean equals(Object o)

  public boolean equals(Object o) {
    StratList sl = null;
    if(o instanceof StratList) {
      sl = (StratList)o;
    } else {
      return false;
    }
    return stratlist.equals(sl.stratlist);
  }
  // }}}

  }
