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

import jtom.runtime.Collect3;

public class Rule extends Strat {
  private Collect3 collect;

  public Rule(Collect3 collect) {
    this.collect = collect;
  }

  public Rule(Strat parent, Collect3 collect) {
    this.collect = collect;
    setParent(parent);
  }
    
  public void run() {
    if (subject == null && subjects == null) {
      System.out.println(this + " subject not initialized");
      return;
    }
	
    if (subject != null) {
      collect.apply(subject, results, this);
      setTerminated();
      if (this.parent != null) { parent.addResults((Strat)this, results); }
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

  protected Collect3 getCollect() { 
    return collect;
  }

  public Strat getClone() {
    Rule clone = new Rule(collect);
    return clone;
  }

  public void print() {
    System.out.print(this + " Collect: " + collect + ";\n");
    return;
  }
    
  public void addResults(Strat t, Collection res) {
    results.addAll(res);
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
  }
  // }}}  

  }
