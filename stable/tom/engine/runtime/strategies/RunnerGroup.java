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
