package jtom.adt;

public abstract class TomError
extends TomErrorImpl
{
  protected TomError(TomSignatureFactory factory) {
    super(factory);
  }

}
