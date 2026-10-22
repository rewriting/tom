package sl




















import (
  "testing"

  "fixture/introspectorbehaviour"
  "fixture/newbehaviour"
  "fixture/oldbehaviour"
  . "fixture/testsl"
  "tom/library/sl"
)

  func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_make_b()  Term  { return (MakeB()) }; func tom_make_c()  Term  { return (MakeC()) }; func tom_make_f(arg1  Term )  Term  { return (MakeF(arg1)) }; func tom_make_g(arg1  Term , arg2  Term )  Term  { return (MakeG(arg1,arg2)) }; func tom_is_fun_sym_l(t  Term ) bool { return func() bool { _, ok := (t).(*LTerm); return ok }();  }; func tom_empty_list_l()  Term  { return (EmptyL()) }; func tom_cons_list_l(e  Term , l  Term )  Term  { return (ConsL(e,l)) }; func tom_get_head_l_Term(l  Term )  Term  { return (l).GetHeadl();  }; func tom_get_tail_l_Term(l  Term )  Term  { return (l).GetTaill();  }; func tom_is_empty_l_Term(l  Term ) bool { return (l).IsEmptyl();  }; func tom_append_list_l(l1  Term , l2  Term )  Term  { if tom_is_empty_l_Term(l1) { return l2 } else if tom_is_empty_l_Term(l2) { return l1 } else if tom_is_fun_sym_l(l1) { if tom_is_empty_l_Term(tom_get_tail_l_Term(l1)) { return tom_cons_list_l(tom_get_head_l_Term(l1), l2) } else { return tom_cons_list_l(tom_get_head_l_Term(l1), tom_append_list_l(tom_get_tail_l_Term(l1), l2)) } } else { return tom_cons_list_l(l1, l2) }; }; func tom_get_slice_l(begin  Term , end  Term , tail  Term )  Term  { if tom_equal_term_Term(begin, end) { return tail } else if tom_equal_term_Term(end, tail) && (tom_is_empty_l_Term(end) || tom_equal_term_Term(end, tom_empty_list_l())) { return begin }; return tom_cons_list_l(func()  Term  { if tom_is_fun_sym_l(begin) { return tom_get_head_l_Term(begin) }; return begin }(), tom_get_slice_l(func()  Term  { if tom_is_fun_sym_l(begin) { return tom_get_tail_l_Term(begin) }; return tom_empty_list_l() }(), end, tail)) }; func tom_equal_term_Strategy(t1  sl.Strategy , t2  sl.Strategy ) bool { return t1 == t2;  }; func tom_is_sort_Strategy(t any) bool { return sl.IsStrategy(t);  }; func tom_equal_term_Position(t1  *sl.Position , t2  *sl.Position ) bool { return (t1).Equals(t2);  }; func tom_is_sort_Position(t any) bool { return sl.IsPosition(t);  }; func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  }; func tom_make_mu(name  sl.Strategy , v  sl.Strategy )  sl.Strategy  { return (sl.NewMu((name).(*sl.MuVar), v)) }; func tom_make_MuVar(name  string )  sl.Strategy  { return (sl.NewMuVar(name)) }; func tom_make_Identity()  sl.Strategy  { return (sl.NewIdentity()) }; func tom_make_All(v  sl.Strategy )  sl.Strategy  { return (sl.NewAll(v)) }; func tom_make_One(v  sl.Strategy )  sl.Strategy  { return (sl.NewOne(v)) }; func tom_make_OneId(v  sl.Strategy )  sl.Strategy  { return (sl.NewOneId(v)) }; func tom_is_fun_sym_Sequence(t  sl.Strategy ) bool { return sl.IsSequence(t);  }; func tom_empty_list_Sequence()  sl.Strategy  { return (nil) }; func tom_cons_list_Sequence(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequence(head, tail)) }; func tom_get_head_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_Sequence_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_Sequence(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_Sequence_Strategy(l1) { return l2 } else if tom_is_empty_Sequence_Strategy(l2) { return l1 } else if tom_is_fun_sym_Sequence(l1) { if tom_is_empty_Sequence_Strategy(tom_get_tail_Sequence_Strategy(l1)) { return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1), l2) } else { return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1), tom_append_list_Sequence(tom_get_tail_Sequence_Strategy(l1), l2)) } } else { return tom_cons_list_Sequence(l1, l2) }; }; func tom_get_slice_Sequence(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_Sequence_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_Sequence())) { return begin }; return tom_cons_list_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(begin) { return tom_get_head_Sequence_Strategy(begin) }; return begin }(), tom_get_slice_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(begin) { return tom_get_tail_Sequence_Strategy(begin) }; return tom_empty_list_Sequence() }(), end, tail)) }; func tom_is_fun_sym_SequenceId(t  sl.Strategy ) bool { return sl.IsSequenceId(t);  }; func tom_empty_list_SequenceId()  sl.Strategy  { return (nil) }; func tom_cons_list_SequenceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequenceId(head, tail)) }; func tom_get_head_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_SequenceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_SequenceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_SequenceId_Strategy(l1) { return l2 } else if tom_is_empty_SequenceId_Strategy(l2) { return l1 } else if tom_is_fun_sym_SequenceId(l1) { if tom_is_empty_SequenceId_Strategy(tom_get_tail_SequenceId_Strategy(l1)) { return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1), l2) } else { return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1), tom_append_list_SequenceId(tom_get_tail_SequenceId_Strategy(l1), l2)) } } else { return tom_cons_list_SequenceId(l1, l2) }; }; func tom_get_slice_SequenceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_SequenceId_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_SequenceId())) { return begin }; return tom_cons_list_SequenceId(func()  sl.Strategy  { if tom_is_fun_sym_SequenceId(begin) { return tom_get_head_SequenceId_Strategy(begin) }; return begin }(), tom_get_slice_SequenceId(func()  sl.Strategy  { if tom_is_fun_sym_SequenceId(begin) { return tom_get_tail_SequenceId_Strategy(begin) }; return tom_empty_list_SequenceId() }(), end, tail)) }; func tom_is_fun_sym_Choice(t  sl.Strategy ) bool { return sl.IsChoice(t);  }; func tom_empty_list_Choice()  sl.Strategy  { return (nil) }; func tom_cons_list_Choice(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoice(head, tail)) }; func tom_get_head_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_Choice_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_Choice(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_Choice_Strategy(l1) { return l2 } else if tom_is_empty_Choice_Strategy(l2) { return l1 } else if tom_is_fun_sym_Choice(l1) { if tom_is_empty_Choice_Strategy(tom_get_tail_Choice_Strategy(l1)) { return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1), l2) } else { return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1), tom_append_list_Choice(tom_get_tail_Choice_Strategy(l1), l2)) } } else { return tom_cons_list_Choice(l1, l2) }; }; func tom_get_slice_Choice(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_Choice_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_Choice())) { return begin }; return tom_cons_list_Choice(func()  sl.Strategy  { if tom_is_fun_sym_Choice(begin) { return tom_get_head_Choice_Strategy(begin) }; return begin }(), tom_get_slice_Choice(func()  sl.Strategy  { if tom_is_fun_sym_Choice(begin) { return tom_get_tail_Choice_Strategy(begin) }; return tom_empty_list_Choice() }(), end, tail)) }; func tom_is_fun_sym_ChoiceId(t  sl.Strategy ) bool { return sl.IsChoiceId(t);  }; func tom_empty_list_ChoiceId()  sl.Strategy  { return (nil) }; func tom_cons_list_ChoiceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoiceId(head, tail)) }; func tom_get_head_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_ChoiceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_ChoiceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_ChoiceId_Strategy(l1) { return l2 } else if tom_is_empty_ChoiceId_Strategy(l2) { return l1 } else if tom_is_fun_sym_ChoiceId(l1) { if tom_is_empty_ChoiceId_Strategy(tom_get_tail_ChoiceId_Strategy(l1)) { return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1), l2) } else { return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1), tom_append_list_ChoiceId(tom_get_tail_ChoiceId_Strategy(l1), l2)) } } else { return tom_cons_list_ChoiceId(l1, l2) }; }; func tom_get_slice_ChoiceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_ChoiceId_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_ChoiceId())) { return begin }; return tom_cons_list_ChoiceId(func()  sl.Strategy  { if tom_is_fun_sym_ChoiceId(begin) { return tom_get_head_ChoiceId_Strategy(begin) }; return begin }(), tom_get_slice_ChoiceId(func()  sl.Strategy  { if tom_is_fun_sym_ChoiceId(begin) { return tom_get_tail_ChoiceId_Strategy(begin) }; return tom_empty_list_ChoiceId() }(), end, tail)) }; func tom_make_Try(s  sl.Strategy )  sl.Strategy  { return (tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) }; func tom_make_Repeat(s  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())))) }; func tom_make_RepeatId(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId())))) }; func tom_make_OnceTopDown(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice())))) }; func tom_make_OnceTopDownId(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId())))) }; 

  var nb *newbehaviour.NewBehaviour = &newbehaviour.NewBehaviour{}
  var ob *oldbehaviour.OldBehaviour = &oldbehaviour.OldBehaviour{}
  var ib *introspectorbehaviour.IntrospectorBehaviour = &introspectorbehaviour.IntrospectorBehaviour{}



