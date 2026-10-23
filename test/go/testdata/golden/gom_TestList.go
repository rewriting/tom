package gom
















import (
  "testing"

  "tom/library/aterm"
)

  func tom_equal_term_Element(t1  Element , t2  Element ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Element(t any) bool { return func() bool { _, ok := (t).(Element); return ok }();  }; func tom_equal_term_List(t1  List , t2  List ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_List(t any) bool { return func() bool { _, ok := (t).(List); return ok }();  }; func tom_make_a()  Element  { return (MakeA()) }; func tom_make_b()  Element  { return (MakeB()) }; func tom_is_fun_sym_conc(t  List ) bool { return func() bool { _, ok := (t).(*ConcList); return ok }();  }; func tom_empty_list_conc()  List  { return (EmptyConc()) }; func tom_cons_list_conc(e  Element , l  List )  List  { return (ConsConc(e,l)) }; func tom_get_head_conc_List(l  List )  Element  { return (l).GetHeadconc();  }; func tom_get_tail_conc_List(l  List )  List  { return (l).GetTailconc();  }; func tom_is_empty_conc_List(l  List ) bool { return (l).IsEmptyconc();  }; func tom_append_list_conc(l1  List , l2  List )  List  { if tom_is_empty_conc_List(l1) { return l2 } else if tom_is_empty_conc_List(l2) { return l1 } else if tom_is_empty_conc_List(tom_get_tail_conc_List(l1)) { return tom_cons_list_conc(tom_get_head_conc_List(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_List(l1), tom_append_list_conc(tom_get_tail_conc_List(l1), l2)) }; }; func tom_get_slice_conc(begin  List , end  List , tail  List )  List  { if tom_equal_term_List(begin, end) { return tail } else if tom_equal_term_List(end, tail) && (tom_is_empty_conc_List(end) || tom_equal_term_List(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(tom_get_head_conc_List(begin), tom_get_slice_conc(tom_get_tail_conc_List(begin), end, tail)) }; 







func TestException(t *testing.T) {
    var a Element = tom_make_a()
    if catchIllegalArgument(func() {
      a.Length()
      fail(t, "length should raise an IllegalArgumentException")
    }) {
      return
    }
}



func TestZeroLength(t *testing.T) {
    var l List = tom_empty_list_conc()
    assertEquals(t, getListLength(l),l.Length())
}



func TestOneLength(t *testing.T) {
    var l List = tom_cons_list_conc(tom_make_a(),tom_empty_list_conc())
    assertEquals(t, getListLength(l),l.Length())
}



func TestTwoLength(t *testing.T) {
    var l List = tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()))
    assertEquals(t, getListLength(l),l.Length())
}
func TestNLength(t *testing.T) {
    n := 15
    var l List = tom_empty_list_conc()
    for i := 0; i<n; i++ {
      l = tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_append_list_conc(l,tom_empty_list_conc())))
    }
    assertEquals(t, getListLength(l),l.Length())
}



func TestZeroArray(t *testing.T) {
    var l List = tom_empty_list_conc()
    assertEquals(t, 0,len(l.(*ConcList).ToArray()))
}



func TestNArray(t *testing.T) {
    n := 15
    var l List = tom_empty_list_conc()
    for i := 0; i<n; i++ {
      l = tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_append_list_conc(l,tom_empty_list_conc())))
    }
    var array1 []Element = getListArray(l)
    var array2 []any = l.(*ConcList).ToArray() 
    assertEquals(t, len(array1),len(array2))
    for i:=0; i<len(array1); i++ {
      assertEquals(t, array1[i],array2[i])
    }
}



func TestFromZeroArray(t *testing.T) {
    var array []Element = []Element{}
    var l List = ConcFromArray(array)
    assertEquals(t, 0,l.Length())
}



func TestFromToZeroArray(t *testing.T) {
    var array []Element = []Element{}
    var l List = ConcFromArray(array)
    assertEquals(t, 0,len(l.(*ConcList).ToArray()))
}



