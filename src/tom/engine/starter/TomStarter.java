package jtom.starter;

import jtom.*;

import aterm.*;

/**
 * The TomStarter "plugin". Only here to initialize the TomEnvironment.
 */
public class TomStarter extends TomGenericPlugin {

  ATerm termToRelay;
  String fileName = null;

  public void setInput(ATerm term) {
    termToRelay = term;
    fileName = ((AFun)term).getName();
  }

  public ATerm getOutput() {
    return termToRelay;
  }

  public void run() {
      TomEnvironment env = TomEnvironment.getInstance();//create();
    env.initInputFromArgs();
    env.updateEnvironment(fileName);
  }

}
