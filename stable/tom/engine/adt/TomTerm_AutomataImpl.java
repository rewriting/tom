package jtom.adt;

abstract public class TomTerm_AutomataImpl
extends TomTerm
{
  TomTerm_AutomataImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_numberList = 0;
  private static int index_instList = 1;
  private static int index_debugName = 2;
  public shared.SharedObject duplicate() {
    TomTerm_Automata clone = new TomTerm_Automata(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_Automata) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_Automata(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isAutomata()
  {
    return true;
  }

  public boolean hasNumberList()
  {
    return true;
  }

  public boolean hasInstList()
  {
    return true;
  }

  public boolean hasDebugName()
  {
    return true;
  }

  public TomNumberList getNumberList()
  {
    return (TomNumberList) this.getArgument(index_numberList) ;
  }

  public TomTerm setNumberList(TomNumberList _numberList)
  {
    return (TomTerm) super.setArgument(_numberList, index_numberList);
  }

  public TomList getInstList()
  {
    return (TomList) this.getArgument(index_instList) ;
  }

  public TomTerm setInstList(TomList _instList)
  {
    return (TomTerm) super.setArgument(_instList, index_instList);
  }

  public TomName getDebugName()
  {
    return (TomName) this.getArgument(index_debugName) ;
  }

  public TomTerm setDebugName(TomName _debugName)
  {
    return (TomTerm) super.setArgument(_debugName, index_debugName);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomNumberList)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_Automata should have type TomNumberList");
        }
        break;
      case 1:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_Automata should have type TomList");
        }
        break;
      case 2:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 2 of a TomTerm_Automata should have type TomName");
        }
        break;
      default: throw new RuntimeException("TomTerm_Automata does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
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