func TestSL1(t *testing.T) {    
    var result Term = tom_make_f(tom_make_a())
    assertEquals(t, result, must(t)(ob.Test1()))
    assertEquals(t, result, must(t)(nb.Test1()))
    assertEquals(t, result, must(t)(ib.Test1()))
  }
func TestSL2(t *testing.T) {    
    var result Term = tom_make_f(tom_make_b())
    assertEquals(t, result, must(t)(ob.Test2()))
    assertEquals(t, result, must(t)(nb.Test2()))
    assertEquals(t, result, must(t)(ib.Test2()))
  }
func TestSL3(t *testing.T) {    
    var result Term = tom_make_f(tom_make_a())
    assertEquals(t, result, must(t)(ob.Test3()))
    assertEquals(t, result, must(t)(nb.Test3()))
    assertEquals(t, result, must(t)(ib.Test3()))
  }
func TestSL4(t *testing.T) {    
    var result Term = tom_make_g(tom_make_f(tom_make_c()),tom_make_c())
    assertEquals(t, result, must(t)(ob.Test4()))
    assertEquals(t, result, must(t)(nb.Test4()))
    assertEquals(t, result, must(t)(ib.Test4()))
  }
func TestSL5(t *testing.T) {    
    var result Term = tom_make_g(tom_make_f(tom_make_b()),tom_make_c()) 
    assertEquals(t, result, must(t)(ob.Test5()))
    assertEquals(t, result, must(t)(nb.Test5()))
    assertEquals(t, result, must(t)(ib.Test5()))
  }
