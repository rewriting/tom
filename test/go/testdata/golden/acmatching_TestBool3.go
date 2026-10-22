package acmatching












import (
  "testing"
)

  func __getTermForMult_m(tempSol []int64, alpha []int64, subject  T , isComplement bool)  T  { var result  T  = tom_empty_list_m(); _ = result; var old  T  =  *new(T) ; _ = old; var elem  T  =  *new(T) ; _ = elem; var elemCounter int64 = 0; _ = elemCounter; var tempSolIndex int64 = -1; _ = tempSolIndex; { for !(tom_equal_term_T(tom_empty_list_m(), subject)) { { if tom_is_fun_sym_m(subject) { elem = tom_get_head_m_T(subject); subject = tom_get_tail_m_T(subject);  } else { elem = subject; subject = tom_empty_list_m();  }; if !(tom_equal_term_T(elem, old)) { tempSolIndex = tempSolIndex + 1; old = elem; elemCounter = 0;  }; var tempSolVal int64 = tom_get_element_concInt_intarray(tempSol,tempSolIndex); _ = tempSolVal; { if isComplement { tempSolVal = tom_get_element_concInt_intarray(alpha,tempSolIndex) - tempSolVal;  }; if  (!(tom_equal_term_int(tempSolVal, 0)) && elemCounter < tempSolVal)  { result = tom_append_list_m(result,elem); elemCounter = elemCounter + 1;  };  };  };  }; return result;  };  }; func __getMultiplicities_m(subject  T ) []int64 { var tom_length_2 int64 = __computeLength_m(subject); _ = tom_length_2; var tom_mult_3 []int64 = tom_empty_array_concInt(tom_length_2); _ = tom_mult_3; var oldElem  T  =  *new(T) ; _ = oldElem; { if tom_equal_term_T(subject, tom_empty_list_m()) { return tom_mult_3;  } else { if tom_is_fun_sym_m(subject) { oldElem = tom_get_head_m_T(subject);  } else { tom_mult_3[0] = 1; return tom_mult_3;  };  }; var counter int64 = 0; _ = counter; for  (tom_is_fun_sym_m(subject) && !(tom_equal_term_T(subject, tom_empty_list_m())))  { { var elem  T  = tom_get_head_m_T(subject); _ = elem; if tom_equal_term_T(elem, oldElem) { tom_mult_3[counter] = tom_get_element_concInt_intarray(tom_mult_3,counter) + 1;  } else { counter = counter + 1; oldElem = elem; tom_mult_3[counter] = 1;  }; subject = tom_get_tail_m_T(subject); if !(tom_is_fun_sym_m(subject)) { if tom_equal_term_T(subject, oldElem) { tom_mult_3[counter] = tom_get_element_concInt_intarray(tom_mult_3,counter) + 1;  } else { counter = counter + 1; tom_mult_3[counter] = 1;  };  };  };  }; return tom_mult_3;  };  }; func __computeLength_m(subject  T ) int64 { { if !(tom_is_fun_sym_m(subject)) { return 1;  }; var old  T  =  *new(T) ; _ = old; var counter int64 = 0; _ = counter; { for  (tom_is_fun_sym_m(subject) && !(tom_equal_term_T(subject, tom_empty_list_m())))  { { var elem  T  = tom_get_head_m_T(subject); _ = elem; if !(tom_equal_term_T(elem, old)) { { counter = counter + 1; old = elem;  };  }; subject = tom_get_tail_m_T(subject); if !(tom_is_fun_sym_m(subject)) { if !(tom_equal_term_T(subject, old)) { counter = counter + 1;  };  };  };  }; return counter;  };  };  }; 
