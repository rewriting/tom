package jtom.adt;

import aterm.*;

public class TargetLanguage_TLImpl
extends TargetLanguage
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_code = 0;
  private static int index_start = 1;
  private static int index_end = 2;

  public shared.SharedObject duplicate() {
    TargetLanguage_TL clone = new TargetLanguage_TL();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTargetLanguage_TL(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("TL(<str>,<term>,<term>)");
  }


  static public TargetLanguage fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TargetLanguage tmp = getStaticTomSignatureFactory().makeTargetLanguage_TL((String) children.get(0), Position.fromTerm( (aterm.ATerm) children.get(1)), Position.fromTerm( (aterm.ATerm) children.get(2)));
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
      args.add(((aterm.ATermAppl) getArgument(0)).getAFun().getName());
      args.add(((TomSignatureConstructor) getArgument(1)).toTerm());
      args.add(((TomSignatureConstructor) getArgument(2)).toTerm());
      setTerm(getFactory().make(getPattern(), args));
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
