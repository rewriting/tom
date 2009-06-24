package mi2.mapping;

import java.util.List;

/**
 * @author nick-vintila
 * @date Jun 23, 2009  9:08:23 PM
 */
public abstract class List_Introspector<T> extends IntrospectorMapping<List<T>> implements ListMapping_I<T> {

    public T setChildren(Object o, Object[] children) {
      //TODO
      return null;
    }

    public Object[] getChildren(Object o) {
      //TODO
      return null;
    }

    public /*<T> T*/ Object setChildAt(/*T*/Object o, int i, Object child) {
      //TODO
      return null;
    }

    public Object getChildAt(Object o, int i) {
      //TODO
      return null;
    }

    public int getChildCount(Object o) {
      if (isEmpty((List<T>)o)) {
        return 0;
      } else {
        return ((List<T>)o).size();//2; //todo why 2? pass in constructor??
      }
    }

    public Class forType() {
      return List.class;
    }
}
