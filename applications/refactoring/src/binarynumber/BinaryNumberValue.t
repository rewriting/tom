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

  static class Context {
    public HashMap<Position,Double> values = new HashMap();
    public HashMap<Position,Integer> scales = new HashMap();
    public HashMap<Position,Integer> lengths = new HashMap();
    public Context() {}
  }

  %typeterm Context {
    implement { Context }
  }

  public static Double value(BinaryNumber n) {
    Context context = new Context();
    try {
      `value(context).visit(n);
      return context.values.get(new Position());
    } catch (VisitFailure v) {
      throw new RuntimeException("Unexpected VisitFailure");
    }
  }

  %strategy value(context:Context) extends Identity() {
    visit Bit {
      // eq Zero.value() = 0;
      Zero() -> {
        context.values.put(getPosition(),new Double(0));
      }
      //eq OneB.value() = java.lang.Math.pow(2.0, scale());
      OneB() -> {
        `scale(context).visit(getEnvironment());  
        Double d = new Double(context.scales.get(getPosition()));
        context.values.put(getPosition(),java.lang.Math.pow(2.0, d));
      }
    }

    visit BitList {
      //eq SingularBitList.value() = getBit().value();
      SingularBitList(bit) -> {
        getEnvironment().down(1);
        `value(context).visit(getEnvironment());
        Double d = context.values.get(getPosition());
        getEnvironment().up();
        context.values.put(getPosition(),d);
      }
      //eq PluralBitList.value() = getBit().value() + getBitList().value();
      PluralBitList(bitList,bit) -> {
        getEnvironment().down(1);
        `value(context).visit(getEnvironment());
        Double d1 = context.values.get(getPosition());
        getEnvironment().up();
        getEnvironment().down(2);
        `value(context).visit(getEnvironment());
        Double d2 = context.values.get(getPosition());
        getEnvironment().up();
        context.values.put(getPosition(),d1+d2);
      }
    }

    visit BinaryNumber {
      //eq IntegralNumber.value() = getIntegralPart().value();
      IntegralNumber(IntegralPart) -> {
        getEnvironment().down(1);
        `value(context).visit(getEnvironment());
        Double d = context.values.get(getPosition());
        getEnvironment().up();
        context.values.put(getPosition(),d);
      }

      //eq RationalNumber.value() = getIntegralPart().value() + getFractionalPart().value();
      RationalNumber(IntegralPart,FractionalPart) -> {
        getEnvironment().down(1);
        `value(context).visit(getEnvironment());
        Double d1 = context.values.get(getPosition());
        getEnvironment().up();
        getEnvironment().down(2);
        `value(context).visit(getEnvironment());
        Double d2 = context.values.get(getPosition());
        getEnvironment().up();
        context.values.put(getPosition(),d1+d2);
      }
    }
  }


  %strategy scale(context:Context) extends Identity() {
    visit Bit {
      x -> {
        Integer index = getEnvironment().getSubOmega();
        getEnvironment().up();
        if (getEnvironment().getSubject() instanceof SingularBitList) {      
          //eq SingularBitList.getBit().scale() = scale();
          `scale(context).visit(getEnvironment());
          Integer i = context.scales.get(getPosition());
          getEnvironment().down(1);
          context.scales.put(getPosition(),i);
        } else {
          //eq PluralBitList.getBit().scale() = scale();
          if (getEnvironment().getSubject() instanceof PluralBitList) {      
            `scale(context).visit(getEnvironment());
            Integer i = context.scales.get(getPosition());
            getEnvironment().down(2);
            context.scales.put(getPosition(),i);
          } else {
            getEnvironment().down(index);
          }
        }
      }
    }

    visit BitList {
      x  -> {
        Integer index = getEnvironment().getSubOmega();
        getEnvironment().up();
        if (getEnvironment().getSubject() instanceof PluralBitList) {      
          //eq PluralBitList.getBitList().scale() = scale() + 1;
          `scale(context).visit(getEnvironment());
          Integer i = context.scales.get(getPosition());
          getEnvironment().down(1);
          context.scales.put(getPosition(),i+1);
        } else {
          //eq IntegralNumber.getIntegralPart().scale() = 0;
          if (getEnvironment().getSubject() instanceof IntegralNumber) {      
            getEnvironment().down(1);
            context.scales.put(getPosition(),0);
          } else {
            if (getEnvironment().getSubject() instanceof RationalNumber) {      
              if (index == 1) {
                //eq RationalNumber.getIntegralPart().scale() = 0;
                getEnvironment().down(1);
                context.scales.put(getPosition(),0);
              } else {
                //eq RationalNumber.getFractionalPart().scale() = -getFractionalPart().length();
                getEnvironment().down(2);
                `length(context).visit(getEnvironment());
                Integer i = context.lengths.get(getPosition());
                context.scales.put(getPosition(),-i);
              } 
            } else {
              getEnvironment().down(index);
            }
          }
        }
      }
    }

  }


  %strategy length(context:Context) extends Identity() {
    visit BitList {
      //eq SingularBitList.length() = 1;
      SingularBitList[] -> {
        context.lengths.put(getPosition(),1);
      }
      //eq PluralBitList.length() = getBitList().length() + 1;
      PluralBitList(bitList,bit) -> {
        getEnvironment().down(1);
        `length(context).visit(getEnvironment());
        Integer i = context.lengths.get(getPosition());
        getEnvironment().up();
        context.lengths.put(getPosition(),i+1);
      }
    }
  }

}
