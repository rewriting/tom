package jtom.adt;

import aterm.*;

abstract public class SlotListImpl extends aterm.pure.ATermListImpl
{
  protected void init (int hashCode, aterm.ATermList annos, aterm.ATerm first, aterm.ATermList next) {
    super.init(hashCode, annos, first, next);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.ATerm first, aterm.ATermList next) {
    super.initHashCode(annos, first, next);
  }
  protected TomSignatureFactory factory = null;
 SlotListImpl(TomSignatureFactory factory) {
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
      SlotList reversed = (SlotList)this.reverse();
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
  public PairNameDecl getHead() {
    return (PairNameDecl) getFirst();
  }
  public SlotList getTail() {
    return (SlotList) getNext();
  }
  public boolean isSortSlotList()  {
    return true;
  }

  public boolean isEmpty() {
    return this == getTomSignatureFactory().makeSlotList();
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
	 if (peer instanceof SlotList) {
	 	return super.equivalent(peer);
	 }
	 else {
      return false;
	 }
  }
  public shared.SharedObject duplicate() {
	 SlotList clone = new SlotList(factory);
	 clone.init(hashCode(), getAnnotations(), getFirst(), getNext());
	 return clone;
  }
  public aterm.ATermList getEmpty() {
    return (aterm.ATermList)getTomSignatureFactory().makeSlotList();
  }

  public SlotList insert(PairNameDecl head) {
    return getTomSignatureFactory().makeSlotList(head, (SlotList) this);
  }
  public aterm.ATermList insert(aterm.ATerm head) {
    return insert((PairNameDecl) head);
  }
}
