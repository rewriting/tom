package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomNumberListImpl extends aterm.pure.ATermListImpl
{
  protected void init (int hashCode, aterm.ATermList annos, aterm.ATerm first,	aterm.ATermList next) {
    super.init(hashCode, annos, first, next);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.ATerm first, aterm.ATermList next) {
    super.initHashCode(annos, first, next);
  }
  protected TomSignatureFactory factory = null;
  TomNumberListImpl(TomSignatureFactory factory) {
     super(factory);
     this.factory = factory;
  }
  public TomSignatureFactory getTomSignatureFactory(){
    return factory;
}
  protected aterm.ATerm term = null;
  public aterm.ATerm toTerm()
  {
    if (this.term == null) {
      TomNumberList reversed = (TomNumberList)this.reverse();
      aterm.ATermList tmp = getTomSignatureFactory().makeList();
      for (; !reversed.isEmpty(); reversed = reversed.getTail()) {
         aterm.ATerm elem = reversed.getHead().toTerm();
         tmp = getTomSignatureFactory().makeList(elem, tmp);
      }
      this.term = tmp;
    }
    return this.term;
  }
  public String toString() {
    return toTerm().toString();
  }
  public TomNumber getHead() {
    return (TomNumber) getFirst();
  }
  public TomNumberList getTail() {
    return (TomNumberList) getNext();
  }
  public boolean isSortTomNumberList()  {
    return true;
  }

  public boolean isEmpty() {
    return this == TomSignatureFactory.emptyTomNumberList;
  }
  public boolean isMany() {
    return !isEmpty();
  }
  public boolean hasHead() {
    return !isEmpty();
  }
  public boolean hasTail() {
    return !isEmpty();
  }
  public boolean equivalent(shared.SharedObject peer) {
	 if (peer instanceof TomNumberList) {
	 	return super.equivalent(peer);
	 }
	 else {
      return false;
	 }
  }
  public shared.SharedObject duplicate() {
	 TomNumberList clone = new TomNumberList(factory);
	 clone.init(hashCode(), getAnnotations(), getFirst(), getNext());
	 return clone;
  }
  public aterm.ATermList getEmpty() {
    return (aterm.ATermList)getTomSignatureFactory().makeTomNumberList();
  }

  public TomNumberList insert(TomNumber head) {
    return getTomSignatureFactory().makeTomNumberList(head, (TomNumberList) this);
  }
  public aterm.ATermList insert(aterm.ATerm head) {
    return insert((TomNumber) head);
  }
}
