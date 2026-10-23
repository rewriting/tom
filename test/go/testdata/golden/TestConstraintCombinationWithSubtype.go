








package testconstraintcombinationwithsubtype



import (
  "fmt"
  "testing"
)

  
  
  
























type tFloat interface {
    GetOperator() string
    Equals(o any) bool
    isTFloat()
  }



type tFloatClass struct{}



func (tFloatClass) isTFloat() {}



type div struct {
    tFloatClass
    n1 tInt
    n2 tInt
  }



func newDiv(nn1 tInt, nn2 tInt) *div {
      return &div{n1: nn1, n2: nn2}
    }



func (d *div) GetOperator() string {
      return "div"
    }



func (d *div) String() string {
      return "div(" + fmt.Sprint(d.n1) + "," + fmt.Sprint(d.n2) + ")"
    }



func (d *div) Equals(o any) bool {
      if obj, ok := o.(*div); ok {
        return d.n1.Equals(obj.n1) && d.n2.Equals(obj.n2)
      }
      return false
    }



type fmult struct {
    tFloatClass
    n1 tInt
    n2 tInt
  }



func newFmult(nn1 tInt, nn2 tInt) *fmult {
      return &fmult{n1: nn1, n2: nn2}
    }



func (f *fmult) GetOperator() string {
      return "fmult"
    }



func (f *fmult) String() string {
      return "fmult(" + fmt.Sprint(f.n1) + "," + fmt.Sprint(f.n2) + ")"
    }



func (f *fmult) Equals(o any) bool {
      if obj, ok := o.(*fmult); ok {
        return f.n1.Equals(obj.n1) && f.n2.Equals(obj.n2)
      }
      return false
    }



type tInt interface {
    tFloat
    isTInt()
  }



type tIntClass struct{ tFloatClass }



func (tIntClass) isTInt() {}



type uminus struct {
    tIntClass
    m tNat
  }



func newUminus(mm tNat) *uminus {
      return &uminus{m: mm}
    }



func (u *uminus) GetOperator() string {
      return "uminus"
    }



func (u *uminus) String() string {
      return "uminus(" + fmt.Sprint(u.m) + ")"
    }



func (u *uminus) Equals(o any) bool {
      if obj, ok := o.(*uminus); ok {
        return u.m.Equals(obj.m)
      }
      return false
    }



type plus struct {
    tIntClass
  }



var plusN1 tInt
var plusN2 tInt



func newPlus(nn1 tInt, nn2 tInt) *plus {
      plusN1 = nn1
      plusN2 = nn2
      return &plus{}
    }



func (p *plus) GetOperator() string {
      return "plus"
    }



func (p *plus) String() string {
      return "plus(" + fmt.Sprint(plusN1) + "," + fmt.Sprint(plusN2) + ")"
    }



func (p *plus) Equals(o any) bool {
      if _, ok := o.(*plus); ok {
        return plusN1.Equals(plusN1) && plusN2.Equals(plusN2)
      }
      return false
    }



type tNat interface {
    tInt
    isTNat()
  }



type tNatClass struct{ tIntClass }



func (tNatClass) isTNat() {}



type zero struct {
    tNatClass
  }



func newZero() *zero { return &zero{} }



func (z *zero) GetOperator() string {
      return "zero"
    }



func (z *zero) String() string {
      return "zero()"
    }



func (z *zero) Equals(o any) bool {
      if _, ok := o.(*zero); ok {
        return true
      }
      return false
    }



type one struct {
    tNatClass
  }



func newOne() *one { return &one{} }



func (z *one) GetOperator() string {
      return "one"
    }



func (z *one) String() string {
      return "one()"
    }



func (z *one) Equals(o any) bool {
      if _, ok := o.(*one); ok {
        return true
      }
      return false
    }



type two struct {
    tNatClass
  }



func newTwo() *two { return &two{} }



func (z *two) GetOperator() string {
      return "two"
    }



func (z *two) String() string {
      return "two()"
    }



func (z *two) Equals(o any) bool {
      if _, ok := o.(*two); ok {
        return true
      }
      return false
    }



type suc struct {
    tNatClass
    m tNat
  }



func newSuc(mm tNat) *suc {
      return &suc{m: mm}
    }



func (s *suc) GetOperator() string {
      return "suc"
    }



func (s *suc) String() string {
      return "suc(" + fmt.Sprint(s.m) + ")"
    }



func (s *suc) Equals(o any) bool {
      if obj, ok := o.(*suc); ok {
        return s.m.Equals(obj.m)
      }
      return false
    }



type square struct {
    tNatClass
    n tInt
  }



func newSquare(nn tInt) *square {
      return &square{n: nn}
    }



func (s *square) GetOperator() string {
      return "square"
    }



func (s *square) String() string {
      return "square(" + fmt.Sprint(s.n) + ")"
    }



