package peano



import (
	"fmt"
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



func (t *Term) String() string {
	if t == nil {
		return "<nil>"
	}
	if len(t.Args) == 0 {
		return t.Name
	}
	s := t.Name + "("
	for i, a := range t.Args {
		if i > 0 {
			s += ","
		}
		s += a.String()
	}
	return s + ")"
}

func tom_equal_term_term(t1  *Term , t2  *Term ) bool { return (t1).Equals(t2);  }; func tom_is_sort_term(t any) bool { return t != nil;  }; func tom_is_fun_sym_zero(t  *Term ) bool { return (t).Name == "zero";  }; func tom_make_zero()  *Term  { return (&Term{Name: "zero"}) }; func tom_is_fun_sym_suc(t  *Term ) bool { return (t).Name == "suc";  }; func tom_make_suc(p  *Term )  *Term  { return (&Term{Name: "suc", Args: []*Term{p}}) }; func tom_get_slot_suc_pred(t  *Term )  *Term  { return (t).Args[0];  }; 



















func plus1(t1, t2 *Term) *Term {
	{ { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { if tom_is_fun_sym_zero(any(t2).( *Term )) { 
 return any(t1).( *Term ) };  };  };  }; { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { if tom_is_fun_sym_suc(any(t2).( *Term )) { 
 return tom_make_suc(plus1(any(t1).( *Term ),tom_get_slot_suc_pred(any(t2).( *Term )))) };  };  };  };  }; 

	return nil
}




func plus2(t1, t2 *Term) *Term {
	{ { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { if tom_is_fun_sym_zero(any(t2).( *Term )) { 
 return any(t1).( *Term ) };  };  };  }; { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { if tom_is_fun_sym_suc(any(t2).( *Term )) { 
 return tom_make_suc(plus2(any(t1).( *Term ),tom_get_slot_suc_pred(any(t2).( *Term )))) };  };  };  };  }; 

	return nil
}




func fib3(t *Term) *Term {
	{ { if tom_is_sort_term(t) { if tom_is_fun_sym_zero(any(t).( *Term )) { 
 return tom_make_suc(tom_make_zero()) };  };  }; { if tom_is_sort_term(t) { if tom_is_fun_sym_suc(any(t).( *Term )) { var tomMatch3_4  *Term  = tom_get_slot_suc_pred(any(t).( *Term )); _ = tomMatch3_4; if tom_is_fun_sym_zero(any(tomMatch3_4).( *Term )) { 
 return tom_make_suc(tomMatch3_4) };  };  };  }; { if tom_is_sort_term(t) { if tom_is_fun_sym_suc(any(t).( *Term )) { var tomMatch3_10  *Term  = tom_get_slot_suc_pred(any(t).( *Term )); _ = tomMatch3_10; if tom_is_fun_sym_suc(any(tomMatch3_10).( *Term )) { var tom___x  *Term  = tom_get_slot_suc_pred(tomMatch3_10); _ = tom___x; 
 return plus1(fib3(tom___x), fib3(tom_make_suc(tom___x)))  };  };  };  };  }; 

	return nil
}




func int2peano(n int) *Term {
	t := tom_make_zero()
	for i := 0; i < n; i++ {
		t = tom_make_suc(t)
	}
	return t
}




func peano2int(t *Term) int {
	{ { if tom_is_sort_term(t) { if tom_is_fun_sym_zero(any(t).( *Term )) { 
 return 0  };  };  }; { if tom_is_sort_term(t) { if tom_is_fun_sym_suc(any(t).( *Term )) { 
 return 1 + peano2int(tom_get_slot_suc_pred(any(t).( *Term )))  };  };  };  }; 

	return 0
}




func fibint(n int) int {
	if n <= 1 {
		return 1
	}
	return fibint(n-1) + fibint(n-2)
}



func TestPlus1(t *testing.T) {
	for i := 0; i < 100; i++ {
		N := int2peano(i)
		if got := peano2int(plus1(N, N)); got != i+i {
			t.Errorf("plus1(%d, %d) = %d, want %d  (N=%s)", i, i, got, i+i, N)
		}
	}
}



func TestPlus2(t *testing.T) {
	for i := 0; i < 100; i++ {
		N := int2peano(i)
		if got := peano2int(plus2(N, N)); got != i+i {
			t.Errorf("plus2(%d, %d) = %d, want %d  (N=%s)", i, i, got, i+i, N)
		}
	}
}



func TestFib3(t *testing.T) {
	for i := 0; i < 15; i++ {
		N := int2peano(i)
		if got := peano2int(fib3(N)); got != fibint(i) {
			t.Errorf("fib3(%d) = %d, want %d  (N=%s)", i, got, fibint(i), N)
		}
	}
}



func TestInt2p(t *testing.T) {
	got := int2peano(5)
	want := tom_make_suc(tom_make_suc(tom_make_suc(tom_make_suc(tom_make_suc(tom_make_zero())))))
	if !got.Equals(want) {
		t.Errorf("int2peano(5) = %s, want %s", got, want)
	}
}



func TestPeano2int(t *testing.T) {
	in := tom_make_suc(tom_make_suc(tom_make_suc(tom_make_suc(tom_make_suc(tom_make_zero())))))
	if got := peano2int(in); got != 5 {
		t.Errorf("peano2int(%s) = %d, want 5", in, got)
	}
}






var _ = fmt.Sprintf
