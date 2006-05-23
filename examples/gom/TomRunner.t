package gom;

import tom.engine.Tom;
import java.util.*;
import java.io.*;

/**
  * This example show how to call Tom from a class, and compile many files
  * to use it, populate a directory "bench/src" with tom files, and run
  * java -Dtom.home=${TOM_HOME} gom.TomRunner
  * It will compile all tom files in that directory, and loop
  */
class TomRunner {

  public static void main(String[] args) {
    int maxiter = 1000;
    if (args.length >= 1) {
      maxiter = Integer.parseInt(args[0]);
    }
    int offset = 4;
    File path = new File("bench/src");
    File[] files = path.listFiles();
    String[] params = new String[files.length + offset];
    params[0] = "-X";
    params[1] = System.getProperty("tom.home")+"/Tom.xml";
    params[2] = "-d";
    params[3] = "bench/gen";
    try {
      for (int i = 0; i < files.length; i++) {
        params[i+offset] = files[i].getCanonicalPath();
      }
    } catch (IOException e) {
      System.out.println("File not found ?");
    }
    
    int cnt = 0;
    while(cnt++ < maxiter) {
      int res = Tom.exec(params);    
      System.out.println("Generation: " + res);
      System.out.println("Iteration: "+cnt);
    }
  }
}