func TestSLChoice(t *testing.T) {   
    var result Term = tom_make_f(tom_make_b()) 
    assertEquals(t, result, must(t)(ob.TestChoice()))
    assertEquals(t, result, must(t)(nb.TestChoice()))
    assertEquals(t, result, must(t)(ib.TestChoice()))
  }
func TestSLChoiceSideEffect(t *testing.T) {   
    var result Term = tom_make_f(tom_make_b()) 
    assertEquals(t, result, must(t)(ob.TestChoiceSideEffect()))
    assertEquals(t, result, must(t)(nb.TestChoiceSideEffect()))
    assertEquals(t, result, must(t)(ib.TestChoiceSideEffect()))
  }
func TestSLNot(t *testing.T) {    
    var result Term = tom_make_f(tom_make_b())
    assertEquals(t, result, must(t)(ob.TestNot()))
    assertEquals(t, result, must(t)(nb.TestNot()))
    assertEquals(t, result, must(t)(ib.TestNot()))
  }
func TestSLNotSideEffect(t *testing.T) {    
    var result Term = tom_make_f(tom_make_a())
    assertEquals(t, result, must(t)(ob.TestNotSideEffect()))
    assertEquals(t, result, must(t)(nb.TestNotSideEffect()))
    assertEquals(t, result, must(t)(ib.TestNotSideEffect()))
  }
func TestSL6(t *testing.T) {
    var result Term = tom_make_g(tom_make_f(tom_make_c()),tom_make_c())
    assertEquals(t, result, must(t)(ob.Test6()))
    assertEquals(t, result, must(t)(nb.Test6()))
    assertEquals(t, result, must(t)(ib.Test6()))
  }
func TestSL7(t *testing.T) {    
    var result Term = tom_make_g(tom_make_f(tom_make_b()),tom_make_b())
    assertEquals(t, result, must(t)(ob.Test7()))
    assertEquals(t, result, must(t)(nb.Test7()))
    assertEquals(t, result, must(t)(ib.Test7()))
  }
