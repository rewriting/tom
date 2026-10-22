public class Match0m {
  public void f(Object t) {
    %match(t) {
      (Foo|Bar)() -> { }
    }
  }
}
