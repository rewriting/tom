import java.util.List;

import org.eclipse.emf.ecore.EObject;

import tom.library.sl.Introspector;

public class EcoreContainmentIntrospector implements Introspector {
  
  @SuppressWarnings("unchecked")
  private <O extends Object> List<O> getList(Object o){
    if(o instanceof List<?>) {
      return (List<O>) o;
    } else if(o instanceof EObject) {
      EObject eo = (EObject) o;
      return (List<O>) eo.eContents();
    }
    return null;
  }
  
  @Override
  public Object getChildAt(Object o, int i) {
    List<Object> l=getList(o);
    if(l!=null){
      return l.get(i);
    }
    return null;
  }

  @Override
  public int getChildCount(Object o) {
    List<Object> l=getList(o);
    if(l!=null){
      return l.size();
    }
    return 0;
  }

  @Override
  public Object[] getChildren(Object o) {
    List<Object> l=getList(o);
    if(l!=null){
      return l.toArray();
    }
    return null;
  }

  @Override
  public <T> T setChildAt(T o, int i, Object obj) {
    List<Object> l=getList(o);
    if(l!=null){
      l.set(i,o);
      return o;
    }
    return null;
  }

  @Override
  public <T> T setChildren(T o, Object[] objs) {
    List<Object> l=getList(o);
    if(l!=null){
      for(int i = 0; i < objs.length; i++) {
        l.set(i, objs[i]);
      }
      return o;
    }
    return null;
  }

}
