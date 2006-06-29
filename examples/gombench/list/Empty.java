package gombench.list;

public class Empty extends List {

  public String toString() {
    return "()";
  }

  public boolean equals(Object o) {
    if(o instanceof Empty) {
      return true;
    }
    return false;
  }
}
