package mi2.gom;

import base.data.types.t1.*;
import base.data.types.t2.*;
import base.data.types.T2;
import base.data.types.T1;

import mi2.mapping.IntrospectorMapping;

/**
 * @author nvintila
 * @date 2:32:06 PM Jun 13, 2009
 */
public class Module {

  public static final Module instance = new Module();

  /**
   * for each constructor f, the interface f_MappingI and the abstract class
   * f_Introspector can be generated by Tom from the Module specification. If
   * the user do not need introspector, he just has to implement f_MappingI. 
   */

  public static interface a_MappingI {

    // Test
    boolean isSym(Object t);

    // Slot getters
    a make();

  }

  public static abstract class a_Introspector extends IntrospectorMapping<a> implements a_MappingI {

    public a make(Object[] children) {
      return make();
    }

    // LocalIntrospector
    public a setChildren(Object o, Object[] children) {
      return make();
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ };
    }

    public/* <T> T*/Object setChildAt(/*T*/Object o, int i, Object child) {
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
      return a.class;
    }
  }


  public static class a_Mapping extends a_Introspector {
    public static a_Mapping instance = new a_Mapping();

    // Test
    public boolean isSym(Object t) {
      return t instanceof a;
    }

    // Make
    public a make() {
      return a.make();
    }

  }

  /** ------------------------------ */
  public static interface b_MappingI {
    // Test
    boolean isSym(Object t);// Slot getters

    b make();
  }

  public static abstract class b_Introspector extends IntrospectorMapping<b> implements b_MappingI {

    // LocalIntrospector
    public b setChildren(Object o, Object[] children) {
      return make();
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ };
    }

    public /* <T> T*/Object setChildAt(/*T*/Object o, int i, Object child) {
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


  public static class b_Mapping extends b_Introspector {
    public static b_Mapping instance = new b_Mapping();

    // Test
    public boolean isSym(Object t) {
      return t instanceof b;
    }

    // Make
    public b make() {
      return b.make();
    }

    public b make(Object[] children) {
      return make();
    }

  }

  /** ------------------------------ */
  public static interface f_MappingI {
    // Test
    boolean isSym(Object t);

    f make(Object s1, Object s2);

    // Slot getters
    T1 gets1(Object t);

    T2 gets2(Object t);

  }

  public static abstract class f_Introspector extends IntrospectorMapping<f> implements f_MappingI {

    // LocalIntrospector
    public f setChildren(Object o, Object[] children) {
      return make(children[0], children[1]);
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ ((f)o).gets1(), ((f)o).gets2() };
    }

    public Object setChildAt(Object o, int i, Object child) {
      switch (i) {
        case 0:
          return make(child, ((f)o).gets2());
        case 1:
          return make(((f)o).gets1(), (T2)child);
          //todo: or ((f)o).setS2((T2)child); ?
      }
      assert false : "Unexpected call.";
      return null;
    }

    public Object getChildAt(Object o, int i) {
      switch (i) {
        case 0:
          return gets1(o);
        case 1:
          return gets2(o);
      }
      assert false : "Unexpected call.";
      return null;
    }

    public int getChildCount(Object o) {
      return 2;
    }

    public f make(Object[] children) {
      return make(children[0], children[1]);
    }

    public Class forType() {
      return f.class;
    }

  }


  public static class f_Mapping extends f_Introspector {
    public static f_Mapping instance = new f_Mapping();

    // Test
    public boolean isSym(Object t) {
      return t instanceof f;
    }

    // Make
    public f make(Object s1, Object s2) {
      return f.make((T1)s1, (T2)s2);
    }

    public T1 gets1(Object t) {
      return ((f)t).gets1();
    }

    public T2 gets2(Object t) {
      return ((f)t).gets2();
    }

  }



  /** ------------------------------ */
  public static interface g_MappingI {
    // Test
    boolean isSym(Object t);

    g make(Object s2);

    // Slot getters
    T2 gets2(Object t);

  }

  public static abstract class g_Introspector extends IntrospectorMapping<g> implements g_MappingI {
    public g setChildren(Object o, Object[] children) {
      return make(children[0]);
    }

    public Object[] getChildren(Object o) {
      return new Object[]{ (((g)o).gets2()) };
    }

    public /*<T> T*/ Object setChildAt(/*T*/Object o, int i, Object child) {
      switch (i) {
        case 0:
          return make(child);
          //todo : or ((g)o).setS2((T2)child); ?
      }
      assert false : "Unexpected call.";
      return null;
    }

    public Object getChildAt(Object o, int i) {
      switch (i) {
        case 0:
          return gets2(o);
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

  public static class g_Mapping extends g_Introspector {
    public static g_Mapping instance = new g_Mapping();

    // Test
    public boolean isSym(Object t) {
      return t instanceof g;
    }

    // Make
    public g make(Object s2) {
      return g.make((T2)s2);
    }

    public g make(Object[] children) {
      return make(children[0]);
    }

    public T2 gets2(Object t) {
      return ((g)t).gets2();
    }

  }



}
