package jtom.adt;

import aterm.*;

public class Declaration_GetElementDeclImpl
extends Declaration
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_kid1 = 0;
  private static int index_kid2 = 1;
  private static int index_tlCode = 2;

  public shared.SharedObject duplicate() {
    Declaration_GetElementDecl clone = new Declaration_GetElementDecl();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeDeclaration_GetElementDecl(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("GetElementDecl(<term>,<term>,<term>)");
  }


  static public Declaration fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Declaration tmp = getStaticTomSignatureFactory().makeDeclaration_GetElementDecl(TomTerm.fromTerm( (aterm.ATerm) children.get(0)), TomTerm.fromTerm( (aterm.ATerm) children.get(1)), TargetLanguage.fromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isGetElementDecl()
  {
    return true;
  }

  public boolean hasKid1()
  {
    return true;
  }

  public boolean hasKid2()
  {
    return true;
  }

  public boolean hasTlCode()
  {
    return true;
  }


  public TomTerm getKid1()
  {
    return (TomTerm) this.getArgument(index_kid1) ;
  }

  public Declaration setKid1(TomTerm _kid1)
  {
    return (Declaration) super.setArgument(_kid1, index_kid1);
  }

  public TomTerm getKid2()
  {
    return (TomTerm) this.getArgument(index_kid2) ;
  }

  public Declaration setKid2(TomTerm _kid2)
  {
    return (Declaration) super.setArgument(_kid2, index_kid2);
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
          throw new RuntimeException("Argument 0 of a Declaration_GetElementDecl should have type TomTerm");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a Declaration_GetElementDecl should have type TomTerm");
        }
        break;
      case 2:
        if (! (arg instanceof TargetLanguage)) { 
          throw new RuntimeException("Argument 2 of a Declaration_GetElementDecl should have type TargetLanguage");
        }
        break;
      default: throw new RuntimeException("Declaration_GetElementDecl does not have an argument at " + i );
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
