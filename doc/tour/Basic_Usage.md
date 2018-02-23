---
title: Documentation:Basic Usage
permalink: /Documentation:Basic_Usage/
---

We provide here cut and paste examples, each of them illustrates one particular aspect of the tom language. Every code snippet comes with the command line to compile and execute it along with the associated program output.

Algebraic terms in Java -- %gom
===============================

``` tom
// import class.module.types.*  whith lower case names
import algebraic.logic.types.*;

public class Algebraic {

  %gom { // algebraic signature definition
    module Logic
      imports int String
      abstract syntax

      /* P, Q and implies are 'constructors'
         for the 'sort' Proposition */
      Proposition = P(t:Term)
                  | Q(t:Term)
                  | implies(p1:Proposition, p2:Proposition)

      Term = nat(i:int)
           | var(name:String)
           | plus(t1:Term, t2:Term)

  }

  public static void main(String[] args) {
    // ` (backquote) allows to build algebraic terms
    Proposition p = `implies(P(plus(nat(1),nat(2))), Q(var("x")));
    System.out.println(p);

    // constant time equality check thanks to maximal sharing
    Proposition p1 = `Q(nat(3));
    Proposition p2 = `Q(nat(3));
    System.out.println(p1 == p2);
  }
}
```

**`tomuser@huggy$`**` tom Algebraic.t && javac Algebraic.java && java Algebraic`
`implies(P(plus(nat(1),nat(2))),Q(var(`“`x`”`)))`
`true`

A pretty printer -- %match
==========================

``` tom
import matching.logic.types.*;

public class Matching {

  %gom {
    module Logic
      imports int String
      abstract syntax

      Proposition = P(t:Term)
                  | Q(t:Term)
                  | implies(p1:Proposition, p2:Proposition)

      Term = nat(i:int)
           | var(name:String)
           | plus(t1:Term, t2:Term)

  }

  public static String prettyProposition(Proposition p) {
    // the sort of p is inferred out of the patterns
    %match(p) {
      // variable instances are accessed with `
      P(t)        -> { return "P(" + prettyTerm(`t) + ")"; }
      Q(var("x")) -> { System.out.println("I was here !"); }
      /* since the previous rule does not break the flow (with a return statement),
         tom still looks for matching patterns in their declaration order  */
      Q(t)        -> { return "Q(" + prettyTerm(`t) + ")"; }
      implies(p1,p2) -> { return "(" + prettyProposition(`p1)
                                 + " => " + prettyProposition(`p2) + ")"; }
    }
    return ""; // this case never occurs, but javac isn't aware of it
  }

  public static String prettyTerm(Term t) {
    %match(t) {
      nat(i) -> { return Integer.toString(`i); }
      var(x) -> { return `x; }
      plus(t1,t2) -> { return prettyTerm(`t1) + " + " + prettyTerm(`t2); }
    }
    return "";
  }

  public static void main(String[] args) {
    Proposition p = `implies(P(plus(nat(1),nat(2))), Q(var("x")));
    System.out.println(prettyProposition(`p));
  }
}
```

**`tomuser@huggy$`**` tom Matching.t && javac Matching.java && java Matching`
`I was here !`
`(P(1 + 2) => Q(x))`

Antipatterns
============

``` tom
import anti.cars.types.*;

public class Anti {

  %gom {
    module Cars
      abstract syntax

      Vehicle = car(interior:Colors, exterior:Colors, type:Type)
              | bike()

      Type = ecological()
           | polluting()

      Colors = red()
             | green()
  }

