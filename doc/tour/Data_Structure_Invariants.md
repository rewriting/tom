---
title: Documentation:Data Structure Invariants
permalink: /Documentation:Data_Structure_Invariants/
---

In , data structure invariants are achieved via the Hook mechanism.

Sorted list -- maintaining invariants with rule-based hooks
===========================================================

``` tom
import rules.sortedlist.types.*;

public class Rules {

  %gom {
    module sortedlist
      imports int
      abstract syntax

      Integers = sorted(int*)

      /* we define a normalizing rewrite system
         for the module (only one rule here) */

      /* everytime a term with 'sorted' as an head symbol
         is constructed, the following conditional rewrite
         rule is applied, hence ensuring an invariant on
         the lists  */
      module sortedlist:rules() {
        sorted(x,y,t*) -> sorted(y,x,t*) if y <= x
      }
  }

  public static void main(String[] args) {
    Integers l1 = `sorted(7,5,3,1,9);
    Integers l2 = `sorted(8,4,6,2,0);
    Integers l3 = `sorted(l1*,10,l2*);
    System.out.println(l1 + "\n" + l2 + "\n" + l3);
  }
}
```

**`tomuser@huggy$`**` tom Rules.t && javac Rules.java && java Rules`
`sorted(1,3,5,7,9)`
`sorted(0,2,4,6,8)`
`sorted(0,1,2,3,4,5,6,7,8,9,10)`

Sorted list revisited -- advanced hooks
=======================================

``` tom
import sort.sortedlist.types.*;

public class Sort {

  %gom {
    module sortedlist
      imports int
      abstract syntax

      Integers = sorted(int*)

      /* this hook is triggered everytime a term with
         'sorted' as an head symbol is constructed */
      sorted:make_insert(e,l) {
        %match(l) {
          sorted(head,tail*) -> {
            /* The 'realMake' function constructs the
               term without calling the associated hook
               in order to avoid infinite loops. */
            if(e >= `head) {
              // hooks are pure tom-java so we allow any side effect
              System.out.println(e + " is greater than the head of " + `tail);
              return `realMake(head,sorted(e,tail*));
            }
          }
        }
      }
  }

  public static void main(String[] args) {
    Integers l = `sorted(7,5,3,1,9);
    System.out.println(l);
  }
}
```

**`tomuser@huggy$`**` tom Sort.t && javac Sort.java && java Sort`
`3 is greater than the head of sorted(9)`
`5 is greater than the head of sorted(3,9)`
`5 is greater than the head of sorted(9)`
`7 is greater than the head of sorted(3,5,9)`
`7 is greater than the head of sorted(5,9)`
`7 is greater than the head of sorted(9)`
`sorted(1,3,5,7,9)`

[Category:Documentation](/Category:Documentation "wikilink")