---
title: Documentation:Language Basics – Level 3
permalink: /Documentation:Language_Basics_–_Level_3/
---

Introduction to strategies
==========================

Elementary transformation
-------------------------

We call *strategy* an elementary transformation. Suppose that you want to transform the object `a()` into the object `b()`. You can of course use all the functionalities provided by and . But in that case, you will certainly end in mixing the *transformation* (the piece of code that really replaces `a()` by `b()`) with the *control* (the part that is executed in order to perform the transformation).

The notion of strategy is a clear separation between *control* and *transformation*. In our case, we will define a strategy named `Trans1` that only describes the transformation we have in mind:

``` tom
import main.example.types.*;
import tom.library.sl.*;
  public class Main {
    %gom {
      module Example
      abstract syntax
      Term = a() | b() | f(x:Term)
    }
    %include { sl.tom }
    public final static void main(String[] args) {
      try {
        Term t1 = `a();
        Term t2 = (Term) `Trans1().visit(t1);
        System.out.println("t2 = " + t2);
      } catch(VisitFailure e) {
        System.out.println("the strategy failed");
      }
    }
    %strategy Trans1() extends Fail() {
      visit Term {
        a() -> b()
      }
    }
}
```

There exists three kinds of elementary strategy: `Fail`, which always fails, `Identity`, which always succeeds, and transformation rules of the form *l* → *r*.

Therefore, if we consider the elementary strategy `a ⇒b` (which replaces `a` by `b`), we have the following results:

`(a -> b)[a]      = b`
`(a -> b)[b]      = failure`
`(a -> b)[f(a)]   = failure`
`(Identity)[a]    = a`
`(Identity)[b]    = b`
`(Identity)[f(a)] = f(a)`
`(Fail)[a]        = failure`

Basic combinators
-----------------

### Composition

The sequential operator, `Sequence(S1,S2)`, applies the strategy `S1`, and then the strategy `S2`. It fails if either `S1` fails, or `S2` fails.

`(Sequence(a -> b, b -> c))[a] = c`
`(Sequence(a -> b, c -> d))[a] = failure`
`(Sequence(b -> c, a -> b))[a] = failure`

### Choice

The choice operator, `Choice(S1,S2)`, applies the strategy `S1`. If the application `S1` fails, it applies the strategy `S2`. Therefore, `Choice(S1,S2)` fails if both `S1` and `S2` fail.

`(Choice(a -> b, b -> c))[a]   = b`
`(Choice(b -> c, a -> b))[a]   = b`
`(Choice(b -> c, c -> d))[a]   = failure`
`(Choice(b -> c, Identity))[a] = a`

### Not

The strategy `Not(S)`, applies the strategy and fails when `S` succeeds. Otherwise, it succeeds and corresponds to the `Identity`.

`(Not(a -> b))[a]   = failure`
`(Not(b -> c))[a]   = a`

Parameterized strategies
------------------------

By combining basic combinators, more complex strategies can be defined. To make the definitions generic, parameters can be used. For example, we can define the two following strategies:

-   `Try(S) = Choice(S, Identity)`, which tries to apply `S`, but never fails
-   `Repeat(S) = Try(Sequence(S, Repeat(S)))`, which applies recursively `S` until it fails, and then returns the last unfailing result

`(Try(b -> c))[a]                    = a`
`(Repeat(a -> b))[a]                 = b`
`(Repeat(Choice(b -> c, a -> b)))[a] = c`
`(Repeat(b -> c))[a]                 = a`

Traversal strategies
--------------------

We consider two kinds of traversal strategy (`All(S)` and `One(S)`). The first one applies `S` to all subterms, whereas the second one applies `S` to only one subterm.

### All

The application of the strategy `All(S)` to a term `t` applies `S` on each immediate subterm of `t`. The strategy `All(S)` fails if `S` fails on at least one immediate subterm.

`(All(a -> b))[f(a)]        = f(b)`
`(All(a -> b))[g(a,a)]      = g(b,b)`
`(All(a -> b))[g(a,b)]      = failure`
`(All(a -> b))[a]           = a`
`(All(Try(a -> b)))[g(a,c)] = g(b,c)`

### One

The application of the strategy `One(S)` to a term `t` tries to apply `S` on an immediate subterm of `t`. The strategy `One(S)` succeeds if there is a subterm such that `S` can be applied. The subterms are tried from left to right.

