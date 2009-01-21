package sa;

import java.util.*;
import sa.rule.types.*;

public class BottomRuleComparator implements Comparator<Rule>{
  public int compare(Rule r1, Rule r2){
    if(Pretty.toString(r1.getrhs(),true).compareTo("Bottom")==0 ){
      return -1;
    } else {
      if(Pretty.toString(r2.getrhs(),true).compareTo("Bottom")==0 ){
        return 1;
      } else {
        return r1.getlhs().toString().compareTo(r2.getlhs().toString());
      }
    }
  }
}
