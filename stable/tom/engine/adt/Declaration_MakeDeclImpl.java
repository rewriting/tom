package jtom.adt;

import aterm.*;

public class Declaration_MakeDeclImpl
extends Declaration
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_astName = 0;
  private static int index_astType = 1;
  private static int index_args = 2;
  private static int index_tlCode = 3;
  private static int index_orgTrack = 4;

  public shared.SharedObject duplicate() {
    Declaration_MakeDecl clone = new Declaration_MakeDecl();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeDeclaration_MakeDecl(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("MakeDecl(<term>,<term>,<term>,<term>,<term>)");
  }


  static public Declaration fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Declaration tmp = getStaticTomSignatureFactory().makeDeclaration_MakeDecl(TomName.fromTerm( (aterm.ATerm) children.get(0)), TomType.fromTerm( (aterm.ATerm) children.get(1)), TomList.fromTerm( (aterm.ATerm) children.get(2)), TargetLanguage.fromTerm( (aterm.ATerm) children.get(3)), Option.fromTerm( (aterm.ATerm) children.get(4)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isMakeDecl()
  {
    return true;
  }

  public boolean hasAstName()
  {
    return true;
  }

  public boolean hasAstType()
  {
    return true;
  }

  public boolean hasArgs()
  {
    return true;
  }

  public boolean hasTlCode()
  {
    return true;
  }

  public boolean hasOrgTrack()
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

  public TomType getAstType()
  {
    return (TomType) this.getArgument(index_astType) ;
  }

  public Declaration setAstType(TomType _astType)
  {
    return (Declaration) super.setArgument(_astType, index_astType);
  }

  public TomList getArgs()
  {
    return (TomList) this.getArgument(index_args) ;
  }

  public Declaration setArgs(TomList _args)
  {
    return (Declaration) super.setArgument(_args, index_args);
  }

  public TargetLanguage getTlCode()
  {
    return (TargetLanguage) this.getArgument(index_tlCode) ;
  }

  public Declaration setTlCode(TargetLanguage _tlCode)
  {
    return (Declaration) super.setArgument(_tlCode, index_tlCode);
  }

  public Option getOrgTrack()
  {
    return (Option) this.getArgument(index_orgTrack) ;
  }

  public Declaration setOrgTrack(Option _orgTrack)
  {
    return (Declaration) super.setArgument(_orgTrack, index_orgTrack);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a Declaration_MakeDecl should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof TomType)) { 
          throw new RuntimeException("Argument 1 of a Declaration_MakeDecl should have type TomType");
        }
        break;
      case 2:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 2 of a Declaration_MakeDecl should have type TomList");
        }
        break;
      case 3:
        if (! (arg instanceof TargetLanguage)) { 
          throw new RuntimeException("Argument 3 of a Declaration_MakeDecl should have type TargetLanguage");
        }
        break;
      case 4:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 4 of a Declaration_MakeDecl should have type Option");
        }
        break;
      default: throw new RuntimeException("Declaration_MakeDecl does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }

  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = 0x9e3779b9;
    b += (getArgument(4).hashCode() << 0);
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
