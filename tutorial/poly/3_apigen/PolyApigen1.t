import aterm.*;
import aterm.pure.*;
import expression.*;
import expression.types.*;

public class PolyApigen1 {
  private Factory factory;
  public PolyApigen1(Factory factory) {
    this.factory = factory;
  }

  public Factory getExpressionFactory() {
    return factory;
  }
 
  %include { expression.tom }

  public Expression differentiate(Expression e, Expression v) {
    %match(Expression e, Expression v) {
        // non-linear patterns are allowed
      variable(v1), variable(v1) -> { return `one(); }
      
      plus(a1,a2), vx -> {
        return `plus(differentiate(a1,vx),
                     differentiate(a2,vx));
      }
      mult(a1,a2), vx -> { 
        return `plus(mult(a1,differentiate(a2,vx)),
                     mult(a2,differentiate(a1,vx)));      
      }
      exp(a1), vx     -> { return `mult(differentiate(a1,vx),e); }
    }
    return `zero();
  }
     
    // simplification
  public Expression simplify(Expression t) {
    Expression res = t;
    block:{
      %match(Expression t) {
        exp(zero)    -> { res = `one(); break block; }
        plus(zero,x) -> { res = simplify(x); break block; }
        plus(x,zero) -> { res = simplify(x); break block; }
        mult(one,x)  -> { res = simplify(x); break block; }
        mult(x,one)  -> { res = simplify(x); break block; }
        mult(zero,x) -> { res = `zero(); break block; }
        mult(x,zero) -> { res = `zero(); break block; }
        plus(x,y)    -> { res = `plus(simplify(x),simplify(y)); break block; }
        mult(x,y)    -> { res = `mult(simplify(x),simplify(y)); break block; }
        exp(x)       -> { res = `exp(simplify(x)); break block; }
      }
    }
    if(t != res) res = simplify(res);
    return res;
  }

  public void run(int n) {
      // a literal string cannot be used in backquoted terms
    String X = "X";
    Expression var = `variable(X);
    Expression t = var;
      // build a tower of exponential
    for(int i=0 ; i<n ; i++) {
      t = `exp(t);
    }
      // compute the n-th derivative form
    Expression res = t;
    for(int i=0 ; i<n ; i++) {
      res = differentiate(res,var);
    }
    
    System.out.println("Derivative form of " + t + " wrt. " + var + " is:\n\t" + res);
    res = simplify(res);
    System.out.println("Simplified form is:\n\t" + res);
  }
    
  public final static void main(String[] args) {
    PolyApigen1 test = new  PolyApigen1(new Factory(new PureFactory()));
    test.run(2);
  }
}
