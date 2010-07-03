import org.junit.Assert;
import testdoublesubject.sig.*;
import testdoublesubject.sig.types.*;

public class TestDoubleSubject {
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestDoubleSubject.class.getName());
  }

%gom {
  module sig
  imports
  abstract syntax
  T1 = a() | f(x:T1) 
}

  @org.junit.Test
  public void test1() {
      T1 e = `a();
      %match(e) {
          f(e) -> {}
      }
  }

  
  
}
