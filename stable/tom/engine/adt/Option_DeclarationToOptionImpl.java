package jtom.adt;

abstract public class Option_DeclarationToOptionImpl
extends Option
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_astDeclaration = 0;
  public shared.SharedObject duplicate() {
    Option_DeclarationToOption clone = new Option_DeclarationToOption();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeOption_DeclarationToOption(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("DeclarationToOption(<term>)");
  }

  static public Option fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Option tmp = getStaticTomSignatureFactory().makeOption_DeclarationToOption(Declaration.fromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  public boolean isDeclarationToOption()
  {
    return true;
  }

  public boolean hasAstDeclaration()
  {
    return true;
  }

  public Declaration getAstDeclaration()
  {
    return (Declaration) this.getArgument(index_astDeclaration) ;
  }

  public Option setAstDeclaration(Declaration _astDeclaration)
  {
    return (Option) super.setArgument(_astDeclaration, index_astDeclaration);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof Declaration)) { 
          throw new RuntimeException("Argument 0 of a Option_DeclarationToOption should have type Declaration");
        }
        break;
      default: throw new RuntimeException("Option_DeclarationToOption does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
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
