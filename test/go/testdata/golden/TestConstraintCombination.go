









package testconstraintcombination



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
    GetOperator() string
    Equals(o any) bool
    isTInt()
  }



type tIntClass struct{}



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
    GetOperator() string
    Equals(o any) bool
    isTNat()
  }



type tNatClass struct{}



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

  
  func tom_TestConstraintCombination_equal_term_tFloat(t1  tFloat , t2  tFloat ) bool { return (t1.Equals(t2));  }; func tom_TestConstraintCombination_is_sort_tFloat(t any) bool { return is[tFloat](t);  }; func tom_TestConstraintCombination_equal_term_tInt(t1  tInt , t2  tInt ) bool { return (t1.Equals(t2));  }; func tom_TestConstraintCombination_is_sort_tInt(t any) bool { return is[tInt](t);  }; func tom_TestConstraintCombination_equal_term_tNat(t1  tNat , t2  tNat ) bool { return (t1.Equals(t2));  }; func tom_TestConstraintCombination_is_sort_tNat(t any) bool { return is[tNat](t);  }; func tom_TestConstraintCombination_equal_term_natList(t1  natList , t2  natList ) bool { return (t1==t2);  }; func tom_TestConstraintCombination_is_sort_natList(t any) bool { return is[natList](t);  }; func tom_TestConstraintCombination_is_fun_sym_nList(t  natList ) bool { return is[*nList](t);  }; func tom_TestConstraintCombination_empty_list_nList()  natList  { return (newNList()) }; func tom_TestConstraintCombination_cons_list_nList(e  tNat , l  natList )  natList  { return (newNListCons(e,l)) }; func tom_TestConstraintCombination_get_head_nList_natList(l  natList )  tNat  { return l.(*nList).headnList;  }; func tom_TestConstraintCombination_get_tail_nList_natList(l  natList )  natList  { return l.(*nList).tailnList;  }; func tom_TestConstraintCombination_is_empty_nList_natList(l  natList ) bool { return l.(*nList).IsEmpty();  }; func tom_TestConstraintCombination_append_list_nList(l1  natList , l2  natList )  natList  { if tom_TestConstraintCombination_is_empty_nList_natList(l1) { return l2 } else if tom_TestConstraintCombination_is_empty_nList_natList(l2) { return l1 } else if tom_TestConstraintCombination_is_empty_nList_natList(tom_TestConstraintCombination_get_tail_nList_natList(l1)) { return tom_TestConstraintCombination_cons_list_nList(tom_TestConstraintCombination_get_head_nList_natList(l1), l2) } else { return tom_TestConstraintCombination_cons_list_nList(tom_TestConstraintCombination_get_head_nList_natList(l1), tom_TestConstraintCombination_append_list_nList(tom_TestConstraintCombination_get_tail_nList_natList(l1), l2)) }; }; func tom_TestConstraintCombination_get_slice_nList(begin  natList , end  natList , tail  natList )  natList  { if tom_TestConstraintCombination_equal_term_natList(begin, end) { return tail } else if tom_TestConstraintCombination_equal_term_natList(end, tail) && (tom_TestConstraintCombination_is_empty_nList_natList(end) || tom_TestConstraintCombination_equal_term_natList(end, tom_TestConstraintCombination_empty_list_nList())) { return begin }; return tom_TestConstraintCombination_cons_list_nList(tom_TestConstraintCombination_get_head_nList_natList(begin), tom_TestConstraintCombination_get_slice_nList(tom_TestConstraintCombination_get_tail_nList_natList(begin), end, tail)) }; func tom_TestConstraintCombination_make_zero()  tNat  { return (newZero()) }; func tom_TestConstraintCombination_make_suc(t0  tNat )  tNat  { return (newSuc(t0)) }; 





































































































  
func TestCombinations(t *testing.T) {
    var subject natList = tom_TestConstraintCombination_cons_list_nList(tom_TestConstraintCombination_make_zero(),tom_TestConstraintCombination_empty_list_nList())
    nbCasesOK := 0
    ; { { var tomMatch1_0  tNat  = tom_TestConstraintCombination_make_suc(tom_TestConstraintCombination_make_zero()); if tom_TestConstraintCombination_is_sort_tNat(tomMatch1_0) { var tom___x  tNat  = any(tomMatch1_0).( tNat ); if !(tom_TestConstraintCombination_equal_term_tNat(tom___x, tom_TestConstraintCombination_make_zero())) { 


        nbCasesOK++
        fmt.Println("Case #1: x = " + fmt.Sprint(tom___x))
      ;  };  }; var tomMatch1_1  tNat  = tom_TestConstraintCombination_make_suc(tom_TestConstraintCombination_make_suc(tom_TestConstraintCombination_make_zero())); if tom_TestConstraintCombination_is_sort_tNat(tomMatch1_1) { var tom___x  tNat  = any(tomMatch1_1).( tNat ); if !(tom_TestConstraintCombination_equal_term_tNat(tom___x, tom_TestConstraintCombination_make_zero())) {          nbCasesOK++;         fmt.Println("Case #1: x = " + fmt.Sprint(tom___x));       ;  };  };  };  }; 
















    _ = subject
    _ = nbCasesOK

  }
