package acmatching
















import (
  "testing"
)

  
func tom_TestAC_next_minimal_extract(multiplicity int64, total int64, E []int64, sol []int64) bool {
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
func tom_TestAC___getTermForMult_f(tempSol []int64, alpha []int64, subject  T , isComplement bool)  T  { var result  T  = tom_TestAC_empty_list_f(); _ = result; var old  T  =  *new(T) ; _ = old; var elem  T  =  *new(T) ; _ = elem; var elemCounter int64 = 0; _ = elemCounter; var tempSolIndex int64 = -1; _ = tempSolIndex; { for !(tom_TestAC_equal_term_T(tom_TestAC_empty_list_f(), subject)) { { if tom_TestAC_is_fun_sym_f(subject) { elem = tom_TestAC_get_head_f_T(subject); subject = tom_TestAC_get_tail_f_T(subject);  } else { elem = subject; subject = tom_TestAC_empty_list_f();  }; if !(tom_TestAC_equal_term_T(elem, old)) { tempSolIndex = tempSolIndex + 1; old = elem; elemCounter = 0;  }; var tempSolVal int64 = tom_TestAC_get_element_concInt_intarray(tempSol,tempSolIndex); _ = tempSolVal; { if isComplement { tempSolVal = tom_TestAC_get_element_concInt_intarray(alpha,tempSolIndex) - tempSolVal;  }; if  (!(tom_TestAC_equal_term_int(tempSolVal, 0)) && elemCounter < tempSolVal)  { result = tom_TestAC_append_list_f(result,elem); elemCounter = elemCounter + 1;  };  };  };  }; return result;  };  }; func tom_TestAC___getMultiplicities_f(subject  T ) []int64 { var tom_length_0 int64 = tom_TestAC___computeLength_f(subject); _ = tom_length_0; var tom_mult_1 []int64 = tom_TestAC_empty_array_concInt(tom_length_0); _ = tom_mult_1; var oldElem  T  =  *new(T) ; _ = oldElem; { if tom_TestAC_equal_term_T(subject, tom_TestAC_empty_list_f()) { return tom_mult_1;  } else { if tom_TestAC_is_fun_sym_f(subject) { oldElem = tom_TestAC_get_head_f_T(subject);  } else { tom_mult_1[0] = 1; return tom_mult_1;  };  }; var counter int64 = 0; _ = counter; for  (tom_TestAC_is_fun_sym_f(subject) && !(tom_TestAC_equal_term_T(subject, tom_TestAC_empty_list_f())))  { { var elem  T  = tom_TestAC_get_head_f_T(subject); _ = elem; if tom_TestAC_equal_term_T(elem, oldElem) { tom_mult_1[counter] = tom_TestAC_get_element_concInt_intarray(tom_mult_1,counter) + 1;  } else { counter = counter + 1; oldElem = elem; tom_mult_1[counter] = 1;  }; subject = tom_TestAC_get_tail_f_T(subject); if !(tom_TestAC_is_fun_sym_f(subject)) { if tom_TestAC_equal_term_T(subject, oldElem) { tom_mult_1[counter] = tom_TestAC_get_element_concInt_intarray(tom_mult_1,counter) + 1;  } else { counter = counter + 1; tom_mult_1[counter] = 1;  };  };  };  }; return tom_mult_1;  };  }; func tom_TestAC___computeLength_f(subject  T ) int64 { { if !(tom_TestAC_is_fun_sym_f(subject)) { return 1;  }; var old  T  =  *new(T) ; _ = old; var counter int64 = 0; _ = counter; { for  (tom_TestAC_is_fun_sym_f(subject) && !(tom_TestAC_equal_term_T(subject, tom_TestAC_empty_list_f())))  { { var elem  T  = tom_TestAC_get_head_f_T(subject); _ = elem; if !(tom_TestAC_equal_term_T(elem, old)) { { counter = counter + 1; old = elem;  };  }; subject = tom_TestAC_get_tail_f_T(subject); if !(tom_TestAC_is_fun_sym_f(subject)) { if !(tom_TestAC_equal_term_T(subject, old)) { counter = counter + 1;  };  };  };  }; return counter;  };  };  }; func tom_TestAC_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_TestAC_is_sort_int(t int64) bool { return true;  }; func tom_TestAC_equal_term_intarray(t1  []int64 , t2  []int64 ) bool { return func(t1, t2 []int64) bool { if len(t1) != len(t2) { return false }; for i := range t1 { if t1[i] != t2[i] { return false } }; return true }(t1, t2);  }; func tom_TestAC_is_sort_intarray(t any) bool { return t != nil;  }; func tom_TestAC_is_fun_sym_concInt(l  []int64 ) bool { return l != nil;  }; func tom_TestAC_empty_array_concInt(n int64)  []int64  { return (make([]int64, n)) }; func tom_TestAC_cons_array_concInt(o  int64 , l  []int64 )  []int64  { return (func(o int64, array []int64) []int64 { tmp := make([]int64, len(array)+1); copy(tmp, array); tmp[len(array)] = o; return tmp }(o,l)) }; func tom_TestAC_get_element_concInt_intarray(l  []int64 , n int64)  int64  { return l[n];  }; func tom_TestAC_get_size_concInt_intarray(l  []int64 ) int64 { return int64(len(l));  }; func tom_TestAC_get_slice_concInt(subject  []int64 , begin int64, end int64)  []int64  { var result  []int64  = tom_TestAC_empty_array_concInt(end-begin); for begin != end { result = tom_TestAC_cons_array_concInt(tom_TestAC_get_element_concInt_intarray(subject, begin), result); begin++ }; return result }; func tom_TestAC_append_array_concInt(l2  []int64 , l1  []int64 )  []int64  { var size1 int64 = tom_TestAC_get_size_concInt_intarray(l1); var size2 int64 = tom_TestAC_get_size_concInt_intarray(l2); var index int64; var result  []int64  = tom_TestAC_empty_array_concInt(size1+size2); index = size1; for index > 0 { result = tom_TestAC_cons_array_concInt(tom_TestAC_get_element_concInt_intarray(l1, size1-index), result); index-- }; index = size2; for index > 0 { result = tom_TestAC_cons_array_concInt(tom_TestAC_get_element_concInt_intarray(l2, size2-index), result); index-- }; return result }; func tom_TestAC_equal_term_T(t1  T , t2  T ) bool { return (t1).Equivalent(t2);  }; func tom_TestAC_is_sort_T(t any) bool { return func() bool { _, ok := (t).(T); return ok }();  }; func tom_TestAC_is_fun_sym_a(t  T ) bool { return func() bool { _, ok := (t).(*AT); return ok }();  }; func tom_TestAC_make_a()  T  { return (MakeA()) }; func tom_TestAC_make_b()  T  { return (MakeB()) }; func tom_TestAC_make_c()  T  { return (MakeC()) }; func tom_TestAC_is_fun_sym_f(t  T ) bool { return func() bool { _, ok := (t).(*FT); return ok }();  }; func tom_TestAC_empty_list_f()  T  { return (EmptyF()) }; func tom_TestAC_cons_list_f(e  T , l  T )  T  { return (ConsF(e,l)) }; func tom_TestAC_get_head_f_T(l  T )  T  { return (l).GetHeadf();  }; func tom_TestAC_get_tail_f_T(l  T )  T  { return (l).GetTailf();  }; func tom_TestAC_is_empty_f_T(l  T ) bool { return (l).IsEmptyf();  }; func tom_TestAC_append_list_f(l1  T , l2  T )  T  { if tom_TestAC_is_empty_f_T(l1) { return l2 } else if tom_TestAC_is_empty_f_T(l2) { return l1 } else if tom_TestAC_is_fun_sym_f(l1) { if tom_TestAC_is_empty_f_T(tom_TestAC_get_tail_f_T(l1)) { return tom_TestAC_cons_list_f(tom_TestAC_get_head_f_T(l1), l2) } else { return tom_TestAC_cons_list_f(tom_TestAC_get_head_f_T(l1), tom_TestAC_append_list_f(tom_TestAC_get_tail_f_T(l1), l2)) } } else { return tom_TestAC_cons_list_f(l1, l2) }; }; func tom_TestAC_get_slice_f(begin  T , end  T , tail  T )  T  { if tom_TestAC_equal_term_T(begin, end) { return tail } else if tom_TestAC_equal_term_T(end, tail) && (tom_TestAC_is_empty_f_T(end) || tom_TestAC_equal_term_T(end, tom_TestAC_empty_list_f())) { return begin }; return tom_TestAC_cons_list_f(func()  T  { if tom_TestAC_is_fun_sym_f(begin) { return tom_TestAC_get_head_f_T(begin) }; return begin }(), tom_TestAC_get_slice_f(func()  T  { if tom_TestAC_is_fun_sym_f(begin) { return tom_TestAC_get_tail_f_T(begin) }; return tom_TestAC_empty_list_f() }(), end, tail)) }; 


















func Test1(t *testing.T) {
    var t1 T = tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_cons_list_f(tom_TestAC_make_b(),tom_TestAC_cons_list_f(tom_TestAC_make_c(),tom_TestAC_empty_list_f())))
    bagX := map[T]bool{}
    bagY := map[T]bool{}

    ; { _ = t1; { if tom_TestAC_is_sort_T(t1) { if tom_TestAC_is_fun_sym_f(any(t1).( T )) { var tomMatch1_end_7  T  = any(t1).( T ); _ = tomMatch1_end_7; for { { var tomMatch1_1  T  = tom_TestAC_get_slice_f(any(t1).( T ),tomMatch1_end_7,tom_TestAC_empty_list_f()); _ = tomMatch1_1; if !( (tom_TestAC_is_empty_f_T(tomMatch1_end_7) || tom_TestAC_equal_term_T(tomMatch1_end_7, tom_TestAC_empty_list_f())) ) { if tom_TestAC_is_fun_sym_a(any(func()  T  { if tom_TestAC_is_fun_sym_f(tomMatch1_end_7) { return tom_TestAC_get_head_f_T(tomMatch1_end_7) }; return tomMatch1_end_7 }()).( T )) { var tomMatch1_8  T  = func()  T  { if tom_TestAC_is_fun_sym_f(tomMatch1_end_7) { return tom_TestAC_get_tail_f_T(tomMatch1_end_7) }; return tom_TestAC_empty_list_f() }(); _ = tomMatch1_8; var tomMatch1_alpha_12 []int64 = tom_TestAC___getMultiplicities_f(tom_TestAC_append_list_f(tomMatch1_1,tom_TestAC_append_list_f(tomMatch1_8,tom_TestAC_empty_list_f()))); _ = tomMatch1_alpha_12; var tomMatch1_length_15 int64 = tom_TestAC_get_size_concInt_intarray(tomMatch1_alpha_12); _ = tomMatch1_length_15; var tomMatch1_tempSol_13 []int64 = tom_TestAC_empty_array_concInt(tomMatch1_length_15); _ = tomMatch1_tempSol_13; for { 

        bagX[tom_TestAC___getTermForMult_f(tomMatch1_tempSol_13,tomMatch1_alpha_12,tom_TestAC_append_list_f(tomMatch1_1,tom_TestAC_append_list_f(tomMatch1_8,tom_TestAC_empty_list_f())), false )] = true
        bagY[tom_TestAC___getTermForMult_f(tomMatch1_tempSol_13,tomMatch1_alpha_12,tom_TestAC_append_list_f(tomMatch1_1,tom_TestAC_append_list_f(tomMatch1_8,tom_TestAC_empty_list_f())), true )] = true
      ;  if !(tom_TestAC_next_minimal_extract(1,tomMatch1_length_15,tomMatch1_alpha_12,tomMatch1_tempSol_13)) { break; }; };  };  }; if  (tom_TestAC_is_empty_f_T(tomMatch1_end_7) || tom_TestAC_equal_term_T(tomMatch1_end_7, tom_TestAC_empty_list_f()))  { tomMatch1_end_7 = any(t1).( T );  } else { tomMatch1_end_7 = func()  T  { if tom_TestAC_is_fun_sym_f(tomMatch1_end_7) { return tom_TestAC_get_tail_f_T(tomMatch1_end_7) }; return tom_TestAC_empty_list_f() }();  };  };  if !(!(tom_TestAC_equal_term_T(tomMatch1_end_7, any(t1).( T )))) { break; }; };  };  };  };  }; 


    assertThat(t, "f() is solution", bagX, hasItem(tom_TestAC_empty_list_f()))
    assertThat(t, "b() is solution", bagX, hasItem(tom_TestAC_make_b()))
    assertThat(t, "c() is solution", bagX, hasItem(tom_TestAC_make_c()))
    assertThat(t, "f(b(),c()) is solution", bagX, hasItem(tom_TestAC_cons_list_f(tom_TestAC_make_b(),tom_TestAC_cons_list_f(tom_TestAC_make_c(),tom_TestAC_empty_list_f()))))
    assertThat(t, "f(c(),b()) is solution", bagX, hasItem(tom_TestAC_cons_list_f(tom_TestAC_make_c(),tom_TestAC_cons_list_f(tom_TestAC_make_b(),tom_TestAC_empty_list_f()))))
    assertEqualSets(t, "bagX==bagY", bagX, bagY)
    assertEquals(t, "bagX.size==4", 4, len(bagX))
}



