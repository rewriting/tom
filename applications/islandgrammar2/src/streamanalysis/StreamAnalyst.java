package streamanalysis;
import java.util.Observable;

import org.antlr.runtime.CharStream;

/**
 * <b>StreamAnalyst</b> :</br>
 * Detect and locate some properties of charStream.<br/>
 * 
 * <i>Typical use</i> :</br>
 * input : CharStream<br/>
 * analysts : StreamAnalyst[0..*]<br/>
 * foreach ( sa : StreamAnalyst in analysts )</br>
 * | sa.readChar(input)</br>
 * input.consume()</br>
 * // test analysts values and react
 * 
 */
public abstract class StreamAnalyst extends Observable{
  
  public abstract void reset();
  
  public abstract boolean match();
  
  /**
   * Have no side effect on input.
   * @param input
   * @return whether 'something' was found
   */
  public abstract boolean readChar(CharStream input);
  
  /**
   * Number of chars that belong to what StreamAnlyst is looking
   * that are already consumed when match become true; 
   * @return
   */
  public abstract int getOffsetAtMatch();
}
