package tree17;

import aterm.*;
import aterm.pure.*;
import java.util.*;

public class ATermTree17 {
    private static AFun ftrue = SingletonFactory.getInstance().makeAFun("true", 0, false);
    private static AFun ffalse = SingletonFactory.getInstance().makeAFun("false", 0, false);
    private static AFun fz = SingletonFactory.getInstance().makeAFun("z", 0, false);
    private static AFun fs = SingletonFactory.getInstance().makeAFun("s", 1, false);
    
    private static AFun fequal = SingletonFactory.getInstance().makeAFun("equal", 2, false);
    private static AFun fplus = SingletonFactory.getInstance().makeAFun("plus", 2, false);
    private static AFun fmult = SingletonFactory.getInstance().makeAFun("mult", 2, false);
    private static AFun fexp = SingletonFactory.getInstance().makeAFun("exp", 2, false);

    private static AFun fsucc17 = SingletonFactory.getInstance().makeAFun("succ17", 1, false);
    private static AFun fpred17 = SingletonFactory.getInstance().makeAFun("pred17", 1, false);
    private static AFun fplus17 = SingletonFactory.getInstance().makeAFun("plus17", 2, false);
    private static AFun fmult17 = SingletonFactory.getInstance().makeAFun("mult17", 2, false);
    private static AFun fexp17 = SingletonFactory.getInstance().makeAFun("exp17", 2, false);
    
    private static AFun fexz = SingletonFactory.getInstance().makeAFun("exz", 0, false);
    private static AFun fexs = SingletonFactory.getInstance().makeAFun("exs", 1, false);
    private static AFun fexplus = SingletonFactory.getInstance().makeAFun("explus", 2, false);
    private static AFun fexmult = SingletonFactory.getInstance().makeAFun("exmult", 2, false);
    private static AFun fexexp = SingletonFactory.getInstance().makeAFun("exexp", 2, false);

    private static AFun fleaf = SingletonFactory.getInstance().makeAFun("leaf", 1, false);
    private static AFun fnode = SingletonFactory.getInstance().makeAFun("node", 4, false);
    private static AFun fbuildtree = SingletonFactory.getInstance().makeAFun("buildtree", 2, false);
    private static AFun fcalctree17 = SingletonFactory.getInstance().makeAFun("calctree17", 2, false);
    private static AFun fgetmax = SingletonFactory.getInstance().makeAFun("getmax", 1, false);
    private static AFun fgetval = SingletonFactory.getInstance().makeAFun("getval", 1, false);
    private static AFun feval = SingletonFactory.getInstance().makeAFun("eval", 1, false);
    private static AFun feval17 = SingletonFactory.getInstance().makeAFun("eval17", 1, false);
    private static AFun fevalsym17 = SingletonFactory.getInstance().makeAFun("evalsym17", 1, false);
    private static AFun fevalexp17 = SingletonFactory.getInstance().makeAFun("evalexp17", 1, false);
    private static AFun fbench_evalsym17 = SingletonFactory.getInstance().makeAFun("bench_evalsym17", 1, false);
    private static AFun fbench_evalexp17 = SingletonFactory.getInstance().makeAFun("bench_evalexp17", 1, false);
    private static AFun fbench_evaltree17 = SingletonFactory.getInstance().makeAFun("bench_evaltree17", 1, false);

    private static AFun fdec = SingletonFactory.getInstance().makeAFun("dec", 1, false);
    private static AFun fexpand = SingletonFactory.getInstance().makeAFun("expand", 1, false);
    private static AFun fexone = SingletonFactory.getInstance().makeAFun("exone", 0, false);
    
    private static ATermAppl ttrue = SingletonFactory.getInstance().makeAppl(ftrue);
    private static ATermAppl tfalse = SingletonFactory.getInstance().makeAppl(ffalse);
    private static ATermAppl tz = SingletonFactory.getInstance().makeAppl(fz);
    private static ATermAppl texz = SingletonFactory.getInstance().makeAppl(fexz);
    private static ATermAppl texone = SingletonFactory.getInstance().makeAppl(fexone);

