package jtom.adt;


abstract public class TomTermImpl extends TomSignatureConstructor
{
  protected TomTermImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(TomTerm peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortTomTerm()  {
    return true;
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

  public boolean isInstructionToTomTerm()
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

  public boolean isFunctionCall()
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

  public boolean isAppl()
  {
    return false;
  }

  public boolean isXMLAppl()
  {
    return false;
  }

  public boolean isBackQuoteAppl()
  {
    return false;
  }

  public boolean isDoubleBackQuote()
  {
    return false;
  }

  public boolean isComposite()
  {
    return false;
  }

  public boolean isRecordAppl()
  {
    return false;
  }

  public boolean isPairSlotAppl()
  {
    return false;
  }

  public boolean isMatch()
  {
    return false;
  }

  public boolean isMatchingCondition()
  {
    return false;
  }

  public boolean isEqualityCondition()
  {
    return false;
  }

  public boolean isRuleSet()
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

  public boolean isTLVar()
  {
    return false;
  }

  public boolean isDeclaration()
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

  public boolean isUnamedVariableStar()
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

  public boolean isCompiledMatch()
  {
    return false;
  }

  public boolean isCompiledPattern()
  {
    return false;
  }

  public boolean isAssignedVariable()
  {
    return false;
  }

  public boolean isAutomata()
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

  public boolean hasAstInstruction()
  {
    return false;
  }

  public boolean hasTomList()
  {
    return false;
  }

  public boolean hasKid1()
  {
    return false;
  }

  public boolean hasArgs()
  {
    return false;
  }

  public boolean hasSubjectListAST()
  {
    return false;
  }

  public boolean hasOption()
  {
    return false;
  }

  public boolean hasAttrList()
  {
    return false;
  }

  public boolean hasChildList()
  {
    return false;
  }

  public boolean hasArg()
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

  public boolean hasSubjectList()
  {
    return false;
  }

  public boolean hasPatternList()
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

  public boolean hasRuleList()
  {
    return false;
  }

  public boolean hasOrgTrack()
  {
    return false;
  }

  public boolean hasTomTerm()
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

  public boolean hasInstList()
  {
    return false;
  }

  public boolean hasVarName()
  {
    return false;
  }

  public boolean hasSource()
  {
    return false;
  }

  public boolean hasNbUse()
  {
    return false;
  }

  public boolean hasUsedInDoWhile()
  {
    return false;
  }

  public boolean hasRemovable()
  {
    return false;
  }

  public boolean hasNumberList()
  {
    return false;
  }

  public TargetLanguage getTl()
  {
     throw new UnsupportedOperationException("This TomTerm has no Tl");
  }

  public TomTerm setTl(TargetLanguage _tl)
  {
     throw new IllegalArgumentException("Illegal argument: " + _tl);
  }

  public TomType getAstType()
  {
     throw new UnsupportedOperationException("This TomTerm has no AstType");
  }

  public TomTerm setAstType(TomType _astType)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astType);
  }

  public TomName getAstName()
  {
     throw new UnsupportedOperationException("This TomTerm has no AstName");
  }

