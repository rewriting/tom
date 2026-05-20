public class Strat2 {
  %strategy Walk() extends `Identity() {
    visit Term {
      x -> { return `f(x); }
    }
  }
}
