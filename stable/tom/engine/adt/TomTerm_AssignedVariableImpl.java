package jtom.adt;

abstract public class TomTerm_AssignedVariableImpl
extends TomTerm
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_varName = 0;
  private static int index_source = 1;
  private static int index_nbUse = 2;
  public shared.SharedObject duplicate() {
    TomTerm_AssignedVariable clone = new TomTerm_AssignedVariable();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_AssignedVariable(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("AssignedVariable(<str>,<term>,<int>)");
  }

  static public TomTerm fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomTerm tmp = getStaticTomSignatureFactory().makeTomTerm_AssignedVariable((String) children.get(0), Expression.fromTerm( (aterm.ATerm) children.get(1)), (Integer) children.get(2));
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
      args.add(new Integer(((aterm.ATermInt) getArgument(2)).getInt()));
      setTerm(getFactory().make(getPattern(), args));
    }
    return term;
  }

  public boolean isAssignedVariable()
  {
    return true;
  }

  public boolean hasVarName()
  {
    return true;
  }

  public boolean hasSource()
  {
    return true;
  }

  public boolean hasNbUse()
  {
    return true;
  }

  public String getVarName()
  {
   return ((aterm.ATermAppl) this.getArgument(index_varName)).getAFun().getName();
  }

  public TomTerm setVarName(String _varName)
  {
    return (TomTerm) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_varName, 0, true)), index_varName);
  }

  public Expression getSource()
  {
    return (Expression) this.getArgument(index_source) ;
  }

  public TomTerm setSource(Expression _source)
  {
    return (TomTerm) super.setArgument(_source, index_source);
  }

  public Integer getNbUse()
  {
   return new Integer(((aterm.ATermInt) this.getArgument(index_nbUse)).getInt());
  }

  public TomTerm setNbUse(Integer _nbUse)
  {
    return (TomTerm) super.setArgument(getFactory().makeInt(_nbUse.intValue()), index_nbUse);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_AssignedVariable should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof Expression)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_AssignedVariable should have type Expression");
        }
        break;
      case 2:
        if (! (arg instanceof aterm.ATermInt)) { 
          throw new RuntimeException("Argument 2 of a TomTerm_AssignedVariable should have type int");
        }
        break;
      default: throw new RuntimeException("TomTerm_AssignedVariable does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