  /* anti-pattern matching works just like pattern matching
     except that we may introduce complement symbols, denoted by '!',
     in the patterns
      */
  private static void searchCars(Vehicle subject){
    %match(subject){
      !car(x,x,_) -> {
        System.out.println(
            "- Not a car, or car with different colors:\t\t" + `subject);
      }
      car(x,!x,!ecological()) -> {
        System.out.println(
            "- Car that is not ecological and that does not\n"+
            "  have the same interior and exterior colors: \t\t" + `subject);
      }
      car(x@!green(),x,ecological()) -> {
        System.out.println(
            "- Ecological car and that has the same interior\n"+
            "  and exterior colors, but different from green: \t" + `subject);
      }
    }
  }

  public static void main(String[] args) {
    Vehicle veh1 = `bike();
    Vehicle veh2 = `car(red(),green(),ecological());
    Vehicle veh3 = `car(red(),red(),ecological());
    Vehicle veh4 = `car(green(),green(),ecological());
    Vehicle veh5 = `car(green(),red(),polluting());

    searchCars(veh1);
    searchCars(veh2);
    searchCars(veh3);
    searchCars(veh4);
    searchCars(veh5);
  }
}
```

**`tomuser@huggy$`**` tom Anti.t && javac Anti.java && java Anti`
`- Not a car, or car with different colors: bike()`
`- Not a car, or car with different colors: car(red(),green(),ecological())`
`- Ecological car and that has the same interior`
`  and exterior colors, but different from green: car(red(),red(),ecological())`
`- Not a car, or car with different colors: car(green(),red(),polluting())`
`- Car that is not ecological and that does not`
`  have the same interior and exterior colors: car(green(),red(),polluting())`

Computing a fixpoint -- %strategy, RepeatId
===========================================

``` tom
import rmdoubles.mylist.types.*;
import tom.library.sl.*; // imports the runtime strategy library

public class RmDoubles {

  // includes description of strategy operators for tom (RepeatId here)
  %include { sl.tom }

  %gom {
    module mylist
      imports  String
      abstract syntax

      StrList = strlist(String*)
  }

  /* we define a 'user strategy' which should be seen as
     a rewrite system (composed of only one rule here) */
  %strategy Remove() extends `Identity() {
    visit StrList { strlist(X*,i,Y*,i,Z*) -> { return `strlist(X*,i,Y*,Z*); } }
  }

  public static void main(String[] args) throws VisitFailure {
    StrList l = `strlist("framboisier","eric","framboisier","remi",
                         "remi","framboisier","rene","bernard");
    System.out.println(l);

    /* We compute a fixpoint for the rewrite system 'Remove'
       applied in head of l thanks to the strategy 'RepeatId'
       which takes another strategy (here Remove) as an
       argument. */
    System.out.println(`RepeatId(Remove()).visit(l));
  }
}
```

**`tomuser@huggy$`**` tom RmDoubles.t && javac RmDoubles.java && java RmDoubles`
`strlist(`“`framboisier`”`,`“`eric`”`,`“`framboisier`”`,`“`remi`”`,`“`remi`”`,`“`framboisier`”`,`“`rene`”`,`“`bernard`”`)`
`strlist(`“`framboisier`”`,`“`eric`”`,`“`remi`”`,`“`rene`”`,`“`bernard`”`)`

An expression evaluator -- %strategy, InnerMostId
=================================================

``` tom
import eval.mydsl.types.*;
import tom.library.sl.*;

public class Eval {

  %include { sl.tom }

  %gom {
    module mydsl
      imports int String
      abstract syntax

      Expr = val(v:int)
           | var(n:String)
           | plus(Expr*)
           | mult(Expr*)
  }

  /* EvalExpr is a rewrite system which simplifies expressions.
     It is meant to be applied on any sub-expression of a bigger one,
     not only in head. */
  %strategy EvalExpr() extends Identity() {
    visit Expr {
      plus(l1*,val(x),l2*,val(y),l3*) -> { return `plus(l1*,l2*,l3*,val(x + y)); }
      mult(l1*,val(x),l2*,val(y),l3*) -> { return `mult(l1*,l2*,l3*,val(x * y)); }
      plus(v@val(_)) -> { return `v; }
      mult(v@val(_)) -> { return `v; }
    }
  }

