package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class OptionImpl extends TomSignatureConstructor
{
  static Option fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  static Option fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(Option peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static Option fromTerm(aterm.ATerm trm)
  {
    Option tmp;
    if ((tmp = Option_DeclarationToOption.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Option_TomNameToOption.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Option_TomTermToOption.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Option_Option.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Option_DefinedSymbol.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Option_OriginTracking.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Option_Constructor.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Option: " + trm);
  }

  public boolean isDeclarationToOption()
  {
    return false;
  }

  public boolean isTomNameToOption()
  {
    return false;
  }

  public boolean isTomTermToOption()
  {
    return false;
  }

  public boolean isOption()
  {
    return false;
  }

  public boolean isDefinedSymbol()
  {
    return false;
  }

  public boolean isOriginTracking()
  {
    return false;
  }

  public boolean isConstructor()
  {
    return false;
  }

  public boolean hasAstDeclaration()
  {
    return false;
  }

  public boolean hasAstName()
  {
    return false;
  }

  public boolean hasAstTerm()
  {
    return false;
  }

  public boolean hasOptionList()
  {
    return false;
  }

  public boolean hasLine()
  {
    return false;
  }

  public boolean hasFileName()
  {
    return false;
  }

  public Declaration getAstDeclaration()
  {
     throw new RuntimeException("This Option has no AstDeclaration");
  }

  public Option setAstDeclaration(Declaration _astDeclaration)
  {
     throw new RuntimeException("This Option has no AstDeclaration");
  }

  public TomName getAstName()
  {
     throw new RuntimeException("This Option has no AstName");
  }

  public Option setAstName(TomName _astName)
  {
     throw new RuntimeException("This Option has no AstName");
  }

  public TomTerm getAstTerm()
  {
     throw new RuntimeException("This Option has no AstTerm");
  }

  public Option setAstTerm(TomTerm _astTerm)
  {
     throw new RuntimeException("This Option has no AstTerm");
  }

  public OptionList getOptionList()
  {
     throw new RuntimeException("This Option has no OptionList");
  }

  public Option setOptionList(OptionList _optionList)
  {
     throw new RuntimeException("This Option has no OptionList");
  }

  public Integer getLine()
  {
     throw new RuntimeException("This Option has no Line");
  }

  public Option setLine(Integer _line)
  {
     throw new RuntimeException("This Option has no Line");
  }

  public TomName getFileName()
  {
     throw new RuntimeException("This Option has no FileName");
  }

  public Option setFileName(TomName _fileName)
  {
     throw new RuntimeException("This Option has no FileName");
  }


}