`(One(a -> b))[f(a)]   = f(b)`
`(One(a -> b))[g(a,a)] = g(b,a)`
`(One(a -> b))[g(b,a)] = g(b,b)`
`(One(a -> b))[a]      = failure`

High level strategies
---------------------

By combining the previously mentioned constructs, it becomes possible to define well know strategies:

`BottomUp(S)     = Sequence(All(BottomUp(S)), S)`
`TopDown(S)      = Sequence(S, All(TopDown(S)))`
`OnceBottomUp(S) = Choice(One(OnceBottomUp(S)), S)`
`OnceTopDown(S)  = Choice(S, One(OnceTopDown(S)))`
`Innermost(S)    = Repeat(OnceBottomUp(S))`
`Outermost(S)    = Repeat(OnceTopDown(S))`

Strategies in practice
======================

Let us consider again a Pico language whose syntax is a bit simpler than the one seen in section [Level 1](/Documentation:Language_Basics_–_Level_1#Programming_in_Tom "wikilink").

``` tom
import pico2.term.types.*;
import java.util.*;
import tom.library.sl.*;
  class Pico2 {
  %include { sl.tom }
    %gom {
      module Term
      imports int String
      abstract syntax
      Bool = True()
           | False()
           | Neg(b:Bool)
           | Or(b1:Bool, b2:Bool)
           | And(b1:Bool, b2:Bool)
           | Eq(e1:Expr, e2:Expr)
      Expr = Var(name:String)
           | Cst(val:int)
           | Let(name:String, e:Expr, body:Expr)
           | Seq( Expr* )
           | If(cond:Bool, e1:Expr, e2:Expr)
           | Print(e:Expr)
           | Plus(e1:Expr, e2:Expr)
    }
  ...
}
```

As an exercise, we want to write an optimization function that replaces an instruction of the form `If(Neg(b),i1,i2)` by a simpler one: `If(b,i2,i1)`. A possible implementation is:

``` tom
public static Expr opti(Expr expr) {
  %match(expr) {
    If(Neg(b),i1,i2) -> { return `opti(If(b,i2,i1)); }
    x -> { return `x; }
  }
  throw new RuntimeException("strange term: " + expr);
}
public final static void main(String[] args) {
  Expr p4 = `Let("i",Cst(0),
               If(Neg(Eq(Var("i"),Cst(10))),
                  Seq(Print(Var("i")), Let("i",Plus(Var("i"),Cst(1)),Var("i"))),
                  Seq()));
  System.out.println("p4       = " + p4);
  System.out.println("opti(p4) = " + opti(p4));
}
```

When executing this program, we obtain:

``` tom
p4       = Let("i",Cst(0),If(Neg(Eq(Var("i"),Cst(10))),
           ConsSeq(Print(Var("i")),ConsSeq(Let("i",
           Plus(Var("i"),Cst(1)),Var("i")),Seq())),Seq()))
opti(p4) = Let("i",Cst(0),If(Neg(Eq(Var("i"),Cst(10))),
           ConsSeq(Print(Var("i")),ConsSeq(Let("i",
           Plus(Var("i"),Cst(1)),Var("i")),Seq())),Seq()))
