package testpeano






import (
  "testing"

  "tomtest/aterm"
)



var factory = aterm.GetInstance()
var fzero, fsuc, fplus *aterm.AFun
var tzero *aterm.ATermAppl



func init() {
  fzero = factory.MakeAFun("zero", 0, false)
  fsuc  = factory.MakeAFun("suc" , 1, false)
  fplus = factory.MakeAFun("plus", 2, false)
  tzero = factory.MakeAppl(fzero)
}

func tom_equal_term_term(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return t1 == t2;  }; func tom_is_sort_term(t any) bool { return t != nil;  }; func tom_equal_term_appl(t1  *aterm.ATermAppl , t2  *aterm.ATermAppl ) bool { return t1 == t2;  }; func tom_is_sort_appl(t any) bool { return t != nil;  }; func tom_is_fun_sym_zero(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetAFun() == fzero;  }; func tom_make_zero()  aterm.ATerm  { return (factory.MakeAppl(fzero)) }; func tom_is_fun_sym_suc(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetAFun() == fsuc;  }; func tom_make_suc(t  aterm.ATerm )  aterm.ATerm  { return (factory.MakeAppl(fsuc,t)) }; func tom_get_slot_suc_pred(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(0);  }; 
































func TestPlus1(t *testing.T) {
  for i:=0 ; i<100 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing plus1 with N ="+N.String()+": ",
	  peano2int(plus1(N,N)) == (i+i) )
  }
}



func TestPlus2(t *testing.T) {
  for i:=0 ; i<100 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing plus2 with N ="+N.String()+": ",
	  peano2int(plus2(N,N)) == (i+i) )
  }
}



func TestPlus3(t *testing.T) {
  for i:=0 ; i<100 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing plus3 with N ="+N.String()+": ",
	  peano2int(plus3(N,N)) == (i+i) )
  }
}



func TestPlus4(t *testing.T) {
  for i:=0 ; i<100 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing plus4 with N ="+N.String()+": ",
	  peano2int(plus4(N,N)) == (i+i) )
  }
}



func TestFib3(t *testing.T) {
  for i:=0 ; i<15 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing fib3 with N ="+N.String()+": ",
	  peano2int(fib3(N)) == fibint(i) )
  }
}



func TestFib4(t *testing.T) {
  for i:=0 ; i<15 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing fib4 with N ="+N.String()+": ",
	  peano2int(fib4(N)) == fibint(i) )
  }
}



func plus1(t1 aterm.ATerm, t2 aterm.ATerm) aterm.ATerm {
  ; { { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { var tomMatch1_5 bool =  false ; _ = tomMatch1_5; var tomMatch1_4  aterm.ATerm  =  *new(aterm.ATerm) ; _ = tomMatch1_4; var tomMatch1_3  aterm.ATerm  =  *new(aterm.ATerm) ; _ = tomMatch1_3; if tom_is_fun_sym_zero(any(t2).( aterm.ATerm )) { { tomMatch1_5 =  true ; tomMatch1_3 = any(t2).( aterm.ATerm );  };  } else { if tom_is_fun_sym_zero(any(t2).( aterm.ATerm )) { { tomMatch1_5 =  true ; tomMatch1_4 = any(t2).( aterm.ATerm );  };  };  }; if tomMatch1_5 { 
 return any(t1).( aterm.ATerm ) };  };  };  }; { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { if tom_is_fun_sym_suc(any(t2).( aterm.ATerm )) { 
 return suc(plus1(any(t1).( aterm.ATerm ),tom_get_slot_suc_pred(any(t2).( aterm.ATerm ))))  };  };  };  };  }; 

  return nil
}



func plus2(t1 aterm.ATerm, t2 aterm.ATerm) aterm.ATerm {
  ; { { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { if tom_is_fun_sym_zero(any(t2).( aterm.ATerm )) { 
 return any(t1).( aterm.ATerm ) };  };  };  }; { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { if tom_is_fun_sym_suc(any(t2).( aterm.ATerm )) { 
 return suc(plus2(any(t1).( aterm.ATerm ),tom_get_slot_suc_pred(any(t2).( aterm.ATerm ))))  };  };  };  };  }; 

  return nil
}



func plus3(t1 aterm.ATerm, t2 aterm.ATerm) aterm.ATerm {
  ; { { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { if tom_is_fun_sym_zero(any(t2).( aterm.ATerm )) { 
 return any(t1).( aterm.ATerm ) };  };  };  }; { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { if tom_is_fun_sym_suc(any(t2).( aterm.ATerm )) { 
 return suc(plus3(any(t1).( aterm.ATerm ),tom_get_slot_suc_pred(any(t2).( aterm.ATerm ))))  };  };  };  };  }; 

  return nil
}



