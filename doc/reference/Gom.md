# Gom

The basic functionality of Gom is to provide a tree implementation in Java corresponding to an algebraic specification. The syntax is inspired from the algebraic type definition of ML languages. Gom generates an efficient code which ensures maximal sharing.

To maintain the terms in a preferred canonical form, Gom provides an original [hooks](Gom.md#hooks) mechanism comparable to “private types” of Caml, or “smart constructors” of Haskell.

The tree implementations Gom generates are characterized by strong typing, immutability (there is no way to manipulate them with side-effects), maximal subterm sharing and the ability to be used with [strategies](Strategies.md).

## Gom syntax

Gom provides a syntax to concisely define abstract syntax tree. Each Gom file consists in the definition of one or more modules. In each module we define sorts, and operators for the sorts. Additionally, we can define [hooks](Gom.md#hooks) to modify the default behavior of a constructor operator.

### Example of signature

The syntax of Gom is quite simple and can be used to define many-sorted abstract-datatypes. The `module` section defines the name of the signature. The `imports` section defines the name of the imported signatures. The `abstract syntax` part defines the operators with their signature. For each argument, a sort and a name (called slot-name) has to be given.

```java
module Expressions
imports String int
abstract syntax
Bool = True()
     | False()
     | Eq(lhs:Expr, rhs:Expr)
Expr = Id(stringValue:String)
     | Nat(intValue:int)
     | Add(lhs:Expr, rhs:Expr)
     | Mul(lhs:Expr, rhs:Expr)
```

The definition of a signature in Gom has several restrictions:

-   there is no overloading: two operators cannot have the same name
-   for a given operator, all slot-names must be different
-   if two slots have the same slot-name, they must belong to the same sort. In the previous example, `Eq`, `Add`, and `Mul` can have a slot called `lhs` of sort `Expr`. But, `Id` and `Nat` cannot have a same slot named `value`, since their sort are not identical (the slots are respectively of sorts `String` and `int` for `Id` and `Nat`).

### Builtin sorts and operators

Gom supports several *builtin* sorts, that may be used as sort for new operators slots. To each of these builtin sorts corresponds a Java type. Native data types from Java can be used as builtin fields, as well as `ATerm` and `ATermList` structures. To use one of these builtin types in a Gom specification, it is required to add an import for the corresponding builtin.

| Name      | Java type       |
|-----------|-----------------|
| int       | int             |
| String    | String          |
| char      | char            |
| double    | double          |
| long      | long            |
| float     | float           |
| ATerm     | aterm.ATerm     |
| ATermList | aterm.ATermList |


| A | B  |
|---|----|
| a | b\|  |


It is not possible to define a new operator whose codomain is one of the builtin sorts, since those sorts are defined in another module.

Note: to be able to use a builtin sort, it is necessary to declare the import of the corresponding module (`int` or `String`) in the Imports section.

External Gom modules may be imported in a Gom specification, by adding the name of the module to import in the imports section. Once a module imported, it is possible to use any of the sorts this module declares or imports itself as type for a new operator slot. Adding new operators to an imported sort is however not allowed.

### Grammar

|                    |     |                                                                                |
|:-------------------|:---:|:------------------------------------------------------------------------------ |
| GomGrammar         | ::= | Module                                                                         |
| Module             | ::= | module ModuleName \[Imports\] Grammar                                          |
| Imports            | ::= | imports (ModuleName)\*  

<div class="center">
|                    |     |                                                                                |
|:-------------------|:---:|:------------------------------------------------------------------------------ |
| GomGrammar         | ::= | Module                                                                         |
| Module             | ::= | module ModuleName \[Imports\] Grammar                                          |
| Imports            | ::= | imports (ModuleName)\*                                                              |
| Grammar            | ::= | abstract syntax (TypeDefinition ∣ HookDefinition)\*                                 |
| TypeDefinition     | ::= | SortName = OperatorDefinition (| OperatorDefinition)\*                              |
| OperatorDefinition | ::= | OperatorName(\[SlotDefinition(,SlotDefinition)\*\])                                 |
|                    |  ∣  | OperatorName( SortName\* )                                                          |
| SlotDefinition     | ::= | SlotName: SortName                                                                  |
| ModuleName         | ::= | Identifier                                                                          |
| SortName           | ::= | Identifier                                                                          |
| OperatorName       | ::= | Identifier                                                                          |
| SlotName           | ::= | Identifier                                                                          |
|                    |
| HookDefinition     | ::= | HookType: HookOperation                                                             |
| HookType           | ::= | OperatorName                                                                        |
|                    |  \∣  | module ModuleName                                                                   |
|                    |  \∣  | sort SortName                                                                       |
| HookOperation      | ::= | make ( \[Identifier (, Identifier)\*\] ) { TomCode <nowiki>lt;/nowiki&gt;}}         |
|                    |  \∣  | make_insert ( Identifier , Identifier ) { TomCode <nowiki>lt;/nowiki&gt;}}         |
|                    |  \∣  | make_empty() { TomCode <nowiki>lt;/nowiki&gt;}}                                    |
|                    |  \∣  | Free() <nowiki>{lt;/nowiki&gt;}}                                                    |
|                    |  \∣  | FL() <nowiki>{lt;/nowiki&gt;}}                                                      |
|                    |  \∣  | (AU ∣ACU) () { \[\` term\] <nowiki>lt;/nowiki&gt;}}                                 |
|                    |  \∣  | interface() { Identifier(, Identifier)\* <nowiki>lt;/nowiki&gt;}}                   |
|                    |  \∣  | import() { JavaImports <nowiki>lt;/nowiki&gt;}}                                     |
|                    |  \∣  | block() { TomCode<nowiki>lt;/nowiki&gt;}}                                           |
|                    |  \∣  | rules() { RulesCode <nowiki>lt;/nowiki&gt;}}                                        |
|                    |  \∣  | graphrules( Identifier , (Identity ∣Fail)){ GraphRulesCode <nowiki>lt;/nowiki&gt;}} |
|                    |
| RulesCode          | ::= | (Rule)\*                                                                            |
| Rule               | ::= | RulePattern -&gt; TomTerm \[if Condition\]                                          |
| RulePattern        | ::= | \[ AnnotedName @ \] PlainRulePattern                                                |
| PlainRulePattern   | ::= | \[!\]VariableName \[ \* \]                                                          |
|                    |  \∣  | \[!\]HeadSymbolList ( \[RulePattern ( , RulePattern )\*\] )                         |
|                    |  \∣  | _                                                                                  |
|                    |  \∣  | _\*                                                                                |
|                    |
| GraphRulesCode     | ::= | (GraphRule)\*                                                                       |
| GraphRule          | ::= | TermGraph -&gt; TermGraph \[if Condition\]                                          |
| TermGraph          | ::= | \[ Label : \] TermGraph                                                             |
|                    |  \∣  | & Label                                                                             |
|                    |  \∣  | VariableName \[ \* \]                                                               |
|                    |  \∣  | HeadSymbolList ( \[TermGraph ( , TermGraph )\*\] )                                  |
| Label              | ::= | Identifier                                                                          |
|                    |
| Condition          | ::= | TomTerm Operator TomTerm                                                            |
|                    |  \∣  | RulePattern &lt;&lt; TomTerm                                                        |
|                    |  \∣  | Condition && Condition                                                              |
|                    |  \∣  | Condition <nowiki>                                                                  |
|                    |  \∣  | ( Condition )                                                                       |
| Operator           | ::= | &gt; ∣ &gt;= ∣&lt; ∣&lt;= ∣== ∣ !=                                                  |

</div>
Using Gom with Tom
------------------

A first solution to combine Gom with Tom is to use Gom as a standalone tool, using the [command line](../tools/Using_Gom.md#command-line-too) tool or the [ant task](../tools/Using_Gom.md#ant-task).

In that case, the module name of the Gom specification and the `package` option determine where the files are generated. To make things correct, it is sufficient to import the generated Java classes, as well as the generated Tom file. In the case of a Gom module called `Module`, all files are generated in a directory named `module` and the Tom program should do the following:

```java
import module.*;
import module.types.*;
class MyClass {
  ...
  %include { module/Module.tom }
  ...
}
```

A second possibility to combine Gom with Tom is to use the [`%gom`](Tom.md#gom-construct) construct offered by Tom. In that case, the Gom module can be directly included into the Tom file, using the `%gom` instruction:

```java
package myPackage;
import myPackage.myclass.expressions.*;
import myPackage.myclass.expressions.types.*;
class MyClass {
  %gom{
    module Expressions
    abstract syntax
    Bool = True()
         | False()
    ...
    Expr = Mul(lhs:Expr, rhs:Expr)
  }
  ...
}
```

Note that the Java code is generated in a package that corresponds to the current package, followed by the class-name and the module-name. This allows to define the same module in several classes, and avoid name clashes.

Hooks
=====

Gom provides hooks that allow to define properties of the data-structure, in particular canonical forms for the terms in the signature in an algebraic way.

Algebraic rules
---------------

The rules hook defines a set of conditional rewrite rules over the current module signature. Those rules are applied systematically using a leftmost-innermost reduction strategy. Thus, the only terms that can be produced and manipulated in the Tom program are normal with respect to the defined system.

```java
module Expressions
imports String int
abstract syntax
  Bool = True()
       | False()
       | Eq(lhs:Expr, rhs:Expr)
  Expr = Id(stringValue:String)
       | Nat(intValue:int)
       | Add(lhs:Expr, rhs:Expr)
       | Mul(lhs:Expr, rhs:Expr)
  module Expressions:rules() {
    Eq(x,x) -> True()
    Eq(x,y) -> False() if x!=y
  }
```

Since the rules do alter the behavior of the construction functions in the term structure, it is required in a module that the rules in a rules hook have as left-hand side a pattern rooted by an operator of the current module. The rules are tried in the order of their definitions, and the first matching rule is applied.

Note: it is possible to define rules on a variadic symbol. However, due to the leftmost-innermost rule application strategy, using a list variable at the left of a pattern is usually not needed, and may result in an inefficient procedure.

Hooks to alter the creation operations
--------------------------------------

*Hooks* may be used to specify how operators should be created. make, make_empty and make_insert hooks are altering the creation operations for respectively algebraic, neutral element (empty variadic) and variadic operators. make_insert is simply a derivative case of make, with two arguments, for variadic operators.

The hook operation type is followed by a list of arguments name between (). The creation operation takes those arguments in order to build a new instance of the operator. Thus, the arguments number has to match the slot number of the operator definition, and types are inferred from this definition.

Then the body of the hook definition is composed of <font color="purple">Java</font> and Tom code. The Tom code is compiled using the mapping definition for the current module, and thus allows to build and match terms from the current module. This code can also use the `realMake` function, which consists in the “inner” default allocation function. This function takes the same number of arguments as the hook. In any case, if the hooks code does not perform a `return` itself, this `realMake` function is called at the end of the hook execution, with the corresponding hooks arguments

Using the `expression` example introduced [above](Gom.md#example-of-signature), we can add *hooks* to implement the computation of `Add` and `Mul` when both arguments are known integers (i.e. when they are `Nat(x)`)

```java
module Expressions
imports String int
abstract syntax
Bool = True()
     | False()
     | Eq(lhs:Expr, rhs:Expr)
Expr = Id(stringValue:String)
     | Nat(intValue:int)
     | Add(lhs:Expr, rhs:Expr)
     | Mul(lhs:Expr, rhs:Expr)
Add:make(l,r) {
  %match(Expr l, Expr r) {
    Nat(lvalue), Nat(rvalue) -> {
      return `Nat(lvalue + rvalue);
    }
  }
}
Mul:make(l,r) {
  %match(Expr l, Expr r) {
    Nat(lvalue), Nat(rvalue) -> {
      return `Nat(lvalue * rvalue);
    }
  }
}
```

Using this definition, it is impossible to have an expression containing unevaluated expressions where a value can be calculated. Thus, a procedure doing constant propagations for `Id` whose value is known could simply replace the `Id` by the corresponding `Nat`, and rely on this mechanism to evaluate the expression. Note that the arguments of the `make` hook are themselves elements built on this signature, and thus the hooks have been applied for them. In the case of hooks encoding a rewrite system, this corresponds to using an innermost strategy.

List theory hooks
-----------------

In order to ease the use of variadic operators with the same domain and co-domain, Gom does provide hooks that enforce a particular canonical form for lists.

-   `FL`, activated with `<op>:FL() {}`, ensures that structures containing the operator <op> are left to right comb, with an empty `<op>()` at the right. This constitutes a particular form of associative with neutral element canonical form, with a restriction on the application of the neutral rules. This corresponds to the generation of the following normalisation rules:

`  make(Empty(),tail)     -> tail`
`  make(Cons(h,t),tail)   -> make(h,make(t,tail))`
`  make(head,tail)        -> make(head,make(tail,Empty)) if tail!=Empty and tail!=Cons`

-   `Free`, activated with `<op>:Free() {}`, ensures the variadic symbol remains free, i.e. deactivates the default `FL` hook.
-   `AU`, activated with `<op>:AU() {}`, ensures that structures containing the `<op>` operator are left to right comb, and that neutral elements are removed. It is possible to specify an alternate neutral element to the associative with neutral theory, using `` <op>:AU() {` ``<elem>`()}`, where `<elem>` is a term in the signature. This will generate the following normalisation rules:

`  make(Empty(),tail)     -> tail`
`  make(head,Empty())     -> head`
`  make(Cons(h,t),tail)   -> make(h,make(t,tail))`

-   `ACU` is similar to `AU`, except that it also ensures elements in the left to right comb are sorted using the builtin `compareTo` function.

`  make(Empty(),tail)     -> tail`
`  make(head,Empty())     -> head`
`  make(Cons(h,t),tail)   -> make(h,make(t,tail))`
`  make(head,Cons(h,t))   -> make(head,Cons(h,t)) if head < h`
`  make(head,Cons(h,t))   -> make(h,make(head,t)) if head >= h`

If you do not define any hook of the form `AU`, `ACU`, `FL`, `Free`, or `rules`, the `FL` hook will be automatically declared for variadic operators whose domain and co-domain are equals. In practice, this makes list matching and associative matching with neutral element easy to use.

If you use a hook `rules`, there may be an interaction that can lead to non-termination. Therefore, no hook will be automatically added, and you are forced to declare a hook of the form `AU`, `ACU`, `FL` or `Free`.

Note: if you do not really understand what happens when you define a hook `rules`, the safest approach is to declare the operator as `<op>:Free() {}` and to encode the desired theory in the `rules`.

## Generated API

For each Module, the Gom compiler generates an API specific of this module, possibly using the API of other modules (declared in the Imports section). This API is located in a <font color="purple">Java</font> package named using the ModuleName lowercased, and contains the tree implementation itself, with some additional utilities:

-   an abstract class named ModuleName`AbstractType` is generated. This class is the generic type for all nodes whose type is declared in this module. It declares generic functions for all tree nodes: a `toATerm()` method returning a `aterm.ATerm` representation of the tree; a `symbolName()` method returning a `String` representation for the function symbol of the tree root; the `toString()` method, which returns a string representation.

```java
public aterm.ATerm toATerm();
public String symbolName();
public int compareTo(Object o);
public int compareToLPO(Object o);
public void toStringBuilder(java.lang.StringBuilder buffer);
```

-   in a subpackage `types`, Gom generates one class for each sort defined in the module, whose name corresponds to the sort name. Each sort class extends ModuleName`AbstractType` for the module, and declares a `boolean` method `is`OperatorName`()` for each operator in the module (`false` by default). It declares getters (methods named `get`SlotName`()`) for each slot used in any operator of the sort (throwing an exception by default). A method `SlotName fromTerm(aterm.ATerm trm)` is generated, allowing to use ATerm as an exchange format. The methods `SlotName fromString(String s)` and `SlotName fromStream(InputStream stream)` allow the use of an ATerm representation in String or stream form, to store terms in file and read them back.

```java
public boolean is<op>();
public <SlotType> get<slotName>();
public <SortName> set<slotName>(<SlotType>);
public static <SortName> fromTerm(aterm.ATerm trm);
public static <SortName> fromString(String s);
public static <SortName> fromStream(java.io.InputStream stream) throws java.io.IOException;
public int length();
public <SortName> reverse();
```

-   given a sort (SortName), generate a class (in a package `types.`SortName) for each operator. This class extends the class generated for the corresponding sort. It provides getters for the slots of the operator, and the `is`OperatorName`()` method is overridden to return `true`. Those classes implement the `tom.library.sl.Visitable` interface. It is worth noting that builtin fields are not accessible from the `Visitable`, and thus will not be visited by strategies. The operator class also implements a static `make` method, building a new instance of the operator. This `make` method is the only way to obtain a new instance.

```java
public static <op> make(arg1,...,argn);
```

-   for each list-operator, Operator for instance, the generated code contains two operator classes: one name `Empty`Operator which is used to represent the empty list of arity 0, and the other named `Cons`Operator having two fields: one with the codomain sort, and one with the domain sort of the variadic operator, respectively named `Head`Operator and `Tail`Operator, leading to getter functions `getHead`Operatorand `getTail`Operator. This allows to define lists as the composition of many `Cons` and one `Empty` objects.
-   for each module, one file ModuleName`.tom` providing a Tom mapping for the sorts and operators defined or imported by the module.

Example of generated API
------------------------

We show elements of the generated API for a very simple example, featuring variadic operator. It defines natural numbers as `Zero()` and `Suc(n)`, and lists of natural numbers.

```java
module Mod
abstract syntax
Nat = Zero()
    | Suc(pred:Nat)
    | Plus(lhs:Nat,rhs:Nat)
    | List(Nat*)
```

Using the command `gom Mod.gom`, the list of generated files is:

`mod/Mod.tom                   (the Tom mapping)`
`mod/ModAbstractType.java      (abstract class for the `“`Mod`”` module)`
`mod/types/Nat.java            (abstract class for the `“`Nat`”` sort)`
`mod/types/nat/List.java      \`
`mod/types/nat/ConsList.java   \`
`mod/types/nat/EmptyList.java  / Implementation for the operator `“`List`”
`mod/types/nat/Plus.java       (Implementation for `“`Plus`”`)`
`mod/types/nat/Suc.java        (Implementation for `“`Suc`”`)`
`mod/types/nat/Zero.java       (Implementation for `“`Zero`”`)`

The `ModAbstractType` class declares generic methods shared by all operators in the `Mod` module:

```java
public aterm.ATerm toATerm()
public String symbolName()
public String toString()
```

The `mod/types/Nat.java` class provides an abstract class for all operators in the `Nat` sort, implementing the `ModAbstractType` and contains the following methods. First, the methods for checking the root operator, returning `false` by default:

```java
public boolean isConsList()
public boolean isEmptyList()
public boolean isPlus()
public boolean isSuc()
public boolean isZero()
```

Then getter methods, throwing an `UnsupportedOperationException` by default, as the slot may not be present in all operators. This is convenient since at the user level, we usually manipulate objects of sort `Nat`, without casting them to more specific types.

```java
public mod.types.Nat getpred()
public mod.types.Nat getlhs()
public mod.types.Nat getHeadList()
public mod.types.Nat getrhs()
public mod.types.Nat getTailList()
```

The `fromTerm` static method allows Gom data structure to be interoperable with `ATerm`
`public static mod.types.Nat fromTerm(aterm.ATerm trm)`

The operator implementations redefine all or some getters for the operator to return its subterms. It also provides a static `make` method to build a new tree rooted by this operator, and implements the `tom.library.sl.Visitable` interface. For instance, in the case of the `Plus` operator, the interface is:

```java
public static Plus make(mod.types.Nat lhs, mod.types.Nat rhs)
public int getChildCount()
public tom.library.sl.Visitable getChildAt(int index)
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v)
```

completed with the methods from the `Nat` class and the `ModAbstractType`.

The operators implementing the variadic operator both extend the `List` class, which provides list related methods, such as `length`, `fromArray`, `getCollection` and `reverse`. The `getCollection` method produces a collection of objects of the codomain type corresponding to the list elements, while the `reverse` method returns the list with all elements in reverse order. The static `fromArray` method produces a list from an array of objects of the codomain type. The `List` class for our example then contains:

```java
public int length()
public mod.types.Nat reverse()
public java.util.Collection<mod.types.Nat> getCollection()
public static mod.types.Nat fromArray(mod.types.Nat[] array)
```

The `List` class implements also the `java.util.Collection` interface:

```java
public int size()
public boolean containsAll(java.util.Collection c)
public boolean contains(Object o)
public boolean isEmpty()
public java.util.Iterator<mod.types.Nat> iterator()
public Object[] toArray()
public <T> T[] toArray(T[] array)
```

Note that all the methods of the `java.util.Collection` that make the list mutable (for example, the `removeAll` method) are not implemented and thus throw an `UnsupportedOperationException`.

For the `ConsList` class, we obtain:

```java
/* the constructor */
public static ConsList make(mod.types.Nat _HeadList, mod.types.Nat _TailList) { ... }
public String symbolName() { ... }
/* From the "Nat" class */
public boolean isConsList() { ... }
public mod.types.Nat getHeadList() { ... }
public mod.types.Nat getTailList() { ... }
/* From the "ModAbstractType" class */
public aterm.ATerm toATerm() { ... }
public static mod.types.Nat fromTerm(aterm.ATerm trm) { ... }
/* The tom.library.sl.Visitable interface */
public int getChildCount() { ... }
public tom.library.sl.Visitable getChildAt(int index) { ... }
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) { ... }
/* The MuVisitable interface */
public tom.library.sl.Visitable setChilds(tom.library.sl.Visitable[] childs) { ... }
```

Hooks to alter the generated API
--------------------------------

There exist four other hooks import, interface, block and mapping that offer possibilities to enrich the generated API. Contrary to make and make_insert, these hooks have no parameters. Moreover, they can be associated not only to an operator but also to a module or a sort.

<div class="center">
|                |     |                                                                     |
|:---------------|:---:|:--------------------------------------------------------------------|
| HookDefinition | ::= | HookType : HookOperation                                            |
| HookType       | ::= | OperatorName                                                        |
|                |  ∣  | module ModuleName                                                   |
|                |  ∣  | sort SortName                                                       |
| HookOperation  | ::= | interface() { Identifier (, Identifier )\* <nowiki>lt;/nowiki&gt;}} |
|                |  ∣  | import() { JavaImports <nowiki>lt;/nowiki&gt;}}                     |
|                |  ∣  | block() { TomCode <nowiki>lt;/nowiki&gt;}}                          |
|                |  ∣  | mapping() { TomMapping <nowiki>lt;/nowiki&gt;}}                     |

</div>
There are few constraints on the form of the code in these hooks:

-   for import, the code is a well-formed block of <font color="purple">Java</font> imports.
-   for interface, the code is a well-formed list of <font color="purple">Java</font> interfaces.
-   for block, the code is a well-formed <font color="purple">Java</font> block which can contain Tom code.
-   for mapping, the code is a well-formed Tom block composed only of mappings.

The code given in the hook is just added at the correct position in the corresponding <font color="purple">Java</font> class:

-   for a module ModuleName, in the abstract class named ModuleName`AbstractType` (for now, you can only use this hook with the current module),
-   for a sort SortName, in the abstract class named SortName in the package types,
-   for an operator OperatorName of sort SortName, in the class named SortName in the package `types/`SortName.

In the case of mapping hooks, the corresponding code is added to the mapping generated for the signature.

```java
module Expressions
imports String int
abstract syntax
  Bool = True()
       | False()
       | Eq(lhs:Expr, rhs:Expr)
  Expr = Id(stringValue:String)
       | Nat(intValue:int)
       | Add(lhs:Expr, rhs:Expr)
       | Mul(lhs:Expr, rhs:Expr)

  True:import() {
    import tom.library.sl.*;
    import java.util.HashMap;
  }
  sort Bool:interface() { Cloneable, Comparable }
  module Expressions:block() {
    %include{ util/HashMap.tom }
    %include{ sl.tom }
    %strategy CollectIds(table:HashMap) extends Identity() {
      visit Expr {
        Id(value) -> {
          table.put(`value,getEnvironment().getPosition());
        }
      }
    }
    public static HashMap collect(Expr t) {
      HashMap table = new HashMap();
      `TopDown(CollectIds(table)).apply(t);
      return table;
    }
  }
```

## Gom Antlr Adaptor

Combining Tom, Gom and Antlr is easy. The tool GomAntlrAdaptor takes a Gom signature as input and generates an adaptor to convert an AST built by Antlr into a Gom tree.

Let us consider a simple grammar:

``` java
grammar Gram;

@header { package parser; }
@lexer::header { package parser; }

ruleset : rule (rule)* EOF ;

rule : 'a' | 'b' ;

WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;
SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
```

Our idea is to use the “rewrite rule” mechanism provided by Antlr to build an AST. Therefore, we consider the following signature (i.e. the node of the AST):

```java
module parser.Rule
abstract syntax

Term = A()
     | B()
     | Conc(a:Term,b:Term)
```

Three things have to be done:

-   set `output=AST`, and `ASTLabelType=Tree`
-   declare the list of nodes (tokens) that will be used in the rewrite rules
-   define the rewrite rules

``` java
grammar Gram;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=RuleTokens;
}

@header { package parser; }
@lexer::header { package parser; }

ruleset : (rule -> rule) (a=rule -> ^(Conc $ruleset $a))* EOF ;

rule : 'a' -> ^(A) | 'b' -> ^(B) ;

WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;
SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
```

In the `Main` file, note the use of `GramRuleAdaptor.getTerm(b)` to convert the tree built by Antlr into a Gom tree:

``` java
package parser;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import parser.rule.RuleAdaptor;

public class Main {
  public static void main(String[] args) {
    try {
      GramLexer lexer = new GramLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      GramParser parser = new GramParser(tokens);
      // Parse the input expression
      Tree b = (Tree) parser.ruleset().getTree();
      System.out.println("Result = " + RuleAdaptor.getTerm(b)); // name of the Gom module + Adaptor
    } catch (Exception e) {
      System.err.println("exception: " + e);
      return;
    }
  }
}
```

To compile this example:

-   create a directory named `parser`,
-   create all three files described above (`Rule.gom`, `Gram.g` and `Main.t`),
-   execute the following commands:

```sh
gom -d gen parser/Rule.gom
gomantlradaptor -d gen -p parser parser/Rule.gom
java org.antlr.Tool -o gen -lib gen/parser/rule parser/Gram.g
tom -d gen parser/Main.t
cd gen
javac parser/Main.java
```

Note: On WIndows systems, replace the ANTLR command by : `java org.antlr.Tool -o gen/parser -lib gen/parser/rule parser/Gram.g`.

## Strategies support (**\***)

The data structures generated by Gom do provide support for the strategy language of Tom. We assume in this section the reader is familiar with the support of Tom as described in the [Strategies](Strategies.md) chapter, and [illustrated in the tutorial](../tutorial/Language_Basics_–_Level_3.md).

Basic strategy support
----------------------

The data structure generated by the Gom compiler provides support for the *sl library* implementing strategies for Tom. It is thus possible without any further manipulation to use the strategy language with Gom data structures.

This strategy support is extended by the Gom generator by providing congruence and construction elementary strategies for all operators of the data structure. Those strategies are made available through a Tom mapping `_`<module>`.tom` generated during Gom compilation.

Congruence strategies
---------------------

The congruence strategies are generated for each operator in the Gom module. For a module containing a sort

```java
Term = a()
     | f(lt:Term,rt:Term)
     | l(Term*)
```

congruence strategies are generated for `a`, `f` and `l` operators, respectively called `_a`, `_f` and `_l`. The semantics of those elementary strategies is as follows:

<div class="center">
|                   |     |                                                                       |
|:------------------|:---:|:----------------------------------------------------------------------|
| (_a)\[t\]        |  ⇒  | t if t equals `a`, *failure* otherwise                                |
| (_f(s1,s2))\[t\] |  ⇒  | f(t1’,t2’) if t = f(t1,t2), with (s1)\[t1\] ⇒t1’ and (s2)\[t2\] ⇒t2’, |
|                   |     | *failure* otherwise                                                   |
| (_l(s))\[t\]     |  ⇒  | l(t1’,...,tn’) if t = l(t1,...,tn), with (s)\[ti\] ⇒ti’,              |
|                   |     | *failure* otherwise                                                   |

</div>
Thus, congruence strategies allows to discriminate terms based on how they are built, and to develop strategies adopting different behaviors depending on the shape of the term they are applied to.

Congruence strategies are commonly used to implement a specific behavior depending on the context (thus, it behaves like a complement to pattern matching). For instance, to print all first children of an operator `f`, it is possible to use a generic `Print()` strategy under a congruence operator.

```java
Strategy specPrint1 = `TopDown(_f(Print(),Identity()));
```

When applied to variadic operators, a congruence strategy acts over all theirs elements. So, the strategy `Print()` is applied under a congruence operator printing all elements of a variadic operator `l`.

```java
Strategy specPrint2 = `TopDown(_l(Print()));
```

Also, congruence strategies are used to implement `map` like strategies on tree structures. Consider a signature with `List = Cons(e:Element,t:List) | Empty()`, then we can define a `map` strategy as:

```java
 Strategy map(Strategy arg) {
  return `mu(MuVar("x"),
    Choice(_Empty(),_Cons(arg,MuVar("x")))
  );
}
```

The congruence strategy generated for variadic operators is similar to the `map` strategy. It applies the strategy passed as argument to all subterms of the considered variadic operator.

Construction strategies
-----------------------

Gom generated strategies’ purpose is to allow to build new terms for the signature at the strategy level. Those strategies do not use the terms they are applied to, and simply create a new term. Their semantics is as follows:

<div class="center">
|                       |     |                                                    |
|:----------------------|:---:|:---------------------------------------------------|
| (Make_a)\[t\]        |  ⇒  | a                                                  |
| (Make_f(s1,s2))\[t\] |  ⇒  | f(t1,t2) if (s1)\[null\] ⇒t1 and (s2)\[null\] ⇒t2, |
|                       |     | *failure* otherwise                                |

</div>
We can note that as the sub-strategies for `Make_f` are applied to the `null` term, it is required that those strategies are themselves construction strategies and do not examine their argument.

These construction strategies, combined with congruence strategies can be used to implement rewrite rules as strategies. For instance, a rule *f*(*a*,*b*) → *g*(*a*,*b*) can be implemented by the strategy:

```java
Strategy rule = `Sequence(
  _f(_a(),_b()),
  _Make_g(_Make_a(),_Make_b())
);
```

## Fresh Gom (**\*\***)

Fresh Gom is an extension of Gom which adds capabilities very similar to that of [AlphaCaml](http://cristal.inria.fr/~fpottier/alphaCaml/) developed by François Pottier for the AlphaCaml programming language. In short, it extends the syntax of Gom in order to allow the specification of binding information (like : “this constructor field is in fact a variable bound in these other fields”) and generates all the boring machinery to deal with fresh variables, alpha-conversion, etc. Moreover, it integrates smoothly with Tom by generating mappings which ensure that every time a constructor is matched, its concerned variables are refreshed. As an example, assume we have defined the grammar of lambda expressions using Fresh Gom. Then, when the following match instruction is run,

```java
%match( `Lambda(abs(x,u)) ) {
  Lambda(abs(y,v)) -> { ... }
}
```

a fresh `y` is generated (fresh here means an unique identifier that has never been generated before), and `v` is `u` where all instances of `x` have been replaced by `y`.

Just like AlphaCaml, Fresh Gom allows for complex binding specifications involving several variable sorts (called *atoms* in the nominal logic jargon) and for nested patterns (think for instance of the meta-representation of a rewrite rule, where the variables of the left-hand side are bound in the right-hand side: you do not statically know how many - neither at which depth - variables will appear in a left-hand side). The semantics of these constructs, although intuitive enough to be grasped as is, is formally defined in [this paper](http://cristal.inria.fr/~fpottier/publis/fpottier-alphacaml.pdf) by François Pottier.

### Grammar

As mentioned, Fresh Gom extends the Gom grammar exposed above.

<div class="center">
|                       |                  |                                                                   |
|:----------------------|:----------------:|:------------------------------------------------------------------|
|                       |   Updated Rules  |                                                                   |
| Grammar               |        ::=       | abstract syntax (... ∣ AtomDecl)\*                                |
| TypeDefinition        |        ::=       | ... ∣ SortName \[binds AtomName+\] = PatternDefinition            |
| OperatorDefinition    |        ::=       | ... ∣ OperatorName( &lt; SortName &gt; \* )SlotDefinition)\*\])   |
| SlotDefinition        |        ::=       | ... ∣ SlotName: &lt; SortName &gt;                                |
|                       | Additional Rules |                                                                   |
| AtomDecl              |        ::=       | atom AtomName                                                     |
| AtomName              |        ::=       | Identifier                                                        |
| PatternDefinition     |        ::=       | OperatorName(\[PatternSlotDefinition(,PatternSlotDefinition)\*\]) |
|                       |         ∣        | OperatorName( SortName\* )                                        |
| PatternSlotDefinition |        ::=       | \[inner ∣ outer ∣ neutral\] SlotName: SortName                    |

</div>
### Description

### Atoms

For each atom declared, Fresh Gom generates a class and a Tom sort of the same name. For example, a Gom module containing the line

```java
atom LambdaVar
```

will generate a class `LambdaVar` along with the declaration of a tom sort

```java
%typeterm LambdaVar { implements LambdaVar }
```

at the usual places. Everything is as it were an usual Gom sort for which no constructors had been declared.

The implementation of these atoms remains opaque to the user. The unique way to create an atom of name `AtomName` is to use the following generated static method which creates a fresh identifier.

```java
public abstract class AtomName {
  public static AtomName freshAtomName() { ... }
}
```

### Raw Sorts and Constructors

Each sort concerned by Fresh Gom (i.e. connected to an atom in the sort dependency graph) is replaced by two other ones:

-   a *normal* one, which is identical, except that every field whose sort is an atom is replaced by a String;
-   a *raw* one, which has exactly the same definition, but whose name and constructor names have been prefixed by “Raw”.

As an example, everything is as the following declarations

```java
atom EVar

Expr = Plus(e1:Expr,e2:Expr)
     | Lit(n:int)
     | Var(v:EVar)
```

were replaced by the code below.

```java
EVar = /* non legit gom */

Expr = Plus(e1:Expr,e2:Expr)
     | Lit(n:int)
     | Var(v:EVar)

RawExpr = RawPlus(e1:RawExpr,e2:RawExpr)
        | RawLit(n:int)
        | RawVar(v:String)
```

The “raw” sorts are meant to be used at parse and pretty-printing time and do not generate any additional method than regular Gom sorts except that for conversion to “normal” sorts. The “normal” sorts are the ones to work with for all tasks jeopardized by variable capture problems: evaluation, typechecking, etc. They generate several useful methods used by the mechanisms described in the next section.

Conversion methods for converting one format into the other are generated in the classes representing the sorts.

```java
public abstract class SortName {
  public RawSortName export() { ... }
}

public abstract class RawSortName {
  public SortName convert() { ... }
}
```

While `export()` always succeeds, a call to `convert()` may miserably fail (`RuntimeException` for the current release) if the subject contains free variables. If one wishes to provide a dictionary from the free variables to objects of “normal” sort in order to convert a non-closed term, specialized `_convert(...)` versions of `convert()` are also generated. Their signature depends on the involved atoms. For example, the following method is generated for the signature above.

```java
public abstract class RawExpr {
  public abstract Expr _convert(tom.library.freshgom.ConvertMap<EVar> EVarMap);
}
```

The `tom.library.freshgom.ConvertMap` is fully documented in the Tom library API.

### Informal Semantics

As in Cαml, the sorts defined in a Fresh Gom module are of two distinct kinds:

-   the sorts the definition of which is preceded by `binds id1 id2 ..` are called *pattern sorts*;
-   the other ones are called *expression sorts*.

When pattern sorts are mentioned in an expression sort constructor, they must be placed inside brackets &lt; &gt;. This indicates a *refresh point*, i.e. a field that will be refreshed whenever the enclosing constructor is deconstructed (using match). For example, consider this excerpt of a module defining a simply typed lambda-calculus:

```java
atom LVar

Type  = Atomic(p:String)
      | Arrow(ty1:Type,ty2:Type)

LTerm = App(t1:LTerm,t2:LTerm)
      | Lam(a:<Abs>)
      | Var(x:LVar)

Abs binds LVar = abs(x:LVar, neutral ty:Type, inner u:LTerm)
```

Since the pattern sort `Lam` is declared to bind the atoms of sort `LVar`, every time the constructor `Lam` is deconstructed, the atoms of sort `LVar` in the field `a` are refreshed. The canonical way to deconstruct such a constructor is thus to use a nested pattern as follows.

```java
%match(t) {
  App(u,v)            -> { /* nothing special happens */ }
  Lambda(abs(x,ty,u)) -> { /* x is fresh */ }
  Var(x)              -> { /* nothing special happens */ }
}
```

Who binds what and where is specified using the neutral, inner and outer keywords. In the example above, in the `abs` constructor, `x` is in *pattern position*, which means that the atoms of sort `LVar` it contains (in that case, `x` itself) may be bound in the other fields of `abs`. If it is the case and how is specified the following way:

-   inner means that it is bound;
-   outer means that it is not bound (useful for representing let statements for instance : in `let x = u in t`, `x` is **not** bound in u);
-   neutral means it is irrelevant (here since there are no variables in types).

### Compilation

The Fresh Gom mode is enable using the `fresh` option of Gom, as described in the [tool usage documentation](../tools/Using_Gom.md). It is not compatible with the `termgraph` option.

## Examples

We present two commented examples: an interpreter for System F and a signature for a subset of ML. While the first example illustrates the use of several atom sorts, the second illustrates nested pattern sorts.

### System F

Let us represent [Girard's System F](http://en.wikipedia.org/wiki/System_F), also known as the second-order lambda calculus. This calculus is used as the intermediate typed representation of many functional programming languages. It has the particularity of involving two kinds of variables : terms (x,y,z...) and types (X,Y,Z...) variables, which will illustrate this particular aspect of Fresh Gom. The language is generated by the following grammar:

`t,u ::= x | λx:A.t | ΛX.t | (t u) | (t A)`
`A,B ::= X | ∀X.A | A → B`

Formal presentations of the system usually go on with

-   in `λx:A.t`, the term variable `x` is bound in the term `t`;
-   in `ΛX.t`, the type variable `X` is bound in the term `t`;
-   in `∀X.A`, the type variable `X` is bound in the type `A`.

We will represent these three kind of abstraction using three pattern sorts: `TermTermAbs`, `TypeTermAbs` and `TypeTypeAbs`. The previous three points will be expressed using the inner keyword. Note that in `λx:A.t`, the question of wether the term variable `x` is bound in the type `A` is irrelevant. We will express this fact using the neutral keyword. The remaining of the grammar is standard Gom code.

```java
module SystemF
imports String int
abstract syntax

atom TermVar
atom TypeVar

LTerm =
 | LVar(x:TermVar)
 | LLam(a1:<TermTermAbs>)
 | LApp(u:LTerm,v:LTerm)
 | TLam(a2:<TypeTermAbs>)
 | TApp(t:LTerm,A:Type)

TermTermAbs binds TermVar = abs1(x:TermVar, neutral A:Type, inner t:LTerm)
TypeTermAbs binds TypeVar = abs2(X:TypeVar, inner t:LTerm)

Type =
 | TVar(X:TypeVar)
 | Forall(a3:<TypeTypeAbs>)
 | Arrow(A:Type,B:Type)

TypeTypeAbs binds TypeVar = abs3(X:TypeVar, inner A:Type)
```

Let us now write a pretty-printer for the lambda-terms. Since pretty-print needs to manipulate raw variable names (strings), we will work in raw mode.

```java
import systemf.types.*;

public class Pretty {

  %include { systemf/SystemF.tom }

  public static String pretty(RawLTerm s) {
    %match(s) {
      RawLVar(x)              -> { return `x; }
      RawLLam(Rawabs1(x,T,t)) -> { return %[(fn @`x@:@`pretty(T)@ -> @`pretty(t)@)]%; }
      RawLApp(t,u)            -> { return %[(@`pretty(t)@ @`pretty(u)@)]%; }
      RawTLam(Rawabs2(T,t))   -> { return %[(FN @`T@ -> @`pretty(t)@)]%; }
      RawTApp(t,T)            -> { return %[(@`pretty(t)@ @`pretty(T)@)]%; }
    }
    throw new RuntimeException("non-exhaustive patterns");
  }

  public static String pretty(RawType s) {
    %match(s) {
      RawTVar(X)              -> { return `X; }
      RawForall(Rawabs3(X,T)) -> { return %[(forall @`X@, @`pretty(T)@)]%; }
      RawArrow(A,B)           -> { return %[(@`pretty(A)@ -> @`pretty(B)@)]%; }
    }
    throw new RuntimeException("non-exhaustive patterns");
  }

  public static void main(String args[]) {
    RawLTerm z = `RawLVar("z");
    RawLTerm s = `RawLVar("s");
    RawLTerm n = `RawLVar("n");
    RawType  A = `RawTVar("A");
    RawType  B = `RawTVar("B");

    RawLTerm zero =
      `RawTLam(Rawabs2("A",
          RawLLam(Rawabs1("z",A,
              RawLLam(Rawabs1("s",RawArrow(A,A),z))))));
    RawLTerm succ =
      `RawLLam(Rawabs1("n",RawForall(Rawabs3("A",RawArrow(RawArrow(A,A),RawArrow(A,A)))),
            RawTLam(Rawabs2("B",
                RawLLam(Rawabs1("z",B,
                    RawLLam(Rawabs1("s",RawArrow(B,B),
                        RawLApp(RawLApp(RawTApp(n,B),RawLApp(s,z)),s)))))))));

    System.out.println(pretty(zero));
    System.out.println(pretty(succ));
  }
}
```

Let us now write an interpreter. The reduction rules of System F are the term-level and type-level β-reductions.

`(λx:A.t) u → t{x := u}`
`(ΛX.t) A   → t{X := A}`

The key point is the reduction system `HeadBeta`. Thanks to the mapping generated by Fresh Gom, every time an abstraction is matched, the variable are refreshed so that we avoid their potential capture. This time, we have to convert the raw term into a “normal” one to benefit of this feature.

```java
import systemf.types.*;
import tom.library.sl.*;

public class Eval {

  %include { systemf/SystemF.tom }
  %include { sl.tom }

  /* returns t{x := u} */
  public static LTerm subst(LTerm t, TermVar x, LTerm u) {
    %match(t) {
      LVar(y)           -> { return `y == x ? u : `LVar(y); }
      LLam(abs1(y,T,v)) -> { return `LLam(abs1(y,T,subst(v,x,u))); }
      LApp(v,w)         -> { return `LApp(subst(v,x,u),subst(w,x,u)); }
      TLam(abs2(T,v))   -> { return `TLam(abs2(T,subst(v,x,u))); }
      TApp(v,T)         -> { return `TApp(subst(v,x,u),T); }
    }
    throw new RuntimeException("non-exhaustive patterns");
  }

  /* returns t{X := A} */
  public static LTerm subst(LTerm t, TypeVar X, Type A) {
    %match(t) {
      LVar(x)           -> { return `LVar(x); }
      LLam(abs1(x,T,u)) -> { return `LLam(abs1(x,subst(T,X,A),u)); }
      LApp(u,v)         -> { return `LApp(subst(u,X,A),subst(v,X,A)); }
      TLam(abs2(T,u))   -> { return `TLam(abs2(T,subst(u,X,A))); }
      TApp(u,T)         -> { return `TApp(subst(u,X,A),subst(T,X,A)); }
    }
    throw new RuntimeException("non-exhaustive patterns");
  }

  /* returns A{X := B} */
  public static Type subst(Type A, TypeVar X, Type B) {
    %match(A) {
      TVar(Y)           -> { return `Y == X ? B : `TVar(Y); }
      Forall(abs3(X,T)) -> { return `Forall(abs3(X,subst(T,X,B))); }
      Arrow(T,U)        -> { return `Arrow(subst(T,X,B),subst(U,X,B)); }
    }
    throw new RuntimeException("non-exhaustive patterns");
  }

  /* beta reductions */
  %strategy HeadBeta() extends Fail() {
    visit LTerm {
      LApp(LLam(abs1(x,T,t)),u) -> subst(t,x,u)
      TApp(TLam(abs2(T,t)),A)   -> subst(t,T,A)
    }
  }

  /* call by name evaluation */
  public static LTerm eval(LTerm t) {
    try { return `Outermost(HeadBeta()).visit(t); }
    catch(VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  public static void main(String args[]) {
    RawLTerm zero = ... // as before
    RawLTerm succ = ... // as before
    RawLTerm rawThree = `RawLApp(succ,RawLApp(succ,RawLApp(succ,zero)));

    // convert to internal representation
    LTerm three = rawThree.convert();
    // eval
    LTerm res = eval(three);
    // export to raw representation
    RawLTerm rawRes = res.export();

    System.out.println(Pretty.pretty(rawRes));
  }
}
```

It is worth noticing that by changing `Outermost` to `Innermost` we obtain a call-by-value interpreter. Although this approach is highly inefficient for writing a real interpreter, Fresh Gom suits the needs of prototypes and is perfectly adapted to the description of source-to-source transfomations (e.g. CPS, see the examples of the Tom distribution).

### Mini ML

The full example can be found in the Tom distribution. We focus here on how to encode `(case .. of ..)` expressions with the help of Fresh Gom.

```java
module lambda
imports int String
abstract syntax

