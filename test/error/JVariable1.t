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

%op type1 op(type:type1, type1) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}

%op type1 op2(type:type1, type2) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}


%rule {
  y@op2(x, x) -> y
}

%rule {
  v@op(w@_, x@op[type=y@z]) -> v
  v@op(w@_, x@op[type=y@z]) -> w
  v@op(w@_, x@op[type=y@z]) -> x
  v@op(w@_, x@op[type=y@z]) -> y
  v@op(w@_, x@op[type=y@z]) -> z
  v@op(w@_, x@op[type=y@z]) -> a
}
