package binarynumber;
import binarynumber.binarynumber.*;
import binarynumber.binarynumber.types.*;
import tom.library.sl.*;

public class BinaryNumberValue {

  %include{ int.tom }
  %include{ sl.tom }
  %include{ double.tom }
  %include{ binarynumber/BinaryNumber.tom }


  public static double value(BinaryNumber n) {
    try {
      double d = 0;
      `value(d).visit(n);
      return d;
    } catch (VisitFailure v) {
      throw new RuntimeException("Unexpected VisitFailure");
    }
  }


  %strategy value(result:double) extends Identity() {
    visit Bit {
      // eq Zero.value() = 0;
      Zero() -> {
        result = 0;
      }
      //eq OneB.value() = java.lang.Math.pow(2.0, scale());
      OneB() -> {
        int i = 0;
        `scale(i).visit(getEnvironment());
        result = java.lang.Math.pow(2.0, i);
      }
    }

    visit BitList {
      //eq SingularBitList.value() = getBit().value();
      SingularBitList(bit) -> {
        double r = 0;
        `value(r).visit(`bit);
        result = r;
      }
      //eq PluralBitList.value() = getBit().value() + getBitList().value();
      PluralBitList(bitList,bit) -> {
        double i1 = 0;
        double i2 = 0;
        `value(i1).visit(`bit);
        `value(i2).visit(`bitList);
        result = i1 + i2;
      }
    }

    visit BinaryNumber {
      //eq IntegralNumber.value() = getIntegralPart().value();
      IntegralNumber(IntegralPart) -> {
        double r = 0 ;
        `value(r).visit(`IntegralPart);
        result = r;
      }

      //eq RationalNumber.value() = getIntegralPart().value() + getFractionalPart().value();
      RationalNumber(IntegralPart,FractionalPart) -> {
        double i1 = 0; 
        double i2 = 0; 
        `value(i1).visit(`IntegralPart);
        `value(i2).visit(`FractionalPart);
        result = i1 + i2;
      }
    }
  }


  %strategy scale(result:int) extends Identity() {
    visit BitList {
      //eq SingularBitList.getBit().scale() = scale();
      SingularBitList(bit) -> {
        int r = 0;
        `scale(r).visit(`bit);
        result = r;
      }
      //eq PluralBitList.getBit().scale() = scale();
      //eq PluralBitList.getBitList().scale() = scale() + 1;
      PluralBitList(bitList,bit) -> {
        `scale(result).visit(`bit);
        int r = result+1;
        `scale(r).visit(`bitList);
      }
    }

    visit BinaryNumber {
      //eq IntegralNumber.getIntegralPart().scale() = 0;
      IntegralNumber(IntegralPart) -> {
        `scale(0).visit(`IntegralPart); 
      }
      //eq RationalNumber.getIntegralPart().scale() = 0;
      //eq RationalNumber.getFractionalPart().scale() = -getFractionalPart().length();
      RationalNumber(IntegralPart,FractionalPart) -> {
        `scale(0).visit(`IntegralPart);
        int res =0;
        `length(res).visit(`FractionalPart);
        `scale(-res).visit(`FractionalPart);
      }
    }
  }


  %strategy length(result:int) extends Identity() {
    visit BitList {
      //eq SingularBitList.length() = 1;
      SingularBitList(bit) -> {
        result =1;
      }
      //eq PluralBitList.length() = getBitList().length() + 1;
      PluralBitList(bitList,bit) -> {
        int i = 0;
        `length(i).visit(`bitList);
        result = i+1;
      }
    }
  }

}
