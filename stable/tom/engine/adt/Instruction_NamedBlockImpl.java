package jtom.adt;

abstract public class Instruction_NamedBlockImpl
extends Instruction
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_blockName = 0;
  private static int index_instList = 1;
  public shared.SharedObject duplicate() {
    Instruction_NamedBlock clone = new Instruction_NamedBlock();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeInstruction_NamedBlock(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("NamedBlock(<str>,<term>)");
  }

  static public Instruction fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Instruction tmp = getStaticTomSignatureFactory().makeInstruction_NamedBlock((String) children.get(0), TomList.fromTerm( (aterm.ATerm) children.get(1)));
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
      args.add(((TomSignatureConstructor) getArgument(1)).toTerm());
      setTerm(getFactory().make(getPattern(), args));
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
