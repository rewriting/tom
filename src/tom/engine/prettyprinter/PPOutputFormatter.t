package tom.engine.prettyprinter;

import java.io.*;

public class PPOutputFormatter {

  public void put(String s, PPTextPosition start, PPTextPosition end) {

    System.out.println(s);
  }

  public void put(String s) {

    System.out.println(s);
  }
}
