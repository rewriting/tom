package jtom.adt;

import aterm.*;

public class TomTerm_CompiledMatchImpl
extends TomTerm
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_decls = 0;
  private static int index_automataList = 1;

  public shared.SharedObject duplicate() {
    TomTerm_CompiledMatch clone = new TomTerm_CompiledMatch();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_CompiledMatch(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("CompiledMatch(<term>,<term>)");
  }


  static public TomTerm fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomTerm tmp = getStaticTomSignatureFactory().makeTomTerm_CompiledMatch(TomList.fromTerm( (aterm.ATerm) children.get(0)), TomList.fromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isCompiledMatch()
  {
    return true;
  }

  public boolean hasDecls()
  {
    return true;
  }

  public boolean hasAutomataList()
  {
    return true;
  }


  public TomList getDecls()
  {
    return (TomList) this.getArgument(index_decls) ;
  }

  public TomTerm setDecls(TomList _decls)
  {
    return (TomTerm) super.setArgument(_decls, index_decls);
  }

  public TomList getAutomataList()
  {
    return (TomList) this.getArgument(index_automataList) ;
  }

  public TomTerm setAutomataList(TomList _automataList)
  {
    return (TomTerm) super.setArgument(_automataList, index_automataList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_CompiledMatch should have type TomList");
        }
        break;
      case 1:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_CompiledMatch should have type TomList");
        }
        break;
      default: throw new RuntimeException("TomTerm_CompiledMatch does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }

  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = 0x9e3779b9;
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
