package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class OptionImpl extends TomSignatureConstructor
{
  protected OptionImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(Option peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortOption()  {
    return true;
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

  public boolean isGeneratedMatch()
  {
    return false;
  }

  public boolean isWithDefaultProduction()
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

  public boolean isOriginalText()
  {
    return false;
  }

  public boolean isXMLPosition()
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

  public boolean hasPlace()
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

  public String getPlace()
  {
     throw new RuntimeException("This Option has no Place");
  }

  public Option setPlace(String _place)
  {
     throw new RuntimeException("This Option has no Place");
  }

}

