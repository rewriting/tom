package adt;


public class Nat_SucImpl
extends Nat
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_pred = 0;

  public shared.SharedObject duplicate() {
    Nat_Suc clone = new Nat_Suc();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getPeanoFactory().makeNat_Suc(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("suc(<term>)");
  }


  static public Nat fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Nat tmp = getStaticPeanoFactory().makeNat_Suc(Nat.fromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
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
    int b = 0x9e3779b9;
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
