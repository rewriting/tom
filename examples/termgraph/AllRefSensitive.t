package termgraph;

import tom.library.strategy.mutraveler.AbstractMuStrategy;
import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.MuVisitable;
import tom.library.strategy.mutraveler.Position;
import jjtraveler.Visitable;
import jjtraveler.Visitor;
import jjtraveler.VisitFailure;
import termgraph.term.types.term.posTerm;
import java.util.*;
/**
 * <code>AllRefSensitive(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children and if the children is a position to the term at this position.
 */

public class AllRefSensitive extends AbstractMuStrategy {
  
  %include{mustrategy.tom}
  %include{term/term.tom}

  public final static int ARG = 0;
  private Visitable originalSubj;

  public AllRefSensitive(Visitable s, MuStrategy v) {
    initSubterm(v);
    originalSubj = s;
  }


  public posTerm visitReference(posTerm ref, MuStrategy strat) throws VisitFailure{
    Position pos = new Position(ref.toArray());
    Position p = strat.getPosition();
    setPosition(pos);
    originalSubj = pos.getOmega(strat).visit(originalSubj);
    setPosition(p);
    return ref;
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
            if (oldChild instanceof posTerm){
              newChild = visitReference((posTerm)oldChild,S);
            }
            else{ 
              newChild = S.visit(oldChild);
              originalSubj = getPosition().getReplace(newChild).visit(originalSubj);
            }
            getPosition().up();
            if (updated || (newChild != oldChild)) {
              if (!updated) {
                updated = true;
                // allocate the array, and fill it
                childs = new Visitable[childCount];
                for (int j = 0 ; j<i ; j++) {
                  // System.out.println("AllRefSensitive pos:"+i+", "+j+", "+any);
                  childs[j] = any.getChildAt(j);
                }
              }
              childs[i] = newChild;
            }
          }
        } catch(VisitFailure f) {
          getPosition().up();
          throw new VisitFailure();
        }
      }
      if (updated) {
        result = ((MuVisitable) any).setChilds(childs);
      }
    } else {
      //System.out.println("AllRefSensitive.visit(" + any.getClass() + ")");
      if(!hasPosition()) {
          throw new RuntimeException("Need to initialize positions");
       } else {
        try {
          for (int i = 0; i < childCount; i++) {
            getPosition().down(i+1);
            Visitable oldChild = result.getChildAt(i);
            Visitable newChild;
            if (oldChild instanceof posTerm){
              newChild = visitReference((posTerm) oldChild,S);
            }
            else{ 
              newChild = S.visit(oldChild);
              originalSubj = getPosition().getReplace(newChild).visit(originalSubj);
            } 
            getPosition().up();
            result = result.setChildAt(i, newChild);
          }
        } catch(VisitFailure f) {
          getPosition().up();
          throw new VisitFailure();
        }
      }
    }
    //return result;
    result =  getPosition().getSubterm().apply(originalSubj);
    return result;
  }

  public Visitable getSubject(){
    return originalSubj;
  }

}

