package gom;

import tom.gom.Gom;
import tom.gom.tools.GomEnvironment;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.StringBufferInputStream;

/**
  * This example shows how gom can be run using a string as input in a program
  * This is what Tom does when processing a %gom construct
  */
class GomRunner {

  public static void main(String[] args) {
    String module = %[
      module Allo
      abstract syntax
      Huile = Clock()
      ]%;

    String[] params = {"-X","/Users/tonio/workspace/jtom/src/dist/Gom.xml","-d", "gom/coin", "-"};
    InputStream tmpIn = System.in;
    System.setIn(new DataInputStream(new StringBufferInputStream(module)));
    int res = Gom.exec(params);    
    System.setIn(tmpIn);
    System.out.println("Generation: " + res);

    System.out.println("Mapping: " + GomEnvironment.getInstance().getLastGeneratedMapping());
  }
}
