package jtom.adt;

abstract public class Position_PositionImpl
extends Position
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected Position_PositionImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_line = 0;
  private static int index_column = 1;
  public shared.SharedObject duplicate() {
    Position_Position clone = new Position_Position(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Position_Position) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makePosition_Position(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isPosition()
  {
    return true;
  }

  public boolean hasLine()
  {
    return true;
  }

  public boolean hasColumn()
  {
    return true;
  }

  public int getLine()
  {
   return ((aterm.ATermInt) this.getArgument(index_line)).getInt();
  }

  public Position setLine(int _line)
  {
    return (Position) super.setArgument(getFactory().makeInt(_line), index_line);
  }

  public int getColumn()
  {
   return ((aterm.ATermInt) this.getArgument(index_column)).getInt();
  }

  public Position setColumn(int _column)
  {
    return (Position) super.setArgument(getFactory().makeInt(_column), index_column);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermInt)) { 
          throw new RuntimeException("Argument 0 of a Position_Position should have type int");
        }
        break;
      case 1:
        if (! (arg instanceof aterm.ATermInt)) { 
          throw new RuntimeException("Argument 1 of a Position_Position should have type int");
        }
        break;
      default: throw new RuntimeException("Position_Position does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
