/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2004 INRIA
			                      Nancy, France.


    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the Free
    Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
    MA 02111-1307, USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
    Antoine Reilles       e-mail: Antoine.Reilles@loria.fr
*/

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
