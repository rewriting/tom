package jtom.adt;

abstract public class Instruction_NamedBlockImpl
extends Instruction
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected Instruction_NamedBlockImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_blockName = 0;
  private static int index_instList = 1;
  public shared.SharedObject duplicate() {
    Instruction_NamedBlock clone = new Instruction_NamedBlock(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Instruction_NamedBlock) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeInstruction_NamedBlock(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isNamedBlock()
  {
    return true;
  }

  public boolean hasBlockName()
  {
    return true;
  }

  public boolean hasInstList()
  {
    return true;
  }

  public String getBlockName()
  {
   return ((aterm.ATermAppl) this.getArgument(index_blockName)).getAFun().getName();
  }

  public Instruction setBlockName(String _blockName)
  {
    return (Instruction) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_blockName, 0, true)), index_blockName);
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
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a Instruction_NamedBlock should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 1 of a Instruction_NamedBlock should have type TomList");
        }
        break;
      default: throw new RuntimeException("Instruction_NamedBlock does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
