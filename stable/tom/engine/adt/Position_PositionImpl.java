package jtom.adt;

abstract public class Position_PositionImpl
extends Position
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_line = 0;
  private static int index_column = 1;
  public shared.SharedObject duplicate() {
    Position_Position clone = new Position_Position();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makePosition_Position(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("Position(<int>,<int>)");
  }

  static public Position fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Position tmp = getStaticTomSignatureFactory().makePosition_Position((Integer) children.get(0), (Integer) children.get(1));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  public aterm.ATerm toTerm() {
    if(term == null) {
      java.util.List args = new java.util.LinkedList();
      args.add(new Integer(((aterm.ATermInt) getArgument(0)).getInt()));
      args.add(new Integer(((aterm.ATermInt) getArgument(1)).getInt()));
      setTerm(getFactory().make(getPattern(), args));
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

  public Integer getLine()
  {
   return new Integer(((aterm.ATermInt) this.getArgument(index_line)).getInt());
  }

  public Position setLine(Integer _line)
  {
    return (Position) super.setArgument(getFactory().makeInt(_line.intValue()), index_line);
  }

  public Integer getColumn()
  {
   return new Integer(((aterm.ATermInt) this.getArgument(index_column)).getInt());
  }

  public Position setColumn(Integer _column)
  {
    return (Position) super.setArgument(getFactory().makeInt(_column.intValue()), index_column);
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
