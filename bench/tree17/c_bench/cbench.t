/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2001  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
    Christophe Ringeissen	e-mail: Christophe.Ringeissen@loria.fr
    Marian Vittek		e-mail: vittek@guma.ii.fmph.uniba.sk

*/
#include <stdio.h>
#include <sys/time.h>
#include <sys/resource.h>
#include <sys/types.h>
#include <aterm1.h>
#include <aterm2.h>


static AFun ftrue, ffalse, fz, fs;
static AFun fequal,fplus,fmult,fexp,fsucc17,fpred17,fplus17,fmult17,fexp17;
static AFun fexz,fexs,fexplus,fexmult,fexexp,fleaf,fnode,fbuildtree,fcalctree17;
static AFun fgetmax,fgetval,feval,feval17,fevalsym17;
static AFun fdec, fexpand, fexone;

static ATermAppl ttrue, tfalse, tz, texz, texone;
  
%typeterm Bool {
  implement           { ATerm }
//  get_fun_sym(t)      { ATgetAFun(t) }
  equals(t1,t2)  { t1 == t2 }
//  get_subterm(t, n)   { ATgetArgument(t,n) }
}

%typeterm Nat {
  implement           { ATerm }
  equals(t1,t2)  { t1 == t2 }
}

%typeterm SNat {
  implement           { ATerm }
  equals(t1,t2)  { t1 == t2 }
}

%typeterm Tree {
  implement           { ATerm }
  equals(t1,t2)  { t1 == t2 }
}