func (s *square) Equals(o any) bool {
      if obj, ok := o.(*square); ok {
        return s.n.Equals(obj.n)
      }
      return false
    }



type mult struct {
    tNatClass
  }



var multM1 tNat
var multM2 tNat



func newMult(mm1 tNat, mm2 tNat) *mult {
      multM1 = mm1
      multM2 = mm2
      return &mult{}
    }



func (m *mult) GetOperator() string {
      return "mult"
    }



func (m *mult) String() string {
      return "mult(" + fmt.Sprint(multM1) + "," + fmt.Sprint(multM2) + ")"
    }



func (m *mult) Equals(o any) bool {
      if _, ok := o.(*mult); ok {
        return multM1.Equals(multM1) && multM2.Equals(multM2)
      }
      return false
    }



type natList interface {
    GetOperator() string
  }



type nList struct {
    headnList tNat
    tailnList natList
  }



func newNList() *nList {
      return &nList{headnList: nil, tailnList: nil}
    }



func newNListCons(headnnList tNat, tailnnList natList) *nList {
      return &nList{headnList: headnnList, tailnList: tailnnList}
    }



func (l *nList) IsEmpty() bool {
      return (l.headnList == nil && l.tailnList == nil)
    }



func (l *nList) GetOperator() string {
      return "nList"
    }



func (l *nList) String() string {
      result := "nList("
      if l.headnList != nil {
        result += fmt.Sprint(l.headnList)
      }
      if l.tailnList != nil {
        result += ("," + fmt.Sprint(l.tailnList))
      }
      result += ")"
      return result
    }




func is[T any](t any) bool { _, ok := t.(T); return ok }

  
  func tom_TestConstraintCombinationWithSubtype_equal_term_tFloat(t1  tFloat , t2  tFloat ) bool { return (t1.Equals(t2));  }; func tom_TestConstraintCombinationWithSubtype_is_sort_tFloat(t any) bool { return is[tFloat](t);  }; func tom_TestConstraintCombinationWithSubtype_equal_term_tInt(t1  tInt , t2  tInt ) bool { return (t1.Equals(t2));  }; func tom_TestConstraintCombinationWithSubtype_is_sort_tInt(t any) bool { return is[tInt](t);  }; func tom_TestConstraintCombinationWithSubtype_equal_term_tNat(t1  tNat , t2  tNat ) bool { return (t1.Equals(t2));  }; func tom_TestConstraintCombinationWithSubtype_is_sort_tNat(t any) bool { return is[tNat](t);  }; func tom_TestConstraintCombinationWithSubtype_equal_term_natList(t1  natList , t2  natList ) bool { return (t1==t2);  }; func tom_TestConstraintCombinationWithSubtype_is_sort_natList(t any) bool { return is[natList](t);  }; func tom_TestConstraintCombinationWithSubtype_is_fun_sym_nList(t  natList ) bool { return is[*nList](t);  }; func tom_TestConstraintCombinationWithSubtype_empty_list_nList()  natList  { return (newNList()) }; func tom_TestConstraintCombinationWithSubtype_cons_list_nList(e  tNat , l  natList )  natList  { return (newNListCons(e,l)) }; func tom_TestConstraintCombinationWithSubtype_get_head_nList_natList(l  natList )  tNat  { return l.(*nList).headnList;  }; func tom_TestConstraintCombinationWithSubtype_get_tail_nList_natList(l  natList )  natList  { return l.(*nList).tailnList;  }; func tom_TestConstraintCombinationWithSubtype_is_empty_nList_natList(l  natList ) bool { return l.(*nList).IsEmpty();  }; func tom_TestConstraintCombinationWithSubtype_append_list_nList(l1  natList , l2  natList )  natList  { if tom_TestConstraintCombinationWithSubtype_is_empty_nList_natList(l1) { return l2 } else if tom_TestConstraintCombinationWithSubtype_is_empty_nList_natList(l2) { return l1 } else if tom_TestConstraintCombinationWithSubtype_is_empty_nList_natList(tom_TestConstraintCombinationWithSubtype_get_tail_nList_natList(l1)) { return tom_TestConstraintCombinationWithSubtype_cons_list_nList(tom_TestConstraintCombinationWithSubtype_get_head_nList_natList(l1), l2) } else { return tom_TestConstraintCombinationWithSubtype_cons_list_nList(tom_TestConstraintCombinationWithSubtype_get_head_nList_natList(l1), tom_TestConstraintCombinationWithSubtype_append_list_nList(tom_TestConstraintCombinationWithSubtype_get_tail_nList_natList(l1), l2)) }; }; func tom_TestConstraintCombinationWithSubtype_get_slice_nList(begin  natList , end  natList , tail  natList )  natList  { if tom_TestConstraintCombinationWithSubtype_equal_term_natList(begin, end) { return tail } else if tom_TestConstraintCombinationWithSubtype_equal_term_natList(end, tail) && (tom_TestConstraintCombinationWithSubtype_is_empty_nList_natList(end) || tom_TestConstraintCombinationWithSubtype_equal_term_natList(end, tom_TestConstraintCombinationWithSubtype_empty_list_nList())) { return begin }; return tom_TestConstraintCombinationWithSubtype_cons_list_nList(tom_TestConstraintCombinationWithSubtype_get_head_nList_natList(begin), tom_TestConstraintCombinationWithSubtype_get_slice_nList(tom_TestConstraintCombinationWithSubtype_get_tail_nList_natList(begin), end, tail)) }; func tom_TestConstraintCombinationWithSubtype_is_fun_sym_zero(t  tNat ) bool { return is[*zero](t);  }; func tom_TestConstraintCombinationWithSubtype_make_zero()  tNat  { return (newZero()) }; func tom_TestConstraintCombinationWithSubtype_make_one()  tNat  { return (newOne()) }; func tom_TestConstraintCombinationWithSubtype_is_fun_sym_suc(t  tNat ) bool { return is[*suc](t);  }; func tom_TestConstraintCombinationWithSubtype_make_suc(t0  tNat )  tNat  { return (newSuc(t0)) }; func tom_TestConstraintCombinationWithSubtype_get_slot_suc_m(t  tNat )  tNat  { return t.(*suc).m;  }; func tom_TestConstraintCombinationWithSubtype_is_fun_sym_div(t  tFloat ) bool { return is[*div](t);  }; func tom_TestConstraintCombinationWithSubtype_make_div(t0  tInt , t1  tInt )  tFloat  { return (newDiv(t0, t1)) }; func tom_TestConstraintCombinationWithSubtype_get_slot_div_n1(t  tFloat )  tInt  { return t.(*div).n1;  }; func tom_TestConstraintCombinationWithSubtype_get_slot_div_n2(t  tFloat )  tInt  { return t.(*div).n2;  }; func tom_TestConstraintCombinationWithSubtype_is_fun_sym_fmult(t  tFloat ) bool { return is[*div](t);  }; func tom_TestConstraintCombinationWithSubtype_get_slot_fmult_n1(t  tFloat )  tInt  { return t.(*fmult).n1;  }; func tom_TestConstraintCombinationWithSubtype_get_slot_fmult_n2(t  tFloat )  tInt  { return t.(*fmult).n2;  }; 





































































































  
