package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomRuleListImpl extends aterm.pure.ATermListImpl
{
  protected void init (int hashCode, aterm.ATermList annos, aterm.ATerm first,	aterm.ATermList next) {
    super.init(hashCode, annos, first, next);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.ATerm first, aterm.ATermList next) {
    super.initHashCode(annos, first, next);
  }
  protected TomSignatureFactory factory = null;
  TomRuleListImpl(TomSignatureFactory factory) {
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
      TomRuleList reversed = (TomRuleList)this.reverse();
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
  public TomRule getHead() {
    return (TomRule) getFirst();
  }
  public TomRuleList getTail() {
    return (TomRuleList) getNext();
  }
  public boolean isSortTomRuleList()  {
    return true;
  }

  public boolean isEmpty() {
    return this == TomSignatureFactory.emptyTomRuleList;
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
	 if (peer instanceof TomRuleList) {
	 	return super.equivalent(peer);
	 }
	 else {
      return false;
	 }
  }
  public shared.SharedObject duplicate() {
	 TomRuleList clone = new TomRuleList(factory);
	 clone.init(hashCode(), getAnnotations(), getFirst(), getNext());
	 return clone;
  }
  public aterm.ATermList getEmpty() {
    return (aterm.ATermList)getTomSignatureFactory().makeTomRuleList();
  }

  public TomRuleList insert(TomRule head) {
    return getTomSignatureFactory().makeTomRuleList(head, (TomRuleList) this);
  }
  public aterm.ATermList insert(aterm.ATerm head) {
    return insert((TomRule) head);
  }
}
