package jtom.adt;


abstract public class TomEntryListImpl extends aterm.pure.ATermListImpl
{
  protected void init (int hashCode, aterm.ATermList annos, aterm.ATerm first, aterm.ATermList next) {
    super.init(hashCode, annos, first, next);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.ATerm first, aterm.ATermList next) {
    super.initHashCode(annos, first, next);
  }
  protected TomSignatureFactory factory = null;
 TomEntryListImpl(TomSignatureFactory factory) {
     super(factory.getPureFactory());
     this.factory = factory;
  }
  public TomSignatureFactory getTomSignatureFactory(){
    return factory;
}
  protected aterm.ATerm term = null;
  public aterm.ATerm toTerm()
  {
    if (this.term == null) {
      TomEntryList reversed = (TomEntryList)this.reverse();
      aterm.ATermList tmp = getTomSignatureFactory().getPureFactory().makeList();
      for (; !reversed.isEmpty(); reversed = reversed.getTail()) {
         aterm.ATerm elem = reversed.getHead().toTerm();
         tmp = getTomSignatureFactory().getPureFactory().makeList(elem, tmp);
      }
      this.term = tmp;
    }
    return this.term;
  }
  public String toString() {
    return toTerm().toString();
  }
  public TomEntry getHead() {
    return (TomEntry) getFirst();
  }
  public TomEntryList getTail() {
    return (TomEntryList) getNext();
  }
  public boolean isSortTomEntryList()  {
    return true;
  }

  public boolean isEmpty() {
    return this == getTomSignatureFactory().makeTomEntryList();
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
	 if (peer instanceof TomEntryList) {
	 	return super.equivalent(peer);
	 }
	 else {
      return false;
	 }
  }
  public shared.SharedObject duplicate() {
	 TomEntryList clone = new TomEntryList(factory);
	 clone.init(hashCode(), getAnnotations(), getFirst(), getNext());
	 return clone;
  }
  public aterm.ATermList getEmpty() {
    return (aterm.ATermList)getTomSignatureFactory().makeTomEntryList();
  }

  public TomEntryList insert(TomEntry head) {
    return getTomSignatureFactory().makeTomEntryList(head, (TomEntryList) this);
  }
  public aterm.ATermList insert(aterm.ATerm head) {
    return insert((TomEntry) head);
  }
}
