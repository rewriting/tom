package lsruntime;

import aterm.ATerm;

public interface Ignore {
  
  /**
   *
   * @return true if the ATerm t should be ignored
   */
  boolean apply(ATerm t);
  
}
