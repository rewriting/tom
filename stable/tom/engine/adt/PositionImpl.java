package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class PositionImpl extends TomSignatureConstructor
{
  static Position fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  static Position fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(Position peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static Position fromTerm(aterm.ATerm trm)
  {
    Position tmp;
    if ((tmp = Position_Position.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Position: " + trm);
  }

  public boolean isPosition()
  {
    return false;
  }

  public boolean hasLine()
  {
    return false;
  }

  public boolean hasColumn()
  {
    return false;
  }

  public Integer getLine()
  {
     throw new RuntimeException("This Position has no Line");
  }

  public Position setLine(Integer _line)
  {
     throw new RuntimeException("This Position has no Line");
  }

  public Integer getColumn()
  {
     throw new RuntimeException("This Position has no Column");
  }

  public Position setColumn(Integer _column)
  {
     throw new RuntimeException("This Position has no Column");
  }


}

