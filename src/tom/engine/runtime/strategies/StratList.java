package jtom.runtime.strategies;

import EDU.oswego.cs.dl.util.concurrent.*;
import aterm.*;
import java.util.*;
import jtom.runtime.*;

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
