    import aterm.*;
import aterm.pure.*;
import java.util.*;
import jtom.debug.TomDebugger;

public class TestDebug {

  ATermFactory factory;
  AFun fzero, fsuc;
  ATerm tzero;

  public TestDebug(ATermFactory factory) {
    this.factory = factory;
    fzero = factory.makeAFun("zero", 0, false);
    fsuc  = factory.makeAFun("suc" , 1, false);
    tzero = factory.makeAppl(fzero);
  }


  

    public Object tom_get_fun_sym_term( ATerm  t) { return  (((ATermAppl)t).getAFun()) ; }
    public boolean tom_cmp_fun_sym_term(Object t1, Object t2) { return  t1 == t2 ; }
    public Object tom_get_subterm_term( ATerm  t, int n) { return  (((ATermAppl)t).getArgument(n)) ; }
    

  

    
  
   

    

  public ATerm suc(ATerm t) {
    return factory.makeAppl(fsuc,t);
  }
  
  public ATerm plus(ATerm t1, ATerm t2) {
    {
      TomDebugger.debug.enteringMatch(1);
        /*Match(  OriginTracking(Name("Match"),39,Name("Peano1.t")),
          SubjectList([TLVar("t1",TomTypeAlone("term")),TLVar("t2",TomTypeAlone("term"))]),
          PatternList(   [  PatternAction(TermList([Appl(Option([OriginTracking(Name("x"),40,Name("Peano1.t"))]),Name("x"),[]),Appl(Option([OriginTracking(Name("zero"),40,Name("Peano1.t"))]),Name("zero"),[])]),Tom([TargetLanguageToTomTerm(TL(" return x; ",Position(40,19),Position(40,31)))])),
                            PatternAction(TermList([Appl(Option([OriginTracking(Name("x"),41,Name("Peano1.t"))]),Name("x"),[]),Appl(Option([OriginTracking(Name("suc"),41,Name("Peano1.t"))]),Name("suc"),[Appl(Option([OriginTracking(Name("y"),41,Name("Peano1.t"))]),Name("y"),[])])]),Tom([TargetLanguageToTomTerm(TL(" return suc(plus(x,y)); ",Position(41,19),Position(41,44)))]))
                            ]))
        */
      TomDebugger.debug.specifySubject(t1);
      TomDebugger.debug.specifySubject(t2);
      ATerm  tom_match1_1 = null;
      ATerm  tom_match1_2 = null;
      tom_match1_1 = ( ATerm ) t1;
      tom_match1_2 = ( ATerm ) t2;
      
matchlab_match1_pattern1: {
        
        TomDebugger.debug.enteringPattern(1);
        ATerm  x = null;
        x = ( ATerm ) tom_match1_1;
        if(tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match1_2) ,  fzero )) {
          TomDebugger.debug.patternSuccess();
           return x;         } else {TomDebugger.debug.patternFail();}
}
matchlab_match1_pattern2: {
        TomDebugger.debug.enteringPattern(2);
        ATerm  y = null;
        ATerm  x = null;
        x = ( ATerm ) tom_match1_1;
        if(tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match1_2) ,  fsuc )) {
          ATerm  tom_match1_2_1 = null;
          tom_match1_2_1 = ( ATerm ) tom_get_subterm_term(tom_match1_2, 0);
          y = ( ATerm ) tom_match1_2_1;
          TomDebugger.debug.patternSuccess();
          ATerm z = suc(plus(x,y));
          TomDebugger.debug.termCreation(z);
          return z;         } else {TomDebugger.debug.patternFail();}
}
    }
    
    return null;
  }

  public void run(int n) {
    ATerm N = tzero;
    for(int i=0 ; i<n ; i++) {
      N = suc(N);
    }
    ATerm res = plus(N,N);
    System.out.println("plus(" + n + "," + n + ") = " + res);
  }

  public final static void main(String[] args) {
    TomDebugger debug = new TomDebugger();
    TestDebug test = new TestDebug(new PureFactory());
    test.run(2);
  }
 

}

