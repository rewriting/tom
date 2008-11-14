package sl;

import jjtraveler.*;

public class InnermostIdJJ extends Sequence {

  public InnermostIdJJ(Visitor v) {
    super(null,null);
    first = new All(this);
    then = new SequenceIdJJ(v,this);
  }

}
