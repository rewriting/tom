package list_test



import (
	"testing"
)




type Term struct {
	Name string
	Args []*Term
}



func (t *Term) Equals(o *Term) bool {
	if t == o {
		return true
	}
	if t == nil || o == nil || t.Name != o.Name || len(t.Args) != len(o.Args) {
		return false
	}
	for i := range t.Args {
		if !t.Args[i].Equals(o.Args[i]) {
			return false
		}
	}
	return true
}



func equalsList(a, b []*Term) bool {
	if len(a) != len(b) {
		return false
	}
	for i := range a {
		if !a[i].Equals(b[i]) {
			return false
		}
	}
	return true
}








func listInsert(e *Term, l []*Term) []*Term {
	out := make([]*Term, 0, len(l)+1)
	out = append(out, e)
	out = append(out, l...)
	return out
}

func tom_equal_term_L(l1  []*Term , l2  []*Term ) bool { return equalsList(l1, l2);  }; func tom_is_sort_L(t any) bool { return t != nil;  }; func tom_is_fun_sym_conc(t  []*Term ) bool { return t != nil;  }; func tom_empty_list_conc()  []*Term  { return ([]*Term{}) }; func tom_cons_list_conc(e  *Term , l  []*Term )  []*Term  { return (listInsert(e, l)) }; func tom_get_head_conc_L(l  []*Term )  *Term  { return (l)[0];  }; func tom_get_tail_conc_L(l  []*Term )  []*Term  { return (l)[1:];  }; func tom_is_empty_conc_L(l  []*Term ) bool { return len(l) == 0;  }; func tom_append_list_conc(l1  []*Term , l2  []*Term )  []*Term  { if tom_is_empty_conc_L(l1) { return l2 } else if tom_is_empty_conc_L(l2) { return l1 } else if tom_is_empty_conc_L(tom_get_tail_conc_L(l1)) { return tom_cons_list_conc(tom_get_head_conc_L(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_L(l1), tom_append_list_conc(tom_get_tail_conc_L(l1), l2)) }; }; func tom_get_slice_conc(begin  []*Term , end  []*Term , tail  []*Term )  []*Term  { if tom_equal_term_L(begin, end) { return tail } else if tom_equal_term_L(end, tail) && (tom_is_empty_conc_L(end) || tom_equal_term_L(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(tom_get_head_conc_L(begin), tom_get_slice_conc(tom_get_tail_conc_L(begin), end, tail)) }; func tom_equal_term_E(t1  *Term , t2  *Term ) bool { return (t1).Equals(t2);  }; func tom_is_sort_E(t any) bool { return t != nil;  }; func tom_is_fun_sym_a(t  *Term ) bool { return (t).Name == "a";  }; func tom_make_a()  *Term  { return (&Term{Name: "a"}) }; func tom_is_fun_sym_b(t  *Term ) bool { return (t).Name == "b";  }; func tom_make_b()  *Term  { return (&Term{Name: "b"}) }; func tom_make_c()  *Term  { return (&Term{Name: "c"}) }; func tom_is_fun_sym_f(t  *Term ) bool { return (t).Name == "f";  }; func tom_make_f(s  *Term )  *Term  { return (&Term{Name: "f", Args: []*Term{s}}) }; func tom_get_slot_f_s1(t  *Term )  *Term  { return (t).Args[0];  }; 


















































func classifyTag(l []*Term) string {
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { if tom_is_empty_conc_L(any(l).( []*Term )) { 
 return "empty"  };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { if !(tom_is_empty_conc_L(any(l).( []*Term ))) { var tomMatch1_6  *Term  = tom_get_head_conc_L(any(l).( []*Term )); _ = tomMatch1_6; if tom_is_fun_sym_f(any(tomMatch1_6).( *Term )) { if tom_is_fun_sym_a(any(tom_get_slot_f_s1(tomMatch1_6)).( *Term )) { if tom_is_empty_conc_L(tom_get_tail_conc_L(any(l).( []*Term ))) { 
 return "singleton_fa"  };  };  };  };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch1_end_14  []*Term  = any(l).( []*Term ); _ = tomMatch1_end_14; for { { if !(tom_is_empty_conc_L(tomMatch1_end_14)) { var tomMatch1_17  *Term  = tom_get_head_conc_L(tomMatch1_end_14); _ = tomMatch1_17; if tom_is_fun_sym_f(any(tomMatch1_17).( *Term )) { if tom_is_fun_sym_a(any(tom_get_slot_f_s1(tomMatch1_17)).( *Term )) { if tom_is_empty_conc_L(tom_get_tail_conc_L(tomMatch1_end_14)) { 
 return "ends_fa"  };  };  };  }; if tom_is_empty_conc_L(tomMatch1_end_14) { tomMatch1_end_14 = any(l).( []*Term );  } else { tomMatch1_end_14 = tom_get_tail_conc_L(tomMatch1_end_14);  };  };  if !(!(tom_equal_term_L(tomMatch1_end_14, any(l).( []*Term )))) { break; }; };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch1_end_25  []*Term  = any(l).( []*Term ); _ = tomMatch1_end_25; for { { if !(tom_is_empty_conc_L(tomMatch1_end_25)) { var tomMatch1_32  *Term  = tom_get_head_conc_L(tomMatch1_end_25); _ = tomMatch1_32; if tom_is_fun_sym_f(any(tomMatch1_32).( *Term )) { if tom_is_fun_sym_a(any(tom_get_slot_f_s1(tomMatch1_32)).( *Term )) { var tomMatch1_26  []*Term  = tom_get_tail_conc_L(tomMatch1_end_25); _ = tomMatch1_26; var tomMatch1_end_29  []*Term  = tomMatch1_26; _ = tomMatch1_end_29; for { { if !(tom_is_empty_conc_L(tomMatch1_end_29)) { var tomMatch1_37  *Term  = tom_get_head_conc_L(tomMatch1_end_29); _ = tomMatch1_37; if tom_is_fun_sym_f(any(tomMatch1_37).( *Term )) { if tom_is_fun_sym_b(any(tom_get_slot_f_s1(tomMatch1_37)).( *Term )) { if tom_is_empty_conc_L(tom_get_tail_conc_L(tomMatch1_end_29)) { 
 return "fa_then_fb"  };  };  };  }; if tom_is_empty_conc_L(tomMatch1_end_29) { tomMatch1_end_29 = tomMatch1_26;  } else { tomMatch1_end_29 = tom_get_tail_conc_L(tomMatch1_end_29);  };  };  if !(!(tom_equal_term_L(tomMatch1_end_29, tomMatch1_26))) { break; }; };  };  };  }; if tom_is_empty_conc_L(tomMatch1_end_25) { tomMatch1_end_25 = any(l).( []*Term );  } else { tomMatch1_end_25 = tom_get_tail_conc_L(tomMatch1_end_25);  };  };  if !(!(tom_equal_term_L(tomMatch1_end_25, any(l).( []*Term )))) { break; }; };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { if !(tom_is_empty_conc_L(any(l).( []*Term ))) { var tomMatch1_50  *Term  = tom_get_head_conc_L(any(l).( []*Term )); _ = tomMatch1_50; if tom_is_fun_sym_f(any(tomMatch1_50).( *Term )) { if tom_is_fun_sym_b(any(tom_get_slot_f_s1(tomMatch1_50)).( *Term )) { var tomMatch1_43  []*Term  = tom_get_tail_conc_L(any(l).( []*Term )); _ = tomMatch1_43; var tomMatch1_end_46  []*Term  = tomMatch1_43; _ = tomMatch1_end_46; for { { if !(tom_is_empty_conc_L(tomMatch1_end_46)) { var tomMatch1_55  *Term  = tom_get_head_conc_L(tomMatch1_end_46); _ = tomMatch1_55; if tom_is_fun_sym_f(any(tomMatch1_55).( *Term )) { if tom_is_fun_sym_b(any(tom_get_slot_f_s1(tomMatch1_55)).( *Term )) { 
 return "starts_fb_then_fb"  };  };  }; if tom_is_empty_conc_L(tomMatch1_end_46) { tomMatch1_end_46 = tomMatch1_43;  } else { tomMatch1_end_46 = tom_get_tail_conc_L(tomMatch1_end_46);  };  };  if !(!(tom_equal_term_L(tomMatch1_end_46, tomMatch1_43))) { break; }; };  };  };  };  };  };  };  }; 

	return "other"
}






func firstHead(l []*Term) *Term {
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { if !(tom_is_empty_conc_L(any(l).( []*Term ))) { 
 return tom_get_head_conc_L(any(l).( []*Term )) };  };  };  };  }; 

	return nil
}




