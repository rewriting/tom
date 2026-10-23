%typelist type1 { // Missing macro-function(s) [ get_fun_sym ] (line1)
  implement { ATermList }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l2)  { l1==l2 }
  get_head(l)    { ((ATermList)l).getFirst() }
  get_tail(l)    { ((ATermList)l).getNext() }
  is_empty(l)    { ((ATermList)l).isEmpty() }
}

%typelist type2 { // Missing macro-function(s) [ cmp_fun_sym ] (line10)
  implement { ATermList }
  get_fun_sym(t) {  }
  equals(l1,l2)  { l1==l2 }
  get_head(l)    { ((ATermList)l).getFirst() }
  get_tail(l)    { ((ATermList)l).getNext() }
  is_empty(l)    { ((ATermList)l).isEmpty() }
}

%typelist type3 { // Missing macro-function(s) [ get_head ] (line19)
  implement { ATermList }
  get_fun_sym(t) {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l2)  { l1==l2 }
  get_tail(l)    { ((ATermList)l).getNext() }
  is_empty(l)    { ((ATermList)l).isEmpty() }
}

%typelist type4 { // Missing macro-function(s) [ get_tail ] (line28)
  implement { ATermList }
  get_fun_sym(t) {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l2)  { l1==l2 }
  get_head(l)    { ((ATermList)l).getFirst() }
  is_empty(l)    { ((ATermList)l).isEmpty() }
}

%typelist type5 { // Missing macro-function(s) [ is_empty ] (line37)
  implement { ATermList }
  get_fun_sym(t) {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l2)  { l1==l2 }
  get_head(l)    { ((ATermList)l).getFirst() }
  get_tail(l)    { ((ATermList)l).getNext() }
}

%typelist type6 {  // No error; Missing optional make macro
  implement { ATermList }
  get_fun_sym(t) {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  get_head(l)    { ((ATermList)l).getFirst() }
  get_tail(l)    { ((ATermList)l).getNext() }
  is_empty(l)    { ((ATermList)l).isEmpty() }
}

%typelist type7 {
  implement { ATermList }
  get_fun_sym(t) {  }
  cmp_fun_sym(t1,t1) { t1 == t2 } // Arguments must be linear in method cmp_fun_sym: Variable t1 is repeated (line 58)
  equals(l1,l2)  { l1==l2 }
  get_head(l)    { ((ATermList)l).getFirst() }
  get_tail(l)    { ((ATermList)l).getNext() }
  is_empty(l)    { ((ATermList)l).isEmpty() }
}

%typelist type8 {
  implement { ATermList }
  get_fun_sym(t) {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l1)  { l1==l2 } // Arguments must be linear in method equals: Variable l1 is repeated (line 69)
  get_head(l)    { ((ATermList)l).getFirst() }
  get_tail(l)    { ((ATermList)l).getNext() }
  is_empty(l)    { ((ATermList)l).isEmpty() }
}

%typelist type9 {
  implement { ATermList }
  get_fun_sym(t) {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  get_head(l)    { ((ATermList)l).getFirst() }
  equals(l1,l2)  { l1==l2 }
  get_head(l)    { ((ATermList)l).getFirst() } // Repeated macro-function get_head (line 81)
  get_tail(l)    { ((ATermList)l).getNext() }
  is_empty(l)    { ((ATermList)l).isEmpty() }
}

%typelist type10 {
  implement { ATermList }
  get_fun_sym(t) {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  get_head(l)    { ((ATermList)l).getFirst() }
  get_tail(l)    { ((ATermList)l).getNext() }
  is_empty(l)    { ((ATermList)l).isEmpty() }
}

%typelist type10 {  // Multiple definition of Symbol type10 (line 95)
  implement { ATermList }
  get_fun_sym(t) {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  get_head(l)    { ((ATermList)l).getFirst() }
  get_tail(l)    { ((ATermList)l).getNext() }
  is_empty(l)    { ((ATermList)l).isEmpty() }
}
