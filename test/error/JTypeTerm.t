%typeterm type1 { // Missing macro-function(s) [ get_fun_sym ] (line1)
  implement { String }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}

%typeterm type2 { // Missing macro-function(s) [ cmp_fun_sym ] (line8)
  implement { String }
  get_fun_sym(t) { t }
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}

%typeterm type3 { // Missing macro-function(s) [ get_subterm ] (line 15)
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  equals(t1,t2) { t1.equals(t2) }
}

%typeterm type4 { // Missing macro-function(s) [ get_fun_sym,  cmp_fun_sym,  get_subterm ] (line 22)
  implement { String }
}

%typeterm type5 { // No error; Missing optional make macro
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
}

%typeterm type6 {
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
  get_fun_sym(t) { t } // Repeated macro-function get_fun_sym (line 38)
  equals(t1,t2) { t1.equals(t2) }
}

%typeterm type7 {
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s1) { s1.equals(s2) } // Arguments must be linear in method cmp_fun_sym :Variable s1 is repeated (line 45)
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}

%typeterm type8 {
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
  equals(t1,t1) { t1.equals(t2) } // Arguments must be linear in method equals :Variable t1 is repeated (line 55)
}

%typeterm type9 {
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}

%typeterm type9 { // Multiple definition of Symbol type9 (line 66)
  implement { String }
  get_fun_sym(t) { t }
  cmp_fun_sym(s1,s2) { s1.equals(s2) }
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}
