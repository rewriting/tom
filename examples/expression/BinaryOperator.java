package expression;

public class BinaryOperator extends Exp {
  public Exp first;
  public Exp second;
  public BinaryOperator(Exp first, Exp second) {
    this.first = first;
    this.second = second;
  }
  public String getOperator() { return ""; }
}
