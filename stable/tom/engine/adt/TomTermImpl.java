package jtom.adt;

import java.io.InputStream;
import java.io.IOException;
import aterm.*;


abstract public class TomTermImpl extends TomSignatureConstructor
{
  static TomTerm fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }

  static TomTerm fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }

  public boolean isEqual(TomTerm peer)
  {
    return term.isEqual(peer.toTerm());
  }

  public static TomTerm fromTerm(aterm.ATerm trm)
  {
    TomTerm tmp;
    if ((tmp = TomTerm_TargetLanguageToTomTerm.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_TomTypeToTomTerm.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_TomNameToTomTerm.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_TomSymbolToTomTerm.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_DeclarationToTomTerm.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_OptionToTomTerm.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_ExpressionToTomTerm.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Tom.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_TomInclude.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_MakeTerm.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_BackQuoteTerm.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_LocalVariable.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_EndLocalVariable.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_BuildVariable.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_BuildVariableStar.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_BuildTerm.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_BuildList.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_BuildArray.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_FunctionCall.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Appl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_RecordAppl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Pair.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_SlotName.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Match.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_MakeFunctionBegin.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_MakeFunctionEnd.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_RuleSet.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_RewriteRule.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_SubjectList.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_PatternList.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_TermList.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Term.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_PatternAction.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_GLVar.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Variable.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_VariableStar.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Placeholder.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_UnamedVariable.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_CompiledMatch.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Automata.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_IfThenElse.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_DoWhile.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Assign.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Declaration.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Begin.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_End.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_MatchNumber.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_PatternNumber.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_ListNumber.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_IndexNumber.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_AbsVar.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_RenamedVar.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_RuleVar.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Number.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Increment.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Action.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_ExitAction.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Return.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_OpenBlock.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_CloseBlock.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_NamedBlock.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_Line.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomTerm_DotTerm.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomTerm: " + trm);
  }


  public boolean isTargetLanguageToTomTerm()
  {
    return false;
  }

  public boolean isTomTypeToTomTerm()
  {
    return false;
  }

  public boolean isTomNameToTomTerm()
  {
    return false;
  }

  public boolean isTomSymbolToTomTerm()
  {
    return false;
  }

  public boolean isDeclarationToTomTerm()
  {
    return false;
  }

  public boolean isOptionToTomTerm()
  {
    return false;
  }

  public boolean isExpressionToTomTerm()
  {
    return false;
  }

  public boolean isTom()
  {
    return false;
  }

  public boolean isTomInclude()
  {
    return false;
  }

  public boolean isMakeTerm()
  {
    return false;
  }

  public boolean isBackQuoteTerm()
  {
    return false;
  }

  public boolean isLocalVariable()
  {
    return false;
  }

  public boolean isEndLocalVariable()
  {
    return false;
  }

  public boolean isBuildVariable()
  {
    return false;
  }

  public boolean isBuildVariableStar()
  {
    return false;
  }

  public boolean isBuildTerm()
  {
    return false;
  }

  public boolean isBuildList()
  {
    return false;
  }

  public boolean isBuildArray()
  {
    return false;
  }

  public boolean isFunctionCall()
  {
    return false;
  }

  public boolean isAppl()
  {
    return false;
  }

  public boolean isRecordAppl()
  {
    return false;
  }

  public boolean isPair()
  {
    return false;
  }

  public boolean isSlotName()
  {
    return false;
  }

  public boolean isMatch()
  {
    return false;
  }

  public boolean isMakeFunctionBegin()
  {
    return false;
  }

  public boolean isMakeFunctionEnd()
  {
    return false;
  }

  public boolean isRuleSet()
  {
    return false;
  }

  public boolean isRewriteRule()
  {
    return false;
  }

  public boolean isSubjectList()
  {
    return false;
  }

  public boolean isPatternList()
  {
    return false;
  }

  public boolean isTermList()
  {
    return false;
  }

  public boolean isTerm()
  {
    return false;
  }

  public boolean isPatternAction()
  {
    return false;
  }

  public boolean isGLVar()
  {
    return false;
  }

  public boolean isVariable()
  {
    return false;
  }

  public boolean isVariableStar()
  {
    return false;
  }

  public boolean isPlaceholder()
  {
    return false;
  }

  public boolean isUnamedVariable()
  {
    return false;
  }

  public boolean isCompiledMatch()
  {
    return false;
  }

  public boolean isAutomata()
  {
    return false;
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

  public boolean isDeclaration()
  {
    return false;
  }

  public boolean isBegin()
  {
    return false;
  }

  public boolean isEnd()
  {
    return false;
  }

  public boolean isMatchNumber()
  {
    return false;
  }

  public boolean isPatternNumber()
  {
    return false;
  }

  public boolean isListNumber()
  {
    return false;
  }

  public boolean isIndexNumber()
  {
    return false;
  }

  public boolean isAbsVar()
  {
    return false;
  }

  public boolean isRenamedVar()
  {
    return false;
  }

  public boolean isRuleVar()
  {
    return false;
  }

  public boolean isNumber()
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

  public boolean isLine()
  {
    return false;
  }

  public boolean isDotTerm()
  {
    return false;
  }

  public boolean hasTl()
  {
    return false;
  }

  public boolean hasAstType()
  {
    return false;
  }

  public boolean hasAstName()
  {
    return false;
  }

  public boolean hasAstSymbol()
  {
    return false;
  }

  public boolean hasAstDeclaration()
  {
    return false;
  }

  public boolean hasAstOption()
  {
    return false;
  }

  public boolean hasAstExpression()
  {
    return false;
  }

  public boolean hasList()
  {
    return false;
  }

  public boolean hasKid1()
  {
    return false;
  }

  public boolean hasTerm()
  {
    return false;
  }

  public boolean hasArgs()
  {
    return false;
  }

  public boolean hasOption()
  {
    return false;
  }

  public boolean hasSlotName()
  {
    return false;
  }

  public boolean hasAppl()
  {
    return false;
  }

  public boolean hasString()
  {
    return false;
  }

  public boolean hasKid2()
  {
    return false;
  }

  public boolean hasSubjectListAST()
  {
    return false;
  }

  public boolean hasLhs()
  {
    return false;
  }

  public boolean hasRhs()
  {
    return false;
  }

  public boolean hasTermList()
  {
    return false;
  }

  public boolean hasTom()
  {
    return false;
  }

  public boolean hasStrName()
  {
    return false;
  }

  public boolean hasDecls()
  {
    return false;
  }

  public boolean hasAutomataList()
  {
    return false;
  }

  public boolean hasNumberList()
  {
    return false;
  }

  public boolean hasInstList()
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

  public boolean hasSource()
  {
    return false;
  }

  public boolean hasNumber()
  {
    return false;
  }

  public boolean hasInteger()
  {
    return false;
  }

  public boolean hasBlockName()
  {
    return false;
  }


  public TargetLanguage getTl()
  {
     throw new RuntimeException("This TomTerm has no Tl");
  }

  public TomTerm setTl(TargetLanguage _tl)
  {
     throw new RuntimeException("This TomTerm has no Tl");
  }

  public TomType getAstType()
  {
     throw new RuntimeException("This TomTerm has no AstType");
  }

  public TomTerm setAstType(TomType _astType)
  {
     throw new RuntimeException("This TomTerm has no AstType");
  }

  public TomName getAstName()
  {
     throw new RuntimeException("This TomTerm has no AstName");
  }

  public TomTerm setAstName(TomName _astName)
  {
     throw new RuntimeException("This TomTerm has no AstName");
  }

  public TomSymbol getAstSymbol()
  {
     throw new RuntimeException("This TomTerm has no AstSymbol");
  }

  public TomTerm setAstSymbol(TomSymbol _astSymbol)
  {
     throw new RuntimeException("This TomTerm has no AstSymbol");
  }

  public Declaration getAstDeclaration()
  {
     throw new RuntimeException("This TomTerm has no AstDeclaration");
  }

  public TomTerm setAstDeclaration(Declaration _astDeclaration)
  {
     throw new RuntimeException("This TomTerm has no AstDeclaration");
  }

  public Option getAstOption()
  {
     throw new RuntimeException("This TomTerm has no AstOption");
  }

  public TomTerm setAstOption(Option _astOption)
  {
     throw new RuntimeException("This TomTerm has no AstOption");
  }

  public Expression getAstExpression()
  {
     throw new RuntimeException("This TomTerm has no AstExpression");
  }

  public TomTerm setAstExpression(Expression _astExpression)
  {
     throw new RuntimeException("This TomTerm has no AstExpression");
  }

  public TomList getList()
  {
     throw new RuntimeException("This TomTerm has no List");
  }

  public TomTerm setList(TomList _list)
  {
     throw new RuntimeException("This TomTerm has no List");
  }

  public TomTerm getKid1()
  {
     throw new RuntimeException("This TomTerm has no Kid1");
  }

  public TomTerm setKid1(TomTerm _kid1)
  {
     throw new RuntimeException("This TomTerm has no Kid1");
  }

  public TomTerm getTerm()
  {
     throw new RuntimeException("This TomTerm has no Term");
  }

  public TomTerm setTerm(TomTerm _term)
  {
     throw new RuntimeException("This TomTerm has no Term");
  }

  public TomList getArgs()
  {
     throw new RuntimeException("This TomTerm has no Args");
  }

  public TomTerm setArgs(TomList _args)
  {
     throw new RuntimeException("This TomTerm has no Args");
  }

  public Option getOption()
  {
     throw new RuntimeException("This TomTerm has no Option");
  }

  public TomTerm setOption(Option _option)
  {
     throw new RuntimeException("This TomTerm has no Option");
  }

  public TomTerm getSlotName()
  {
     throw new RuntimeException("This TomTerm has no SlotName");
  }

  public TomTerm setSlotName(TomTerm _slotName)
  {
     throw new RuntimeException("This TomTerm has no SlotName");
  }

  public TomTerm getAppl()
  {
     throw new RuntimeException("This TomTerm has no Appl");
  }

  public TomTerm setAppl(TomTerm _appl)
  {
     throw new RuntimeException("This TomTerm has no Appl");
  }

  public String getString()
  {
     throw new RuntimeException("This TomTerm has no String");
  }

  public TomTerm setString(String _string)
  {
     throw new RuntimeException("This TomTerm has no String");
  }

  public TomTerm getKid2()
  {
     throw new RuntimeException("This TomTerm has no Kid2");
  }

  public TomTerm setKid2(TomTerm _kid2)
  {
     throw new RuntimeException("This TomTerm has no Kid2");
  }

  public TomTerm getSubjectListAST()
  {
     throw new RuntimeException("This TomTerm has no SubjectListAST");
  }

  public TomTerm setSubjectListAST(TomTerm _subjectListAST)
  {
     throw new RuntimeException("This TomTerm has no SubjectListAST");
  }

  public TomTerm getLhs()
  {
     throw new RuntimeException("This TomTerm has no Lhs");
  }

  public TomTerm setLhs(TomTerm _lhs)
  {
     throw new RuntimeException("This TomTerm has no Lhs");
  }

  public TomTerm getRhs()
  {
     throw new RuntimeException("This TomTerm has no Rhs");
  }

  public TomTerm setRhs(TomTerm _rhs)
  {
     throw new RuntimeException("This TomTerm has no Rhs");
  }

  public TomTerm getTermList()
  {
     throw new RuntimeException("This TomTerm has no TermList");
  }

  public TomTerm setTermList(TomTerm _termList)
  {
     throw new RuntimeException("This TomTerm has no TermList");
  }

  public TomTerm getTom()
  {
     throw new RuntimeException("This TomTerm has no Tom");
  }

  public TomTerm setTom(TomTerm _tom)
  {
     throw new RuntimeException("This TomTerm has no Tom");
  }

  public String getStrName()
  {
     throw new RuntimeException("This TomTerm has no StrName");
  }

  public TomTerm setStrName(String _strName)
  {
     throw new RuntimeException("This TomTerm has no StrName");
  }

  public TomList getDecls()
  {
     throw new RuntimeException("This TomTerm has no Decls");
  }

  public TomTerm setDecls(TomList _decls)
  {
     throw new RuntimeException("This TomTerm has no Decls");
  }

  public TomList getAutomataList()
  {
     throw new RuntimeException("This TomTerm has no AutomataList");
  }

  public TomTerm setAutomataList(TomList _automataList)
  {
     throw new RuntimeException("This TomTerm has no AutomataList");
  }

  public TomList getNumberList()
  {
     throw new RuntimeException("This TomTerm has no NumberList");
  }

  public TomTerm setNumberList(TomList _numberList)
  {
     throw new RuntimeException("This TomTerm has no NumberList");
  }

  public TomList getInstList()
  {
     throw new RuntimeException("This TomTerm has no InstList");
  }

  public TomTerm setInstList(TomList _instList)
  {
     throw new RuntimeException("This TomTerm has no InstList");
  }

  public Expression getCondition()
  {
     throw new RuntimeException("This TomTerm has no Condition");
  }

  public TomTerm setCondition(Expression _condition)
  {
     throw new RuntimeException("This TomTerm has no Condition");
  }

  public TomList getSuccesList()
  {
     throw new RuntimeException("This TomTerm has no SuccesList");
  }

  public TomTerm setSuccesList(TomList _succesList)
  {
     throw new RuntimeException("This TomTerm has no SuccesList");
  }

  public TomList getFailureList()
  {
     throw new RuntimeException("This TomTerm has no FailureList");
  }

  public TomTerm setFailureList(TomList _failureList)
  {
     throw new RuntimeException("This TomTerm has no FailureList");
  }

  public Expression getSource()
  {
     throw new RuntimeException("This TomTerm has no Source");
  }

  public TomTerm setSource(Expression _source)
  {
     throw new RuntimeException("This TomTerm has no Source");
  }

  public TomTerm getNumber()
  {
     throw new RuntimeException("This TomTerm has no Number");
  }

  public TomTerm setNumber(TomTerm _number)
  {
     throw new RuntimeException("This TomTerm has no Number");
  }

  public Integer getInteger()
  {
     throw new RuntimeException("This TomTerm has no Integer");
  }

  public TomTerm setInteger(Integer _integer)
  {
     throw new RuntimeException("This TomTerm has no Integer");
  }

  public String getBlockName()
  {
     throw new RuntimeException("This TomTerm has no BlockName");
  }

  public TomTerm setBlockName(String _blockName)
  {
     throw new RuntimeException("This TomTerm has no BlockName");
  }



}

