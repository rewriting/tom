package lemu2;

import tom.library.sl.*;
import lemu2.proofterms.types.*;
import lemu2.proofterms.types.namelist.nameList;
import lemu2.proofterms.types.conamelist.conameList;

import java.util.HashSet;
import java.util.Collection;

public class Evaluation {

  %include { proofterms/proofterms.tom } 
  %include { sl.tom } 

  private static boolean topIntroducedName(ProofTerm pt, Name name) { 
    %match (pt) {
      ax(n,_) -> { return `name == `n; }
      falseL(n) -> { return `name == `n; }
      andL(_,n) -> { return `name == `n; }
      orL(_,_,n) -> { return `name == `n; }
      implyL(_,_,n) -> { return `name == `n; }
      forallL(_,_,n) -> { return `name == `n; }
      existsL(_,n) -> { return `name == `n; }
      foldL(_,_,n) -> { return `name == `n; }
    }
    return false;
  }

  public static boolean topIntroducedCoName(ProofTerm pt, CoName coname) { 
    %match (pt) {
      ax(_,cn) -> { return `coname == `cn; }
      trueR(cn) -> { return `coname == `cn; }
      orR(_,cn) -> { return `coname == `cn; }
      andR(_,_,cn) -> { return `coname == `cn; }
      implyR(_,cn) -> { return `coname == `cn; }
      existsR(_,_,cn) -> { return `coname == `cn; }
      forallR(_,cn) -> { return `coname == `cn; }
      foldR(_,_,cn) -> { return `coname == `cn; }
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
      foldL(_,FoldLPrem1(x,_,M),n) -> {
        NameList Mnames = `getFreeNames(nameList(x,c*),M);
        return (nameList) (c.contains(`n) ? Mnames : `nameList(n,Mnames*)); 
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
      foldR(_,FoldRPrem1(_,_,M),cn) -> {
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
      foldL(_,FoldLPrem1(_,_,M),n) -> {
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
      foldR(_,FoldRPrem1(a,_,M),cn) -> {
        CoNameList Mconames = `getFreeCoNames(conameList(a,c*),M);
        return (conameList) (c.contains(`cn) ? Mconames : `conameList(cn,Mconames*)); 
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
      andL(AndLPrem1(x,_,y,_,M),n) -> { return `nameAppears(M,n); }
      orL(OrLPrem1(x,_,M1),OrLPrem2(y,_,M2),n) -> { return `nameAppears(M1,n) || `nameAppears(M2,n); }
      implyL(ImplyLPrem1(x,_,M1),ImplyLPrem2(a,_,M2),n) -> { return `nameAppears(M1,n) || `nameAppears(M2,n); }
      forallL(ForallLPrem1(x,_,M),_,n) -> { return `nameAppears(M,n); }
      existsL(ExistsLPrem1(x,_,_,M),n) -> { return `nameAppears(M,n); }
      foldL(_,FoldLPrem1(x,_,M),n) -> { return `nameAppears(M,n); }
      // right rules
      orR(OrRPrem1(a,_,b,_,M),cn) -> { return `conameAppears(M,cn); }
      andR(AndRPrem1(a,_,M1),AndRPrem2(b,_,M2),cn) -> { return `conameAppears(M1,cn) || `conameAppears(M2,cn); }
      implyR(ImplyRPrem1(x,_,a,_,M),cn) -> { return `conameAppears(M,cn); }
      existsR(ExistsRPrem1(a,_,M),_,cn) -> { return `conameAppears(M,cn); }
      forallR(ForallRPrem1(a,_,_,M),cn) -> { return `conameAppears(M,cn); }
      foldR(_,FoldRPrem1(a,_,M),cn) -> { return `conameAppears(M,cn); }
    }
    return false;
  }

  private static ProofTerm rename(ProofTerm pt, Name n1, Name n2) { 
    %match(pt) {
      ax(n,cn) -> {
        if (`n == n1) 
          return `ax(n2,cn);
        else 
          return `ax(n,cn); 
      }
      cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
        return `cut(CutPrem1(a,pa,rename(M1,n1,n2)),CutPrem2(x,px,rename(M2,n1,n2)));
      }
      // left rules
      andL(AndLPrem1(x,px,y,py,M),n) -> {
        if (`n == n1)
          return `andL(AndLPrem1(x,px,y,py,rename(M,n1,n2)),n2); 
        else
          return `andL(AndLPrem1(x,px,y,py,rename(M,n1,n2)),n); 
      }
      orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
        if (`n == n1)
          return `orL(OrLPrem1(x,px,rename(M1,n1,n2)),OrLPrem2(y,py,rename(M2,n1,n2)),n2);
        else
          return `orL(OrLPrem1(x,px,rename(M1,n1,n2)),OrLPrem2(y,py,rename(M2,n1,n2)),n);
      }
      implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
        if (`n == n1)
          return `implyL(ImplyLPrem1(x,px,rename(M1,n1,n2)),ImplyLPrem2(a,pa,rename(M2,n1,n2)),n2);
        else
          return `implyL(ImplyLPrem1(x,px,rename(M1,n1,n2)),ImplyLPrem2(a,pa,rename(M2,n1,n2)),n);
      }
      forallL(ForallLPrem1(x,px,M),t,n) -> {
        if (`n == n1)
          return `forallL(ForallLPrem1(x,px,rename(M,n1,n2)),t,n2);
        else
          return `forallL(ForallLPrem1(x,px,rename(M,n1,n2)),t,n);
      }
      existsL(ExistsLPrem1(x,px,fx,M),n) -> {
        if (`n == n1)
          return `existsL(ExistsLPrem1(x,px,fx,rename(M,n1,n2)),n2);
        else
          return `existsL(ExistsLPrem1(x,px,fx,rename(M,n1,n2)),n);
      }
      rootL(RootLPrem1(x,px,M)) -> {
        return `rootL(RootLPrem1(x,px,rename(M,n1,n2)));
      }
      foldL(id,FoldLPrem1(x,px,M),n) -> {
        if (`n == n1)
          return `foldL(id,FoldLPrem1(x,px,rename(M,n1,n2)),n2);
        else
          return `foldL(id,FoldLPrem1(x,px,rename(M,n1,n2)),n);
      }
      // right rules
      orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
        return `orR(OrRPrem1(a,pa,b,pb,rename(M,n1,n2)),cn);
      }
      andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
        return `andR(AndRPrem1(a,pa,rename(M1,n1,n2)),AndRPrem2(b,pb,rename(M2,n1,n2)),cn);
      }
      implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
        return `implyR(ImplyRPrem1(x,px,a,pa,rename(M,n1,n2)),cn);
      }
      existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
        return `existsR(ExistsRPrem1(a,pa,rename(M,n1,n2)),t,cn);
      }
      forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
        return `forallR(ForallRPrem1(a,pa,fx,rename(M,n1,n2)),cn);
      }
      rootR(RootRPrem1(a,pa,M)) -> {
        return `rootR(RootRPrem1(a,pa,rename(M,n1,n2)));
      }
      foldR(id,FoldRPrem1(a,pa,M),cn) -> {
        return `foldR(id,FoldRPrem1(a,pa,rename(M,n1,n2)),cn); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static ProofTerm reconame(ProofTerm pt, CoName cn1, CoName cn2) { 
    %match(pt) {
      ax(n,cn) -> {
        if (`cn.equals(cn1)) 
          return `ax(n,cn2);
        else 
          return `ax(n,cn); 
      }
      cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
        return `cut(CutPrem1(a,pa,reconame(M1,cn1,cn2)),CutPrem2(x,px,reconame(M2,cn1,cn2)));
      }
      // left rules
      andL(AndLPrem1(x,px,y,py,M),n) -> {
        return `andL(AndLPrem1(x,px,y,py,reconame(M,cn1,cn2)),n); 
      }
      orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
        return `orL(OrLPrem1(x,px,reconame(M1,cn1,cn2)),OrLPrem2(y,py,reconame(M2,cn1,cn2)),n);
      }
      implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
        return `implyL(ImplyLPrem1(x,px,reconame(M1,cn1,cn2)),ImplyLPrem2(a,pa,reconame(M2,cn1,cn2)),n);
      }
      forallL(ForallLPrem1(x,px,M),t,n) -> {
        return `forallL(ForallLPrem1(x,px,reconame(M,cn1,cn2)),t,n);
      }
      existsL(ExistsLPrem1(x,px,fx,M),n) -> {
        return `existsL(ExistsLPrem1(x,px,fx,reconame(M,cn1,cn2)),n);
      }
      rootL(RootLPrem1(x,px,M)) -> {
        return `rootL(RootLPrem1(x,px,reconame(M,cn1,cn2)));
      }
      foldL(id,FoldLPrem1(x,px,M),n) -> {
        return `foldL(id,FoldLPrem1(x,px,reconame(M,cn1,cn2)),n);
      }
      // right rules
      orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
        if (`cn == cn1)
          return `orR(OrRPrem1(a,pa,b,pb,reconame(M,cn1,cn2)),cn2);
        else
          return `orR(OrRPrem1(a,pa,b,pb,reconame(M,cn1,cn2)),cn);
      }
      andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
        if (`cn == cn1)
          return `andR(AndRPrem1(a,pa,reconame(M1,cn1,cn2)),AndRPrem2(b,pb,reconame(M2,cn1,cn2)),cn2);
        else
          return `andR(AndRPrem1(a,pa,reconame(M1,cn1,cn2)),AndRPrem2(b,pb,reconame(M2,cn1,cn2)),cn);
      }
      implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
        if (`cn == cn1)
          return `implyR(ImplyRPrem1(x,px,a,pa,reconame(M,cn1,cn2)),cn2);
        else
          return `implyR(ImplyRPrem1(x,px,a,pa,reconame(M,cn1,cn2)),cn);
      }
      existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
        if (`cn == cn1)
          return `existsR(ExistsRPrem1(a,pa,reconame(M,cn1,cn2)),t,cn2);
        else
          return `existsR(ExistsRPrem1(a,pa,reconame(M,cn1,cn2)),t,cn);
      }
      forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
        if (`cn == cn1)
          return `forallR(ForallRPrem1(a,pa,fx,reconame(M,cn1,cn2)),cn2);
        else
          return `forallR(ForallRPrem1(a,pa,fx,reconame(M,cn1,cn2)),cn);
      }
      rootR(RootRPrem1(a,pa,M)) -> {
        return `rootR(RootRPrem1(a,pa,reconame(M,cn1,cn2)));
      }
      foldR(id,FoldRPrem1(a,pa,M),cn) -> {
        if (`cn == cn1)
          return `foldR(id,FoldRPrem1(a,pa,reconame(M,cn1,cn2)),cn2); 
        else
          return `foldR(id,FoldRPrem1(a,pa,reconame(M,cn1,cn2)),cn); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static ProofTerm substName(ProofTerm pt, Name n1, CoName cn1, Prop prop, ProofTerm P) {
    %match(pt, Name n1) {
      ax(n,cn), n -> {
        return `reconame(P,cn1,cn);
      }
      andL(AndLPrem1(x,px,y,py,M),n), n -> {
        return `cut(
            CutPrem1(cn1,prop,P),
            CutPrem2(n1,prop,andL(
                AndLPrem1(x,px,y,py,substName(M,n1,cn1,prop,P)),n1)));
      }
      orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n), n -> {
        return `cut(
            CutPrem1(cn1,prop,P),
            CutPrem2(n1,prop,orL(
                OrLPrem1(x,px,substName(M1,n1,cn1,prop,P)),
                OrLPrem2(y,py,substName(M2,n1,cn1,prop,P)),n1)));
      }
      implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n), n -> {
        return `cut(
            CutPrem1(cn1,prop,P),
            CutPrem2(n1,prop,implyL(
                ImplyLPrem1(x,px,substName(M1,n1,cn1,prop,P)),
                ImplyLPrem2(a,pa,substName(M2,n1,cn1,prop,P)),n1)));
      }
      forallL(ForallLPrem1(x,px,M),t,n), n -> {
        return `cut(
            CutPrem1(cn1,prop,P),
            CutPrem2(n1,prop,forallL(
                ForallLPrem1(x,px,substName(M,n1,cn1,prop,P)),t,n1))); 
      }
      existsL(ExistsLPrem1(x,px,fx,M),n), n -> {
        return `cut(
            CutPrem1(cn1,prop,P),
            CutPrem2(n1,prop,existsL(
                ExistsLPrem1(x,px,fx,substName(M,n1,cn1,prop,P)),n1))); 
      }
      foldL(id,FoldLPrem1(x,px,M),n), n -> {
        return `cut(
            CutPrem1(cn1,prop,P),
            CutPrem2(n1,prop,foldL(id,
                FoldLPrem1(x,px,substName(M,n1,cn1,prop,P)),n1)));
      }
    }
    %match(pt) { // if introduced name different from n1
      ax(n,cn) -> {
        return `ax(n,cn); 
      }
      cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
        return `cut(
            CutPrem1(a,pa,substName(M1,n1,cn1,prop,P)),
            CutPrem2(x,px,substName(M2,n1,cn1,prop,P)));
      }
      // left rules
      andL(AndLPrem1(x,px,y,py,M),n) -> {
        return `andL(
            AndLPrem1(x,px,y,py,substName(M,n1,cn1,prop,P)),n); 
      }
      orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
        return `orL(
            OrLPrem1(x,px,substName(M1,n1,cn1,prop,P)),
            OrLPrem2(y,py,substName(M2,n1,cn1,prop,P)),n);
      }
      implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
        return `implyL(
            ImplyLPrem1(x,px,substName(M1,n1,cn1,prop,P)),
            ImplyLPrem2(a,pa,substName(M2,n1,cn1,prop,P)),n);
      }
      forallL(ForallLPrem1(x,px,M),t,n) -> {
        return `forallL(
            ForallLPrem1(x,px,substName(M,n1,cn1,prop,P)),t,n);
      }
      existsL(ExistsLPrem1(x,px,fx,M),n) -> {
        return `existsL(
            ExistsLPrem1(x,px,fx,substName(M,n1,cn1,prop,P)),n);
      }
      foldL(id,FoldLPrem1(x,px,M),n) -> {
        return `foldL(id,
            FoldLPrem1(x,px,substName(M,n1,cn1,prop,P)),n);
      }
      // right rules
      orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
        return `orR(
            OrRPrem1(a,pa,b,pb,substName(M,n1,cn1,prop,P)),cn);
      }
      andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
        return `andR(
            AndRPrem1(a,pa,substName(M1,n1,cn1,prop,P)),
            AndRPrem2(b,pb,substName(M2,n1,cn1,prop,P)),cn);
      }
      implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
        return `implyR(
            ImplyRPrem1(x,px,a,pa,substName(M,n1,cn1,prop,P)),cn);
      }
      existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
        return `existsR(
            ExistsRPrem1(a,pa,substName(M,n1,cn1,prop,P)),t,cn);
      }
      forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
        return `forallR(
            ForallRPrem1(a,pa,fx,substName(M,n1,cn1,prop,P)),cn);
      }
      foldR(id,FoldRPrem1(a,pa,M),cn) -> {
        return `foldR(id,
            FoldRPrem1(a,pa,substName(M,n1,cn1,prop,P)),cn);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static ProofTerm substCoName(ProofTerm pt, CoName cn1, Name n1, Prop prop, ProofTerm P) {
    %match(pt, CoName cn1) {
      ax(n,cn), cn -> {
        return `rename(P,n1,n);
      }
      orR(OrRPrem1(a,pa,b,pb,M),cn), cn -> {
        return `cut(
            CutPrem1(cn1,prop,orR(
                OrRPrem1(a,pa,b,pb,substCoName(M,cn1,n1,prop,P)),cn1)),
            CutPrem2(n1,prop,P));
      }
      andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn), cn -> {
        return `cut(
            CutPrem1(cn1,prop,andR(
                AndRPrem1(a,pa,substCoName(M1,cn1,n1,prop,P)),
                AndRPrem2(b,pb,substCoName(M2,cn1,n1,prop,P)),cn1)),
            CutPrem2(n1,prop,P));
      }
      implyR(ImplyRPrem1(x,px,a,pa,M),cn), cn -> {
        return `cut(
            CutPrem1(cn1,prop,implyR(
                ImplyRPrem1(x,px,a,pa,substCoName(M,cn1,n1,prop,P)),cn1)),
            CutPrem2(n1,prop,P));
      }
      existsR(ExistsRPrem1(a,pa,M),t,cn), cn -> {
        return `cut(
            CutPrem1(cn1,prop,existsR(
                ExistsRPrem1(a,pa,substCoName(M,cn1,n1,prop,P)),t,cn1)),
            CutPrem2(n1,prop,P));
      }
      forallR(ForallRPrem1(a,pa,fx,M),cn), cn -> {
        return `forallR(
            ForallRPrem1(a,pa,fx,substCoName(M,cn1,n1,prop,P)),cn);
      }
      foldR(id,FoldRPrem1(a,pa,M),cn), cn -> {
        return `cut(
            CutPrem1(cn1,prop,foldR(id,
                FoldRPrem1(a,pa,substCoName(M,cn1,n1,prop,P)),cn1)),
            CutPrem2(n1,prop,P));
      }
    }
    %match(pt) { // if introduced coname different from cn1
      ax(n,cn) -> {
        return `ax(n,cn); 
      }
      cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
        return `cut(
            CutPrem1(a,pa,substCoName(M1,cn1,n1,prop,P)),
            CutPrem2(x,px,substCoName(M2,cn1,n1,prop,P)));
      }
      // left rules
      andL(AndLPrem1(x,px,y,py,M),n) -> {
        return `andL(
            AndLPrem1(x,px,y,py,substCoName(M,cn1,n1,prop,P)),n); 
      }
      orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
        return `orL(
            OrLPrem1(x,px,substCoName(M1,cn1,n1,prop,P)),
            OrLPrem2(y,py,substCoName(M2,cn1,n1,prop,P)),n);
      }
      implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
        return `implyL(
            ImplyLPrem1(x,px,substCoName(M1,cn1,n1,prop,P)),
            ImplyLPrem2(a,pa,substCoName(M2,cn1,n1,prop,P)),n);
      }
      forallL(ForallLPrem1(x,px,M),t,n) -> {
        return `forallL(
            ForallLPrem1(x,px,substCoName(M,cn1,n1,prop,P)),t,n);
      }
      existsL(ExistsLPrem1(x,px,fx,M),n) -> {
        return `existsL(
            ExistsLPrem1(x,px,fx,substCoName(M,cn1,n1,prop,P)),n);
      }
      foldL(id,FoldLPrem1(x,px,M),n) -> {
        return `foldL(id,
            FoldLPrem1(x,px,substCoName(M,cn1,n1,prop,P)),n);
      }
      // right rules
      orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
        return `orR(
            OrRPrem1(a,pa,b,pb,substCoName(M,cn1,n1,prop,P)),cn);
      }
      andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
        return `andR(
            AndRPrem1(a,pa,substCoName(M1,cn1,n1,prop,P)),
            AndRPrem2(b,pb,substCoName(M2,cn1,n1,prop,P)),cn);
      }
      implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
        return `implyR(
            ImplyRPrem1(x,px,a,pa,substCoName(M,cn1,n1,prop,P)),cn);
      }
      existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
        return `existsR(
            ExistsRPrem1(a,pa,substCoName(M,cn1,n1,prop,P)),t,cn);
      }
      forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
        return `forallR(
            ForallRPrem1(a,pa,fx,substCoName(M,cn1,n1,prop,P)),cn);
      }
      foldR(id,FoldRPrem1(a,pa,M),cn) -> {
        return `foldR(id,
            FoldRPrem1(a,pa,substCoName(M,cn1,n1,prop,P)),cn);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  %strategy SubstFoVarInTerm(FoVar x, Term u) extends Identity() {
    visit Term {
     var(y) && y << FoVar x -> { return `u; }
    }
  }

  private static Term substFoVar(Term t, FoVar x, Term u) {
    try { return (Term) `TopDown(SubstFoVarInTerm(x,u)).visit(t); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  private static TermList substFoVar(TermList tl, FoVar x, Term u) {
    %match(tl) {
      termList() -> { return `EmptytermList(); }
      termList(t,ts*) -> { return `ConstermList(substFoVar(t,x,u),substFoVar(ts,x,u)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Prop substFoVar(Prop prop, FoVar x, Term t) {
    %match(prop) {
      relApp(r,tl) -> { return `relApp(r,substFoVar(tl,x,t)); }
      and(p,q) -> { return `and(substFoVar(p,x,t),substFoVar(q,x,t)); }
      or(p,q) -> { return `or(substFoVar(p,x,t),substFoVar(q,x,t)); }
      implies(p,q) -> { return `implies(substFoVar(p,x,t),substFoVar(q,x,t)); }
      forall(Fa(x,p)) -> { return `forall(Fa(x,substFoVar(p,x,t))); }
      exists(Ex(x,p)) -> { return `exists(Ex(x,substFoVar(p,x,t))); }
      bottom() -> { return `bottom(); }
      top() -> { return `top(); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static ProofTerm substFoVar(ProofTerm pt, FoVar x1, Term t1) {
    %match(pt) { 
      ax(n,cn) -> {
        return `ax(n,cn); 
      }
      cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
        return `cut(
            CutPrem1(a,substFoVar(pa,x1,t1),substFoVar(M1,x1,t1)),
            CutPrem2(x,substFoVar(px,x1,t1),substFoVar(M2,x1,t1)));
      }
      // left rules
      andL(AndLPrem1(x,px,y,py,M),n) -> {
        return `andL(AndLPrem1( x,substFoVar(px,x1,t1), y,substFoVar(py,x1,t1), substFoVar(M,x1,t1)),n); 
      }
      orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
        return `orL(
            OrLPrem1(x,substFoVar(px,x1,t1),substFoVar(M1,x1,t1)),
            OrLPrem2(y,substFoVar(py,x1,t1),substFoVar(M2,x1,t1)),n);
      }
      implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
        return `implyL(
            ImplyLPrem1(x,substFoVar(px,x1,t1),substFoVar(M1,x1,t1)),
            ImplyLPrem2(a,substFoVar(pa,x1,t1),substFoVar(M2,x1,t1)),n);
      }
      forallL(ForallLPrem1(x,px,M),t,n) -> {
        return `forallL(ForallLPrem1(x,substFoVar(px,x1,t1),substFoVar(M,x1,t1)),substFoVar(t,x1,t1),n);
      }
      existsL(ExistsLPrem1(x,px,fx,M),n) -> {
        return `existsL(ExistsLPrem1(x,substFoVar(px,x1,t1),fx,substFoVar(M,x1,t1)),n);
      }
      foldL(id,FoldLPrem1(x,px,M),n) -> {
        return `foldL(id,FoldLPrem1(x,substFoVar(px,x1,t1),substFoVar(M,x1,t1)),n);
      }
      // right rules
      orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
        return `orR(OrRPrem1(a,substFoVar(pa,x1,t1),b,substFoVar(pb,x1,t1),substFoVar(M,x1,t1)),cn);
      }
      andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
        return `andR(
            AndRPrem1(a,substFoVar(pa,x1,t1),substFoVar(M1,x1,t1)),
            AndRPrem2(b,substFoVar(pb,x1,t1),substFoVar(M2,x1,t1)),cn);
      }
      implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
        return `implyR(ImplyRPrem1(x,substFoVar(px,x1,t1),a,substFoVar(pa,x1,t1),substFoVar(M,x1,t1)),cn);
      }
      existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
        return `existsR(ExistsRPrem1(a,substFoVar(pa,x1,t1),substFoVar(M,x1,t1)),substFoVar(t,x1,t1),cn);
      }
      forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
        return `forallR(ForallRPrem1(a,substFoVar(pa,x1,t1),fx,substFoVar(M,x1,t1)),cn);
      }
      foldR(id,FoldRPrem1(x,px,M),cn) -> {
        return `foldR(id,FoldRPrem1(x,substFoVar(px,x1,t1),substFoVar(M,x1,t1)),cn);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  %typeterm PTCollection { implement { java.util.Collection<ProofTerm> } }

  public static ProofTerm subst(Position w, ProofTerm orig, ProofTerm st) {
    try { return (ProofTerm) w.getReplace(st).visit(orig); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  %strategy RStep(c:PTCollection, last:ProofTerm) extends Identity() {
    visit ProofTerm {
      // commuting cut -- non determinism
      cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
        if (!`freshlyIntroducedCoName(M1,a)) 
          c.add(subst(getPosition(),last,
                `substCoName(M1,a,x,pa,M2)));
        if (!`freshlyIntroducedName(M2,x))
          c.add(subst(getPosition(),last,
                `substName(M2,x,a,px,M1)));
      }
      // axiom cuts -- non determinism 
      cut(CutPrem1(a,pa,M),CutPrem2(x,px,ax(x,b))) -> {
        if (`freshlyIntroducedCoName(M,a)) 
          c.add(subst(getPosition(),last,
                `reconame(M,a,b)));
      }
      cut(CutPrem1(a,pa,ax(y,a)),CutPrem2(x,px,M)) -> {
        if (`freshlyIntroducedName(M,x)) 
          c.add(subst(getPosition(),last,
                `rename(M,x,y)));
      }
      // top and bottom cuts
      cut(CutPrem1(a,pa,trueR(a)),CutPrem2(x,px,M)) -> {
        if (`freshlyIntroducedName(M,x))
          c.add(subst(getPosition(),last,`M));
      }
      cut(CutPrem1(a,pa,M),CutPrem2(x,px,falseL(x))) -> {
        if (`freshlyIntroducedCoName(M,a))
          c.add(subst(getPosition(),last,`M));
      }
      // => cut -- non determinism
      cut(
          CutPrem1(b,pb,p1@implyR(ImplyRPrem1(x,px,a,pa,M),b)),
          CutPrem2(z,pz,p2@implyL(ImplyLPrem1(y,py,P),ImplyLPrem2(c,pc,N),z))) -> {
        if (`freshlyIntroducedCoName(p1,b) && `freshlyIntroducedName(p2,z)) {
          c.add(subst(getPosition(),last,
                `cut(CutPrem1(a,pa,cut(CutPrem1(c,pc,N),CutPrem2(x,px,M))),CutPrem2(y,py,P))));
          c.add(subst(getPosition(),last,
                `cut(CutPrem1(c,pc,N),CutPrem2(x,px,cut(CutPrem1(a,pa,M),CutPrem2(y,py,P))))));
        }
      }
      // /\ cut -- non determinism
      cut(
          CutPrem1(a,pa,p1@andR(AndRPrem1(b,pb,M1),AndRPrem2(c,pc,M2),a)),
          CutPrem2(x,px,p2@andL(AndLPrem1(y,py,z,pz,N),x))) -> {
        if (`freshlyIntroducedCoName(p1,a) && `freshlyIntroducedName(p2,x)) {
          c.add(subst(getPosition(),last,
                `cut(CutPrem1(b,pb,M1),CutPrem2(y,py,cut(CutPrem1(c,pc,M2),CutPrem2(z,pz,N))))));
          c.add(subst(getPosition(),last,
                `cut(CutPrem1(c,pc,M1),CutPrem2(z,pz,cut(CutPrem1(b,pb,M1),CutPrem2(y,py,N))))));
        }
      }
      // \/ cut -- non determinism
      cut(
          CutPrem1(a,pa,p1@orR(OrRPrem1(b,pb,c,pc,M),a)),
          CutPrem2(x,px,p2@orL(OrLPrem1(y,py,N1),OrLPrem2(z,pz,N2),x))) -> {
        if (`freshlyIntroducedCoName(p1,a) && `freshlyIntroducedName(p2,x)) {
          c.add(subst(getPosition(),last,
                `cut(CutPrem1(b,pb,cut(CutPrem1(c,pc,M),CutPrem2(z,pz,N2))),CutPrem2(y,py,N1))));
          c.add(subst(getPosition(),last,
                `cut(CutPrem1(c,pc,cut(CutPrem1(b,pb,M),CutPrem2(y,py,N1))),CutPrem2(z,pz,N2))));
        }
      }
      // first-order cuts
      cut(
          CutPrem1(b,pb,p1@existsR(ExistsRPrem1(a,pa,M),t,b)),
          CutPrem2(y,py,p2@existsL(ExistsLPrem1(x,px,fx,N),y))) -> {
        if (`freshlyIntroducedCoName(p1,b) && `freshlyIntroducedName(p2,y))
          c.add(subst(getPosition(),last,
                `cut(CutPrem1(a,pa,M),CutPrem2(x,substFoVar(px,fx,t),substFoVar(N,fx,t)))));

      }
      cut(
          CutPrem1(b,pb,p1@forallR(ForallRPrem1(a,pa,fx,M),b)),
          CutPrem2(y,py,p2@forallL(ForallLPrem1(x,px,N),t,y))) -> {
        if (`freshlyIntroducedCoName(p1,b) && `freshlyIntroducedName(p2,y))
          c.add(subst(getPosition(),last,
                `cut(CutPrem1(a,substFoVar(pa,fx,t),substFoVar(M,fx,t)),CutPrem2(x,px,N))));
      }
      // fold cuts
      cut(
          CutPrem1(a,pa,p1@foldR(id,FoldRPrem1(b,pb,M),a)),
          CutPrem2(x,px,p2@foldL(id,FoldLPrem1(y,py,N),x))) -> {
        if (`freshlyIntroducedCoName(p1,a) && `freshlyIntroducedName(p2,x)) {
          c.add(subst(getPosition(),last,
                `cut(CutPrem1(b,pb,M),CutPrem2(y,py,N))));
        }
      }
    }
  }

  static Collection<ProofTerm> reduce(ProofTerm pt) {
    Collection<ProofTerm> res = new HashSet<ProofTerm>();
    res.add(pt);
    Collection<ProofTerm> nf = new HashSet<ProofTerm>();
    Collection<ProofTerm> tmp = new HashSet<ProofTerm>();
    Collection<ProofTerm> tmpres = new HashSet<ProofTerm>();
    do {
      tmpres.clear();
      for(ProofTerm t : res) {
        tmp.clear();
        try { `TopDown(RStep(tmp,t)).visit(t); }
        catch (VisitFailure e) { throw new RuntimeException("never happens"); }
        if (tmp.isEmpty()) {
          boolean in_nf = false;
          for (ProofTerm nft: nf) {
            if (nft.equals(t)) {
              in_nf = true;
              break;
            }
          }
          if (!in_nf)  nf.add(t);
        }
        else tmpres.addAll(tmp);
      }
      res.addAll(tmpres);
      System.out.println("res.size() = " + res.size());
    } while(res.size()>0);
    return nf;
  }
}

