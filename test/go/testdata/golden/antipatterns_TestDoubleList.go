package antipatterns













import (
  "container/list"
  "testing"
)

  func tom_equal_term_LinkedList(l1  *list.List , l2  *list.List ) bool { return tom_linkedListEquals(l1, l2);  }; func tom_is_sort_LinkedList(t any) bool { return t != nil;  };   func tom_linkedListEquals(l1, l2 *list.List) bool { if l1.Len() != l2.Len() { return false }; for e1, e2 := l1.Front(), l2.Front(); e1 != nil; e1, e2 = e1.Next(), e2.Next() { if e1.Value != e2.Value { return false } }; return true }; func tom_equal_term_Object(o1  any , o2  any ) bool { return o1 == o2;  }; func tom_is_sort_Object(t any) bool { return true;  }; func tom_is_fun_sym_concLinkedList(l  *list.List ) bool { return (l != nil);  }; func tom_empty_list_concLinkedList()  *list.List  { return (list.New()) }; func tom_cons_list_concLinkedList(o  any , l  *list.List )  *list.List  { return (concLinkedListAppend(o,l)) }; func tom_get_head_concLinkedList_LinkedList(l  *list.List )  any  { return l.Front().Value;  }; func tom_get_tail_concLinkedList_LinkedList(l  *list.List )  *list.List  { return concLinkedListgetTail(l);  }; func tom_is_empty_concLinkedList_LinkedList(l  *list.List ) bool { return (l.Len()==0);  }; func tom_append_list_concLinkedList(l1  *list.List , l2  *list.List )  *list.List  { if tom_is_empty_concLinkedList_LinkedList(l1) { return l2 } else if tom_is_empty_concLinkedList_LinkedList(l2) { return l1 } else if tom_is_empty_concLinkedList_LinkedList(tom_get_tail_concLinkedList_LinkedList(l1)) { return tom_cons_list_concLinkedList(tom_get_head_concLinkedList_LinkedList(l1), l2) } else { return tom_cons_list_concLinkedList(tom_get_head_concLinkedList_LinkedList(l1), tom_append_list_concLinkedList(tom_get_tail_concLinkedList_LinkedList(l1), l2)) }; }; func tom_get_slice_concLinkedList(begin  *list.List , end  *list.List , tail  *list.List )  *list.List  { if tom_equal_term_LinkedList(begin, end) { return tail } else if tom_equal_term_LinkedList(end, tail) && (tom_is_empty_concLinkedList_LinkedList(end) || tom_equal_term_LinkedList(end, tom_empty_list_concLinkedList())) { return begin }; return tom_cons_list_concLinkedList(tom_get_head_concLinkedList_LinkedList(begin), tom_get_slice_concLinkedList(tom_get_tail_concLinkedList_LinkedList(begin), end, tail)) };   func concLinkedListAppend(o any, l *list.List) *list.List { res := list.New(); res.PushBackList(l); res.PushFront(o); return res };    func concLinkedListgetTail(l *list.List) *list.List { res := list.New(); res.PushBackList(l); res.Remove(res.Front()); return res }; 
  
func Test1(t *testing.T) {
    var l *list.List = newLinkedList(1,2,3,1,4,3,1,1)
    
    var result *list.List = list.New()
    ; { { if tom_is_sort_LinkedList(l) { var tomMatch1_freshSubject_0  *list.List  = any(l).( *list.List ); _ = tomMatch1_freshSubject_0; if tom_is_sort_LinkedList(l) { tomMatch1_freshSubject_0 = any(l).( *list.List ); if tom_is_fun_sym_concLinkedList(any(tomMatch1_freshSubject_0).( *list.List )) { var tomMatch1_end_5  *list.List  = tomMatch1_freshSubject_0; _ = tomMatch1_end_5; for { { if !(tom_is_empty_concLinkedList_LinkedList(tomMatch1_end_5)) { var tom___x  any  = tom_get_head_concLinkedList_LinkedList(tomMatch1_end_5); _ = tom___x; var tomMatch1_20 bool =  false ; _ = tomMatch1_20; if tom_is_fun_sym_concLinkedList(any(tomMatch1_freshSubject_0).( *list.List )) { var tomMatch1_end_11  *list.List  = tomMatch1_freshSubject_0; _ = tomMatch1_end_11; for { { if !(tom_is_empty_concLinkedList_LinkedList(tomMatch1_end_11)) { if tom_equal_term_Object(tom___x, tom_get_head_concLinkedList_LinkedList(tomMatch1_end_11)) { var tomMatch1_12  *list.List  = tom_get_tail_concLinkedList_LinkedList(tomMatch1_end_11); _ = tomMatch1_12; var tomMatch1_end_15  *list.List  = tomMatch1_12; _ = tomMatch1_end_15; for { { if !(tom_is_empty_concLinkedList_LinkedList(tomMatch1_end_15)) { if tom_equal_term_Object(tom___x, tom_get_head_concLinkedList_LinkedList(tomMatch1_end_15)) { tomMatch1_20 =  true ;  };  }; if tom_is_empty_concLinkedList_LinkedList(tomMatch1_end_15) { tomMatch1_end_15 = tomMatch1_12;  } else { tomMatch1_end_15 = tom_get_tail_concLinkedList_LinkedList(tomMatch1_end_15);  };  };  if !(!(tom_equal_term_LinkedList(tomMatch1_end_15, tomMatch1_12))) { break; }; };  };  }; if tom_is_empty_concLinkedList_LinkedList(tomMatch1_end_11) { tomMatch1_end_11 = tomMatch1_freshSubject_0;  } else { tomMatch1_end_11 = tom_get_tail_concLinkedList_LinkedList(tomMatch1_end_11);  };  };  if !(!(tom_equal_term_LinkedList(tomMatch1_end_11, tomMatch1_freshSubject_0))) { break; }; };  }; if !(tomMatch1_20) { 
 result.PushBack(tom___x.(int))  };  }; if tom_is_empty_concLinkedList_LinkedList(tomMatch1_end_5) { tomMatch1_end_5 = tomMatch1_freshSubject_0;  } else { tomMatch1_end_5 = tom_get_tail_concLinkedList_LinkedList(tomMatch1_end_5);  };  };  if !(!(tom_equal_term_LinkedList(tomMatch1_end_5, tomMatch1_freshSubject_0))) { break; }; };  };  };  };  };  }; 

    if (!(contains(result, 2) && contains(result, 4))) { fail(t) }
}
  

