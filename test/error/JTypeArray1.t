%typearray type1 { // Missing macro-function(s) [ get_fun_sym ] (line1)
  implement { ArrayList }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l2)    { l1.equals(l2) }
  get_element(l,n) { ((ArrayList)l).get(n) }
  get_size(l)      { ((ArrayList)l).size() }
}

%typearray type2 { // Missing macro-function(s) [ cmp_fun_sym ] (line 9)
  implement { ArrayList }
  get_fun_sym(t)   {  }
  equals(l1,l2)    { l1.equals(l2) }
  get_element(l,n) { ((ArrayList)l).get(n) }
  get_size(l)      { ((ArrayList)l).size() }
}

%typearray type3 { // Missing macro-function(s) [ get_element ] (line17)
  implement { ArrayList }
  get_fun_sym(t)   {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l2)    { l1.equals(l2) }
  get_size(l)      { ((ArrayList)l).size() }
}

%typearray type4 { // Missing macro-function(s) [ get_size ] (line 25)
  implement { ArrayList }
  get_fun_sym(t)   {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l2)    { l1.equals(l2) }
  get_element(l,n) { ((ArrayList)l).get(n) }
}

%typearray type5 { // No error; Missing optional make macro
  implement { ArrayList }
  get_fun_sym(t)   {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  get_element(l,n) { ((ArrayList)l).get(n) }
  get_size(l)      { ((ArrayList)l).size() }
}

%typearray type6 { 
  implement { ArrayList }
  get_fun_sym(t)   {  }
  cmp_fun_sym(t1,t1) { t1 == t2 } // Arguments must be linear in method cmp_fun_sym: Variable t1 is repeated
  equals(l1,l2)    { l1.equals(l2) }
  get_element(l,n) { ((ArrayList)l).get(n) }
  get_size(l)      { ((ArrayList)l).size() }
}

%typearray type7 {
  implement { ArrayList }
  get_fun_sym(t)   {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l1)    { l1.equals(l2) } // Arguments must be linear in method equals: Variable l1 is repeated (line 54)
  get_element(l,n) { ((ArrayList)l).get(n) }
  get_size(l)      { ((ArrayList)l).size() }
}

%typearray type8 {
  implement { ArrayList }
  get_fun_sym(t)   {  }
  cmp_fun_sym(t1,t2) { t1 == t2 }
  equals(l1,l2)    { l1.equals(l2) }
  get_element(l,n) { ((ArrayList)l).get(n) }
  get_size(l)      { ((ArrayList)l).size() }
  get_element(l,n) { ((ArrayList)l).get(n) } // Repeated macro-function get_element (line 66)
}
