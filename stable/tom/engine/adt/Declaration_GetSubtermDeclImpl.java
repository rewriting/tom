package jtom.adt;

import aterm.*;

public class Declaration_GetSubtermDeclImpl
extends Declaration
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_termArg = 0;
  private static int index_numberArg = 1;
  private static int index_tlCode = 2;

  public shared.SharedObject duplicate() {
    Declaration_GetSubtermDecl clone = new Declaration_GetSubtermDecl();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeDeclaration_GetSubtermDecl(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("GetSubtermDecl(<term>,<term>,<term>)");
  }


  static public Declaration fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Declaration tmp = getStaticTomSignatureFactory().makeDeclaration_GetSubtermDecl(TomTerm.fromTerm( (aterm.ATerm) children.get(0)), TomTerm.fromTerm( (aterm.ATerm) children.get(1)), TargetLanguage.fromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isGetSubtermDecl()
  {
    return true;
  }

  public boolean hasTermArg()
  {
    return true;
  }

  public boolean hasNumberArg()
  {
    return true;
  }

  public boolean hasTlCode()
  {
    return true;
  }


  public TomTerm getTermArg()
  {
    return (TomTerm) this.getArgument(index_termArg) ;
  }

  public Declaration setTermArg(TomTerm _termArg)
  {
    return (Declaration) super.setArgument(_termArg, index_termArg);
  }

  public TomTerm getNumberArg()
  {
    return (TomTerm) this.getArgument(index_numberArg) ;
  }

  public Declaration setNumberArg(TomTerm _numberArg)
  {
    return (Declaration) super.setArgument(_numberArg, index_numberArg);
  }

  public TargetLanguage getTlCode()
  {
    return (TargetLanguage) this.getArgument(index_tlCode) ;
  }

  public Declaration setTlCode(TargetLanguage _tlCode)
  {
    return (Declaration) super.setArgument(_tlCode, index_tlCode);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 0 of a Declaration_GetSubtermDecl should have type TomTerm");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a Declaration_GetSubtermDecl should have type TomTerm");
        }
        break;
      case 2:
        if (! (arg instanceof TargetLanguage)) { 
          throw new RuntimeException("Argument 2 of a Declaration_GetSubtermDecl should have type TargetLanguage");
        }
        break;
      default: throw new RuntimeException("Declaration_GetSubtermDecl does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }

  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = 0x9e3779b9;
    a += (getArgument(2).hashCode() << 16);
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
