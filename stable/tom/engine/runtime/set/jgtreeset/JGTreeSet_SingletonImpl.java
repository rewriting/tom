package jtom.runtime.set.jgtreeset;

abstract public class JGTreeSet_SingletonImpl
extends JGTreeSet
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_value = 0;
  public shared.SharedObject duplicate() {
    JGTreeSet_Singleton clone = new JGTreeSet_Singleton();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getSetFactory().makeJGTreeSet_Singleton(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("singleton(<term>)");
  }

  static public JGTreeSet fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      JGTreeSet tmp = getStaticSetFactory().makeJGTreeSet_Singleton((aterm.ATerm) children.get(0));
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
      setTerm(getFactory().make(getPattern(), args));
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
