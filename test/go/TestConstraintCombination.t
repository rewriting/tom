// Port of test/TestConstraintCombination.t.
// port: the Java classes are Go types: tFloat, tInt, tNat and natList are
// interfaces (getOperator, equals and a marker method per class), each
// concrete class a struct whose methods are Java's; the abstract classes are unrelated, as in Java.
// port: `instanceof C` is is[*C] (a type assertion), `new C(…)` newC(…),
// toString is String, the static fields of plus and mult are package
// variables (plusN1, plusN2, multM1, multM2).
// port: no main — `go test` runs the tests (the original main only launched JUnit).
// port: `_ = subject` and `_ = nbCasesOK`, as Go rejects an unused local
// variable (the original's assertion on nbCasesOK is commented out).
package testconstraintcombination

import (
  "fmt"
  "testing"
)

  /*
  %gom {
    module Example
      abstract syntax
      tNat = zero()
          | one()
          | two()
          | suc(m:tNat)
          | square(n:tInt)
          | mult(m1:tNat,m2:tNat)

      natList = nList(tNat*)

      tInt = uminus(m:tNat)
          | plus(n1:tInt,n2:tInt)

      tFloat = div(n1:tInt,n2:tInt)
             | fmult(n1:tInt,n2:tInt)  }
  */
  
  //-------- Java classes -----------
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

// is is `t instanceof T`.
func is[T any](t any) bool { _, ok := t.(T); return ok }

  //-------- Tom mappings -----------
  %typeterm tFloat {
    implement { tFloat }
    is_sort(t) { is[tFloat]($t) }

    equals(t1,t2) { ($t1.Equals($t2)) }

  }

  %typeterm tInt {
    implement { tInt }
    is_sort(t) { is[tInt]($t) }

    equals(t1,t2) { ($t1.Equals($t2)) }

  }

  %typeterm tNat {
    implement { tNat }
    is_sort(t) { is[tNat]($t) }

    equals(t1,t2) { ($t1.Equals($t2)) }

  }

  %typeterm natList {
    implement { natList }
    is_sort(t) { is[natList]($t) }

    equals(t1,t2) { ($t1==$t2) }

  }

  %oplist natList nList( tNat* ) {
    is_fsym(t)         { is[*nList]($t) }
    get_head(l)        { $l.(*nList).headnList }
    get_tail(l)        { $l.(*nList).tailnList }
    is_empty(l)        { $l.(*nList).IsEmpty() }
    make_empty()       { newNList() }
    make_insert(e,l)   { newNListCons($e,$l) }
  }

  %op tNat zero() {
    is_fsym(t) { is[*zero]($t) }
    make() { newZero() }
  }

  %op tNat one() {
    is_fsym(t) { is[*one]($t) }
    make() { newOne() }
  }

  %op tNat two() {
    is_fsym(t) { is[*two]($t) }
    make() { newTwo() }
  }

  %op tNat suc(m:tNat) {
    is_fsym(t) { is[*suc]($t) }
    get_slot(m, t) { $t.(*suc).m }
    make(t0) { newSuc($t0) }
  }

  %op tNat square(n:tInt) {
    is_fsym(t) { is[*square]($t) }
    get_slot(n, t) { $t.(*square).n }
    make(t0) {  newSquare($t0) }
  }

  %op tNat mult(m1:tNat, m2:tNat) {
    is_fsym(t) { is[*mult]($t) }
    get_slot(m1, t) { multM1 }
    get_slot(m2, t) { multM2 }
    make(t0, t1) { newMult($t0, $t1) }
  }

  %op tInt uminus(m:tNat) {
    is_fsym(t) { is[*uminus]($t) }
    get_slot(m, t) { $t.(*uminus).m }
    make(t0) { newUminus($t0) }
  }

  %op tInt plus(n1:tInt, n2:tInt) {
    is_fsym(t) { is[*plus]($t) }
    get_slot(n1, t) { plusN1 }
    get_slot(n2, t) { plusN2 }
    make(t0, t1) { newPlus($t0, $t1) }
  }

  %op tFloat div(n1:tInt, n2:tInt) {
    is_fsym(t) { is[*div]($t) }
    get_slot(n1, t) { $t.(*div).n1 }
    get_slot(n2, t) { $t.(*div).n2 }
    make(t0, t1) { newDiv($t0, $t1) }
  }

  %op tFloat fmult(n1:tInt, n2:tInt) {
    is_fsym(t) { is[*div]($t) }
    get_slot(n1, t) { $t.(*fmult).n1 }
    get_slot(n2, t) { $t.(*fmult).n2 }
    make(t0, t1) { newFmult($t0, $t1) }
  }

  //---------------------------------
func TestCombinations(t *testing.T) {
    var subject natList = `nList(zero())
    nbCasesOK := 0
    %match {
      // Case 1: NumericConstraint(...) && OrConstraint(...)
      (zero() != x) && (x << suc(zero())|| x << suc(suc(zero()))) -> {
        nbCasesOK++
        fmt.Println("Case #1: x = " + fmt.Sprint(`x))
      } 

      // Case 2: MatchConstraint(...) && OrConstraint(...)
//      suc(zero()) << x && (x << suc(zero())|| x << suc(suc(zero()))) -> {
//        nbCasesOK++;
//      }

      // Case 3: MatchConstraint(...) && OrConstraintDisjunction(...)
//      uminus(zero()) << x && (div|fmult)[n1=x] << div(uminus(zero()),uminus(one())) -> {
//        nbCasesOK++;
//      }

      // Case 4:  NumericConstraint(...) && OrConstraintDisjunction(...)
//      (uminus(zero()) == x) && (div|fmult)[n1=x] << div(uminus(zero()),uminus(one())) -> {
//        nbCasesOK++;
//      }
    }
    _ = subject
    _ = nbCasesOK
//    assertTrue("testCombinations",nbCasesOK==5);
  }
