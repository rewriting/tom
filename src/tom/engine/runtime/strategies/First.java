package jtom.runtime.strategies;

import EDU.oswego.cs.dl.util.concurrent.*;
import aterm.*;
import java.util.*;
import jtom.runtime.*;

public class First extends Strat {
  private int callbacksExpected = 0;
  private volatile int callbacksReceived = 0;
  private List childs = new ArrayList();
  private int waiter = 0;

  public First() { }

  public First(StratList strats) {
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
	  List childandres = new ArrayList();
	  childandres.add(task);
	  childandres.add(null);
	  childs.add(childandres);
	  task.fork();
	} catch(Exception e) {
	  System.out.println("Error during fork() in First");
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

      while (!terminated && (callbacksReceived < callbacksExpected)) { yield(); }
      setTerminated();
      if (this.parent != null) { parent.addResults((Strat)this, results); }

      // do some cleanup
      Iterator it2 = childs.iterator();
      while(it2.hasNext()) {
	// it should be modified when generalizing First to Strat[]
	Strat child = (Strat)((List)it2.next()).get(0);
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
    if (!multi) {
      if (!terminated) {
	// add result in childs list
	List localchild = new ArrayList();
	{
	  int index = 0;
	  ListIterator iter = childs.listIterator();
	  while (iter.hasNext()) {
	    index = iter.nextIndex();
	    localchild = (List)iter.next();
	    if (((Strat)(localchild.get(0))) == t) {
	      localchild.set(1, res);
	      // if this is the result we are waiting for, terminate
	      if (index == waiter) {
		if (!res.isEmpty()) {
		  results.addAll(res);
		  this.setTerminated();
		  ++callbacksReceived;
		  return;
		}

		if (waiter < (childs.size()-1) ) { waiter += 1 ; }
		// verify for the new waiter : the result may be already there
		Collection nextItem = (Collection)((List)childs.get(waiter)).get(1);
		if ((nextItem != null) && (!nextItem.isEmpty())) {
		  results.addAll(nextItem);
		  this.setTerminated();
		  ++callbacksReceived;
		  return;
		}
	      } 
	    }
	  }
	}
      }
      ++callbacksReceived;
    } else {
      results.addAll(res);
      this.setTerminated();
    }
  }
// }}}  

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
    childs = new ArrayList();
    waiter = 0;
    for (int i = 0;i < strats.length();i++) {
      strats.get(i).clean();
    }
  }
// }}}  

}
