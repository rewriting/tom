package jtom.runtime.xml.adt;

abstract public class TNodeConstructor
extends aterm.pure.ATermApplImpl
implements aterm.ATerm
{
  protected aterm.ATerm term = null;

  TNodeFactory factory = null;

  public TNodeConstructor(TNodeFactory factory) {
    super(factory.getPureFactory());
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
  public TNodeFactory getTNodeFactory() {
    return factory;
  }
  public boolean isSortTNode() {
    return false;
  }

  public boolean isSortTNodeList() {
    return false;
  }

}
