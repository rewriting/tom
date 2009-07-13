package mi3.congruence;

import tom.library.sl.*;

public class _StrategyList extends tom.library.sl.AbstractStrategyCombinator {

  protected mi3.mapping.IListMapping mapping;

  public _StrategyList(mi3.mapping.IListMapping mapping,Strategy s) {
    this.mapping = mapping;
    initSubterm(s);
  }

  /** 
   * Visit the subject any without managing any environment
   *
   * @param any the subject to visit
   * @param introspector the introspector
   * @return a Visitable
   * @throws VisitFailure if visitLight fails
   */ 
  public final <T> T visitLight(T any, Introspector introspector) throws VisitFailure {
    if(mapping.isInstanceOf(any)) {
      return mapLight((mi3.mapping.IListMapping<T,Object>)mapping, arguments[0], any, introspector);
    }
    throw new tom.library.sl.VisitFailure();
  }

  private <C,D> C mapLight(mi3.mapping.IListMapping<C,D> mapping, Strategy s, C subject, Introspector introspector) throws VisitFailure {
    if(mapping.isEmpty(subject)) {
      return subject;
    } else {
      D head = s.visitLight(mapping.getHead(subject),introspector);
      C tail = mapLight(mapping, s, mapping.getTail(subject), introspector);
      return mapping.makeInsert(head,tail);
    }
  }

  /**
   * Visit the current subject (found in the environment)
   * and place its result in the environment.
   * Sets the environment flag to Environment.FAILURE in case of failure
   *
   * @param introspector the introspector
   * @return 0 if success
   */
  public int visit(Introspector introspector) {
    environment.setIntrospector(introspector);
    Object any = environment.getSubject();
    if(mapping.isInstanceOf(any)) {
      int childCount = introspector.getChildCount(any);
      Object[] childs = null;

      for(int i = 0; i < childCount; i++) {
        Object oldChild = introspector.getChildAt(any,i);
        environment.down(i+1);
        int status = arguments[0].visit(introspector);
        if(status != Environment.SUCCESS) {
          environment.upLocal();
          return status;
        }
        Object newChild = environment.getSubject();
        if(childs != null) {
          childs[i] = newChild;
        } else if(newChild != oldChild) {
          // allocate the array, and fill it
          childs = introspector.getChildren(any);
          childs[i] = newChild;
        } 
        environment.upLocal();
      }
      if(childs!=null) {
        environment.setSubject(introspector.setChildren(any,childs));
      }
      return Environment.SUCCESS;
    }
    System.out.println("fail");
    return Environment.FAILURE;
  }
}
