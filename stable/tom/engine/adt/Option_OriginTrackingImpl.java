package jtom.adt;

abstract public class Option_OriginTrackingImpl
extends Option
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected Option_OriginTrackingImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_astName = 0;
  private static int index_line = 1;
  private static int index_fileName = 2;
  public shared.SharedObject duplicate() {
    Option_OriginTracking clone = new Option_OriginTracking(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Option_OriginTracking) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeOption_OriginTracking(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isOriginTracking()
  {
    return true;
  }

  public boolean hasAstName()
  {
    return true;
  }

  public boolean hasLine()
  {
    return true;
  }

  public boolean hasFileName()
  {
    return true;
  }

  public TomName getAstName()
  {
    return (TomName) this.getArgument(index_astName) ;
  }

  public Option setAstName(TomName _astName)
  {
    return (Option) super.setArgument(_astName, index_astName);
  }

  public int getLine()
  {
   return ((aterm.ATermInt) this.getArgument(index_line)).getInt();
  }

  public Option setLine(int _line)
  {
    return (Option) super.setArgument(getFactory().makeInt(_line), index_line);
  }

  public TomName getFileName()
  {
    return (TomName) this.getArgument(index_fileName) ;
  }

  public Option setFileName(TomName _fileName)
  {
    return (Option) super.setArgument(_fileName, index_fileName);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a Option_OriginTracking should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof aterm.ATermInt)) { 
          throw new RuntimeException("Argument 1 of a Option_OriginTracking should have type int");
        }
        break;
      case 2:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 2 of a Option_OriginTracking should have type TomName");
        }
        break;
      default: throw new RuntimeException("Option_OriginTracking does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
