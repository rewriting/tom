---
title: Documentation:Strategies
permalink: /Documentation:Strategies/
---

To exercise some control over the application of the rules, rewriting based languages provide abstract ways by using reflexivity and the meta-level for <font color="purple">Maude</font>, or the notion of rewriting strategies as in . Strategies such as `bottom-up`, `top-down` or `leftmost-innermost` are higher-order features that describe how rewrite rules should be applied (At each step, the strategy chooses which rule is applied and in which position inside the term).

In , we have developed a flexible and expressive strategy language inspired by <font color="purple">ELAN</font>, <font color="purple">Stratego</font>, and <font color="purple">JJTraveler</font> where high-level strategies are defined by combining low-level primitives. For example, the `top-down` strategy is recursively defined by `Sequence(s,All(TopDown(s)))`. Users can define *elementary strategies* (corresponding to a set of rewriting rules) and control them using various combinators proposed in the <font color="purple">sl</font> library. This rich strategy language allows to easily define various kinds of term traversals.

In this chapter, first, we briefly present the <font color="purple">sl</font> library functioning. After that, we describe in detail every elementary strategy and strategy combinator. Then, we explain how the <font color="purple">sl</font> library can be used in combination with structures.

Overview
========

The package `tom.library.sl` is mainly composed by an interface `Strategy` and a class for each strategy combinator.

Strategy interface
------------------

Every strategy (elementary strategy or combinator) implements the `Strategy` interface. To apply a strategy on a term (generally called the subject), this interface offers two methods:

-   `Visitable visitLight(Visitable any)` visits the subject `any` in a light way (without environment)
-   `Visitable visit(Visitable any)` visits the subject `any` and manages the environment