  %typeterm term {
    implement           { ATerm }
    equals(t1, t2)      { (t1.equals(t2)) }
  }

  %op term true() {
    is_fsym(t) { (((ATermAppl)t).getAFun())== ftrue }
    make { SingletonFactory.getInstance().makeAppl(ftrue) }
  }
  %op term false() {
    is_fsym(t) { (((ATermAppl)t).getAFun())== ffalse }
    make { SingletonFactory.getInstance().makeAppl(ffalse) }
  }
  %op term z() {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fz }
    make { SingletonFactory.getInstance().makeAppl(fz) }
  }
  %op term s(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fs }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fs,t) }
  }
  %op term equal(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fequal }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fequal,t1,t2) }
  }
  
  %op term plus(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fplus }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fplus,t1,t2) }
  }
  %op term mult(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fmult }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fmult,t1,t2) }
  }
  %op term exp(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fexp }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fexp,t1,t2) }
  }

  %op term pred17(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fpred17 }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fpred17,t) }
  }
  %op term plus17(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fplus17 }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fplus17,t1,t2) }
  }
  %op term mult17(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fmult17 }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fmult17,t1,t2) }
  }
  %op term exp17(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fexp17 }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fexp17,t1,t2) }
  }

  %op term exz() {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fexz }
    make { texz }
  }
  %op term exs(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fexs }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fexs,t) }
  }
  %op term explus(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fexplus }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fexplus,t1,t2) }
  }
  %op term exmult(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fexmult }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fexmult,t1,t2) }
  }
  %op term exexp(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fexexp }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fexexp,t1,t2) }
  }

  %op term leaf(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fleaf }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fleaf,t) }
  }
  %op term node(a1:term,a2:term,a3:term,a4:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fnode }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    get_slot(a3,t) { (((ATermAppl)t).getArgument(2)) }
    get_slot(a4,t) { (((ATermAppl)t).getArgument(3)) }
    make(t1,t2,t3,t4) { SingletonFactory.getInstance().makeAppl(fnode,new ATerm[] {t1,t2,t3,t4}) }
  }
  %op term buildtree(a1:term,a2:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fbuildtree }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    get_slot(a2,t) { (((ATermAppl)t).getArgument(1)) }
    make(t1,t2) { SingletonFactory.getInstance().makeAppl(fbuildtree,t1,t2) }
  }
  %op term getmax(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fgetmax }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fgetmax,t) }
  }
  %op term getval(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fgetval }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fgetval,t) }
  }
  %op term eval(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== feval }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(feval,t) }
  }
  %op term eval17(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== feval17 }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(feval17,t) }
  }
  %op term evalsym17(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fevalsym17 }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fevalsym17,t) }
  }
  %op term bench_evalsym17(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fbench_evalsym17 }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fbench_evalsym17,t) }
  }
  %op term bench_evalexp17(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fbench_evalexp17 }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fbench_evalexp17,t) }
  }
  %op term bench_evaltree17(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fbench_evaltree17 }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fbench_evaltree17,t) }
  }
  
  %op term dec(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fdec }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fdec,t) }
  }
  %op term expand(a1:term) {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fexpand }
    get_slot(a1,t) { (((ATermAppl)t).getArgument(0)) }
    make(t) { SingletonFactory.getInstance().makeAppl(fexpand,t) }
  }
  %op term exone() {
    is_fsym(t) { (((ATermAppl)t).getAFun())== fexone }
    make { texone }
  }
  

  public ATerm int2aterm(int n) {
    ATerm N = texz;
    for(int i=0 ; i<n ; i++) {
      N = SingletonFactory.getInstance().makeAppl(fexs,N);
    }
    return N;
  }

  public final static void main(String[] args) {
    ATermTree17 test = new ATermTree17();
    int max = 0;
    try {
      max = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java tree17.GomTree17 <max>");
      return;
    }
    test.run_evalsym17(max);
    test.run_evalexp17(max);
    test.run_evaltree17(max);
  }
  
  public ATerm equal(ATerm t1, ATerm t2) {
    if(t1==t2) {
      return ttrue;
    } else {
      return tfalse;
    }
  }

  public void run_evalsym17(int max) {
    System.out.print(max);
    long startChrono = System.currentTimeMillis();
    ATerm n = `exexp(int2aterm(2),int2aterm(max));
    ATerm t1 = eval17(n);
    ATerm t2 = evalsym17(n);
    ATerm res = equal(t1,t2);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000. + "\t evalsym17 " + res);
    System.out.println("t1 = " + t1);
    System.out.println("t2 = " + t2);
  }

  public void run_evalexp17(int max) {
    System.out.print(max);
    long startChrono = System.currentTimeMillis();
    ATerm n = `exexp(int2aterm(2),int2aterm(max));
    ATerm t1 = eval17(n);
    ATerm t2 = evalexp17(n);
    ATerm res = equal(t1,t2);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000. + "\t evalexp17 " + res);
    System.out.println("t1 = " + t1);
    System.out.println("t2 = " + t2);
  }

  public void run_evaltree17(int max) {
    System.out.print(max);
    long startChrono = System.currentTimeMillis();
    ATerm n = eval(int2aterm(max));
    ATerm t1 = calctree17(n);
    ATerm t2 = getval(buildtree(n,`z()));
    ATerm res = equal(t1,t2);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000. + "\t evaltree17 " + res);
    System.out.println("t1 = " + t1);
    System.out.println("t2 = " + t2);
  }
  public ATerm buildtree(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      z(),Val   -> { return `leaf(Val); }
      s(X), Y -> {
        ATerm Left = buildtree(`X, `Y);	
        ATerm Max2 = getmax(Left);
        ATerm Right= buildtree(`X, succ17(Max2));
        ATerm Val2 = getval(Left);
        ATerm Val3 = getval(Right);
        ATerm Val  = plus17(Val2, Val3);
        ATerm Max  = getmax(Right);
        return `node(Val, Max, Left, Right);
      }
    }
    return null;
  }

    %rule {
    plus(x,z()) -> x 
    plus(x,s(y)) -> s(plus(x,y)) 
  }
  
  %rule {
    mult(x,z()) -> z()
    mult(x,s(y)) -> plus(mult(x,y),x) 
  }
  
  %rule {
    exp(x,z()) -> s(z()) 
    exp(x,s(y)) -> mult(x,exp(x,y)) 
  }
  
  public static ATerm succ17(ATerm arg) {
    %match(term arg) {
      s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(z())))))))))))))))) -> {
        return `z();
      }
      x -> { return `s(x); }
    }
    return null;
  }

  
  %rule {
    pred17(s(x)) -> x 
    pred17(z()) -> s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(z())))))))))))))))) 
  }
  
  %rule {
    plus17(x,z()) -> x 
    plus17(x,s(y)) -> succ17(plus17(x,y)) 
  }

    
  %rule {
    mult17(x,z()) -> z()
    mult17(x,s(y)) -> plus17(x,mult17(x,y)) 
  }
  
  %rule {
    exp17(x,z()) -> succ17(z()) 
    exp17(x,s(y)) -> mult17(x,exp17(x,y)) 
  }
  
  %rule {
    eval(exz()) -> z()
    eval(exs(xs)) -> s(eval(xs)) 
    eval(explus(xs,ys)) -> plus(eval(xs), eval(ys)) 
    eval(exmult(xs,ys)) -> mult(eval(xs), eval(ys)) 
    eval(exexp(xs,ys)) -> exp(eval(xs), eval(ys)) 
  }
  
  %rule {
    evalsym17(exz()) -> z()
    evalsym17(exs(Xs)) -> succ17(evalsym17(Xs)) 
    evalsym17(explus(Xs,Ys)) -> plus17(evalsym17(Xs),evalsym17(Ys)) 
    evalsym17(exmult(Xs,exz())) -> z()
    evalsym17(exmult(Xs,exs(Ys))) -> evalsym17(explus(exmult(Xs,Ys),Xs)) 
    evalsym17(exmult(Xs,explus(Ys,Zs))) -> evalsym17(explus(exmult(Xs,Ys),exmult(Xs,Zs))) 
    evalsym17(exmult(Xs,exmult(Ys,Zs))) -> evalsym17(exmult(exmult(Xs,Ys),Zs)) 
    evalsym17(exmult(Xs,exexp(Ys,Zs))) -> evalsym17(exmult(Xs,dec(exexp(Ys,Zs)))) 
    evalsym17(exexp(Xs,exz())) -> succ17(z()) 
    evalsym17(exexp(Xs,exs(Ys))) -> evalsym17(exmult(exexp(Xs,Ys),Xs)) 
    evalsym17(exexp(Xs,explus(Ys,Zs))) -> evalsym17(exmult(exexp(Xs,Ys),exexp(Xs,Zs))) 
    evalsym17(exexp(Xs,exmult(Ys,Zs))) -> evalsym17(exexp(exexp(Xs,Ys),Zs)) 
    evalsym17(exexp(Xs,exexp(Ys,Zs))) -> evalsym17(exexp(Xs,dec(exexp(Ys,Zs))))  
  }

  public ATerm evalexp17(ATerm Xs) {
    return eval17(expand(Xs));
  }
    
  %rule {
      dec(exexp(Xs,exz)) -> exs(exz)
      dec(exexp(Xs,exs(Ys))) -> exmult(exexp(Xs,Ys),Xs)
      dec(exexp(Xs,explus(Ys,Zs))) -> exmult(exexp(Xs,Ys),exexp(Xs,Zs))
      dec(exexp(Xs,exmult(Ys,Zs))) -> dec(exexp(exexp(Xs,Ys),Zs))
      dec(exexp(Xs,exexp(Ys,Zs))) -> dec(exexp(Xs, dec(exexp(Ys,Zs)))) 
  }
  %rule {
    eval17(exone()) -> s(z()) 
    eval17(exz()) -> z()
    eval17(exs(xs)) -> succ17(eval17(xs))
    eval17(explus(xs,ys)) -> plus17(eval17(xs), eval17(ys))
    eval17(exmult(xs,ys)) -> mult17(eval17(xs), eval17(ys))
    eval17(exexp(xs,ys)) -> exp17(eval17(xs), eval(ys))
  }

  %rule {
    expand(exz()) -> exz() 
    expand(exone()) -> exone() 
    expand(exs(Xs)) -> explus(exone(),expand(Xs)) 
    expand(explus(Xs,Ys)) -> explus(expand(Xs),expand(Ys)) 
    expand(exmult(Xs,exz())) -> exz() 
    expand(exmult(Xs,exone())) -> expand(Xs) 
    expand(exmult(Xs,explus(Ys,Zs))) -> expand(explus(exmult(Xs,Ys),exmult(Xs,Zs))) 
    expand(exmult(Xs,Ys)) -> expand(exmult(Xs,expand(Ys))) 
    expand(exexp(Xs,exz())) -> exone() 
    expand(exexp(Xs,exone())) -> expand(Xs) 
    expand(exexp(Xs,explus(Ys,Zs))) -> expand(exmult(exexp(Xs,Ys),exexp(Xs,Zs))) 
    expand(exexp(Xs,Ys)) -> expand(exexp(Xs, expand(Ys))) 
  }

  %rule {
    getval(leaf(Val)) -> Val
    getval(node(Val,Max,Left,Right)) -> Val
  }

  %rule {
    getmax(leaf(Val)) -> Val
    getmax(node(Val,Max,Left,Right)) -> Max
  }

  public ATerm calctree17(ATerm X) {
    return mult17(exp17(`s(s(z())), pred17(X)),pred17(exp17(`s(s(z())),X)));
  }

}

