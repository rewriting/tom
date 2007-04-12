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
static AFun fexz,fexs,fexplus,fexmult,fexexp,fleaf,fnode,fcalctree17;
static AFun fgetmax,fgetval,feval,feval17,fevalsym17;
static AFun fdec, fexpand, fexone;

static ATermAppl ttrue, tfalse, tz, texz, texone;


ATerm hook_plus(ATerm t1,ATerm t2);
ATerm hook_mult(ATerm t1,ATerm t2);
ATerm hook_exp(ATerm t1,ATerm t2);
ATerm hook_succ17(ATerm t);
ATerm hook_pred17(ATerm t);
ATerm hook_plus17(ATerm t1,ATerm t2);
ATerm hook_mult17(ATerm t1,ATerm t2);
ATerm hook_exp17(ATerm t1,ATerm t2);
ATerm hook_eval(ATerm t);
ATerm hook_evalsym17(ATerm t);
ATerm hook_eval17(ATerm t);
ATerm hook_expand(ATerm t);
ATerm hook_getval(ATerm t);
ATerm hook_getmax(ATerm t);


%typeterm term {
  implement      { ATerm }
  is_sort(t)     { (0==0) }
  equals(t1,t2)  { t1 == t2 }
}

%op term true() {
  is_fsym(t) { ATgetAFun(t) == ftrue }
  make() { (ATerm)ttrue }
}
%op term false() {
  is_fsym(t) { ATgetAFun(t) == ffalse }
  make() { (ATerm)tfalse }
}
%op term z() {
  is_fsym(t) { ATgetAFun(t) == fz }
  make() { (ATerm)tz }
}
%op term s(n1:term) {
  is_fsym(t) { ATgetAFun(t) ==  fs  }
  make(t) { (ATerm)ATmakeAppl1(fs,t) }
  get_slot(n1,t) { ATgetArgument(t,0) }
}
%op term plus(n1:term,n2:term) {
  is_fsym(t) { ATgetAFun(t) ==  fplus  }
  make(t1,t2) { hook_plus(t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) }
}
%op term mult(n1:term,n2:term) {
  is_fsym(t) { ATgetAFun(t) ==  fmult  }
  make(t1,t2) { hook_mult(t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) }
}
%op term exp(n1:term,n2:term) {
  is_fsym(t) { ATgetAFun(t) ==  fexp  }
  make(t1,t2) { hook_exp(t1,t2) } 
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) }
}
%op term succ17(n1:term) {
  is_fsym(t) { ATgetAFun(t) ==  fsucc17  }
  make(t) { hook_succ17(t) }
  get_slot(n1,t) { ATgetArgument(t,0) }
}
%op term pred17(n1:term) {
  is_fsym(t) { ATgetAFun(t) ==  fpred17  }
  make(t) { hook_pred17(t) }
  get_slot(n1,t) { ATgetArgument(t,0) }
}
%op term plus17(n1:term,n2:term) {
  is_fsym(t) { ATgetAFun(t) ==  fplus17  }
  make(t1,t2) { hook_plus17(t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) } 
}
%op term mult17(n1:term,n2:term) {
  is_fsym(t) { ATgetAFun(t) ==  fmult17  }
  make(t1,t2) { hook_mult17(t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) } 
}
%op term exp17(n1:term,n2:term) {
  is_fsym(t) { ATgetAFun(t) ==  fexp17  }
  make(t1,t2) { hook_exp17(t1,t2) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) } 
}
%op term exz() {
  is_fsym(t) { ATgetAFun(t) ==  fexz  }
  make() { (ATerm)texz }
}
%op term exs(s1:term) {
  is_fsym(t) { ATgetAFun(t) ==  fexs  }
  make(t) { (ATerm)ATmakeAppl1(fexs,t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op term explus(s1:term,s2:term) {
  is_fsym(t) { ATgetAFun(t) ==  fexplus  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fexplus,t1,t2) }
  get_slot(s1,t) { ATgetArgument(t,0) }
  get_slot(s2,t) { ATgetArgument(t,1) }
}
%op term exmult(s1:term,s2:term) {
  is_fsym(t) { ATgetAFun(t) ==  fexmult  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fexmult,t1,t2) }
  get_slot(s1,t) { ATgetArgument(t,0) }
  get_slot(s2,t) { ATgetArgument(t,1) }
}
%op term exexp(s1:term,s2:term) {
  is_fsym(t) { ATgetAFun(t) ==  fexexp  }
  make(t1,t2) { (ATerm)ATmakeAppl2(fexexp,t1,t2) }
  get_slot(s1,t) { ATgetArgument(t,0) }
  get_slot(s2,t) { ATgetArgument(t,1) }
}
%op term leaf(n1:term) {
  is_fsym(t) { ATgetAFun(t) ==  fleaf  }
  make(t) { (ATerm)ATmakeAppl1(fleaf,t) }
  get_slot(n1,t) { ATgetArgument(t,0) }
}
%op term node(n1:term,n2:term,t3:term,t4:term) {
  is_fsym(t) { ATgetAFun(t) ==  fnode  }
  make(t1,t2,t3,t4) { (ATerm)ATmakeAppl4(fnode,t1,t2,t3,t4) }
  get_slot(n1,t) { ATgetArgument(t,0) }
  get_slot(n2,t) { ATgetArgument(t,1) }
  get_slot(t3,t) { ATgetArgument(t,2) }
  get_slot(t4,t) { ATgetArgument(t,3) }
}

%op term getmax(t1:term) {
  is_fsym(t) { ATgetAFun(t) ==  fgetmax  }
  make(t) { hook_getmax(t) }
  get_slot(t1,t) { ATgetArgument(t,0) }
}
%op term getval(t1:term) {
  is_fsym(t) { ATgetAFun(t) ==  fgetval  }
  make(t) { hook_getval(t) }
  get_slot(t1,t) { ATgetArgument(t,0) }
}
%op term eval(s1:term) {
  is_fsym(t) { ATgetAFun(t) ==  feval  }
  make(t) { hook_eval(t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op term eval17(s1:term) {
  is_fsym(t) { ATgetAFun(t) ==  feval17  }
  make(t) { hook_eval17(t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op term evalsym17(s1:term) {
  is_fsym(t) { ATgetAFun(t) ==  fevalsym17  }
  make(t) { hook_evalsym17(t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op term dec(s1:term) {
  is_fsym(t) { ATgetAFun(t) ==  fdec  }
  make(t) { (ATerm)ATmakeAppl1(fdec,t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op term expand(s1:term) {
  is_fsym(t) { ATgetAFun(t) ==  fexpand  }
  make(t) { hook_expand(t) }
  get_slot(s1,t) { ATgetArgument(t,0) }
}
%op term exone() {
  is_fsym(t) { ATgetAFun(t) ==  fexone  }
  make { (ATerm)texone }
}


/* MACROS */
%op term calctree17(n1:term) {
  make(X) { (ATerm) `mult17(exp17(s(s(z())), pred17(X)),pred17(exp17(s(s(z())),X))) }
}
%op term evalexp17(s1:term) {
  make(Xs) { (ATerm) `eval17(expand(Xs)) }
}
  
void init() {
  ftrue = ATmakeAFun("true", 0, ATfalse);
  ffalse = ATmakeAFun("false", 0, ATfalse);
  fz = ATmakeAFun("z", 0, ATfalse);
  fs = ATmakeAFun("s", 1, ATfalse);
  
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

  fgetmax = ATmakeAFun("getmax", 1, ATfalse);
  fgetval = ATmakeAFun("getval", 1, ATfalse);
  feval = ATmakeAFun("eval", 1, ATfalse);
  feval17 = ATmakeAFun("eval17", 1, ATfalse);
  fevalsym17 = ATmakeAFun("evalsym17", 1, ATfalse);
  
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

ATerm buildtree(ATerm t1, ATerm t2) {
  %match(term t1, term t2) {
    z,Val   -> { return (ATerm) `ATmakeAppl1(fleaf,Val); }
    s(X), Y -> {
      ATerm Left = `buildtree(X, Y);	
      ATerm Max2 = `getmax(Left);
      ATerm Right= `buildtree(X, succ17(Max2));
      ATerm Val2 = `getval(Left);
      ATerm Val3 = `getval(Right);
      ATerm Val  = `plus17(Val2, Val3);
      ATerm Max  = `getmax(Right);
      return (ATerm) ATmakeAppl4(fnode,Val, Max, Left, Right);
    }
  }
  return (ATerm) NULL;
}

ATerm equal(ATerm t1, ATerm t2) {
  if(t1==t2) {
    return `true();
  } else {
    return `false();
  }
}

ATerm bench_evalsym17(ATerm Xs) {
  ATerm Zs = `exexp(build_exs(2),Xs);
  return `equal(eval17(Zs),evalsym17(Zs));
}

ATerm bench_evalexp17(ATerm Xs) {
  ATerm Zs = `exexp(build_exs(2),Xs);
  return `equal(eval17(Zs),evalexp17(Zs));
}

ATerm bench_evaltree17(ATerm Xs) {
  ATerm y = `eval(Xs);
  ATprintf("calctree17(y) = %t\n", `calctree17(y));
  ATprintf("getval... = %t\n", `getval(buildtree(y,z())) );
  return `equal(calctree17(y),getval(buildtree(y,z())));
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

ATerm hook_plus(ATerm t1, ATerm t2) {
  %match(term t1, term t2) {
    x,z() -> { return `x; }
    x,s(y) -> { return `s(plus(x,y)); }
  }
  return (ATerm)ATmakeAppl2(fplus,t1,t2); 
}

ATerm hook_mult(ATerm t1, ATerm t2) {
  %match(term t1, term t2) {
    x,z() -> { return `z(); }
    x,s(y) -> { return `plus(mult(x,y),x); } 
  }
  return (ATerm)ATmakeAppl2(fmult,t1,t2); 
}
 

ATerm hook_exp(ATerm t1,ATerm t2) {
  %match(term t1, term t2) {
    x,z() -> { return `s(z()); }
    x,s(y) -> { return `mult(x,exp(x,y)); }
  }
  return (ATerm)ATmakeAppl2(fexp,t1,t2); 
}

ATerm hook_succ17(ATerm t) {
  %match(term t) {
    s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(z())))))))))))))))) -> { return `z(); } 
    x -> { return `s(x); }
  }
  return (ATerm)ATmakeAppl1(fsucc17,t); 
}

