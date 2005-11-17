package ted;

import java.io.*;
import aterm.*;
import tom.library.strategy.mutraveler.*;


public class ReplaceVisitor extends aterm.ATermFwd {

  Ted ted = null;
  ATerm tomatch;
  ATerm replacement;

  public ReplaceVisitor(ATerm tomatch, ATerm replacement) {
    super(new Identity());
    this.tomatch = tomatch;
    this.replacement = replacement;
    this.ted = new Ted();
  }

  public ReplaceVisitor(ATerm tomatch, ATerm replacement, Ted ted) {
    this(tomatch,replacement);
    this.ted = ted;
  }
  
  public aterm.Visitable visitATerm(ATerm arg) {
    if (ted.match(tomatch, arg)) {
      return ted.modifyReplacement(replacement);
    }
    return arg;
  }
}

