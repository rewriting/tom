package Expression;

public class IntExp extends CstExp {
  public int value;

  public IntExp(int value) {
    this.value = value;
  }

  public String getOperator() {
      return "" + value;
  }
 
}