```

This does not correspond to the expected result, simply because the `opti` function performs an optimization when the expression starts with an `If` instruction. To get the expected behavior, we have to add congruence rules that will allow to apply the rule in subterms (one rule for each constructor):

``` tom
public static Expr opti(Expr expr) {
  %match(expr) {
    If(Neg(b),i1,i2) -> { return `opti(If(b,i2,i1)); }
    // congruence rules
    Let(n,e1,e2)     -> { return `Let(n,opti(e1),opti(e2)); }
    Seq(head,tail*)  -> { return `Seq(opti(head),opti(tail*)); }
    If(b,i1,i2)      -> { return `If(b,opti(i1),opti(i2)); }
    Print(e)         -> { return `Print(e); }
    Plus(e1,e2)      -> { return `Plus(e1,e2); }
    x -> { return `x; }
  }
  throw new RuntimeException("strange term: " + expr);
}
```

Since this is not very convenient, we will show how the use of strategies can simplify this task.

Printing constants using a strategy
-----------------------------------

Let us start with a very simple task which consists in printing all the nodes that corresponds to a constant (`Cst(_)`. To do that, we have to define an elementary strategy that is successful when it is applied on a node `Cst(_)`:

``` tom
%strategy stratPrintCst() extends Fail() {
  visit Expr {
    Cst(x) -> { System.out.println("cst: " + `x); }
  }
}
```

To traverse the program and print all `Cst` nodes, a `TopDown` strategy can be applied:

``` tom
public static void printCst(Expr expr) {
  try {
    `TopDown(Try(stratPrintCst())).visit(expr);
  } catch (VisitFailure e) {
    System.out.println("strategy failed");
  }
}
public final static void main(String[] args) {
  ...
  System.out.println("p4 = " + p4);
  printCst(p4);
}
```

This results in:

`p4 = Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`i`”`),Cst(10))),`
`     ConsSeq(Print(Var(`“`i`”`)),ConsSeq(Let(`“`i`”`,`
`     Plus(Var(`“`i`”`),Cst(1)),Var(`“`i`”`)),Seq())),Seq()))`
`cst: 0`
`cst: 10`
`cst: 1`

Combining elementary strategies
-------------------------------

As a second exercise, we will try to write another strategy that performs the same task, but we will try to separate the strategy that looks for a constant from the strategy that prints a node. So, let us define these two strategies:

``` tom
%strategy FindCst() extends Fail() {
  visit Expr {
    c@Cst(x) -> c
  }
}
%strategy PrintTree() extends Identity() {
  visit Expr {
    x -> { System.out.println(`x); }
  }
}
```

Similarly to `stratPrintCst`, the strategy `FindCst` extends `Fail`. The goal of the `PrintTree` strategy is to print a node of sort `Expr`. By extending `Identity`, we specify the default behavior when the strategy is applied on a term of a different sort.

To print the node `Cst`, we have to look for a `Cst` and print this node. This can be done by combining, using a `Sequence`, the two strategies `FindCst` and `PrintTree`:

``` tom
public static void printCst(Expr expr) {
  try {
    `TopDown(Try(stratPrintCst())).visit(expr);
    `TopDown(Try(Sequence(FindCst(),PrintTree()))).visit(expr);
  } catch (VisitFailure e) {
    System.out.println("strategy failed");
  }
}
```

This results in:

`cst: 0`
`cst: 10`
`cst: 1`
`Cst(0)`
`Cst(10)`
`Cst(1)`

Modifying a subterm
-------------------

Here, we will try to rename all the variables from a given program: the name should be modified into `_name`.

To achieve this task, you can define a primitive strategy that performs the modification, and apply it using a strategy such as `TopDown`:

``` tom
%strategy stratRenameVar() extends Fail() {
  visit Expr {
    Var(name) -> { return `Var("_"+name); }
  }
}
public static void optimize(Expr expr) {
  try {
    `Sequence(TopDown(Try(stratRenameVar())),PrintTree()).visit(expr);
  } catch (VisitFailure e) {
    System.out.println("strategy failed");
  }
}
```

The application of `optimize` to `p4` results in:

`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`_i`”`),Cst(10))),`
`  ConsSeq(Print(Var(`“`_i`”`)),ConsSeq(Let(`“`i`”`,`
`  Plus(Var(`“`_i`”`),Cst(1)),Var(`“`_i`”`)),Seq())),Seq()))`

Suppose now that we want to print the intermediate steps: we do not want to perform all the replacements in one step, but for debugging purpose, we want to print the intermediate term after each application of the renaming rule.

The solution consists in combining the `stratRenameVar` strategy with the `PrintTree` strategy.

A first solution consists in applying `stratRenameVar` using a `OnceBottomUp` strategy, and immediately apply `PrintTree` on the resulting term. This could be implemented as follows:

``` tom
try {
  `Repeat(Sequence(OnceBottomUp(stratRenameVar()),PrintTree())).visit(p4);
} catch (Exception e) {
  e.printStackTrace();
}
```

Unfortunately, this results in:

`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`_i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`__i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`___i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`____i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`_____i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`______i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`_______i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`________i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`_________i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`__________i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`___________i`”`),Cst(10))),...`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`____________i`”`),Cst(10))),...`
`...`

This is not the expected behavior! Why?

Simply because the renaming rule can be applied several times on a same variable. To fix this problem, we have to apply the renaming rule only if the considered variable has not already be renamed.

To know if a variable has been renamed, you just have to define an elementary strategy, called `RenamedVar`, that succeeds when the name of the variable starts with an underscore. This can be easily implemented using string matching capabilities:

``` tom
%strategy RenamedVar() extends Fail() {
  visit Expr {
    v@Var(concString('_',name*)) -> v
  }
}
```

To finish our implementation, it is sufficient to apply `stratRenameVar` only when `RenamedVar` fails, i.e., when `Not(RenamedVar)` succeeds.

``` tom
`Repeat(Sequence(
      OnceBottomUp(Sequence(Not(RenamedVar()),stratRenameVar())),
      PrintTree())
    ).visit(p4);
```

