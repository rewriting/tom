package jtom.adt;

abstract public class TomType_TomTypeImpl
extends TomType
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_string = 0;
  public shared.SharedObject duplicate() {
    TomType_TomType clone = new TomType_TomType();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomType_TomType(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("TomType(<str>)");
  }

  static public TomType fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomType tmp = getStaticTomSignatureFactory().makeTomType_TomType((String) children.get(0));
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
      args.add(((aterm.ATermAppl) getArgument(0)).getAFun().getName());
      setTerm(getFactory().make(getPattern(), args));
    }
    return term;
  }

  public boolean isTomType()
  {
    return true;
  }

  public boolean hasString()
  {
    return true;
  }

  public String getString()
  {
   return ((aterm.ATermAppl) this.getArgument(index_string)).getAFun().getName();
  }

  public TomType setString(String _string)
  {
    return (TomType) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_string, 0, true)), index_string);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomType_TomType should have type str");
        }
        break;
      default: throw new RuntimeException("TomType_TomType does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
