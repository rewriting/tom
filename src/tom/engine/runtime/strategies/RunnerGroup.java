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
