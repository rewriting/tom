package jtom.runtime.set.jgtreeset;

abstract public class JGTreeSet_SingletonImpl
extends JGTreeSet
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected JGTreeSet_SingletonImpl(SetFactory factory) {
    super(factory);
  }
  private static int index_value = 0;
  public shared.SharedObject duplicate() {
    JGTreeSet_Singleton clone = new JGTreeSet_Singleton(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof JGTreeSet_Singleton) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getSetFactory().makeJGTreeSet_Singleton(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getSetFactory().toTerm(this);
    }
    return term;
  }

  public boolean isSingleton()
  {
    return true;
  }

  public boolean hasValue()
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

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATerm)) { 
          throw new RuntimeException("Argument 0 of a JGTreeSet_Singleton should have type term");
        }
        break;
      default: throw new RuntimeException("JGTreeSet_Singleton does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
