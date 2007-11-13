
package polygraphicprogram.strategy.twopath;

public class Is_TwoCell extends tom.library.sl.AbstractStrategy {
  private static final String msg = "Not an TwoCell";

  public Is_TwoCell() {
    initSubterm();
  }

  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    if(any instanceof polygraphicprogram.types.twopath.TwoCell) {
     return any;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit() {
    tom.library.sl.Visitable any = environment.getSubject();
    if(any instanceof polygraphicprogram.types.twopath.TwoCell) {
     return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
