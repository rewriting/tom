package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class SlotListImpl extends TomSignatureConstructor
{
  public static SlotList fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  public static SlotList fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(SlotList peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static SlotList fromTerm(aterm.ATerm trm)
  {
    SlotList tmp;
    if ((tmp = SlotList_EmptySlotList.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = SlotList_ConsSlotList.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a SlotList: " + trm);
  }

  public boolean isEmptySlotList()
  {
    return false;
  }

  public boolean isConsSlotList()
  {
    return false;
  }

  public boolean hasHeadSlotList()
  {
    return false;
  }

  public boolean hasTailSlotList()
  {
    return false;
  }

  public PairNameDecl getHeadSlotList()
  {
     throw new RuntimeException("This SlotList has no HeadSlotList");
  }

  public SlotList setHeadSlotList(PairNameDecl _headSlotList)
  {
     throw new RuntimeException("This SlotList has no HeadSlotList");
  }

  public SlotList getTailSlotList()
  {
     throw new RuntimeException("This SlotList has no TailSlotList");
  }

  public SlotList setTailSlotList(SlotList _tailSlotList)
  {
     throw new RuntimeException("This SlotList has no TailSlotList");
  }


}

