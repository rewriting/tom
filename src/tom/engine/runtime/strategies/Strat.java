package jtom.runtime.strategies;

import EDU.oswego.cs.dl.util.concurrent.*;
import aterm.*;
import java.util.*;
import jtom.runtime.*;

public abstract class Strat extends FJTask {
  protected StratList strats;    
  protected Strat parent = null;
  protected Collection subjects = null;
  protected ATerm subject = null;
  protected Collection results = new HashSet();
  protected boolean multi = false;
  protected boolean terminated = false;
  protected boolean canFail = true;

  //public void run() {}

  //{{{ public void initialize(StratList strats)
  
  public void initialize(StratList strats) {
    this.strats = strats;
    for (int i = 0;i < strats.length();i++) {
      strats.get(i).setParent(this);
    }
  }
  // }}} 

  public void setParent(Strat parent) {
    this.parent = parent;
  }

  //{{{ public void clean()

  public void clean() {
    this.reset();
    subjects = null;
    subject = null;
    results = new HashSet();
    multi = false;
    terminated = false;
    canFail = true;
    for (int i = 0;i < strats.length();i++) {
      strats.get(i).clean();
    }
  }
  // }}}  

  public void setSubject(ATerm subject) {
    this.subject = subject;
  }

  public void setSubject(Collection subjects) {
    this.subjects = subjects;
    this.multi = true;
  }

  public StratList getArgument(int n) {
    return strats;
  }

  //{{{ public void invoke(Strat t)
  
  public void invoke(Strat t) {
    try {
      invoke((FJTask)t);
    }
    catch (Exception e) {}
  }
  // }}}        

  //{{{ public Collection getAnswer() throws InterruptedException
  
  public Collection getAnswer() throws InterruptedException {
    if (!terminated) {
      throw new InterruptedException();
    }	
    return results;
  }
  // }}}        

  //{{{ protected synchronized void setTerminated()
  
  protected synchronized void setTerminated() {
    terminated = true;
    notifyAll(); 
  }
  // }}}        

  //{{{ public void print()

  public void print() {
    System.out.print(this + "{\n");
    for (int i = 0; i < strats.length(); i++) {
      strats.get(i).print();
    }
    System.out.print("}\n");
    return;
  }
  // }}}  

  //{{{ public Strat getClone()
  
  public Strat getClone() {
    try { 
      Strat clone = (Strat)(this.getClass()).newInstance();
      clone.initialize(strats.getClone());
      return clone;
    }
    catch (Exception e) {
      System.out.println(this + " problem while making clone");
    }
    return null;
  }
  // }}}

  public boolean canFail() {
    return canFail;
  }

  public void noFail() {
    canFail = false;
    return;
  }

  abstract public void addResults(Strat t, Collection res);

  //{{{ public boolean equals(Object o) {
  
  public boolean equals(Object o) {
    if(this == o) return true;

    Strat s = null;
    if(o instanceof Strat) {
      s = (Strat)o;
    } else {
      return false;
    }

    if(!getClass().equals(o.getClass())) {
      return false;
    }

    if(this instanceof Rule) { 
      return ((Rule)this).getCollect().equals(((Rule)o).getCollect());
    } else {
      StratList sl1 = this.getArgument(0);
      StratList sl2 = s.getArgument(0);
      if(sl1==sl2) {
	return true;
      } else if(sl1==null || sl2==null) {
	return false;
      }
      return sl1.equals(sl2);
    }
  }
  // }}}        


}
