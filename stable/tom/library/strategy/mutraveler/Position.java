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

  private Position(LinkedList list) {
    this.list = list;
  }
  
  protected Object clone() {
    return new Position((LinkedList)this.list.clone());
  }
  
  protected boolean isEmpty() {
    return list.isEmpty();
  }
 
  /*
   * remove the last sub-position
   */
  protected void up() {
    list.removeLast();
  }

  /*
   * add a sub-position n
   */
  protected void down(int n) {
    if(n>0) {
      list.addLast(new Integer(n));
    }
  }

  /*
   * create s=omega(v)
   * such that s[subject] returns subject[ s[subject|omega] ]|omega
   */
  public VisitableVisitor getOmega(VisitableVisitor v) {
    VisitableVisitor res = v;
    for(ListIterator it=list.listIterator(list.size()); it.hasPrevious() ;) {
     int index = ((Integer)it.previous()).intValue();
     res = new Omega(index,res);
    }
    return res;
  }
    
  /*
   * create s=omega(x->t)
   * such that s[subject] returns subject[t]|omega
   */
  public VisitableVisitor getReplace(final Visitable t) {
   return this.getOmega(new Identity() { public Visitable visit(Visitable x) { return t; }});
  }

  /*
   * create s=x->t|omega
   * such that s[subject] returns subject|omega
   */
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
