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
 * <code>AllRefSensitive(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children and if the children is a position to the wfg at this position.
 */

public class AllRefSensitive extends AbstractMuStrategy {
  
  %include{mustrategy.tom}

  public final static int ARG = 0;
  protected Visitable originalSubj;

  public AllRefSensitive(Visitable s, MuStrategy v) {
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
    Visitable result = any;
    MuStrategy S = (MuStrategy) getArgument(ARG);
    if (any instanceof MuVisitable) {
      boolean updated = false;
      Visitable[] childs = null;
      if(!hasPosition()) {
        throw new RuntimeException("Need to initialize positions");
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            //childs[i] = getArgument(ARG).visit(any.getChildAt(i));
            Visitable oldChild = any.getChildAt(i);
            getPosition().down(i+1);
            Visitable newChild;
            if (oldChild instanceof MuReference){
              newChild = visitReference((MuReference)oldChild,S);
            }
            else{ 
              newChild = S.visit(oldChild);
            }
            originalSubj = getPosition().getReplace(newChild).visit(originalSubj);
            getPosition().up();

          }
        } catch(VisitFailure f) {
          getPosition().up();
          throw new VisitFailure();
        }
        childs = new Visitable[childCount];
        Visitable newSubterm = getPosition().getSubterm().visit(originalSubj);
        for (int i = 0 ; i<childCount ; i++) {
          childs[i] = newSubterm.getChildAt(i);
        }
        result = ((MuVisitable) any).setChilds(childs);
      }
    } else {
      if(!hasPosition()) {
        throw new RuntimeException("Need to initialize positions");
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            getPosition().down(i+1);
            Visitable oldChild = any.getChildAt(i);
            Visitable newChild;
            if (oldChild instanceof MuReference){
              newChild = visitReference((MuReference) oldChild,S);
            }
            else{ 
              newChild = S.visit(oldChild);
            } 
            originalSubj = getPosition().getReplace(newChild).visit(originalSubj);
            getPosition().up();
          }
        } catch(VisitFailure f) {
          getPosition().up();
          throw new VisitFailure();
        }
      }
    }
    return result;
  }


  public Visitable getSubject(){
    return originalSubj;
  }

}

