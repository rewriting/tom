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

%op type1 op1(type:type1, type1) {
  fsym { fzero }
  make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}
%op type1 op2(type:type2, type2) {
  fsym { fzero }
    //make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}

%op type2 op3 {
  fsym { fzero }
    //make(t1,t2) { factory.makeAppl(fzero) }
  is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  get_slot(type,t) { t.getType() }
}


%rule {
  x@_ -> x // `_`: Impossible in rule left hand side
}

%rule {
  x@X* -> x // `X*`: Impossible in rule left hand side
}

%rule {
  x@_* -> x // `_*`: Impossible in rule left hand side
}

%rule {
  x@<a></a> -> x // `XML Construct a`: Impossible in rule left hand side
}

%rule {
  x@(op1|op2)() -> x // `Disjunction`: Impossible in rule left hand side
}
/*
%rule {
  (op1|op2)[] -> f() // `Disjunction`: Impossible in rule left hand side
}*/

%rule {
  op2(_,_) -> f() // Rule head symbol `op2` has no `make` method: It is necessary to define one!!
}

%rule {
  op1(_,_) -> f()
    op2(_,_) -> f() // Rule head symbol name `op1` expected, but `op2` found
}

%rule {
  op1(_,_) -> f()
    op1(_, op1(_,op1())) -> f() // Bad number of arguments for symbol `op1`: 2 argument(s) required but 0 found
    op1(_, op1(_,op3)) -> f() // The symbol `op3` has type `type2` but type `type1` was required
}

%rule {
  op3 -> op2(op3, op3) //The symbol `op2` has type `type1` but type `type2` was required
    op3 -> _ //`_`: Impossible in rule right hand side
  op3 -> _*  //`_*`: Impossible in rule right hand side
    op3 -> X*  // //`X*`: Impossible in rule right hand side
  op3 -> <a></a> //`XML construct a`: Impossible in rule right hand side
  op3 -> (op1|op2)() //`Disjunction`: Impossible in rule right hand side
}
%rule {
  op1[] -> op1(op1(_,op1(_)) ,_)
// `_`: Impossible in rule right hand side
      //Bad number of arguments for symbol `op1`: 2 argument(s) required but 1 found
//    Bad number of arguments for symbol `op1`: 2 argument(s) required but 1 found
    op1[] -> op1(op1(_,op1[]) ,_) //`op1[...]`: Impossible in rule right hand side
}
