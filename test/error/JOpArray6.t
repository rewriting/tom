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

%oparray type1 concArray( type2* ) {
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty(n)  { factory.makeList() }
  make_append(e,l) { ((ATermList)l).insert((ATerm)e) }
  make_append(e,l) { ((ATermList)l).insert((ATerm)e) }
}
