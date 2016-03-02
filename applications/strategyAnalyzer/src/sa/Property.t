package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;
import com.google.common.collect.HashMultiset;

public class Property {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/Collection.tom }
  %include { java/util/types/Map.tom }
  %include { java/util/types/HashSet.tom }
  %include { java/util/types/Set.tom }

  %typeterm Signature { implement { Signature }}
  %typeterm HashMultiset { implement { HashMultiset }}

  /*
   * predicates for assert
   */

  /*
   * returns true if the term does not contain any Add or Sub
   */
  public static boolean isPlainTerm(Term t) {
    try {
      `TopDown(PlainTerm()).visitLight(t);
    } catch(VisitFailure e) {
      return false;
    }
    return true;
  }

  %strategy PlainTerm() extends Identity() {
    visit Term {
      t@(Add|Sub)[] -> {
        `Fail().visitLight(`t);
      }
    }

  }

  /*
   * returns true if contains no Add/Sub (except at top level)
   */
  public static boolean onlyTopLevelAdd(Term subject) {
    %match(subject) {
      Add(tl) -> {
        boolean res = true;
        for(Term t:`tl.getCollectionConcAdd()) {
          res &= isPlainTerm(t);
        }
        return res;
      }
      Sub[] -> {
        return false;
      }
    }
    return true;
  }

  public static boolean isLinear(Term t) {
    HashMultiset<String> bag = Tools.collectVariableMultiplicity(t);
    for(String name:bag.elementSet()) {
      if(bag.count(name) > 1) {
        return false;
      }
    }
    return true;
  }

  public static boolean isLhsLinear(RuleList rules) {
    boolean res = true;
    for(Rule r: rules.getCollectionConcRule()) {
      res &= isLinear(r.getlhs());
    }
    return res;
  }
  
  public static boolean isLhsLinear(Trs trs) {
    boolean res = true;
    %match(trs) {
      (Trs|Otrs)(list) -> {
        res = isLhsLinear(`list);
      }
    }
    return res;
  }

  public static boolean containsSub(tom.library.sl.Visitable t) {
    try {
      `TopDown(ContainsSub()).visitLight(t);
      return false;
    } catch(VisitFailure e) {
      return true;
    }
  }

  %strategy ContainsSub() extends Identity() {
    visit Term {
      t@Sub(_,_) -> {
        `Fail().visitLight(`t);
      }
    }
  }

}
