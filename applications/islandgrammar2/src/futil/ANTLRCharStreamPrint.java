package futil;

import org.antlr.runtime.CharStream;

/**
 * Provide a function that print ANTLR's CharStream state.
 * (in a "digest" way)
 */
public class ANTLRCharStreamPrint {

  private ANTLRCharStreamPrint(){;}
  
  /**
   * Same behavior that {@link #getLA1Visual(CharStream, int, int)}.
   * Use 5 as value for 2nd and 3rd parameter (respectively chardbeforeLA1
   * and charsafterLA1).
   * 
   * @param input
   * @return
   * @see #getLA1Visual(CharStream, int, int)
   */
  public static String getLA1Visual(CharStream input){
	  return getLA1Visual(input, 5, 5);
  }

  /**
   * Create a String containing a line withsome chars around input.LA(1),
   * a line of spaces with a '^' under LA(1) char and line and position
   * in line of LA(1).
   * 
   * @param input
   * @param charsbeforeLA1
   * @param charsafterLA1
   * @return
   * @see #getLA1Visual(CharStream)
   */
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
