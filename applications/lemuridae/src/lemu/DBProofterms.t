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
          int dbn = deep - names.get(`n);
          int dbcn = deep - conames.get(`cn);
          return `DBax(dbname(dbn),dbconame(dbcn));
        }
        cut(cnprop(a,p1),m1,nprop(x,p2),m2) -> {
          conames.put(`a,deep);
          names.put(`x,deep);
          return `DBcut(p1,translate(m1,names,conames,deep+1)
              ,p2,translate(m2,names,conames,deep+1));
        }
        falseL(n) -> { 
          int dbn = deep - names.get(`n);
          return `DBfalseL(dbname(dbn));
        }
        trueR(cn) -> {
          int dbcn = deep - conames.get(`cn);
          return `DBtrueR(dbconame(dbcn));
        }
        andR(cnprop(a,p1),m1,cnprop(b,p2),m2,cn) -> {
          int dbcn = deep - conames.get(`cn);
          conames.put(`a,deep);
          conames.put(`b,deep);
          return `DBandR(p1,translate(m1,names,conames,deep+1),
              p2,translate(m2,names,conames,deep+1),dbconame(dbcn));
        }
        andL(nprop(x,p1),nprop(y,p2),m,n) -> { 
          int dbn = deep - names.get(`n);
          names.put(`x,deep);
          names.put(`y,deep);
          return `DBandL(p1,p2,
              translate(m,names,conames,deep+1),dbname(dbn));
        }
        orR(cnprop(a,p1),cnprop(b,p2),m,cn) -> { 
          int dbcn = deep - conames.get(`cn);
          conames.put(`a,deep);
          conames.put(`b,deep);
          return `DBorR(p1,p2,
              translate(m,names,conames,deep+1),dbconame(dbcn));
        }
        orL(nprop(x,p1),m1,nprop(y,p2),m2,n) -> { 
          int dbn = deep - names.get(`n);
          names.put(`x,deep);
          names.put(`y,deep);
          return `DBorL(p1,translate(m1,names,conames,deep+1),
              p2,translate(m2,names,conames,deep+1),dbname(dbn));
        }
        implyR(nprop(x,p1),cnprop(a,p2),m1,cn) -> { 
          conames.put(`a,deep);
          names.put(`x,deep);
          int dbcn = deep - conames.get(`cn);
          return `DBimplyR(p1,p2,
              translate(m1,names,conames,deep+1),dbconame(dbcn));
        }
        implyL(cnprop(a,p1),m1,nprop(x,p2),m2,n) -> { 
          conames.put(`a,deep);
          names.put(`x,deep);
          int dbn = deep - names.get(`n);
          return `DBimplyL(p1, translate(m1,names,conames,deep+1),
              p2,translate(m2,names,conames,deep+1),dbname(dbn));
        }
        existsR(cnprop(a,p1),m,t,cn) -> { 
          conames.put(`a,deep);
          int dbcn = deep - conames.get(`cn);
          return `DBexistsR(p1, translate(m,names,conames,deep+1),
              t,dbconame(dbcn));
        }
        existsL(nprop(x,p1),varx,m,n) -> { 
          names.put(`x,deep);
          int dbn = deep - names.get(`n);
          return `DBexistsL(p1,varx,
              translate(m,names,conames,deep+1),dbname(dbn));
        }
        forallR(cnprop(a,p1),varx,m,cn) -> { 
          conames.put(`a,deep);
          int dbcn = deep - conames.get(`cn);
          return `DBforallR(p1,varx,
              translate(m,names,conames,deep+1),dbconame(dbcn));
        }
        forallL(nprop(x,p1),m,t,n) -> { 
          names.put(`x,deep);
          int dbn = deep - names.get(`n);
          return `DBforallL(p1,translate(m,names,conames,deep+1),
              t,dbname(dbn));
        }
        foldR(cnprop(a,p1),m,cn,rulenum) -> { 
          conames.put(`a,deep);
          int dbcn = deep - conames.get(`cn);
          return `DBfoldR(p1,translate(m,names,conames,deep+1),
              dbconame(dbcn),rulenum);
        }
        foldL(nprop(x,p1),m,n,rulenum) -> { 
          names.put(`x,deep);
          int dbn = deep - names.get(`n);
          return `DBfoldL(p1,translate(m,names,conames,deep+1),
              dbname(dbn),rulenum);
        }
        metaVar(mvar(name)) -> {
          return `DBmetaVar(dbmvar(name));
        }
      }
      return null;
    }

}
