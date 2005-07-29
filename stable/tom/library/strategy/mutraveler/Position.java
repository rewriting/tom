package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import java.util.*;

public class Position {
  private LinkedList list = null;
   
  public Position() {
    this.list = new LinkedList();
  }

  protected boolean isEmpty() {
    return list.isEmpty();
  }
  
  protected void up() {
    list.removeLast();
  }

  protected void down(int subtree) {
    if(subtree>0) {
      list.addLast(new Integer(subtree));
    }
  }

  public VisitableVisitor getOmega(VisitableVisitor v) {
    VisitableVisitor res = v;
    for(ListIterator it=list.listIterator(list.size()); it.hasPrevious() ;) {
     int index = ((Integer)it.previous()).intValue();
     res = new Omega(index,res);
    }
    return res;
  }
          
  public VisitableVisitor getReplace(final Visitable subject) {
   return this.getOmega(new Identity() { public Visitable visit(Visitable x) { return subject; }});
  }
  
  public VisitableVisitor getSubterm() {
   return new AbstractVisitableVisitor() { 
     { initSubterm(); }
     public Visitable visit(Visitable subject) throws VisitFailure {
       final Visitable[] ref = new Visitable[1];
       getOmega(new Identity() { public Visitable visit(Visitable x) { ref[0]=x; return x; }}).visit(subject);
       return ref[0];
     }
   };
  }

  public String toString() {
    return list.toString();
  }
}