This results in (layouts have been added to improve readability):

`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`_i`”`),Cst(10))),`
`  ConsSeq(Print(Var(`“`i`”`)),ConsSeq(Let(`“`i`”`,`
`  Plus(Var(`“`i`”`),Cst(1)),Var(`“`i`”`)),Seq())),Seq()))`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`_i`”`),Cst(10))),`
`  ConsSeq(Print(Var(`“`_i`”`)),ConsSeq(Let(`“`i`”`,`
`  Plus(Var(`“`i`”`),Cst(1)),Var(`“`i`”`)),Seq())),Seq()))`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`_i`”`),Cst(10))),`
`  ConsSeq(Print(Var(`“`_i`”`)),ConsSeq(Let(`“`i`”`,`
`  Plus(Var(`“`_i`”`),Cst(1)),Var(`“`i`”`)),Seq())),Seq()))`
`Let(`“`i`”`,Cst(0),If(Neg(Eq(Var(`“`_i`”`),Cst(10))),`
`  ConsSeq(Print(Var(`“`_i`”`)),ConsSeq(Let(`“`i`”`,`
`  Plus(Var(`“`_i`”`),Cst(1)),Var(`“`_i`”`)),Seq())),Seq()))`

Re-implementing the tiny optimizer
----------------------------------

Now that you know how to use strategies, it should be easy to implement the tiny optimizer seen in the beginning of section [Level 3](/Documentation:Language_Basics_–_Level_3#Strategies_in_practice "wikilink").

You just have to define the transformation rule and a strategy that will apply the rule in an innermost way:

``` tom
%strategy OptIf() extends Fail() {
  visit Expr {
    If(Neg(b),i1,i2) -> If(b,i2,i1)
  }
}
public static void optimize(Expr expr) {
  try {
    `Sequence(Innermost(OptIf()),PrintTree()).visit(expr);
  } catch (VisitFailure e) {
    System.out.println("strategy failed");
  }
}
```

Applied to the program `p4`, as expected this results in:

`Let(`“`i`”`,Cst(0),If(Eq(Var(`“`i`”`),Cst(10)),Seq(),`
`  ConsSeq(Print(Var(`“`i`”`)),ConsSeq(Let(`“`i`”`,`
`  Plus(Var(`“`i`”`),Cst(1)),Var(`“`i`”`)),Seq()))))`

Anti pattern-matching
=====================

Tom patterns support the use of complements, called anti-patterns. In other words, it is possible to specify what you *don’t want* to match. This is done via the <font color="blue">‘</font><font color="blue">`!`</font><font color="blue">’</font> symbol, according to the grammar defined in the language reference.

If we consider the Gom signature

``` tom
import list1.list.types.*;
public class List1 {
  %gom {
    module List
    abstract syntax
    E = a()
      | b()
      | c()
      | f(x1:E, x2:E)
      | g(x3:E)
    L = List( E* )
  }
  ...
}
```

a very simple use of the anti-patterns would be

``` tom
...
%match(subject) {
  !a() -> {
    System.out.println("The subject is different from 'a'");
  }
}
...
```

The <font color="blue">‘</font><font color="blue">`!`</font><font color="blue">’</font> symbols can be nested, and therefore more complicated examples can be generated:

``` tom
...
%match(subject) {
  f(a(),!b()) -> {
    // matches an f which has x1=a() and x2!=b()
  }
  f(!a(),!b()) -> {
    // matches an f which has x1!=a() and x2!=b()
  }
  !f(a(),!b()) -> {
    // matches either something different from f(a(),_) or f(a(),b())
  }
  !f(x,x) -> {
    // matches either something different from f, or an f with x1 != x2
  }
  f(x,!x) -> {
    // matches an f which has x1 != x2
  }
  f(x,!g(x)) -> {
    // matches an f which has either x2!=g or x2=g(y) with y != x1
  }
}
...
```

The anti-patterns can be also quite useful when used with lists. Imagine that you what to search for a list that doesn’t contain a specific element. Without the use of anti-patterns, you would be forced to match with a variable instead of the element you don’t want, and after that to perform a test in the action part to check the contents of the variable. For the signature defined above, if we are looking for a list that doesn’t contain `a()`, using the anti-patterns we would write:

``` tom
...
%match(listSubject) {
  !List(_*,a(),_*) -> {
    System.out.println("The list doesn't contain 'a'");
  }
}
...
```

Please note that this is different from writing

``` tom
...
%match(listSubject) {
  List(_*,!a(),_*) -> {
    // at least an element different from a()
  }
}
...
```

which would match a list that has at least one element different from `a()`.

Some more useful anti-patterns can be imagined in the case of lists:

``` tom
...
%match(listSubject) {
  !List(_*,!a(),_*) -> {
    // matches a list that contains only 'a()'
  }
  !List(X*,X*) -> {
    // matches a non-symmetrical list
  }
  !List(_*,x,_*,x,_*) -> {
    // matches a list that has all the elements distinct
  }
  List(_*,x,_*,!x,_*) -> {
    // matches a list that has at least two elements that are distinct
  }
}
...
```

Using constraints for more flexibility
======================================

Since version 2.6 of the language, a more modular syntax for `%match` and `%strategy` is proposed. Instead of having a pattern (or several for more subjects) as the left-hand side of rules in a `%match` or `%strategy`, now more complex conditions can be used.

Let’s consider the following class that includes a Gom signature:

``` tom
import constraints.example.types.*;
public class Constraints {
  %gom {
    module Example
    abstract syntax
    E = a()
      | b()
      | c()
      | f(x1:E, x2:E)
      | g(x3:E)
      L = List( E* )
  }
  ...
}
```

Let’s now suppose that we have the following `%match` construct:

``` tom
...
%match(subject) {
  f(x,y) -> {
      boolean flag = false;
      %match(y){
        g(a())     -> { flag = true; }
        f(a(),b()) -> { flag = true; }
      }
      if (flag) { /* some action */ }
    }
    g(_) -> { System.out.println("a g(_)"); }
}
...
```

This is a very basic example where we want to check if the subject is an `f` with the second sub-term either `g(a())` or `f(a(),b())`. If it is the case, we want to perform an action.

Using the new syntax of the `%match` construct (detailed in the language reference), we could write the following equivalent code:

``` tom
...
%match(subject) {
  f(x,y) && ( g(a()) << y || f(a(),b()) << y ) -> { /* some action */ }
  g(_) -> { System.out.println("g()"); }
}
...
```

The `%match` construct can be also used without any parameters. The following three constructs are all equivalent:

``` tom
...
%match(subject1, subject2) {
  p1,p2 -> { /* action 1 */ }
  p3,p4 -> { /* action 2 */ }
}
...
...
%match(subject1) {
  p1 && p2 << subject2 -> { /* action 1 */ }
  p3 && p4 << subject2 -> { /* action 2 */ }
}
...
...
%match {
  p1 << subject1 && p2 << subject2 -> { /* action 1 */ }
  p3 << subject1 && p4 << subject2 -> { /* action 2 */ }
}
...
```

A big advantage of the this approach compared to the classical one is its flexibility. The right-hand side of a match constraint can be a term built on variables coming from the left-hand sides of other constraints, as we saw in the first example of this section. A more advanced example could verify for instance that a list only contains two occurrences of an object:

``` tom
...
%match(sList) {
  List(X*,a(),Y*,a(),Z*) && !List(_*,a(),_*) << List(X*,Y*,Z*) -> {
    System.out.println("Only two objects a()");
  }
}
...
```

In the above example, the first pattern checks that the subject contains two objects `a()`, and the second constraint verifies that the rest of the list doesn’t contain any `a()`.

Besides match constraints introduced with the symbol `<<`, we ca also have *boolean* constraints by using the following operators: `>`, `>=`, `<`, `<=`, `==` and `!=`. These can be used between any terms, and are trivially translated into host code (this means that the constraint `term1 == term2` will correspond exactly to an `if (term1 == term2) ...` in the generated code). A simple example is the following one, which prints all the elements in a list of integers that are bigger than `5`:

``` tom
...
%gom {
  module Example
  imports int
  abstract syntax
  Lst = intList( int* )
}
Lst sList = `intList(6,7,4,5,3,2,8,9);
%match(sList) {
  intList(_*,x,_*) && x > 5 -> { System.out.println(`x);
  }
}
...
```

[Category:Documentation](/Category:Documentation "wikilink")