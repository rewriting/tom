package Poly;

import aterm.*;

public class PolyCommon {

  protected interface Replace {
    ATerm apply(ATerm t);
  }

    /*
     * Apply a function to each subterm of a term
     */
  protected ATerm genericTraversal(ATerm subject, Replace replace) {
    if(subject instanceof ATermAppl) {
      ATermAppl appl = (ATermAppl) subject;
      ATermAppl newSubterm;
      for(int i=0 ; i<appl.getArity() ; i++) {
        newSubterm = (ATermAppl) replace.apply(appl.getArgument(i));
        if(newSubterm != appl.getArgument(i)) {
          appl = appl.setArgument(newSubterm,i);
        }
      }
      return appl;
    } else {
      System.out.println("Please, extend genericTraversal");
      System.exit(0);
    }
    return null;
  }

}
