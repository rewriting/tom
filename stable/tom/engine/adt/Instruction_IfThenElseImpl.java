package jtom.adt;

abstract public class Instruction_IfThenElseImpl
extends Instruction
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected Instruction_IfThenElseImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_condition = 0;
  private static int index_succesList = 1;
  private static int index_failureList = 2;
  public shared.SharedObject duplicate() {
    Instruction_IfThenElse clone = new Instruction_IfThenElse(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Instruction_IfThenElse) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeInstruction_IfThenElse(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
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

  public Instruction setCondition(Expression _condition)
  {
    return (Instruction) super.setArgument(_condition, index_condition);
  }

  public TomList getSuccesList()
  {
    return (TomList) this.getArgument(index_succesList) ;
  }

  public Instruction setSuccesList(TomList _succesList)
  {
    return (Instruction) super.setArgument(_succesList, index_succesList);
  }

  public TomList getFailureList()
  {
    return (TomList) this.getArgument(index_failureList) ;
  }

  public Instruction setFailureList(TomList _failureList)
  {
    return (Instruction) super.setArgument(_failureList, index_failureList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof Expression)) { 
          throw new RuntimeException("Argument 0 of a Instruction_IfThenElse should have type Expression");
        }
        break;
      case 1:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 1 of a Instruction_IfThenElse should have type TomList");
        }
        break;
      case 2:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 2 of a Instruction_IfThenElse should have type TomList");
        }
        break;
      default: throw new RuntimeException("Instruction_IfThenElse does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
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
