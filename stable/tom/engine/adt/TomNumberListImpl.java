package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomNumberListImpl extends aterm.pure.ATermListImpl
{
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

  public aterm.ATermList insert(aterm.ATerm head) {
    return (aterm.ATermList)getTomSignatureFactory().makeTomNumberList((TomNumber) head, (TomNumberList) this);
  }

  public TomNumberList insert(TomNumber head) {
    return getTomSignatureFactory().makeTomNumberList(head, (TomNumberList) this);
  }
  public aterm.ATermList reverse() {
  	 TomNumberListImpl cur = this;
  	 TomNumberListImpl reverse = (TomNumberListImpl) getTomSignatureFactory().makeTomNumberList();
  	 while(!cur.isEmpty()){
  	   reverse = (TomNumberListImpl)reverse.insert((aterm.ATerm) cur.getHead());
  	   cur = cur.getTail();
  	 }
  	 return reverse;
  }
}
