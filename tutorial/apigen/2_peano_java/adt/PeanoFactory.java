package adt;

import aterm.pure.PureFactory;

public class PeanoFactory extends PureFactory
{
  private aterm.AFun funNat_Zero;
  private Nat protoNat_Zero;
  private aterm.AFun funNat_Suc;
  private Nat protoNat_Suc;

  public PeanoFactory()
  {
     super();
     initialize();
  }

  public PeanoFactory(int logSize)
  {
     super(logSize);
     initialize();
  }

  private void initialize()
  {
    Nat.initialize(this);

    Nat_Zero.initializePattern();
    funNat_Zero = makeAFun("Nat_Zero", 0, false);
    protoNat_Zero = new Nat_Zero();

    Nat_Suc.initializePattern();
    funNat_Suc = makeAFun("Nat_Suc", 1, false);
    protoNat_Suc = new Nat_Suc();

  }

  protected Nat makeNat_Zero(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoNat_Zero) {
      protoNat_Zero.initHashCode(annos,fun,args);
      return (Nat) build(protoNat_Zero);
    }
  }

  public Nat makeNat_Zero() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeNat_Zero( funNat_Zero, args, empty);
  }

  protected Nat makeNat_Suc(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoNat_Suc) {
      protoNat_Suc.initHashCode(annos,fun,args);
      return (Nat) build(protoNat_Suc);
    }
  }

  public Nat makeNat_Suc(Nat _pred) {
    aterm.ATerm[] args = new aterm.ATerm[] {_pred};
    return makeNat_Suc( funNat_Suc, args, empty);
  }

}
