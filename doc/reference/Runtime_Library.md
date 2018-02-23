---
title: Documentation:Runtime Library
permalink: /Documentation:Runtime_Library/
---

Java API
========

The documentation of the API is available [here](/Development:IndexAPI "wikilink")

Predefined mappings
===================

Builtin sorts
-------------

The system comes with several predefined signature-mappings for <font color="purple">C</font>, <font color="purple">Java</font> and <font color="purple">Caml</font>. Among them, let us mention:

-   `boolean.tom` (true or false)
-   `char.tom` (written ’a’, ’b’, *etc.*)
-   `double.tom` (using grammar for double)
-   `float.tom` (using grammar for float)
-   `int.tom` (written 1, 2, 3, *etc.*)
-   `long.tom` (written 1l or 1L, *etc.*)
-   `string.tom` (written “a”, “ab”, *etc.*)

These mappings define, for each builtin sort of the host language, an algebraic sort that can be used in a `%match` or a signature definition construct. Thus, builtin values, such as `f(5)`, `g(’a’)` or `h(`“`foo`”`)`, can be used in patterns.

The `string.tom` mapping is interesting because it provides an associative operator (`concString`) which allows the programmer to consider a string as a list of characters. Thus, the string “`foo`” can be seen as the algebraic object `concString(’f’,’o’,’o’)`. By using this mapping, it becomes possible to perform pattern matching against the content of a string. The pattern `concString(’f’,X*)` will match any string which begins with the character `’f’`. By using the unnamed-symbol capability, this pattern can be written: `(’f’,X*)` instead of `concString(’f’,X*)`.

To match any string which begins with the substring “`fo`”, the corresponding pattern should be `(’f’,’o’,X*)`. To simplify the definitions of such patterns, Tom supports an exception which allows the programmer to write `(’fo’,X*)` instead. Internally, the ill-formed character `’fo’` is expanded into the list `(’f’,’o’)`.

In addition to these mappings, several other predefined mappings come with the system:

