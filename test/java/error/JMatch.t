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

%match(type t, type1 t2) { // Variable `t` has an unknown type: `type` (line 30)
}

%match(type1 t1, type1 t1) { // Warning: Repeated variable `t1` in `match` declaration (line 33) 
}

%match(type1 t1, type1 t2) {
  _     -> {} // Bad number of arguments: 2 argument(s) required but 1 found (line 37)
  
  _,_,_ -> {} // Bad number of arguments: 2 argument(s) required but 3 found (line 39)
  
  _*, X* -> {} // Single list variable `_*` is not allowed (line 41)
               // Single list variable `X*` is not allowed (line 41)
  
  op1[], op0() -> {} // Wrong type for slot 2:Type `type1` required but Type `type0` found (line 44)
}
