package tom.engine.newparser.streamanalysis;
import org.antlr.runtime.CharStream;

/**
 * Detected property : specific delimited sequences like double quoted strings</br>
 * found() returns true when input.index() is in the sequence (from last char of
 * opening sequence to last char of closing sequence included).</br>
 *
 */
public class DelimitedSequenceDetector extends StreamAnalyst{

  private KeywordDetector openingKeyword;
  private KeywordDetector closingKeyword;
  private char escapeChar;
  private boolean inside; // inside delimited sequence
  private boolean escaped;
  
  public DelimitedSequenceDetector(KeywordDetector openingKeywordDetector,
                                   KeywordDetector closingKeywordDetector,
                                   char escapeChar){
    this.openingKeyword = openingKeywordDetector;
    this.closingKeyword = closingKeywordDetector;
    this.escapeChar = escapeChar;
    this.escaped = false;
    this.inside = false;
  }
  
  public DelimitedSequenceDetector(KeywordDetector openingKeyword,
                                   KeywordDetector closingKeyword) {
    this(openingKeyword, closingKeyword, (char)CharStream.EOF);
  }
  
  public DelimitedSequenceDetector(String openingKeyword, String closingKeyword, char escapeChar) {
    this(new KeywordDetector(openingKeyword),
         new KeywordDetector(closingKeyword),
         escapeChar);
  }
  
  public DelimitedSequenceDetector(String openingKeyword, String closingKeyword) {
    this(new KeywordDetector(openingKeyword),
         new KeywordDetector(closingKeyword));
  }
 
  public String getOpeningKeywordString() {
    return openingKeyword.getKeywordString();
  }

  public String getClosingKeywordString() {
    return closingKeyword.getKeywordString();
  }

  @Override
  public boolean match() {
    return inside || closingKeyword.match();
  }
  
  /**
   * readChar returns true as long as read char is part of the sequence.
   * 
   * exemple :
   * dtor = new DelimitedSequenceDetector("\"", "\"", '\\');
   * readChar( a ) > false
   * readChar( " ) > true
   * readChar( b ) > true
   * readChar( \ ) > true
   * readChar( " ) > true
   * readChar( " ) > true
   * readChar( c ) > false
   *
   * This method DOESN'T care about EndOfFile. This should be checked
   * elsewhere.
   */
  @Override
  public boolean readChar(CharStream input) {
	
    boolean oldFoundValue = match();
    
    char nextChar = (char) input.LA(1);
    
    if(inside){
      if(escaped){
        escaped = false;
        // closingKeyword don't know about escaped char
      }else {
        // if !escaped (when function was called)
        if(nextChar == escapeChar) {
          escaped = true;
          closingKeyword.reset();
        } else {
          closingKeyword.readChar(input);
          
          if(closingKeyword.match()) {
            this.inside=false;
            //this.escaped=false; // useless
          }
          
        }
      }
    }else{
      //if !inside (when fonction was called)
      
      // if we matched closingKeyword with previous char
      // inside==false and closingKeyword.found()==true
      // so this.found()== true
      // we need to reset closingKeyword to make found false
      if(closingKeyword.match()){
        closingKeyword.reset();
      }
      
      openingKeyword.readChar(input);
      
      if(openingKeyword.match()){
        this.inside = true;
        openingKeyword.reset();
      }
    }
    
    return match();
  }
  
  @Override
  public void reset() {
    openingKeyword.reset();
    closingKeyword.reset();
    inside = false;
    escaped = false;
  }

  @Override
  public int getOffsetAtMatch() {
    return openingKeyword.getOffsetAtMatch();
  }

}
