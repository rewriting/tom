package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class PairNameDeclImpl extends TomSignatureConstructor
{
  protected PairNameDeclImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(PairNameDecl peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortPairNameDecl()  {
    return true;
  }

  public boolean isSlot()
  {
    return false;
  }

  public boolean hasSlotName()
  {
    return false;
  }

  public boolean hasSlotDecl()
  {
    return false;
  }

  public TomName getSlotName()
  {
     throw new RuntimeException("This PairNameDecl has no SlotName");
  }

  public PairNameDecl setSlotName(TomName _slotName)
  {
     throw new RuntimeException("This PairNameDecl has no SlotName");
  }

  public Declaration getSlotDecl()
  {
     throw new RuntimeException("This PairNameDecl has no SlotDecl");
  }

  public PairNameDecl setSlotDecl(Declaration _slotDecl)
  {
     throw new RuntimeException("This PairNameDecl has no SlotDecl");
  }

}

