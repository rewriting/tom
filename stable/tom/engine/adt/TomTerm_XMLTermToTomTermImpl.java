package jtom.adt;

abstract public class TomTerm_XMLTermToTomTermImpl
extends TomTerm
{
  TomTerm_XMLTermToTomTermImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_xmlTerm = 0;
  public shared.SharedObject duplicate() {
    TomTerm_XMLTermToTomTerm clone = new TomTerm_XMLTermToTomTerm(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_XMLTermToTomTerm) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_XMLTermToTomTerm(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isXMLTermToTomTerm()
  {
    return true;
  }

  public boolean hasXmlTerm()
  {
    return true;
  }

  public XMLTerm getXmlTerm()
  {
    return (XMLTerm) this.getArgument(index_xmlTerm) ;
  }

  public TomTerm setXmlTerm(XMLTerm _xmlTerm)
  {
    return (TomTerm) super.setArgument(_xmlTerm, index_xmlTerm);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof XMLTerm)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_XMLTermToTomTerm should have type XMLTerm");
        }
        break;
      default: throw new RuntimeException("TomTerm_XMLTermToTomTerm does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
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
