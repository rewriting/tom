package Expression;

public class UnaryOperator extends Exp {
  public Exp first;
  public UnaryOperator(Exp first) {
    this.first = first;
  } 
  public String getOperator() { return ""; }
}
