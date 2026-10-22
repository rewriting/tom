public class Match0r {
  public void f(Object t) {
    %match(t) {
      x -> { return `Foo(`a, `b); }
    }
  }
}