ATerm hook_pred17(ATerm t) {
  %match(term t) {
    s(x) -> { return `x; } 
    z() -> { return `s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(s(z())))))))))))))))); }
  }
  return (ATerm)ATmakeAppl1(fpred17,t); 
}

ATerm hook_plus17(ATerm t1, ATerm t2) {
  %match(term t1, term t2) {
    x,z() -> { return `x; }
    x,s(y) -> { return `succ17(plus17(x,y)); }
  }
  return (ATerm)ATmakeAppl2(fplus17,t1,t2); 
}

ATerm hook_mult17(ATerm t1, ATerm t2) {
  %match(term t1, term t2) {
    x,z() -> { return `z(); } 
    x,s(y) -> { return `plus17(x,mult17(x,y)); }
  }
  return (ATerm)ATmakeAppl2(fmult17,t1,t2); 
}
  

ATerm hook_exp17(ATerm t1, ATerm t2) {
  %match(term t1, term t2) {
    x,z() -> { return `succ17(z()); }
    x,s(y) -> { return `mult17(x,exp17(x,y)); }
  }
  return (ATerm)ATmakeAppl2(fexp17,t1,t2); 
}
 
ATerm hook_eval(ATerm t) {
  %match(term t) {
    exz() -> { return `z(); }
    exs(xs) -> { return `s(eval(xs)); }
    explus(xs,ys) -> { return `plus(eval(xs), eval(ys)); }
    exmult(xs,ys) -> { return `mult(eval(xs), eval(ys)); }
    exexp(xs,ys) -> { return `exp(eval(xs), eval(ys)); }
  }
  return (ATerm)ATmakeAppl1(feval,t); 
}

ATerm hook_evalsym17(ATerm t) {
  %match(term t) {
    exz() -> { return `z(); }
    exs(Xs) -> { return `succ17(evalsym17(Xs)); }
    explus(Xs,Ys) -> { return `plus17(evalsym17(Xs),evalsym17(Ys)); }
    exmult(Xs,exz()) -> { return `z(); }
    exmult(Xs,exs(Ys)) ->
    { return `evalsym17(explus(exmult(Xs,Ys),Xs)); }
    exmult(Xs,explus(Ys,Zs)) ->
    { return `evalsym17(explus(exmult(Xs,Ys),exmult(Xs,Zs))); }
    exmult(Xs,exmult(Ys,Zs)) ->
    { return `evalsym17(exmult(exmult(Xs,Ys),Zs)); }
    exmult(Xs,exexp(Ys,Zs)) ->
    { return `evalsym17(exmult(Xs,dec(exexp(Ys,Zs)))); }
    exexp(Xs,exz()) -> { return `succ17(z()); }
    exexp(Xs,exs(Ys)) ->
    { return `evalsym17(exmult(exexp(Xs,Ys),Xs)); }
    exexp(Xs,explus(Ys,Zs)) ->
    { return `evalsym17(exmult(exexp(Xs,Ys),exexp(Xs,Zs))); }
    exexp(Xs,exmult(Ys,Zs)) ->
    { return `evalsym17(exexp(exexp(Xs,Ys),Zs)); }
    exexp(Xs,exexp(Ys,Zs)) ->
    { return `evalsym17(exexp(Xs,dec(exexp(Ys,Zs)))); }
  }
  return (ATerm)ATmakeAppl1(fevalsym17,t); 
}


