package jtom.starter;

import jtom.*;
import aterm.*;

/**
 * The TomStarter "plugin". Only here to initialize the TomEnvironment.
 */
public class TomStarter extends TomGenericPlugin {

  ATerm termToRelay;
  String fileName = null;

  public TomStarter() {
    super("TomStarter");
  }

  public void setTerm(ATerm term) {
    termToRelay = term;
    fileName = ((AFun)term).getName();
  }

  public ATerm getTerm() {
    return termToRelay;
  }

  public void run() {
    // We need here to create the environment : 
    // We need to be sure we don't have side effects with the environment singleton
    TomEnvironment env = TomEnvironment.create();
    env.initInputFromArgs();
    env.updateEnvironment(fileName);
  }

}
