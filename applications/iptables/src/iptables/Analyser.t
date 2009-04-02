package iptables;

import iptables.ast.types.*;
import tom.library.sl.*; 
import java.util.*;

public class Analyser {

  %include {iptables/ast/Ast.tom}
  %include {sl.tom}
  
  public static void main(String[] args) {

    Rule rule = `rule(Accept(),Addr("4.2.3.40"),Addr("5.6.7.90"));

    Rules rules = 	`rules(
                           rule(Accept(),Addr("1.2.3.40"),Addr("5.6.7.80")),
                           rule(Accept(),Addr("4.2.3.40"),Addr("5.6.7.90"))
                           );

    %match(rule){
      rule(Accept(),Addr(src),Addr(dst)) -> {
        System.out.println("ACCEPT:" +`src + "->" + `dst);
      }
    }

    System.out.println(rules);
  }

}