func Test2(t *testing.T) {
    var l *list.List = newLinkedList(1,2,3,1,4,3,1,1)
    
    var result *list.List = list.New()
    ; { { if tom_is_sort_LinkedList(l) { var tomMatch2_freshSubject_0  *list.List  = any(l).( *list.List ); _ = tomMatch2_freshSubject_0; if tom_is_sort_LinkedList(l) { tomMatch2_freshSubject_0 = any(l).( *list.List ); if tom_is_fun_sym_concLinkedList(any(tomMatch2_freshSubject_0).( *list.List )) { var tomMatch2_end_5  *list.List  = tomMatch2_freshSubject_0; _ = tomMatch2_end_5; for { { if !(tom_is_empty_concLinkedList_LinkedList(tomMatch2_end_5)) { var tom___x  any  = tom_get_head_concLinkedList_LinkedList(tomMatch2_end_5); _ = tom___x; var tomMatch2_20 bool =  false ; _ = tomMatch2_20; if tom_is_fun_sym_concLinkedList(any(tomMatch2_freshSubject_0).( *list.List )) { var tomMatch2_end_11  *list.List  = tomMatch2_freshSubject_0; _ = tomMatch2_end_11; for { { if !(tom_is_empty_concLinkedList_LinkedList(tomMatch2_end_11)) { if tom_equal_term_Object(tom___x, tom_get_head_concLinkedList_LinkedList(tomMatch2_end_11)) { var tomMatch2_12  *list.List  = tom_get_tail_concLinkedList_LinkedList(tomMatch2_end_11); _ = tomMatch2_12; var tomMatch2_end_15  *list.List  = tomMatch2_12; _ = tomMatch2_end_15; for { { if !(tom_is_empty_concLinkedList_LinkedList(tomMatch2_end_15)) { if tom_equal_term_Object(tom___x, tom_get_head_concLinkedList_LinkedList(tomMatch2_end_15)) { tomMatch2_20 =  true ;  };  }; if tom_is_empty_concLinkedList_LinkedList(tomMatch2_end_15) { tomMatch2_end_15 = tomMatch2_12;  } else { tomMatch2_end_15 = tom_get_tail_concLinkedList_LinkedList(tomMatch2_end_15);  };  };  if !(!(tom_equal_term_LinkedList(tomMatch2_end_15, tomMatch2_12))) { break; }; };  };  }; if tom_is_empty_concLinkedList_LinkedList(tomMatch2_end_11) { tomMatch2_end_11 = tomMatch2_freshSubject_0;  } else { tomMatch2_end_11 = tom_get_tail_concLinkedList_LinkedList(tomMatch2_end_11);  };  };  if !(!(tom_equal_term_LinkedList(tomMatch2_end_11, tomMatch2_freshSubject_0))) { break; }; };  }; if !(tomMatch2_20) { 
 result.PushBack(tom___x.(int))  };  }; if tom_is_empty_concLinkedList_LinkedList(tomMatch2_end_5) { tomMatch2_end_5 = tomMatch2_freshSubject_0;  } else { tomMatch2_end_5 = tom_get_tail_concLinkedList_LinkedList(tomMatch2_end_5);  };  };  if !(!(tom_equal_term_LinkedList(tomMatch2_end_5, tomMatch2_freshSubject_0))) { break; }; };  };  };  };  };  }; 

    if (!(contains(result, 2) && contains(result, 4))) { fail(t) }
}




func newLinkedList(xs ...int) *list.List {
  l := list.New()
  for _, x := range xs {
    l.PushBack(x)
  }
  return l
}




func contains(l *list.List, x any) bool {
  for e := l.Front(); e != nil; e = e.Next() {
    if e.Value == x {
      return true
    }
  }
  return false
}




func fail(t *testing.T) {
  t.Helper()
  t.Fatal()
}
