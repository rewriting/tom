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

%op type1 op1(type:type1) {
  fsym { fzero }
  make(t1) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}

%op type2 op2(type1, type2) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
}

%oplist type1 concList1( type2* ) {
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty()  { factory.makeList() }
  make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
}

%oplist type2 concList2( type1* ) {
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty()  { factory.makeList() }
  make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
}

%oplist type2 concList3( type1* ) {
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty()  { factory.makeList() }
  make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
}

%match(type2 t, type2 u, type1 v, type0 w) {
  op2(_), _      , _, _  -> {} // Bad number of arguments for symbol `op2`: 2 argument(s) required but 1 found (line 62)
  
  _, op2(_, _, _), _, _  -> {} // Bad number of arguments for symbol `op2`: 2 argument(s) required but 3 found (line 64)
  
  _, ()         , _ , _  -> {} // Too many list symbols with returned type `type2`:  concList2 concList3 (line 66)
  
  _, _          ,() , _  -> {} // OK: Only one list symbol with returned type `type1`
  
  _,   _,   _,        () -> {} // Not able to found a list symbol of type: `type0 (line 70)
  
  op1(_), op1(_), _ , _  -> {} // The symbol op1 has type `type1` but type `type2` was required (x2) (line 72)

  op2(_*, X*), op2(_,_), _, _   -> {} // _* ( && X*) is not allowed in non list symbol (line 74)
  
  _, opVariable, _, _   -> {} // OK: variable
  
  _, opError(_,_), _, _   -> {} // Unknown symbol opError (line78)
  
  _, opError2(), _, _   -> {} // Unknown symbol opError2 (line 80)
  
  _, _, (op0), _   -> {} // Warning: Ambiguous symbol name. Is`op0` a variable or a constructor? Prefer `op0`() if it is a constructor (line 82)
    //The symbol op0 has type `type0` but type `type2` was required (line 82)

  _, _, (op0()), _   -> {} // The symbol op0 has type `type0` but type `type2` was required (line 85)

  _, _, (op2(_, op2(_, op2(_, op2(oneArgsOnly))))), _   -> {} // The symbol op0 has type `type0` but type `type2` was required (line 87)

  
}
