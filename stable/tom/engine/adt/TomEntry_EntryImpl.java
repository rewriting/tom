package jtom.adt;

import aterm.*;

public class TomEntry_EntryImpl
extends TomEntry
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_strName = 0;
  private static int index_astSymbol = 1;

  public shared.SharedObject duplicate() {
    TomEntry_Entry clone = new TomEntry_Entry();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomEntry_Entry(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("Entry(<str>,<term>)");
  }


  static public TomEntry fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomEntry tmp = getStaticTomSignatureFactory().makeTomEntry_Entry((String) children.get(0), TomSymbol.fromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public aterm.ATerm toTerm() {
    if(term == null) {
      java.util.List args = new java.util.LinkedList();
      args.add(((aterm.ATermAppl) getArgument(0)).getAFun().getName());
      args.add(((TomSignatureConstructor) getArgument(1)).toTerm());
      setTerm(getFactory().make(getPattern(), args));
    }
    return term;
  }

  public boolean isEntry()
  {
    return true;
  }

  public boolean hasStrName()
  {
    return true;
  }

  public boolean hasAstSymbol()
  {
    return true;
  }


  public String getStrName()
  {
   return ((aterm.ATermAppl) this.getArgument(index_strName)).getAFun().getName();
  }

  public TomEntry setStrName(String _strName)
  {
    return (TomEntry) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_strName, 0, true)), index_strName);
  }

  public TomSymbol getAstSymbol()
  {
    return (TomSymbol) this.getArgument(index_astSymbol) ;
  }

  public TomEntry setAstSymbol(TomSymbol _astSymbol)
  {
    return (TomEntry) super.setArgument(_astSymbol, index_astSymbol);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomEntry_Entry should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof TomSymbol)) { 
          throw new RuntimeException("Argument 1 of a TomEntry_Entry should have type TomSymbol");
        }
        break;
      default: throw new RuntimeException("TomEntry_Entry does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }

}
