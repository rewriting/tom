// At least one or 2 type definitions
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

%op wrongtype op1(type1, type1) { // Operator op1 has an unknown return type: wrongtype (line18)
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%op type1 op2(wrongtype, type1) { // Slot 1 of op2 signature has an unknown type: wrongtype (line24)
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}
%op type1 op2bis(type1, wrongtype) { // Slot 2 of op2bis signature has an unknown type: wrongtype (line29)
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%op type1 op3(type1, type1) {
  fsym { fzero }
  make(t1, t2) { factory.makeAppl(fzero) }
  make(t1, t2) { factory.makeAppl(fzero) } // Repeated macro-function make (line38)
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%op type1 op4(type1, type1) {
  fsym { fzero }
  make(t1,t1) { factory.makeAppl(fzero) } // Arguments must be linear in method make: Variable t1 is repeated (line 44)
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%op type1 op5(type8) { // No message: Operator defined before type8 declaration
  fsym { fzero }
  make(t1) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%typeterm type8 {
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}
