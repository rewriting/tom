package debug;

import org.antlr.runtime.CharStream;

/**
 * TODO : "clean" this... <br/>
 * 
 * <b>PROS :</b>
 * <ul>
 * <li>This is a singleton, so there is no need to create and pass around an instance of this.</li>
 * <li>This is cleaner than a public static variable in a randomly chosen class...</li>
 * <li>This was good enough to highlight what was going wrong when ANTLR generated Parser was called</li>
 * </ul>
 * <b>CONS :</b>
 * <ul>
 * <li>It wont work if several HostParsers are working at the same time (but is it useful ?)</li>
 * <li>Request a lot (not that much, but still..) to be added in HostParser and ANTLR generated Parser</li>
 * </ul>
 * <b>ADDITIONAL FEATURES :</b>
 * <ul>
 * <li>it could be useful to print infos about what action is performed at current indent level</li>
 * <li>maybe a "multiton" based on parsed file name could be usefull</li>
 * <li>could be nice to create an HostParserDebuggable Interface</li>
 * </ul>
 */
public class HostParserDebugger {

  private int imbricationLevel;
  
  public static boolean isOn(){
    return instance!=null;
  }
  
  public static void turnOn(){
    getInstance();
  }
  
  public static void reset(){
    instance = new HostParserDebugger();
  }
  
  // singleton =============================
  private static HostParserDebugger instance = null;
  
  private HostParserDebugger(){
    imbricationLevel=0;
  }
  
  public static HostParserDebugger getInstance(){
    if(instance==null){
      instance = new HostParserDebugger();
    }
    
    return instance;
  }
  //singleton =============================
 
  public void debugNewCall(String classDesc,
                           CharStream input,
                           String additionnalInfos){
    
    imbricationLevel++;
    
    String infoString = 
      futil.StringIndent.indent(
          "->\n",
          imbricationLevel-1,
          2)
     +futil.StringIndent.indent(
          "[>>"+classDesc+"] ("+additionnalInfos+")"+"\n"
          +"-------------------------\n"
          +"--- charstream before ---\n"
          +"-------------------------\n"
          + futil.ANTLRCharStreamPrint.getLA1Visual(input)+"\n"
          +"-------------------------\n",
          imbricationLevel,
          2);

    System.out.print(infoString);
  }
  
  public void debugReturnedCall(String classDesc,
                                CharStream input,
                                String additionnalInfos){
    imbricationLevel--;
    
    String infoString = 
     futil.StringIndent.indent(
         "-------------------------\n"
         +"--- charstream after ----\n"
         +"-------------------------\n"
         +futil.ANTLRCharStreamPrint.getLA1Visual(input)+"\n"
         +"[<<"+classDesc+"] ("+additionnalInfos+")"+"\n"
         +"-------------------------\n",
          imbricationLevel+1,
          2)
     +futil.StringIndent.indent(
         "<-\n",
         imbricationLevel,
         2);
    
    
    System.out.print(infoString);
  }
}
