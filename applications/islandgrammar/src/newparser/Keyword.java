package newparser;

import org.antlr.runtime.tree.*;

/**<p>Keyword is an abstract class to allow watching for a keyword and trigger an arbitrary action when it is found in the data stream.</p>
<p> To create a Keyword, you need to define a class inheriting from this one, and to write a proper action() method. Pay attention to the fact that all your keywords' patterns should be different and none should be a substring of another one as it would pose the problem to decide which one should be triggered. Currently it will be accepted by the compiler buruntime behaviour isn't guaranteed.</p>*/
public abstract class Keyword {

/** The pattern this Keyword looks for.*/
  protected String pattern; /* this is protected because it's supposed to be modified by all inheriting classes (the actual Keyword ) */
/** Index of next character to compare.*/
  private int cursor = 0; /* this is private because we'll surely want to change the recognition mechanism one day (to introduce reg. exp. based Keyword ?) and don't want to break existing code */
/** A flag to tell whether or not the pattern has been found.*/
  protected boolean matched; /* in this implementation it will equal (cursor == pattern.length), but what about more complex recognition mechanism ? (reg. exp. for instance) */

/**
 ** @param  c a character proposed to this Keyword to match it's pattern
 ** @return whether c was accepted by this Keyword
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

/** A simple getter on boolean 'matched'.*/
  public boolean isReady() {
    return matched;
  }

/** Puts the Keyword in it's original state.*/
  public void reset() {
    cursor = 0;
    matched = false;
  }

/** The action the Keyword is supposed to do when its pattern is matched.*/
  protected abstract void action() throws org.antlr.runtime.RecognitionException;

}
