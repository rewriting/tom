package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class DeclarationImpl extends TomSignatureConstructor
{
  static Declaration fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  static Declaration fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(Declaration peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static Declaration fromTerm(aterm.ATerm trm)
  {
    Declaration tmp;
    if ((tmp = Declaration_TypeTermDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_TypeListDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_TypeArrayDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_GetFunctionSymbolDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_GetSubtermDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_IsFsymDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_GetSlotDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_CompareFunctionSymbolDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_TermsEqualDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_GetHeadDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_GetTailDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_IsEmptyDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_MakeEmptyList.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_MakeAddList.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_GetElementDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_GetSizeDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_MakeEmptyArray.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_MakeAddArray.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_MakeDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_SymbolDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_ListSymbolDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_ArraySymbolDecl.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Declaration_EmptyDeclaration.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Declaration: " + trm);
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

  public boolean hasNumberArg()
  {
    return false;
  }

  public boolean hasTerm()
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
     throw new RuntimeException("This Declaration has no AstName");
  }

  public Declaration setAstName(TomName _astName)
  {
     throw new RuntimeException("This Declaration has no AstName");
  }

  public TomList getKeywordList()
  {
     throw new RuntimeException("This Declaration has no KeywordList");
  }

  public Declaration setKeywordList(TomList _keywordList)
  {
     throw new RuntimeException("This Declaration has no KeywordList");
  }

  public Option getOrgTrack()
  {
     throw new RuntimeException("This Declaration has no OrgTrack");
  }

  public Declaration setOrgTrack(Option _orgTrack)
  {
     throw new RuntimeException("This Declaration has no OrgTrack");
  }

  public TomTerm getTermArg()
  {
     throw new RuntimeException("This Declaration has no TermArg");
  }

  public Declaration setTermArg(TomTerm _termArg)
  {
     throw new RuntimeException("This Declaration has no TermArg");
  }

  public TargetLanguage getTlCode()
  {
     throw new RuntimeException("This Declaration has no TlCode");
  }

  public Declaration setTlCode(TargetLanguage _tlCode)
  {
     throw new RuntimeException("This Declaration has no TlCode");
  }

  public TomTerm getNumberArg()
  {
     throw new RuntimeException("This Declaration has no NumberArg");
  }

  public Declaration setNumberArg(TomTerm _numberArg)
  {
     throw new RuntimeException("This Declaration has no NumberArg");
  }

  public TomTerm getTerm()
  {
     throw new RuntimeException("This Declaration has no Term");
  }

  public Declaration setTerm(TomTerm _term)
  {
     throw new RuntimeException("This Declaration has no Term");
  }

  public TomName getSlotName()
  {
     throw new RuntimeException("This Declaration has no SlotName");
  }

  public Declaration setSlotName(TomName _slotName)
  {
     throw new RuntimeException("This Declaration has no SlotName");
  }

  public TomTerm getSymbolArg1()
  {
     throw new RuntimeException("This Declaration has no SymbolArg1");
  }

  public Declaration setSymbolArg1(TomTerm _symbolArg1)
  {
     throw new RuntimeException("This Declaration has no SymbolArg1");
  }

  public TomTerm getSymbolArg2()
  {
     throw new RuntimeException("This Declaration has no SymbolArg2");
  }

  public Declaration setSymbolArg2(TomTerm _symbolArg2)
  {
     throw new RuntimeException("This Declaration has no SymbolArg2");
  }

  public TomTerm getTermArg1()
  {
     throw new RuntimeException("This Declaration has no TermArg1");
  }

  public Declaration setTermArg1(TomTerm _termArg1)
  {
     throw new RuntimeException("This Declaration has no TermArg1");
  }

  public TomTerm getTermArg2()
  {
     throw new RuntimeException("This Declaration has no TermArg2");
  }

  public Declaration setTermArg2(TomTerm _termArg2)
  {
     throw new RuntimeException("This Declaration has no TermArg2");
  }

  public TomTerm getVar()
  {
     throw new RuntimeException("This Declaration has no Var");
  }

  public Declaration setVar(TomTerm _var)
  {
     throw new RuntimeException("This Declaration has no Var");
  }

  public TargetLanguage getTlcode()
  {
     throw new RuntimeException("This Declaration has no Tlcode");
  }

  public Declaration setTlcode(TargetLanguage _tlcode)
  {
     throw new RuntimeException("This Declaration has no Tlcode");
  }

  public TomTerm getVarElt()
  {
     throw new RuntimeException("This Declaration has no VarElt");
  }

  public Declaration setVarElt(TomTerm _varElt)
  {
     throw new RuntimeException("This Declaration has no VarElt");
  }

  public TomTerm getVarList()
  {
     throw new RuntimeException("This Declaration has no VarList");
  }

  public Declaration setVarList(TomTerm _varList)
  {
     throw new RuntimeException("This Declaration has no VarList");
  }

  public TomTerm getKid1()
  {
     throw new RuntimeException("This Declaration has no Kid1");
  }

  public Declaration setKid1(TomTerm _kid1)
  {
     throw new RuntimeException("This Declaration has no Kid1");
  }

  public TomTerm getKid2()
  {
     throw new RuntimeException("This Declaration has no Kid2");
  }

  public Declaration setKid2(TomTerm _kid2)
  {
     throw new RuntimeException("This Declaration has no Kid2");
  }

  public TomTerm getVarSize()
  {
     throw new RuntimeException("This Declaration has no VarSize");
  }

  public Declaration setVarSize(TomTerm _varSize)
  {
     throw new RuntimeException("This Declaration has no VarSize");
  }

  public TomType getAstType()
  {
     throw new RuntimeException("This Declaration has no AstType");
  }

  public Declaration setAstType(TomType _astType)
  {
     throw new RuntimeException("This Declaration has no AstType");
  }

  public TomList getArgs()
  {
     throw new RuntimeException("This Declaration has no Args");
  }

  public Declaration setArgs(TomList _args)
  {
     throw new RuntimeException("This Declaration has no Args");
  }


}

