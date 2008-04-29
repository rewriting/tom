package debugger;

import tom.library.sl.*;

public class Factory {

  %include{ sl.tom }

  public static Strategy makeTry(Strategy s) {
    return new Decorator("Try", new Maker() { public Strategy build(Strategy[] a) { return `Try(a[0]); }}, new Strategy[] {s});
  }

  public static Strategy makeTopDown(Strategy s) {
    return new Decorator("TopDown", new Maker() { public Strategy build(Strategy[] a) { return `TopDown(a[0]); }}, new Strategy[] {s});
  }

  /*
  public static Strategy makeTopDownCollect(Strategy s) {
    return new Decorator("TopDownCollect", `TopDownCollect(s), new Strategy[] {s});
  }


  public static Strategy makeTopDownOnSucces(Strategy s) {
    return new Decorator("TopDownOnSucces", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeTopDownIdStopOnSucces(Strategy s) {
    return new Decorator("TopDownIdStopOnSucces", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeBottomUp(Strategy s) {
    return new Decorator("BottomUp", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeOnceBottomUp(Strategy s) {
    return new Decorator("OnceBottomUp", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeOnceTopDown(Strategy s) {
    return new Decorator("OnceTopDown", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeInnermost(Strategy s) {
    return new Decorator("Innermost", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeOutermost(Strategy s) {
    return new Decorator("Outermost", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeRepeat(Strategy s) {
    return new Decorator("Repeat", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeRepeatId(Strategy s) {
    return new Decorator("RepeatId", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeOnceBottomUpId(Strategy s) {
    return new Decorator("OnceBottomUpId", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeOnceTopDownId(Strategy s) {
    return new Decorator("OnceTopDownId", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeInnermostId(Strategy s) {
    return new Decorator("InnermostId", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeInnermostIdSeq(Strategy s) {
    return new Decorator("InnermostIdSeq", `TopDown(s), new Strategy[] {s});
  }

  public static Strategy makeOutermostId(Strategy s) {
    return new Decorator("OutermostId", `TopDown(s), new Strategy[] {s});
  }
*/

}
