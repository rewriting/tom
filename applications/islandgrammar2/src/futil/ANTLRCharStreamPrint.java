package futil;

import org.antlr.runtime.CharStream;

public class ANTLRCharStreamPrint {

  public static String getLA1Visual(CharStream input){
	  return getLA1Visual(input, 5, 5);
  }
	
  public static String getLA1Visual(CharStream input, int charsbeforeLA1, int charsafterLA1){
	  
	String beforeLA1 = "";
	String LA1andAfter = "";
	
	
	// using home made loop instead of substring to always "stay in" CharStream 
	int tmpChar;
	int offset;
	
	tmpChar = -1;
	offset = 0;
	while((tmpChar=input.LA(-1-offset))!=-1 && offset<charsbeforeLA1){
		beforeLA1 = (char)tmpChar + beforeLA1;
		offset++;
	}
	
	// because we need 'charsafterLA1+1' chars (this loop read LA1)
	tmpChar = -1;
	offset = 0;
	while((tmpChar=input.LA(1+offset))!=-1 && offset<=charsafterLA1){
		LA1andAfter = LA1andAfter+(char)tmpChar;
		offset++;
	}
    
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
