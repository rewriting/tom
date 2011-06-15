package debugger;

import tom.library.sl.*;

public class Factory {

  %include{ sl.tom }

  public static Strategy makeTry(Strategy s) {
    return new Decorator("Try", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `Try(tmp); }}, new Strategy[] {s});
  }

  public static Strategy makeTopDown(Strategy s) {
    return new Decorator("TopDown", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `TopDown(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeTopDownCollect(Strategy s) {
    return new Decorator("TopDownCollect", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `TopDownCollect(tmp); }}, new Strategy[] {s});
  }

  public static Strategy makeTopDownIdStopOnSuccess(Strategy s) {
    return new Decorator("TopDownIdStopOnSuccess", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `TopDownIdStopOnSuccess(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeBottomUp(Strategy s) {
    return new Decorator("BottomUp", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `BottomUp(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeOnceBottomUp(Strategy s) {
    return new Decorator("OnceBottomUp", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `OnceBottomUp(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeOnceTopDown(Strategy s) {
    return new Decorator("OnceTopDown", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `OnceTopDown(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeInnermost(Strategy s) {
    return new Decorator("Innermost", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `Innermost(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeOutermost(Strategy s) {
    return new Decorator("Outermost", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `Outermost(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeRepeat(Strategy s) {
    return new Decorator("Repeat", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `Repeat(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeRepeatId(Strategy s) {
    return new Decorator("RepeatId", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `RepeatId(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeOnceBottomUpId(Strategy s) {
    return new Decorator("OnceBottomUpId", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `OnceBottomUpId(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeOnceTopDownId(Strategy s) {
    return new Decorator("OnceTopDownId", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `OnceTopDownId(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeInnermostId(Strategy s) {
    return new Decorator("InnermostId", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `InnermostId(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeInnermostIdSeq(Strategy s) {
    return new Decorator("InnermostIdSeq", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `InnermostIdSeq(tmp); }}, new Strategy[] {s});
  }


  public static Strategy makeOutermostId(Strategy s) {
    return new Decorator("OutermostId", new Maker() { public Strategy build(Strategy[] a) { Strategy tmp=a[0]; return `OutermostId(tmp); }}, new Strategy[] {s});
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
