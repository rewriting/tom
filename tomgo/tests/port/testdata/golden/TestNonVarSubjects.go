package testnonvarsubjects



import "testing"

func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_is_fun_sym_list(t  Term ) bool { return func() bool { _, ok := (t).(*ListTerm); return ok }();  }; func tom_empty_list_list()  Term  { return (MakeList()) }; func tom_cons_list_list(e  Term , l  Term )  Term  { return (func() Term { s := (l).(*ListTerm).Slots; return MakeList(append([]Term{e}, s...)...) }()) }; func tom_get_head_list_Term(l  Term )  Term  { return (l).(*ListTerm).Slots[0];  }; func tom_get_tail_list_Term(l  Term )  Term  { return func() Term { s := (l).(*ListTerm).Slots; return MakeList(s[1:]...) }();  }; func tom_is_empty_list_Term(l  Term ) bool { return len((l).(*ListTerm).Slots) == 0;  }; func tom_append_list_list(l1  Term , l2  Term )  Term  { if tom_is_empty_list_Term(l1) { return l2 } else if tom_is_empty_list_Term(l2) { return l1 } else if tom_is_fun_sym_list(l1) { if tom_is_empty_list_Term(tom_get_tail_list_Term(l1)) { return tom_cons_list_list(tom_get_head_list_Term(l1), l2) } else { return tom_cons_list_list(tom_get_head_list_Term(l1), tom_append_list_list(tom_get_tail_list_Term(l1), l2)) } } else { return tom_cons_list_list(l1, l2) }; }; func tom_get_slice_list(begin  Term , end  Term , tail  Term )  Term  { if tom_equal_term_Term(begin, end) { return tail } else if tom_equal_term_Term(end, tail) && (tom_is_empty_list_Term(end) || tom_equal_term_Term(end, tom_empty_list_list())) { return begin }; return tom_cons_list_list(func()  Term  { if tom_is_fun_sym_list(begin) { return tom_get_head_list_Term(begin) }; return begin }(), tom_get_slice_list(func()  Term  { if tom_is_fun_sym_list(begin) { return tom_get_tail_list_Term(begin) }; return tom_empty_list_list() }(), end, tail)) }; 



















func TestNonVarSubject(t *testing.T) {
	matched := false
	
{ { if tom_is_sort_Term(tom_cons_list_list(tom_make_a(),tom_empty_list_list())) { 
 matched = true  };  };  }; 

	if !matched {
		t.Errorf("%%match(list(a())) { _x -> ... } should fire")
	}
}