ATerm hook_eval17(ATerm t) {
  %match(term t) {
    exone() -> { return `s(z()); }
    exz() -> { return `z(); }
    exs(xs) -> { return `succ17(eval17(xs)); }
    explus(xs,ys) -> { return `plus17(eval17(xs), eval17(ys)); }
    exmult(xs,ys) -> { return `mult17(eval17(xs), eval17(ys)); }
    exexp(xs,ys) -> { return `exp17(eval17(xs), eval(ys)); }
  }
  return (ATerm)ATmakeAppl1(feval17,t); 

}

ATerm hook_expand(ATerm t) {
  %match(term t) {
    exz() -> { return `exz(); }
    exone() -> { return `exone(); }
    exs(Xs) -> { return `explus(exone(),expand(Xs)); }
    explus(Xs,Ys) -> { return `explus(expand(Xs),expand(Ys)); }
    exmult(Xs,exz()) -> { return `exz(); } 
    exmult(Xs,exone()) -> { return `expand(Xs); }
    exmult(Xs,explus(Ys,Zs)) ->
    { return `expand(explus(exmult(Xs,Ys),exmult(Xs,Zs))); }
    exmult(Xs,Ys) -> { return `expand(exmult(Xs,expand(Ys))); }
    exexp(Xs,exz()) -> { return `exone(); }
    exexp(Xs,exone()) -> { return `expand(Xs); }
    exexp(Xs,explus(Ys,Zs)) ->
    { return `expand(exmult(exexp(Xs,Ys),exexp(Xs,Zs))); }
    exexp(Xs,Ys) -> { return `expand(exexp(Xs, expand(Ys))); }
  }
  return (ATerm)ATmakeAppl1(fexpand,t); 
}

ATerm hook_getval(ATerm t) {
%match(term t) {
    leaf(Val) -> { return `Val; }
    node(Val,Max,Left,Right) -> { return `Val; }
  }
  return (ATerm)ATmakeAppl1(fgetval,t); 
}

ATerm hook_getmax(ATerm t) {
%match(term t) {
    leaf(Val) -> { return `Val; }
    node(Val,Max,Left,Right) -> { return `Max; }
  }
  return (ATerm)ATmakeAppl1(fgetmax,t); 
}

