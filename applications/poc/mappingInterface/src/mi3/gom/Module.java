package mi3.gom;

import base.data.types.t1.*;
import base.data.types.t2.*;
import base.data.types.T2;
import base.data.types.T1;


public class Module {

  public static final Module instance = new Module();

  public static interface Mapping0<C> extends tom.library.sl.Introspector {
    boolean isInstanceOf(Object subject, C classObject);
    C make();
  }

  public static interface Mapping1<C, D1> extends tom.library.sl.Introspector {
    boolean isInstanceOf(Object subject, C classObject);
    C make(D1 t);
    D1 get1(C c);
  }

public interface Introspector1 {
  public <T> T setChildren(T o, Object[] children);
  public Object[] getChildren(Object o);
}

  public static interface Mapping2<C, D1, D2> extends tom.library.sl.Introspector {
    boolean isInstanceOf(Object subject, C classObject);
    C make(D1 t1, D2 t2);
    D1 get1(C c);
    D2 get2(C c);
  }



public interface Introspector2 {
  public <T> T setChildAt(T o, int i, Object child);
  public Object getChildAt(Object o, int i);
  public int getChildCount(Object o);
}

  public static class a_Mapping implements Mapping0<T1> {
    private static Mapping0<T1> instance = new a_Mapping();
  }


// move into SL
public class IntrospectorFactory {
  private IntrospectorFactory() {}
  public static tom.library.sl.Introspector makeFromInstrospertor1(Introspector1 i) {
  }

  public static Mapping0<T1> getInstance() {
    return instance;
  }

  public static tom.library.sl.Introspector makeFromInstrospertor2(Introspector2 i) {
    public boolean isInstanceOf(Object subject, T1 classObject) {
      return subject instanceof a;
    }

  }

  public static tom.library.sl.Introspector makeFromInstrospertor12(Introspector1 i1, Introspector2 i2) {
    return new tom.library.sl.Introspector() {
    };
  }

  public T1 make() {
    return a.make();
  }

}

private Introspector1 introspector1 = new Introspector1() {
    public Object setChildren(Object o, Object[] children) {
      return a.make();
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ };
    }
}

private Introspector2 introspector2 = new Introspector2() {
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

  public static class a_Mapping implements MappingI<a> {
    private tom.library.sl.Introspector introspector;
    public a_Mapping(tom.library.sl.Introspector i) {
      this.introspector = i;
    }

    private static MappingI<a> instance = new a_Mapping();

    public static MappingI<a> getInstance() {
      return instance;
    }

    public boolean isInstanceOf(Object subject, a classObject) {
      return subject instanceof a;
    }

    public a make(Object[] children) {
      return a.make();
    }

    // LocalIntrospector


    public Class forType() {
      return a.class;
    }
  }

  /** ------------------------------ */

  public static class b_Mapping implements Mapping0<T2> {
    private static Mapping0<T2> instance = new b_Mapping();

    public static Mapping0<T2> getInstance() {
      return instance;
    }

    public boolean isInstanceOf(Object subject, T2 classObject) {
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
  public static class f_Mapping implements Mapping2<T1, T1, T2> {
    private static Mapping2<T1, T1, T2> instance = new f_Mapping();

    public static Mapping2<T1, T1, T2> getInstance() {
      return instance;
    }

    public boolean isInstanceOf(Object subject, T1 classObject) {
      return subject instanceof f;
    }

    public T1 make(T1 t1, T2 t2) {
      return f.make(t1, t2);
    }

    public T1 get1(T1 t) {
      return ((f)t).gets1(); 
    }

    public T2 get2(T1 t) {
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
  public static class g_Mapping implements Mapping1<T2, T2> {
    private static Mapping1<T2, T2> instance = new g_Mapping();

    public static Mapping1<T2, T2> getInstance() {
      return instance;
    }

    public boolean isInstanceOf(Object subject, T2 classObject) {
      return subject instanceof g;
    }

    public T2 make(T2 t2) {
      return g.make(t2);
    }

    public T2 get1(T2 t) {
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