%op Bool true() {
  is_fsym(t) { ATgetAFun(t) == ftrue }
  make() { (ATerm)ttrue }
}
%op Bool false() {
  is_fsym(t) { ATgetAFun(t) == ffalse }
  make() { (ATerm)tfalse }
}
%op Nat z() {
  is_fsym(t) { ATgetAFun(t) == fz }
  make() { (ATerm)tz }
}
%op Nat s(n1:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fs  }
  make(t) { (ATerm)ATmakeAppl1(fs,t) }
  get_slot(n1,t) { ATgetArgument(t,0) }
}
%op Nat plus(n1:Nat,n2:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fplus  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fplus,t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) }
}
%op Nat mult(n1:Nat,n2:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fmult  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fmult,t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) }
}
%op Nat exp(n1:Nat,n2:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fexp  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fexp,t1,t2) } 
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) }
}
%op Nat succ17(n1:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fsucc17  }
  make(t) { (ATerm)ATmakeAppl1(fsucc17,t) }
  get_slot(n1,t) { ATgetArgument(t,0) }
}
%op Nat pred17(n1:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fpred17  }
  make(t) { (ATerm)ATmakeAppl1(fpred17,t) }
  get_slot(n1,t) { ATgetArgument(t,0) }
}
%op Nat plus17(n1:Nat,n2:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fplus17  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fplus17,t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) } 
}
%op Nat mult17(n1:Nat,n2:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fmult17  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fmult17,t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) } 
}
%op Nat exp17(n1:Nat,n2:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fexp17  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fexp17,t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) } 
}
%op SNat exz() {
  is_fsym(t) { ATgetAFun(t) ==  fexz  }
  make() { (ATerm)texz }
}
%op SNat exs(s1:SNat) {
  is_fsym(t) { ATgetAFun(t) ==  fexs  }
  make(t) { (ATerm)ATmakeAppl1(fexs,t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op SNat explus(s1:SNat,s2:SNat) {
  is_fsym(t) { ATgetAFun(t) ==  fexplus  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fexplus,t1,t2) }
  get_slot(s1,t) { ATgetArgument(t,0) }
  get_slot(s2,t) { ATgetArgument(t,1) }
}
%op SNat exmult(s1:SNat,s2:SNat) {
  is_fsym(t) { ATgetAFun(t) ==  fexmult  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fexmult,t1,t2) }
  get_slot(s1,t) { ATgetArgument(t,0) }
  get_slot(s2,t) { ATgetArgument(t,1) }
}
%op SNat exexp(s1:SNat,s2:SNat) {
  is_fsym(t) { ATgetAFun(t) ==  fexexp  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fexexp,t1,t2) }
  get_slot(s1,t) { ATgetArgument(t,0) }
  get_slot(s2,t) { ATgetArgument(t,1) }
}
%op Tree leaf(n1:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fleaf  }
  make(t) { (ATerm)ATmakeAppl1(fleaf,t) }
  get_slot(n1,t) { ATgetArgument(t,0) }
}
%op Tree node(n1:Nat,n2:Nat,t3:Tree,t4:Tree) {
  is_fsym(t) { ATgetAFun(t) ==  fnode  }
  make(t1,t2,t3,t4) { (ATerm)ATmakeAppl4(fnode,t1,t2,t3,t4) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) }
  get_slot(t3,t) { ATgetArgument(t,2) }
  get_slot(t4,t) { ATgetArgument(t,3) }
}
%op Tree buildtree(n1:Nat,n2:Nat) {
  is_fsym(t) { ATgetAFun(t) ==  fbuildtree  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fbuildtree,t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) }
}
%op Nat getmax(t1:Tree) {
  is_fsym(t) { ATgetAFun(t) ==  fgetmax  }
  make(t) { (ATerm)ATmakeAppl1(fgetmax,t) }
  get_slot(t1,t) { ATgetArgument(t,0) }
}
%op Nat getval(t1:Tree) {
  is_fsym(t) { ATgetAFun(t) ==  fgetval  }
  make(t) { (ATerm)ATmakeAppl1(fgetval,t) }
  get_slot(t1,t) { ATgetArgument(t,0) }
}
%op Nat eval(s1:SNat) {
  is_fsym(t) { ATgetAFun(t) ==  feval  }
  make(t) { (ATerm)ATmakeAppl1(feval,t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op Nat eval17(s1:SNat) {
  is_fsym(t) { ATgetAFun(t) ==  feval17  }
  make(t) { (ATerm)ATmakeAppl1(feval17,t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op Nat evalsym17(s1:SNat) {
  is_fsym(t) { ATgetAFun(t) ==  fevalsym17  }
  make(t) { (ATerm)ATmakeAppl1(fevalsym17,t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op SNat dec(s1:SNat) {
  is_fsym(t) { ATgetAFun(t) ==  fdec  }
  make(t) { (ATerm)ATmakeAppl1(fdec,t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op SNat expand(s1:SNat) {
  is_fsym(t) { ATgetAFun(t) ==  fexpand  }
  make(t) { (ATerm)ATmakeAppl1(fexpand,t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op SNat exone() {
  is_fsym(t) { ATgetAFun(t) ==  fexone  }
  make { (ATerm)texone }
}


/* MACROS */
%op Nat calctree17(n1:Nat) {
  make(X) { (ATerm) mult17(exp17(s(s(z)), pred17(X)),pred17(exp17(s(s(z)),X))) }
}
%op Nat evalexp17(s1:SNat) {
  make(Xs) { (ATerm) eval17(expand(Xs)) }
}
  
void init() {
  ftrue = ATmakeAFun("true", 0, ATfalse);
  ffalse = ATmakeAFun("false", 0, ATfalse);
  fz = ATmakeAFun("z", 0, ATfalse);
  fs = ATmakeAFun("s", 1, ATfalse);
  
  fequal = ATmakeAFun("equal", 2, ATfalse);
  fplus = ATmakeAFun("plus", 2, ATfalse);
  fmult = ATmakeAFun("mult", 2, ATfalse);
  fexp = ATmakeAFun("exp", 2, ATfalse);
  
  fsucc17 = ATmakeAFun("succ17", 1, ATfalse);
  fpred17 = ATmakeAFun("pred17", 1, ATfalse);
  fplus17 = ATmakeAFun("plus17", 2, ATfalse);
  fmult17 = ATmakeAFun("mult17", 2, ATfalse);
  fexp17 = ATmakeAFun("exp17", 2, ATfalse);
    
  fexz = ATmakeAFun("exz", 0, ATfalse);
  fexs = ATmakeAFun("exs", 1, ATfalse);
  fexplus = ATmakeAFun("explus", 2, ATfalse);
  fexmult = ATmakeAFun("exmult", 2, ATfalse);
  fexexp = ATmakeAFun("exexp", 2, ATfalse);
  
  fleaf = ATmakeAFun("leaf", 1, ATfalse);
  fnode = ATmakeAFun("node", 4, ATfalse);
  fbuildtree = ATmakeAFun("buildtree", 2, ATfalse);
//  fcalctree17 = ATmakeAFun("calctree17", 2, ATfalse);
  fgetmax = ATmakeAFun("getmax", 1, ATfalse);
  fgetval = ATmakeAFun("getval", 1, ATfalse);
  feval = ATmakeAFun("eval", 1, ATfalse);
  feval17 = ATmakeAFun("eval17", 1, ATfalse);
  fevalsym17 = ATmakeAFun("evalsym17", 1, ATfalse);
//  fevalexp17 = ATmakeAFun("evalexp17", 1, ATfalse);
//  fbench_evalsym17 = ATmakeAFun("bench_evalsym17", 1, ATfalse);
//  fbench_evalexp17 = ATmakeAFun("bench_evalexp17", 1, ATfalse);
//  fbench_evaltree17 = ATmakeAFun("bench_evaltree17", 1, ATfalse);
  
  fdec = ATmakeAFun("dec", 1, ATfalse);
  fexpand = ATmakeAFun("expand", 1, ATfalse);
  fexone = ATmakeAFun("exone", 0, ATfalse);
  
  ttrue = ATmakeAppl0(ftrue);
  tfalse = ATmakeAppl0(ffalse);
  tz = ATmakeAppl0(fz);
  texz = ATmakeAppl0(fexz);
  texone = ATmakeAppl0(fexone);
}

ATerm build_exs(int n) {
  ATerm N = (ATerm)texz;
  int i;
  for(i=0 ; i<n ; i++) {
    N = (ATerm)ATmakeAppl1(fexs,N);
  }
  return N;
}
  
void run(int n) {
  ATerm res;
  struct rusage before_self,after_self;
  long diff_sec,diff_usec;
  double total_time;

  getrusage(RUSAGE_SELF, &before_self);
  res = bench_evalsym17(build_exs(n));
  getrusage(RUSAGE_SELF, &after_self);
  diff_sec  = after_self.ru_utime.tv_sec  - before_self.ru_utime.tv_sec;
  diff_usec = after_self.ru_utime.tv_usec - before_self.ru_utime.tv_usec;
  total_time= (((double)diff_sec)*1000000.0) + ((double)diff_usec);
  ATprintf("bench_evalsym17(%d) = %t in %d ms\n",n,res,(long)(total_time/1000.0));

  getrusage(RUSAGE_SELF, &before_self);
  res = bench_evalexp17(build_exs(n));
  getrusage(RUSAGE_SELF, &after_self);
  diff_sec  = after_self.ru_utime.tv_sec  - before_self.ru_utime.tv_sec;
  diff_usec = after_self.ru_utime.tv_usec - before_self.ru_utime.tv_usec;
  total_time= (((double)diff_sec)*1000000.0) + ((double)diff_usec);
  ATprintf("bench_evalexp17(%d) = %t in %d ms\n",n,res,(long)(total_time/1000.0));

  getrusage(RUSAGE_SELF, &before_self);
  res = bench_evaltree17(build_exs(n));
  getrusage(RUSAGE_SELF, &after_self);
  diff_sec  = after_self.ru_utime.tv_sec  - before_self.ru_utime.tv_sec;
  diff_usec = after_self.ru_utime.tv_usec - before_self.ru_utime.tv_usec;
  total_time= (((double)diff_sec)*1000000.0) + ((double)diff_usec);
  ATprintf("bench_evaltree17(%d) = %t in %d ms\n",n,res,(long)(total_time/1000.0));

  
}
  
int main(int argc, char **argv) {
  int i;
  ATerm     bottomOfStack;
    /*  Initialise the ATerm bits & pieces  */
  ATinit(argc, argv, &bottomOfStack);
  init();
  for(i=18 ; i<=22 ; i++) {
    run(i);
  }
  return(0);
}


ATerm equal(ATerm t1, ATerm t2) {
  if(t1==t2) {
    return (ATerm)ttrue;
  } else {
    return (ATerm)tfalse;
  }
}

ATerm bench_evalsym17(ATerm Xs) {
  ATerm Zs = (ATerm)tom_make_exexp(build_exs(2),Xs);
  return equal(eval17(Zs),evalsym17(Zs));
}

ATerm bench_evalexp17(ATerm Xs) {
  ATerm Zs = (ATerm)tom_make_exexp(build_exs(2),Xs);
  return equal(eval17(Zs),evalexp17(Zs));
}

ATerm bench_evaltree17(ATerm Xs) {
  ATerm y = eval(Xs);
  return equal(calctree17(y),getval(buildtree(y,(ATerm)tz)));
}

ATerm buildtree(ATerm t1, ATerm t2) {
  %match(Nat t1, Nat t2) {
    z,Val   -> { return (ATerm) `ATmakeAppl1(fleaf,Val); }
    s(X), Y -> {
      {
      ATerm Left = `buildtree(X, Y);	
      ATerm Max2 = getmax(Left);
      ATerm Right= `buildtree(X, succ17(Max2));
      ATerm Val2 = getval(Left);
      ATerm Val3 = getval(Right);
      ATerm Val  = plus17(Val2, Val3);
      ATerm Max  = getmax(Right);
      return ATmakeAppl4(fnode,Val, Max, Left, Right);
      }
    }
  }
}

  %rule {
    plus(x,z) -> x 
    plus(x,s(y)) -> s(plus(x,y)) 
  }

  %rule {
    mult(x,z) -> z 
    mult(x,s(y)) -> plus(mult(x,y),x) 
  }
  
  %rule {
    exp(x,z) -> s(z) 
    exp(x,s(y)) -> mult(x,exp(x,y)) 
  }
  
  %rule {
    succ17(s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(z))))))))))))))))) -> z 
    succ17(x) -> s(x) 
  }
  
  %rule {
    pred17(s(x)) -> x 
    pred17(z) -> s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(z)))))))))))))))) 
  }
  
  %rule {
    plus17(x,z) -> x 
    plus17(x,s(y)) -> succ17(plus17(x,y)) 
  }
  
  %rule {
    mult17(x,z) -> z 
    mult17(x,s(y)) -> plus17(x,mult17(x,y)) 
  }
  
  %rule {
    exp17(x,z) -> succ17(z) 
    exp17(x,s(y)) -> mult17(x,exp17(x,y)) 
  }
  
  %rule {
    eval(exz) -> z 
    eval(exs(xs)) -> s(eval(xs)) 
    eval(explus(xs,ys)) -> plus(eval(xs), eval(ys)) 
    eval(exmult(xs,ys)) -> mult(eval(xs), eval(ys)) 
    eval(exexp(xs,ys)) -> exp(eval(xs), eval(ys)) 
  }
  
  %rule {
    evalsym17(exz) -> z 
    evalsym17(exs(Xs)) -> succ17(evalsym17(Xs)) 
    evalsym17(explus(Xs,Ys)) -> plus17(evalsym17(Xs),evalsym17(Ys)) 
    evalsym17(exmult(Xs,exz)) -> z 
    evalsym17(exmult(Xs,exs(Ys))) ->
                evalsym17(explus(exmult(Xs,Ys),Xs)) 
    evalsym17(exmult(Xs,explus(Ys,Zs))) ->
                evalsym17(explus(exmult(Xs,Ys),exmult(Xs,Zs))) 
    evalsym17(exmult(Xs,exmult(Ys,Zs))) ->
                evalsym17(exmult(exmult(Xs,Ys),Zs)) 
    evalsym17(exmult(Xs,exexp(Ys,Zs))) ->
                evalsym17(exmult(Xs,dec(exexp(Ys,Zs)))) 
    evalsym17(exexp(Xs,exz)) -> succ17(z) 
    evalsym17(exexp(Xs,exs(Ys))) ->
                evalsym17(exmult(exexp(Xs,Ys),Xs)) 
    evalsym17(exexp(Xs,explus(Ys,Zs))) ->
                evalsym17(exmult(exexp(Xs,Ys),exexp(Xs,Zs))) 
    evalsym17(exexp(Xs,exmult(Ys,Zs))) ->
                evalsym17(exexp(exexp(Xs,Ys),Zs)) 
    evalsym17(exexp(Xs,exexp(Ys,Zs))) ->
                evalsym17(exexp(Xs,dec(exexp(Ys,Zs))))  
  }

  %rule {
    eval17(exone) -> s(z) 
    eval17(exz) -> z
    eval17(exs(xs)) -> succ17(eval17(xs))
    eval17(explus(xs,ys)) -> plus17(eval17(xs), eval17(ys))
    eval17(exmult(xs,ys)) -> mult17(eval17(xs), eval17(ys))
    eval17(exexp(xs,ys)) -> exp17(eval17(xs), eval(ys))
  }

  %rule {
    expand(exz) -> exz 
    expand(exone) -> exone 
    expand(exs(Xs)) -> explus(exone,expand(Xs)) 
    expand(explus(Xs,Ys)) -> explus(expand(Xs),expand(Ys)) 
    expand(exmult(Xs,exz)) -> exz 
    expand(exmult(Xs,exone)) -> expand(Xs) 
    expand(exmult(Xs,explus(Ys,Zs))) ->
                expand(explus(exmult(Xs,Ys),exmult(Xs,Zs))) 
    expand(exmult(Xs,Ys)) -> expand(exmult(Xs,expand(Ys))) 
    expand(exexp(Xs,exz)) -> exone 
    expand(exexp(Xs,exone)) -> expand(Xs) 
    expand(exexp(Xs,explus(Ys,Zs))) ->
                expand(exmult(exexp(Xs,Ys),exexp(Xs,Zs))) 
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

