
 import aterm.*;
import aterm.pure.*;
import java.util.*;

public class JMatch3 {

  private ATermFactory factory;
  private AFun fzero, fsuc, fplus;
  public ATermAppl tzero;

  

 public Object tom_get_fun_sym_term(ATerm t) { return  (((ATermAppl)t).getAFun()) ; }
 public boolean tom_cmp_fun_sym_term(Object t1, Object t2) { return  t1 == t2 ; }
 public Object tom_get_subterm_term(ATerm t, int n) { return  (((ATermAppl)t).getArgument(n)) ; }
 
  
  

 public Object tom_get_fun_sym_term1(ATerm t) { return  (((ATermAppl)t).getAFun()) ; }
 public boolean tom_cmp_fun_sym_term1(Object t1, Object t2) { return  t1 == t2 ; }
 public Object tom_get_subterm_term1(ATerm t, int n) { return  (((ATermAppl)t).getArgument(n)) ; }
 
  
  

 public ATerm tom_make_zero1() { return  factory.makeAppl(fzero) ; }
 public boolean tom_is_fun_sym_zero1(ATerm t) { return  ((((ATermAppl)t).getAFun()) == fzero)  ; }
   

  

 public ATerm tom_make_zero() { return  factory.makeAppl(fzero) ; }
 public boolean tom_is_fun_sym_zero(ATerm t) { return  ((((ATermAppl)t).getAFun()) == fzero)  ; }
 
  
  

 public ATerm tom_make_suc(ATerm t) { return  factory.makeAppl(fsuc,t) ; }
 

  

 public ATerm tom_make_fib1(ATerm t) { return  fib1(t) ; }
 

  

 public ATerm tom_make_fib2(ATerm t) { return  fib2(t) ; }
 

  public JMatch3(ATermFactory factory) {
    this.factory = factory;

    fzero = factory.makeAFun("zero", 0, false);
    fsuc  = factory.makeAFun("suc" , 1, false);
    fplus = factory.makeAFun("plus", 2, false);
    tzero = factory.makeAppl(fzero);
  }

  public void run() {
    for(int i=0 ; i<100 ; i++) {
      ATerm N = int2peano(i);
      assertTrue( peano2int(plus1(N,N)) == (i+i) );
      assertTrue( peano2int(plus2(N,N)) == (i+i) );
      assertTrue( peano2int(plus3(N,N)) == (i+i) );
    }

    for(int i=0 ; i<15 ; i++) {
      ATerm N = int2peano(i);
      assertTrue( peano2int(fib1(N)) == fibint(i) );
      assertTrue( peano2int(fib2(N)) == fibint(i) );
    }
    
  }
  
  public final static void main(String[] args) {
    JMatch3 test = new JMatch3(new PureFactory(16));
    test.run();
  }

  public ATerm plus1(ATerm t1, ATerm t2) {
     { ATerm tom_match1_1 = null; ATerm tom_match1_2 = null; tom_match1_1 = (ATerm) t1; tom_match1_2 = (ATerm) t2;matchlab_match1_pattern1: { ATerm x = null; x = (ATerm) tom_match1_1; if(tom_is_fun_sym_zero1(tom_match1_2)) {
  return x;  }}matchlab_match1_pattern2: { ATerm x = null; ATerm y = null; x = (ATerm) tom_match1_1; if(tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match1_2) , fsuc)) { ATerm tom_match1_2_1 = null; tom_match1_2_1 = (ATerm) tom_get_subterm_term(tom_match1_2, 0); y = (ATerm) tom_match1_2_1;
  return suc(plus1(x,y));  }} }
 
    return null;
  }

  public ATerm plus2(ATerm t1, ATerm t2) {
     { ATerm tom_match2_1 = null; ATerm tom_match2_2 = null; tom_match2_1 = (ATerm) t1; tom_match2_2 = (ATerm) t2;matchlab_match2_pattern1: {
  return x; }matchlab_match2_pattern2: { ATerm y = null; ATerm x = null; x = (ATerm) tom_match2_1; if(tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match2_2) , fsuc)) { ATerm tom_match2_2_1 = null; tom_match2_2_1 = (ATerm) tom_get_subterm_term(tom_match2_2, 0); y = (ATerm) tom_match2_2_1;
  return suc(plus2(x,y));  }} }
 
    return null;
  }

  public ATerm plus3(ATerm t1, ATerm t2) {
     { ATerm tom_match3_1 = null; ATerm tom_match3_2 = null; tom_match3_1 = (ATerm) t1; tom_match3_2 = (ATerm) t2;matchlab_match3_pattern1: { ATerm x = null; x = (ATerm) tom_match3_1; if(tom_is_fun_sym_zero(tom_match3_2)) {
  return x;  }}matchlab_match3_pattern2: { ATerm x = null; ATerm y = null; ATerm z = null; x = (ATerm) tom_match3_1; if(tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match3_2) , fsuc)) { ATerm tom_match3_2_1 = null; tom_match3_2_1 = (ATerm) tom_get_subterm_term(tom_match3_2, 0); z = (ATerm) tom_match3_2_1; y = (ATerm) z;
  return suc(plus3(x,y));  }} }
 
    return null;
  }

  public ATerm fib3(ATerm t) {
     { ATerm tom_match4_1 = null; tom_match4_1 = (ATerm) t;matchlab_match4_pattern1: { if(tom_is_fun_sym_zero(tom_match4_1)) {
  return tom_make_suc(tom_make_zero()) ;  }}matchlab_match4_pattern2: { ATerm x = null; if(tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match4_1) , fsuc)) { ATerm tom_match4_1_1 = null; tom_match4_1_1 = (ATerm) tom_get_subterm_term(tom_match4_1, 0); if(tom_is_fun_sym_zero(tom_match4_1_1)) { x = (ATerm) tom_match4_1_1;
  return tom_make_suc(x) ;  } }}matchlab_match4_pattern3: { ATerm x = null; if(tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match4_1) , fsuc)) { ATerm tom_match4_1_1 = null; tom_match4_1_1 = (ATerm) tom_get_subterm_term(tom_match4_1, 0); if(tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match4_1_1) , fsuc)) { ATerm tom_match4_1_1_1 = null; tom_match4_1_1_1 = (ATerm) tom_get_subterm_term(tom_match4_1_1, 0); x = (ATerm) tom_match4_1_1_1;
  return plus3(fib3(x),fib3(suc(x)));  } }} }
 
    return null;
  }

  public ATerm suc(ATerm t) {
    return (ATerm)factory.makeAppl(fsuc,t);
  }

  public int fibint(int n) {
    if(n<=1) {
      return 1;
    } else {
      return fibint(n-1)+fibint(n-2);
    }
  }
  
  public ATerm int2peano(int n) {
    ATerm N = tzero;
    for(int i=0 ; i<n ; i++) {
      N = suc(N);
    }
    return N;
  }

  public int peano2int(ATerm N) {
     { ATerm tom_match5_1 = null; tom_match5_1 = (ATerm) N;matchlab_match5_pattern1: { if(tom_is_fun_sym_zero(tom_match5_1)) {
  return 0;  }}matchlab_match5_pattern2: { ATerm x = null; if(tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match5_1) , fsuc)) { ATerm tom_match5_1_1 = null; tom_match5_1_1 = (ATerm) tom_get_subterm_term(tom_match5_1, 0); x = (ATerm) tom_match5_1_1;
 return 1+peano2int(x);  }} }
 
    return 0;
  }
  
  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }
  
}