func Test2(t *testing.T) {
      var t1 T = tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_cons_list_f(tom_TestAC_make_b(),tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_empty_list_f())))
      bagX := map[T]bool{}
      bagY := map[T]bool{}

      ; { _ = t1; { if tom_TestAC_is_sort_T(t1) { var tomMatch2_alpha_2 []int64 = tom_TestAC___getMultiplicities_f(any(t1).( T )); _ = tomMatch2_alpha_2; var tomMatch2_length_5 int64 = tom_TestAC_get_size_concInt_intarray(tomMatch2_alpha_2); _ = tomMatch2_length_5; var tomMatch2_tempSol_3 []int64 = tom_TestAC_empty_array_concInt(tomMatch2_length_5); _ = tomMatch2_tempSol_3; for { 

          bagX[tom_TestAC___getTermForMult_f(tomMatch2_tempSol_3,tomMatch2_alpha_2,any(t1).( T ), false )] = true
          bagY[tom_TestAC___getTermForMult_f(tomMatch2_tempSol_3,tomMatch2_alpha_2,any(t1).( T ), true )] = true
        ;  if !(tom_TestAC_next_minimal_extract(2,tomMatch2_length_5,tomMatch2_alpha_2,tomMatch2_tempSol_3)) { break; }; };  };  };  }; 


      assertThat(t, "f() is solution of X", bagX, hasItem(tom_TestAC_empty_list_f()))
      assertThat(t, "f(a(),a()) is solution of X", bagX, hasItem(tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_empty_list_f()))))
      assertThat(t, "t1 is solution ofYX", bagY, hasItem(t1))
      assertThat(t, "b() is solution of Y", bagY, hasItem(tom_TestAC_make_b()))
      assertEquals(t, "bagX.size==2", 2, len(bagX))
      assertEquals(t, "bagY.size==2", 2, len(bagY))

      bagXX := map[T]bool{}
      bagYY := map[T]bool{}
      ; { _ = t1; { if tom_TestAC_is_sort_T(t1) { var tomMatch3_alpha_2 []int64 = tom_TestAC___getMultiplicities_f(any(t1).( T )); _ = tomMatch3_alpha_2; var tomMatch3_length_5 int64 = tom_TestAC_get_size_concInt_intarray(tomMatch3_alpha_2); _ = tomMatch3_length_5; var tomMatch3_tempSol_3 []int64 = tom_TestAC_empty_array_concInt(tomMatch3_length_5); _ = tomMatch3_tempSol_3; for { 

          bagXX[tom_TestAC___getTermForMult_f(tomMatch3_tempSol_3,tomMatch3_alpha_2,any(t1).( T ), true )] = true
          bagYY[tom_TestAC___getTermForMult_f(tomMatch3_tempSol_3,tomMatch3_alpha_2,any(t1).( T ), false )] = true
        ;  if !(tom_TestAC_next_minimal_extract(2,tomMatch3_length_5,tomMatch3_alpha_2,tomMatch3_tempSol_3)) { break; }; };  };  };  }; 

      assertEqualSets(t, "bagX==bagYY",bagX,bagYY)
      assertEqualSets(t, "bagY==bagXX",bagY,bagXX)



}



