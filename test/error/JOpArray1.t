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

%oparray wrongType concArray1( type2* ) { // Symbol concArray1 has an unknown return type: wrongType (line18)
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty(n)  { factory.makeList() }
  make_append(e,l) { ((ATermList)l).insert((ATerm)e) }
}

%oparray type1 concArray2( wrongType* ) { // List symbol concArray2 has an unknown parameter type: wrongType (line24)
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty(n)  { factory.makeList() }
  make_append(e,l) { ((ATermList)l).insert((ATerm)e) }
}

%oparray type1 concArray3( type2* ) { // Missing macro-function(s) [ make_empty ] (line30)
  fsym { factory.makeAFun("conc", 1, false) }
  make_append(e,l) { ((ATermList)l).insert((ATerm)e) }
}

%oparray type1 concArray4( type2* ) { // Missing macro-function(s) [ make_append ] (line 35)
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty(n)  { factory.makeList() }
}

%oparray type1 concArray5( type2* ) {
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty(n)  { factory.makeList() }
  make_append(e,e) { ((ATermList)l).insert((ATerm)e) } // Arguments must be linear in method make_append: Variable e is repeated (line43)
}

%oparray type1 concArray6( type2* ) {
  fsym { factory.makeAFun("conc", 1, false) }
  make_empty(n)  { factory.makeList() }
  make_append(e,l) { ((ATermList)l).insert((ATerm)e) }
  make_append(e,l) { ((ATermList)l).insert((ATerm)e) } // Repeated macro-function make_append
}
