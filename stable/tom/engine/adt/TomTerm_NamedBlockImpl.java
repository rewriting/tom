package jtom.adt;

import aterm.*;

public class TomTerm_NamedBlockImpl
extends TomTerm
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_blockName = 0;
  private static int index_instList = 1;

  public shared.SharedObject duplicate() {
    TomTerm_NamedBlock clone = new TomTerm_NamedBlock();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_NamedBlock(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("NamedBlock(<str>,<term>)");
  }


  static public TomTerm fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomTerm tmp = getStaticTomSignatureFactory().makeTomTerm_NamedBlock((String) children.get(0), TomList.fromTerm( (aterm.ATerm) children.get(1)));
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

  public boolean isNamedBlock()
  {
    return true;
  }

  public boolean hasBlockName()
  {
    return true;
  }

  public boolean hasInstList()
  {
    return true;
  }


  public String getBlockName()
  {
   return ((aterm.ATermAppl) this.getArgument(index_blockName)).getAFun().getName();
  }

  public TomTerm setBlockName(String _blockName)
  {
    return (TomTerm) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_blockName, 0, true)), index_blockName);
  }

  public TomList getInstList()
  {
    return (TomList) this.getArgument(index_instList) ;
  }

  public TomTerm setInstList(TomList _instList)
  {
    return (TomTerm) super.setArgument(_instList, index_instList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_NamedBlock should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_NamedBlock should have type TomList");
        }
        break;
      default: throw new RuntimeException("TomTerm_NamedBlock does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }

}
