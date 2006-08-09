package strategy;

import tom.library.strategy.mutraveler.AbstractMuStrategy;
import tom.library.strategy.mutraveler.MuVisitable;
import tom.library.strategy.mutraveler.MuReference;
import tom.library.strategy.mutraveler.Position;
import jjtraveler.Visitable;
import jjtraveler.Visitor;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

import java.util.*;
/**
 * <code>AllRefSensitive(v).visit(T(t1,...,tN) = T(v.visit(t1), ..., v.visit(t1))</code>
 * <p>
 * Basic visitor combinator with one visitor argument, that applies
 * this visitor to all children and if the children is a position to the term at this position.
 */

public class AllRefSensitive extends AbstractMuStrategy {
  public final static int ARG = 0;
  private Visitable originalSubj;
  private ArrayList markedReferences;

  public AllRefSensitive(Visitable s, VisitableVisitor v) {
    initSubterm(v);
    originalSubj = s;
    markedReferences = new ArrayList();
  }


  public MuReference visitReference(MuReference ref, Visitor strat) throws VisitFailure{
    //find the subterm at the position and modify it
    if(! markedReferences.contains(ref)){
    markedReferences.add(ref);
    Position pos = new Position(ref.toArray());
    Visitable subtermRef = pos.getSubterm().visit(originalSubj);
    final Visitable subtermModified = strat.visit(subtermRef);
     pos.getReplace(subtermModified).visit(originalSubj);
    }
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
          //childs[i] = getArgument(ARG).visit(any.getChildAt(i));
          Visitable oldChild = any.getChildAt(i);
          Visitable newChild;
          if (oldChild instanceof MuReference){
            newChild = visitReference((MuReference) oldChild,S);
          }
          else{ 
            newChild = S.visit(oldChild);
          }
          if (updated || (newChild != oldChild)) {
            if (!updated) { // this is the first change
              updated = true;
              // allocate the array, and fill it
              childs = new Visitable[childCount];
              for (int j = 0 ; j<i ; j++) {
                //  System.out.println("AllRefSensitive nopos:"+i+", "+j+", "+any);
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
            Visitable newChild;
            if (oldChild instanceof MuReference){
              newChild = visitReference((MuReference) oldChild,S);
            }
            else{ 
              newChild = S.visit(oldChild);
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
        for (int i = 0; i < childCount; i++) {
          Visitable oldChild = result.getChildAt(i);
          Visitable newChild;
          if (oldChild instanceof MuReference){
            newChild = visitReference((MuReference) oldChild,S);
          }
          else{ 
            newChild = S.visit(oldChild);
          } 
          result = result.setChildAt(i, newChild);
        }
      } else {
        try {
          for (int i = 0; i < childCount; i++) {
            getPosition().down(i+1);
            Visitable oldChild = result.getChildAt(i);
            Visitable newChild;
            if (oldChild instanceof MuReference){
              newChild = visitReference((MuReference) oldChild,S);
            }
            else{ 
              newChild = S.visit(oldChild);
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

