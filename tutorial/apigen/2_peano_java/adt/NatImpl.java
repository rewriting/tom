package adt;


abstract public class NatImpl extends PeanoConstructor
{
  protected NatImpl(PeanoFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(Nat peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortNat()  {
    return true;
  }

  public boolean isZero()
  {
    return false;
  }

  public boolean isSuc()
  {
    return false;
  }

  public boolean hasPred()
  {
    return false;
  }

  public Nat getPred()
  {
     throw new UnsupportedOperationException("This Nat has no Pred");
  }

  public Nat setPred(Nat _pred)
  {
     throw new IllegalArgumentException("Illegal argument: " + _pred);
  }

}

