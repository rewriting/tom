package jtom.adt;

import aterm.*;

public class TomTerm_GLVarImpl
extends TomTerm
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_strName = 0;
  private static int index_astType = 1;

  public shared.SharedObject duplicate() {
    TomTerm_GLVar clone = new TomTerm_GLVar();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_GLVar(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("GLVar(<str>,<term>)");
  }


  static public TomTerm fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomTerm tmp = getStaticTomSignatureFactory().makeTomTerm_GLVar((String) children.get(0), TomType.fromTerm( (aterm.ATerm) children.get(1)));
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

  public boolean isGLVar()
  {
    return true;
  }

  public boolean hasStrName()
  {
    return true;
  }

  public boolean hasAstType()
  {
    return true;
  }


  public String getStrName()
  {
   return ((aterm.ATermAppl) this.getArgument(index_strName)).getAFun().getName();
  }

  public TomTerm setStrName(String _strName)
  {
    return (TomTerm) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_strName, 0, true)), index_strName);
  }

  public TomType getAstType()
  {
    return (TomType) this.getArgument(index_astType) ;
  }

  public TomTerm setAstType(TomType _astType)
  {
    return (TomTerm) super.setArgument(_astType, index_astType);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_GLVar should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof TomType)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_GLVar should have type TomType");
        }
        break;
      default: throw new RuntimeException("TomTerm_GLVar does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }

}