  public static void main(String[] args) throws VisitFailure {
    Expr e = `plus(val(2),var("x"),mult(val(3),val(4)),var("y"),val(5));

    /* We choose to apply the rewrite system in an innermost way
       (aka. call by value). The InnermostId strategy does the
       job and stops when a fixpoint is reached.
       Since the visit method of strategies returns a Visitable,
       we have to cast the result. */
    Expr res = (Expr) `InnermostId(EvalExpr()).visit(e);
    System.out.println(e + "\n" + res);
  }
}
```

**`tomuser@huggy$`**` tom Eval.t && javac Eval.java && java Eval`
`plus(val(2),var(`“`x`”`),mult(val(3),val(4)),var(`“`y`”`),val(5))`
`plus(var(`“`x`”`),var(`“`y`”`),val(19))`

Variable Collector -- %strategy with mutable parameters
=======================================================

``` tom
import collect.logic.types.*;
import tom.library.sl.*;
import java.util.LinkedList;
import java.util.HashSet;

public class Collect {

  %include { sl.tom }

  /* Since strategies arguments (Collection c here)
     have to be seen as algebraic terms by tom, we
     include the tom's java Collection description */
  %include { util/types/Collection.tom }

  %gom {
    module logic
      imports String
      abstract syntax

      Term = var(name:String)
           | f(t:Term)

      Prop = P(t: Term)
           | implies(l:Prop, r:Prop)
           | forall(name:String, p:Prop)
  }

  /* This strategy takes a java Collection as an argument
     and performs some side-effect (add). */
  %strategy CollectVars(Collection c) extends `Identity() {
    visit Term { v@var(_) -> { c.add(`v); } }
  }

  public static void main(String[] args) throws VisitFailure {
    Prop p = `forall("x", implies(implies(P(f(var("x"))), P(var("y"))),P(f(var("y")))));

    /* We choose an HashSet as a collection and we
       apply the collector to the proposition p
       in a TopDown way */
    HashSet result = new HashSet();
    `TopDown(CollectVars(result)).visit(p);
    System.out.println("vars: " + result);
  }
}
```

**`tomuser@huggy$`**` tom Collect.t && javac Collect.java && java Collect`
`vars: [var(`“`x`”`), var(`“`y`”`)]`

Collecting free variables -- mu operator, composed strategies
=============================================================

``` tom
import advanced.logic.types.*;
import tom.library.sl.*;
import java.util.LinkedList;

public class Advanced {

  %include { sl.tom }
  %include { util/types/Collection.tom }

  %gom {
    module logic
      imports String
      abstract syntax

      Term = var(name:String)
           | f(t:Term)

      Prop = P(t: Term)
           | implies(l:Prop, r:Prop)
           | forall(name:String, p:Prop)
  }

   /* - If the strategy encounters 'forall name, ...', then it
        returns the identity.
      - If it encounters 'name', then it adds its position to the collection c.
      - By default, it fails. (extends Fails) */
  %strategy CollectFree(String name, Collection c) extends Fail() {
    visit Prop { p@forall(x,_) -> { if(`x == name) return `p; } }
    visit Term { v@var(x)      -> { if(`x == name) c.add(getEnvironment().getPosition()); } }
  }

  private static LinkedList<Position>
    collectFreeOccurences(String name, Prop p) throws VisitFailure {
      LinkedList res = new LinkedList();
      /* This strategy means:
           - try to apply CollectFree on the current term
             - if it worked then stop here
             - else apply yourself (mu operator) on all the
               subterms of the current term */
      `mu(MuVar("x"), Choice(CollectFree(name,res),All(MuVar("x")))).visit(p);
      return res;
    }

  public static void main(String[] args) throws VisitFailure {
    Prop p = `forall("x", implies(implies(P(f(var("x"))), P(var("y"))),P(f(var("y")))));

    System.out.println("free occurences of x: " + collectFreeOccurences("x",p));
    System.out.println("free occurences of y: " + collectFreeOccurences("y",p));
  }
}
```

**`tomuser@huggy$`**` tom Advanced.t && javac Advanced.java && java Advanced`
`free occurences of x: []`
`free occurences of y: [[/2,_1,_2,_1],_[2,_2,_1,_1|2, 1, 2, 1], [2, 2, 1, 1]]`

[Category:Documentation](/Category:Documentation "wikilink")