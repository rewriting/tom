package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomEntryListImpl extends aterm.pure.ATermListImpl
{
  protected TomSignatureFactory factory = null;
  TomEntryListImpl(TomSignatureFactory factory) {
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
      TomEntryList reversed = (TomEntryList)this.reverse();
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
    return this == TomSignatureFactory.emptyTomEntryList;
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

  public aterm.ATermList insert(aterm.ATerm head) {
    return (aterm.ATermList)getTomSignatureFactory().makeTomEntryList((TomEntry) head, (TomEntryList) this);
  }

  public TomEntryList insert(TomEntry head) {
    return getTomSignatureFactory().makeTomEntryList(head, (TomEntryList) this);
  }
  public aterm.ATermList reverse() {
  	 TomEntryListImpl cur = this;
  	 TomEntryListImpl reverse = (TomEntryListImpl) getTomSignatureFactory().makeTomEntryList();
  	 while(!cur.isEmpty()){
  	   reverse = (TomEntryListImpl)reverse.insert((aterm.ATerm) cur.getHead());
  	   cur = cur.getTail();
  	 }
  	 return reverse;
  }
}
