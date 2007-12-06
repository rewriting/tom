package lemu;

import tom.library.sl.*;
import java.util.*;

import lemu.urban.types.*;

public class DBProofterms {
  %include{ urban/urban.tom }
  %include{ sl.tom }

  private static class SymbolTable<T> {
    private Stack<T> stack = new Stack<T>();
    private HashSet<T> res = new HashSet<T>();
    public void push(T var) { stack.push(var); }
    public void pop() { stack.pop(); }
    public void add(T var) { if(!stack.contains(var)) res.add(var); }
    public Set<T> getFreeVars() { return res; }
    public String toString() { return res.toString(); }
  }

  private static void getFreeNames(ProofTerm term, SymbolTable<Name> nst, SymbolTable<CoName> cnst) {
    %match(term) {
      ax(n,cn) -> {
        nst.add(`n);
        cnst.add(`cn);
      }
      cut(cnprop(a,_),m1,nprop(x,_),m2) -> {
        cnst.push(`a);
        `getFreeNames(m1,nst,cnst);
        cnst.pop();
        nst.push(`x);
        `getFreeNames(m2,nst,cnst);
        nst.pop();
      }
      falseL(n) -> { 
        nst.add(`n);
      }
      trueR(cn) -> { }
      andR(cnprop(a,_),m1,cnprop(b,_),m2,cn) -> {
        cnst.add(`cn);
        cnst.push(`a);
        `getFreeNames(m1,nst,cnst);
        cnst.pop();
        cnst.push(`b);
        `getFreeNames(m2,nst,cnst);
        cnst.pop();
      }
      andL(nprop(x,_),nprop(y,_),m,n) -> { 
        nst.add(`n);
        nst.push(`x);
        nst.push(`y);
        `getFreeNames(m,nst,cnst);
        nst.pop();
        nst.pop();
      }
      orR(cnprop(a,_),cnprop(b,_),m,cn) -> {
        cnst.add(`cn);
        cnst.push(`a);
        cnst.push(`b);
        `getFreeNames(m,nst,cnst);
        cnst.pop();
        cnst.pop();
      }
      orL(nprop(x,_),m1,nprop(y,_),m2,n) -> { 
        nst.add(`n);
        nst.push(`x);
        `getFreeNames(m1,nst,cnst);
        nst.pop();
        nst.push(`y);
        `getFreeNames(m2,nst,cnst);
        nst.pop();
      }
      implyR(nprop(x,_),cnprop(a,_),m1,cn) -> { 
        cnst.add(`cn);
        nst.push(`x);
        cnst.push(`a);
        `getFreeNames(m1,nst,cnst);
        cnst.pop();
        nst.pop();
      }
      implyL(cnprop(a,_),m1,nprop(x,_),m2,n) -> { 
        nst.add(`n);
        cnst.push(`a);
        `getFreeNames(m1,nst,cnst);
        cnst.pop();
        nst.push(`x);
        `getFreeNames(m2,nst,cnst);
        nst.pop();
      }
      existsR(cnprop(a,_),m,t,cn) -> { 
        cnst.add(`cn);
        cnst.push(`a);
        `getFreeNames(m,nst,cnst);
        cnst.pop();
      }
      existsL(nprop(x,_),varx,m,n) -> { 
        nst.add(`n);
        nst.push(`x);
        `getFreeNames(m,nst,cnst);
        nst.pop();
      }
      forallR(cnprop(a,_),varx,m,cn) -> { 
        cnst.add(`cn);
        cnst.push(`a);
        `getFreeNames(m,nst,cnst);
        cnst.pop();
      }
      forallL(nprop(x,_),m,t,n) -> { 
        nst.add(`n);
        nst.push(`x);
        `getFreeNames(m,nst,cnst);
        nst.pop();
      }
      foldR(cnprop(a,_),m,cn,rulenum) -> { 
        cnst.add(`cn);
        cnst.push(`a);
        `getFreeNames(m,nst,cnst);
        cnst.pop();
      }
      foldL(nprop(x,_),m,n,rulenum) -> { 
        nst.add(`n);
        nst.push(`x);
        `getFreeNames(m,nst,cnst);
        cnst.pop();
      }
      metaVar(mv) -> { 
      }
    }
  }

  public static DBProofTerm translate(ProofTerm term) {
    SymbolTable<Name> nst = new SymbolTable<Name>();
    SymbolTable<CoName> cnst = new SymbolTable<CoName>();
    getFreeNames(term,nst,cnst);

    HashMap<Name,Integer> nmap = new HashMap<Name,Integer>();
    HashMap<CoName,Integer> cnmap = new HashMap<CoName,Integer>();
    int i = 0; for(Name n: nst.getFreeVars()) { nmap.put(n,--i); }
    i = 0; for(CoName n: cnst.getFreeVars()) { cnmap.put(n,--i); }
    return translate(term, nmap, cnmap, 0);
  }

  private static DBProofTerm 
    translate(ProofTerm term, HashMap<Name,Integer> names, 
        HashMap<CoName,Integer> conames, int deep) {
      %match(term) {
        ax(n,cn) -> {
          int dbn = deep - names.get(`n) -1;
          int dbcn = deep - conames.get(`cn) -1;
          return `DBax(dbname(dbn),dbconame(dbcn));
        }
        cut(cnprop(a,p1),m1,nprop(x,p2),m2) -> {
          conames.put(`a,deep);
          names.put(`x,deep);
          return `DBcut(p1,translate(m1,names,conames,deep+1)
              ,p2,translate(m2,names,conames,deep+1));
        }
        falseL(n) -> { 
          int dbn = deep - names.get(`n) -1;
          return `DBfalseL(dbname(dbn));
        }
        trueR(cn) -> {
          int dbcn = deep - conames.get(`cn) -1;
          return `DBtrueR(dbconame(dbcn));
        }
        andR(cnprop(a,p1),m1,cnprop(b,p2),m2,cn) -> {
          int dbcn = deep - conames.get(`cn) -1;
          conames.put(`a,deep);
          conames.put(`b,deep);
          return `DBandR(p1,translate(m1,names,conames,deep+1),
              p2,translate(m2,names,conames,deep+1),dbconame(dbcn));
        }
        andL(nprop(x,p1),nprop(y,p2),m,n) -> { 
          int dbn = deep - names.get(`n) -1;
          names.put(`x,deep);
          names.put(`y,deep);
          return `DBandL(p1,p2,
              translate(m,names,conames,deep+1),dbname(dbn));
        }
        orR(cnprop(a,p1),cnprop(b,p2),m,cn) -> { 
          int dbcn = deep - conames.get(`cn) -1;
          conames.put(`a,deep);
          conames.put(`b,deep);
          return `DBorR(p1,p2,
              translate(m,names,conames,deep+1),dbconame(dbcn));
        }
        orL(nprop(x,p1),m1,nprop(y,p2),m2,n) -> { 
          int dbn = deep - names.get(`n) -1;
          names.put(`x,deep);
          names.put(`y,deep);
          return `DBorL(p1,translate(m1,names,conames,deep+1),
              p2,translate(m2,names,conames,deep+1),dbname(dbn));
        }
        implyR(nprop(x,p1),cnprop(a,p2),m1,cn) -> { 
          conames.put(`a,deep);
          names.put(`x,deep);
          int dbcn = deep - conames.get(`cn) -1;
          return `DBimplyR(p1,p2,
              translate(m1,names,conames,deep+1),dbconame(dbcn));
        }
        implyL(cnprop(a,p1),m1,nprop(x,p2),m2,n) -> { 
          conames.put(`a,deep);
          names.put(`x,deep);
          int dbn = deep - names.get(`n) -1;
          return `DBimplyL(p1, translate(m1,names,conames,deep+1),
              p2,translate(m2,names,conames,deep+1),dbname(dbn));
        }
        existsR(cnprop(a,p1),m,t,cn) -> { 
          conames.put(`a,deep);
          int dbcn = deep - conames.get(`cn) -1;
          return `DBexistsR(p1, translate(m,names,conames,deep+1),
              t,dbconame(dbcn));
        }
        existsL(nprop(x,p1),varx,m,n) -> { 
          names.put(`x,deep);
          int dbn = deep - names.get(`n) -1;
          return `DBexistsL(p1,varx,
              translate(m,names,conames,deep+1),dbname(dbn));
        }
        forallR(cnprop(a,p1),varx,m,cn) -> { 
          conames.put(`a,deep);
          int dbcn = deep - conames.get(`cn) -1;
          return `DBforallR(p1,varx,
              translate(m,names,conames,deep+1),dbconame(dbcn));
        }
        forallL(nprop(x,p1),m,t,n) -> { 
          names.put(`x,deep);
          int dbn = deep - names.get(`n) -1;
          return `DBforallL(p1,translate(m,names,conames,deep+1),
              t,dbname(dbn));
        }
        foldR(cnprop(a,p1),m,cn,rulenum) -> { 
          conames.put(`a,deep);
          int dbcn = deep - conames.get(`cn) -1;
          return `DBfoldR(p1,translate(m,names,conames,deep+1),
              dbconame(dbcn),rulenum);
        }
        foldL(nprop(x,p1),m,n,rulenum) -> { 
          names.put(`x,deep);
          int dbn = deep - names.get(`n) -1;
          return `DBfoldL(p1,translate(m,names,conames,deep+1),
              dbname(dbn),rulenum);
        }
        metaVar(mvar(name)) -> {
          return `DBmetaVar(dbmvar(name));
        }
      }
      return null;
    }

  private static DBProofTerm
    liftName(DBProofTerm term, int d, int c) {
      %match(term) {
        t@DBax(dbname(i),cn) -> { 
          if(`i<c) return `t;
          else return `DBax(dbname(i+d),cn);
        }
        DBcut(a,m1,x,m2) -> { 
          return `DBcut(a,liftName(m1,d,c),x,liftName(m2,d,c+1));
        }
        t@DBfalseL(dbname(i)) -> { 
          if(`i<c) return `t;
          else return `DBfalseL(dbname(i+d));
        }
        t@DBtrueR(cn) -> {
          return `t;
        }
        DBandR(a,m1,b,m2,cn) -> { 
          return `DBandR(a,liftName(m1,d,c),b,liftName(m2,d,c),cn);
        }
        DBandL(x,y,m,n@dbname(i)) -> { 
          if(`i<c) return `DBandL(x,y,liftName(m,d,c+1),n);
          else return `DBandL(x,y,liftName(m,d,c+1),dbname(i+d));
        }
        DBorR(a,b,m,cn) -> { 
          return `DBorR(a,b,liftName(m,d,c),cn);
        }
        DBorL(x,m1,y,m2,n@dbname(i)) -> { 
          if(`i<c) return `DBorL(x,liftName(m1,d,c+1),y,liftName(m2,d,c+1),n);
          else return `DBorL(x,liftName(m1,d,c+1),y,liftName(m2,d,c+1),dbname(i+d));
        }
        DBimplyR(x,a,m1,cn) -> {  
          return `DBimplyR(x,a,liftName(m1,d,c+1),cn);
        }
        DBimplyL(a,m1,x,m2,n@dbname(i)) -> { 
          if(`i<c) return `DBimplyL(a,liftName(m1,d,c),x,liftName(m2,d,c+1),n);
          else return `DBimplyL(a,liftName(m1,d,c),x,liftName(m2,d,c+1),dbname(i+d));
        }
        DBexistsR(a,m,t,cn) -> { 
          return `DBexistsR(a,liftName(m,d,c),t,cn);
        }
        DBexistsL(x,varx,m,n@dbname(i)) -> { 
          if(`i<c) return `DBexistsL(x,varx,liftName(m,d,c+1),n);
          else return `DBexistsL(x,varx,liftName(m,d,c+1),dbname(i+d));
        }
        DBforallR(a,varx,m,cn) -> { 
          return `DBforallR(a,varx,liftName(m,d,c),cn);
        }
        DBforallL(x,m,t,n@dbname(i)) -> { 
          if(`i<c) return `DBforallL(x,liftName(m,d,c+1),t,n);
          else return `DBforallL(x,liftName(m,d,c+1),t,dbname(i+d));
        }
        DBfoldR(a,m,cn,rulenum) -> { 
          return `DBfoldR(a,liftName(m,d,c),cn,rulenum);
        }
        DBfoldL(x,m,n@dbname(i),rulenum) -> { 
          if(`i<c) return `DBfoldL(x,liftName(m,d,c+1),n,rulenum);
          else return `DBfoldL(x,liftName(m,d,c+1),dbname(i+d),rulenum);
        }
        DBmetaVar(mv) -> { 
        }
      }
      throw new RuntimeException("should not happen");
    }

  public static DBProofTerm
    liftCoName(DBProofTerm term, int d, int c) {
      %match(term) {
        t@DBax(n,dbconame(i)) -> { 
          if(`i<c) return `t;
          else return `DBax(n,dbconame(i+d));
        }
        DBcut(a,m1,x,m2) -> { 
          return `DBcut(a,liftCoName(m1,d,c+1),x,liftCoName(m2,d,c));
        }
        t@DBfalseL(n) -> { 
          return `t;
        }
        t@DBtrueR(dbconame(i)) -> {
          if(`i<c) return `t;
          else return `DBtrueR(dbconame(i+d));
        }
        DBandR(a,m1,b,m2,cn@dbconame(i)) -> { 
          if(`i<c) return `DBandR(a,liftCoName(m1,d,c+1),b,liftCoName(m2,d,c+1),cn); 
          return `DBandR(a,liftCoName(m1,d,c+1),b,liftCoName(m2,d,c+1),dbconame(i+d));
        }
        DBandL(x,y,m,n) -> { 
          return `DBandL(x,y,liftCoName(m,d,c),n);
        }
        DBorR(a,b,m,cn@dbconame(i)) -> { 
          if(`i<c) return `DBorR(a,b,liftCoName(m,d,c+1),cn); 
          return `DBorR(a,b,liftCoName(m,d,c+1),dbconame(i+d)); 
        }
        DBorL(x,m1,y,m2,n) -> { 
          return `DBorL(x,liftCoName(m1,d,c),y,liftCoName(m2,d,c),n);
        }
        DBimplyR(x,a,m1,cn@dbconame(i)) -> {
          if(`i<c) return `DBimplyR(x,a,liftCoName(m1,d,c+1),cn); 
          return `DBorR(x,a,liftCoName(m1,d,c+1),dbconame(i+d)); 
        }
        DBimplyL(a,m1,x,m2,n) -> {
          return `DBimplyL(a,liftCoName(m1,d,c+1),x,liftCoName(m2,d,c),n);
        }
        DBexistsR(a,m,t,cn@dbconame(i)) -> { 
          if(`i<c)`DBexistsR(a,liftCoName(m,d,c+1),t,cn); 
          return `DBexistsR(a,liftCoName(m,d,c+1),t,dbconame(i+d));
        }
        DBexistsL(x,varx,m,n) -> { 
          return `DBexistsL(x,varx,liftCoName(m,d,c),n);
        }
        DBforallR(a,varx,m,cn@dbconame(i)) -> { 
          if(`i<c) `DBforallR(a,varx,liftCoName(m,d,c+1),cn);
          else return `DBforallR(a,varx,liftCoName(m,d,c+1),dbconame(i+d));
        }
        DBforallL(x,m,t,n) -> { 
          return `DBforallL(x,liftCoName(m,d,c),t,n);
        }
        DBfoldR(a,m,cn@dbconame(i),rulenum) -> { 
          if(`i<c) `DBfoldR(a,liftCoName(m,d,c+1),cn,rulenum);
          return `DBfoldR(a,liftCoName(m,d,c+1),dbconame(i+d),rulenum);
        }
        DBfoldL(x,m,n,rulenum) -> { 
          return `DBfoldL(x,liftName(m,d,c),n,rulenum);
        }
        DBmetaVar(mv) -> { 
        }
      }
      throw new RuntimeException("should not happen");
    }

  private static DBProofTerm
    reName(DBProofTerm term, int n1, int n2) {
      %match(term) {
        t@DBax(dbname(i),cn) -> {
          if (`i==n1) return `DBax(dbname(n2),cn);
          else return `t;
        }
        DBcut(a,m1,x,m2) -> { 
          return `DBcut(a,reName(m1,n1,n2),x,reName(m2,n1+1,n2+1)); 
        }
        t@DBfalseL(dbname(i)) -> { 
          if (`i==n1) return `DBfalseL(dbname(n2));
          else return `t;
        }
        t@DBtrueR(cn) -> { 
          return `t;
        }
        DBandR(a,m1,b,m2,cn) -> { 
          return `DBandR(a,reName(m1,n1,n2),b,reName(m2,n1,n2),cn); 
        }
        DBandL(x,y,m,n) -> { 
        }
        DBorR(a,b,m,cn) -> { 
        }
        DBorL(x,m1,y,m2,n) -> { 
        }
        DBimplyR(x,a,m1,cn) -> { 
        }
        DBimplyL(a,m1,x,m2,n) -> { 
        }
        DBexistsR(a,m,t,cn) -> { 
        }
        DBexistsL(x,varx,m,n) -> { 
        }
        DBforallR(a,varx,m,cn) -> { 
        }
        DBforallL(x,m,t,n) -> { 
        }
        DBfoldR(a,m,cn,rulenum) -> { 
        }
        DBfoldL(x,m,n,rulenum) -> { 
        }
        DBmetaVar(mv) -> { 
        }
      }
      throw new RuntimeException("should not happen");
    }

  private static DBProofTerm reCoNameFreeOccurences
    (DBProofTerm term, DBName name1, DBName name2, int deep) {
      %match(term) {
        DBax(n,cn) -> { 
        }
        DBcut(a,m1,x,m2) -> { 
        }
        DBfalseL(n) -> { 
        }
        DBtrueR(cn) -> { 
        }
        DBandR(a,m1,b,m2,cn) -> { 
        }
        DBandL(x,y,m,n) -> { 
        }
        DBorR(a,b,m,cn) -> { 
        }
        DBorL(x,m1,y,m2,n) -> { 
        }
        DBimplyR(x,a,m1,cn) -> { 
        }
        DBimplyL(a,m1,x,m2,n) -> { 
        }
        DBexistsR(a,m,t,cn) -> { 
        }
        DBexistsL(x,varx,m,n) -> { 
        }
        DBforallR(a,varx,m,cn) -> { 
        }
        DBforallL(x,m,t,n) -> { 
        }
        DBfoldR(a,m,cn,rulenum) -> { 
        }
        DBfoldL(x,m,n,rulenum) -> { 
        }
        DBmetaVar(mv) -> { 
        }
      }
      throw new RuntimeException("should not happen");
    }
}
