








package main



import (
  "fmt"
  "os"
)



const (
  ZERO = 0
  SUC = 1
)

func tom_loulou_is_sort_Nat(t any) bool { return (0==0);  }; func tom_loulou_is_fun_sym_zero(t  int ) bool { return t==0;  }; func tom_loulou_is_fun_sym_suc(t  int ) bool { return t>0;  }; func tom_loulou_make_suc(t  int )  int  { return (t+1) }; func tom_loulou_get_slot_suc_p(t  int )  int  { return t-1;  }; 
















func plus(t1 int, t2 int) int {
  ; { _ = t1; _ = t2; { if tom_loulou_is_sort_Nat(t1) { if tom_loulou_is_sort_Nat(t2) { if tom_loulou_is_fun_sym_zero(any(t2).( int )) { 
 return any(t1).( int );  };  };  };  }; { if tom_loulou_is_sort_Nat(t1) { if tom_loulou_is_sort_Nat(t2) { if tom_loulou_is_fun_sym_suc(any(t2).( int )) { 
 return tom_loulou_make_suc(plus(any(t1).( int ),tom_loulou_get_slot_suc_p(any(t2).( int ))));  };  };  };  };  }; 

  panic("no rule applies")
}



func test_failed(category string) {
  fmt.Fprintf(os.Stderr, "test '%s' failed!\n", category)
  os.Exit(1)
}
func test_assert(cat string, cond bool) { if !(cond) { test_failed(cat) } }



func main() {

    
  
  test_assert("1+1 = 2", (plus(1,1) == 2))
  test_assert("10+10 = 20", (plus(10,10) == 20))
}
