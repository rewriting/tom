%typeterm type1 {
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

%typelist TomList{
  implement { TomList}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  equals(t1,t2) {t1.equals(t2)}
  get_head(l) {l.getHead()}
  get_tail(l) {l.getTail()}
  is_empty(l) {l.isEmpty()}
}

%oplist TomList concTomTerm(type1*) {
  fsym { null }
  is_fsym(t) {(t!= null) && t.isSortTomList()}
  make_empty() {getTomSignatureFactory().makeTomList()}
  make_insert(e,l) {getTomSignatureFactory().makeTomList(e,l)}
}

%match(type1 t, TomList t2) {
  _,_ -> {}
}
