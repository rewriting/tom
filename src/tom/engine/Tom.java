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
    rootLogger = Logger.getLogger("jtom", "jtom.TomMessageResources");

    rootLogger.setLevel(Level.WARNING);
    rootLogger.setUseParentHandlers(false);

    Handler ch = new ConsoleHandler();
    ch.setLevel(Level.ALL); // that way, all the logs accepted by the logger are printed
    ch.setFormatter( new TomBasicFormatter() );

    rootLogger.addHandler(ch);

//     try{
//       Handler fh = new FileHandler("log");
//       rootLogger.addHandler(fh);
//     } catch(Exception e) { 
//       System.out.println("No log output"); 
//     }

    OptionManager om = new TomOptionManager();

    return TomServer.exec(args, om, "jtom");
  }

  public static void main(String[] args) {
    exec(args);
  }

}
