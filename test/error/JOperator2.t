// At least one or 2 type definitions
%typeterm type1 {
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}

%op type1 op9(slot1:type1, slot2: type1) { // Warning: GetSlot declaration `get_slot(slot2,...)` is missing. Slot slot2 will not be considered
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(badslot,t) { t.getType() } // Incompatible GetSlot declaration: badslot does not appears in symbol signature (line14)
  get_slot(slot1,t) { t.getType() }
}

%op type1 op7(type:type1, type1) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
  get_slot(type,t) { t.getType() } // GetSlot declaration 'get_slot(type,...)' is repeated in operator declaration (line24)
}