func TestFromNArray(t *testing.T) {
    n := 15
    var array []Element = make([]Element, 15)
    for i := 0; i<n; i++ {
      if (0 == i%2) {
        array[i] = tom_make_a()
      } else {
        array[i] = tom_make_b()
      }
    }
    var list List = ConcFromArray(array)
    assertEquals(t, list.Length(),n)
    var l List = tom_empty_list_conc()
    for i := 0; i<n; i++ {
      if (0 == i%2) {
        l = tom_append_list_conc(l,tom_cons_list_conc(tom_make_a(),tom_empty_list_conc()))
      } else {
        l = tom_append_list_conc(l,tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()))
      }
    }
    assertEquals(t, l,list)
}



func TestZeroAdd(t *testing.T) {
    var l List = tom_empty_list_conc()
    assertEquals(t, tom_cons_list_conc(tom_make_a(),tom_empty_list_conc()),l.(*ConcList).Append(tom_make_a()))
}



func TestOneAdd(t *testing.T) {
    var l List = tom_cons_list_conc(tom_make_a(),tom_empty_list_conc())
    assertEquals(t, tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc())),l.(*ConcList).Append(tom_make_b()))
}



func TestZeroReverse(t *testing.T) {
    var l List = tom_empty_list_conc()
    assertEquals(t, getListReverse(l),l.Reverse())
}



func TestOneReverse(t *testing.T) {
    var l List = tom_cons_list_conc(tom_make_a(),tom_empty_list_conc())
    assertEquals(t, getListReverse(l),l.Reverse())
}



func TestTwoReverse(t *testing.T) {
    var l List = tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()))
    assertEquals(t, getListReverse(l),l.Reverse())
}



func TestNReverse(t *testing.T) {
    n := 15
    var l List = tom_empty_list_conc()
    for i := 0; i<n; i++ {
      l = tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_append_list_conc(l,tom_empty_list_conc())))
    }
    assertEquals(t, getListReverse(l),l.Reverse())
}

  func getListReverse(l List) List {
    ; { _ = l; { if tom_is_sort_List(l) { if tom_is_fun_sym_conc(any(l).( List )) { if !(tom_is_empty_conc_List(any(l).( List ))) { 

        var nt List = getListReverse(tom_get_tail_conc_List(any(l).( List )))
        return tom_append_list_conc(nt,tom_cons_list_conc(tom_get_head_conc_List(any(l).( List )),tom_empty_list_conc()));  };  };  };  };  }; 


    return l
  }

  func getListArray(l List) []Element {
    length := getListLength(l)
    var array []Element = make([]Element, length)
    ; { _ = l; { if tom_is_sort_List(l) { if tom_is_fun_sym_conc(any(l).( List )) { if !(tom_is_empty_conc_List(any(l).( List ))) { 

        array[0]=tom_get_head_conc_List(any(l).( List ))
        var tailArray []Element = getListArray(tom_get_tail_conc_List(any(l).( List )))
        for i:=0;i<length-1;i++ {
          array[i+1]=tailArray[i]
        };  };  };  };  };  }; 


    return array
  }

  func getListLength(l List) int {
    ; { _ = l; { if tom_is_sort_List(l) { if tom_is_fun_sym_conc(any(l).( List )) { if tom_is_empty_conc_List(any(l).( List )) { 
 return 0 ;  };  };  };  }; { if tom_is_sort_List(l) { if tom_is_fun_sym_conc(any(l).( List )) { if !(tom_is_empty_conc_List(any(l).( List ))) { 
 return 1+getListLength(tom_get_tail_conc_List(any(l).( List ))) ;  };  };  };  };  }; 

    return 0
  }








func catchIllegalArgument(f func()) (caught bool) {
  defer func() {
    if r := recover(); r != nil {
      if e, ok := r.(*aterm.GomError); ok && e.IllegalArgument {
        caught = true
        return
      }
      panic(r)
    }
  }()
  f()
  return false
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}




func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
