// At least one or 2 type/constructor definitions
%typeterm type0 {
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}
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


%op type0 op0 {
  fsym { fzero }
  make() { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%op type1 op1(type1) {
  fsym { fzero }
  make(t1) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%op type2 op2(type1, type2) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%match(type t, type1 t2) {
}

%match(type1 t1, type1 t1) {
}

%match(type1 t1, type1 t2) {
  _     -> {} // Bad number of arguments: 2 argument(s) required but 1 found (line)
  _,_,_ -> {} // Bad number of arguments: 2 argument(s) required but 3 found (line)
  _*, X* -> {} // Single list variable `_*` is not allowed (line)
               // Single list variable `X*` is not allowed (line)
  op1[], op0() -> {} // Wrong type for slot 2:Type `type1` required but Type `type0` found (line)
}
