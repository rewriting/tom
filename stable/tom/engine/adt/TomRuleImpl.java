package jtom.adt;

import aterm.*;

abstract public class TomRuleImpl extends TomSignatureConstructor
{
  protected TomRuleImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(TomRule peer)
  {
    return super.isEqual(peer);
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
     throw new UnsupportedOperationException("This TomRule has no Lhs");
  }

  public TomRule setLhs(TomTerm _lhs)
  {
     throw new IllegalArgumentException("Illegal argument: " + _lhs);
  }

  public TomTerm getRhs()
  {
     throw new UnsupportedOperationException("This TomRule has no Rhs");
  }

  public TomRule setRhs(TomTerm _rhs)
  {
     throw new IllegalArgumentException("Illegal argument: " + _rhs);
  }

  public TomList getCondList()
  {
     throw new UnsupportedOperationException("This TomRule has no CondList");
  }

  public TomRule setCondList(TomList _condList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _condList);
  }

  public Option getOption()
  {
     throw new UnsupportedOperationException("This TomRule has no Option");
  }

  public TomRule setOption(Option _option)
  {
     throw new IllegalArgumentException("Illegal argument: " + _option);
  }

}

