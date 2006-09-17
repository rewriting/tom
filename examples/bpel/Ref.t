package bpel;

import tom.library.strategy.mutraveler.AbstractMuStrategy;
import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.MuVisitable;
import tom.library.strategy.mutraveler.MuReference;
import tom.library.strategy.mutraveler.Position;
import jjtraveler.Visitable;
import jjtraveler.Visitor;
import jjtraveler.VisitFailure;
import java.util.*;
/**
 * Basic visitor combinator with one visitor argument and one visitable argument
 */

public class Ref extends AbstractMuStrategy {

  %include{mustrategy.tom}

  public final static int ARG = 0;
  protected Visitable originalSubj;

  public Ref(Visitable s, MuStrategy v) {
    initSubterm(v);
    originalSubj = s;
  }

  public MuReference visitReference(MuReference ref, MuStrategy strat) throws VisitFailure{
    Position pos = new Position(ref.toArray());
    visitPosition(pos,strat);
    return ref;
  }

  public void visitPosition(Position pos, MuStrategy strat) throws VisitFailure{
    int[] oldpos = getPosition().toArray();
    getPosition().init(pos);
    try{
      originalSubj = pos.getOmega(strat).visit(originalSubj);
    }catch(VisitFailure e){
      getPosition().init(oldpos);
      throw new VisitFailure();
    }
    getPosition().init(oldpos);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    if (any instanceof MuReference){
      return visitReference((MuReference)any,(MuStrategy)visitors[ARG]);
    }
    else{ 
      return visitors[ARG].visit(any);
    }
  }

}


