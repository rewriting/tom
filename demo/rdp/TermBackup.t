import termbackup.term.types.*;

class TermBackup {
  
  %gom {
    module Term
    imports String
    abstract syntax

    T = | f(x1:T, x2:T) 
        | g(x1:T) 
        | a()
        | b()
        | c()

    //module Term:rules() {
    //  f(a(),x) -> x
    //}
    //L = conc( T* )
  }

  public final static void main(String[] args) {
    T t = `f(a(),b());
    System.out.println("t = " + t.getx1());

    //%match(t) {
    //  f(a(),x) -> { System.out.println("x = " + `x); }
    //}
    
    //L l = `conc(a(),b(),c());
    //System.out.println("l = " + l);

    //%match(l) {
    //  conc(_*,x,_*) -> { System.out.println("x = " + `x); }
    //}
    
  }
 
}
/*
 * getx1()
 * match
 * rule
 * list
 * string
 */
