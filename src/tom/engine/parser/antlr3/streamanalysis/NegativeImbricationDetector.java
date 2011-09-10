package tom.engine.parser.antlr3.streamanalysis;

import org.antlr.runtime.CharStream;

/**
 * Detected property : imbrication level (relative to init</br>
 * match() returns true when input.index() is level-closing char index.</br>
 * So if you readChar then consume then test, test result tells you if you just
 * entered -1 imbrication level.
 * You may need to rewind CharStream to allow nice parsing of this block.
 */
public class NegativeImbricationDetector extends StreamAnalyst {

  private int imbricationLevel;
  private final char levelOpeningChar;
  private final char levelClosingChar;
  
  /**
   * If levelOpeningChar have already been consumed and you don't want to push it back
   * you need to use an initialLevel=0.
   * 
   * If levelOpeningChar (for the imbrication level you want to watch) is still in the
   * stream, you need to use initialLevel=-1. Be aware that found() will return true
   * until this openingChar is read.
   * 
   * @param levelOpeningChar
   * @param levelClosingChar
   * @param initialLevel
   */
  public NegativeImbricationDetector(char levelOpeningChar, char levelClosingChar, int initialLevel){
    this.imbricationLevel = initialLevel;
    this.levelOpeningChar = levelOpeningChar;
    this.levelClosingChar = levelClosingChar;
  }
  
  @Override
  public boolean match() {
    return imbricationLevel<0;
  }

  @Override
  public boolean readChar(CharStream input) {
    
    boolean oldFoundValue = match();
    
    char nextChar = (char)input.LA(1);
    
    if(nextChar==levelOpeningChar) {
      imbricationLevel++;
    }
    
    if(nextChar==levelClosingChar) {
      imbricationLevel--;
    }
    
    return match();
  }

  /**
   * reset do nothing.
   * useful to hide among other StreamAnalystObjects
   */
  @Override
  public void reset() {}

  @Override
  public int getOffsetAtMatch() {
    return 0;
  }


}
