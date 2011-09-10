package tom.engine.parser.antlr3.streamanalysis;
import org.antlr.runtime.CharStream;

/**
 * Detect a specific string in a CharStream.
 * 
 * Please note that one needs to use CharStream.consume() between two calls to
 * KeywordDetector.readChar().
 * 
 * readChar doesn't consume chars automatically to allow to use several
 * instances of KeywordDetector.
 * 
 * match() will return true after the last keyword's char have been read.
 *
 * XXX Could check that every call to readChar reads char right after
 * previously read char.
 */
public class KeywordDetector extends StreamAnalyst {

  private final String keyword;
  private int cursor;
  private boolean matched;
  
  public KeywordDetector(String keyword) {
    this.keyword = keyword;
    this.cursor = 0;
    this.matched = false;
  }
 
  public String getKeywordString() {
    return keyword;
  }

  // XXX hasMatched would be a better name (?)
  /**
   * Return matching status.
   */
  @Override
  public boolean match() {
    return matched;
  }

  /**
   * This method read next char in CharStream and update matching status.
   * This method DOESN'T care about EndOfFile. This should be checked
   * elsewhere.
   * @return boolean, new matching status.
   */
  @Override
  public boolean readChar(CharStream input) {
    char nextChar = (char)input.LA(1);
    
    if(match()) {
      reset();
    }
    
    if(keyword.charAt(cursor)==nextChar) {
      cursor++;
      if(cursor==keyword.length()) {
        matched = true;
        cursor = 0;
      }
      return matched;
    } else {
      if(keyword.charAt(0)==nextChar){
        cursor = 1;
      }else {
        cursor = 0;
      }
    }
    
    return matched;
  }

  @Override
  public void reset() {
    this.cursor = 0;
    this.matched = false;
  }

  @Override
  public int getOffsetAtMatch() {
    return keyword.length()-1;
  }
}
