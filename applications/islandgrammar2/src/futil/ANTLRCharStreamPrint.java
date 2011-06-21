package futil;

import org.antlr.runtime.CharStream;

public class ANTLRCharStreamPrint {

  public static String getLA1Visual(CharStream input){
    
    String beforeLA1 = input.substring(input.index()-5, input.index()-1);
    String LA1andAfter = input.substring(input.index(), input.index()+5);
    
    beforeLA1 = beforeLA1.replaceAll("\n", "\\\\n");
    beforeLA1 = beforeLA1.replaceAll("\r", "\\\\r");
    beforeLA1 = beforeLA1.replaceAll("\t", "\\\\t");
    LA1andAfter = LA1andAfter.replaceAll("\n", "\\\\n");
    LA1andAfter = LA1andAfter.replaceAll("\r", "\\\\r");
    LA1andAfter = LA1andAfter.replaceAll("\t", "\\\\t");
    
    String visual_index = "";
    for(int i=0; i<beforeLA1.length(); i++){
      visual_index+=" ";
    }
    visual_index += "^";
    
   return "'"+beforeLA1+LA1andAfter+"'"+"\n"+" "+visual_index+" "+input.getLine()+":"+input.getCharPositionInLine();
  }
  
}
