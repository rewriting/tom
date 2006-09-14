package termgraph;

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
 * <code>OneRefSensitive(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children and if the children is a position to the wfg at this position.
 */

public class OneRefSensitive extends AbstractMuStrategy {
  
  %include{mustrategy.tom}

  public final static int ARG = 0;
  protected Visitable originalSubj;

  public OneRefSensitive(Visitable s, MuStrategy v) {
    initSubterm(v);
    originalSubj = s;
  }


  public MuReference visitReference(MuReference ref, MuStrategy strat) throws VisitFailure{
    Position pos = new Position(ref.toArray());
    visitPosition(pos,strat);
    return ref;
  }

  public void visitPosition(Position pos, MuStrategy strat) throws VisitFailure{
    Position p = getPosition();
    setPosition(pos);
    try{
      originalSubj = pos.getOmega(strat).visit(originalSubj);
    }catch(VisitFailure e){
      setPosition(p);
      throw new VisitFailure();
    }
    setPosition(p);
  }

  public Visitable visit(Visitable any) throws VisitFailure {
   int childCount = any.getChildCount();
   if(!hasPosition()) {
     throw new RuntimeException("Need to initialize positions");
   } else {
     for(int i = 0; i < childCount; i++) {
       Visitable oldChild = any.getChildAt(i);
       try { 
         //System.out.println("One.pos = " + position);
         position.down(i+1);
         Visitable newChild;
         if (oldChild instanceof MuReference){
           newChild = visitReference((MuReference)oldChild,(MuStrategy)visitors[ARG]);
         }
         else{ 
           newChild = visitors[ARG].visit(oldChild);
         }
         position.up();
         return any.setChildAt(i,newChild);
       } catch(VisitFailure f) {
         position.up();
       }
     }
   }
   throw new VisitFailure();
  }

  public Visitable getSubject(){
    return originalSubj;
  }

}

