package mi3.congruence;

import tom.library.sl.*;

public class _StrategyList extends tom.library.sl.AbstractStrategyCombinator {

  protected mi3.mapping.IMapping mapping;

  public _StrategyList(mi3.mapping.IMapping mapping,Strategy s) {
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
    if(mapping instanceof mi3.mapping.IListMapping) {
      if(mapping.isInstanceOf(any)) {
        return mapLight((mi3.mapping.IListMapping<T,Object>)mapping, arguments[0], any, introspector);
      }
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
      return tom.library.sl.Environment.SUCCESS;
  }

  private <C,D> int map(mi3.mapping.IListMapping<C,D> mapping, Strategy s, Introspector introspector) {
    C subject = (C)environment.getSubject();
    if(mapping.isEmpty(subject)) {
      return tom.library.sl.Environment.SUCCESS;
    } else {
      environment.down(1);
      // DEPEND ON INTROSPECTOR, EVEN FOR LIST!
      //D head = s.visit(mapping.getHead(subject),introspector);
      //C tail = mapLight(mapping, s, mapping.getTail(subject), introspector);
      //return mapping.makeInsert(head,tail);
    }
    return tom.library.sl.Environment.SUCCESS;
  }
}
