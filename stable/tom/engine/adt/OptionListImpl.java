package jtom.adt;

import java.io.InputStream;
import java.io.IOException;
import aterm.*;


abstract public class OptionListImpl extends TomSignatureConstructor
{
  static OptionList fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }

  static OptionList fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }

  public boolean isEqual(OptionList peer)
  {
    return term.isEqual(peer.toTerm());
  }

  public static OptionList fromTerm(aterm.ATerm trm)
  {
    OptionList tmp;
    if ((tmp = OptionList_EmptyOptionList.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = OptionList_ConsOptionList.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a OptionList: " + trm);
  }


  public boolean isEmptyOptionList()
  {
    return false;
  }

  public boolean isConsOptionList()
  {
    return false;
  }

  public boolean hasHead()
  {
    return false;
  }

  public boolean hasTail()
  {
    return false;
  }


  public Option getHead()
  {
     throw new RuntimeException("This OptionList has no Head");
  }

  public OptionList setHead(Option _head)
  {
     throw new RuntimeException("This OptionList has no Head");
  }

  public OptionList getTail()
  {
     throw new RuntimeException("This OptionList has no Tail");
  }

  public OptionList setTail(OptionList _tail)
  {
     throw new RuntimeException("This OptionList has no Tail");
  }



}

