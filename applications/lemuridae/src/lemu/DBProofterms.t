package lemu;

import tom.library.sl.*;
import java.util.*;

import lemu.urban.types.*;
import lemu.sequents.types.*;

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

  public static class ImmutableMap<K,V> extends HashMap<K,V> {
    public ImmutableMap<K,V> myput(K key,V value) {
      ImmutableMap<K,V> res = (ImmutableMap<K,V>) this.clone();
      res.put(key,value);
      return res;
    }
  }

  public static DBProofTerm translate(ProofTerm term) {
    SymbolTable<Name> nst = new SymbolTable<Name>();
    SymbolTable<CoName> cnst = new SymbolTable<CoName>();
    getFreeNames(term,nst,cnst);

    ImmutableMap<Name,Integer> nmap = new ImmutableMap<Name,Integer>();
    ImmutableMap<CoName,Integer> cnmap = new ImmutableMap<CoName,Integer>();
    int i = 0; for(Name n: nst.getFreeVars()) { nmap.put(n,--i); }
    i = 0; for(CoName n: cnst.getFreeVars()) { cnmap.put(n,--i); }
    return translate(term, nmap, cnmap, 0, 0);
  }

  private static DBProofTerm 
    translate(ProofTerm term, ImmutableMap<Name,Integer> names, 
        ImmutableMap<CoName,Integer> conames, int ndeep, int cndeep) {
      %match(term) {
        ax(n,cn) -> {
          int dbn = ndeep - names.get(`n) -1;
          int dbcn = cndeep - conames.get(`cn) -1;
          return `DBax(dbname(dbn),dbconame(dbcn));
        }
        cut(cnprop(a,p1),m1,nprop(x,p2),m2) -> {
          conames = conames.myput(`a,cndeep);
          names = names.myput(`x,ndeep);
          return `DBcut(p1,translate(m1,names,conames,ndeep,cndeep+1)
              ,p2,translate(m2,names,conames,ndeep+1,cndeep));
        }
        falseL(n) -> { 
          int dbn = ndeep - names.get(`n) -1;
          return `DBfalseL(dbname(dbn));
        }
        trueR(cn) -> {
          int dbcn = cndeep - conames.get(`cn) -1;
          return `DBtrueR(dbconame(dbcn));
        }
        andR(cnprop(a,p1),m1,cnprop(b,p2),m2,cn) -> {
          int dbcn = cndeep - conames.get(`cn) -1;
          conames = conames.myput(`a,cndeep);
          conames = conames.myput(`b,cndeep);
          return `DBandR(p1,translate(m1,names,conames,ndeep,cndeep+1),
              p2,translate(m2,names,conames,ndeep,cndeep+1),dbconame(dbcn));
        }
        andL(nprop(x,p1),nprop(y,p2),m,n) -> { 
          int dbn = ndeep - names.get(`n) -1;
          names = names.myput(`x,ndeep);
          names = names.myput(`y,ndeep+1);
          return `DBandL(p1,p2,
              translate(m,names,conames,ndeep+2,cndeep),dbname(dbn));
        }
        orR(cnprop(a,p1),cnprop(b,p2),m,cn) -> { 
          int dbcn = cndeep - conames.get(`cn) -1;
          conames = conames.myput(`a,cndeep);
          conames = conames.myput(`b,cndeep+1);
          return `DBorR(p1,p2,
              translate(m,names,conames,ndeep,cndeep+2),dbconame(dbcn));
        }
        orL(nprop(x,p1),m1,nprop(y,p2),m2,n) -> { 
          int dbn = ndeep - names.get(`n) -1;
          names = names.myput(`x,ndeep);
          names = names.myput(`y,ndeep);
          return `DBorL(p1,translate(m1,names,conames,ndeep+1,cndeep),
              p2,translate(m2,names,conames,ndeep+1,cndeep),dbname(dbn));
        }
        implyR(nprop(x,p1),cnprop(a,p2),m1,cn) -> { 
          conames = conames.myput(`a,cndeep);
          names = names.myput(`x,ndeep);
          int dbcn = cndeep - conames.get(`cn) -1;
          return `DBimplyR(p1,p2,
              translate(m1,names,conames,ndeep+1,cndeep+1),dbconame(dbcn));
        }
        implyL(cnprop(a,p1),m1,nprop(x,p2),m2,n) -> { 
          conames = conames.myput(`a,cndeep);
          names = names.myput(`x,ndeep);
          int dbn = ndeep - names.get(`n) -1;
          return `DBimplyL(p1, translate(m1,names,conames,ndeep,cndeep+1),
              p2,translate(m2,names,conames,ndeep+1,cndeep),dbname(dbn));
        }
        existsR(cnprop(a,p1),m,t,cn) -> { 
          conames = conames.myput(`a,cndeep);
          int dbcn = cndeep - conames.get(`cn) -1;
          return `DBexistsR(p1, translate(m,names,conames,ndeep,cndeep+1),
              t,dbconame(dbcn));
        }
        existsL(nprop(x,p1),varx,m,n) -> { 
          names = names.myput(`x,ndeep);
          int dbn = ndeep - names.get(`n) -1;
          return `DBexistsL(p1,varx,
              translate(m,names,conames,ndeep+1,cndeep),dbname(dbn));
        }
        forallR(cnprop(a,p1),varx,m,cn) -> { 
          conames = conames.myput(`a,cndeep);
          int dbcn = cndeep - conames.get(`cn) -1;
          return `DBforallR(p1,varx,
              translate(m,names,conames,ndeep,cndeep+1),dbconame(dbcn));
        }
        forallL(nprop(x,p1),m,t,n) -> { 
          names = names.myput(`x,ndeep);
          int dbn = ndeep - names.get(`n) -1;
          return `DBforallL(p1,translate(m,names,conames,ndeep+1,cndeep),
              t,dbname(dbn));
        }
        foldR(cnprop(a,p1),m,cn,rulenum) -> { 
          conames = conames.myput(`a,cndeep);
          int dbcn = cndeep - conames.get(`cn) -1;
          return `DBfoldR(p1,translate(m,names,conames,ndeep,cndeep+1),
              dbconame(dbcn),rulenum);
        }
        foldL(nprop(x,p1),m,n,rulenum) -> { 
          names = names.myput(`x,ndeep);
          int dbn = ndeep - names.get(`n) -1;
          return `DBfoldL(p1,translate(m,names,conames,ndeep+1,cndeep),
              dbname(dbn),rulenum);
        }
        metaVar(mvar(name)) -> {
          return `DBmetaVar(dbmvar(name));
        }
      }
      return null;
    }

  private static DBProofTerm
    shiftName(DBProofTerm term, int d, int c) {
      %match(term) {
        t@DBax(dbname(i),cn) -> { 
          if(`i<c) return `t;
          else return `DBax(dbname(i+d),cn);
        }
        DBcut(a,m1,x,m2) -> { 
          return `DBcut(a,shiftName(m1,d,c),x,shiftName(m2,d,c+1));
        }
        t@DBfalseL(dbname(i)) -> { 
          if(`i<c) return `t;
          else return `DBfalseL(dbname(i+d));
        }
        t@DBtrueR(cn) -> {
          return `t;
        }
        DBandR(a,m1,b,m2,cn) -> { 
          return `DBandR(a,shiftName(m1,d,c),b,shiftName(m2,d,c),cn);
        }
        DBandL(x,y,m,n@dbname(i)) -> { 
          if(`i<c) return `DBandL(x,y,shiftName(m,d,c+2),n);
          else return `DBandL(x,y,shiftName(m,d,c+2),dbname(i+d));
        }
        DBorR(a,b,m,cn) -> { 
          return `DBorR(a,b,shiftName(m,d,c),cn);
        }
        DBorL(x,m1,y,m2,n@dbname(i)) -> { 
          if(`i<c) return `DBorL(x,shiftName(m1,d,c+1),y,shiftName(m2,d,c+1),n);
          else return `DBorL(x,shiftName(m1,d,c+1),y,shiftName(m2,d,c+1),dbname(i+d));
        }
        DBimplyR(x,a,m1,cn) -> {  
          return `DBimplyR(x,a,shiftName(m1,d,c+1),cn);
        }
        DBimplyL(a,m1,x,m2,n@dbname(i)) -> { 
          if(`i<c) return `DBimplyL(a,shiftName(m1,d,c),x,shiftName(m2,d,c+1),n);
          else return `DBimplyL(a,shiftName(m1,d,c),x,shiftName(m2,d,c+1),dbname(i+d));
        }
        DBexistsR(a,m,t,cn) -> { 
          return `DBexistsR(a,shiftName(m,d,c),t,cn);
        }
        DBexistsL(x,varx,m,n@dbname(i)) -> { 
          if(`i<c) return `DBexistsL(x,varx,shiftName(m,d,c+1),n);
          else return `DBexistsL(x,varx,shiftName(m,d,c+1),dbname(i+d));
        }
        DBforallR(a,varx,m,cn) -> { 
          return `DBforallR(a,varx,shiftName(m,d,c),cn);
        }
        DBforallL(x,m,t,n@dbname(i)) -> { 
          if(`i<c) return `DBforallL(x,shiftName(m,d,c+1),t,n);
          else return `DBforallL(x,shiftName(m,d,c+1),t,dbname(i+d));
        }
        DBfoldR(a,m,cn,rulenum) -> { 
          return `DBfoldR(a,shiftName(m,d,c),cn,rulenum);
        }
        DBfoldL(x,m,n@dbname(i),rulenum) -> { 
          if(`i<c) return `DBfoldL(x,shiftName(m,d,c+1),n,rulenum);
          else return `DBfoldL(x,shiftName(m,d,c+1),dbname(i+d),rulenum);
        }
        DBmetaVar(mv) -> { 
        }
      }
      throw new RuntimeException("should not happen");
    }

  public static DBProofTerm
    shiftCoName(DBProofTerm term, int d, int c) {
      %match(term) {
        t@DBax(n,dbconame(i)) -> {
          if(`i<c) return `t;
          else return `DBax(n,dbconame(i+d));
        }
        DBcut(a,m1,x,m2) -> { 
          return `DBcut(a,shiftCoName(m1,d,c+1),x,shiftCoName(m2,d,c));
        }
        t@DBfalseL(n) -> { 
          return `t;
        }
        t@DBtrueR(dbconame(i)) -> {
          if(`i<c) return `t;
          else return `DBtrueR(dbconame(i+d));
        }
        DBandR(a,m1,b,m2,cn@dbconame(i)) -> { 
          if(`i<c) return `DBandR(a,shiftCoName(m1,d,c+1),b,shiftCoName(m2,d,c+1),cn); 
          return `DBandR(a,shiftCoName(m1,d,c+1),b,shiftCoName(m2,d,c+1),dbconame(i+d));
        }
        DBandL(x,y,m,n) -> { 
          return `DBandL(x,y,shiftCoName(m,d,c),n);
        }
        DBorR(a,b,m,cn@dbconame(i)) -> { 
          if(`i<c) return `DBorR(a,b,shiftCoName(m,d,c+2),cn); 
          return `DBorR(a,b,shiftCoName(m,d,c+2),dbconame(i+d)); 
        }
        DBorL(x,m1,y,m2,n) -> { 
          return `DBorL(x,shiftCoName(m1,d,c),y,shiftCoName(m2,d,c),n);
        }
        DBimplyR(x,a,m1,cn@dbconame(i)) -> {
          if(`i<c) return `DBimplyR(x,a,shiftCoName(m1,d,c+1),cn); 
          return `DBorR(x,a,shiftCoName(m1,d,c+1),dbconame(i+d)); 
        }
        DBimplyL(a,m1,x,m2,n) -> {
          return `DBimplyL(a,shiftCoName(m1,d,c+1),x,shiftCoName(m2,d,c),n);
        }
        DBexistsR(a,m,t,cn@dbconame(i)) -> { 
          if(`i<c)`DBexistsR(a,shiftCoName(m,d,c+1),t,cn); 
          return `DBexistsR(a,shiftCoName(m,d,c+1),t,dbconame(i+d));
        }
        DBexistsL(x,varx,m,n) -> { 
          return `DBexistsL(x,varx,shiftCoName(m,d,c),n);
        }
        DBforallR(a,varx,m,cn@dbconame(i)) -> { 
          if(`i<c) `DBforallR(a,varx,shiftCoName(m,d,c+1),cn);
          else return `DBforallR(a,varx,shiftCoName(m,d,c+1),dbconame(i+d));
        }
        DBforallL(x,m,t,n) -> { 
          return `DBforallL(x,shiftCoName(m,d,c),t,n);
        }
        DBfoldR(a,m,cn@dbconame(i),rulenum) -> { 
          if(`i<c) `DBfoldR(a,shiftCoName(m,d,c+1),cn,rulenum);
          return `DBfoldR(a,shiftCoName(m,d,c+1),dbconame(i+d),rulenum);
        }
        DBfoldL(x,m,n,rulenum) -> { 
          return `DBfoldL(x,shiftName(m,d,c),n,rulenum);
        }
        DBmetaVar(mv) -> { 
        }
      }
      throw new RuntimeException("should not happen");
    }

  private static DBProofTerm shift(DBProofTerm M, int cut, int ndeep, int cndeep) {
    M = shiftName(M,ndeep,cut);
    M = shiftCoName(M,cndeep,cut);
    return M;
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
        t@DBandL(x,y,m,n@dbname(i)) -> { 
          if (`i==n1) return `DBandL(x,y,reName(m,n1+2,n2+2),dbname(n2));
          else return `DBandL(x,y,reName(m,n1+2,n2+2),n);
        }
        t@DBorR(a,b,m,cn) -> { 
          return `DBorR(a,b,reName(m,n1,n2),cn);
        }
        DBorL(x,m1,y,m2,n@dbname(i)) -> { 
          if (`i==n1) return `DBorL(x,reName(m1,n1+1,n2+1),y,reName(m2,n1+1,n2+1),dbname(n2));
          else return `DBorL(x,reName(m1,n1+1,n2+1),y,reName(m2,n1+1,n2+1),n);
        }
        DBimplyR(x,a,m1,cn) -> { 
          return `DBimplyR(x,a,reName(m1,n1,n2),cn);
        }
        DBimplyL(a,m1,x,m2,n@dbname(i)) -> { 
          if (`i==n1) return `DBimplyL(a,reName(m1,n1,n2),x,reName(m2,n1+1,n2+1),dbname(n2));
          else return `DBimplyL(a,reName(m1,n1,n2),x,reName(m2,n1+1,n2+1),n);
        }
        DBexistsR(a,m,t,cn) -> { 
          return `DBexistsR(a,reName(m,n1,n2),t,cn);
        }
        DBexistsL(x,varx,m,n@dbname(i)) -> { 
          if (`i==n1) return `DBexistsL(x,varx,reName(m,n1+1,n2+1),dbname(n2));
          else return `DBexistsL(x,varx,reName(m,n1+1,n2+1),n);
        }
        DBforallR(a,varx,m,cn) -> { 
          return `DBforallR(a,varx,reName(m,n1,n2),cn);
        }
        DBforallL(x,m,t,n@dbname(i)) -> { 
          if (`i==n1) return `DBforallL(x,reName(m,n1+1,n2+1),t,dbname(n2));
          else return `DBforallL(x,reName(m,n1+1,n2+1),t,n);
        }
        DBfoldR(a,m,cn,rulenum) -> { 
          return `DBfoldR(a,reName(m,n1,n2),cn,rulenum); 
        }
        DBfoldL(x,m,n@dbname(i),rulenum) -> { 
          if (`i==n1) return `DBfoldL(x,reName(m,n1+1,n2+1),dbname(n2),rulenum);
          else return `DBfoldL(x,reName(m,n1+1,n2+1),n,rulenum);
        }
        DBmetaVar(mv) -> { 
        }
      }
      throw new RuntimeException("should not happen");
    }

  private static DBProofTerm
    reCoName(DBProofTerm term, int cn1, int cn2) {
      %match(term) {
        t@DBax(n,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBax(n,dbconame(cn2));
          else return `t;
        }
        DBcut(a,m1,x,m2) -> { 
        }
        DBfalseL(n) -> { 
        }
        DBtrueR(cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBtrueR(dbconame(cn2));
          else return `DBtrueR(cn);
        }
        DBandR(a,m1,b,m2,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBandR(a,reCoName(m1,cn1+1,cn2+1),b, reCoName(m2,cn1+1,cn2+1),dbconame(cn2));
          else return `DBandR(a,reCoName(m1,cn1+1,cn2+1),b, reCoName(m2,cn1+1,cn2+1),cn);
        }
        DBandL(x,y,m,n) -> { 
        }
        DBorR(a,b,m,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBorR(a,b,reCoName(m,cn1+2,cn2+2),dbconame(cn2));
          else return `DBorR(a,b,reCoName(m,cn1+2,cn2+2),cn);
        }
        DBorL(x,m1,y,m2,n) -> { 
        }
        DBimplyR(x,a,m1,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBimplyR(x,a,reCoName(m1,cn1+1,cn2+1),dbconame(cn2));
          else return `DBimplyR(x,a,reCoName(m1,cn1+1,cn2+1),cn);
        }
        DBimplyL(a,m1,x,m2,n) -> { 
        }
        DBexistsR(a,m,t,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBexistsR(a,reCoName(m,cn1+1,cn2+1),t,dbconame(cn2));
          else return `DBexistsR(a,reCoName(m,cn1+1,cn2+1),t,cn);
        }
        DBexistsL(x,varx,m,n) -> { 
        }
        DBforallR(a,varx,m,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBforallR(a,varx,reCoName(m,cn1+1,cn2+1),dbconame(cn2));
          else return `DBforallR(a,varx,reCoName(m,cn1+1,cn2+1),cn);
        }
        DBforallL(x,m,t,n) -> { 
        }
        DBfoldR(a,m,cn@dbconame(i),rulenum) -> { 
          if (`i==cn1) return `DBfoldR(a,reCoName(m,cn1+1,cn2+1),dbconame(cn2),rulenum);
          else return `DBfoldR(a,reCoName(m,cn1+1,cn2+1),cn,rulenum);
        }
        DBfoldL(x,m,n,rulenum) -> { 
        }
        DBmetaVar(mv) -> { 
        }
      }
      throw new RuntimeException("should not happen");
    }

  private static DBProofTerm
    substName(DBProofTerm term, int i, Prop a, DBProofTerm M, int ndeep, int cndeep) {
      %match(term) {
        t@DBax(dbname(ni),dbconame(cni)) -> {
          if (`ni==i+ndeep) return `reCoName(shift(M,0,ndeep,cndeep),0,cni);
          else return `t;
        }
        DBcut(a,m1,x,m2) -> {
          return `DBcut(a,substName(m1,i,a,M,ndeep,cndeep+1),x,substName(m2,i,a,M,ndeep+1,cndeep)); 
        }
        /*
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
        */
        t@DBandL(x,y,m,n@dbname(ni)) -> { 
          if (`ni==i+ndeep) {
            DBProofTerm new_and = `DBandL(x,y,substName(m,i,a,M,ndeep+2,cndeep),dbname(0));
            return `DBcut(a,shift(M,0,ndeep,cndeep),a,shift(new_and,0,ndeep+1,cndeep));
          }
          else return `DBandL(x,y,substName(m,i,a,M,ndeep+2,cndeep),n);
        }
        /*
        t@DBorR(a,b,m,cn) -> { 
          return `DBorR(a,b,reName(m,n1,n2),cn);
        }
        DBorL(x,m1,y,m2,n@dbname(i)) -> { 
          if (`i==n1) return `DBorL(x,reName(m1,n1+1,n2+1),y,reName(m2,n1+1,n2+1),dbname(n2));
          else return `DBorL(x,reName(m1,n1+1,n2+1),y,reName(m2,n1+1,n2+1),n);
        }
        DBimplyR(x,a,m1,cn) -> { 
          return `DBimplyR(x,a,reName(m1,n1,n2),cn);
        }
        DBimplyL(a,m1,x,m2,n@dbname(i)) -> { 
          if (`i==n1) return `DBimplyL(a,reName(m1,n1,n2),x,reName(m2,n1+1,n2+1),dbname(n2));
          else return `DBimplyL(a,reName(m1,n1,n2),x,reName(m2,n1+1,n2+1),n);
        }
        DBexistsR(a,m,t,cn) -> { 
          return `DBexistsR(a,reName(m,n1,n2),t,cn);
        }
        DBexistsL(x,varx,m,n@dbname(i)) -> { 
          if (`i==n1) return `DBexistsL(x,varx,reName(m,n1+1,n2+1),dbname(n2));
          else return `DBexistsL(x,varx,reName(m,n1+1,n2+1),n);
        }
        DBforallR(a,varx,m,cn) -> { 
          return `DBforallR(a,varx,reName(m,n1,n2),cn);
        }
        DBforallL(x,m,t,n@dbname(i)) -> { 
          if (`i==n1) return `DBforallL(x,reName(m,n1+1,n2+1),t,dbname(n2));
          else return `DBforallL(x,reName(m,n1+1,n2+1),t,n);
        }
        DBfoldR(a,m,cn,rulenum) -> { 
          return `DBfoldR(a,reName(m,n1,n2),cn,rulenum); 
        }
        DBfoldL(x,m,n@dbname(i),rulenum) -> { 
          if (`i==n1) return `DBfoldL(x,reName(m,n1+1,n2+1),dbname(n2),rulenum);
          else return `DBfoldL(x,reName(m,n1+1,n2+1),n,rulenum);
        }
        DBmetaVar(mv) -> { 
        }
        */
      }
      throw new RuntimeException("should not happen");
    }

  /*
  private static DBProofTerm
    substCoName(DBProofTerm term, int cn1, int cn2) {
      %match(term) {
        t@DBax(n,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBax(n,dbconame(cn2));
          else return `t;
        }
        DBcut(a,m1,x,m2) -> { 
        }
        DBfalseL(n) -> { 
        }
        DBtrueR(cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBtrueR(dbconame(cn2));
          else return `DBtrueR(cn);
        }
        DBandR(a,m1,b,m2,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBandR(a,reCoName(m1,cn1+1,cn2+1),b, reCoName(m2,cn1+1,cn2+1),dbconame(cn2));
          else return `DBandR(a,reCoName(m1,cn1+1,cn2+1),b, reCoName(m2,cn1+1,cn2+1),cn);
        }
        DBandL(x,y,m,n) -> { 
        }
        DBorR(a,b,m,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBorR(a,b,reCoName(m,cn1+2,cn2+2),dbconame(cn2));
          else return `DBorR(a,b,reCoName(m,cn1+2,cn2+2),cn);
        }
        DBorL(x,m1,y,m2,n) -> { 
        }
        DBimplyR(x,a,m1,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBimplyR(x,a,reCoName(m1,cn1+1,cn2+1),dbconame(cn2));
          else return `DBimplyR(x,a,reCoName(m1,cn1+1,cn2+1),cn);
        }
        DBimplyL(a,m1,x,m2,n) -> { 
        }
        DBexistsR(a,m,t,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBexistsR(a,reCoName(m,cn1+1,cn2+1),t,dbconame(cn2));
          else return `DBexistsR(a,reCoName(m,cn1+1,cn2+1),t,cn);
        }
        DBexistsL(x,varx,m,n) -> { 
        }
        DBforallR(a,varx,m,cn@dbconame(i)) -> { 
          if (`i==cn1) return `DBforallR(a,varx,reCoName(m,cn1+1,cn2+1),dbconame(cn2));
          else return `DBforallR(a,varx,reCoName(m,cn1+1,cn2+1),cn);
        }
        DBforallL(x,m,t,n) -> { 
        }
        DBfoldR(a,m,cn@dbconame(i),rulenum) -> { 
          if (`i==cn1) return `DBfoldR(a,reCoName(m,cn1+1,cn2+1),dbconame(cn2),rulenum);
          else return `DBfoldR(a,reCoName(m,cn1+1,cn2+1),cn,rulenum);
        }
        DBfoldL(x,m,n,rulenum) -> { 
        }
        DBmetaVar(mv) -> { 
        }
      }
      throw new RuntimeException("should not happen");
    }
    */

  public static void main(String[] args) {
    Prop A = `relationAppl("A",concTerm());
    Prop B = `relationAppl("B",concTerm());

    DBProofTerm pt = `DBorL(A,DBax(dbname(0),dbconame(0)),B,DBax(dbname(1),dbconame(0)),dbname(0)); 
    DBProofTerm ptbis = reName(pt,0,1);
    System.out.println("pt             = " + PrettyPrinter.prettyPrint(pt));
    System.out.println("pt[n(0)->n(1)] = " + PrettyPrinter.prettyPrint(ptbis));

    System.out.println();
    DBProofTerm pt2 = `DBandL(A,B,DBax(dbname(1),dbconame(0)),dbname(0)); 
    DBProofTerm pt2bis = reName(pt2,0,1);
    System.out.println("pt2             = " + PrettyPrinter.prettyPrint(pt2));
    System.out.println("pt2[n(0)->n(1)] = " + PrettyPrinter.prettyPrint(pt2bis));

    System.out.println();
    DBProofTerm pt3 = `DBandL(A,B,DBax(dbname(2),dbconame(0)),dbname(0)); 
    DBProofTerm pt3bis = reName(pt3,0,1);
    System.out.println("pt3             = " + PrettyPrinter.prettyPrint(pt3));
    System.out.println("pt3[n(0)->n(1)] = " + PrettyPrinter.prettyPrint(pt3bis));

    System.out.println();
    DBProofTerm pt4 = `DBandL(A,B,DBax(dbname(3),dbconame(0)),dbname(0)); 
    DBProofTerm pt4bis = reName(pt4,1,2);
    System.out.println("pt4             = " + PrettyPrinter.prettyPrint(pt4));
    System.out.println("pt4[n(1)->n(2)] = " + PrettyPrinter.prettyPrint(pt4bis));

    System.out.println();
    DBProofTerm pt5 = `DBandL(A,B,DBax(dbname(3),dbconame(2)),dbname(0)); 
    DBProofTerm M5 = `DBax(dbname(4),dbconame(0)); 
    DBProofTerm pt5bis = substName(pt5,1,A,M5,0,0);
    System.out.println("pt5                       = " + PrettyPrinter.prettyPrint(pt5));
    System.out.println("pt5[n(1):=ax(n(4),cn(0))] = " + PrettyPrinter.prettyPrint(pt5bis));

    System.out.println();
    DBProofTerm pt6 = `DBax(dbname(0),dbconame(0)); 
    DBProofTerm pt6bis = shift(pt6,0,1,0); 
    System.out.println("pt6              = " + PrettyPrinter.prettyPrint(pt6));
    System.out.println("shift(pt6,0,1,0) = " + PrettyPrinter.prettyPrint(pt6bis));
  }
}
