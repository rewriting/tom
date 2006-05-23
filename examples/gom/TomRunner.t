package gom;

import tom.engine.Tom;
import java.util.*;
import java.io.*;

/**
  * This example show how to call Tom from a class, and compile many files
  */
class TomRunner {

  public static void main(String[] args) {
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
    while(cnt++ < 1000) {
      int res = Tom.exec(params);    
      System.out.println("Generation: " + res);
    }
    System.out.println("Iterations "+cnt);
  }
}
