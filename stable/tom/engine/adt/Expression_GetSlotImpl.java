package jtom.adt;

abstract public class Expression_GetSlotImpl
extends Expression
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_astName = 0;
  private static int index_slotNameString = 1;
  private static int index_term = 2;
  public shared.SharedObject duplicate() {
    Expression_GetSlot clone = new Expression_GetSlot();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeExpression_GetSlot(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("GetSlot(<term>,<str>,<term>)");
  }

  static public Expression fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Expression tmp = getStaticTomSignatureFactory().makeExpression_GetSlot(TomName.fromTerm( (aterm.ATerm) children.get(0)), (String) children.get(1), TomTerm.fromTerm( (aterm.ATerm) children.get(2)));
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
      args.add(((TomSignatureConstructor) getArgument(0)).toTerm());
      args.add(((aterm.ATermAppl) getArgument(1)).getAFun().getName());
      args.add(((TomSignatureConstructor) getArgument(2)).toTerm());
      setTerm(getFactory().make(getPattern(), args));
    }
    return term;
  }

  public boolean isGetSlot()
  {
    return true;
  }

  public boolean hasAstName()
  {
    return true;
  }

  public boolean hasSlotNameString()
  {
    return true;
  }

  public boolean hasTerm()
  {
    return true;
  }

  public TomName getAstName()
  {
    return (TomName) this.getArgument(index_astName) ;
  }

  public Expression setAstName(TomName _astName)
  {
    return (Expression) super.setArgument(_astName, index_astName);
  }

  public String getSlotNameString()
  {
   return ((aterm.ATermAppl) this.getArgument(index_slotNameString)).getAFun().getName();
  }

  public Expression setSlotNameString(String _slotNameString)
  {
    return (Expression) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_slotNameString, 0, true)), index_slotNameString);
  }

  public TomTerm getTerm()
  {
    return (TomTerm) this.getArgument(index_term) ;
  }

  public Expression setTerm(TomTerm _term)
  {
    return (Expression) super.setArgument(_term, index_term);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a Expression_GetSlot should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 1 of a Expression_GetSlot should have type str");
        }
        break;
      case 2:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 2 of a Expression_GetSlot should have type TomTerm");
        }
        break;
      default: throw new RuntimeException("Expression_GetSlot does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
