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

import EDU.oswego.cs.dl.util.concurrent.*;

public class RunnerGroup {

  private FJTaskRunnerGroup group = null;    

  protected RunnerGroup(int procs) {
    group = new FJTaskRunnerGroup(procs);
  }

  private static RunnerGroup _instance = null;

  private static final Object classLock = RunnerGroup.class;

  public static RunnerGroup instance() {
    int procs = 2;
    return instance(procs);
  }

  public static RunnerGroup instance(int procs) {
    synchronized(classLock) {
      if (null == _instance) {
        _instance = new RunnerGroup(procs);
      }
      return _instance;
    }
  }

  public FJTaskRunnerGroup getGroup() {
    return group;
  }

  public void invoke(FJTask t) throws InterruptedException {
    if (null != group) {
      group.invoke(t);
    }
    else {
      throw new Error(this.getClass() + " Not initialized");
    }
  }

  public void stats() {
    if (null != group) {
      group.stats();
    }
    else {
      throw new Error(this.getClass() + " Not initialized");
    }
  }

}