func Test3(t *testing.T) {
      var t1 T = tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_cons_list_f(tom_TestAC_make_b(),tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_empty_list_f())))
      bagX := map[T]bool{}
      bagY := map[T]bool{}

      ; { _ = t1; { if tom_TestAC_is_sort_T(t1) { var tomMatch4_alpha_13 []int64 = tom_TestAC___getMultiplicities_f(any(t1).( T )); _ = tomMatch4_alpha_13; var tomMatch4_length_16 int64 = tom_TestAC_get_size_concInt_intarray(tomMatch4_alpha_13); _ = tomMatch4_length_16; var tomMatch4_tempSol_14 []int64 = tom_TestAC_empty_array_concInt(tomMatch4_length_16); _ = tomMatch4_tempSol_14; for { var tomMatch4_1  T  = tom_TestAC___getTermForMult_f(tomMatch4_tempSol_14,tomMatch4_alpha_13,any(t1).( T ), true ); _ = tomMatch4_1; var tomMatch4_alpha_8 []int64 = tom_TestAC___getMultiplicities_f(tomMatch4_1); _ = tomMatch4_alpha_8; var tomMatch4_length_11 int64 = tom_TestAC_get_size_concInt_intarray(tomMatch4_alpha_8); _ = tomMatch4_length_11; var tomMatch4_tempSol_9 []int64 = tom_TestAC_empty_array_concInt(tomMatch4_length_11); _ = tomMatch4_tempSol_9; for { 

          bagX[tom_TestAC___getTermForMult_f(tomMatch4_tempSol_14,tomMatch4_alpha_13,any(t1).( T ), false )] = true
          bagY[tom_TestAC___getTermForMult_f(tomMatch4_tempSol_9,tomMatch4_alpha_8,tomMatch4_1, false )] = true
        ;  if !(tom_TestAC_next_minimal_extract(2,tomMatch4_length_11,tomMatch4_alpha_8,tomMatch4_tempSol_9)) { break; }; };  if !(tom_TestAC_next_minimal_extract(2,tomMatch4_length_16,tomMatch4_alpha_13,tomMatch4_tempSol_14)) { break; }; };  };  };  }; 


      assertThat(t, "f() is solution of X", bagX, hasItem(tom_TestAC_empty_list_f()))
      assertThat(t, "f() is solution of Y", bagY, hasItem(tom_TestAC_empty_list_f()))
      assertThat(t, "f(a(),a()) is solution of X", bagX, hasItem(tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_empty_list_f()))))
      assertThat(t, "f(a(),a()) is solution of Y", bagY, hasItem(tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_empty_list_f()))))
      assertEquals(t, "bagX.size==2", 2, len(bagX))
      assertEquals(t, "bagY.size==2", 2, len(bagY))
}



