package jtom.adt;

import aterm.*;

public class TomTerm_RewriteRuleImpl
extends TomTerm
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_lhs = 0;
  private static int index_rhs = 1;
  private static int index_orgTrack = 2;

  public shared.SharedObject duplicate() {
    TomTerm_RewriteRule clone = new TomTerm_RewriteRule();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_RewriteRule(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("RewriteRule(<term>,<term>,<term>)");
  }


  static public TomTerm fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomTerm tmp = getStaticTomSignatureFactory().makeTomTerm_RewriteRule(TomTerm.fromTerm( (aterm.ATerm) children.get(0)), TomTerm.fromTerm( (aterm.ATerm) children.get(1)), Option.fromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isRewriteRule()
  {
    return true;
  }

  public boolean hasLhs()
  {
    return true;
  }

  public boolean hasRhs()
  {
    return true;
  }

  public boolean hasOrgTrack()
  {
    return true;
  }


  public TomTerm getLhs()
  {
    return (TomTerm) this.getArgument(index_lhs) ;
  }

  public TomTerm setLhs(TomTerm _lhs)
  {
    return (TomTerm) super.setArgument(_lhs, index_lhs);
  }

  public TomTerm getRhs()
  {
    return (TomTerm) this.getArgument(index_rhs) ;
  }

  public TomTerm setRhs(TomTerm _rhs)
  {
    return (TomTerm) super.setArgument(_rhs, index_rhs);
  }

  public Option getOrgTrack()
  {
    return (Option) this.getArgument(index_orgTrack) ;
  }

  public TomTerm setOrgTrack(Option _orgTrack)
  {
    return (TomTerm) super.setArgument(_orgTrack, index_orgTrack);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_RewriteRule should have type TomTerm");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_RewriteRule should have type TomTerm");
        }
        break;
      case 2:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 2 of a TomTerm_RewriteRule should have type Option");
        }
        break;
      default: throw new RuntimeException("TomTerm_RewriteRule does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }

  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = 0x9e3779b9;
    a += (getArgument(2).hashCode() << 16);
    a += (getArgument(1).hashCode() << 8);
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
