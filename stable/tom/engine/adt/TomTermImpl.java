package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

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
    return term.isEqual(peer.toTerm());
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

  public boolean isBackQuoteAppl()
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

  public boolean isDefaultPatternAction()
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

  public boolean isDefaultAutomata()
  {
    return false;
  }

  public boolean isMatchXML()
  {
    return false;
  }

  public boolean isBackQuoteXML()
  {
    return false;
  }

  public boolean isXMLTermToTomTerm()
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

  public boolean hasDebugName()
  {
    return false;
  }

  public boolean hasDocName()
  {
    return false;
  }

  public boolean hasXmlTerm()
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

  public Instruction getAstInstruction()
  {
     throw new RuntimeException("This TomTerm has no AstInstruction");
  }

  public TomTerm setAstInstruction(Instruction _astInstruction)
  {
     throw new RuntimeException("This TomTerm has no AstInstruction");
  }

  public TomList getTomList()
  {
     throw new RuntimeException("This TomTerm has no TomList");
  }

  public TomTerm setTomList(TomList _tomList)
  {
     throw new RuntimeException("This TomTerm has no TomList");
  }

  public TomTerm getKid1()
  {
     throw new RuntimeException("This TomTerm has no Kid1");
  }

  public TomTerm setKid1(TomTerm _kid1)
  {
     throw new RuntimeException("This TomTerm has no Kid1");
  }

  public TomList getArgs()
  {
     throw new RuntimeException("This TomTerm has no Args");
  }

  public TomTerm setArgs(TomList _args)
  {
     throw new RuntimeException("This TomTerm has no Args");
  }

  public TomTerm getSubjectListAST()
  {
     throw new RuntimeException("This TomTerm has no SubjectListAST");
  }

  public TomTerm setSubjectListAST(TomTerm _subjectListAST)
  {
     throw new RuntimeException("This TomTerm has no SubjectListAST");
  }

  public Option getOption()
  {
     throw new RuntimeException("This TomTerm has no Option");
  }

  public TomTerm setOption(Option _option)
  {
     throw new RuntimeException("This TomTerm has no Option");
  }

  public TomName getSlotName()
  {
     throw new RuntimeException("This TomTerm has no SlotName");
  }

  public TomTerm setSlotName(TomName _slotName)
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

  public TomTerm getSubjectList()
  {
     throw new RuntimeException("This TomTerm has no SubjectList");
  }

  public TomTerm setSubjectList(TomTerm _subjectList)
  {
     throw new RuntimeException("This TomTerm has no SubjectList");
  }

  public TomTerm getPatternList()
  {
     throw new RuntimeException("This TomTerm has no PatternList");
  }

  public TomTerm setPatternList(TomTerm _patternList)
  {
     throw new RuntimeException("This TomTerm has no PatternList");
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

  public TomRuleList getRuleList()
  {
     throw new RuntimeException("This TomTerm has no RuleList");
  }

  public TomTerm setRuleList(TomRuleList _ruleList)
  {
     throw new RuntimeException("This TomTerm has no RuleList");
  }

  public Option getOrgTrack()
  {
     throw new RuntimeException("This TomTerm has no OrgTrack");
  }

  public TomTerm setOrgTrack(Option _orgTrack)
  {
     throw new RuntimeException("This TomTerm has no OrgTrack");
  }

  public TomTerm getTomTerm()
  {
     throw new RuntimeException("This TomTerm has no TomTerm");
  }

  public TomTerm setTomTerm(TomTerm _tomTerm)
  {
     throw new RuntimeException("This TomTerm has no TomTerm");
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

  public TomList getInstList()
  {
     throw new RuntimeException("This TomTerm has no InstList");
  }

  public TomTerm setInstList(TomList _instList)
  {
     throw new RuntimeException("This TomTerm has no InstList");
  }

  public String getVarName()
  {
     throw new RuntimeException("This TomTerm has no VarName");
  }

  public TomTerm setVarName(String _varName)
  {
     throw new RuntimeException("This TomTerm has no VarName");
  }

  public Expression getSource()
  {
     throw new RuntimeException("This TomTerm has no Source");
  }

  public TomTerm setSource(Expression _source)
  {
     throw new RuntimeException("This TomTerm has no Source");
  }

  public int getNbUse()
  {
     throw new RuntimeException("This TomTerm has no NbUse");
  }

  public TomTerm setNbUse(int _nbUse)
  {
     throw new RuntimeException("This TomTerm has no NbUse");
  }

  public Expression getUsedInDoWhile()
  {
     throw new RuntimeException("This TomTerm has no UsedInDoWhile");
  }

  public TomTerm setUsedInDoWhile(Expression _usedInDoWhile)
  {
     throw new RuntimeException("This TomTerm has no UsedInDoWhile");
  }

  public Expression getRemovable()
  {
     throw new RuntimeException("This TomTerm has no Removable");
  }

  public TomTerm setRemovable(Expression _removable)
  {
     throw new RuntimeException("This TomTerm has no Removable");
  }

  public TomNumberList getNumberList()
  {
     throw new RuntimeException("This TomTerm has no NumberList");
  }

  public TomTerm setNumberList(TomNumberList _numberList)
  {
     throw new RuntimeException("This TomTerm has no NumberList");
  }

  public TomName getDebugName()
  {
     throw new RuntimeException("This TomTerm has no DebugName");
  }

  public TomTerm setDebugName(TomName _debugName)
  {
     throw new RuntimeException("This TomTerm has no DebugName");
  }

  public String getDocName()
  {
     throw new RuntimeException("This TomTerm has no DocName");
  }

  public TomTerm setDocName(String _docName)
  {
     throw new RuntimeException("This TomTerm has no DocName");
  }

  public XMLTerm getXmlTerm()
  {
     throw new RuntimeException("This TomTerm has no XmlTerm");
  }

  public TomTerm setXmlTerm(XMLTerm _xmlTerm)
  {
     throw new RuntimeException("This TomTerm has no XmlTerm");
  }

}

