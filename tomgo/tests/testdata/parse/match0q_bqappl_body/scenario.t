public class Match0q {
  public void f(Object t) {
    %match(t) {
      x -> { return `Foo(x); }
    }
  }
}
