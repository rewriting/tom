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

%oplist type1 concList( type2* ) {
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty()  { factory.makeList() }
  make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
}

%op type1 op2 {
  fsym { fzero }
  make() { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%match(type1 t) {
  concList(X*,op2,X*) -> {}
}
