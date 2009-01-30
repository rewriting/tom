package debugger;

import tom.library.sl.Strategy;

public abstract class Maker {
  protected abstract Strategy build(Strategy[] children);
  public Strategy buildDecorated(Strategy[] children) {
    Strategy res = build(children);
    return debugger.Lib.weaveBasicDecorators(res);
  }
}
