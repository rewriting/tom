package jtom.adt;

abstract public class TomTerm_XMLApplImpl
extends TomTerm
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomTerm_XMLApplImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_option = 0;
  private static int index_astName = 1;
  private static int index_attrList = 2;
  private static int index_childList = 3;
  public shared.SharedObject duplicate() {
    TomTerm_XMLAppl clone = new TomTerm_XMLAppl(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_XMLAppl) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_XMLAppl(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isXMLAppl()
  {
    return true;
  }

  public boolean hasOption()
  {
    return true;
  }

  public boolean hasAstName()
  {
    return true;
  }

  public boolean hasAttrList()
  {
    return true;
  }

  public boolean hasChildList()
  {
    return true;
  }

  public Option getOption()
  {
    return (Option) this.getArgument(index_option) ;
  }

  public TomTerm setOption(Option _option)
  {
    return (TomTerm) super.setArgument(_option, index_option);
  }

  public TomName getAstName()
  {
    return (TomName) this.getArgument(index_astName) ;
  }

  public TomTerm setAstName(TomName _astName)
  {
    return (TomTerm) super.setArgument(_astName, index_astName);
  }

  public TomList getAttrList()
  {
    return (TomList) this.getArgument(index_attrList) ;
  }

  public TomTerm setAttrList(TomList _attrList)
  {
    return (TomTerm) super.setArgument(_attrList, index_attrList);
  }

  public TomList getChildList()
  {
    return (TomList) this.getArgument(index_childList) ;
  }

  public TomTerm setChildList(TomList _childList)
  {
    return (TomTerm) super.setArgument(_childList, index_childList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_XMLAppl should have type Option");
        }
        break;
      case 1:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_XMLAppl should have type TomName");
        }
        break;
      case 2:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 2 of a TomTerm_XMLAppl should have type TomList");
        }
        break;
      case 3:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 3 of a TomTerm_XMLAppl should have type TomList");
        }
        break;
      default: throw new RuntimeException("TomTerm_XMLAppl does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
    a += (getArgument(3).hashCode() << 24);
    a += (getArgument(2).hashCode() << 16);
    a += (getArgument(1).hashCode() << 8);
    a += (getArgument(0).hashCode() << 0);

    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);

    return c;
  }
}
