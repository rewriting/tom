package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class ExpressionImpl extends TomSignatureConstructor
{
  ExpressionImpl(TomSignatureFactory factory) {
     super(factory);
  }
  public boolean isEqual(Expression peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortExpression()  {
    return true;
  }

  public boolean isTomTermToExpression()
  {
    return false;
  }

  public boolean isNot()
  {
    return false;
  }

  public boolean isAnd()
  {
    return false;
  }

  public boolean isTrueTL()
  {
    return false;
  }

  public boolean isFalseTL()
  {
    return false;
  }

  public boolean isIsEmptyList()
  {
    return false;
  }

  public boolean isIsEmptyArray()
  {
    return false;
  }

  public boolean isEqualFunctionSymbol()
  {
    return false;
  }

  public boolean isEqualTerm()
  {
    return false;
  }

  public boolean isGetSubterm()
  {
    return false;
  }

  public boolean isIsFsym()
  {
    return false;
  }

  public boolean isGetSlot()
  {
    return false;
  }

  public boolean isGetHead()
  {
    return false;
  }

  public boolean isGetTail()
  {
    return false;
  }

  public boolean isGetSize()
  {
    return false;
  }

  public boolean isGetElement()
  {
    return false;
  }

  public boolean isGetSliceList()
  {
    return false;
  }

  public boolean isGetSliceArray()
  {
    return false;
  }

  public boolean hasAstTerm()
  {
    return false;
  }

  public boolean hasArg()
  {
    return false;
  }

  public boolean hasArg1()
  {
    return false;
  }

  public boolean hasArg2()
  {
    return false;
  }

  public boolean hasKid1()
  {
    return false;
  }

  public boolean hasKid2()
  {
    return false;
  }

  public boolean hasVariable()
  {
    return false;
  }

  public boolean hasNumber()
  {
    return false;
  }

  public boolean hasAstName()
  {
    return false;
  }

  public boolean hasSlotNameString()
  {
    return false;
  }

  public boolean hasVariableBeginAST()
  {
    return false;
  }

  public boolean hasVariableEndAST()
  {
    return false;
  }

  public boolean hasSubjectListName()
  {
    return false;
  }

  public TomTerm getAstTerm()
  {
     throw new RuntimeException("This Expression has no AstTerm");
  }

  public Expression setAstTerm(TomTerm _astTerm)
  {
     throw new RuntimeException("This Expression has no AstTerm");
  }

  public Expression getArg()
  {
     throw new RuntimeException("This Expression has no Arg");
  }

  public Expression setArg(Expression _arg)
  {
     throw new RuntimeException("This Expression has no Arg");
  }

  public Expression getArg1()
  {
     throw new RuntimeException("This Expression has no Arg1");
  }

  public Expression setArg1(Expression _arg1)
  {
     throw new RuntimeException("This Expression has no Arg1");
  }

  public Expression getArg2()
  {
     throw new RuntimeException("This Expression has no Arg2");
  }

  public Expression setArg2(Expression _arg2)
  {
     throw new RuntimeException("This Expression has no Arg2");
  }

  public TomTerm getKid1()
  {
     throw new RuntimeException("This Expression has no Kid1");
  }

  public Expression setKid1(TomTerm _kid1)
  {
     throw new RuntimeException("This Expression has no Kid1");
  }

  public TomTerm getKid2()
  {
     throw new RuntimeException("This Expression has no Kid2");
  }

  public Expression setKid2(TomTerm _kid2)
  {
     throw new RuntimeException("This Expression has no Kid2");
  }

  public TomTerm getVariable()
  {
     throw new RuntimeException("This Expression has no Variable");
  }

  public Expression setVariable(TomTerm _variable)
  {
     throw new RuntimeException("This Expression has no Variable");
  }

  public TomNumber getNumber()
  {
     throw new RuntimeException("This Expression has no Number");
  }

  public Expression setNumber(TomNumber _number)
  {
     throw new RuntimeException("This Expression has no Number");
  }

  public TomName getAstName()
  {
     throw new RuntimeException("This Expression has no AstName");
  }

  public Expression setAstName(TomName _astName)
  {
     throw new RuntimeException("This Expression has no AstName");
  }

  public String getSlotNameString()
  {
     throw new RuntimeException("This Expression has no SlotNameString");
  }

  public Expression setSlotNameString(String _slotNameString)
  {
     throw new RuntimeException("This Expression has no SlotNameString");
  }

  public TomTerm getVariableBeginAST()
  {
     throw new RuntimeException("This Expression has no VariableBeginAST");
  }

  public Expression setVariableBeginAST(TomTerm _variableBeginAST)
  {
     throw new RuntimeException("This Expression has no VariableBeginAST");
  }

  public TomTerm getVariableEndAST()
  {
     throw new RuntimeException("This Expression has no VariableEndAST");
  }

  public Expression setVariableEndAST(TomTerm _variableEndAST)
  {
     throw new RuntimeException("This Expression has no VariableEndAST");
  }

  public TomTerm getSubjectListName()
  {
     throw new RuntimeException("This Expression has no SubjectListName");
  }

  public Expression setSubjectListName(TomTerm _subjectListName)
  {
     throw new RuntimeException("This Expression has no SubjectListName");
  }

}

