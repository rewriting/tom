package expression;

public class StringExp extends CstExp {
  String value;

  public StringExp(String value) {
    this.value = value;
  }

    public String getOperator() {
      return value;
    }
}
