package gombench.list;

public class Cons extends List {
  public int head;
  public List tail;
  public Cons(int arg0, List arg1) {
    head = arg0;
    tail = arg1;
  }

  public String toString() {
    return "("+head+" "+tail.toString()+")";
  }

  public boolean equals(Object o) {
    if(o instanceof Cons) {
      Cons f = (Cons) o;
      return (head == (f.head)) && tail.equals(f.tail);
    }
    return false;
  }
}
