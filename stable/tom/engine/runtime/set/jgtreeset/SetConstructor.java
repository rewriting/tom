package jtom.runtime.set.jgtreeset;

abstract public class SetConstructor
extends aterm.pure.ATermApplImpl
implements aterm.ATerm
{
  protected aterm.ATerm term = null;

  SetFactory factory = null;

  public SetConstructor(SetFactory factory) {
    super(factory);
    this.factory = factory;
  }

  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  abstract public aterm.ATerm toTerm();
  public String toString() {
    return toTerm().toString();
  }
  protected void setTerm(aterm.ATerm term) {
   this.term = term;
  }
  public SetFactory getSetFactory() {
    return factory;
  }
  public boolean isSortJGTreeSet() {
    return false;
  }

}
