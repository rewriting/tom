package jtom.runtime.set.jgtreeset;

abstract public class JGTreeSet_PairImpl
extends JGTreeSet
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected JGTreeSet_PairImpl(SetFactory factory) {
    super(factory);
  }
  private static int index_value = 0;
  private static int index_multiplicity = 1;
  public shared.SharedObject duplicate() {
    JGTreeSet_Pair clone = new JGTreeSet_Pair(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof JGTreeSet_Pair) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getSetFactory().makeJGTreeSet_Pair(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getSetFactory().toTerm(this);
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

  public int getMultiplicity()
  {
   return ((aterm.ATermInt) this.getArgument(index_multiplicity)).getInt();
  }

  public JGTreeSet setMultiplicity(int _multiplicity)
  {
    return (JGTreeSet) super.setArgument(getFactory().makeInt(_multiplicity), index_multiplicity);
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
