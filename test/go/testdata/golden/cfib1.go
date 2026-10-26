











package main



import (
  "fmt"
  "os"
)



const (
  ZERO = 0
  SUC = 1
  PLUS = 2
  FIB = 3
)



type term struct {
  fs int
  arity int
  subt []*term
}



var s_zero term = term{ZERO, 0, nil}
var zero *term = &s_zero



func suc(x *term) *term {
  var res *term
  res = &term{}
  res.fs = SUC
  res.arity = 1
  res.subt = make([]*term, 1)
  res.subt[0] = x
  return(res)
}

func tom_cfib1_is_sort_term(t any) bool { return (0==0);  }; func tom_cfib1_is_fun_sym_zero(t  *term ) bool { return t.fs == ZERO;  }; func tom_cfib1_is_fun_sym_suc(t  *term ) bool { return t.fs == SUC;  }; func tom_cfib1_get_slot_suc_s1(t  *term )  *term  { return t.subt[0];  }; 


























func plus(t1 *term, t2 *term) *term {
  ; { { if tom_cfib1_is_sort_term(t1) { if tom_cfib1_is_sort_term(t2) { if tom_cfib1_is_fun_sym_zero(any(t2).( *term )) { 
 return (any(t1).( *term ));  };  };  };  }; { if tom_cfib1_is_sort_term(t1) { if tom_cfib1_is_sort_term(t2) { if tom_cfib1_is_fun_sym_suc(any(t2).( *term )) { 
 return (suc(plus(any(t1).( *term ),tom_cfib1_get_slot_suc_s1(any(t2).( *term ))))) ;  };  };  };  };  }; 

  panic("no rule applies")
}



func fib(t *term) *term {
  ; { { if tom_cfib1_is_sort_term(t) { if tom_cfib1_is_fun_sym_zero(any(t).( *term )) { 
 return(suc(zero)) ;  };  };  }; { if tom_cfib1_is_sort_term(t) { if tom_cfib1_is_fun_sym_suc(any(t).( *term )) { if tom_cfib1_is_fun_sym_zero(any(tom_cfib1_get_slot_suc_s1(any(t).( *term ))).( *term )) { 
 return(suc(zero)) ;  };  };  };  }; { if tom_cfib1_is_sort_term(t) { if tom_cfib1_is_fun_sym_suc(any(t).( *term )) { var tomMatch2_10  *term  = tom_cfib1_get_slot_suc_s1(any(t).( *term )); if tom_cfib1_is_fun_sym_suc(any(tomMatch2_10).( *term )) { var tom___x  *term  = tom_cfib1_get_slot_suc_s1(tomMatch2_10); 
 return(plus(fib(tom___x),fib(suc(tom___x)))) ;  };  };  };  };  }; 

  panic("no rule applies")
}



func count(t *term) int {
  ; { { if tom_cfib1_is_sort_term(t) { if tom_cfib1_is_fun_sym_zero(any(t).( *term )) { 
 return 0 ;  };  };  }; { if tom_cfib1_is_sort_term(t) { if tom_cfib1_is_fun_sym_suc(any(t).( *term )) { 
 return 1 + count(tom_cfib1_get_slot_suc_s1(any(t).( *term ))) ;  };  };  };  }; 

  panic("no rule applies")
}



func printTerm(tt *term) {
  var i int
  fsnames := []string{"zero", "suc", "plus", "fib"}
  fmt.Printf("%s", fsnames[tt.fs])
  if tt.arity!=0 {
    fmt.Printf("(")
    for i=0; i<tt.arity; i++ {
      printTerm(tt.subt[i])
      if i+1 != tt.arity { fmt.Printf(",") }
    }
    fmt.Printf(")")
  }
}



func buildPeano(n int) *term {
  var i int
  var t *term
  t = zero
  for i=0; i<n; i++ {
    t = suc(t)
  }
  return t
}



func symbolicFib( n int ) {
  fmt.Printf("fib %d == ", n)
  printTerm(fib(buildPeano(n)))
  fmt.Printf("\n")
  os.Stdout.Sync()
}



func test_failed(category string) {
  fmt.Fprintf(os.Stderr, "test %s failed!\n", category)
  os.Exit(1)
}
func test_assert(cat string, cond bool) { if !(cond) { test_failed(cat) } }





func main() {
    
  test_assert("fib(10)", (count(fib(buildPeano(10))) == 89) )
}
