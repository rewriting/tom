package sa;

import java.util.*;
import sa.rule.types.*;

public class MyRuleComparator implements Comparator<Rule>{
  public int compare(Rule r1, Rule r2){
    return r1.getlhs().toString().compareTo(r2.getlhs().toString());
  }
}
