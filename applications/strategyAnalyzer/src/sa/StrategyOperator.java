package sa;

public enum StrategyOperator{
  NONE("none"), 
  IDENTITY("id"), 
  FAIL("fail"), 
  RULE("rule"), 
  SEQ("seq"), 
  CHOICE("choice"), 
  MU("mu"), 
  ALL("all"), 
  ONE("one");

  private final String name;

  private StrategyOperator(String name) {
    this.name = name;
  }

  public String getName(){
    return this.name;
  }

  public static StrategyOperator getStrategyOperator(String name){
    StrategyOperator op=NONE;
    switch (name){
      case "id":
        op = IDENTITY;
        break;
      case "fail":
        op = FAIL;
        break;
      case "rule":
        op = RULE;
        break;
      case "seq":
        op = SEQ;
        break;
      case "choice":
        op = CHOICE;
        break;
      case "mu":
        op = MU;
        break;
      case "all":
        op = ALL;
        break;
      case "one":
        op = ONE;
        break;
      default:
        op = NONE;
        break;
      }
    return op;
  }
}
