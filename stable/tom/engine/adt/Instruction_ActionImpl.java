package jtom.adt;

abstract public class Instruction_ActionImpl
extends Instruction
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_instList = 0;
  public shared.SharedObject duplicate() {
    Instruction_Action clone = new Instruction_Action();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeInstruction_Action(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("Action(<term>)");
  }

  static public Instruction fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Instruction tmp = getStaticTomSignatureFactory().makeInstruction_Action(TomList.fromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  public boolean isAction()
  {
    return true;
  }

  public boolean hasInstList()
  {
    return true;
  }

  public TomList getInstList()
  {
    return (TomList) this.getArgument(index_instList) ;
  }

  public Instruction setInstList(TomList _instList)
  {
    return (Instruction) super.setArgument(_instList, index_instList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 0 of a Instruction_Action should have type TomList");
        }
        break;
      default: throw new RuntimeException("Instruction_Action does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = 0x9e3779b9;
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
