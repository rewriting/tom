package jtom.adt;

import aterm.*;

public class Declaration_GetSlotDeclImpl
extends Declaration
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_astName = 0;
  private static int index_slotName = 1;
  private static int index_term = 2;
  private static int index_tlCode = 3;

  public shared.SharedObject duplicate() {
    Declaration_GetSlotDecl clone = new Declaration_GetSlotDecl();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeDeclaration_GetSlotDecl(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("GetSlotDecl(<term>,<term>,<term>,<term>)");
  }


  static public Declaration fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Declaration tmp = getStaticTomSignatureFactory().makeDeclaration_GetSlotDecl(TomName.fromTerm( (aterm.ATerm) children.get(0)), TomTerm.fromTerm( (aterm.ATerm) children.get(1)), TomTerm.fromTerm( (aterm.ATerm) children.get(2)), TargetLanguage.fromTerm( (aterm.ATerm) children.get(3)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isGetSlotDecl()
  {
    return true;
  }

  public boolean hasAstName()
  {
    return true;
  }

  public boolean hasSlotName()
  {
    return true;
  }

  public boolean hasTerm()
  {
    return true;
  }

  public boolean hasTlCode()
  {
    return true;
  }


  public TomName getAstName()
  {
    return (TomName) this.getArgument(index_astName) ;
  }

  public Declaration setAstName(TomName _astName)
  {
    return (Declaration) super.setArgument(_astName, index_astName);
  }

  public TomTerm getSlotName()
  {
    return (TomTerm) this.getArgument(index_slotName) ;
  }

  public Declaration setSlotName(TomTerm _slotName)
  {
    return (Declaration) super.setArgument(_slotName, index_slotName);
  }

  public TomTerm getTerm()
  {
    return (TomTerm) this.getArgument(index_term) ;
  }

  public Declaration setTerm(TomTerm _term)
  {
    return (Declaration) super.setArgument(_term, index_term);
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
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a Declaration_GetSlotDecl should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a Declaration_GetSlotDecl should have type TomTerm");
        }
        break;
      case 2:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 2 of a Declaration_GetSlotDecl should have type TomTerm");
        }
        break;
      case 3:
        if (! (arg instanceof TargetLanguage)) { 
          throw new RuntimeException("Argument 3 of a Declaration_GetSlotDecl should have type TargetLanguage");
        }
        break;
      default: throw new RuntimeException("Declaration_GetSlotDecl does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }

  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = 0x9e3779b9;
    a += (getArgument(3).hashCode() << 24);
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
