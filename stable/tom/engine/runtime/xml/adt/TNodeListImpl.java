package jtom.runtime.xml.adt;


abstract public class TNodeListImpl extends aterm.pure.ATermListImpl
{
  protected void init (int hashCode, aterm.ATermList annos, aterm.ATerm first, aterm.ATermList next) {
    super.init(hashCode, annos, first, next);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.ATerm first, aterm.ATermList next) {
    super.initHashCode(annos, first, next);
  }
  protected TNodeFactory factory = null;
 TNodeListImpl(TNodeFactory factory) {
     super(factory.getPureFactory());
     this.factory = factory;
  }
  public TNodeFactory getTNodeFactory(){
    return factory;
}
  protected aterm.ATerm term = null;
  public aterm.ATerm toTerm()
  {
    if (this.term == null) {
      TNodeList reversed = (TNodeList)this.reverse();
      aterm.ATermList tmp = getTNodeFactory().getPureFactory().makeList();
      for (; !reversed.isEmpty(); reversed = reversed.getTail()) {
         aterm.ATerm elem = reversed.getHead().toTerm();
         tmp = getTNodeFactory().getPureFactory().makeList(elem, tmp);
      }
      this.term = tmp;
    }
    return this.term;
  }
  public String toString() {
    return toTerm().toString();
  }
  public TNode getHead() {
    return (TNode) getFirst();
  }
  public TNodeList getTail() {
    return (TNodeList) getNext();
  }
  public boolean isSortTNodeList()  {
    return true;
  }

  public boolean isEmpty() {
    return this == getTNodeFactory().makeTNodeList();
  }
  public boolean isMany() {
    return !isEmpty();
  }
  public boolean isSingle() {
    return !isEmpty() && getNext().isEmpty();
  }
  public boolean hasHead() {
    return !isEmpty();
  }
  public boolean hasTail() {
    return !isEmpty();
  }
  public boolean equivalent(shared.SharedObject peer) {
	 if (peer instanceof TNodeList) {
	 	return super.equivalent(peer);
	 }
	 else {
      return false;
	 }
  }
  public shared.SharedObject duplicate() {
	 TNodeList clone = new TNodeList(factory);
	 clone.init(hashCode(), getAnnotations(), getFirst(), getNext());
	 return clone;
  }
  public aterm.ATermList getEmpty() {
    return (aterm.ATermList)getTNodeFactory().makeTNodeList();
  }

  public TNodeList insert(TNode head) {
    return getTNodeFactory().makeTNodeList(head, (TNodeList) this);
  }
  public aterm.ATermList insert(aterm.ATerm head) {
    return insert((TNode) head);
  }
}
