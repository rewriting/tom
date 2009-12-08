import essai.sig.types.*;
import java.util.LinkedList;

class Essai {

  %include { string.tom }

  %gom {
    module sig
    abstract syntax

    T1 = f(x:T1)
       | a()

    T2 = g(x:T2)
       | b()
  }


  private static <T> java.util.LinkedList<T> listAppend(T o, java.util.LinkedList<T> l) {
      java.util.LinkedList res = (java.util.LinkedList<T>) l.clone();
      res.addFirst(o);
      return res;
    }
  private static <T> java.util.LinkedList<T> listGetTail(java.util.LinkedList<T> l) {
      java.util.LinkedList<T> res = (java.util.LinkedList<T>) l.clone();
      res.removeFirst();
      return res;
  }

  //generics

  %typeterm LList<T> {
    implement      { java.util.LinkedList<$T> }
    is_sort(t)     { t instanceof java.util.LinkedList }
    equals(l1,l2)  { l1.equals(l2) }
  }

  %oplist LList<T> list<T>(T*) {
    is_fsym(l)       { l instanceof java.util.LinkedList }
    make_empty()     { new java.util.LinkedList<$T>() }
    make_insert(o,l) { listAppend(o,l) }
    get_head(l)      { l.getFirst() }
    get_tail(l)      { listGetTail(l) }
    is_empty(l)      { l.isEmpty() }
  }

  //generics

  public static void main(String[] args) {
    LinkedList<T1> l1 = new LinkedList<T1>();
    LinkedList<T2> l2 = new LinkedList<T2>();

    l1.add(`a());
    l1.add(`f(a()));
    l1.add(`f(f(a())));
    
    l2.add(`b());
    l2.add(`g(b()));
    l2.add(`g(g(b())));

    %match(l1) {
      list<T1>(_*,t@f(x),_*) -> { System.out.println("in l1 : " + `t); }
    }
    %match(LList<T2> l2) {
      (_*,t@g(x),_*) -> { System.out.println("in l2 : " + `t); }
    }
  }
}
