%typeterm type1 {
  implement { String }
  get_fun_sym(t) { t }
  get_subterm(t,n) { null }
  equals(t1,t2) { t1.equals(t2) }
}