func next_minimal_extract(multiplicity int64, total int64, E []int64, sol []int64) bool {
  pos := total-1
  for pos>=0 && (sol[pos]+multiplicity)>E[pos] {
    sol[pos]=0
    pos--
  }
  if pos<0 {
    return false
  }
  sol[pos]+=multiplicity
  return true
}
func __getTermForMult_p(tempSol []int64, alpha []int64, subject  T , isComplement bool)  T  { var result  T  = tom_empty_list_p(); _ = result; var old  T  =  *new(T) ; _ = old; var elem  T  =  *new(T) ; _ = elem; var elemCounter int64 = 0; _ = elemCounter; var tempSolIndex int64 = -1; _ = tempSolIndex; { for !(tom_equal_term_T(tom_empty_list_p(), subject)) { { if tom_is_fun_sym_p(subject) { elem = tom_get_head_p_T(subject); subject = tom_get_tail_p_T(subject);  } else { elem = subject; subject = tom_empty_list_p();  }; if !(tom_equal_term_T(elem, old)) { tempSolIndex = tempSolIndex + 1; old = elem; elemCounter = 0;  }; var tempSolVal int64 = tom_get_element_concInt_intarray(tempSol,tempSolIndex); _ = tempSolVal; { if isComplement { tempSolVal = tom_get_element_concInt_intarray(alpha,tempSolIndex) - tempSolVal;  }; if  (!(tom_equal_term_int(tempSolVal, 0)) && elemCounter < tempSolVal)  { result = tom_append_list_p(result,elem); elemCounter = elemCounter + 1;  };  };  };  }; return result;  };  }; func __getMultiplicities_p(subject  T ) []int64 { var tom_length_0 int64 = __computeLength_p(subject); _ = tom_length_0; var tom_mult_1 []int64 = tom_empty_array_concInt(tom_length_0); _ = tom_mult_1; var oldElem  T  =  *new(T) ; _ = oldElem; { if tom_equal_term_T(subject, tom_empty_list_p()) { return tom_mult_1;  } else { if tom_is_fun_sym_p(subject) { oldElem = tom_get_head_p_T(subject);  } else { tom_mult_1[0] = 1; return tom_mult_1;  };  }; var counter int64 = 0; _ = counter; for  (tom_is_fun_sym_p(subject) && !(tom_equal_term_T(subject, tom_empty_list_p())))  { { var elem  T  = tom_get_head_p_T(subject); _ = elem; if tom_equal_term_T(elem, oldElem) { tom_mult_1[counter] = tom_get_element_concInt_intarray(tom_mult_1,counter) + 1;  } else { counter = counter + 1; oldElem = elem; tom_mult_1[counter] = 1;  }; subject = tom_get_tail_p_T(subject); if !(tom_is_fun_sym_p(subject)) { if tom_equal_term_T(subject, oldElem) { tom_mult_1[counter] = tom_get_element_concInt_intarray(tom_mult_1,counter) + 1;  } else { counter = counter + 1; tom_mult_1[counter] = 1;  };  };  };  }; return tom_mult_1;  };  }; func __computeLength_p(subject  T ) int64 { { if !(tom_is_fun_sym_p(subject)) { return 1;  }; var old  T  =  *new(T) ; _ = old; var counter int64 = 0; _ = counter; { for  (tom_is_fun_sym_p(subject) && !(tom_equal_term_T(subject, tom_empty_list_p())))  { { var elem  T  = tom_get_head_p_T(subject); _ = elem; if !(tom_equal_term_T(elem, old)) { { counter = counter + 1; old = elem;  };  }; subject = tom_get_tail_p_T(subject); if !(tom_is_fun_sym_p(subject)) { if !(tom_equal_term_T(subject, old)) { counter = counter + 1;  };  };  };  }; return counter;  };  };  }; func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_intarray(t1  []int64 , t2  []int64 ) bool { return tom_intArrayEquals(t1, t2);  }; func tom_is_sort_intarray(t any) bool { return t != nil;  }; func tom_is_fun_sym_concInt(l  []int64 ) bool { return l != nil;  }; func tom_empty_array_concInt(n int64)  []int64  { return (make([]int64, n)) }; func tom_cons_array_concInt(o  int64 , l  []int64 )  []int64  { return (concIntArrayAppend(o,l)) }; func tom_get_element_concInt_intarray(l  []int64 , n int64)  int64  { return l[n];  }; func tom_get_size_concInt_intarray(l  []int64 ) int64 { return int64(len(l));  }; func tom_get_slice_concInt(subject  []int64 , begin int64, end int64)  []int64  { var result  []int64  = tom_empty_array_concInt(end-begin); for begin != end { result = tom_cons_array_concInt(tom_get_element_concInt_intarray(subject, begin), result); begin++ }; return result }; func tom_append_array_concInt(l2  []int64 , l1  []int64 )  []int64  { var size1 int64 = tom_get_size_concInt_intarray(l1); var size2 int64 = tom_get_size_concInt_intarray(l2); var index int64; var result  []int64  = tom_empty_array_concInt(size1+size2); index = size1; for index > 0 { result = tom_cons_array_concInt(tom_get_element_concInt_intarray(l1, size1-index), result); index-- }; index = size2; for index > 0 { result = tom_cons_array_concInt(tom_get_element_concInt_intarray(l2, size2-index), result); index-- }; return result };   func concIntArrayAppend(o int64, array []int64) []int64 { tmp := make([]int64, len(array)+1); copy(tmp, array); tmp[len(array)] = o; return tmp };    func tom_intArrayEquals(t1, t2 []int64) bool { if len(t1) != len(t2) { return false }; for i := range t1 { if t1[i] != t2[i] { return false } }; return true }; func tom_equal_term_T(t1  T , t2  T ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_T(t any) bool { return func() bool { _, ok := (t).(T); return ok }();  }; func tom_is_fun_sym_b0(t  T ) bool { return func() bool { _, ok := (t).(*B0T); return ok }();  }; func tom_make_b0()  T  { return (MakeB0()) }; func tom_is_fun_sym_b1(t  T ) bool { return func() bool { _, ok := (t).(*B1T); return ok }();  }; func tom_make_b1()  T  { return (MakeB1()) }; func tom_make_a1()  T  { return (MakeA1()) }; func tom_make_a2()  T  { return (MakeA2()) }; func tom_make_a3()  T  { return (MakeA3()) }; func tom_make_a4()  T  { return (MakeA4()) }; func tom_make_a5()  T  { return (MakeA5()) }; func tom_make_a6()  T  { return (MakeA6()) }; func tom_is_fun_sym_p(t  T ) bool { return func() bool { _, ok := (t).(*PT); return ok }();  }; func tom_empty_list_p()  T  { return (EmptyP()) }; func tom_cons_list_p(e  T , l  T )  T  { return (ConsP(e,l)) }; func tom_get_head_p_T(l  T )  T  { return (l).GetHeadp();  }; func tom_get_tail_p_T(l  T )  T  { return (l).GetTailp();  }; func tom_is_empty_p_T(l  T ) bool { return (l).IsEmptyp();  }; func tom_append_list_p(l1  T , l2  T )  T  { if tom_is_empty_p_T(l1) { return l2 } else if tom_is_empty_p_T(l2) { return l1 } else if tom_is_fun_sym_p(l1) { if tom_is_empty_p_T(tom_get_tail_p_T(l1)) { return tom_cons_list_p(tom_get_head_p_T(l1), l2) } else { return tom_cons_list_p(tom_get_head_p_T(l1), tom_append_list_p(tom_get_tail_p_T(l1), l2)) } } else { return tom_cons_list_p(l1, l2) }; }; func tom_get_slice_p(begin  T , end  T , tail  T )  T  { if tom_equal_term_T(begin, end) { return tail } else if tom_equal_term_T(end, tail) && (tom_is_empty_p_T(end) || tom_equal_term_T(end, tom_empty_list_p())) { return begin }; return tom_cons_list_p(func()  T  { if tom_is_fun_sym_p(begin) { return tom_get_head_p_T(begin) }; return begin }(), tom_get_slice_p(func()  T  { if tom_is_fun_sym_p(begin) { return tom_get_tail_p_T(begin) }; return tom_empty_list_p() }(), end, tail)) }; func tom_is_fun_sym_m(t  T ) bool { return func() bool { _, ok := (t).(*MT); return ok }();  }; func tom_empty_list_m()  T  { return (EmptyM()) }; func tom_cons_list_m(e  T , l  T )  T  { return (ConsM(e,l)) }; func tom_get_head_m_T(l  T )  T  { return (l).GetHeadm();  }; func tom_get_tail_m_T(l  T )  T  { return (l).GetTailm();  }; func tom_is_empty_m_T(l  T ) bool { return (l).IsEmptym();  }; func tom_append_list_m(l1  T , l2  T )  T  { if tom_is_empty_m_T(l1) { return l2 } else if tom_is_empty_m_T(l2) { return l1 } else if tom_is_fun_sym_m(l1) { if tom_is_empty_m_T(tom_get_tail_m_T(l1)) { return tom_cons_list_m(tom_get_head_m_T(l1), l2) } else { return tom_cons_list_m(tom_get_head_m_T(l1), tom_append_list_m(tom_get_tail_m_T(l1), l2)) } } else { return tom_cons_list_m(l1, l2) }; }; func tom_get_slice_m(begin  T , end  T , tail  T )  T  { if tom_equal_term_T(begin, end) { return tail } else if tom_equal_term_T(end, tail) && (tom_is_empty_m_T(end) || tom_equal_term_T(end, tom_empty_list_m())) { return begin }; return tom_cons_list_m(func()  T  { if tom_is_fun_sym_m(begin) { return tom_get_head_m_T(begin) }; return begin }(), tom_get_slice_m(func()  T  { if tom_is_fun_sym_m(begin) { return tom_get_tail_m_T(begin) }; return tom_empty_list_m() }(), end, tail)) }; 




























func Test1(t *testing.T) {
      var res1 T = e_and(tom_make_a1(),tom_make_a1())
      var res2 T = e_not( e_or( e_not(tom_make_a1()), e_not(tom_make_a1())))
      assertEquals(t, "and(a1,a1)==not(or(not(a1),not(a1)))",res1,res2)
}



func Test2(t *testing.T) {
      
      var res1 T = e_and(e_and(e_and(tom_make_a1(), tom_make_a2()), e_and(tom_make_a3(), tom_make_a4())),e_and(tom_make_a5(),tom_make_a6()))
      var res2 T = e_not(e_or(e_or(e_or(e_not(tom_make_a1()), e_not(tom_make_a2())), e_or(e_not(tom_make_a3()), e_not(tom_make_a4()))),e_or(e_not(tom_make_a5()),e_not(tom_make_a6()))))

      
      
      
      assertEquals(t, "and(a1,...,a6)==not(or(not(a1),...,not(a6)))",res1,res2)
}



func e(t T) T {
    ; { { if tom_is_sort_T(t) { if tom_is_fun_sym_m(any(t).( T )) { if !( (tom_is_empty_m_T(any(t).( T )) || tom_equal_term_T(any(t).( T ), tom_empty_list_m())) ) { var tomMatch1_2  T  = func()  T  { if tom_is_fun_sym_m(any(t).( T )) { return tom_get_tail_m_T(any(t).( T )) }; return tom_empty_list_m() }(); _ = tomMatch1_2; if  (tom_is_empty_m_T(tomMatch1_2) || tom_equal_term_T(tomMatch1_2, tom_empty_list_m()))  { 
 
        return e(func()  T  { if tom_is_fun_sym_m(any(t).( T )) { return tom_get_head_m_T(any(t).( T )) }; return any(t).( T ) }()) };  };  };  };  }; { if tom_is_sort_T(t) { if tom_is_fun_sym_m(any(t).( T )) { var tomMatch1_end_16  T  = any(t).( T ); _ = tomMatch1_end_16; for { { if !( (tom_is_empty_m_T(tomMatch1_end_16) || tom_equal_term_T(tomMatch1_end_16, tom_empty_list_m())) ) { var tom___X  T  = func()  T  { if tom_is_fun_sym_m(tomMatch1_end_16) { return tom_get_head_m_T(tomMatch1_end_16) }; return tomMatch1_end_16 }(); _ = tom___X; var tomMatch1_19  T  = tom_append_list_m(tom_get_slice_m(any(t).( T ),tomMatch1_end_16,tom_empty_list_m()),tom_append_list_m(func()  T  { if tom_is_fun_sym_m(tomMatch1_end_16) { return tom_get_tail_m_T(tomMatch1_end_16) }; return tom_empty_list_m() }(),tom_empty_list_m())); _ = tomMatch1_19; if tom_is_fun_sym_m(any(tomMatch1_19).( T )) { var tomMatch1_end_22  T  = tomMatch1_19; _ = tomMatch1_end_22; for { { if !( (tom_is_empty_m_T(tomMatch1_end_22) || tom_equal_term_T(tomMatch1_end_22, tom_empty_list_m())) ) { if tom_equal_term_T(tom___X, func()  T  { if tom_is_fun_sym_m(tomMatch1_end_22) { return tom_get_head_m_T(tomMatch1_end_22) }; return tomMatch1_end_22 }()) { var tomMatch1_25  T  = tom_append_list_m(tom_get_slice_m(tomMatch1_19,tomMatch1_end_22,tom_empty_list_m()),tom_append_list_m(func()  T  { if tom_is_fun_sym_m(tomMatch1_end_22) { return tom_get_tail_m_T(tomMatch1_end_22) }; return tom_empty_list_m() }(),tom_empty_list_m())); _ = tomMatch1_25; if tom_is_fun_sym_m(any(tomMatch1_25).( T )) { var tomMatch1_end_28  T  = tomMatch1_25; _ = tomMatch1_end_28; for { { if !( (tom_is_empty_m_T(tomMatch1_end_28) || tom_equal_term_T(tomMatch1_end_28, tom_empty_list_m())) ) { if tom_equal_term_T(tom___X, func()  T  { if tom_is_fun_sym_m(tomMatch1_end_28) { return tom_get_head_m_T(tomMatch1_end_28) }; return tomMatch1_end_28 }()) { var tomMatch1_31  T  = tom_append_list_m(tom_get_slice_m(tomMatch1_25,tomMatch1_end_28,tom_empty_list_m()),tom_append_list_m(func()  T  { if tom_is_fun_sym_m(tomMatch1_end_28) { return tom_get_tail_m_T(tomMatch1_end_28) }; return tom_empty_list_m() }(),tom_empty_list_m())); _ = tomMatch1_31; if tom_is_fun_sym_m(any(tomMatch1_31).( T )) { 


 
        return e(tom_cons_list_p(e(tom_cons_list_m(tom___X,tom_append_list_m(tomMatch1_31,tom_empty_list_m()))),tom_empty_list_p())) };  };  }; if  (tom_is_empty_m_T(tomMatch1_end_28) || tom_equal_term_T(tomMatch1_end_28, tom_empty_list_m()))  { tomMatch1_end_28 = tomMatch1_25;  } else { tomMatch1_end_28 = func()  T  { if tom_is_fun_sym_m(tomMatch1_end_28) { return tom_get_tail_m_T(tomMatch1_end_28) }; return tom_empty_list_m() }();  };  };  if !(!(tom_equal_term_T(tomMatch1_end_28, tomMatch1_25))) { break; }; };  };  };  }; if  (tom_is_empty_m_T(tomMatch1_end_22) || tom_equal_term_T(tomMatch1_end_22, tom_empty_list_m()))  { tomMatch1_end_22 = tomMatch1_19;  } else { tomMatch1_end_22 = func()  T  { if tom_is_fun_sym_m(tomMatch1_end_22) { return tom_get_tail_m_T(tomMatch1_end_22) }; return tom_empty_list_m() }();  };  };  if !(!(tom_equal_term_T(tomMatch1_end_22, tomMatch1_19))) { break; }; };  };  }; if  (tom_is_empty_m_T(tomMatch1_end_16) || tom_equal_term_T(tomMatch1_end_16, tom_empty_list_m()))  { tomMatch1_end_16 = any(t).( T );  } else { tomMatch1_end_16 = func()  T  { if tom_is_fun_sym_m(tomMatch1_end_16) { return tom_get_tail_m_T(tomMatch1_end_16) }; return tom_empty_list_m() }();  };  };  if !(!(tom_equal_term_T(tomMatch1_end_16, any(t).( T )))) { break; }; };  };  };  }; { if tom_is_sort_T(t) { if tom_is_fun_sym_m(any(t).( T )) { var tomMatch1_end_42  T  = any(t).( T ); _ = tomMatch1_end_42; for { { if !( (tom_is_empty_m_T(tomMatch1_end_42) || tom_equal_term_T(tomMatch1_end_42, tom_empty_list_m())) ) { if tom_is_fun_sym_b0(any(func()  T  { if tom_is_fun_sym_m(tomMatch1_end_42) { return tom_get_head_m_T(tomMatch1_end_42) }; return tomMatch1_end_42 }()).( T )) { if tom_is_fun_sym_m(any(tom_append_list_m(tom_get_slice_m(any(t).( T ),tomMatch1_end_42,tom_empty_list_m()),tom_append_list_m(func()  T  { if tom_is_fun_sym_m(tomMatch1_end_42) { return tom_get_tail_m_T(tomMatch1_end_42) }; return tom_empty_list_m() }(),tom_empty_list_m()))).( T )) { 


 
        return tom_make_b0() };  };  }; if  (tom_is_empty_m_T(tomMatch1_end_42) || tom_equal_term_T(tomMatch1_end_42, tom_empty_list_m()))  { tomMatch1_end_42 = any(t).( T );  } else { tomMatch1_end_42 = func()  T  { if tom_is_fun_sym_m(tomMatch1_end_42) { return tom_get_tail_m_T(tomMatch1_end_42) }; return tom_empty_list_m() }();  };  };  if !(!(tom_equal_term_T(tomMatch1_end_42, any(t).( T )))) { break; }; };  };  };  }; { if tom_is_sort_T(t) { if tom_is_fun_sym_m(any(t).( T )) { var tomMatch1_end_56  T  = any(t).( T ); _ = tomMatch1_end_56; for { { if !( (tom_is_empty_m_T(tomMatch1_end_56) || tom_equal_term_T(tomMatch1_end_56, tom_empty_list_m())) ) { if tom_is_fun_sym_b1(any(func()  T  { if tom_is_fun_sym_m(tomMatch1_end_56) { return tom_get_head_m_T(tomMatch1_end_56) }; return tomMatch1_end_56 }()).( T )) { var tomMatch1_59  T  = tom_append_list_m(tom_get_slice_m(any(t).( T ),tomMatch1_end_56,tom_empty_list_m()),tom_append_list_m(func()  T  { if tom_is_fun_sym_m(tomMatch1_end_56) { return tom_get_tail_m_T(tomMatch1_end_56) }; return tom_empty_list_m() }(),tom_empty_list_m())); _ = tomMatch1_59; if tom_is_fun_sym_m(any(tomMatch1_59).( T )) { 


 
        return e(tomMatch1_59) };  };  }; if  (tom_is_empty_m_T(tomMatch1_end_56) || tom_equal_term_T(tomMatch1_end_56, tom_empty_list_m()))  { tomMatch1_end_56 = any(t).( T );  } else { tomMatch1_end_56 = func()  T  { if tom_is_fun_sym_m(tomMatch1_end_56) { return tom_get_tail_m_T(tomMatch1_end_56) }; return tom_empty_list_m() }();  };  };  if !(!(tom_equal_term_T(tomMatch1_end_56, any(t).( T )))) { break; }; };  };  };  }; { if tom_is_sort_T(t) { if tom_is_fun_sym_m(any(t).( T )) { var tomMatch1_end_70  T  = any(t).( T ); _ = tomMatch1_end_70; for { { if !( (tom_is_empty_m_T(tomMatch1_end_70) || tom_equal_term_T(tomMatch1_end_70, tom_empty_list_m())) ) { var tomMatch1_alpha_75 []int64 = __getMultiplicities_p(func()  T  { if tom_is_fun_sym_m(tomMatch1_end_70) { return tom_get_head_m_T(tomMatch1_end_70) }; return tomMatch1_end_70 }()); _ = tomMatch1_alpha_75; var tomMatch1_length_78 int64 = tom_get_size_concInt_intarray(tomMatch1_alpha_75); _ = tomMatch1_length_78; var tomMatch1_tempSol_76 []int64 = tom_empty_array_concInt(tomMatch1_length_78); _ = tomMatch1_tempSol_76; for { var tom___X  T  = __getTermForMult_p(tomMatch1_tempSol_76,tomMatch1_alpha_75,func()  T  { if tom_is_fun_sym_m(tomMatch1_end_70) { return tom_get_head_m_T(tomMatch1_end_70) }; return tomMatch1_end_70 }(), false ); _ = tom___X; var tom___Y  T  = __getTermForMult_p(tomMatch1_tempSol_76,tomMatch1_alpha_75,func()  T  { if tom_is_fun_sym_m(tomMatch1_end_70) { return tom_get_head_m_T(tomMatch1_end_70) }; return tomMatch1_end_70 }(), true ); _ = tom___Y; var tomMatch1_73  T  = tom_append_list_m(tom_get_slice_m(any(t).( T ),tomMatch1_end_70,tom_empty_list_m()),tom_append_list_m(func()  T  { if tom_is_fun_sym_m(tomMatch1_end_70) { return tom_get_tail_m_T(tomMatch1_end_70) }; return tom_empty_list_m() }(),tom_empty_list_m())); _ = tomMatch1_73; if tom_is_fun_sym_m(any(tomMatch1_73).( T )) { var tom___Z  T  = tomMatch1_73; _ = tom___Z; 



        if(!tom___X.IsEmptyp() && !tom___Y.IsEmptyp() && !tom___Z.IsEmptym()) { 
          return e(tom_cons_list_p(e(tom_cons_list_m(e(tom___X),tom_append_list_m(tom___Z,tom_empty_list_m()))),tom_cons_list_p(e(tom_cons_list_m(e(tom___Y),tom_append_list_m(tom___Z,tom_empty_list_m()))),tom_empty_list_p()))) };  };  if !(next_minimal_extract(1,tomMatch1_length_78,tomMatch1_alpha_75,tomMatch1_tempSol_76)) { break; }; };  }; if  (tom_is_empty_m_T(tomMatch1_end_70) || tom_equal_term_T(tomMatch1_end_70, tom_empty_list_m()))  { tomMatch1_end_70 = any(t).( T );  } else { tomMatch1_end_70 = func()  T  { if tom_is_fun_sym_m(tomMatch1_end_70) { return tom_get_tail_m_T(tomMatch1_end_70) }; return tom_empty_list_m() }();  };  };  if !(!(tom_equal_term_T(tomMatch1_end_70, any(t).( T )))) { break; }; };  };  };  }; { if tom_is_sort_T(t) { if tom_is_fun_sym_p(any(t).( T )) { if !( (tom_is_empty_p_T(any(t).( T )) || tom_equal_term_T(any(t).( T ), tom_empty_list_p())) ) { var tomMatch1_82  T  = func()  T  { if tom_is_fun_sym_p(any(t).( T )) { return tom_get_tail_p_T(any(t).( T )) }; return tom_empty_list_p() }(); _ = tomMatch1_82; if  (tom_is_empty_p_T(tomMatch1_82) || tom_equal_term_T(tomMatch1_82, tom_empty_list_p()))  { 






 
        return e(func()  T  { if tom_is_fun_sym_p(any(t).( T )) { return tom_get_head_p_T(any(t).( T )) }; return any(t).( T ) }()) };  };  };  };  }; { if tom_is_sort_T(t) { if tom_is_fun_sym_p(any(t).( T )) { var tomMatch1_end_96  T  = any(t).( T ); _ = tomMatch1_end_96; for { { if !( (tom_is_empty_p_T(tomMatch1_end_96) || tom_equal_term_T(tomMatch1_end_96, tom_empty_list_p())) ) { var tom___X  T  = func()  T  { if tom_is_fun_sym_p(tomMatch1_end_96) { return tom_get_head_p_T(tomMatch1_end_96) }; return tomMatch1_end_96 }(); _ = tom___X; var tomMatch1_99  T  = tom_append_list_p(tom_get_slice_p(any(t).( T ),tomMatch1_end_96,tom_empty_list_p()),tom_append_list_p(func()  T  { if tom_is_fun_sym_p(tomMatch1_end_96) { return tom_get_tail_p_T(tomMatch1_end_96) }; return tom_empty_list_p() }(),tom_empty_list_p())); _ = tomMatch1_99; if tom_is_fun_sym_p(any(tomMatch1_99).( T )) { var tomMatch1_end_102  T  = tomMatch1_99; _ = tomMatch1_end_102; for { { if !( (tom_is_empty_p_T(tomMatch1_end_102) || tom_equal_term_T(tomMatch1_end_102, tom_empty_list_p())) ) { if tom_equal_term_T(tom___X, func()  T  { if tom_is_fun_sym_p(tomMatch1_end_102) { return tom_get_head_p_T(tomMatch1_end_102) }; return tomMatch1_end_102 }()) { var tomMatch1_105  T  = tom_append_list_p(tom_get_slice_p(tomMatch1_99,tomMatch1_end_102,tom_empty_list_p()),tom_append_list_p(func()  T  { if tom_is_fun_sym_p(tomMatch1_end_102) { return tom_get_tail_p_T(tomMatch1_end_102) }; return tom_empty_list_p() }(),tom_empty_list_p())); _ = tomMatch1_105; if tom_is_fun_sym_p(any(tomMatch1_105).( T )) { var tomMatch1_end_108  T  = tomMatch1_105; _ = tomMatch1_end_108; for { { if !( (tom_is_empty_p_T(tomMatch1_end_108) || tom_equal_term_T(tomMatch1_end_108, tom_empty_list_p())) ) { if tom_equal_term_T(tom___X, func()  T  { if tom_is_fun_sym_p(tomMatch1_end_108) { return tom_get_head_p_T(tomMatch1_end_108) }; return tomMatch1_end_108 }()) { var tomMatch1_111  T  = tom_append_list_p(tom_get_slice_p(tomMatch1_105,tomMatch1_end_108,tom_empty_list_p()),tom_append_list_p(func()  T  { if tom_is_fun_sym_p(tomMatch1_end_108) { return tom_get_tail_p_T(tomMatch1_end_108) }; return tom_empty_list_p() }(),tom_empty_list_p())); _ = tomMatch1_111; if tom_is_fun_sym_p(any(tomMatch1_111).( T )) { 


 
        return e(tom_cons_list_p(tom_make_b0(),tom_append_list_p(tomMatch1_111,tom_empty_list_p()))) };  };  }; if  (tom_is_empty_p_T(tomMatch1_end_108) || tom_equal_term_T(tomMatch1_end_108, tom_empty_list_p()))  { tomMatch1_end_108 = tomMatch1_105;  } else { tomMatch1_end_108 = func()  T  { if tom_is_fun_sym_p(tomMatch1_end_108) { return tom_get_tail_p_T(tomMatch1_end_108) }; return tom_empty_list_p() }();  };  };  if !(!(tom_equal_term_T(tomMatch1_end_108, tomMatch1_105))) { break; }; };  };  };  }; if  (tom_is_empty_p_T(tomMatch1_end_102) || tom_equal_term_T(tomMatch1_end_102, tom_empty_list_p()))  { tomMatch1_end_102 = tomMatch1_99;  } else { tomMatch1_end_102 = func()  T  { if tom_is_fun_sym_p(tomMatch1_end_102) { return tom_get_tail_p_T(tomMatch1_end_102) }; return tom_empty_list_p() }();  };  };  if !(!(tom_equal_term_T(tomMatch1_end_102, tomMatch1_99))) { break; }; };  };  }; if  (tom_is_empty_p_T(tomMatch1_end_96) || tom_equal_term_T(tomMatch1_end_96, tom_empty_list_p()))  { tomMatch1_end_96 = any(t).( T );  } else { tomMatch1_end_96 = func()  T  { if tom_is_fun_sym_p(tomMatch1_end_96) { return tom_get_tail_p_T(tomMatch1_end_96) }; return tom_empty_list_p() }();  };  };  if !(!(tom_equal_term_T(tomMatch1_end_96, any(t).( T )))) { break; }; };  };  };  }; { if tom_is_sort_T(t) { if tom_is_fun_sym_p(any(t).( T )) { var tomMatch1_end_122  T  = any(t).( T ); _ = tomMatch1_end_122; for { { if !( (tom_is_empty_p_T(tomMatch1_end_122) || tom_equal_term_T(tomMatch1_end_122, tom_empty_list_p())) ) { if tom_is_fun_sym_b0(any(func()  T  { if tom_is_fun_sym_p(tomMatch1_end_122) { return tom_get_head_p_T(tomMatch1_end_122) }; return tomMatch1_end_122 }()).( T )) { var tomMatch1_125  T  = tom_append_list_p(tom_get_slice_p(any(t).( T ),tomMatch1_end_122,tom_empty_list_p()),tom_append_list_p(func()  T  { if tom_is_fun_sym_p(tomMatch1_end_122) { return tom_get_tail_p_T(tomMatch1_end_122) }; return tom_empty_list_p() }(),tom_empty_list_p())); _ = tomMatch1_125; if tom_is_fun_sym_p(any(tomMatch1_125).( T )) { 



        return e(tomMatch1_125) };  };  }; if  (tom_is_empty_p_T(tomMatch1_end_122) || tom_equal_term_T(tomMatch1_end_122, tom_empty_list_p()))  { tomMatch1_end_122 = any(t).( T );  } else { tomMatch1_end_122 = func()  T  { if tom_is_fun_sym_p(tomMatch1_end_122) { return tom_get_tail_p_T(tomMatch1_end_122) }; return tom_empty_list_p() }();  };  };  if !(!(tom_equal_term_T(tomMatch1_end_122, any(t).( T )))) { break; }; };  };  };  };  }; 


    return t
}



func e_and(X T,Y T) T {
    var mX T= e(tom_cons_list_m(X,tom_cons_list_m(X,tom_empty_list_m())))
    var mY T= e(tom_cons_list_m(Y,tom_cons_list_m(Y,tom_empty_list_m())))
    var b2 T= e_b2()
    return e(tom_cons_list_p(e(tom_cons_list_m(mX,tom_cons_list_m(mY,tom_empty_list_m()))),tom_cons_list_p(e(tom_cons_list_m(b2,tom_cons_list_m(mX,tom_cons_list_m(Y,tom_empty_list_m())))),tom_cons_list_p(e(tom_cons_list_m(b2,tom_cons_list_m(mY,tom_cons_list_m(X,tom_empty_list_m())))),tom_cons_list_p(e(tom_cons_list_m(b2,tom_cons_list_m(X,tom_cons_list_m(Y,tom_empty_list_m())))),tom_empty_list_p())))))





}



func e_or(X T, Y T) T {
    var mX T= e(tom_cons_list_m(X,tom_cons_list_m(X,tom_empty_list_m())))
    var mY T= e(tom_cons_list_m(Y,tom_cons_list_m(Y,tom_empty_list_m())))
    var b2 T= e_b2()
    return e(tom_cons_list_p(e(tom_cons_list_m(b2,tom_cons_list_m(mX,tom_cons_list_m(mY,tom_empty_list_m())))),tom_cons_list_p(e(tom_cons_list_m(mX,tom_cons_list_m(Y,tom_empty_list_m()))),tom_cons_list_p(e(tom_cons_list_m(mY,tom_cons_list_m(X,tom_empty_list_m()))),tom_cons_list_p(e(tom_cons_list_m(X,tom_cons_list_m(Y,tom_empty_list_m()))),tom_cons_list_p(X,tom_cons_list_p(Y,tom_empty_list_p())))))))





}





func e_b2() T {
    return e(tom_cons_list_p(tom_make_b1(),tom_cons_list_p(tom_make_b1(),tom_empty_list_p()))) 
}



func e_not(x T) T {
    var b2 T= e_b2()
    return e(tom_cons_list_p(e(tom_cons_list_m(b2,tom_cons_list_m(x,tom_empty_list_m()))),tom_cons_list_p(tom_make_b1(),tom_empty_list_p())))


 
}







func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
