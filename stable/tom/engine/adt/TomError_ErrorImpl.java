package jtom.adt;

abstract public class TomError_ErrorImpl
extends TomError
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomError_ErrorImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_message = 0;
  private static int index_file = 1;
  private static int index_line = 2;
  private static int index_level = 3;
  public shared.SharedObject duplicate() {
    TomError_Error clone = new TomError_Error(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomError_Error) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomError_Error(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isError()
  {
    return true;
  }

  public boolean hasMessage()
  {
    return true;
  }

  public boolean hasFile()
  {
    return true;
  }

  public boolean hasLine()
  {
    return true;
  }

  public boolean hasLevel()
  {
    return true;
  }

  public String getMessage()
  {
   return ((aterm.ATermAppl) this.getArgument(index_message)).getAFun().getName();
  }

  public TomError setMessage(String _message)
  {
    return (TomError) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_message, 0, true)), index_message);
  }

  public String getFile()
  {
   return ((aterm.ATermAppl) this.getArgument(index_file)).getAFun().getName();
  }

  public TomError setFile(String _file)
  {
    return (TomError) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_file, 0, true)), index_file);
  }

  public int getLine()
  {
   return ((aterm.ATermInt) this.getArgument(index_line)).getInt();
  }

  public TomError setLine(int _line)
  {
    return (TomError) super.setArgument(getFactory().makeInt(_line), index_line);
  }

  public int getLevel()
  {
   return ((aterm.ATermInt) this.getArgument(index_level)).getInt();
  }

  public TomError setLevel(int _level)
  {
    return (TomError) super.setArgument(getFactory().makeInt(_level), index_level);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomError_Error should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 1 of a TomError_Error should have type str");
        }
        break;
      case 2:
        if (! (arg instanceof aterm.ATermInt)) { 
          throw new RuntimeException("Argument 2 of a TomError_Error should have type int");
        }
        break;
      case 3:
        if (! (arg instanceof aterm.ATermInt)) { 
          throw new RuntimeException("Argument 3 of a TomError_Error should have type int");
        }
        break;
      default: throw new RuntimeException("TomError_Error does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
