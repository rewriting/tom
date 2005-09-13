package ted;

import java.io.*;
import aterm.*;
import tom.library.strategy.mutraveler.*;

// look at the sons and removes those which match the argument
public class MatchAndRemoveVisitor extends aterm.ATermFwd {

  Ted ted = new Ted();
  ATerm tomatch;

  public MatchAndRemoveVisitor(ATerm tomatch) {
    super(new Identity());
    this.tomatch = tomatch;
  }

  public aterm.Visitable visitAppl(ATermAppl arg) {
    ATermFactory factory = aterm.pure.SingletonFactory.getInstance();

    ATermList newSons = (ATermList) visitList(arg.getArguments());

    AFun oldFun = arg.getAFun();
    AFun newFun = factory.makeAFun(oldFun.getName(), newSons.getLength(), oldFun.isQuoted());
    return factory.makeApplList(newFun, newSons);
  }

  public aterm.Visitable visitList(ATermList arg) {
    ATermList res = arg.getEmpty();
    ATerm term;

    while(!arg.isEmpty()) {
      term = arg.getFirst();
      if(!ted.match(tomatch,term)) {
        res = (ATermList) res.insert(term);
      }
      arg = arg.getNext();
    }
    return res.reverse();
  }
}

