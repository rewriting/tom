package tom.engine.parserv3.streamanalysis;

import org.antlr.runtime.CharStream;

public class EOLdetector extends KeywordDetector{

  private boolean match;
  private int offset;
  
  private char lastChar;
  
  public EOLdetector(){
    super(""); //TODO change inheritance scheme to avoid this ugly trick
    match = false;
    lastChar = (char)-1;
    offset = -1;
  }
  
  /**
   * Returned value may change at every match
   * depending on EOL type (CRLF, CR, LF).
   */
  @Override
  public int getOffsetAtMatch() {
    return offset;
  }

  @Override
  public boolean match() {
    return match;
  }

  @Override
  public boolean readChar(CharStream input) {
    char newChar = (char) input.LA(1);
    
    // \n means windows' or linux's new line
    if(newChar=='\n'){
      match = true;
      
      if(lastChar=='\r'){
        offset = 1; // windows's new line
      }else{
        // linux's new line
        offset = 0;
      }
      
    // \r means macOS' new line or beginning of windows' new line
    }else if(newChar=='\r'){
      
      if((char)input.LA(2)=='\n'){
        match = false;
      }else{
        match = true;
        offset = 0;
      }
    }else{
      match = false;
    }
    
    lastChar = newChar;
    
    return match();
  }

  @Override
  public void reset() {
    lastChar = (char) -1;
    match = false;
    offset = -1;
  }

}
