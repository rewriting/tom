public class Match0i {
  public void f(Object t) {
    %match(t) {
      Foo() << t -> { }
    }
  }
}
