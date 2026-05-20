public class Match0s {
  public void f(Object t) {
    %match(t) {
      x -> { return `Foo(`x*); }
    }
  }
}
