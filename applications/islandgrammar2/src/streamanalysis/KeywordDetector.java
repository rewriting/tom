package streamanalysis;
import org.antlr.runtime.CharStream;

/**
 * Detected property : some keyword have been found</br>
 * found() returns true when input.index() is last char of the keyword index.</br>
 * @see StreamAnalyst
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
  
  @Override
  public boolean match() {
    return matched;
  }

  @Override
  public boolean readChar(CharStream input) {
    char nextChar = (char)input.LA(1);
    
    if(match()){
      reset();
    }
    
    //TODO check EOF
    if(keyword.charAt(cursor)==nextChar) {
      cursor++;
    }else{
      cursor = 0;
    }
    
    if(cursor==keyword.length()){
      matched = true;
      cursor = 0;
      
      // don't forget Observable features
      setChanged();
      notifyObservers();
    }
    
    return match();
  }

  @Override
  public void reset() {
    this.cursor = 0;
    this.matched = false;
    
    // don't forget Observable features
    setChanged();
    notifyObservers();
  }

  @Override
  public int getOffsetAtMatch() {
    return keyword.length()-1;
  }
}
