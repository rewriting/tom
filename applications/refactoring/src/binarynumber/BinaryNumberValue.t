package binarynumber;

import binarynumber.binarynumber.*;
import binarynumber.binarynumber.types.*;
import binarynumber.binarynumber.types.bit.*;
import binarynumber.binarynumber.types.bitlist.*;
import binarynumber.binarynumber.types.binarynumber.*;
import tom.library.sl.*;
import java.util.*;

public class BinaryNumberValue {

  %include{ sl.tom }
  %include{ binarynumber/BinaryNumber.tom }


  public static double value(BinaryNumber n) {
    try {
      `value().visit(n);
      return n.value;
    } catch (VisitFailure v) {
      throw new RuntimeException("Unexpected VisitFailure");
    }
  }

  %strategy value() extends Identity() {
    visit Bit {
      // eq Zero.value() = 0;
      zero@Zero() -> {
        `zero.value = 0;
      }
      //eq OneB.value() = java.lang.Math.pow(2.0, scale());
      one@OneB() -> {
        `scale().visit(getEnvironment());  
        `one.value = java.lang.Math.pow(2.0, `one.scale);
      }
    }

    visit BitList {
      //eq SingularBitList.value() = getBit().value();
      singularbitlist@SingularBitList(bit) -> {
        getEnvironment().down(1);
        `value().visit(getEnvironment());
        getEnvironment().up();
        `singularbitlist.value = `bit.value;
      }
      //eq PluralBitList.value() = getBit().value() + getBitList().value();
      pluralbitlist@PluralBitList(bitlist,bit) -> {
        getEnvironment().down(1);
        `value().visit(getEnvironment());
        getEnvironment().up();
        getEnvironment().down(2);
        `value().visit(getEnvironment());
        getEnvironment().up();
        `pluralbitlist.value = `bitlist.value + `bit.value;
      }
    }

    visit BinaryNumber {
      //eq IntegralNumber.value() = getIntegralPart().value();
      integralnumber@IntegralNumber(integralpart) -> {
        getEnvironment().down(1);
        `value().visit(getEnvironment());
        getEnvironment().up();
        `integralnumber.value = `integralpart.value;
      }

      //eq RationalNumber.value() = getIntegralPart().value() + getFractionalPart().value();
      rationalnumber@RationalNumber(integralpart,fractionalpart) -> {
        getEnvironment().down(1);
        `value().visit(getEnvironment());
        getEnvironment().up();
        getEnvironment().down(2);
        `value().visit(getEnvironment());
        getEnvironment().up();
        `rationalnumber.value = `integralpart.value + `fractionalpart.value;
      }
    }
  }


  %strategy scale() extends Identity() {
    visit Bit {
      bit -> {
        Integer index = getEnvironment().getSubOmega();
        getEnvironment().up();
        if (getEnvironment().getSubject() instanceof SingularBitList) {      
          //eq SingularBitList.getBit().scale() = scale();
          SingularBitList singularbitlist = (SingularBitList) getEnvironment().getSubject();
          `scale().visit(getEnvironment());
          `bit.scale = singularbitlist.scale;
          getEnvironment().down(1);
        } else {
          //eq PluralBitList.getBit().scale() = scale();
          if (getEnvironment().getSubject() instanceof PluralBitList) {      
            PluralBitList pluralbitlist = (PluralBitList) getEnvironment().getSubject();
            `scale().visit(getEnvironment());
            `bit.scale = pluralbitlist.scale;
            getEnvironment().down(2);
          } else {
            getEnvironment().down(index);
          }
        }
      }
    }

    visit BitList {
      bitlist  -> {
        Integer index = getEnvironment().getSubOmega();
        getEnvironment().up();
        if (getEnvironment().getSubject() instanceof PluralBitList) {      
          //eq PluralBitList.getBitList().scale() = scale() + 1;
          PluralBitList pluralbitlist = (PluralBitList) getEnvironment().getSubject();
          `scale().visit(getEnvironment());
          `bitlist.scale = pluralbitlist.scale + 1;
          getEnvironment().down(1);
        } else {
          //eq IntegralNumber.getIntegralPart().scale() = 0;
          if (getEnvironment().getSubject() instanceof IntegralNumber) {      
            getEnvironment().down(1);
            `bitlist.scale = 0 ;
          } else {
            if (getEnvironment().getSubject() instanceof RationalNumber) {      
              if (index == 1) {
                //eq RationalNumber.getIntegralPart().scale() = 0;
                getEnvironment().down(1);
                `bitlist.scale = 0 ;
              } else {
                //eq RationalNumber.getFractionalPart().scale() = -getFractionalPart().length();
                getEnvironment().down(2);
                `length().visit(getEnvironment());
                `bitlist.scale = - `bitlist.length;
              } 
            } else {
              getEnvironment().down(index);
            }
          }
        }
      }
    }
  }


  %strategy length() extends Identity() {
    visit BitList {
      //eq SingularBitList.length() = 1;
      singularbitlist@SingularBitList[] -> {
        `singularbitlist.length = 1;
      }
      //eq PluralBitList.length() = getBitList().length() + 1;
      pluralbitlist@PluralBitList(bitlist,_) -> {
        getEnvironment().down(1);
        `length().visit(getEnvironment());
        getEnvironment().up();
        `pluralbitlist.length = `bitlist.length + 1;
      }
    }
  }

}
