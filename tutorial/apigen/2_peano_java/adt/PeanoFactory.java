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
    funNat_Zero = makeAFun("_Nat_zero", 0, false);
    protoNat_Zero = new Nat_Zero();

    Nat_Suc.initializePattern();
    funNat_Suc = makeAFun("_Nat_suc", 1, false);
    protoNat_Suc = new Nat_Suc();

  }

  protected Nat_Zero makeNat_Zero(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoNat_Zero) {
      protoNat_Zero.initHashCode(annos,fun,args);
      return (Nat_Zero) build(protoNat_Zero);
    }
  }

  public Nat_Zero makeNat_Zero() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeNat_Zero( funNat_Zero, args, empty);
  }

  protected Nat_Suc makeNat_Suc(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoNat_Suc) {
      protoNat_Suc.initHashCode(annos,fun,args);
      return (Nat_Suc) build(protoNat_Suc);
    }
  }

  public Nat_Suc makeNat_Suc(Nat _pred) {
    aterm.ATerm[] args = new aterm.ATerm[] {_pred};
    return makeNat_Suc( funNat_Suc, args, empty);
  }

}
