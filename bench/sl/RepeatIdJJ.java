package sl;

import jjtraveler.*;

public class RepeatIdJJ extends SequenceIdJJ {

  public RepeatIdJJ(Visitor v) {
    super(v,null);
    then = this;
  }

}
