package jtom.runtime.set.jgtreeset;

import aterm.pure.PureFactory;

abstract public class SetConstructor
extends aterm.pure.ATermApplImpl
implements aterm.ATerm
{
  private static SetFactory factory;
  
  protected aterm.ATerm term = null;

  abstract protected aterm.ATerm getPattern();

  
  static public void initialize(SetFactory f) {
    factory = f;
  }
  
  public aterm.ATerm toTerm() {
    if(term == null) {
      java.util.List args = new java.util.LinkedList();
      for(int i = 0; i<getArity() ; i++) {
        args.add(((SetConstructor) getArgument(i)).toTerm());
      }
      setTerm(getFactory().make(getPattern(), args));
    }
    return term;
  }

  public String toString() {
    return toTerm().toString();
  }

  protected void setTerm(aterm.ATerm term) {
   this.term = term;
  }

  public SetFactory getSetFactory() {
    return factory;
  }

  static protected SetFactory getStaticSetFactory() {
    return factory;
  }

}