func TestSL8(t *testing.T) {    
    var result Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_c()) 
    assertEquals(t, result, must(t)(ob.Test8()))
    assertEquals(t, result, must(t)(nb.Test8()))
    assertEquals(t, result, must(t)(ib.Test8()))
  }
func TestSL9(t *testing.T) {    
    var result Term = tom_make_b() 
    assertEquals(t, result, must(t)(ob.Test9()))
    assertEquals(t, result, must(t)(nb.Test9()))
    assertEquals(t, result, must(t)(ib.Test9()))
  }
func TestITESideEffect(t *testing.T) {    
    var result Term = tom_make_f(tom_make_a()) 
    assertEquals(t, result, must(t)(ob.TestITESideEffect()))
    assertEquals(t, result, must(t)(nb.TestITESideEffect()))
    assertEquals(t, result, must(t)(ib.TestITESideEffect()))
  }
func TestSL10(t *testing.T) {   
    var result Term = tom_make_a()
    assertEquals(t, result, must(t)(ob.Test10()))
    assertEquals(t, result, must(t)(nb.Test10()))
    assertEquals(t, result, must(t)(ib.Test10()))
  }
func TestSL11(t *testing.T) {   
    var result Term = tom_make_g(tom_make_f(tom_make_c()),tom_make_c())
    assertEquals(t, result, must(t)(ob.Test11()))
    assertEquals(t, result, must(t)(nb.Test11()))
    assertEquals(t, result, must(t)(ib.Test11()))
  }
func TestSL12(t *testing.T) {   
    var result Term = tom_make_g(tom_make_f(tom_make_c()),tom_make_c()) 
    assertEquals(t, result, must(t)(ob.Test12()))
    assertEquals(t, result, must(t)(nb.Test12()))
    assertEquals(t, result, must(t)(ib.Test12()))
  }
func TestSL13(t *testing.T) {   
    var result Term = tom_make_g(tom_make_f(tom_make_c()),tom_make_c()) 
    assertEquals(t, result, must(t)(ob.Test13()))
    assertEquals(t, result, must(t)(nb.Test13()))
    assertEquals(t, result, must(t)(ib.Test13()))
  }
func TestSLWhen1(t *testing.T) {    
    var result Term = tom_make_f(tom_make_b())
    assertEquals(t, result, must(t)(ob.TestWhen1()))
    assertEquals(t, result, must(t)(nb.TestWhen1()))
    assertEquals(t, result, must(t)(ib.TestWhen1()))
  }
func TestSLCongruence1(t *testing.T) {    
    var result Term = tom_make_g(tom_make_f(tom_make_b()),tom_make_c())
    assertEquals(t, result, must(t)(ob.TestCongruence1()))
    assertEquals(t, result, must(t)(nb.TestCongruence1()))
    assertEquals(t, result, must(t)(ib.TestCongruence1()))
  }
func TestSLCongruence2(t *testing.T) {
    
    
    if catchRuntimeException(func() {
      if _, err := ob.TestCongruence2(); err != nil {
        t.Fatal("tom.library.sl.VisitFailure()")
      }
      fail(t)
    }) {
      if _, err := nb.TestCongruence2(); err == nil {
        fail(t)
      } else {           }
    }
    if _, err := ib.TestCongruence2(); err == nil {
      fail(t)
    } else {           }
}
func TestSLCongruenceList(t *testing.T) {
    var result Term = tom_cons_list_l(tom_make_a(),tom_cons_list_l(tom_make_c(),tom_empty_list_l()))
    assertEquals(t, result, must(t)(ob.TestCongruenceList()))
    assertEquals(t, result, must(t)(nb.TestCongruenceList()))
    assertEquals(t, result, must(t)(ib.TestCongruenceList()))
}






func must(t *testing.T) func(Term, error) Term {
  return func(r Term, err error) Term {
    t.Helper()
    if err != nil {
      t.Fatal("tom.library.sl.VisitFailure()")
    }
    return r
  }
}










func catchRuntimeException(f func()) (caught bool) {
  defer func() {
    if r := recover(); r != nil {
      caught = true
    }
  }()
  f()
  return false
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}




func fail(t *testing.T) {
  t.Helper()
  t.Fatal()
}
