package binarynumber;
import binarynumber.binarynumber.*;
import binarynumber.binarynumber.types.*;
import tom.library.sl.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BinaryNumberTests {
  double precision = 0.000000000001;
  %include{ binarynumber/BinaryNumber.tom }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(BinaryNumberTests.class.getName());
  }

  @Test
  public void testIntegralNumbers() {
    double d;
    // 0
    BinaryNumber ast = `IntegralNumber(SingularBitList(Zero()));
    assertEquals(0.0, BinaryNumberValue.value(ast), precision);
    
    // 1
    ast = `IntegralNumber(SingularBitList(OneB()));
    assertEquals(1.0, BinaryNumberValue.value(ast), precision);
    
    // 101
    ast = `IntegralNumber(PluralBitList(PluralBitList(SingularBitList(OneB()), Zero()), OneB()));
    assertEquals(5.0, BinaryNumberValue.value(ast), precision);
  }
  
  @Test
  public void testRationalNumbers() {
    // 0.1
    BinaryNumber ast = `RationalNumber(SingularBitList(Zero()), SingularBitList(OneB()));
    assertEquals(0.5, BinaryNumberValue.value(ast), precision);
    
    // 0.01
    ast = ` RationalNumber(SingularBitList(Zero()),
        PluralBitList(SingularBitList(Zero()), OneB()));
    assertEquals(0.25, BinaryNumberValue.value(ast), precision);
    
    // 10.01
    ast = ` RationalNumber(PluralBitList(SingularBitList(OneB()), Zero()), 
        PluralBitList(SingularBitList(Zero()), OneB()));
    assertEquals(2.25, BinaryNumberValue.value(ast), precision);
    
    // 1101.01
    ast = ` RationalNumber ( PluralBitList ( PluralBitList ( PluralBitList (SingularBitList(OneB()), OneB()), Zero()), OneB()), PluralBitList(SingularBitList(Zero()), OneB()));
    assertEquals(13.25, BinaryNumberValue.value(ast), precision);
  }

}
