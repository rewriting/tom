package jtom.runtime.strategies;

import EDU.oswego.cs.dl.util.concurrent.*;
import aterm.*;
import java.util.*;
import jtom.runtime.*;

public class EvalGetOne extends Strat {
    private Strat strat;
    private Collection subjects;
    private int callbacksExpected = 0;
    private volatile int callbacksReceived = 0;
    private Collection childs = new HashSet();

    public EvalGetOne() { }

    public EvalGetOne(Strat strat, Collection subjects) {
	this.strat = strat;
	this.setParent(strat);
	this.subjects = subjects;
	// don't forget : always use a copy !!
    }

    public void initialize(StratList strats) {
	// do nothing
    }

    public void run() {

	// split using strats
	Iterator it = subjects.iterator();
	try {
	    while (it.hasNext()) {

		Strat task = strat.getClone();
		task.setParent(this);
		task.setSubject((ATerm)it.next());
		// we have to stop tasks when they are no more needed
		childs.add(task);
		callbacksExpected++;
		task.fork();
	    }
	}
	catch(Exception e) {
	    System.out.println(e);
	    e.printStackTrace();
	    // TODO
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

    public synchronized void addResults(Strat t, Collection res) {
	results.addAll(res);
	++callbacksReceived;
    }

}

