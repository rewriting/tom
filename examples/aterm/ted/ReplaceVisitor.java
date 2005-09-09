import java.io.*;
import aterm.*;
import tom.library.strategy.mutraveler.*;


public class ReplaceVisitor extends aterm.ATermFwd {

  Ted ted = new Ted();
  ATerm tomatch;
  ATerm replacement;

  public ReplaceVisitor(ATerm tomatch, ATerm replacement) {
    super(new Identity());
    this.tomatch = tomatch;
    this.replacement = replacement;
  }

  public aterm.Visitable visitATerm(ATerm arg) {
    if (ted.match(tomatch, arg)) {
      return replacement;
    }
    return arg;
  }
}