A strategy can visit any `Visitable` object. Any term constructed using a mapping or a signature is `Visitable`. The first method `visitLight` is the most efficient because it does not manage any environment. Most of time, it is sufficient. The second method depends on an [environment](/Documentation:Strategies#Environment "wikilink"). The `visit(Visitable)` method behaves like `visitLight` but updates at each step an environment.

When applied on a term, a strategy returns a `Visitable` corresponding to the result. In case of failures, these two methods throw a `VisitFailure` exception.

Environment
-----------

An environment is composed of the current position and the current subterm where the strategy is applied. This object corresponds to the class `Environment` of the package `tom.library.sl` and can be associated to a strategy using `setEnvironment(Environment)` and accessed using `getEnvironment()`. A position in a term is a sequence of integers that represents the path from the root of the term to the current subterm where the strategy is applied.

The method `getPosition()` in the `Environment` class returns a `Position` object that represents the current position. The method `getSubject()` allows users to get the current subterm where the strategy is applied.

To retrieve all this information from a strategy, the `visit` method is necessary.

Elementary strategies
=====================

Elementary strategies of <font color ="purple">sl</font>
--------------------------------------------------------

An elementary strategy corresponds to a minimal transformation. It could be *Identity* (does nothing), *Fail* (always fails), or a set of *rewrite rules* (performs an elementary rewrite step only at the root position). In our system, strategies are type-preserving and have a default behavior (introduced by the keyword `extends`)

The first two elementary strategies are the identity and the failure. There are defined by two corresponding classes in *sl library*: `Identity` and `Fail`. These two strategies have no effect on the term. The identity strategy returns the term unchanged. The failure strategy throws a `VisitFailure` exception.

``` tom
import tom.library.sl.*; // imports the runtime strategy library

public class SimplestStrategies {

  // includes description of strategy operators for tom (Identity here)
  %include { sl.tom }

  %gom { // signature
      module Term
        imports int
        abstract syntax

        Term = a()
             | b()
             | c()
             | f(x:Term)
             | g(x:Term,y:Term)

  }

  public static void main(String[] args) throws VisitFailure {

     System.out.println(`Identity().visit(`a()));

     System.out.println(`Fail().visit(`a()));
  }

}
```

Elementary strategies defined by users
--------------------------------------

Users can define elementary strategies by using the `%strategy` construction. This corresponds to a list of `MatchStatement` (one associated to each sort) and can be schematically seen as a set of rewriting rules.

Here is the `%strategy` grammar:

<div class="center">
|     |     |           |
|:----|:---:|:----------|
|     | ::= |  \[\]     |
|     |     | \[\]      |
|     | ::= |           |
|     | ::= |  ( )\*    |
|     |  |  |  ( )\*    |
|     | ::= | ( )\*     |
|     | ::= |  ( )\*    |
|     | ::= | \[\] ( ∣) |

</div>
This strategy has a name, followed by mandatory parenthesis. Inside these parenthesis, we have optional parameters.

The keyword defines the behavior of the default strategy that will be applied. In most cases, two default behaviors are used:

-   the failure: `Fail()`
-   the identity: `Identity()`

Note that this default behavior is executed if no rule can be applied or if there is no <font color="purple">Java</font> `return` statement executed in the applied rules.

The body of the strategy is a list of visited sorts. Each `StrategyVisit` contains a list of `VisitAction` that will be applied to the corresponding sort. A `VisitAction` is either a `PatternAction` or simply a `Term` (equivalent to the `VisitAction` `{ return Term; }`). In other words, a `StrategyVisit` is translated into a `MatchStatement`.

For instance, here is an elementary strategy `RewriteSystem` that can be instantiated as follows:

``` tom
%strategy RewriteSystem() extends Identity() {
  visit Term {
    a() -> { return `b(); }
    b() -> { return `c(); }
  }
}

public static void main(String[] args) throws VisitFailure {
    Strategy rule = `RewriteSystem();
    System.out.println(rule.visit(`c()));
}
```

Basic strategy combinators
==========================

The following operators are the key-component that can be used to define more complex strategies. In this framework, the application of a strategy to a term can fail. In <font color="purple">Java</font>, the *failure* is implemented by an exception (`VisitFailure`) of the package `library.sl`.

<div class="center">
|                    |     |                                                      |
|:-------------------|:---:|:-----------------------------------------------------|
| ()\[t\]            |  ⇒  | t                                                    |
| ()\[t\]            |  ⇒  | *failure*                                            |
| ()\[t\]            |  ⇒  | *failure* if ()\[t\] fails                           |
|                    |     | ()\[t’\] if ()\[t\] ⇒t’                              |
| ()\[t\]            |  ⇒  | t’ if ()\[t\] ⇒t’                                    |
|                    |     | ()\[t\] if ()\[t\] fails                             |
| ()\[f(t1,...,tn)\] |  ⇒  | f(t1’,...,tn’) if ()\[t1\] ⇒t1’, ..., ()\[tn\] ⇒tn’  |
|                    |     | *failure* if there exists i such that ()\[ti\] fails |
| ()\[cst\]          |  ⇒  | cst                                                  |
| ()\[f(t1,...,tn)\] |  ⇒  | f(t1,...,ti’,...,tn) if ()\[ti\] ⇒ti’                |
|                    |     | *failure* ()\[t1\] fails, ..., ()\[tn\] fails        |
| ()\[cst\]          |  ⇒  | *failure*                                            |

</div>
For example, we can define = where is defined as the elementary strategy:

``` tom
%strategy RewriteSystem() extends Fail() {
  visit Term {
    a() -> { return `b(); }
  }
}
```

When applying this strategy to different subjects, we obtain:

<div class="center">
|                  |     |            |
|:-----------------|:---:|:-----------|
| ()\[f(a(),a())\] |  ⇒  | f(b(),b()) |
| ()\[f(a(),b())\] |  ⇒  | *failure*  |
| ()\[b()\]        |  ⇒  | b()        |

</div>
Sometimes, it is interesting to get the environment where the strategy is applied. The interface `Strategy` has a method `getEnvironment()` which returns the current environment of the strategy. This is particularly interesting to obtain the position (in the subject) where the strategy is currently applied. In this case, you can call `getEnvironment().getPosition()`.

This information is also available through the `getEnvironment()` method from the `AbstractStrategy` class. To use this method or the following strategies which depend on it, you must call the `visit` method on your strategy instead of `visitLight`. That is the difference between `visitLight` and `visit`. Only the `visit` method maintain an environment.

``` tom
Strategy s;
try {
  s = `OnceBottomUp(rule);
  s.visit(subject));
} catch(VisitFailure e) {
  System.out.prinltn("Failure at position" + s.getEnvironment().getPosition());
}
```

The library gives several basic strategies using the position:

<div class="center">
|                    |     |                                       |
|:-------------------|:---:|:--------------------------------------|
| ()\[f(t1,...,tn)\] |  ⇒  | f(t1,...,ti’,...,tn) if ()\[ti\] ⇒ti’ |
|                    |     | *failure* if ()\[ti\] fails           |
| ()\[f(t1,...,tn)\] |  ⇒  | f(t1,...,t,...,tn)                    |
| ()\[f(t1,...,tn)\] |  ⇒  | ti                                    |

</div>
Strategy library
================

In order to define recursive strategies, we introduce the µ abstractor. This allows to give a name to the current strategy, which can be referenced later.

<div class="center">
|     |     |                                         |
|:----|:---:|:----------------------------------------|
|     |  =  | Choice(s,Identity)                      |
|     |  =  | µ x.Choice(Sequence(s,x),Identity())    |
|     |  =  | µ x.Choice(One(x),s)                    |
|     |  =  | µ x.Sequence(All(x),s)                  |
|     |  =  | µ x.Sequence(s,All(x))                  |
|     |  =  | µ x.Sequence(All(x),Try(Sequence(s,x))) |

</div>
The strategy never fails: it tries to apply the strategy . If it succeeds, the result is returned. Otherwise,the strategy is applied, and the subject is not modified.

The strategy applies the strategy as many times as possible, until a failure occurs. The last unfailing result is returned.

The strategy tries to apply the strategy once, starting from the leftmost-innermost leaves. looks like but is not similar: is applied to all nodes, starting from the leaves. Note that the application of should not fail, otherwise the whole strategy also fails.

The strategy tries to apply as many times as possible, starting from the leaves. This construct is useful to compute normal forms.

For example, we define = where is defined as the elementary strategy:

``` tom
%strategy RewriteSystem() extends Fail() {
  visit Term {
    a()        -> { return `b(); }
    b()        -> { return `c(); }
    g(c(),c()) -> { return `c(); }
  }
}
```

The application of this strategy to different subject terms gives:

<div class="center">
|                           |     |            |
|:--------------------------|:---:|:-----------|
| ()\[g(a(),b())\]          |  ⇒  | c()        |
| ()\[f(g(g(a,b),g(a,a)))\] |  ⇒  | f(c(),c()) |
| ()\[g(d(),d())\]          |  ⇒  | g(d(),d()) |

</div>
We can notice that Innermost strategy never fails. If we try = with the same subjects, we obtain always *failure* because if fails on a node, the whole strategy fails.

Strategies with identity considered as failure (**\***)
=======================================================

In order to get more efficient strategies (in particular when performing leftmost-innermost normalization), we consider variants where the notion of *failure* corresponds to the *identity*. This means that when a term cannot be transformed by a strategy (into a different term), this is considered as a *failure*.

<div class="center">
|                    |     |                                                   |
|:-------------------|:---:|:--------------------------------------------------|
| ()\[t\]            |  ⇒  | ()\[t’\] if ()\[t\] ⇒t’ with t≠t’                 |
|                    |     | t otherwise                                       |
| ()\[t\]            |  ⇒  | t’ if ()\[t\] ⇒t’ with t≠t’                       |
|                    |     | ()\[t\] otherwise                                 |
| ()\[f(t1,...,tn)\] |  ⇒  | f(t1,...,ti’,...,tn) if ()\[ti\] ⇒ti’ with ti≠ti’ |
|                    |     | f(t1,...,tn) otherwise                            |
| ()\[cst\]          |  ⇒  | cst                                               |
|                    |     |                                                   |
|                    |  =  | s                                                 |
|                    |  =  | µ x.SequenceId(s,x))                              |
|                    |  =  | µ x.ChoiceId(OneId(x),s))                         |
|                    |  =  | µ x.ChoiceId(s,OneId(x)))                         |
|                    |  =  | µ x.Sequence(All(x),SequenceId(s,x)))             |
|                    |  =  | µ x.Sequence(SequenceId(s,x),All(x)))             |

</div>
We can define a strategy trying to apply a simple rewrite system to the root of a term, replacing `a()` by `b()`, `b()` by `c()`, and `g(c(),c())` by `a()`, and otherwise returning the identity:

``` tom
import tom.library.sl.*;
```

We also need to import the corresponding mapping:

``` tom
%include { sl.tom }
```

Then we define an elementary strategy:

``` tom
%strategy RewriteSystem() extends Fail() {
  visit Term {
    a()        -> { return `b(); }
    b()        -> { return `c(); }
    g(c(),c()) -> { return `c(); }
  }
}
```

Then, it becomes quite easy to define various strategies on top of this elementary strategy:

``` tom
Term subject = `f(g(g(a,b),g(a,a)));
Strategy rule = `RewriteSystem();
try {
  System.out.println("subject       = " + subject);
  System.out.println("onceBottomUp  = " +
      `OnceBottomUp(rule).visitLight(subject));
  System.out.println("innermost   = " +
      `Choice(BottomUp(rule),Innermost(rule)).visitLight(subject));
} catch (VisitFailure e) {
  System.out.println("reduction failed on: " + subject);
}
```

Congruence strategies (generated by )
=====================================

As mentioned in Section [Congruence strategies](/Documentation:Gom#Congruence_strategies "wikilink"), automatically generates congruence and construction strategies for each constructor of the signature.

Matching and visiting a strategy (**\***)
=========================================

Strategies can be considered as algebraic terms. For instance, the `TopDown(v)` strategy corresponds to the algebraic term `mu(MuVar(`“`x`”`),Sequence(v,All(MuVar(`“`x`”`))))`.

Those strategy terms can then be traversed and transformed by mean of strategies. As strategies are considered as algebraic terms, it is possible to use pattern matching on the elementary strategies of the strategy language.

The following function uses pattern matching on a strategy expression, to identify a `TopDown` strategy and transform it into `BottomUp`.

``` tom

 public Strategy topDown2BottomUp(Strategy s) {
  %match(s) {
    Mu(x,Sequence(v,All(x))) -> {
      return `Mu(x,Sequence(All(x),v));
    }
  }
  return s;
}
```

Strategy expressions being visitable terms, we can also use the construction to define strategy transformations, for example, removing unnecessary `Identity()` strategies.

``` tom
%strategy RemId extends Identity() {
  visit Strategy {
    Sequence(Identity(),x) -> { return `x; }
    Sequence(x,Identity()) -> { return `x; }
  }
}
```

Applying a strategy on a user defined data-structures (**\***)
==============================================================

The simplest way to use strategies is to apply them on data-structures generated by Gom, as this data structure implementation provides the interfaces and classes needed to use . In particular, all the data structure classes implement the `tom.library.sl.Visitable` interface used as argument of `visit` methods in the `tom.library.sl.Strategy` interface. However, it is also possible to use with any term implementation.

We detail here on a simple example of hand written data structure and how to use statements and the Tom strategy library.

Given a <font color="purple">Java</font> class `Person` we can define an algebraic mapping for this class:

``` tom
%typeterm TomPerson {
  implement { Person }
  equals(t1,t2) { t1.equals(t2) }
}
```

For this example, we consider a data-structure those Gom equivalent could be

``` tom
Term = A()
     | B()
     | G(arg:Slot)
     | F(arg1:Term, arg2:Term)
Slot = Name(name:String)
```

Simple implementation of the data structure
-------------------------------------------

We first present a very straightforward implementation of this data structure. It will serve as a basis and will be extended to provide support for strategies.

We first define abstract classes for the two sorts of this definition, `Term` and `Slot`, and classes for those operators extending those sort classes.

``` tom
public abstract class Term { }
public abstract class Slot { }

/* A and B are similar up to a renaming */
public class A extends Term {
  public String toString() {
    return "A()";
  }
  public boolean equals(Object o) {
    if(o instanceof A) {
      return true;
    }
    return false;
  }
}

/* G is similar to F, but has only one child */
public class F extends Term {
  public Term a;
  public Term b;
  public F(Term arg0, Term arg1) {
    a = arg0;
    b = arg1;
  }
  public String toString() {
    return "F("+a.toString()+", "+b.toString()+")";
  }
  public boolean equals(Object o) {
    if(o instanceof F) {
      F f = (F) o;
      return a.equals(f.a) && b.equals(f.b);
    }
    return false;
  }
}

public class Name extends Slot {
  public String name;
  public Name(String s) {
    this.name = s;
  }
  public String toString() {
    return "Name("+name+")";
  }
  public boolean equals(Object o) {
    if(o instanceof Name) {
      Name n = (Name) o;
      return name.equals(n.name);
    }
    return false;
  }
}
```

We only took care in this implementation to get a correct behavior for the `equals` method. Then, the Tom mapping is simply

``` tom
%include { string.tom }
%typeterm Term {
  implement { Term }
  equals(t1,t2) {t1.equals(t2)}
}
%typeterm Slot {
  implement { Slot }
  equals(t1,t2) {t1.equals(t2)}
}
%op Term A() {
  is_fsym(t) { (t!= null) && (t instanceof A) }
  make() { new A() }
}
%op Term F(arg1:Term, arg2:Term) {
  is_fsym(t) { (t!= null) && (t instanceof F) }
  get_slot(arg1,t) { ((F) t).a }
  get_slot(arg2,t) { ((F) t).b }
  make(t0, t1) { new F(t0, t1) }
}
...
%op Slot Name(name:String) {
  is_fsym(t) { (t!= null) && (t instanceof Name) }
  get_slot(name,t) { ((Name) t).name }
  make(t0) { new Name(t0) }
}
```

Visiting data-structures by introspection
-----------------------------------------

If the classes representing operators do not implement the `tom.library.sl.Visitable` interface, there is no special code modification for supporting strategies. Users have just to activate the Tom option `–gi`.

We can use the `tom.library.sl.Introspector` interface to apply strategies on such terms:

``` tom
public Object setChildren(Object o, Object[] children);
public Object[] getChildren(Object o);
public Object setChildAt( Object o, int i, Object child);
public Object getChildAt(Object o, int i);
public int getChildCount(Object o);
```

In the `tom.library.sl.Strategy` interface, there exist corresponding methods to visit objects that do not implement the `tom.library.sl.Visitable` interface:

``` tom
public Object visit(Object any, Introspector i) throws VisitFailure;
public Object visitLight(Object any, Introspector i) throws VisitFailure;
public Object visit(Environment envt, Introspector i) throws VisitFailure;
```

In the implementation of these methods, the introspector behaves like a proxy to render any object visitable. When activating the Tom option `–gi`, the compiler generates in each Tom class an inner class named `LocalIntrospector` that implements the `tom.library.sl.Introspector` interface. This class uses informations from the mappings to know how visiting the corresponding classes.

For example, we can define the following statement:

``` tom
%strategy Rename(oldname:String,newname:String) extends Identity() {
  visit Slot {
    Name(n) -> {
      if(n.equals(oldname)) {
        return `Name(newname);
      }
    }
  }
}
```

Then by using the generated class `LocalIntrospector`, it is possible to use the strategy `Rename` with any strategy combinators:

``` tom
public static void main(String[] args) {
  Term t = `F(F(G("x"),G("y")),G("x"));
  `TopDown(Rename("x","z")).visit(t, new LocalIntrospector());
}
```

[Category:Documentation](/Category:Documentation "wikilink")