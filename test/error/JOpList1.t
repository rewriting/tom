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

%oplist wrongtype concList1( type2* ) { // Symbol concList1 has an unknown return type: wrongtype (line17)
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty()  { factory.makeList() }
  make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
}

%oplist type1 concList2( wrongtype* ) { // List symbol concList2 has an unknown parameter type: wrongtype (line23)
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty()  { factory.makeList() }
  make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
}

%oplist type1 concList3( type2* ) { // Missing macro-function(s) [ make_empty ] (line29)
  fsym { factory.makeAFun("conc", 1, false) }
  make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
}

%oplist type1 concList4( type2* ) { // Missing macro-function(s) [ make_insert ] (line34)
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty()  { factory.makeList() }
}

%oplist type1 concList5( type2* ) {
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty()  { factory.makeList() }
  make_insert(e,e) { ((ATermList)l).insert((ATerm)e) } // Arguments must be linear in method make_insert: Variable e is repeated (line42)
}

%oplist type1 concList6( type2* ) {
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty()  { factory.makeList() }
  make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
  make_empty()  { factory.makeList() } // Repeated macro-function make_empty (line49)
}
