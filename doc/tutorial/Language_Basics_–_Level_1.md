---
title: Documentation:Language Basics – Level 1
permalink: /Documentation:Language_Basics_–_Level_1/
---

In this first part, we introduce the basic notions provided by Tom: the *definition* of a data-type, the *construction* of data (i.e. trees), and the *transformation* of such data using pattern-matching.

Defining a data-structure
=========================

One of the most simple Tom programs is the following. It defines a data-type to represent Peano integers and builds the integers 0=zero and 1=suc(zero):

``` tom
import main.peano.types.*;
  public class Main {
  %gom {
    module Peano
    abstract syntax
    Nat = zero()
        | suc(pred:Nat)
        | plus(x1:Nat, x2:Nat)
  }
  public final static void main(String[] args) {
    Nat z   = `zero();
    Nat one = `suc(z);
    System.out.println(z);
    System.out.println(one);
  }
}
```

The `%gom {...}` construct defines a data-structure, also called signature or algebraic data-type. This data-structure declares a sort (`Nat`) that has three operators (`zero`, `suc`, and `plus`). These operators are called constructors, because they are used to construct the data-structure. `zero` is a constant (arity 0), whereas the arities of `suc` and `plus` are respectively 1 and 2. Note that a name has to be given for each slot (`pred` for suc, and `x1`, `x2` for plus).

Retrieving information in a data-structure
==========================================

In addition to `%gom` and `` ` ``, Tom provides a third construct: `%match`. Using the same program, we add a method `evaluate(Nat n)` and we modify the method `run()`:

``` tom
import main.peano.types.*;
  public class Main {
  %gom {
    module Peano
    abstract syntax
    Nat = zero()
        | suc(pred:Nat)
        | plus(x1:Nat, x2:Nat)
  }
  public final static void main(String[] args) {
    Nat two = `plus(suc(zero()),suc(zero()));
    System.out.println(two);
    two = evaluate(two);
    System.out.println(two);
  }
  public static Nat evaluate(Nat n) {
    %match(n) {
      plus(x, zero()) -> { return `x; }
      plus(x, suc(y)) -> { return `suc(plus(x,y)); }
    }
    return n;
  }
}
```

The `%match` construct is similar to `switch/case` in the sense that given a tree (`n`), the `%match` selects the first pattern that *matches* the tree, and executes the associated action. By *matching*, we mean: giving values to variables to make the pattern equal to the subject. In our example `n` has the value `plus(suc(zero()),suc(zero()))`. The first pattern does not match `n`, because the second subterm of `n` is not a `zero()`, but a `suc(zero())`. In this example, the second pattern matches `n` and gives the value `suc(zero())` to `x`, and `zero()` to `y`.

When a pattern matches a subject, we say that the variables (`x` and `y` in our case) are *instantiated*. They can then be used in the action part (also called right-hand side). Similarly, the second rules can be applied when the second subterm is rooted by a `suc`. In that case, `x` and `y` are instantiated.

The method `evaluate` defines the addition of two integers represented by Peano successors (i.e. `zero` and `suc`). The first case says that the addition of an integer with `zero` is evaluated into the integer itself. The second case builds a new term (rooted by `suc`) whose subterm (`plus(x,y)`) denotes the addition of `x` and `y`.

When executing the program we obtain:

`plus(suc(zero()),suc(zero()))`
`suc(plus(suc(zero()),zero()))`

Using rules to simplify expressions
===================================

In this section, we will show how to use the notion of *rules* to simplify expressions. Suppose that we want to simplify boolean expressions. It is frequent to define the relations between expressions using a set of simplification rules like the following:

|                                                                                         |
|-----------------------------------------------------------------------------------------|
| |                        |     |                                                      |
 |:-----------------------|:---:|:-----------------------------------------------------|
 | *Not*(*a*)             |  →  | *Nand*(*a*,*a*)                                      |
 | *Or*(*a*, *b*)         |  →  | *Nand*(*Not*(*a*), *Not*(*b*))                       |
 | *And*(*a*, *b*)        |  →  | *Not*(*Nand*(*a*, *b*))                              |
 | *Xor*(*a*, *b*)        |  →  | *Or*(*And*(*a*, *Not*(*b*)), *And*(*Not*(*a*), *b*)) |
 | *Nand*(*False*, *b*)   |  →  | *True*                                               |
 | *Nand*(*a*, *False*)   |  →  | *True*                                               |
 | *Nand*(*True*, *True*) |  →  | *False*                                              |  |

To encode such a simplification system, we have to implement a function that simplifies an expression until no more reduction can be performed. This can of course be done using functions defined in <font color="purple">Java</font>, combined with the `%match` constructs, but it is more convenient to use an algebraic construct that ensures that a rule is applied whenever a reduction is possible. For this purpose, the `%gom` construct provides a `rule()` construct where the left and the right-hand sides are terms. The previous simplification system can be defined as follows:

``` tom
import gates.logic.types.*;
  public class Gates {
    %gom {
      module Logic
        imports int
        abstract syntax
        Bool = Input(n:int)
             | True()
             | False()
             | Not(b:Bool)
             | Or(b1:Bool, b2:Bool)
             | And(b1:Bool, b2:Bool)
             | Nand(b1:Bool, b2:Bool)
             | Xor(b1:Bool, b2:Bool)

        module Logic:rules() {
          Not(a)   -> Nand(a,a)
          Or(a,b)  -> Nand(Not(a),Not(b))
          And(a,b) -> Not(Nand(a,b))
          Xor(a,b) -> Or(And(a,Not(b)),And(Not(a),b))
          Nand(False(),_)     -> True()
          Nand(_,False())     -> True()
          Nand(True(),True()) -> False()
        }
    }
    public final static void main(String[] args) {
    Bool b = `Xor(True(),False());
    System.out.println("b = " + b);
  }
}
```

When using the `module Logic:rules() { ... }` constructs, the simplification rules are integrated into the data-structure. This means that the rules are applied any time it is possible to do a reduction. The user does not have any control on them, and thus cannot prevent from applying a rule. Of course, the simplification system should be terminating, otherwise, infinite reductions may occur. When compiling and executing the previous program, we obtain `b = True()`.

Separating Gom from Tom (**\***)
================================

When programming large applications, it may be more convenient to introduce several classes that share a common data-structure. In that case, the `%gom {...}` construct could be avoided and a Gom file (`Logic.gom` for example) could be used instead:

``` tom
module Logic
imports int
abstract syntax
Bool = Input(n:int)
     | True()
     | False()
     | Not(b:Bool)
     | Or(b1:Bool, b2:Bool)
     | And(b1:Bool, b2:Bool)
     | Nand(b1:Bool, b2:Bool)
     | Xor(b1:Bool, b2:Bool)

