package jtom.adt;

import aterm.*;

public class TomType_TypeImpl
extends TomType
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_tomType = 0;
  private static int index_tlType = 1;

  public shared.SharedObject duplicate() {
    TomType_Type clone = new TomType_Type();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomType_Type(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("Type(<term>,<term>)");
  }


  static public TomType fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomType tmp = getStaticTomSignatureFactory().makeTomType_Type(TomType.fromTerm( (aterm.ATerm) children.get(0)), TomType.fromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isType()
  {
    return true;
  }

  public boolean hasTomType()
  {
    return true;
  }

  public boolean hasTlType()
  {
    return true;
  }


  public TomType getTomType()
  {
    return (TomType) this.getArgument(index_tomType) ;
  }

  public TomType setTomType(TomType _tomType)
  {
    return (TomType) super.setArgument(_tomType, index_tomType);
  }

  public TomType getTlType()
  {
    return (TomType) this.getArgument(index_tlType) ;
  }

  public TomType setTlType(TomType _tlType)
  {
    return (TomType) super.setArgument(_tlType, index_tlType);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomType)) { 
          throw new RuntimeException("Argument 0 of a TomType_Type should have type TomType");
        }
        break;
      case 1:
        if (! (arg instanceof TomType)) { 
          throw new RuntimeException("Argument 1 of a TomType_Type should have type TomType");
        }
        break;
      default: throw new RuntimeException("TomType_Type does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }

  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = 0x9e3779b9;
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
