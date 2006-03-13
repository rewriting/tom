package gom;

import tom.gom.Gom;
import tom.gom.tools.GomEnvironment;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.StringBufferInputStream;

class GomRunner {

  public static void main(String[] args) {
    String module = %[
      module Allo
      public
        sorts Huile
      abstract syntax
        Clock() -> Huile
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
