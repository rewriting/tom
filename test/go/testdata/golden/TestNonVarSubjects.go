





























package testnonvarsubjects



import "testing"

func tom_TestNonVarSubjects_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestNonVarSubjects_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestNonVarSubjects_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_TestNonVarSubjects_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_TestNonVarSubjects_make_a()  Term  { return (MakeA()) }; func tom_TestNonVarSubjects_is_fun_sym_list(t  Term ) bool { return func() bool { _, ok := (t).(*ListTerm); return ok }();  }; func tom_TestNonVarSubjects_empty_list_list()  Term  { return (EmptyList()) }; func tom_TestNonVarSubjects_cons_list_list(e  Term , l  Term )  Term  { return (ConsList(e,l)) }; func tom_TestNonVarSubjects_get_head_list_Term(l  Term )  Term  { return (l).GetHeadlist();  }; func tom_TestNonVarSubjects_get_tail_list_Term(l  Term )  Term  { return (l).GetTaillist();  }; func tom_TestNonVarSubjects_is_empty_list_Term(l  Term ) bool { return (l).IsEmptylist();  }; func tom_TestNonVarSubjects_append_list_list(l1  Term , l2  Term )  Term  { if tom_TestNonVarSubjects_is_empty_list_Term(l1) { return l2 } else if tom_TestNonVarSubjects_is_empty_list_Term(l2) { return l1 } else if tom_TestNonVarSubjects_is_fun_sym_list(l1) { if tom_TestNonVarSubjects_is_empty_list_Term(tom_TestNonVarSubjects_get_tail_list_Term(l1)) { return tom_TestNonVarSubjects_cons_list_list(tom_TestNonVarSubjects_get_head_list_Term(l1), l2) } else { return tom_TestNonVarSubjects_cons_list_list(tom_TestNonVarSubjects_get_head_list_Term(l1), tom_TestNonVarSubjects_append_list_list(tom_TestNonVarSubjects_get_tail_list_Term(l1), l2)) } } else { return tom_TestNonVarSubjects_cons_list_list(l1, l2) }; }; func tom_TestNonVarSubjects_get_slice_list(begin  Term , end  Term , tail  Term )  Term  { if tom_TestNonVarSubjects_equal_term_Term(begin, end) { return tail } else if tom_TestNonVarSubjects_equal_term_Term(end, tail) && (tom_TestNonVarSubjects_is_empty_list_Term(end) || tom_TestNonVarSubjects_equal_term_Term(end, tom_TestNonVarSubjects_empty_list_list())) { return begin }; return tom_TestNonVarSubjects_cons_list_list(func()  Term  { if tom_TestNonVarSubjects_is_fun_sym_list(begin) { return tom_TestNonVarSubjects_get_head_list_Term(begin) }; return begin }(), tom_TestNonVarSubjects_get_slice_list(func()  Term  { if tom_TestNonVarSubjects_is_fun_sym_list(begin) { return tom_TestNonVarSubjects_get_tail_list_Term(begin) }; return tom_TestNonVarSubjects_empty_list_list() }(), end, tail)) }; 















func Test1(t *testing.T) {    
  ; { { if tom_TestNonVarSubjects_is_sort_Term(tom_TestNonVarSubjects_cons_list_list(tom_TestNonVarSubjects_make_a(),tom_TestNonVarSubjects_empty_list_list())) { 
 return ;  };  };  }; 

  t.FailNow()
}  
