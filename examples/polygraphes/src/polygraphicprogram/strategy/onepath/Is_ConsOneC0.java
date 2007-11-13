
package polygraphicprogram.strategy.onepath;

public class Is_ConsOneC0 extends tom.library.sl.AbstractStrategy {
  private static final String msg = "Not an ConsOneC0";

  public Is_ConsOneC0() {
    initSubterm();
  }

  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    if(any instanceof polygraphicprogram.types.onepath.ConsOneC0) {
     return any;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit() {
    tom.library.sl.Visitable any = environment.getSubject();
    if(any instanceof polygraphicprogram.types.onepath.ConsOneC0) {
     return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