func countElements(l []*Term) int {
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { if tom_is_empty_conc_L(any(l).( []*Term )) { 
 return 0  };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { if !(tom_is_empty_conc_L(any(l).( []*Term ))) { 
 return 1 + countElements(tom_get_tail_conc_L(any(l).( []*Term )))  };  };  };  };  }; 

	return 0
}




func ta() *Term { return tom_make_a() }
func tb() *Term { return tom_make_b() }
func tc() *Term { return tom_make_c() }
func fa() *Term { return tom_make_f(tom_make_a()) }
func fb() *Term { return tom_make_f(tom_make_b()) }



func TestEmpty(t *testing.T) {
	if got := classifyTag([]*Term{}); got != "empty" {
		t.Errorf("classifyTag([]) = %q, want empty", got)
	}
}



func TestSingletonFA(t *testing.T) {
	if got := classifyTag([]*Term{fa()}); got != "singleton_fa" {
		t.Errorf("classifyTag([f(a)]) = %q, want singleton_fa", got)
	}
}



func TestEndsFA(t *testing.T) {
	if got := classifyTag([]*Term{ta(), tb(), fa()}); got != "ends_fa" {
		t.Errorf("classifyTag([a,b,f(a)]) = %q, want ends_fa", got)
	}
}



func TestFAthenFB(t *testing.T) {
	if got := classifyTag([]*Term{ta(), fa(), tb(), fb()}); got != "fa_then_fb" {
		t.Errorf("classifyTag([a,f(a),b,f(b)]) = %q, want fa_then_fb", got)
	}
}



func TestStartsFBthenFB(t *testing.T) {
	if got := classifyTag([]*Term{fb(), fb()}); got != "starts_fb_then_fb" {
		t.Errorf("classifyTag([f(b),f(b)]) = %q, want starts_fb_then_fb", got)
	}
}



func TestFirstHead(t *testing.T) {
	if got := firstHead([]*Term{ta(), tb(), tc()}); !got.Equals(ta()) {
		t.Errorf("firstHead([a,b,c]) = %v, want a", got)
	}
	if got := firstHead([]*Term{}); got != nil {
		t.Errorf("firstHead([]) = %v, want nil", got)
	}
}



func TestCount(t *testing.T) {
	if got := countElements([]*Term{}); got != 0 {
		t.Errorf("countElements([]) = %d, want 0", got)
	}
	if got := countElements([]*Term{ta(), tb(), tc()}); got != 3 {
		t.Errorf("countElements([a,b,c]) = %d, want 3", got)
	}
}
