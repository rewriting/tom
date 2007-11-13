
package polygraphicprogram.strategy.threepath;

public class Is_EmptyThreeC2 extends tom.library.sl.AbstractStrategy {
  private static final String msg = "Not an EmptyThreeC2";

  public Is_EmptyThreeC2() {
    initSubterm();
  }

  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    if(any instanceof polygraphicprogram.types.threepath.EmptyThreeC2) {
     return any;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit() {
    tom.library.sl.Visitable any = environment.getSubject();
    if(any instanceof polygraphicprogram.types.threepath.EmptyThreeC2) {
     return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