module Logic:rules() {
  Not(a)   -> Nand(a,a)
  Or(a,b)  -> Nand(Not(a),Not(b))
  And(a,b) -> Not(Nand(a,b))
  Xor(a,b) -> Or(And(a,Not(b)),And(Not(a),b))
  Nand(False(),_)     -> True()
  Nand(_,False())     -> True()
  Nand(True(),True()) -> False()
}
```

This file can by compiled as follows:

``` tom
$ gom Logic.gom
```

This generates several <font color="purple">Java</font> classes, among them a particular file called `Logic.tom` which explains to Tom how the data-structure is implemented. This process is hidden when using the `%gom {...}` construct:

``` tom
$ ls logic
_Logic.tom              Logic.tom               LogicAbstractType.java
strategy                types
```

One of the simplest Tom program that uses the defined data-structure is the following:

``` tom
import logic.types.*;
public class Main {
  %include{ logic/Logic.tom }
  public final static void main(String[] args) {
    Bool b = `Xor(True(),False());
    System.out.println("b = " + b);
  }
}
```

Programming in Tom
==================

In this section we present how Tom can be used to describe the abstract syntax and to implement an interpreter for a given language. We also give some tips and more information about the generated code.

When using `%gom { ... }`, the generated data-structure is particular: data are maximally shared. This technique, also known as *hash-consing* ensures that two identical subterms are implemented by the same objects. Therefore, the memory footprint is minimal and the equality-check can be done in constant time using the `==` construct (two terms are identical if the pointers are equal). As a consequence, a term is not mutable: once created, it cannot be modified.

Even if the generated API offers getters and setters for each defined slot (`Bool getb1()`, `Bool getb2()`, `Bool setb1(Bool t)` and `Bool setb2(Bool t)` for the `And(b1:Bool,b2:Bool)` defined below). These methods do not really modify the term on which they are applied: `t.setb1(‘True())` will return a copy of `t` where the first child is set to `True()`, but the previous `t` is not modified. There is no side-effect in a maximally shared setting.

In Tom, it is quite easy to quickly develop applications which manipulates trees. As an example, let us consider a tiny language composed of boolean expressions (`True`, `False`, `And`, `Or`, and `Not`), expressions (`constant`, `variable`, `Plus`, `Mult`, and `Mod`), and instructions (`Skip`, `Print`, `;`, `If`, and `While`). The abstract syntax of this language can be described as follows:

``` tom
import pico1.term.types.*;
import java.util.*;
  class Pico1 {
    %gom {
      module Term
      imports int String
      abstract syntax
      Bool = True()
           | False()
           | Not(b:Bool)
           | Or(b1:Bool, b2:Bool)
           | And(b1:Bool, b2:Bool)
           | Eq(e1:Expr, e2:Expr)
      Expr = Var(name:String)
           | Cst(val:int)
           | Plus(e1:Expr, e2:Expr)
           | Mult(e1:Expr, e2:Expr)
           | Mod(e1:Expr, e2:Expr)
      Inst = Skip()
           | Assign(name:String, e:Expr)
           | Seq(i1:Inst, i2:Inst)
           | If(cond:Bool, i1:Inst, i2:Inst)
           | While(cond:Bool, i:Inst)
           | Print(e:Expr)
    }
  ...
}
```

Assume that we want to write an interpreter for this language, we need a notion of *environment* to store the value assigned to a variable. A simple solution is to use a `Map` which associate an expression (of sort `Expr`) to a name of variable (of sort `String`). Given this environment, the evaluation of an expression can be implemented as follows:

``` tom
public static Expr evalExpr(Map env,Expr expr) {
  %match(expr) {
    Var(n)                -> { return (Expr)env.get(`n); }
    Plus(Cst(v1),Cst(v2)) -> { return `Cst(v1 + v2); }
    Mult(Cst(v1),Cst(v2)) -> { return `Cst(v1 * v2); }
    Mod(Cst(v1),Cst(v2))  -> { return `Cst(v1 % v2); }
    // congruence rules
    Plus(e1,e2) -> { return `evalExpr(env,Plus(evalExpr(env,e1),evalExpr(env,e2))); }
    Mult(e1,e2) -> { return `evalExpr(env,Mult(evalExpr(env,e1),evalExpr(env,e2))); }
    Mod(e1,e2)  -> { return `evalExpr(env,Mod(evalExpr(env,e1),evalExpr(env,e2)));  }
    x -> { return `x; }
  }
  throw new RuntimeException("should not be there: " + expr);
}
```

Similarly, the evaluation of boolean expressions can be implemented as follows:

``` tom
public static Bool evalBool(Map env,Bool bool) {
  %match(bool) {
    Not(True())     -> { return `False(); }
    Not(False())    -> { return `True(); }
    Not(b)          -> { return `evalBool(env,Not(evalBool(env,b))); }
    Or(True(),_)    -> { return `True(); }
    Or(_,True())    -> { return `True(); }
    Or(False(),b2)  -> { return `evalBool(env, b2); }
    Or(b1,False())  -> { return `evalBool(env, b1); }
    Or(b1,b2)       -> { return `evalBool(env, Or(evalBool(env,b1),evalBool(env,b2))); }
    And(True(),b2)  -> { return `evalBool(env, b2); }
    And(b1,True())  -> { return `evalBool(env, b1); }
    And(False(),_)  -> { return `False(); }
    And(_,False())  -> { return `False(); }
    And(b1,b2)      -> { return `evalBool(env, And(evalBool(env,b1),evalBool(env,b2))); }
    Eq(e1,e2)       -> {
      Expr x = `evalExpr(env,e1);
      Expr y = `evalExpr(env,e2);
      return (x==y)?`True():`False();
    }
    x               -> { return `x; }
  }
  throw new RuntimeException("should not be there: " + bool);
}
```

Once defined the methods `evalExpr` and `evalBool`, it becomes easy to define the interpreter:

``` tom
public static void eval(Map env, Inst inst) {
  %match(inst) {
    Skip() -> {
      return;
    }
    Assign(name,e) -> {
      env.put(`name,evalExpr(env,`e));
      return;
    }
    Seq(i1,i2) -> {
      eval(env,`i1);
      eval(env,`i2);
      return;
    }
    Print(e) -> {
      System.out.println(evalExpr(env,`e));
      return;
    }
    If(b,i1,i2) -> {
      if(evalBool(env,`b)==`True()) {
        eval(env,`i1);
      } else {
        eval(env,`i2);
      }
      return;
    }
    w@While(b,i) -> {
      Bool cond = evalBool(env,`b);
      if(cond==`True()) {
        eval(env,`i);
        eval(env,`w);
      }
      return;
    }
  }
  throw new RuntimeException("strange term: " + inst);
}
```

To play with the `Pico` language, we just have to initialize the environment (`env`), create programs (`p1` and `p2`), and evaluate them (`eval`):

``` tom
public final static void main(String[] args) {
  Map env = new HashMap();
  Inst p1 = `Seq(Assign("a",Cst(1)) , Print(Var("a")));
  System.out.println("p1: " + p1);
  eval(env,p1);
  Inst p2 = `Seq(Assign("i",Cst(0)),
               While(Not(Eq(Var("i"),Cst(10))),
                     Seq(Print(Var("i")),
                         Assign("i",Plus(Var("i"),Cst(1))))));
  System.out.println("p2: " + p2);
  eval(env,p2);
}
```

<div class="note">
**Exercise:** write a Pico program that computes prime numbers up to 100.

</div>
[Category:Documentation](/Category:Documentation "wikilink")