package jtom.adt;

abstract public class TomTerm_MatchXMLImpl
extends TomTerm
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomTerm_MatchXMLImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_docName = 0;
  private static int index_patternList = 1;
  private static int index_option = 2;
  public shared.SharedObject duplicate() {
    TomTerm_MatchXML clone = new TomTerm_MatchXML(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_MatchXML) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_MatchXML(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isMatchXML()
  {
    return true;
  }

  public boolean hasDocName()
  {
    return true;
  }

  public boolean hasPatternList()
  {
    return true;
  }

  public boolean hasOption()
  {
    return true;
  }

  public String getDocName()
  {
   return ((aterm.ATermAppl) this.getArgument(index_docName)).getAFun().getName();
  }

  public TomTerm setDocName(String _docName)
  {
    return (TomTerm) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_docName, 0, true)), index_docName);
  }

  public TomTerm getPatternList()
  {
    return (TomTerm) this.getArgument(index_patternList) ;
  }

  public TomTerm setPatternList(TomTerm _patternList)
  {
    return (TomTerm) super.setArgument(_patternList, index_patternList);
  }

  public Option getOption()
  {
    return (Option) this.getArgument(index_option) ;
  }

  public TomTerm setOption(Option _option)
  {
    return (TomTerm) super.setArgument(_option, index_option);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_MatchXML should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_MatchXML should have type TomTerm");
        }
        break;
      case 2:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 2 of a TomTerm_MatchXML should have type Option");
        }
        break;
      default: throw new RuntimeException("TomTerm_MatchXML does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
