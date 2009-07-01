package mi3.gom;

import base.data.types.t1.*;
import base.data.types.t2.*;
import base.data.types.T2;
import base.data.types.T1;
import tom.library.sl.Introspector;

public class Module {

  public static final Module instance = new Module();

  // generic mapping interfaces
  
  public static interface IMapping {
    public boolean isInstanceOf(Object subject);
    public Introspector getIntrospector();
  }

  public static interface IMapping0<C> extends IMapping {
    C make();
  }

  public static abstract class Mapping0<C> implements IMapping0<C> {
  
    public Introspector getIntrospector() {
      return new Introspector() {

        public <T> T setChildAt(T o, int i, Object child) {
          throw new RuntimeException("unexpected call");
        }

        public Object getChildAt(Object o, int i) {
          throw new RuntimeException("unexpected call");
        }

        public int getChildCount(Object o) {
          return 0;
        }

        public <T> T setChildren(T o, Object[] children) {
          throw new RuntimeException("unexpected call");
        }

        public Object[] getChildren(Object o) {
          return new Object[]{};
        }
      };
    }

  }

  public static interface IMapping1<C, D1> extends IMapping {
    C make(D1 t);
    D1 get0(C c);
  }

  public static abstract class Mapping1<C, D1> implements IMapping1<C, D1> {
  
    public Introspector getIntrospector() {
      return new Introspector() {

        public <T> T setChildAt(T o, int i, Object child) {
          throw new RuntimeException("unexpected call");
        }

        public Object getChildAt(Object o, int i) {
          throw new RuntimeException("unexpected call");
        }

        public int getChildCount(Object o) {
          return 0;
        }

        public <T> T setChildren(T o, Object[] children) {
          throw new RuntimeException("unexpected call");
        }

        public Object[] getChildren(Object o) {
          return new Object[]{};
        }
      };
    }

  }

  public static interface IMapping2<C, D1, D2> extends IMapping {
    C make(D1 t1, D2 t2);
    D1 get0(C c);
    D2 get1(C c);
  }

  public static abstract class Mapping2<C, D1, D2> implements IMapping2<C, D1, D2> {
  
    public Introspector getIntrospector() {
      return new Introspector() {

        public <T> T setChildAt(T o, int i, Object child) {
          throw new RuntimeException("unexpected call");
        }

        public Object getChildAt(Object o, int i) {
          throw new RuntimeException("unexpected call");
        }

        public int getChildCount(Object o) {
          return 0;
        }

        public <T> T setChildren(T o, Object[] children) {
          throw new RuntimeException("unexpected call");
        }

        public Object[] getChildren(Object o) {
          return new Object[]{};
        }
      };
    }

  }

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

  //implementation of the interfaces defined by users

  public static class a_Mapping extends Mapping0<T1> {
    private static Mapping0<T1> instance = new a_Mapping();

    public static Mapping0<T1> getInstance() {
      return instance;
    }

    public boolean isInstanceOf(Object subject) {
      return subject instanceof a;
    }

    public T1 make() {
      return a.make();
    }

    public Object setChildren(Object o, Object[] children) {
      return a.make();
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ };
    }

    public <T> T setChildAt(T o, int i, Object child) {
      assert false : "Unexpected call.";
      return null;
    }

    public Object getChildAt(Object o, int i) {
      return null;
    }

    public int getChildCount(Object o) {
      return 0;
    }

  }

  /** ------------------------------ */

  public static class b_Mapping extends Mapping0<T2> {
    private static Mapping0<T2> instance = new b_Mapping();

    public static Mapping0<T2> getInstance() {
      return instance;
    }

    public boolean isInstanceOf(Object subject) {
      return subject instanceof b;
    }

    public T2 make() {
      return b.make();
    }

    // LocalIntrospector
    public Object setChildren(Object o, Object[] children) {
      return b.make();
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ };
    }

    public <T> T setChildAt(T o, int i, Object child) {
      assert false : "Unexpected call.";
      return null;
    }

    public Object getChildAt(Object o, int i) {
      return null;
    }

    public int getChildCount(Object o) {
      return 0;
    }

    public Class forType() {
      return b.class;
    }
  }

  /** ------------------------------ */
  public static class f_Mapping extends Mapping2<T1, T1, T2> {
    private static Mapping2<T1, T1, T2> instance = new f_Mapping();

    public static Mapping2<T1, T1, T2> getInstance() {
      return instance;
    }

    public boolean isInstanceOf(Object subject) {
      return subject instanceof f;
    }

    public T1 make(T1 t1, T2 t2) {
      return f.make(t1, t2);
    }

    public T1 get0(T1 t) {
      return ((f)t).gets1(); 
    }

    public T2 get1(T1 t) {
      return ((f)t).gets2(); 
    }

    // LocalIntrospector
    public Object setChildren(Object o, Object[] children) {
      return f.make((T1) children[0], (T2) children[1]);
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ ((f)o).gets1(), ((f)o).gets2() };
    }

    public Object setChildAt(Object o, int i, Object child) {
      switch (i) {
        case 0: return f.make((T1) child, ((f)o).gets2());
        case 1: return f.make(((f)o).gets1(), (T2) child);
      }
      assert false : "Unexpected call.";
      return null;
    }

    public Object getChildAt(Object o, int i) {
      switch (i) {
        case 0: return ((f)o).gets1();
        case 1: return ((f)o).gets2();
      }
      assert false : "Unexpected call.";
      return null;
    }

    public int getChildCount(Object o) {
      return 2;
    }

    public Class forType() {
      return f.class;
    }

  }

  /** ------------------------------ */
  public static class g_Mapping extends Mapping1<T2, T2> {
    private static Mapping1<T2, T2> instance = new g_Mapping();

    public static Mapping1<T2, T2> getInstance() {
      return instance;
    }

    public boolean isInstanceOf(Object subject) {
      return subject instanceof g;
    }

    public T2 make(T2 t2) {
      return g.make(t2);
    }

    public T2 get0(T2 t) {
      return ((g) t).gets2();
    }

    // LocalIntrospector
    public Object setChildren(Object o, Object[] children) {
      return g.make((T2) children[0]);
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ ((g)o).gets2() };
    }

    public Object setChildAt(Object o, int i, Object child) {
      switch (i) {
        case 0: return g.make((T2) child);
      }
      assert false : "Unexpected call.";
      return null;
    }

    public Object getChildAt(Object o, int i) {
      switch (i) {
        case 0: return ((g)o).gets2();
      }
      assert false : "Unexpected call.";
      return null;
    }

    public int getChildCount(Object o) {
      return 1;
    }

    public Class forType() {
      return g.class;
    }

  }


}