func Test4(t *testing.T) {
    var t1 T = tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_cons_list_f(tom_TestAC_make_b(),tom_TestAC_cons_list_f(tom_TestAC_make_b(),tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_empty_list_f()))))
    bagX := map[T]bool{}

    ; { _ = t1; { if tom_TestAC_is_sort_T(t1) { var tomMatch5_alpha_1 []int64 = tom_TestAC___getMultiplicities_f(any(t1).( T )); _ = tomMatch5_alpha_1; var tomMatch5_length_4 int64 = tom_TestAC_get_size_concInt_intarray(tomMatch5_alpha_1); _ = tomMatch5_length_4; var tomMatch5_tempSol_2 []int64 = tom_TestAC_empty_array_concInt(tomMatch5_length_4); _ = tomMatch5_tempSol_2; for { var tom___X  T  = tom_TestAC___getTermForMult_f(tomMatch5_tempSol_2,tomMatch5_alpha_1,any(t1).( T ), false ); _ = tom___X; if tom_TestAC_equal_term_T(tom_TestAC___getTermForMult_f(tomMatch5_tempSol_2,tomMatch5_alpha_1,any(t1).( T ), true ), tom___X) { 

        bagX[tom___X] = true
      ;  };  if !(tom_TestAC_next_minimal_extract(1,tomMatch5_length_4,tomMatch5_alpha_1,tomMatch5_tempSol_2)) { break; }; };  };  };  }; 


    assertThat(t, "f(a(),b()) is solution of X", bagX, hasItem(tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_cons_list_f(tom_TestAC_make_b(),tom_TestAC_empty_list_f()))))
    assertThat(t, "f(b(),a()) is solution of X", bagX, hasItem(tom_TestAC_cons_list_f(tom_TestAC_make_b(),tom_TestAC_cons_list_f(tom_TestAC_make_a(),tom_TestAC_empty_list_f()))))
    assertEquals(t, "bagX.size==1", 1, len(bagX))
}




func hasItem(item T) func(map[T]bool) bool {
  return func(bag map[T]bool) bool { return bag[item] }
}




func assertThat(t *testing.T, reason string, actual map[T]bool, matcher func(map[T]bool) bool) {
  t.Helper()
  if !matcher(actual) {
    t.Fatalf("%s: got %v", reason, actual)
  }
}




func assertEqualSets(t *testing.T, message string, expected, actual map[T]bool) {
  t.Helper()
  equal := len(expected) == len(actual)
  for x := range expected {
    equal = equal && actual[x]
  }
  if !equal {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}




func assertEquals(t *testing.T, message string, expected, actual int) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
