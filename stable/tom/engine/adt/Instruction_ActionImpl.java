package jtom.adt;

abstract public class Instruction_ActionImpl
extends Instruction
{
  Instruction_ActionImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_instList = 0;
  public shared.SharedObject duplicate() {
    Instruction_Action clone = new Instruction_Action(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Instruction_Action) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeInstruction_Action(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isAction()
  {
    return true;
  }

  public boolean hasInstList()
  {
    return true;
  }

  public TomList getInstList()
  {
    return (TomList) this.getArgument(index_instList) ;
  }

  public Instruction setInstList(TomList _instList)
  {
    return (Instruction) super.setArgument(_instList, index_instList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 0 of a Instruction_Action should have type TomList");
        }
        break;
      default: throw new RuntimeException("Instruction_Action does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
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
