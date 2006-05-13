package ted;

import java.io.*;
import aterm.*;
import tom.library.strategy.mutraveler.*;
import java.util.*;
import aterm.*;
import aterm.pure.PureFactory;

public class ReplaceVisitor extends aterm.ATermFwd {

  private static ATermFactory atermFactory = new PureFactory();

  // replaces in the term the placeholders with
  // their initialization from tds
  public ATerm modifyReplacement(ATerm term, Map tds){

    String termStr = term.toString();
    Iterator it = tds.keySet().iterator();
    while (it.hasNext()){

      ATerm key = (ATerm)it.next();
      String keyStr = key.toString();
      
      termStr = termStr.replaceAll(keyStr,((ATerm)tds.get(key)).toString());
    }
    return atermFactory.parse(termStr);
  }

  ATerm tomatch;
  ATerm replacement;

  public ReplaceVisitor(ATerm tomatch, ATerm replacement) {
    super(new Identity());
    this.tomatch = tomatch;
    this.replacement = replacement;
  }

  public aterm.Visitable visitATerm(ATerm arg) {
    Map tds = Ted.match(tomatch, arg);
    if (tds != null) 
      return modifyReplacement(replacement, tds);
    else 
      return arg;
  }
}

