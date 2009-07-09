package mi3.congruence;

import tom.library.sl.*;

public class _Strategy extends tom.library.sl.AbstractStrategyCombinator {

  protected mi3.mapping.IMapping mapping;

  public _Strategy(mi3.mapping.IMapping mapping,Strategy... s) {
    this.mapping = mapping;
    initSubterm(s);
  }

  /** 
   * Visits the subject any in a light way (without environment)  
   *
   * @param any the subject to visit
   * @throws VisitFailure if visitLight fails
   */
  public <T extends Visitable> T visitLight(T any) throws VisitFailure {
    return visitLight(any,mapping.getIntrospector());
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
      T result = any;
      Object[] childs = null;
      for (int i = 0, nbi = 0; i < getChildCount(); i++) {
          Object oldChild = introspector.getChildAt(any,nbi);
          Object newChild = arguments[i].visitLight(oldChild,introspector);
          if(childs != null) {
            childs[nbi] = newChild;
          } else if(newChild != oldChild) {
            // allocate the array, and fill it
            childs = introspector.getChildren(any);
            childs[nbi] = newChild;
          }
          nbi++;
      }
      if(childs!=null) {
        result = introspector.setChildren(any,childs);
      }
      return result;
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  /** 
   * Visits the subject any by providing the environment 
   *
   * @param any the subject to visit. 
   * @throws VisitFailure if visit fails
   */
  public <T extends Visitable> T visit(T any) throws VisitFailure {
    return visit(any,mapping.getIntrospector());
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
    Object any = environment.getSubject();
    if(mapping.isInstanceOf(any)) {
      Object[] childs = null;
      for(int i = 0, nbi = 0; i < getChildCount(); i++) {
          Object oldChild = introspector.getChildAt(any,nbi);
          environment.down(nbi+1);
          int status = arguments[i].visit(introspector);
          if(status != tom.library.sl.Environment.SUCCESS) {
            environment.upLocal();
            return status;
          }
          Object newChild = environment.getSubject();
          if(childs != null) {
            childs[nbi] = newChild;
          } else if(newChild != oldChild) {
            childs = introspector.getChildren(any);
            childs[nbi] = newChild;
          } 
          environment.upLocal();
          nbi++;
      }
      if(childs!=null) {
        environment.setSubject(introspector.setChildren(any,childs));
      }
      return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }

}
