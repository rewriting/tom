package adt;

abstract public class PeanoConstructor
extends aterm.pure.ATermApplImpl
implements aterm.ATerm
{
  protected aterm.ATerm term = null;

  abstract protected aterm.ATerm getPattern();

  public aterm.ATerm toTerm() {
    if(term == null) {
      java.util.List args = new java.util.LinkedList();
      for(int i = 0; i<getArity() ; i++) {
        args.add(((PeanoConstructor) getArgument(i)).toTerm());
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

  protected PeanoFactory getPeanoFactory() {
    return (PeanoFactory) getFactory();
  }

  static protected PeanoFactory getStaticPeanoFactory() {
    return (PeanoFactory) getStaticFactory();
  }

}
