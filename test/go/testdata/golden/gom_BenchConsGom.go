package main
















import (
  "fmt"
  "os"
  "strconv"
  "time"

  . "fixture/list"
)

  func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_List(t1  List , t2  List ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_List(t any) bool { return func() bool { _, ok := (t).(List); return ok }();  }; func tom_is_fun_sym_conc(t  List ) bool { return func() bool { _, ok := (t).(*ConcList); return ok }();  }; func tom_empty_list_conc()  List  { return (EmptyConc()) }; func tom_cons_list_conc(e  int64 , l  List )  List  { return (ConsConc(e,l)) }; func tom_get_head_conc_List(l  List )  int64  { return (l).GetHeadconc();  }; func tom_get_tail_conc_List(l  List )  List  { return (l).GetTailconc();  }; func tom_is_empty_conc_List(l  List ) bool { return (l).IsEmptyconc();  }; func tom_append_list_conc(l1  List , l2  List )  List  { if tom_is_empty_conc_List(l1) { return l2 } else if tom_is_empty_conc_List(l2) { return l1 } else if tom_is_empty_conc_List(tom_get_tail_conc_List(l1)) { return tom_cons_list_conc(tom_get_head_conc_List(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_List(l1), tom_append_list_conc(tom_get_tail_conc_List(l1), l2)) }; }; func tom_get_slice_conc(begin  List , end  List , tail  List )  List  { if tom_equal_term_List(begin, end) { return tail } else if tom_equal_term_List(end, tail) && (tom_is_empty_conc_List(end) || tom_equal_term_List(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(tom_get_head_conc_List(begin), tom_get_slice_conc(tom_get_tail_conc_List(begin), end, tail)) }; 

  func genere(n int64) List {
    if(n>2) {
      var l List = genere(n-1)
      return tom_cons_list_conc(n,tom_append_list_conc(l,tom_empty_list_conc()))
    } else {
      return tom_cons_list_conc(2,tom_empty_list_conc())
    }
  }

  func elim(l List) List {
    ; { _ = l; { if tom_is_sort_List(l) { if tom_is_fun_sym_conc(any(l).( List )) { var tomMatch1_end_4  List  = any(l).( List ); _ = tomMatch1_end_4; for { { if !(tom_is_empty_conc_List(tomMatch1_end_4)) { var tom___e1  int64  = tom_get_head_conc_List(tomMatch1_end_4); _ = tom___e1; var tomMatch1_5  List  = tom_get_tail_conc_List(tomMatch1_end_4); _ = tomMatch1_5; var tomMatch1_end_8  List  = tomMatch1_5; _ = tomMatch1_end_8; for { { if !(tom_is_empty_conc_List(tomMatch1_end_8)) { 

        if(tom_get_head_conc_List(tomMatch1_end_8)%tom___e1 == 0) {
          return elim(tom_append_list_conc(tom_get_slice_conc(any(l).( List ),tomMatch1_end_4,tom_empty_list_conc()),tom_cons_list_conc(tom___e1,tom_append_list_conc(tom_get_slice_conc(tomMatch1_5,tomMatch1_end_8,tom_empty_list_conc()),tom_append_list_conc(tom_get_tail_conc_List(tomMatch1_end_8),tom_empty_list_conc())))))};  }; if tom_is_empty_conc_List(tomMatch1_end_8) { tomMatch1_end_8 = tomMatch1_5;  } else { tomMatch1_end_8 = tom_get_tail_conc_List(tomMatch1_end_8);  };  };  if !(!(tom_equal_term_List(tomMatch1_end_8, tomMatch1_5))) { break; }; };  }; if tom_is_empty_conc_List(tomMatch1_end_4) { tomMatch1_end_4 = any(l).( List );  } else { tomMatch1_end_4 = tom_get_tail_conc_List(tomMatch1_end_4);  };  };  if !(!(tom_equal_term_List(tomMatch1_end_4, any(l).( List )))) { break; }; };  };  };  };  }; 



    return l
  }

  func reverse(l List) List {
    var res List = tom_empty_list_conc()
    for {
      ; { _ = l; { if tom_is_sort_List(l) { if tom_is_fun_sym_conc(any(l).( List )) { if tom_is_empty_conc_List(any(l).( List )) { 
 return res ;  };  };  };  }; { if tom_is_sort_List(l) { if tom_is_fun_sym_conc(any(l).( List )) { if !(tom_is_empty_conc_List(any(l).( List ))) { 

          res = tom_cons_list_conc(tom_get_head_conc_List(any(l).( List )),tom_append_list_conc(res,tom_empty_list_conc()))
          l = tom_get_tail_conc_List(any(l).( List ));  };  };  };  };  }; 


    }
  }

  func run(max int64) {
    

    var startChrono int64 = time.Now().UnixMilli()
    
    elim(reverse(genere(max)))
    
    fmt.Println(fmt.Sprint(max) + " " + fmt.Sprint(time.Now().UnixMilli()-startChrono))
    fmt.Println(Factory().Stats())
  }

  func main() {
    var max int64 = 100
    
    
    if len(os.Args) < 2 {
      fmt.Println("Usage: java list.BenchConsGom <max>")
      return
    }
    m, err := strconv.ParseInt(os.Args[1], 10, 32)
    if err != nil {
      fmt.Println("Usage: java list.BenchConsGom <max>")
      return
    }
    max = m
    run(max)
  }