  public TomTerm setAstName(TomName _astName)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astName);
  }

  public TomSymbol getAstSymbol()
  {
     throw new UnsupportedOperationException("This TomTerm has no AstSymbol");
  }

  public TomTerm setAstSymbol(TomSymbol _astSymbol)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astSymbol);
  }

  public Declaration getAstDeclaration()
  {
     throw new UnsupportedOperationException("This TomTerm has no AstDeclaration");
  }

  public TomTerm setAstDeclaration(Declaration _astDeclaration)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astDeclaration);
  }

  public Option getAstOption()
  {
     throw new UnsupportedOperationException("This TomTerm has no AstOption");
  }

  public TomTerm setAstOption(Option _astOption)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astOption);
  }

  public Expression getAstExpression()
  {
     throw new UnsupportedOperationException("This TomTerm has no AstExpression");
  }

  public TomTerm setAstExpression(Expression _astExpression)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astExpression);
  }

  public Instruction getAstInstruction()
  {
     throw new UnsupportedOperationException("This TomTerm has no AstInstruction");
  }

  public TomTerm setAstInstruction(Instruction _astInstruction)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astInstruction);
  }

  public TomList getTomList()
  {
     throw new UnsupportedOperationException("This TomTerm has no TomList");
  }

  public TomTerm setTomList(TomList _tomList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _tomList);
  }

  public TomTerm getKid1()
  {
     throw new UnsupportedOperationException("This TomTerm has no Kid1");
  }

  public TomTerm setKid1(TomTerm _kid1)
  {
     throw new IllegalArgumentException("Illegal argument: " + _kid1);
  }

  public TomList getArgs()
  {
     throw new UnsupportedOperationException("This TomTerm has no Args");
  }

  public TomTerm setArgs(TomList _args)
  {
     throw new IllegalArgumentException("Illegal argument: " + _args);
  }

  public TomTerm getSubjectListAST()
  {
     throw new UnsupportedOperationException("This TomTerm has no SubjectListAST");
  }

  public TomTerm setSubjectListAST(TomTerm _subjectListAST)
  {
     throw new IllegalArgumentException("Illegal argument: " + _subjectListAST);
  }

  public Option getOption()
  {
     throw new UnsupportedOperationException("This TomTerm has no Option");
  }

  public TomTerm setOption(Option _option)
  {
     throw new IllegalArgumentException("Illegal argument: " + _option);
  }

  public TomList getAttrList()
  {
     throw new UnsupportedOperationException("This TomTerm has no AttrList");
  }

  public TomTerm setAttrList(TomList _attrList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _attrList);
  }

  public TomList getChildList()
  {
     throw new UnsupportedOperationException("This TomTerm has no ChildList");
  }

  public TomTerm setChildList(TomList _childList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _childList);
  }

  public TomTerm getArg()
  {
     throw new UnsupportedOperationException("This TomTerm has no Arg");
  }

  public TomTerm setArg(TomTerm _arg)
  {
     throw new IllegalArgumentException("Illegal argument: " + _arg);
  }

  public TomName getSlotName()
  {
     throw new UnsupportedOperationException("This TomTerm has no SlotName");
  }

  public TomTerm setSlotName(TomName _slotName)
  {
     throw new IllegalArgumentException("Illegal argument: " + _slotName);
  }

  public TomTerm getAppl()
  {
     throw new UnsupportedOperationException("This TomTerm has no Appl");
  }

  public TomTerm setAppl(TomTerm _appl)
  {
     throw new IllegalArgumentException("Illegal argument: " + _appl);
  }

  public TomTerm getSubjectList()
  {
     throw new UnsupportedOperationException("This TomTerm has no SubjectList");
  }

  public TomTerm setSubjectList(TomTerm _subjectList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _subjectList);
  }

  public TomTerm getPatternList()
  {
     throw new UnsupportedOperationException("This TomTerm has no PatternList");
  }

  public TomTerm setPatternList(TomTerm _patternList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _patternList);
  }

  public TomTerm getLhs()
  {
     throw new UnsupportedOperationException("This TomTerm has no Lhs");
  }

  public TomTerm setLhs(TomTerm _lhs)
  {
     throw new IllegalArgumentException("Illegal argument: " + _lhs);
  }

  public TomTerm getRhs()
  {
     throw new UnsupportedOperationException("This TomTerm has no Rhs");
  }

  public TomTerm setRhs(TomTerm _rhs)
  {
     throw new IllegalArgumentException("Illegal argument: " + _rhs);
  }

  public TomRuleList getRuleList()
  {
     throw new UnsupportedOperationException("This TomTerm has no RuleList");
  }

  public TomTerm setRuleList(TomRuleList _ruleList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _ruleList);
  }

  public Option getOrgTrack()
  {
     throw new UnsupportedOperationException("This TomTerm has no OrgTrack");
  }

  public TomTerm setOrgTrack(Option _orgTrack)
  {
     throw new IllegalArgumentException("Illegal argument: " + _orgTrack);
  }

  public TomTerm getTomTerm()
  {
     throw new UnsupportedOperationException("This TomTerm has no TomTerm");
  }

  public TomTerm setTomTerm(TomTerm _tomTerm)
  {
     throw new IllegalArgumentException("Illegal argument: " + _tomTerm);
  }

  public TomTerm getTermList()
  {
     throw new UnsupportedOperationException("This TomTerm has no TermList");
  }

  public TomTerm setTermList(TomTerm _termList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _termList);
  }

  public TomTerm getTom()
  {
     throw new UnsupportedOperationException("This TomTerm has no Tom");
  }

  public TomTerm setTom(TomTerm _tom)
  {
     throw new IllegalArgumentException("Illegal argument: " + _tom);
  }

  public String getStrName()
  {
     throw new UnsupportedOperationException("This TomTerm has no StrName");
  }

  public TomTerm setStrName(String _strName)
  {
     throw new IllegalArgumentException("Illegal argument: " + _strName);
  }

  public TomList getDecls()
  {
     throw new UnsupportedOperationException("This TomTerm has no Decls");
  }

  public TomTerm setDecls(TomList _decls)
  {
     throw new IllegalArgumentException("Illegal argument: " + _decls);
  }

  public TomList getAutomataList()
  {
     throw new UnsupportedOperationException("This TomTerm has no AutomataList");
  }

  public TomTerm setAutomataList(TomList _automataList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _automataList);
  }

  public TomList getInstList()
  {
     throw new UnsupportedOperationException("This TomTerm has no InstList");
  }

  public TomTerm setInstList(TomList _instList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _instList);
  }

  public String getVarName()
  {
     throw new UnsupportedOperationException("This TomTerm has no VarName");
  }

  public TomTerm setVarName(String _varName)
  {
     throw new IllegalArgumentException("Illegal argument: " + _varName);
  }

  public Expression getSource()
  {
     throw new UnsupportedOperationException("This TomTerm has no Source");
  }

  public TomTerm setSource(Expression _source)
  {
     throw new IllegalArgumentException("Illegal argument: " + _source);
  }

  public int getNbUse()
  {
     throw new UnsupportedOperationException("This TomTerm has no NbUse");
  }

  public TomTerm setNbUse(int _nbUse)
  {
     throw new IllegalArgumentException("Illegal argument: " + _nbUse);
  }

  public Expression getUsedInDoWhile()
  {
     throw new UnsupportedOperationException("This TomTerm has no UsedInDoWhile");
  }

  public TomTerm setUsedInDoWhile(Expression _usedInDoWhile)
  {
     throw new IllegalArgumentException("Illegal argument: " + _usedInDoWhile);
  }

  public Expression getRemovable()
  {
     throw new UnsupportedOperationException("This TomTerm has no Removable");
  }

  public TomTerm setRemovable(Expression _removable)
  {
     throw new IllegalArgumentException("Illegal argument: " + _removable);
  }

  public TomNumberList getNumberList()
  {
     throw new UnsupportedOperationException("This TomTerm has no NumberList");
  }

  public TomTerm setNumberList(TomNumberList _numberList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _numberList);
  }

}

