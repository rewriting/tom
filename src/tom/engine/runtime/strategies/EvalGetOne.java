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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import aterm.ATerm;

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