func plus4(t1 aterm.ATerm, t2 aterm.ATerm) aterm.ATerm {
  ; { { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { var tomMatch4_5 bool =  false ; _ = tomMatch4_5; var tomMatch4_4  aterm.ATerm  =  *new(aterm.ATerm) ; _ = tomMatch4_4; var tomMatch4_3  aterm.ATerm  =  *new(aterm.ATerm) ; _ = tomMatch4_3; if tom_is_fun_sym_zero(any(t2).( aterm.ATerm )) { { tomMatch4_5 =  true ; tomMatch4_3 = any(t2).( aterm.ATerm );  };  } else { if tom_is_fun_sym_zero(any(t2).( aterm.ATerm )) { { tomMatch4_5 =  true ; tomMatch4_4 = any(t2).( aterm.ATerm );  };  };  }; if tomMatch4_5 { 
 return any(t1).( aterm.ATerm ) };  };  };  }; { if tom_is_sort_term(t1) { if tom_is_sort_term(t2) { var tomMatch4_12 bool =  false ; _ = tomMatch4_12; var tomMatch4_10  aterm.ATerm  =  *new(aterm.ATerm) ; _ = tomMatch4_10; var tomMatch4_11  aterm.ATerm  =  *new(aterm.ATerm) ; _ = tomMatch4_11; var tomMatch4_8  aterm.ATerm  =  *new(aterm.ATerm) ; _ = tomMatch4_8; if tom_is_fun_sym_suc(any(t2).( aterm.ATerm )) { { tomMatch4_12 =  true ; tomMatch4_10 = any(t2).( aterm.ATerm ); tomMatch4_8 = tom_get_slot_suc_pred(tomMatch4_10);  };  } else { if tom_is_fun_sym_suc(any(t2).( aterm.ATerm )) { { tomMatch4_12 =  true ; tomMatch4_11 = any(t2).( aterm.ATerm ); tomMatch4_8 = tom_get_slot_suc_pred(tomMatch4_11);  };  };  }; if tomMatch4_12 { 
 return suc(plus1(any(t1).( aterm.ATerm ),tomMatch4_8))  };  };  };  };  }; 

  return nil
}



func fib3(t aterm.ATerm) aterm.ATerm {
  ; { { if tom_is_sort_term(t) { if tom_is_fun_sym_zero(any(t).( aterm.ATerm )) { 
 return tom_make_suc(tom_make_zero()) };  };  }; { if tom_is_sort_term(t) { if tom_is_fun_sym_suc(any(t).( aterm.ATerm )) { var tomMatch5_4  aterm.ATerm  = tom_get_slot_suc_pred(any(t).( aterm.ATerm )); _ = tomMatch5_4; if tom_is_fun_sym_zero(any(tomMatch5_4).( aterm.ATerm )) { 
 return tom_make_suc(tomMatch5_4) };  };  };  }; { if tom_is_sort_term(t) { if tom_is_fun_sym_suc(any(t).( aterm.ATerm )) { var tomMatch5_10  aterm.ATerm  = tom_get_slot_suc_pred(any(t).( aterm.ATerm )); _ = tomMatch5_10; if tom_is_fun_sym_suc(any(tomMatch5_10).( aterm.ATerm )) { var tom___x  aterm.ATerm  = tom_get_slot_suc_pred(tomMatch5_10); _ = tom___x; 
 return plus3(fib3(tom___x),fib3(suc(tom___x)))  };  };  };  };  }; 

  return nil
}



func fib4(t aterm.ATerm) aterm.ATerm {
  ; { { if tom_is_sort_term(t) { if tom_is_fun_sym_zero(any(t).( aterm.ATerm )) { 
 return tom_make_suc(tom_make_zero()) };  };  }; { if tom_is_sort_term(t) { if tom_is_fun_sym_suc(any(t).( aterm.ATerm )) { if tom_is_fun_sym_zero(any(tom_get_slot_suc_pred(any(t).( aterm.ATerm ))).( aterm.ATerm )) { 
 return tom_make_suc(tom_make_zero()) };  };  };  }; { if tom_is_sort_term(t) { if tom_is_fun_sym_suc(any(t).( aterm.ATerm )) { var tomMatch6_10  aterm.ATerm  = tom_get_slot_suc_pred(any(t).( aterm.ATerm )); _ = tomMatch6_10; if tom_is_fun_sym_suc(any(tomMatch6_10).( aterm.ATerm )) { var tom___x  aterm.ATerm  = tom_get_slot_suc_pred(tomMatch6_10); _ = tom___x; 
 return plus3(fib4(tom___x),fib4(suc(tom___x)))  };  };  };  };  }; 

  return nil
}



func suc(t aterm.ATerm) aterm.ATerm {
  return factory.MakeAppl(fsuc,t)
}



func fibint(n int) int {
  if n<=1 {
    return 1
  } else {
    return fibint(n-1)+fibint(n-2)
  }
}



func TestInt2p(t *testing.T) {
  assertEquals(t, "int2peano should return sssss(tzero) for 5",
	int2peano(5),tom_make_suc(tom_make_suc(tom_make_suc(tom_make_suc(tom_make_suc(tzero))))))
}



func int2peano(n int) aterm.ATerm {
  var N aterm.ATerm = tzero
  for i:=0 ; i<n ; i++ {
    N = suc(N)
  }
  return N
}



func TestPeano2int(t *testing.T) {
  assertEquals(t, "peano2int should return 5 for sssss(tzero)",
	peano2int(tom_make_suc(tom_make_suc(tom_make_suc(tom_make_suc(tom_make_suc(tzero)))))),5)
}



func peano2int(N aterm.ATerm) int {
  ; { { if tom_is_sort_term(N) { if tom_is_fun_sym_zero(any(N).( aterm.ATerm )) { 
 return 0  };  };  }; { if tom_is_sort_term(N) { if tom_is_fun_sym_suc(any(N).( aterm.ATerm )) { 
return 1+peano2int(tom_get_slot_suc_pred(any(N).( aterm.ATerm )))  };  };  };  }; 

  return 0
}




func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Error(message)
  }
}



func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
