package jtom;

import java.util.logging.*;

/**
 *
 */
public class Tom {

  /**
   * The current version of the TOM compiler. 
   */
  public final static String VERSION = "3.0alpha";

  protected static Logger rootLogger;

  public static int exec(String[] args) {
    rootLogger = Logger.getLogger("jtom");

    OptionManager om = new TomOptionManager();

    return TomServer.exec(args, om, "jtom");
  }

  public static void main(String[] args) {
    exec(args);
  }

}
