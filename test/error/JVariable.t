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

%op type1 op(type:type1, type1) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}

%op type0 op2(type:type1, type2) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}


%rule {
  x@op2(x, x) -> x
// Bad variable type for `x`: it has both type `type1` and `type2`(line 41)
// Variable `x` has type `type0` instead of type `type1`
}

%rule {
  v@op(w@_, x@op[type=y@z]) -> v
  v@op(w@_, x@op[type=y@z]) -> w
  v@op(w@_, x@op[type=y@z]) -> x
  v@op(w@_, x@op[type=y@z]) -> y
  v@op(w@_, x@op[type=y@z]) -> z
  v@op(w@_, x@op[type=y@z]) -> a //Unknown variable(s) `a` (line 52)
    
  a@op(w@_, x@op[type=y@z]) -> a() // OK:
}
