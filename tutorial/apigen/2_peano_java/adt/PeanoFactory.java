package adt;

import aterm.pure.PureFactory;
public class PeanoFactory
{
  private PureFactory factory;
  private aterm.AFun funNat_Zero;
  private Nat protoNat_Zero;
  private aterm.ATerm patternNat_Zero;
  private aterm.AFun funNat_Suc;
  private Nat protoNat_Suc;
  private aterm.ATerm patternNat_Suc;
  public PeanoFactory(PureFactory factory)
  {
     this.factory = factory;
     initialize();
  }
  public PureFactory getPureFactory()
  {
    return factory;
  }
  private void initialize()
  {

    patternNat_Zero = factory.parse("zero");
    funNat_Zero = factory.makeAFun("_Nat_zero", 0, false);
    protoNat_Zero = new Nat_Zero(this);

    patternNat_Suc = factory.parse("suc(<term>)");
    funNat_Suc = factory.makeAFun("_Nat_suc", 1, false);
    protoNat_Suc = new Nat_Suc(this);

  }
  protected Nat_Zero makeNat_Zero(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoNat_Zero) {
      protoNat_Zero.initHashCode(annos,fun,args);
      return (Nat_Zero) factory.build(protoNat_Zero);
    }
  }

  public Nat_Zero makeNat_Zero() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeNat_Zero(funNat_Zero, args, factory.getEmpty());
  }

  public Nat Nat_ZeroFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternNat_Zero);

    if (children != null) {
      Nat tmp = makeNat_Zero();
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Nat_ZeroImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return factory.make(patternNat_Zero, args);
  }

  protected Nat_Suc makeNat_Suc(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoNat_Suc) {
      protoNat_Suc.initHashCode(annos,fun,args);
      return (Nat_Suc) factory.build(protoNat_Suc);
    }
  }

  public Nat_Suc makeNat_Suc(Nat _pred) {
    aterm.ATerm[] args = new aterm.ATerm[] {_pred};
    return makeNat_Suc(funNat_Suc, args, factory.getEmpty());
  }

  public Nat Nat_SucFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternNat_Suc);

    if (children != null) {
      Nat tmp = makeNat_Suc(NatFromTerm( (aterm.ATerm) children.get(0)));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Nat_SucImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add((arg.getPred()).toTerm());    return factory.make(patternNat_Suc, args);
  }

  public Nat NatFromTerm(aterm.ATerm trm)
  {
    Nat tmp;
    tmp = Nat_ZeroFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Nat_SucFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Nat: " + trm);
  }
  public Nat NatFromString(String str)
  {
    aterm.ATerm trm = factory.parse(str);
    return NatFromTerm(trm);
  }
  public Nat NatFromFile(java.io.InputStream stream) throws java.io.IOException {
    return NatFromTerm(factory.readFromFile(stream));
  }
}
