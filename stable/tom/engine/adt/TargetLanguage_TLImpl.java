package jtom.adt;

abstract public class TargetLanguage_TLImpl
extends TargetLanguage
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TargetLanguage_TLImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_code = 0;
  private static int index_start = 1;
  private static int index_end = 2;
  public shared.SharedObject duplicate() {
    TargetLanguage_TL clone = new TargetLanguage_TL(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TargetLanguage_TL) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTargetLanguage_TL(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isTL()
  {
    return true;
  }

  public boolean hasCode()
  {
    return true;
  }

  public boolean hasStart()
  {
    return true;
  }

  public boolean hasEnd()
  {
    return true;
  }

  public String getCode()
  {
   return ((aterm.ATermAppl) this.getArgument(index_code)).getAFun().getName();
  }

  public TargetLanguage setCode(String _code)
  {
    return (TargetLanguage) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_code, 0, true)), index_code);
  }

  public Position getStart()
  {
    return (Position) this.getArgument(index_start) ;
  }

  public TargetLanguage setStart(Position _start)
  {
    return (TargetLanguage) super.setArgument(_start, index_start);
  }

  public Position getEnd()
  {
    return (Position) this.getArgument(index_end) ;
  }

  public TargetLanguage setEnd(Position _end)
  {
    return (TargetLanguage) super.setArgument(_end, index_end);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TargetLanguage_TL should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof Position)) { 
          throw new RuntimeException("Argument 1 of a TargetLanguage_TL should have type Position");
        }
        break;
      case 2:
        if (! (arg instanceof Position)) { 
          throw new RuntimeException("Argument 2 of a TargetLanguage_TL should have type Position");
        }
        break;
      default: throw new RuntimeException("TargetLanguage_TL does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
