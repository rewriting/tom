package mi3.hand;

import base.hand.types.T1;
import base.hand.types.T2;
import base.hand.types.t1.a;
import base.hand.types.t1.f;
import base.hand.types.t2.b;
import base.hand.types.t2.g;
import base.hand.types.t2.h;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mi3.mapping.*;

public class Module {
  /*
   * this file is hand-written (or generated for Gom data-structures)
   */

  public static final Module instance = new Module();


  //implementation of the interfaces defined by users
  public static class a_Mapping extends Mapping0<T1> {
    public Class getImplementation() {
      return a.class;
    }

    public boolean isInstanceOf(Object subject) {
      return subject instanceof a;
    }

    public T1 make() {
      return new a();
    }

  }

  /** ------------------------------ */

  public static class b_Mapping extends Mapping0<T2> {
    public Class getImplementation() {
      return b.class;
    }

    public boolean isInstanceOf(Object subject) {
      return subject instanceof b;
    }

    public T2 make() {
      return new b();
    }

  }

  /** ------------------------------ */

  public static class f_Mapping extends Mapping2<T1, T1, T2> {
    public Class getImplementation() {
      return f.class;
    }

    public boolean isInstanceOf(Object subject) {
      return subject instanceof f;
    }

    public T1 make(T1 t1, T2 t2) {
      return new f(t1, t2);
    }

    public T1 get0(T1 t) {
      return ((f)t).getS1(); 
    }

    public T2 get1(T1 t) {
      return ((f)t).getS2(); 
    }

  }

  /** ------------------------------ */
  public static class g_Mapping extends Mapping1<T2, T2> {
    public Class getImplementation() {
      return g.class;
    }

    public boolean isInstanceOf(Object subject) {
      return subject instanceof g;
    }

    public T2 make(T2 t2) {
      return new g(t2);
    }

    public T2 get0(T2 t) {
      return ((g)t).getS2();
    }

  }

  /** ------------------------------ */
  public static class h_Mapping extends Mapping1<T2, List<T1>> {
    public Class getImplementation() {
      return h.class;
    }

    public boolean isInstanceOf(Object subject) {
      return subject instanceof h;
    }

    public T2 make(List<T1> l) {
      return new h(l);
    }

    public List<T1> get0(T2 t) {
      return ((h)t).getTs();
    }

  }

  /** ------------------------------ */
  public static class concT1_Mapping extends ListMapping<List<T1>, T1> {
    public Class getImplementation() {
      //TODO: cannot discriminate List<T> 
      return List.class;
    }

    public boolean isInstanceOf(Object subject) {
      //TODO: check also the type of the elements
      return subject instanceof List;
    }

    public boolean isEmpty(List<T1> l) {
      return l.isEmpty();
    }

    public List<T1> makeEmpty() {
      return new ArrayList<T1>();
    }

    public List<T1> makeInsert(T1 o, List<T1> l) {
        List<T1> res = new ArrayList<T1>(l);
        res.add(0, o);
        return res;
    }

    public T1 getHead(List<T1> l) {
      return l.get(0);
    }

    public List<T1> getTail(List<T1> l) {
      List<T1> res = new ArrayList<T1>(l);
      res.remove(0);
      return res;
    }

  }

  /*
   * Not used at present
   */
  // split SL introspectors
  public interface Introspector1 {
    public <T> T setChildren(T o, Object[] children);
    public Object[] getChildren(Object o);
  }

  public interface Introspector2 {
    public <T> T setChildAt(T o, int i, Object child);
    public Object getChildAt(Object o, int i);
    public int getChildCount(Object o);
  }

  public class IntrospectorFactory {

    public Introspector makeFromIntrospector1(final Introspector1 introspector) {
      return new Introspector() {

        public <T> T setChildAt(T o, int i, Object child) {
          Object[] children = getChildren(o);
          children[i] = child;
          return setChildren(o, children);
        }

        public Object getChildAt(Object o, int i) {
          return getChildren(o)[i];
        }

        public int getChildCount(Object o) {
          return getChildren(o).length;
        }

        public <T> T setChildren(T o, Object[] children) {
          return introspector.setChildren(o, children);
        }

        public Object[] getChildren(Object o) {
          return introspector.getChildren(o);
        }
      };
    }

    public Introspector makeFromInstrospector2(final Introspector2 introspector) {
      return new Introspector() {

        public <T> T setChildAt(T o, int i, Object child) {
          return introspector.setChildAt(o, i, child);
        }

        public Object getChildAt(Object o, int i) {
          return introspector.getChildAt(o, i);
        }

        public int getChildCount(Object o) {
          return introspector.getChildCount(o);
        }

        //FIX in case of normalization rules
        public <T> T setChildren(T o, Object[] children) {
          for (int i = 0; i<children.length ; i++) {
            o = setChildAt(o, i, children[i]);
          }
          return o;
        }

        public Object[] getChildren(Object o) {
          Object[] children = new Object[getChildCount(o)];
          for (int i = 0; i< getChildCount(o); i++) {
            children[i] = getChildAt(o, i);
          }
          return children;
        }
      };
    }

  }
}
