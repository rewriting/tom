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

  public String getName() {
    return this.name;
  }

  public static StrategyOperator getStrategyOperator(String name) {
    StrategyOperator op=NONE;
    if(name == "id") {
      op = IDENTITY;
    } else if(name == "fail") {
      op = FAIL;
    } else if(name == "rule") {
      op = RULE;
    } else if(name == "seq") {
      op = SEQ;
    } else if(name == "choice") {
      op = CHOICE;
    } else if(name == "mu") {
      op = MU;
    } else if(name == "all") {
      op = ALL;
    } else if(name == "one") {
      op = ONE;
    } else {
      op = NONE;
    }
    return op;
    /*
    switch(name) {
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
    */
  }

}
