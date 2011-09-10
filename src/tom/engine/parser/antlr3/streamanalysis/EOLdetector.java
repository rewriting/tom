package tom.engine.parser.antlr3.streamanalysis;

import org.antlr.runtime.CharStream;

public class EOLdetector extends KeywordDetector{

  private boolean match;
  private int offset;
  
  private char lastChar;
  
  public EOLdetector(){
    super(System.getProperty("line.separator"));
  }
}
