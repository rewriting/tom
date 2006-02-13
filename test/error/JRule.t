%typeterm type1 {
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}

%typeterm type2 {
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}

%op type1 op1(type:type1, type2) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}
%op type1 op2(type:type1, type1) {
  fsym { fzero }
    //make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}

%op type1 op3() {
  fsym { fzero }
  make() { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%op type1 op4() {
  fsym { fzero }
  make() { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}
%op type2 op5(type:type1, type1) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}


%rule {
  x@_ -> x // `_`: Impossible in rule left hand side (line 50)
}

%rule {
  x@X* -> x // `X*`: Impossible in rule left hand side (line 54)
}

%rule {
  x@_* -> x // `_*`: Impossible in rule left hand side (line 58)
}

%rule {
  x@<a></a> -> x // `XML Construct a`: Impossible in rule left hand side (line 62)
}

%rule {
  x@(op1|op2)() -> x // `Disjunction`: Impossible in rule left hand side (line 66)
}

%rule {
  (op1|op2)[] -> f() // `Disjunction`: Impossible in rule left hand side (line 70)
}

%rule {
  x@op2(_,_) -> x // Rule head symbol `op2` has no `make` method: It is necessary to define one!! (line 74)
}

%rule {
  x@op1(_,_) -> f()  // Unknown symbol `f`: Cant do further analyses (line 78)
  x@op2(_,_) -> x // Rule head symbol name `op1` expected, but `op2` found (line 79)
}

%rule {
  x@op5(_,_) -> x
  x@op5(_, op1(_,op1())) -> x // Bad number of arguments for symbol `op1`: 2 argument(s) required but 0 found (line 84)
  x@op5(_, op1(_,op4)) -> x // The symbol `op4` has type `type1` but type `type2` was required (line 85) + ambigous op4
}

%rule {
  op3() -> op5(op3(), op3()) //The symbol `op5` has type `type2` but type `type1` was required (line 89)
  op3() -> _ //`_`: Impossible in rule right hand side (line 90)
  op3() -> _*  //`_*`: Impossible in rule right hand side (line 91)
  op3() -> X*  // //`X*`: Impossible in rule right hand side (line 92)
  op3() -> <a></a> //`XML construct a`: Impossible in rule right hand side (line 93)
  op3() -> (op1|op2)() //`Disjunction`: Impossible in rule right hand side (line 94)
}
%rule {
  op4[] -> op2(op2(_,op2(_)) ,_)
// `_`: Impossible in rule right hand side (line 97)
// Bad number of arguments for symbol `op1`: 2 argument(s) required but 1 found (line 97)
// `_`: Impossible in rule right hand side  (line 97)
  op4[] -> op2(op2(_,op2[]) ,_)
// `_`: Impossible in rule right hand side (line 101)
//`op1[...]`: Impossible in rule right hand side (line 101)
// `_`: Impossible in rule right hand side (line 101)
}

%rule {
  x@op4[] -> x // Rule `op4` is repeated
}

