package jtom.runtime.set.jgtreeset;

abstract public class JGTreeSet_PairImpl
extends JGTreeSet
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_value = 0;
  private static int index_multiplicity = 1;
  public shared.SharedObject duplicate() {
    JGTreeSet_Pair clone = new JGTreeSet_Pair();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getSetFactory().makeJGTreeSet_Pair(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("pair(<term>,<int>)");
  }

  static public JGTreeSet fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      JGTreeSet tmp = getStaticSetFactory().makeJGTreeSet_Pair((aterm.ATerm) children.get(0), (Integer) children.get(1));
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
      args.add((aterm.ATerm) getArgument(0));
      args.add(new Integer(((aterm.ATermInt) getArgument(1)).getInt()));
      setTerm(getFactory().make(getPattern(), args));
    }
    return term;
  }

  public boolean isPair()
  {
    return true;
  }

  public boolean hasValue()
  {
    return true;
  }

  public boolean hasMultiplicity()
  {
    return true;
  }

  public aterm.ATerm getValue()
  {
   return this.getArgument(index_value);
  }

  public JGTreeSet setValue(aterm.ATerm _value)
  {
    return (JGTreeSet) super.setArgument(_value, index_value);
  }

  public Integer getMultiplicity()
  {
   return new Integer(((aterm.ATermInt) this.getArgument(index_multiplicity)).getInt());
  }

  public JGTreeSet setMultiplicity(Integer _multiplicity)
  {
    return (JGTreeSet) super.setArgument(getFactory().makeInt(_multiplicity.intValue()), index_multiplicity);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATerm)) { 
          throw new RuntimeException("Argument 0 of a JGTreeSet_Pair should have type term");
        }
        break;
      case 1:
        if (! (arg instanceof aterm.ATermInt)) { 
          throw new RuntimeException("Argument 1 of a JGTreeSet_Pair should have type int");
        }
        break;
      default: throw new RuntimeException("JGTreeSet_Pair does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
