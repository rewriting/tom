package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomListImpl extends aterm.pure.ATermListImpl
{
  protected TomSignatureFactory factory = null;
  TomListImpl(TomSignatureFactory factory) {
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
      TomList reversed = (TomList)this.reverse();
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
  public TomTerm getHead() {
    return (TomTerm) getFirst();
  }
  public TomList getTail() {
    return (TomList) getNext();
  }
  public boolean isSortTomList()  {
    return true;
  }

  public boolean isEmpty() {
    return this == TomSignatureFactory.emptyTomList;
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
	 if (peer instanceof TomList) {
	 	return super.equivalent(peer);
	 }
	 else {
      return false;
	 }
  }
  public shared.SharedObject duplicate() {
	 TomList clone = new TomList(factory);
	 clone.init(hashCode(), getAnnotations(), getFirst(), getNext());
	 return clone;
  }
  public aterm.ATermList getEmpty() {
    return (aterm.ATermList)getTomSignatureFactory().makeTomList();
  }

  public aterm.ATermList insert(aterm.ATerm head) {
    return (aterm.ATermList)getTomSignatureFactory().makeTomList((TomTerm) head, (TomList) this);
  }

  public TomList insert(TomTerm head) {
    return getTomSignatureFactory().makeTomList(head, (TomList) this);
  }
  public aterm.ATermList reverse() {
  	 TomListImpl cur = this;
  	 TomListImpl reverse = (TomListImpl) getTomSignatureFactory().makeTomList();
  	 while(!cur.isEmpty()){
  	   reverse = (TomListImpl)reverse.insert((aterm.ATerm) cur.getHead());
  	   cur = cur.getTail();
  	 }
  	 return reverse;
  }
}