atom LVar

LType = Atom(n:String)
      | Arrow(t1:LType,t2:LType)
      | TypeVar(i:int)

LTerm = App(t1:LTerm,t2:LTerm)
      | Abs(a:<Lam>)
      | Let(b:<Letin>)
      | Fix(c:<Fixpoint>)
      | Var(x:LVar)
      | Constr(f:String, children:LTermList)
      | Case(subject:LTerm,rules:Rules)

Rules = RList(<Clause>*)

/* all the variables of p are bound in t */
Clause binds LVar = Rule(p:Pattern, inner t:LTerm)

Pattern binds LVar = PFun(neutral f:String, children:PatternList)
                   | PVar(x:LVar, neutral ty:LType)

LTermList = LTList(LTerm*)

PatternList binds LVar = PList(Pattern*)

Lam binds LVar = lam(x:LVar, neutral ty:LType, inner t:LTerm)
Letin binds LVar = letin(x:LVar, outer u:LTerm, inner t:LTerm)
Fixpoint binds LVar = fixpoint(x:LVar, neutral ty:LType, inner t:LTerm)
```

The key definition is that of `Clause`, which declares that all the atoms of sort `LVar` present in the pattern `p` are bound in the term `t`. Therefore, when deconstructing a `Clause` using the match construct of Tom, all the variables in the left-hand side of the fetched clauses are refreshed in their corresponding right-hand side.

## Term-Graph Rewriting (**\*\***)

A term-graph is a term where subterms can be shared and where there may be cycles. Gom offers support to define term-graphs and term-graph rule systems. There exist several ways to define term-graphs but in our case, we propose to represent term-graphs by terms with pointers. These pointers are defined by a relative path inside the term. All the formal definitions can be found in this [paper](http://hal.inria.fr/inria-00173535/fr/).

In order to use term-graph rewriting, it is necessary to compile the Gom signatures with the `termgraph` option, as described in the [tool usage documentation](../tools/Using_Gom.md).

Term-Graph Data-Structures
--------------------------

When defining a Gom algebraic signature, it is possible to construct term-graphs on these signature using the option `termgraph`. In this case, the signature is automatically extended to manage labels. For every sort, `Term` for instance, two new constructors are added:

```java
LabTerm(label:String,term:Term)
RefTerm(label:String)
```

With these two new constructors, users can define term-graphs as labelled terms:

```java
Term cyclicTerm = `LabTerm("l",f(RefTerm("l")));
Term termWithSharing = `g(RefTerm("a"),LabTerm("a",a()));
```

From this labelled term, users can obtain the term-graph representation with paths using the `expand` method. This method must be called before applying a term-graph strategy.

## Term-Graph Rules

Using the hook `graphrules`, it is possible to define a set of term-graph rules. The first parameter (MyGraphStrat) is the name of the strategy associated to the set of rules. The second parameter is the default strategy (Identity in the following example). The left-hand and right-hand sides of these rules are term-graphs. A set of rules can only be associated to a given sort.

```java
sort Term: graphrules(MyGraphStrat,Identity) {
  g(l:a(),&l) -> f(b())
  f(g(g(a(),&l),l:x)) -> g(ll:b(),&ll) if b()<<x
}
```

In the rules, sharings and cycles are not represented by the constructor `LabTerm` and `RefTerm` but using a light syntax. `l:t` is equivalent to `LabTerm(l,t)` and `&l` corresponds to `RefTerm(l)`.

Contrary to classical term-graph rewriting, it is possible to reuse a label from the left-hand side in the right-hand side in order to obtain side effects. This feature is inspired from Rachid Echahed’s [formalism](http://hal.archives-ouvertes.fr/ccsd-00004558).

```java
sort Term: graphrules(SideEffect,Identity) {
  f(l:a()) -> g(&l,l:b())
}
```

This set of rules is translated into a Tom `%strategy` that can be used in a Tom program:

```java
Term t = (Term) `g(RefTerm("a"),LabTerm("a",a())).expand();
`TopDown(Term.MyGraphStrat()).visit(t)
```
