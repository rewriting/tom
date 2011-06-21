package debug;

import org.antlr.runtime.CharStream;

public class HostParserDebugger {

  private int imbricationLevel;
  
  public static boolean isOn(){
    return instance!=null;
  }
  
  public static void turnOn(){
    getInstance();
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
