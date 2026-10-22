package main



import "fmt"












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

func tom_equal_term_term(t1  *Term , t2  *Term ) bool { return (t1).Equals(t2);  }; func tom_is_sort_term(t any) bool { return t != nil;  }; func tom_is_fun_sym_zero(t  *Term ) bool { return (t).Name == "zero";  }; func tom_make_zero()  *Term  { return (&Term{Name: "zero"}) }; func tom_is_fun_sym_suc(t  *Term ) bool { return (t).Name == "suc";  }; func tom_make_suc(p  *Term )  *Term  { return (&Term{Name: "suc", Args: []*Term{p}}) }; func tom_get_slot_suc_pred(t  *Term )  *Term  { return (t).Args[0];  }; 


























func plus1(t1, t2 *Term) *Term {
	
{ { if tom_is_sort_term(t1) { if tom_is_fun_sym_zero(any(t1).( *Term )) { if tom_is_sort_term(t2) { 
 return any(t2).( *Term ) };  };  };  }; { if tom_is_sort_term(t1) { if tom_is_fun_sym_suc(any(t1).( *Term )) { if tom_is_sort_term(t2) { 
 return tom_make_suc(plus1(tom_get_slot_suc_pred(any(t1).( *Term )),any(t2).( *Term ))) };  };  };  };  }; 

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
	n := 0
	for t != nil && t.Name == "suc" {
		n++
		t = t.Args[0]
	}
	return n
}



func main() {
	ok := true
	for i := 0; i < 20; i++ {
		n := int2peano(i)
		got := peano2int(plus1(n, n))
		if got != i+i {
			ok = false
			fmt.Printf("FAIL: plus1(%d,%d) = %d, want %d\n", i, i, got, i+i)
		}
	}
	if ok {
		fmt.Println("First loop has successfully terminated.")
	}
	fmt.Println("Peano test has successfully terminated.")
}
