package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomRuleImpl extends TomSignatureConstructor
{
  TomRuleImpl(TomSignatureFactory factory) {
     super(factory);
  }
  public boolean isEqual(TomRule peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortTomRule()  {
    return true;
  }

  public boolean isRewriteRule()
  {
    return false;
  }

  public boolean hasLhs()
  {
    return false;
  }

  public boolean hasRhs()
  {
    return false;
  }

  public boolean hasCondList()
  {
    return false;
  }

  public boolean hasOption()
  {
    return false;
  }

  public TomTerm getLhs()
  {
     throw new RuntimeException("This TomRule has no Lhs");
  }

  public TomRule setLhs(TomTerm _lhs)
  {
     throw new RuntimeException("This TomRule has no Lhs");
  }

  public TomTerm getRhs()
  {
     throw new RuntimeException("This TomRule has no Rhs");
  }

  public TomRule setRhs(TomTerm _rhs)
  {
     throw new RuntimeException("This TomRule has no Rhs");
  }

  public TomList getCondList()
  {
     throw new RuntimeException("This TomRule has no CondList");
  }

  public TomRule setCondList(TomList _condList)
  {
     throw new RuntimeException("This TomRule has no CondList");
  }

  public Option getOption()
  {
     throw new RuntimeException("This TomRule has no Option");
  }

  public TomRule setOption(Option _option)
  {
     throw new RuntimeException("This TomRule has no Option");
  }

}

