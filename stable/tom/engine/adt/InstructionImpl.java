package jtom.adt;


abstract public class InstructionImpl extends TomSignatureConstructor
{
  protected InstructionImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(Instruction peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortInstruction()  {
    return true;
  }

  public boolean isIfThenElse()
  {
    return false;
  }

  public boolean isDoWhile()
  {
    return false;
  }

  public boolean isAssign()
  {
    return false;
  }

  public boolean isAssignMatchSubject()
  {
    return false;
  }

  public boolean isIncrement()
  {
    return false;
  }

  public boolean isAction()
  {
    return false;
  }

  public boolean isExitAction()
  {
    return false;
  }

  public boolean isReturn()
  {
    return false;
  }

  public boolean isOpenBlock()
  {
    return false;
  }

  public boolean isCloseBlock()
  {
    return false;
  }

  public boolean isNamedBlock()
  {
    return false;
  }

  public boolean hasCondition()
  {
    return false;
  }

  public boolean hasSuccesList()
  {
    return false;
  }

  public boolean hasFailureList()
  {
    return false;
  }

  public boolean hasInstList()
  {
    return false;
  }

  public boolean hasKid1()
  {
    return false;
  }

  public boolean hasSource()
  {
    return false;
  }

  public boolean hasNumberList()
  {
    return false;
  }

  public boolean hasBlockName()
  {
    return false;
  }

  public Expression getCondition()
  {
     throw new UnsupportedOperationException("This Instruction has no Condition");
  }

  public Instruction setCondition(Expression _condition)
  {
     throw new IllegalArgumentException("Illegal argument: " + _condition);
  }

  public TomList getSuccesList()
  {
     throw new UnsupportedOperationException("This Instruction has no SuccesList");
  }

  public Instruction setSuccesList(TomList _succesList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _succesList);
  }

  public TomList getFailureList()
  {
     throw new UnsupportedOperationException("This Instruction has no FailureList");
  }

  public Instruction setFailureList(TomList _failureList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _failureList);
  }

  public TomList getInstList()
  {
     throw new UnsupportedOperationException("This Instruction has no InstList");
  }

  public Instruction setInstList(TomList _instList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _instList);
  }

  public TomTerm getKid1()
  {
     throw new UnsupportedOperationException("This Instruction has no Kid1");
  }

  public Instruction setKid1(TomTerm _kid1)
  {
     throw new IllegalArgumentException("Illegal argument: " + _kid1);
  }

  public Expression getSource()
  {
     throw new UnsupportedOperationException("This Instruction has no Source");
  }

  public Instruction setSource(Expression _source)
  {
     throw new IllegalArgumentException("Illegal argument: " + _source);
  }

  public TomNumberList getNumberList()
  {
     throw new UnsupportedOperationException("This Instruction has no NumberList");
  }

  public Instruction setNumberList(TomNumberList _numberList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _numberList);
  }

  public String getBlockName()
  {
     throw new UnsupportedOperationException("This Instruction has no BlockName");
  }

  public Instruction setBlockName(String _blockName)
  {
     throw new IllegalArgumentException("Illegal argument: " + _blockName);
  }

}

