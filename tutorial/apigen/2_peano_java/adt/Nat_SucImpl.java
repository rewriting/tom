package adt;

abstract public class Nat_SucImpl
extends Nat
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected Nat_SucImpl(PeanoFactory factory) {
    super(factory);
  }
  private static int index_pred = 0;
  public shared.SharedObject duplicate() {
    Nat_Suc clone = new Nat_Suc(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Nat_Suc) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getPeanoFactory().makeNat_Suc(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getPeanoFactory().toTerm(this);
    }
    return term;
  }

  public boolean isSuc()
  {
    return true;
  }

  public boolean hasPred()
  {
    return true;
  }

  public Nat getPred()
  {
    return (Nat) this.getArgument(index_pred) ;
  }

  public Nat setPred(Nat _pred)
  {
    return (Nat) super.setArgument(_pred, index_pred);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof Nat)) { 
          throw new RuntimeException("Argument 0 of a Nat_Suc should have type Nat");
        }
        break;
      default: throw new RuntimeException("Nat_Suc does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
    a += (getArgument(0).hashCode() << 0);

    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);

    return c;
  }
}
