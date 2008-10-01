package lemu;
import lemu.proofterms.types.*;
import lemu.proofterms.types.namelist.nameList;
import lemu.proofterms.types.conamelist.conameList;

public class Evaluation {

  %include { proofterms/proofterms.tom } 

  private static boolean topIntroducedName(ProofTerm pt, Name name) { 
    %match (pt) {
      ax(n,_) -> { return `name.equals(`n); }
      falseL(n) -> { return `name.equals(`n); }
      andL(_,n) -> { return `name.equals(`n); }
      orL(_,_,n) -> { return `name.equals(`n); }
      implyL(_,_,n) -> { return `name.equals(`n); }
      forallL(_,_,n) -> { return `name.equals(`n); }
      existsL(_,n) -> { return `name.equals(`n); }
    }
    return false;
  }

  public static boolean topIntroducedCoName(ProofTerm pt, CoName coname) { 
    %match (pt) {
      ax(_,cn) -> { return `coname.equals(`cn); }
      trueR(cn) -> { return `coname.equals(`cn); }
      orR(_,cn) -> { return `coname.equals(`cn); }
      andR(_,_,cn) -> { return `coname.equals(`cn); }
      implyR(_,cn) -> { return `coname.equals(`cn); }
      existsR(_,_,cn) -> { return `coname.equals(`cn); }
      forallR(_,cn) -> { return `coname.equals(`cn); }
    }
    return false;
  }

  public static nameList getFreeNames(NameList ctx, ProofTerm pt) {
    nameList c = (nameList) ctx;
    %match (pt) {
      ax(n,_) -> {
        return (nameList) (c.contains(`n) ? `nameList() : `nameList(n));
      }
      cut(CutPrem1(_,_,M1),CutPrem2(x,_,M2)) -> {
        NameList M1names = `getFreeNames(c,M1);
        NameList M2names = `getFreeNames(nameList(x,c*),M2);
        return (nameList) `nameList(M1names*,M2names*); 
      }
      // left rules
      andL(AndLPrem1(x,_,y,_,M),n) -> {
        NameList Mnames = `getFreeNames(nameList(x,y,c*),M);
        return (nameList) (c.contains(`n) ? Mnames : `nameList(n,Mnames*)); 
      }
      orL(OrLPrem1(x,_,M1),OrLPrem2(y,_,M2),n) -> {
        NameList M1names = `getFreeNames(nameList(x,c*),M1);
        NameList M2names = `getFreeNames(nameList(y,c*),M2);
        return (nameList) (c.contains(`n) ? `nameList(M1names*,M2names*) : `nameList(n,M1names*,M2names*));
      }
      implyL(ImplyLPrem1(x,_,M1),ImplyLPrem2(_,_,M2),n) -> {
        NameList M1names = `getFreeNames(nameList(x,c*),M1);
        NameList M2names = `getFreeNames(c,M2);
        return (nameList) (c.contains(`n) ? `nameList(M1names*,M2names*) : `nameList(n,M1names*,M2names*)); 
      }
      forallL(ForallLPrem1(x,_,M),_,n) -> {
        NameList Mnames = `getFreeNames(nameList(x,c*),M);
        return (nameList) (c.contains(`n) ? Mnames : `nameList(n,Mnames*)); 
      }
      existsL(ExistsLPrem1(x,_,_,M),n) -> {
        NameList Mnames = `getFreeNames(nameList(x,c*),M);
        return (nameList) (c.contains(`n) ? Mnames : `nameList(n,Mnames*)); 
      }
      rootL(RootLPrem1(x,_,M)) -> {
        return `getFreeNames(nameList(x,c*),M);
      }
      // right rules
      orR(OrRPrem1(_,_,_,_,M),cn) -> {
        return `getFreeNames(c,M);
      }
      andR(AndRPrem1(_,_,M1),AndRPrem2(_,_,M2),cn) -> {
        NameList M1names = `getFreeNames(c,M1);
        NameList M2names = `getFreeNames(c,M2);
        return (nameList) `nameList(M1names*,M2names*); 
      }
      implyR(ImplyRPrem1(x,_,_,_,M),cn) -> {
        return `getFreeNames(nameList(x,c*),M);
      }
      existsR(ExistsRPrem1(_,_,M),_,cn) -> {
        return `getFreeNames(c,M);
      }
      forallR(ForallRPrem1(_,_,_,M),cn) -> {
        return `getFreeNames(c,M);
      }
      rootR(RootRPrem1(_,_,M)) -> {
        return `getFreeNames(c,M);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static nameList getFreeNames(ProofTerm pt) {
    return `getFreeNames(nameList(),pt);
  }

  public static boolean nameAppears(ProofTerm pt, Name n) {
    return getFreeNames(pt).contains(n);
  }

  public static boolean freshlyIntroducedName(ProofTerm pt, Name n) {
    return topIntroducedName(pt,n) && !isImplicitContraction(pt);
  }

  public static conameList getFreeCoNames(CoNameList ctx, ProofTerm pt) {
    conameList c = (conameList) ctx;
    %match (pt) {
      ax(_,cn) -> {
        return (conameList) (c.contains(`cn) ? `conameList() : `conameList(cn));
      }
      cut(CutPrem1(a,_,M1),CutPrem2(_,_,M2)) -> {
        CoNameList M1conames = `getFreeCoNames(conameList(a,c*),M1);
        CoNameList M2conames = `getFreeCoNames(c,M2);
        return (conameList) `conameList(M1conames*,M2conames*); 
      }
      // left rules
      andL(AndLPrem1(_,_,_,_,M),n) -> {
        return `getFreeCoNames(c,M);
      }
      orL(OrLPrem1(_,_,M1),OrLPrem2(_,_,M2),n) -> {
        CoNameList M1conames = `getFreeCoNames(c,M1);
        CoNameList M2conames = `getFreeCoNames(c,M2);
        return (conameList) `conameList(M1conames*,M2conames*); 
      }
      implyL(ImplyLPrem1(_,_,M1),ImplyLPrem2(a,_,M2),n) -> {
        CoNameList M1names = `getFreeCoNames(c,M1);
        CoNameList M2names = `getFreeCoNames(conameList(a,c*),M2);
        return (conameList) `conameList(M1names*,M2names*); 
      }
      forallL(ForallLPrem1(_,_,M),_,n) -> {
        return `getFreeCoNames(c,M);
      }
      existsL(ExistsLPrem1(_,_,_,M),n) -> {
        return `getFreeCoNames(c,M);
      }
      rootL(RootLPrem1(_,_,M)) -> {
        return `getFreeCoNames(c,M);
      }
      // right rules
      orR(OrRPrem1(a,_,b,_,M),cn) -> {
        CoNameList Mconames = `getFreeCoNames(conameList(a,b,c*),M);
        return (conameList) (c.contains(`cn) ? Mconames : `conameList(cn,Mconames*)); 
      }
      andR(AndRPrem1(a,_,M1),AndRPrem2(b,_,M2),cn) -> {
        CoNameList M1conames = `getFreeCoNames(conameList(a,c*),M1);
        CoNameList M2conames = `getFreeCoNames(conameList(b,c*),M2);
        return (conameList) (c.contains(`cn)  ? `conameList(M1conames*,M2conames*) : `conameList(cn,M1conames*,M2conames*)); 
      }
      implyR(ImplyRPrem1(_,_,a,_,M),cn) -> {
        CoNameList Mconames = `getFreeCoNames(conameList(a,c*),M);
        return (conameList) (c.contains(`cn) ? Mconames : `conameList(cn,Mconames*)); 
      }
      existsR(ExistsRPrem1(a,_,M),_,cn) -> {
        CoNameList Mconames = `getFreeCoNames(conameList(a,c*),M);
        return (conameList) (c.contains(`cn) ? Mconames : `conameList(cn,Mconames*)); 
      }
      forallR(ForallRPrem1(a,_,_,M),cn) -> {
        CoNameList Mconames = `getFreeCoNames(conameList(a,c*),M);
        return (conameList) (c.contains(`cn) ? Mconames : `conameList(cn,Mconames*)); 
      }
      rootR(RootRPrem1(a,_,M)) -> {
        return `getFreeCoNames(conameList(a,c*),M);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static conameList getFreeCoNames(ProofTerm pt) {
    return `getFreeCoNames(conameList(),pt);
  }

  public static boolean conameAppears(ProofTerm pt, CoName cn) {
    return getFreeCoNames(pt).contains(cn);
  }

  public static boolean freshlyIntroducedCoName(ProofTerm pt, CoName cn) {
    return topIntroducedCoName(pt,cn) && !isImplicitContraction(pt);
  }

  public static boolean isImplicitContraction(ProofTerm pt) {
    %match (pt) {
      // left rules
      andL(AndLPrem1(x,_,y,_,M),n) -> {
        return `nameAppears(M,n) && !`x.equals(`n) && !`y.equals(`n); 
      }
      orL(OrLPrem1(x,_,M1),OrLPrem2(y,_,M2),n) -> {
        return (`nameAppears(M1,n) && !`x.equals(`n)) || (`nameAppears(M2,n) && !`y.equals(`n));
      }
      implyL(ImplyLPrem1(x,_,M1),ImplyLPrem2(a,_,M2),n) -> {
        return (`nameAppears(M1,n) && !`x.equals(`n)) || `nameAppears(M2,n);
      }
      forallL(ForallLPrem1(x,_,M),_,n) -> {
        return `nameAppears(M,n) && !`x.equals(`n);
      }
      existsL(ExistsLPrem1(x,_,_,M),n) -> {
        return `nameAppears(M,n) && ! `x.equals(`n);
      }
      // right rules
      orR(OrRPrem1(a,_,b,_,M),cn) -> {
        return `conameAppears(M,cn) && !`a.equals(`cn) && !`b.equals(`cn); 
      }
      andR(AndRPrem1(a,_,M1),AndRPrem2(b,_,M2),cn) -> {
        return (`conameAppears(M1,cn) && !`a.equals(`cn)) || (`conameAppears(M2,cn) && !`b.equals(`cn));
      }
      implyR(ImplyRPrem1(x,_,a,_,M),cn) -> {
        return `conameAppears(M,cn) && !`a.equals(`cn);
      }
      existsR(ExistsRPrem1(a,_,M),_,cn) -> {
        return `conameAppears(M,cn) && !`a.equals(`cn);
      }
      forallR(ForallRPrem1(a,_,_,M),cn) -> {
        return `conameAppears(M,cn) && ! `a.equals(`cn);
      }
    }
    return false;
  }

  private static ProofTerm ReName(ProofTerm pt, Name n1, Name n2) { 
    %match(ProofTerm pt) {
      ax(n,_) -> {
      }
      cut(CutPrem1(_,_,M1),CutPrem2(x,_,M2)) -> {
      }
      // left rules
      andL(AndLPrem1(x,_,y,_,M),n) -> {
      }
      orL(OrLPrem1(x,_,M1),OrLPrem2(y,_,M2),n) -> {
      }
      implyL(ImplyLPrem1(x,_,M1),ImplyLPrem2(_,_,M2),n) -> {
      }
      forallL(ForallLPrem1(x,_,M),_,n) -> {
      }
      existsL(ExistsLPrem1(x,_,_,M),n) -> {
      }
      rootL(RootLPrem1(x,_,M)) -> {
      }
      // right rules
      orR(OrRPrem1(_,_,_,_,M),cn) -> {
      }
      andR(AndRPrem1(_,_,M1),AndRPrem2(_,_,M2),cn) -> {
      }
      implyR(ImplyRPrem1(x,_,_,_,M),cn) -> {
      }
      existsR(ExistsRPrem1(_,_,M),_,cn) -> {
      }
      forallR(ForallRPrem1(_,_,_,M),cn) -> {
      }
      rootR(RootRPrem1(_,_,M)) -> {
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static ProofTerm ReCoName(ProofTerm pt, CoName cn1, CoName cn2) { 
    %match(ProofTerm pt) {
      ax(n,_) -> {
      }
      cut(CutPrem1(_,_,M1),CutPrem2(x,_,M2)) -> {
      }
      // left rules
      andL(AndLPrem1(x,_,y,_,M),n) -> {
      }
      orL(OrLPrem1(x,_,M1),OrLPrem2(y,_,M2),n) -> {
      }
      implyL(ImplyLPrem1(x,_,M1),ImplyLPrem2(_,_,M2),n) -> {
      }
      forallL(ForallLPrem1(x,_,M),_,n) -> {
      }
      existsL(ExistsLPrem1(x,_,_,M),n) -> {
      }
      rootL(RootLPrem1(x,_,M)) -> {
      }
      // right rules
      orR(OrRPrem1(_,_,_,_,M),cn) -> {
      }
      andR(AndRPrem1(_,_,M1),AndRPrem2(_,_,M2),cn) -> {
      }
      implyR(ImplyRPrem1(x,_,_,_,M),cn) -> {
      }
      existsR(ExistsRPrem1(_,_,M),_,cn) -> {
      }
      forallR(ForallRPrem1(_,_,_,M),cn) -> {
      }
      rootR(RootRPrem1(_,_,M)) -> {
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  /* \x.x : A -> A */
  static RawProp A = `RawrelApp("A",RawtermList());
  static RawProofTerm pt = 
    `RawrootR(
        RawRootRPrem1("a",Rawimplies(A,A),RawimplyR(
            RawImplyRPrem1("x",A,"a",A,Rawax("x","a")),
            "a")));

  public static void main(String[] args) {
    System.out.println(Pretty.pretty(pt));
    ProofTerm cpt = pt.convert();
    System.out.println(Pretty.pretty(cpt.export()));
    Pretty.setChurchStyle(false);
    System.out.println(Pretty.pretty(cpt.export()));
    System.out.println(getFreeNames(cpt));
    System.out.println(getFreeCoNames(cpt));
    System.out.println(getFreeNames(`ax(Name.freshName("x"),CoName.freshCoName("a"))));
    System.out.println(getFreeCoNames(`ax(Name.freshName("x"),CoName.freshCoName("a"))));
  }
}