-   `aterm.tom` and `atermlist.tom` provide a mapping to the and version of the ATerm Library
-   `caml/list.tom` provides a mapping to the builtin notion of List in
-   `java/dom.tom` provides a mapping to the {{java} version of the DOM Library

Java
----

To help manipulating data-structures, Tom provides several mappings for the Runtime Library. The naming convention follows ’s one:

`java`
`├── boolean.tom`
`├── char.tom`
`├── double.tom`
`├── float.tom`
`├── int.tom`
`├── long.tom`
`├── string.tom`
`├┬─ emf`
`│└── ecore.tom`
`└┬─ util`
`    ├── ArrayList.tom`
`    ├── HashMap.tom`
`    ├── HashSet.tom`
`    ├── LinkedList.tom`
`    ├── MapEntry.tom`
`    ├── Object.tom`
`    ├── TreeMap.tom`
`    ├── TreeSet.tom`
`    └┬─ types`
`        ├── AbstractCollection.tom`
`        ├── AbstractList.tom`
`        ├── AbstractSequentialList.tom`
`        ├── AbstractSet.tom`
`        ├── ArrayList.tom`
`        ├── Collection.tom`
`        ├── HashMap.tom`
`        ├── HashSet.tom`
`        ├── LinkedHashSet.tom`
`        ├── LinkedList.tom`
`        ├── List.tom`
`        ├── Map.tom`
`        ├── Object.tom`
`        ├── Set.tom`
`        ├── Stack.tom`
`        ├── TreeMap.tom`
`        ├── TreeSet.tom`
`        ├── Vector.tom`
`        └── WeakHashMap.tom`

The directory `types` contains only `%typeterm` declarations.

Strategies
==========

To support strategies, Tom provides a runtime library, called SL and implemented in `strategy.jar`. This library implements the elementary strategy combinators (Identity, Fail, All, One, Choice, Sequence, etc.) as well as basic support for the computation of positions described in section [Basic Strategy Combinators](/Documentation:Strategies#Basic_strategy_combinators "wikilink").

In addition, some predefined mapping are also available to allow the description of strategies:

-   `sl.tom`: maps the basic strategies classes, and provides a `mu` operator to express the recursion as well a `MuVar(v:String)` operator to allow the definition of complex strategies as described in Section [Strategy Library](/Documentation:Strategies#Strategy_library "wikilink").

Term viewer
===========

The class `tom.library.utils.Viewer` contains a set of methods to visualize visitable terms.

``` tom
/* dot representations */
// on the writer stream
public static void toDot(tom.library.sl.Visitable v, Writer w)
// on the standard output stream
public static void toDot(tom.library.sl.Visitable v)
  /* pstree-like representations */
// on the writer stream
public static void toTree(tom.library.sl.Visitable v, Writer w)
// on standard output stream
public static void toTree(tom.library.sl.Visitable v)
  /* gui display */
public static void display(tom.library.sl.Visitable v)
```

Note that these methods can also be used on strategies as they are also visitable.

XML
===

To support the transformation of <span style="font-variant: small-caps">Xml</span> documents, Tom provides a specific syntax for defining patterns, as well as several predefined mappings:

-   `dom.tom`: maps <span style="font-variant: small-caps">Xml</span> notation to a <font color="purple">Java</font> implementation of the DOM library
-   `dom_1.5`.tom: maps <span style="font-variant: small-caps">Xml</span> notation to a <font color="purple">Java</font> (version 1.5) implementation of the DOM library
-   `TNode.tom`: maps <span style="font-variant: small-caps">Xml</span> notation to an ATerm based representation, generated by Gom

See Section [XML pattern](/Documentation:Tom#XML_pattern "wikilink") for a detailed description of the <span style="font-variant: small-caps">Xml</span> facilities offered by Tom.

Bytecode transformation (**\***)
================================

To manipulate <font color="purple">Java</font> classes, Tom provides a library which supplies a Gom term usable by Tom out of a <font color="purple">Java</font> class. The library enables to define transformations of this term by strategic rewriting as well as functionalities to generate a new <font color="purple">Java</font> class from the modified term.

This approach is similar to [BCEL library](http://jakarta.apache.org/bcel) in the sense that we construct a complete representation of the class. But thanks to Gom, we obtain a very efficient structure with maximal sharing. Moreover, thanks to associative matching, we can easily express patterns on the bytecode and in this way, ease the definition of transformations.

The library generates a Gom term using the [ASM library](http://asm.objectweb.org). This term is a memory-efficient representation of the <font color="purple">Java</font> class, which can then be traversed and transformed using Tom. After translating the <font color="purple">Java</font> class into a Gom term, we use Tom features to define transformations and traversals and to obtain a new Gom term which can be transformed into a new <font color="purple">Java</font> class.

Predefined mapping
------------------

To support the analysis and transformation of <font color="purple">Java</font> bytecode programs, Tom provides several mappings:

-   `adt/bytecode/Bytecode.tom` provides an abstract syntax tree implementation to represent any <font color="purple">Java</font> bytecode program.
-   `adt/bytecode/_Bytecode.tom` contains the congruence strategies associated to the AST.
-   `java/bytecode/cfg.tom` defines new strategies that allows to explore the control flow graph.

Java classes as Gom terms
-------------------------

In order to represent bytecode programs, we have defined a Gom signature that allows us to represent any bytecode program by a typed term. Given a <font color="purple">Java</font> class, we use ASM to read the content and build an algebraic representation of the complete <font color="purple">Java</font> class. This approach is similar to BCEL. Contrary to ASM, this permits multi-pass or global analysis.

``` tom
module Bytecode
imports int long float double String
abstract syntax
TClass = Class(info:TClassInfo, fields:TFieldList,
                 methods:TMethodList)
...
TMethodList = MethodList(TMethod*)
TMethod = Method(info:TMethodInfo, code:TMethodCode)
TMethodCode = MethodCode(instructions:TInstructionList,
                           localVariables:TLocalVariableList,
                           tryCatchBlocks:TTryCatchBlockList)
...
TInstructionList = InstructionList(TInstruction*)
TInstruction = Nop()
             | Iload(var:int)
             | Ifeq(label:TLabel)
             | Invokevirtual(owner:String, name:String,
                               methodDesc:TMethodDescriptor)
...
```

The real signature (`adt/bytecode/Bytecode.tom`) contains more than 250 different constructors. The given signature shows that a class is represented by a constructor `Class`, which contains information such as name, packages, and imports. It also contains a list of fields and a list of methods. The latter is encoded using an associative operator `MethodList`. Similarly, a list of instructions is represented by the associative operator `InstructionList`. A method contains an `info` part and a `code` part. The `code` part is mainly composed by local variables and a list of instructions. Each bytecode instruction is represented by an algebraic constructor: `Nop`, `Iload`, *etc.*

In the package `tom.library.bytecode`, there exist two principal classes based on the ASM library:

-   the `BytecodeReader` class whose constructor takes the path of a <font color="purple">Java</font> class. Its main method is `getTClass()` that returns the Gom representation of the <font color="purple">Java</font> class.
-   the `BytecodeGenerator` class whose main method named `toBytecode(TClass c)` returns a byte array that corresponds to the Bytecode of the Gom term `c`.

``` tom
public static byte[] transform(String file){
  BytecodeReader br = new BytecodeReader(file);
  TClass c = br.getTClass();
  TClass cc = transform(c);
  BytecodeGenerator bg = new BytecodeGenerator();
  return bg.toBytecode(cc);
}
```

Simulation of control flow by Strategies
----------------------------------------

When considering a Bytecode program, with the <font color="purple">sl</font> library, we can just define traversals without considering the control flow. Our suggestion is to use strategies in order to simulate the control flow during the traversal of the list of instructions. In the Tom language, the rules and the control are completely separated so an alternative for representing control flow graphs (CFG) is to use the control to indicate what is the possible following instruction. We have seen in the previous section that to apply a strategy to children, there exist two combinators `All` and `One`.

In the mapping `bytecode/cfg.tom`, the two combinators `AllCfg` and `OneCfg` behave almost as `All` and `One` but the considered children are the following instructions in the Control flow graph instead of the following instruction in the list. For example, the `Goto` instruction has one child with respect to the control flow graph (the instruction corresponding to the label). An `If_XX` instruction has two children: the one which satisfies the expression, and the one that does not.

[Category:Documentation](/Category:Documentation "wikilink")