package jtom.adt;

abstract public class TomTerm_NumberImpl
extends TomTerm
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_integer = 0;
  public shared.SharedObject duplicate() {
    TomTerm_Number clone = new TomTerm_Number();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_Number(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("Number(<int>)");
  }

  static public TomTerm fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomTerm tmp = getStaticTomSignatureFactory().makeTomTerm_Number((Integer) children.get(0));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  public aterm.ATerm toTerm() {
    if(term == null) {
      java.util.List args = new java.util.LinkedList();
      args.add(new Integer(((aterm.ATermInt) getArgument(0)).getInt()));
      setTerm(getFactory().make(getPattern(), args));
    }
    return term;
  }

  public boolean isNumber()
  {
    return true;
  }

  public boolean hasInteger()
  {
    return true;
  }

  public Integer getInteger()
  {
   return new Integer(((aterm.ATermInt) this.getArgument(index_integer)).getInt());
  }

  public TomTerm setInteger(Integer _integer)
  {
    return (TomTerm) super.setArgument(getFactory().makeInt(_integer.intValue()), index_integer);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermInt)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_Number should have type int");
        }
        break;
      default: throw new RuntimeException("TomTerm_Number does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
