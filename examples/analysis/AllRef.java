package analysis;

import tom.library.strategy.mutraveler.AbstractMuStrategy;
import tom.library.strategy.mutraveler.MuVisitable;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.MuReference;
import jjtraveler.Visitable;
import jjtraveler.Visitor;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import java.util.ArrayList;
/**
 * <code>AllRef(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children of type MuReference.
 */

public class AllRef extends AbstractMuStrategy {
  public final static int ARG = 0;
  private Visitable originalSubj;

  public AllRef(Visitable s, VisitableVisitor v) {
    initSubterm(v);
    originalSubj = s;
  }


  public MuReference visitMuReference(MuReference ref, Visitor strat) throws VisitFailure{
    //find the subterm at the position and modify it
    Position pos = new Position(ref.toArray());
    Visitable subtermMuReference = pos.getSubterm().visit(originalSubj);
    final Visitable subtermModified = strat.visit(subtermMuReference);
     pos.getReplace(subtermModified).visit(originalSubj);
    return ref;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    int childCount = any.getChildCount();
    Visitable result = any;
    Visitor S = getArgument(ARG);
    if (any instanceof MuVisitable) {
      boolean updated = false;
      Visitable[] childs = null;

      if(!hasPosition()) {
        for (int i = 0; i < childCount; i++) {
          Visitable oldChild = any.getChildAt(i);
          Visitable newChild = oldChild;
          if (oldChild instanceof MuReference){
            newChild = visitMuReference((MuReference) oldChild,S);
          }
          else{
            //only visiting references
            //newChild = S.visit(oldChild);
          }
          if (updated || (newChild != oldChild)) {
            if (!updated) { // this is the first change
              updated = true;
              // allocate the array, and fill it
              childs = new Visitable[childCount];
              for (int j = 0 ; j<i ; j++) {
                childs[j] = any.getChildAt(j);
              }
            }
            childs[i] = newChild;
          }
        }
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            //childs[i] = getArgument(ARG).visit(any.getChildAt(i));
            Visitable oldChild = any.getChildAt(i);
            getPosition().down(i+1);
            Visitable newChild = oldChild;
            if (oldChild instanceof MuReference){
              newChild = visitMuReference((MuReference) oldChild,S);
            }
            else{
              //only visit references
              //newChild = S.visit(oldChild);
            }
            getPosition().up();
            if (updated || (newChild != oldChild)) {
              if (!updated) {
                updated = true;
                // allocate the array, and fill it
                childs = new Visitable[childCount];
                for (int j = 0 ; j<i ; j++) {
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
      if(!hasPosition()) {
        for (int i = 0; i < childCount; i++) {
          Visitable oldChild = result.getChildAt(i);
          Visitable newChild = oldChild;
          if (oldChild instanceof MuReference){
            newChild = visitMuReference((MuReference) oldChild,S);
          }
          else{
            //only visit references
            //newChild = S.visit(oldChild);
          } 
          result = result.setChildAt(i, newChild);
        }
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            getPosition().down(i+1);
            Visitable oldChild = result.getChildAt(i);
            Visitable newChild = oldChild;
            if (oldChild instanceof MuReference){
              newChild = visitMuReference((MuReference) oldChild,S);
            }
            else{ 
            //only visit references
            //  newChild = S.visit(oldChild);
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
    return result;
  }

}

