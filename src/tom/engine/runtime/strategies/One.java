package jtom.runtime.strategies;

import EDU.oswego.cs.dl.util.concurrent.*;
import aterm.*;
import java.util.*;
import jtom.runtime.*;

public class One extends Strat {
  private int callbacksExpected = 0;
  private volatile int callbacksReceived = 0;
  private Collection childs = new HashSet();

  public One() { }

  public One(StratList strats) {
    this.initialize(strats);
  }
    
  public void run() {

    if (subject == null && subjects == null) {
      System.out.println(this + " subject not initialized");
      return;
    }
	
    if (subject != null) {
      // split using strats
      for (int i = 0; i < strats.length(); i++) {
	try {
	  Strat task = strats.get(i);
	  task.setSubject(subject);
	  callbacksExpected++;
	  task.fork();
	} catch(Exception e) {
	  System.out.println("Error during fork() in One");
	  // TODO
	}
      }

      // join
      if (callbacksExpected == 0) {
	// no split...failure
	setTerminated();
	if (this.parent != null) { parent.addResults((Strat)this, results); }
	return;
      }

      while (results.isEmpty() && (callbacksReceived < callbacksExpected)) { yield(); }
      setTerminated();
      if (this.parent != null) { parent.addResults((Strat)this, results); }

      // do some cleanup
      Iterator it2 = childs.iterator();
      while(it2.hasNext()) {
	Strat child = (Strat) it2.next();
	child.cancel();
      }

      return;
    }

    if (subjects != null) {
      EvalGetAll getall = new EvalGetAll(this, subjects);
      invoke(getall);
      setTerminated();
      if (this.parent != null) { parent.addResults((Strat)this, results); }
      return;
    }
    return;
  }

  //{{{ public synchronized void addResults(Strat t, Collection res)

  public synchronized void addResults(Strat t, Collection res) {
    if (!terminated) {
      results.addAll(res);
      if (!results.isEmpty())
      {
        this.setTerminated();
      }
    } 
    ++callbacksReceived;
  }
  //}}}        

//{{{ public void clean()
  
  public void clean() {
    this.reset();
    subjects = null;
    subject = null;
    results = new HashSet();
    multi = false;
    terminated = false;
    canFail = true;
    callbacksExpected = 0;
    callbacksReceived = 0;
    childs = new HashSet();
    for (int i = 0;i < strats.length();i++) {
      strats.get(i).clean();
    }
  }
// }}}  

}

