package jtom.adt;

abstract public class Expression_GetSlotImpl
extends Expression
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected Expression_GetSlotImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_astName = 0;
  private static int index_slotNameString = 1;
  private static int index_variable = 2;
  public shared.SharedObject duplicate() {
    Expression_GetSlot clone = new Expression_GetSlot(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Expression_GetSlot) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeExpression_GetSlot(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
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

  public boolean hasVariable()
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

  public TomTerm getVariable()
  {
    return (TomTerm) this.getArgument(index_variable) ;
  }

  public Expression setVariable(TomTerm _variable)
  {
    return (Expression) super.setArgument(_variable, index_variable);
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