func TestCombinations(t *testing.T) {
    var subject natList = tom_TestConstraintCombinationWithSubtype_cons_list_nList(tom_TestConstraintCombinationWithSubtype_make_zero(),tom_TestConstraintCombinationWithSubtype_empty_list_nList())
    nbCasesOK := 0
    ; { { var tomMatch1_0  tNat  = tom_TestConstraintCombinationWithSubtype_make_suc(tom_TestConstraintCombinationWithSubtype_make_zero()); _ = tomMatch1_0; if tom_TestConstraintCombinationWithSubtype_is_sort_tNat(tomMatch1_0) { if !(tom_TestConstraintCombinationWithSubtype_equal_term_tNat(any(tomMatch1_0).( tNat ), tom_TestConstraintCombinationWithSubtype_make_zero())) { 


        nbCasesOK++
      ;  };  }; var tomMatch1_1  tNat  = tom_TestConstraintCombinationWithSubtype_make_suc(tom_TestConstraintCombinationWithSubtype_make_suc(tom_TestConstraintCombinationWithSubtype_make_zero())); _ = tomMatch1_1; if tom_TestConstraintCombinationWithSubtype_is_sort_tNat(tomMatch1_1) { if !(tom_TestConstraintCombinationWithSubtype_equal_term_tNat(any(tomMatch1_1).( tNat ), tom_TestConstraintCombinationWithSubtype_make_zero())) {          nbCasesOK++;       ;  };  };  }; { var tomMatch1_3  tNat  = tom_TestConstraintCombinationWithSubtype_make_suc(tom_TestConstraintCombinationWithSubtype_make_zero()); _ = tomMatch1_3; if tom_TestConstraintCombinationWithSubtype_is_sort_tNat(tomMatch1_3) { if tom_TestConstraintCombinationWithSubtype_is_sort_tNat(any(tomMatch1_3).( tNat )) { if tom_TestConstraintCombinationWithSubtype_is_fun_sym_suc(any(tomMatch1_3).( tNat )) { if tom_TestConstraintCombinationWithSubtype_is_fun_sym_zero(any(tom_TestConstraintCombinationWithSubtype_get_slot_suc_m(any(tomMatch1_3).( tNat ))).( tNat )) { 



        nbCasesOK++
      ;  };  };  };  }; var tomMatch1_4  tNat  = tom_TestConstraintCombinationWithSubtype_make_suc(tom_TestConstraintCombinationWithSubtype_make_suc(tom_TestConstraintCombinationWithSubtype_make_zero())); _ = tomMatch1_4; if tom_TestConstraintCombinationWithSubtype_is_sort_tNat(tomMatch1_4) { if tom_TestConstraintCombinationWithSubtype_is_sort_tNat(any(tomMatch1_4).( tNat )) { if tom_TestConstraintCombinationWithSubtype_is_fun_sym_suc(any(tomMatch1_4).( tNat )) { if tom_TestConstraintCombinationWithSubtype_is_fun_sym_zero(any(tom_TestConstraintCombinationWithSubtype_get_slot_suc_m(any(tomMatch1_4).( tNat ))).( tNat )) {          nbCasesOK++;       ;  };  };  };  };  }; { var tomMatch1_11  tFloat  = tom_TestConstraintCombinationWithSubtype_make_div(tom_TestConstraintCombinationWithSubtype_make_zero(),tom_TestConstraintCombinationWithSubtype_make_one()); _ = tomMatch1_11; if tom_TestConstraintCombinationWithSubtype_is_sort_tFloat(tomMatch1_11) { var tomMatch1_18 bool =  false ; _ = tomMatch1_18; var tomMatch1_17  tFloat  =  *new(tFloat) ; _ = tomMatch1_17; var tomMatch1_14  tInt  =  *new(tInt) ; _ = tomMatch1_14; var tomMatch1_16  tFloat  =  *new(tFloat) ; _ = tomMatch1_16; if tom_TestConstraintCombinationWithSubtype_is_fun_sym_div(any(tomMatch1_11).( tFloat )) { { tomMatch1_18 =  true ; tomMatch1_16 = any(tomMatch1_11).( tFloat ); tomMatch1_14 = tom_TestConstraintCombinationWithSubtype_get_slot_div_n1(tomMatch1_16);  };  } else { if tom_TestConstraintCombinationWithSubtype_is_fun_sym_fmult(any(tomMatch1_11).( tFloat )) { { tomMatch1_18 =  true ; tomMatch1_17 = any(tomMatch1_11).( tFloat ); tomMatch1_14 = tom_TestConstraintCombinationWithSubtype_get_slot_fmult_n1(tomMatch1_17);  };  };  }; if tomMatch1_18 { if tom_TestConstraintCombinationWithSubtype_is_sort_tNat(tomMatch1_14) { if tom_TestConstraintCombinationWithSubtype_is_sort_tNat(any(tomMatch1_14).( tInt )) { if tom_TestConstraintCombinationWithSubtype_is_fun_sym_zero(any(any(tomMatch1_14).( tInt )).( tNat )) { 



        nbCasesOK++
      ;  };  };  };  };  };  }; { var tomMatch1_19  tFloat  = tom_TestConstraintCombinationWithSubtype_make_div(tom_TestConstraintCombinationWithSubtype_make_zero(),tom_TestConstraintCombinationWithSubtype_make_one()); _ = tomMatch1_19; if tom_TestConstraintCombinationWithSubtype_is_sort_tFloat(tomMatch1_19) { var tomMatch1_24 bool =  false ; _ = tomMatch1_24; var tomMatch1_23  tFloat  =  *new(tFloat) ; _ = tomMatch1_23; var tomMatch1_22  tFloat  =  *new(tFloat) ; _ = tomMatch1_22; var tomMatch1_20  tInt  =  *new(tInt) ; _ = tomMatch1_20; if tom_TestConstraintCombinationWithSubtype_is_fun_sym_div(any(tomMatch1_19).( tFloat )) { { tomMatch1_24 =  true ; tomMatch1_22 = any(tomMatch1_19).( tFloat ); tomMatch1_20 = tom_TestConstraintCombinationWithSubtype_get_slot_div_n1(tomMatch1_22);  };  } else { if tom_TestConstraintCombinationWithSubtype_is_fun_sym_fmult(any(tomMatch1_19).( tFloat )) { { tomMatch1_24 =  true ; tomMatch1_23 = any(tomMatch1_19).( tFloat ); tomMatch1_20 = tom_TestConstraintCombinationWithSubtype_get_slot_fmult_n1(tomMatch1_23);  };  };  }; if tomMatch1_24 { if func() bool { tom_a, tom_ok1 := any(tomMatch1_20).( tNat ); tom_b, tom_ok2 := any(tom_TestConstraintCombinationWithSubtype_make_zero()).( tNat ); return tom_ok1 && tom_ok2 && tom_TestConstraintCombinationWithSubtype_equal_term_tNat(tom_a, tom_b) }() { 



        nbCasesOK++
      ;  };  };  };  };  }; 

    _ = subject
    assertTrue(t, "testCombinations",nbCasesOK==5)
  }




func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Error(message)
  }
}
