package subtypeinference;

public class Problem5 {

	public static void main(String[] args) {
	}

    // ------------------------------------------------------------
  abstract class Exp {
    public abstract String getOperator();
  }
   
  class CstExp extends Exp {
    public Object value;
    public CstExp(Object value) {
      this.value = value;
    }
    public String getOperator() {
      return "" + value;
    }
  }

  class IntExp extends CstExp {
    public IntExp(int value) {
      super(new Integer(value));
    }
  }
  
  class BinaryOperator extends Exp {
    public Exp first;
    public Exp second;
    public BinaryOperator(Exp first, Exp second) {
      this.first = first;
      this.second = second;
    }
    public String getOperator() { return ""; }
  }
  
  class Plus extends BinaryOperator {
    public Plus(Exp first, Exp second) {
      super(first,second);
    }
    public String getOperator() { return "Plus"; }
  }


    // ------------------------------------------------------------
  
  %typeterm TomExp {
    implement { Exp }
    is_sort(t) { $t instanceof Exp }
  }

  %typeterm TomBinaryOperator extends TomExp {
    implement { BinaryOperator }
    is_sort(t) { $t instanceof BinaryOperator }
  }

  %typeterm TomCstExp extends TomExp {
    implement { CstExp }
    is_sort(t) { $t instanceof CstExp }
  }
  
  %typeterm TomInteger {
    implement { Integer }
    is_sort(t) { $t instanceof Integer }
  }

    // ------------------------------------------------------------
  %op TomBinaryOperator Plus(first:TomExp, second:TomExp) {
    is_fsym(t) { $t instanceof Plus }
    get_slot(first,t) { ((Plus)$t).first }
    get_slot(second,t) { ((Plus)$t).second }
  }
  
  %op TomCstExp IntExp(value:TomInteger) {
    is_fsym(t) { $t instanceof IntExp }
    get_slot(value,t) { ((Integer)((IntExp)$t).value) }
  }

  public Exp simplify(Exp t) {
    %match(t) {
      //IntExp(v1) -> {
      //  return t;
      //}

      Plus[first=IntExp(v1), second=IntExp(v2)] -> {
        return new IntExp(`v1.intValue() + `v2.intValue());
      }
    }

    return t;
  }

}

