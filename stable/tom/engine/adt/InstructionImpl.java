package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class InstructionImpl extends TomSignatureConstructor
{
  public static Instruction fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  public static Instruction fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(Instruction peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static Instruction fromTerm(aterm.ATerm trm)
  {
    Instruction tmp;
    if ((tmp = Instruction_IfThenElse.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Instruction_DoWhile.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Instruction_Assign.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Instruction_AssignMatchSubject.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Instruction_Increment.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Instruction_Action.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Instruction_ExitAction.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Instruction_Return.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Instruction_OpenBlock.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Instruction_CloseBlock.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Instruction_NamedBlock.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Instruction: " + trm);
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
     throw new RuntimeException("This Instruction has no Condition");
  }

  public Instruction setCondition(Expression _condition)
  {
     throw new RuntimeException("This Instruction has no Condition");
  }

  public TomList getSuccesList()
  {
     throw new RuntimeException("This Instruction has no SuccesList");
  }

  public Instruction setSuccesList(TomList _succesList)
  {
     throw new RuntimeException("This Instruction has no SuccesList");
  }

  public TomList getFailureList()
  {
     throw new RuntimeException("This Instruction has no FailureList");
  }

  public Instruction setFailureList(TomList _failureList)
  {
     throw new RuntimeException("This Instruction has no FailureList");
  }

  public TomList getInstList()
  {
     throw new RuntimeException("This Instruction has no InstList");
  }

  public Instruction setInstList(TomList _instList)
  {
     throw new RuntimeException("This Instruction has no InstList");
  }

  public TomTerm getKid1()
  {
     throw new RuntimeException("This Instruction has no Kid1");
  }

  public Instruction setKid1(TomTerm _kid1)
  {
     throw new RuntimeException("This Instruction has no Kid1");
  }

  public Expression getSource()
  {
     throw new RuntimeException("This Instruction has no Source");
  }

  public Instruction setSource(Expression _source)
  {
     throw new RuntimeException("This Instruction has no Source");
  }

  public TomList getNumberList()
  {
     throw new RuntimeException("This Instruction has no NumberList");
  }

  public Instruction setNumberList(TomList _numberList)
  {
     throw new RuntimeException("This Instruction has no NumberList");
  }

  public String getBlockName()
  {
     throw new RuntimeException("This Instruction has no BlockName");
  }

  public Instruction setBlockName(String _blockName)
  {
     throw new RuntimeException("This Instruction has no BlockName");
  }


}

