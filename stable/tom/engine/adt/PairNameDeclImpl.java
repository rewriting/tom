package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class PairNameDeclImpl extends TomSignatureConstructor
{
  public static PairNameDecl fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  public static PairNameDecl fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(PairNameDecl peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static PairNameDecl fromTerm(aterm.ATerm trm)
  {
    PairNameDecl tmp;
    if ((tmp = PairNameDecl_Slot.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a PairNameDecl: " + trm);
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

