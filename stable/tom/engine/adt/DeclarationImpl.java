package jtom.adt;


abstract public class DeclarationImpl extends TomSignatureConstructor
{
  protected DeclarationImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(Declaration peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortDeclaration()  {
    return true;
  }

  public boolean isTypeTermDecl()
  {
    return false;
  }

  public boolean isTypeListDecl()
  {
    return false;
  }

  public boolean isTypeArrayDecl()
  {
    return false;
  }

  public boolean isGetFunctionSymbolDecl()
  {
    return false;
  }

  public boolean isGetSubtermDecl()
  {
    return false;
  }

  public boolean isIsFsymDecl()
  {
    return false;
  }

  public boolean isGetSlotDecl()
  {
    return false;
  }

  public boolean isCompareFunctionSymbolDecl()
  {
    return false;
  }

  public boolean isTermsEqualDecl()
  {
    return false;
  }

  public boolean isGetHeadDecl()
  {
    return false;
  }

  public boolean isGetTailDecl()
  {
    return false;
  }

  public boolean isIsEmptyDecl()
  {
    return false;
  }

  public boolean isMakeEmptyList()
  {
    return false;
  }

  public boolean isMakeAddList()
  {
    return false;
  }

  public boolean isGetElementDecl()
  {
    return false;
  }

  public boolean isGetSizeDecl()
  {
    return false;
  }

  public boolean isMakeEmptyArray()
  {
    return false;
  }

  public boolean isMakeAddArray()
  {
    return false;
  }

  public boolean isMakeDecl()
  {
    return false;
  }

  public boolean isSymbolDecl()
  {
    return false;
  }

  public boolean isListSymbolDecl()
  {
    return false;
  }

  public boolean isArraySymbolDecl()
  {
    return false;
  }

  public boolean isEmptyDeclaration()
  {
    return false;
  }

  public boolean hasAstName()
  {
    return false;
  }

  public boolean hasKeywordList()
  {
    return false;
  }

  public boolean hasOrgTrack()
  {
    return false;
  }

  public boolean hasTermArg()
  {
    return false;
  }

  public boolean hasTlCode()
  {
    return false;
  }

  public boolean hasVariable()
  {
    return false;
  }

  public boolean hasSlotName()
  {
    return false;
  }

  public boolean hasSymbolArg1()
  {
    return false;
  }

  public boolean hasSymbolArg2()
  {
    return false;
  }

  public boolean hasTermArg1()
  {
    return false;
  }

  public boolean hasTermArg2()
  {
    return false;
  }

  public boolean hasVar()
  {
    return false;
  }

  public boolean hasTlcode()
  {
    return false;
  }

  public boolean hasVarElt()
  {
    return false;
  }

  public boolean hasVarList()
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

  public boolean hasVarSize()
  {
    return false;
  }

  public boolean hasAstType()
  {
    return false;
  }

  public boolean hasArgs()
  {
    return false;
  }

  public TomName getAstName()
  {
     throw new UnsupportedOperationException("This Declaration has no AstName");
  }

  public Declaration setAstName(TomName _astName)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astName);
  }

  public TomList getKeywordList()
  {
     throw new UnsupportedOperationException("This Declaration has no KeywordList");
  }

  public Declaration setKeywordList(TomList _keywordList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _keywordList);
  }

  public Option getOrgTrack()
  {
     throw new UnsupportedOperationException("This Declaration has no OrgTrack");
  }

  public Declaration setOrgTrack(Option _orgTrack)
  {
     throw new IllegalArgumentException("Illegal argument: " + _orgTrack);
  }

  public TomTerm getTermArg()
  {
     throw new UnsupportedOperationException("This Declaration has no TermArg");
  }

  public Declaration setTermArg(TomTerm _termArg)
  {
     throw new IllegalArgumentException("Illegal argument: " + _termArg);
  }

  public TargetLanguage getTlCode()
  {
     throw new UnsupportedOperationException("This Declaration has no TlCode");
  }

  public Declaration setTlCode(TargetLanguage _tlCode)
  {
     throw new IllegalArgumentException("Illegal argument: " + _tlCode);
  }

  public TomTerm getVariable()
  {
     throw new UnsupportedOperationException("This Declaration has no Variable");
  }

  public Declaration setVariable(TomTerm _variable)
  {
     throw new IllegalArgumentException("Illegal argument: " + _variable);
  }

  public TomName getSlotName()
  {
     throw new UnsupportedOperationException("This Declaration has no SlotName");
  }

  public Declaration setSlotName(TomName _slotName)
  {
     throw new IllegalArgumentException("Illegal argument: " + _slotName);
  }

  public TomTerm getSymbolArg1()
  {
     throw new UnsupportedOperationException("This Declaration has no SymbolArg1");
  }

  public Declaration setSymbolArg1(TomTerm _symbolArg1)
  {
     throw new IllegalArgumentException("Illegal argument: " + _symbolArg1);
  }

  public TomTerm getSymbolArg2()
  {
     throw new UnsupportedOperationException("This Declaration has no SymbolArg2");
  }

  public Declaration setSymbolArg2(TomTerm _symbolArg2)
  {
     throw new IllegalArgumentException("Illegal argument: " + _symbolArg2);
  }

  public TomTerm getTermArg1()
  {
     throw new UnsupportedOperationException("This Declaration has no TermArg1");
  }

  public Declaration setTermArg1(TomTerm _termArg1)
  {
     throw new IllegalArgumentException("Illegal argument: " + _termArg1);
  }

  public TomTerm getTermArg2()
  {
     throw new UnsupportedOperationException("This Declaration has no TermArg2");
  }

  public Declaration setTermArg2(TomTerm _termArg2)
  {
     throw new IllegalArgumentException("Illegal argument: " + _termArg2);
  }

  public TomTerm getVar()
  {
     throw new UnsupportedOperationException("This Declaration has no Var");
  }

  public Declaration setVar(TomTerm _var)
  {
     throw new IllegalArgumentException("Illegal argument: " + _var);
  }

  public TargetLanguage getTlcode()
  {
     throw new UnsupportedOperationException("This Declaration has no Tlcode");
  }

  public Declaration setTlcode(TargetLanguage _tlcode)
  {
     throw new IllegalArgumentException("Illegal argument: " + _tlcode);
  }

  public TomTerm getVarElt()
  {
     throw new UnsupportedOperationException("This Declaration has no VarElt");
  }

  public Declaration setVarElt(TomTerm _varElt)
  {
     throw new IllegalArgumentException("Illegal argument: " + _varElt);
  }

  public TomTerm getVarList()
  {
     throw new UnsupportedOperationException("This Declaration has no VarList");
  }

  public Declaration setVarList(TomTerm _varList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _varList);
  }

  public TomTerm getKid1()
  {
     throw new UnsupportedOperationException("This Declaration has no Kid1");
  }

  public Declaration setKid1(TomTerm _kid1)
  {
     throw new IllegalArgumentException("Illegal argument: " + _kid1);
  }

  public TomTerm getKid2()
  {
     throw new UnsupportedOperationException("This Declaration has no Kid2");
  }

  public Declaration setKid2(TomTerm _kid2)
  {
     throw new IllegalArgumentException("Illegal argument: " + _kid2);
  }

  public TomTerm getVarSize()
  {
     throw new UnsupportedOperationException("This Declaration has no VarSize");
  }

  public Declaration setVarSize(TomTerm _varSize)
  {
     throw new IllegalArgumentException("Illegal argument: " + _varSize);
  }

  public TomType getAstType()
  {
     throw new UnsupportedOperationException("This Declaration has no AstType");
  }

  public Declaration setAstType(TomType _astType)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astType);
  }

  public TomList getArgs()
  {
     throw new UnsupportedOperationException("This Declaration has no Args");
  }

  public Declaration setArgs(TomList _args)
  {
     throw new IllegalArgumentException("Illegal argument: " + _args);
  }

}

