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

%op type1 op(slot1:type1, slot2:type1) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(slot1,t) { t.getType() }
  get_slot(slot2,t) { t.getType() }
}

%oplist type1 concList1( type2* ) {
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty()  { factory.makeList() }
  make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
}


%match(type1 u, type1 v) { 
  op[badslot=x], _ -> {} // Slot Name `slot` is not correct for symbol `op`. Possible slot names are : [slot1, slot2] (line 33)

  _, concList1[] -> {} // [] are not allowed on lists or arrays, see `concList1`(line 35)

  op[slot1=_, slot1=_], _ -> {} // Same slot names can not be used several times in symbol `op`: Repeated slot Name : `slot1` (line 37)

  op[slot1=_, slot2=_], _ -> {}

  opp[slot1=_, slot1=_], _ -> {} // Unknown symbol opp (line 41)
  
}
