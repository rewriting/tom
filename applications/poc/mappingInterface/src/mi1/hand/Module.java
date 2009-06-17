package mi1.hand;

import base.hand.types.t1.*;
import base.hand.types.t2.*;
import base.hand.types.T2;
import base.hand.types.T1;
import base.hand.types.T;

import mi1.mapping.Mapping;

/**
 * @author nvintila
 * @date 2:32:06 PM Jun 13, 2009
 */
public class Module {

    public static final Module instance = new Module();


    /**
     * Mapping interface - contract between Tom and an arbitrary type.
     * Generated from either Gom or Tom mapping.
     * The implementation could be generated as well - same code as it is today but put behind this
     * interface.
     * Alternatively, the impl can be manually written thus permitting completely manual mappings!
     */
    public static interface a_MappingI /*extends tom.library.sl.Introspector */ {
        // Test
        boolean isSym(Object t);// Slot getters

        a make();
    }

    public static class a_Mapping extends Mapping implements a_MappingI {
        public static a_Mapping instance = new a_Mapping();

        // Test
        public boolean isSym(Object t) {
            return t instanceof a;
        }

        // Make
        public a make() {
            return new a();
        }

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

    /** ------------------------------ */
    public static interface b_MappingI /*extends tom.library.sl.Introspector*/ {
        // Test
        boolean isSym(Object t);// Slot getters

        b make();
    }

    public static class b_Mapping extends Mapping implements b_MappingI {
        public static b_Mapping instance = new b_Mapping();

        // Test
        public boolean isSym(Object t) {
            return t instanceof b;
        }

        // Make
        public b make() {
            return new b();
        }

        public b make(Object[] children) {
          return make();
        }

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

    /** ------------------------------ */
    public static interface f_MappingI /*extends tom.library.sl.Introspector*/ {
        // Test
        boolean isSym(Object t);

        f make(T s1, T s2);

        // Slot getters
        T1 getS1(Object t);

        T2 getS2(Object t);

    }

    public static class f_Mapping extends Mapping implements f_MappingI {
        public static f_Mapping instance = new f_Mapping();

        // Test
        public boolean isSym(Object t) {
            return t instanceof f;
        }

        // Make
        public f make(T s1, T s2) {
            return new f((T1)s1, (T2)s2);
        }

        public f make(Object[] children) {
          return make((T)children[0],(T)children[1]);
        }

        public T1 getS1(Object t) {
            return ((f)t).getS1();
        }

        public T2 getS2(Object t) {
            return ((f)t).getS2();
        }

        // LocalIntrospector
        public f setChildren(Object o, Object[] children) {
            return make((T1)children[0], (T2)children[1]);
        }

        public Object[] getChildren(Object o) {
            return new Object[]{ ((f)o).getS1(), ((f)o).getS2() };
        }

        public Object setChildAt(Object o, int i, Object child) {
            switch (i) {
                case 0:
                    return make((T1)child, ((f)o).getS2());
                case 1:
                    return make(((f)o).getS1(), (T2)child);
                //todo: or ((f)o).setS2((T2)child); ?
            }
            assert false : "Unexpected call.";
            return null;
        }

        public Object getChildAt(Object o, int i) {
            switch (i) {
                case 0:
                    return getS1(o);
                case 1:
                    return getS2(o);
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
    public static interface g_MappingI {
        // Test
        boolean isSym(Object t);

        g make(T s2);

        // Slot getters
        T2 getS2(Object t);

    }

    public static class g_Mapping extends Mapping implements g_MappingI {
        public static g_Mapping instance = new g_Mapping();

        // Test
        public boolean isSym(Object t) {
            return t instanceof g;
        }

        // Make
        public g make(T s2) {
            return new g((T2)s2);
        }
        public g make(Object[] children) {
          return make((T)children[0]);
        }

        public T2 getS2(Object t) {
            return ((g)t).getS2();
        }

        // LocalIntrospector
        public g setChildren(Object o, Object[] children) {
            return make((T2)children[0]);
        }

        public Object[] getChildren(Object o) {
            return new Object[]{ (((g)o).getS2()) };
        }

        public /*<T> T*/ Object setChildAt(/*T*/Object o, int i, Object child) {
            switch (i) {
                case 0:
                    return make((T2)child);
                //todo : or ((g)o).setS2((T2)child); ?
            }
            assert false : "Unexpected call.";
            return null;
        }

        public Object getChildAt(Object o, int i) {
            switch (i) {
                case 0:
                    return getS2(o);
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
