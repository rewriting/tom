
package polygraphicprogram.strategy.threepath;

public class Is_ThreeCell extends tom.library.sl.AbstractStrategy {
  private static final String msg = "Not an ThreeCell";

  public Is_ThreeCell() {
    initSubterm();
  }

  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    if(any instanceof polygraphicprogram.types.threepath.ThreeCell) {
     return any;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit() {
    tom.library.sl.Visitable any = environment.getSubject();
    if(any instanceof polygraphicprogram.types.threepath.ThreeCell) {
     return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
