package jtom.adt;

abstract public class Instruction_AssignMatchSubjectImpl
extends Instruction
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_kid1 = 0;
  private static int index_source = 1;
  public shared.SharedObject duplicate() {
    Instruction_AssignMatchSubject clone = new Instruction_AssignMatchSubject();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeInstruction_AssignMatchSubject(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("AssignMatchSubject(<term>,<term>)");
  }

  static public Instruction fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Instruction tmp = getStaticTomSignatureFactory().makeInstruction_AssignMatchSubject(TomTerm.fromTerm( (aterm.ATerm) children.get(0)), Expression.fromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  public boolean isAssignMatchSubject()
  {
    return true;
  }

  public boolean hasKid1()
  {
    return true;
  }

  public boolean hasSource()
  {
    return true;
  }

  public TomTerm getKid1()
  {
    return (TomTerm) this.getArgument(index_kid1) ;
  }

  public Instruction setKid1(TomTerm _kid1)
  {
    return (Instruction) super.setArgument(_kid1, index_kid1);
  }

  public Expression getSource()
  {
    return (Expression) this.getArgument(index_source) ;
  }

  public Instruction setSource(Expression _source)
  {
    return (Instruction) super.setArgument(_source, index_source);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 0 of a Instruction_AssignMatchSubject should have type TomTerm");
        }
        break;
      case 1:
        if (! (arg instanceof Expression)) { 
          throw new RuntimeException("Argument 1 of a Instruction_AssignMatchSubject should have type Expression");
        }
        break;
      default: throw new RuntimeException("Instruction_AssignMatchSubject does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
    a += (getArgument(1).hashCode() << 8);
    a += (getArgument(0).hashCode() << 0);

    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);

    return c;
  }
}
