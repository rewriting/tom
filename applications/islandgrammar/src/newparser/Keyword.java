package newparser;

import org.antlr.runtime.tree.*;

public abstract class Keyword {

/** The pattern this Keyword looks for */
  protected String pattern; /* this is protected because it's supposed to be modified by all inheriting classes (the actual Keyword ) */
/** Index of next character to compare */
  private int cursor = 0; /* this is private because we'll surely want to change the recognition mechanism one day (to introduce reg. exp. based Keyword ?) and don't want to break existing code */
/** A flag to tell whether or not the pattern has been found */
  protected boolean matched; /* in this implementation it will equal (cursor == pattern.length), but what about more complex recognition mechanism ? (reg. exp. for instance) */

/**
 ** @param  : a character proposed to this Keyword for recognition
 ** @return : whether c was accepted by this Keyword
 **/
  public boolean take(char c) {
    if (pattern.charAt(cursor) == c) {
/* Character is accepted */
      cursor++;
      matched = (cursor == pattern.length());
      return true;
    } else {
/* Character is refused */
      reset();
      return false;
    }
  }

/** A simple getter on boolean 'matched' */
  public boolean isReady() {
    return matched;
  }

/** Puts the Keyword in it's original state */
  public void reset() {
    cursor = 0;
    matched = false;
  }

/** The action the Keyword is supposed to do when its pattern is matched */
  protected abstract void action() throws org.antlr.runtime.RecognitionException;

}
