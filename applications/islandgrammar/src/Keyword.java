import org.antlr.runtime.tree.*;

public abstract class Keyword {

  protected String pattern;
  protected boolean regExp = false;
  private int cursor = 0;
  private boolean matched;

/**
 ** @param  : a character proposed to this Keyword for recognition
 ** @return : whether c was accepted by this Keyword
 **/
  public boolean take(char c) {
    /*System.out.print(pattern + "  : ");*/
    if(regExp) {
      /* The regexp mode is only a joke now, but it could become cool someday
        Currently only support special characters '.' and '^' (for respectively any char and not following character) */
      switch(pattern.charAt(cursor)) {
        case '.':return true;
        case '^':return (c != pattern.charAt(++cursor));
        default: return (c == pattern.charAt(cursor));
      }
    } else {
      if (pattern.charAt(cursor) == c) {
        cursor++;
        matched = (cursor == pattern.length());
        /*System.out.println(" Miam !");*/
        return true;
      } else {
        /*System.out.println(" Beurk !");*/
        reset();
        return false;
      }
    }
  }

  public boolean isReady() {
    return matched;
  }

  public void reset() {
    cursor = 0;
    matched = false;
  }

  protected abstract void action() throws org.antlr.runtime.RecognitionException;

}
