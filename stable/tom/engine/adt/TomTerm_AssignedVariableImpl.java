package jtom.adt;

abstract public class TomTerm_AssignedVariableImpl
extends TomTerm
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomTerm_AssignedVariableImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_varName = 0;
  private static int index_source = 1;
  private static int index_nbUse = 2;
  private static int index_usedInDoWhile = 3;
  private static int index_removable = 4;
  public shared.SharedObject duplicate() {
    TomTerm_AssignedVariable clone = new TomTerm_AssignedVariable(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_AssignedVariable) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_AssignedVariable(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isAssignedVariable()
  {
    return true;
  }

  public boolean hasVarName()
  {
    return true;
  }

  public boolean hasSource()
  {
    return true;
  }

  public boolean hasNbUse()
  {
    return true;
  }

  public boolean hasUsedInDoWhile()
  {
    return true;
  }

  public boolean hasRemovable()
  {
    return true;
  }

  public String getVarName()
  {
   return ((aterm.ATermAppl) this.getArgument(index_varName)).getAFun().getName();
  }

  public TomTerm setVarName(String _varName)
  {
    return (TomTerm) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_varName, 0, true)), index_varName);
  }

  public Expression getSource()
  {
    return (Expression) this.getArgument(index_source) ;
  }

  public TomTerm setSource(Expression _source)
  {
    return (TomTerm) super.setArgument(_source, index_source);
  }

  public Integer getNbUse()
  {
   return new Integer(((aterm.ATermInt) this.getArgument(index_nbUse)).getInt());
  }

  public TomTerm setNbUse(Integer _nbUse)
  {
    return (TomTerm) super.setArgument(getFactory().makeInt(_nbUse.intValue()), index_nbUse);
  }

  public Expression getUsedInDoWhile()
  {
    return (Expression) this.getArgument(index_usedInDoWhile) ;
  }

  public TomTerm setUsedInDoWhile(Expression _usedInDoWhile)
  {
    return (TomTerm) super.setArgument(_usedInDoWhile, index_usedInDoWhile);
  }

  public Expression getRemovable()
  {
    return (Expression) this.getArgument(index_removable) ;
  }

  public TomTerm setRemovable(Expression _removable)
  {
    return (TomTerm) super.setArgument(_removable, index_removable);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_AssignedVariable should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof Expression)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_AssignedVariable should have type Expression");
        }
        break;
      case 2:
        if (! (arg instanceof aterm.ATermInt)) { 
          throw new RuntimeException("Argument 2 of a TomTerm_AssignedVariable should have type int");
        }
        break;
      case 3:
        if (! (arg instanceof Expression)) { 
          throw new RuntimeException("Argument 3 of a TomTerm_AssignedVariable should have type Expression");
        }
        break;
      case 4:
        if (! (arg instanceof Expression)) { 
          throw new RuntimeException("Argument 4 of a TomTerm_AssignedVariable should have type Expression");
        }
        break;
      default: throw new RuntimeException("TomTerm_AssignedVariable does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
