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

import java.util.Collection;
import java.util.HashSet;

import EDU.oswego.cs.dl.util.concurrent.FJTask;
import aterm.ATerm;

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
