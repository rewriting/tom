package jtom.adt;

import aterm.*;

public class Declaration_TermsEqualDeclImpl
extends Declaration
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_termArg1 = 0;
  private static int index_termArg2 = 1;
  private static int index_tlCode = 2;
  private static int index_orgTrack = 3;

  public shared.SharedObject duplicate() {
    Declaration_TermsEqualDecl clone = new Declaration_TermsEqualDecl();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeDeclaration_TermsEqualDecl(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("TermsEqualDecl(<term>,<term>,<term>,<term>)");
  }


  static public Declaration fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Declaration tmp = getStaticTomSignatureFactory().makeDeclaration_TermsEqualDecl(TomTerm.fromTerm( (aterm.ATerm) children.get(0)), TomTerm.fromTerm( (aterm.ATerm) children.get(1)), TargetLanguage.fromTerm( (aterm.ATerm) children.get(2)), Option.fromTerm( (aterm.ATerm) children.get(3)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isTermsEqualDecl()
  {
    return true;
  }

  public boolean hasTermArg1()
  {
    return true;
  }

  public boolean hasTermArg2()
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


  public TomTerm getTermArg1()
  {
    return (TomTerm) this.getArgument(index_termArg1) ;
  }

  public Declaration setTermArg1(TomTerm _termArg1)
  {
    return (Declaration) super.setArgument(_termArg1, index_termArg1);
  }

  public TomTerm getTermArg2()
  {
    return (TomTerm) this.getArgument(index_termArg2) ;
  }

  public Declaration setTermArg2(TomTerm _termArg2)
  {
    return (Declaration) super.setArgument(_termArg2, index_termArg2);
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
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 0 of a Declaration_TermsEqualDecl should have type TomTerm");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a Declaration_TermsEqualDecl should have type TomTerm");
        }
        break;
      case 2:
        if (! (arg instanceof TargetLanguage)) { 
          throw new RuntimeException("Argument 2 of a Declaration_TermsEqualDecl should have type TargetLanguage");
        }
        break;
      case 3:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 3 of a Declaration_TermsEqualDecl should have type Option");
        }
        break;
      default: throw new RuntimeException("Declaration_TermsEqualDecl does not have an argument at " + i );
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
