package jtom.adt;

import aterm.*;

public class TomTerm_IfThenElseImpl
extends TomTerm
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_condition = 0;
  private static int index_succesList = 1;
  private static int index_failureList = 2;

  public shared.SharedObject duplicate() {
    TomTerm_IfThenElse clone = new TomTerm_IfThenElse();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_IfThenElse(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("IfThenElse(<term>,<term>,<term>)");
  }


  static public TomTerm fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomTerm tmp = getStaticTomSignatureFactory().makeTomTerm_IfThenElse(Expression.fromTerm( (aterm.ATerm) children.get(0)), TomList.fromTerm( (aterm.ATerm) children.get(1)), TomList.fromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isIfThenElse()
  {
    return true;
  }

  public boolean hasCondition()
  {
    return true;
  }

  public boolean hasSuccesList()
  {
    return true;
  }

  public boolean hasFailureList()
  {
    return true;
  }


  public Expression getCondition()
  {
    return (Expression) this.getArgument(index_condition) ;
  }

  public TomTerm setCondition(Expression _condition)
  {
    return (TomTerm) super.setArgument(_condition, index_condition);
  }

  public TomList getSuccesList()
  {
    return (TomList) this.getArgument(index_succesList) ;
  }

  public TomTerm setSuccesList(TomList _succesList)
  {
    return (TomTerm) super.setArgument(_succesList, index_succesList);
  }

  public TomList getFailureList()
  {
    return (TomList) this.getArgument(index_failureList) ;
  }

  public TomTerm setFailureList(TomList _failureList)
  {
    return (TomTerm) super.setArgument(_failureList, index_failureList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof Expression)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_IfThenElse should have type Expression");
        }
        break;
      case 1:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_IfThenElse should have type TomList");
        }
        break;
      case 2:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 2 of a TomTerm_IfThenElse should have type TomList");
        }
        break;
      default: throw new RuntimeException("TomTerm_IfThenElse does not have an argument at " + i );
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
