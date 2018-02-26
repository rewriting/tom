# Tom
## Notations

The syntax of the language is given in BNF-like notation. Terminal and non-terminal symbols are set in typewriter font (`'like this'`). Square brackets \[...\] denote optional components. Parentheses with a trailing star sign (...)\* denotes zero, one or several repetitions of the enclosed components. Parentheses with a trailing plus sign (...)+ denote one or several repetitions of the enclosed components. Parentheses (...) denote grouping.

### Lexical conventions

```java
Identifier ::=	Letter ( Letter ∣ Digit ∣ '_' ∣ '-' )*
Integer    ::=	(Digit )+
Double     ::=	(Digit)+ ['.'] (Digit)* ∣ '.' (Digit)+
String     ::=	'"' (Letter ∣ ('\' ('n' ∣ 't' ∣ 'b' ∣ 'r' ∣ 'f' ∣ '\' ∣ '’' ∣ '"') ) )* '"'
Letter     ::=	'A' ... 'Z' ∣ 'a' ... 'z'
Digit      ::=	'0' ... '9'
Char       ::=	'’' (Letter ∣Digit) '’'
```

### Names
```java
SubjectName   ::=	Identifier
Type          ::=	Identifier
SlotName      ::=	Identifier
HeadSymbol    ::=	Identifier
              ∣		Integer
              |		Double
              ∣		String
              ∣		Char
VariableName  ::=	Identifier
AnnotedName   ::=	Identifier
LabelName     ::=	Identifier
FileName      ::=	Identifier
AttributeName ::=	Identifier
XMLName       ::=	Identifier
Name          ::=	Identifier
```
## Tom constructs

A Tom program is a Java extended by several new constructs such as `%match`, `%strategy`, `%include`, `%gom`, or `backquote`. For example, the following Tom program is correct:

```java
public class HelloWorld {
  %include { string.tom }
    public final static void main(String[] args) {
    String who = "World";
    %match(who) {
      "World" -> { System.out.println("Hello " + who); }
      _       -> { System.out.println("Don't panic"); }
    }
  }
}
```

### Tom program

A Tom program is a list of blocks, where each block is either a construct, or a sequence of characters (host language code). When compiling a program, the constructs are transformed into host language code, and the result is a valid host language program. For instance, in the previous example, `%include` and `%match` constructs are replaced by function definitions and instructions, making the resulting program a correct program.

Syntax of a program:

```java
Tom       ::= BlockList
BlockList ::=
			(	MatchConstruct
			∣	StrategyConstruct
			∣	BackQuoteTerm
			∣	IncludeConstruct
			∣	GomConstruct
			∣	TransformationConstruct
			∣	TypeTerm
			∣	Operator
			∣	OperatorList
			∣	OperatorArray
			∣	'{' BlockList '}'
			)*
```

- `MatchConstruct` is translated into a list of instructions. This construct may appear anywhere a list of instructions is valid in a Java program.

- `StrategyConstruct` is translated into a class definition. For a more detailed documentation concerning strategies, please refer to Chapter [Strategie](Strategies.md).

- `BackQuoteTerm` is translated into a function call.

- `IncludeConstruct` is replaced by the content of the file referenced by the construct. looks for include files in: `./packageName/`, `$TOM_HOME/share/jtom/` and <path>, where <path> is specified by option: `--import `<path>. If the file contains some constructs, they are expanded.

- `GomConstruct` allows to define a grammar. This construct is replaced by the content of the generated mapping. See Section [Gom construct](#gom-construct) and Chapter [Gom](Gom.md) for more details.

- `TransformationConstruct` allows to write easily models transformations. It is compiled as a complex strategy which is called as every classical ones. For the moment, only Java and EMF are supported. Note that this construct is new and only available as an alpha version.

- `TypeTerm`, as well as `Operator`, `OperatorList`, and `OperatorArray` are replaced by some functions definitions.

### Match construct

The `%match` construct () is one of the main contributions of . This construct can be seen as an extension of the construct in <font color="purple">C</font> or <font color="purple">Java</font>, except that patterns are no longer restricted to constants (chars or integers). Given an object (the subject) and a list of patterns, our goal is to find the first pattern that *matches* the subjects (i.e. that has a *compatible shape*). As in the construct, in a the type of each pattern is expected to be either the same or a subtype of that of the subject. More formally, a pattern is a term built over variables and constructors. The latter ones describe the *shape* of the pattern, whereas the variables are *holes* that can be instantiated to capture a value. When we consider the term *f*(*a*(),*g*(*b*())), it has to be viewed as a tree based data-structure with *f* as a root and *a*() the first child. Similarly, *b*() is the unique child of *g*, which is the second child of the root *f* . We say that the pattern *f*(*x*,*y*) matches this term (called subject), because we can give values to *x* and *y* such that the pattern and the subject become equal: we just have to assign *a*() to *x* and *g*(*b*()) to *y*. Finding this assignment is called matching and instantiating. This is exactly what is supposed to do. A pattern may of course contain subterms. Therefore, *f*(*x*,*g*(*b*())) or *f*(*a*(),*g*(*y*)) are valid patterns which match against the subject.

Assuming that `s` is a <font color="purple">Java</font> variable, referencing a term (the tree based object *f*(*a*(),*g*(*b*())) for example), the following construct is valid:

``` tom
%match(s) {
  f(a(),g(y)) -> { /* action 1: code that uses y */ }
  f(x,g(b())) -> { /* action 2: code that uses x */ }
  f(x,y)      -> { /* action 3: code that uses x and y */ }
}
```

The `%match` construct is defined as follows:

```java
MatchConstruct	::=	'%match' '(' MatchArguments ')' '{' ( PatternAction )* '}'
∣	'%match' '{' ( ConstraintAction )* '}'
MatchArguments	::=	[Type] Term ( ',' [Type] Term )*
PatternAction	::=	[LabelName':'] PatternList '->' '{' BlockList '}'
PatternList	::=	Pattern( ',' Pattern )* [ ('&&' ∣'||') Constraint ]
ConstraintAction	::=	Constraint '->' '{' BlockList '}'
Constraint	::=	Pattern '<<' [Type] Term
∣	Constraint '&&' Constraint
∣	Constraint '||' Constraint
∣	'(' Constraint ')'
∣	Term Operator Term
Operator	::=	'>' ∣'>=' ∣'<' ∣'<=' ∣'==' ∣'!='
```

A is composed of two parts:

-   a list of *subjects* (the arguments of `%match`). They can be declared with their respective types, for instance `Exp s` where `Exp` corresponds to the exact type or to a subtype of `s` in case of a downcast.
-   a list of : this is a list of pairs (pattern,action), where an action is a set of host language instructions which is executed each time a pattern matches the subjects.

Since version 2.6 of the language, an additional syntax, based on *constraints*, is proposed. In this case, the can be simply seen as a list of pairs (constraint,action) corresponding to from the above syntax. For instance, the example we presented before can now be written:

``` tom
%match {
  f(a(),g(y)) << s -> { /* action 1 : code that uses y */ }
  f(x,g(b())) << s -> { /* action 2 : code that uses x */ }
  f(x,y) << s      -> { /* action 3 : code that uses x and y */ }
}
```

The two versions are semantically equivalent. The expression `p << s` denotes a match constraint between the pattern `p` and the subject `s`. The advantage of this new syntax is the modularity that it offers. For instance, multiple constraints can be combined with the boolean connectors and , leading to a greater expressivity, sometimes closed to the one offered by regular expressions.

The two syntaxes are compatible. For instance the following construct is valid:

``` tom
%match(s) {
  f(a(),g(y)) && ( y << a() || y << b() ) -> { /* action 1 : code that uses y */ }
  f(x,g(b())) -> { /* action 2 : code that uses x */ }
}
```

For expository reasons, we consider that a `%match` construct is evaluated in the following way:

-   given a list of subjects (they correspond to objects only composed of constructors, therefore without variables, usually called ground terms), the execution control is transferred to the first whose patterns match the list of ground terms (in the case constraints are used, the execution control is transferred to the first whose is evaluated to true).

-   given such a (respectively ), its variables are instantiated and the associated semantic action is executed. The instantiated variables are bound in the underlying host-language, and thus can be used in the action part.
-   if the execution control is transferred outside the `%match` instruction (by a `goto`, `break` or `return` for example), the matching process is finished. Otherwise, the execution control is transferred to the next whose patterns match the list of ground terms (respectively to the next whose evaluates to true).
-   when there is no more whose patterns match the list of subjects (respectively no more whose evaluates to true), the `%match` instruction is finished, and the execution control is transferred to the next instruction.

The semantics of a match construct may remind the switch/case construct. However, there is a big difference. For instance, in we can have patterns (list-patterns) that can match in several ways a subject. Informally, when considering the subject *conc*(*a*(),*b*(),*c*()), and the pattern *conc*(_<sup>\*</sup>,*x*,_<sup>\*</sup>), there are three possible match for this problem: either *x*=*a*(), either *x*=*b*(), either *x*=*c*(). Note that _<sup>\*</sup> is a special *hole* which can capture any sublist of *conc*(…). The list-matching is also known as associative matching. Besides this, some other matching theories are supported in .

When taking this new possibility into account, the evaluation of a `%match` construct gets a little bit more complex:

-   given a whose patterns match the list of ground terms (or a whose is evaluated to true), the list of variables is instantiated and the associated semantic action is executed.
-   if the execution control is not transferred outside the `%match` instruction, in addition to the previous explanations, if the considered matching theory may return several matches, for each match, the free variables are instantiated and the associated semantic action is executed. This means that the same action may be executed several times, but in a different context: i.e. the variables have different instantiations.
-   when all matches have been computed (there is at most one match in the syntactic theory, i.e. when no special theory, as associativity for instance, is associated to the symbols used in the patterns), the execution control is transferred to the next whose patterns match the list of ground terms (respectively to the next whose evaluates to true).
-   as before, when there is no more whose patterns match the list of subject (or respectively no whose evaluates to true), the `%match` instruction is finished, and the execution control is transferred to the next instruction.

As mentioned in the BNF-syntax, a *label* may be attached to a pattern. In that case, in <font color="purple">C</font> and <font color="purple">Java</font>, it becomes possible to exit from the current (using a `goto` or a `break`), without exiting from the whole `%match` construct. The control is transferred to the next pattern which matches the subjects (respectively to the next constraint). This feature is useful to exit from a complex associative-matching for which, for any reason, we are no longer interested in getting other solutions.

When using the new syntax based on constraints, there are several restrictions that are imposed by the compiler (an error is generated if they are not respected):

-   no circular references among variables are allowed. For instance, something like `x << x` will generate an error. This verification works even when the cycles are less evident, like for instance for the following constraint: `f(g(x),a()) << y && f(b(),y) << z && g(f(z,c())) << x`.
-   when using disjunctions, all the variables that are used in the action have to be found in each member of the disjunction (for ensuring that no non-instantiated variable is used in the action). For instance, using the following generates an error: `x << s1 || y << s2 -> { /* code that uses y */ }`

### Tom pattern

As we can imagine, the behavior of a `%match` construct strongly depends on the patterns which are involved. The formalism which defines the syntax of a pattern is also an essential component of . But because there exist several ways to define patterns with equivalent behavior, its formal definition is not so simple. Although, the different shortcuts help the programmer to simplify the definitions of patterns.

Informally, a pattern is a term built with constructors and variables (please note that `x` denotes a variable, whereas `a()` is a constructor). A variable can also be anonymous, and it is denoted by `_`. Let’s look at some examples of patterns: `x`, `a()`, `f(a())`, `g(a(),x)`, or `h(a(),_,x)`. When a pattern matches a subject, it may be useful to keep a reference to a matched subterm. The annotation mechanism (`z@g(y)` for example) can be used for this purpose. Thus, considering the matching between the pattern `f(x,z@g(y))` and the subject `f(a(),g(h(b())))`, `y` is instantiated by `h(b())`, and `z` is instantiated by `g(h(b()))`. This can be useful in <font color="purple">C</font>, to free the memory for example.

When identical actions have to be performed for a set of patterns which share a common structure, the *disjunction of symbols* may be used: pattern `(f∣g)(a())` is equivalent to the set `{f(a()), g(a())}`. The disjunction of symbols may also be used in subterms, like in `h( (f∣g)(x) )`.

More formally, a pattern and a term has the following syntax:

```java
Term	::=	VariableName['*']
∣	Name '('[Term ( ',' Term )*]')'
Pattern	::=	[ AnnotedName '@' ] PlainPattern
PlainPattern	::=	['!']VariableName [ '*' ]
∣	['!'] HeadSymbolList (ExplicitTermList ∣ ImplicitPairList)
∣	ExplicitTermList
∣	'_'
∣	'_*'
∣	XMLTerm
HeadSymbolList	::=	HeadSymbol [ '?' ]
∣	'(' HeadSymbol ( '∣' HeadSymbol )+ ')'
ExplicitTermList	::=	'(' [ Pattern ( ',' Pattern )*] ')'
ImplicitPairList	::=	'[' [ PairPattern ( ',' PairPattern )*] ']'
PairPattern	::=	SlotName '=' Pattern
```

Concerning the syntax, both and host-language variables can be used and can be either a function symbol declared in or the name of a method from the host language.

A pattern is a term which could contain variables. When matching a pattern against a subject (a ground term), these variables are instantiated by the matching procedure (generated by ). In , the variables do not have to be declared: their type is inferred automatically, depending on the context in which they appear.

As described previously, offers several mechanisms to simplify the definition of a pattern:

-   standard notation: a pattern can be defined using a classical prefix term notation. To make a distinction between variables and constants, the latter have to be written with explicit parentheses, like `x()`. In this case, the corresponding operator (`%op x()`) should have been declared. When omitting parentheses, like `x`, this denotes a variable.
-   unnamed variables: the `_` notation denotes an anonymous variable. It can be used everywhere a variable name can be used. It is useful when the instance of the variable does not need to be used. Similarly, the `_*` notation can be used to denote an anonymous list-variable. This last notation can improve the efficiency of list-matching because the instances of anonymous list-variables do not need to be built.
-   annotated variable: the `@` operator allows to give a variable name to a subterm. In `f(x@g(_))` for example, `x` is a variable that will be instantiated by the instance of the subterm `g(_)`. The variable `x` can then be used as any other variable.
-   implicit notation: as explained below, the `%op` operator forces to give name to arguments. Assuming that the operator `f` has two arguments, named `arg1` and `arg2`, then we can write the pattern `f[arg1=a()]` which is equivalent to `f(a(),_)`. This notation is interesting mostly when using constructors with many subterms. Besides that, using this notation can avoid changing the patterns when the signature slightly changes (the order of the arguments, adding a new argument etc).
-   symbol disjunction notation: to factorize the definition of pattern which have common subterms, it is possible to describe a family of patterns using a disjunction of symbols. The pattern `(f∣g)(a(),b())` corresponds to the disjunction `f(a(),b())` or `g(a(),b())`. To be allowed in a disjunction (in standard notation), the constructors should have the same signature (arity, domain and codomain). Thus the disjunction of list operators is not allowed since these operators do not have fixed arities and can not be compared.

In practice, it is usually better to use the disjunction notation with the explicit notation offers: (`(f∣g)[arg1=a()]`). In that case, the signatures of symbols do not have to be identical: only involved slots have to be common (same names and types). Thus, the pattern `(f∣g)[arg1=a()]` is correct, even if `g` has more slots than `f`: it only has to have the slot `arg1`, with the same sort.

The use of after a list operator enables real associative with neutral elements (AU) matchings. By using `conc?` for example, we simply specify that the subject may not start with a `conc` symbol. For instance, the following code would produce the output `matched`, because `x` and respectively `y` can be instantiated with the neutral element of `conc` (please see the documentation on for further details about specifying symbols’ type and their neutral elements).

``` tom
L l = `a();
%match(l) {
  conc?(x*,y*) -> { System.out.println("matched"); }
}
```

Note that without the use of , the subject *must* start with a `conc` in order to have a match.

### Tom anti-pattern

The notion of anti-pattern offers more expressive power by allowing complement constructs: a pattern can describe what *should not* be in the matched subject.

The notion of complement is introduced by the symbol , as illustrated by the following grammar fragment:

```java
PlainPattern	::=	['!'] VariableName
∣	['!'] HeadSymbolList ( ExplicitTermList ∣ ImplicitPairList )
∣	...
```

The semantics of anti-patterns can be best understood when regarding them as complements. For example, a pattern like `car[color=blue()]` will match all the blue cars. If we add an symbol, the anti-pattern `!car[color=blue()]` will match everything that is not a blue car, i.e all objects that are not cars or the cars that have a color different from blue. The grammar allows also `car[color=!blue()]` - matches all cars that are not blue, or `!car[color=!blue()]` - either everything that is not a car, or a blue car.

Using the non-linearity combined with anti-patterns allows to express interesting searches also. For example, `car[interiorColor=x,exteriorColor=x]` will match the cars that have the same interior and exterior color. By using the anti-pattern `car[interiorColor=x,exteriorColor=!x]`, the result is as one would expect: it will match all the cars with different interior - exterior colors.

It is also possible to use the anti-patterns in list constructions. Please refer to the tutorial for more examples.

### Backquote construct

Backquote construct (`` ` ``) can be used to build an algebraic term or to retrieve the value of a variable (a variable instantiated by pattern-matching).

```java
BackQuoteTerm	::=	['`'] CompositeTerm
```

The syntax of is not fixed since it depends on the underlying language.

However, should be of the following form:

-   : to denote a {{tom}} variable

-   : to denote a {{tom}} list-variable

-   `(` ... `)`: to build a prefix term

-   `(` ... `)`: to build an expression
-   `(` ... `)`: to build an <span style="font-variant: small-caps">Xml</span> term

In general, it is sufficient to add a backquote before the term you want to build to have the desired behavior. The execution of `` `f(g(a())) `` will build the term *f*(*g*(*a*)), assuming that *f*, *g*, and *a* are operators. Suppose now that *g* is no longer a constructor but a function of the host-language. The construction `` `f(g(a())) `` is still valid, but the semantics is the following: the constant *a* is built, then the function *g* is called, and the returned result is put under the constructor *f*. Therefore, the result of *g* must be a correct term, which belongs to the right type (i.e. the domain of *f*).

To simplify the interaction with the host-language, it is also possible to use “unknown symbols” like `f(x.g())` or `f(1+x)`. The scope of the backquote construct is determined by the scope of the most external enclosing braces, except in two case: `` `x `` and `` `x* `` which allow you to use variables instantiated by the pattern part. In that case the scope is limited to the length of the variable name, eventually extended by the . Sometimes, when writing complex expression like `` if(`x==`y || `x==`z) ``, it can be useful to introduce extra parenthesis (`` if( `(x==y || x==z) ) ``) in order to extend the scope of the backquote.

Backquote construct can use default values if they have been defined in the mapping (`get_default` construct). Assuming that *f*, *a* and *b* are Tom operators of sort *T*, and *f* has two fields *x* and *y* of sort *T*, where a default value for *x* is set to *a*. Two notations are available to build a *f* by using default value: the explicit one (`` `f[y=b()] ``) and the implicit one (`` `f(_,b()) ``). Both notations will return the term `` `f(a(),b()) ``.

### *Meta-quote* construct

provides the construct … that allows to build formatted strings without the need to encode special characters such as tabulations and carriage returns as it is usually done in <font color="purple">Java</font>. For example, to build a string containing the `HelloWorld` program, one can simply write:

``` tom
String hello = %[
  public class Hello {
    public static void main(String[] args) {
      System.out.println("Hello\n\tWorld !");
    }
  }
]%
```

Additionally, it is possible to insert in this string the content of a `String` variable, or the result of a function call (if it is a `String`) using the as escape character: the code contained between in this construct will be evaluated and the result inserted in the surrounding formatted string. Then, to add to the `HelloWorld` example a version string, we can use:

``` tom
String version = "v12";
String hello2=%[
  public class Hello {
    public static void main(String[] args) {
      System.out.println("Hello\n\tWorld   @version@");
    }
  }
]%;
```

Even if the contents of the *meta-quote* construct is a formatted string, it is required that this string contains correctly balanced curly braces.

### Transformation construct

Transformation construct is a high level construct dedicated to EMF Ecore models transformations.

Here is the `%transformation` grammar:

<div class="center">
|     |     |            |
|:----|:---:|:-----------|
|     | ::= |  \[\] ()\* |
|     | ::= |            |
|     | ::= |            |
|     | ::= |  ( )\*     |
|     |  |  |  ( )\*     |
|     | ::= |  ()\*      |
|     | ::= |            |
|     | ::= |            |

</div>
<span style="color: red">Work in progress</span>

Here is the `%tracelink` grammar:

<div class="center">
|     |     |     |
|:----|:---:|:----|
|     | ::= |     |
|     | ::= |     |
|     | ::= |     |

</div>
<span style="color: red">Work in progress</span>

Here is the `%resolve` grammar:

<div class="center">
|     |     |     |
|:----|:---:|:----|
|     | ::= |     |

</div>
<span style="color: red">Work in progress</span>

## Tom signature constructs (**\***)

### Sort and subsort definition constructs

To define the mapping between the algebraic constructors and their concrete implementation, provides a signature-mapping mechanism composed of several constructs. In addition to predefined mapping for usual builtin sorts (`int`, `long`, `double`, `boolean`, `string`, and `char`), all other algebraic sorts have to be declared using the `%typeterm` construct. Their name have to be different from the predefined builtin sorts names.

To use predefined sorts (in a construct or in the definition of an algebraic operator), it is sufficient to use the construct (`%include { int.tom }` for example).

When defining a new type with the construct, some information has to be provided:

-   the construct describes how the new type is implemented. The host language part written between braces ( and ) is never parsed. It is used by the compiler to declare some functions and variables.
-   the construct specifies how to check the sort of an object of this type (in this is usually done with ). It is only required when using this type in a construct.
-   the construct corresponds to a predicate (parameterized by two term variables). This predicate should return `true` if the terms are “equal”. The `true` value should correspond to the builtin `true` value of the considered host language. This last optional predicate is used to compare builtin values and to compile non-linear left-hand sides.

Given a class `Person` we can define an algebraic mapping for this class:

``` tom
%typeterm TomPerson {
  implement { Person }
  is_sort(t) { t instanceof Person }
  equals(t1,t2) { t1.equals(t2) }
}
```

Here, we assume that the method `equals` implements a comparison function over instances of `Person`. Note that we used `TomPerson` to make a clear distinction between algebraic sorts (defined in ) and implementation sorts (defined in <font color="purple">Java</font>, via the use of classes). In practice, we usually use the same name to denote both the algebraic sort and the implementation sort.

In order to define algebraic subsorts representing subtype binary relations between the algebraic sorts we use the keyword . Given a class `Woman` we can define an algebraic mapping `TomWoman` as a subsort of `TomPerson`:

``` tom
%typeterm TomWoman extends TomPerson {
  implement { Woman }
  is_sort(t) { t instanceof Woman }
  equals(t1,t2) { t1.equals(t2) }
}
```

Note that multiple inheritance is not allowed in since a given algebraic sort can not be defined twice. The supertypes of a sort are obtained by reflexive and transitive closure over the direct supertype relation.

The grammar is the following:

```java
IncludeConstruct	::=	'%include' '{' FileName '}'
GoalLanguageBlock	::=	'{' BlockList '}'
TypeTerm	::=	'%typeterm' Type ['extends' Type] '{'
KeywordImplement [KeywordIsSort] [KeywordEquals]
'}'
KeywordImplement	::=	'implement' GoalLanguageBlock
KeywordIsSort	::=	'is_sort' GoalLanguageSortCheck
KeywordEquals	::=	'equals' '(' Name ',' Name ')' GoalLanguageBlock
OptionString	::=	'--fresh' | '--termgraph' | ... (see Gom tool for the list of available options)
```

### Constructor definition constructs

Once algebraic sorts are declared (using ), provides a mechanism to define signatures for constructors of these sorts using , or constructs. When defining a new symbol with the construct, the user should specify the name of the operator, its codomain, and its domain. The later one is defined by a list of pairs (slot-name, sort).

Let us consider again the class `Person`, and let us suppose that an instance of `Person` has two fields (`name` and `age`), we can define the following operator:

``` tom
%op TomPerson person(name:String, age:int)
```

In this example, the algebraic operator `person` has two slots (`name` and `age`) respectively of sorts `String` and `int`, where `String` and `int` are pre-defined sorts.

In addition to the signature of an operator, several auxiliary functions have to be defined:

-   The construct is used to check if a term is rooted by the considered symbol. The `true` value should correspond to the builtin `true` value of the considered host language (`true` in or , and something different from `0` in for example).
-   The construct is parameterized by several variables (i.e. that should correspond to the arity of the symbol). A call to this function should return a term rooted by the considered symbol, where each subterm correspond to the terms given in arguments to the function. When defining a constant (i.e. an operator without argument, `make` can be defined without braces: `make { ... }`).
-   The construct has to be defined for all slots of the signature. The implementation of these constructs should be such that the corresponding subterm is returned.
-   The construct is optional but may be defined to assign a default value for a given slot.

Coming back to our example, checking if an object is rooted by the symbol `person` can be done by checking that is an instance of the class `Person`. Building a `person` can be done via the function `new Person(...)`. Accessing to the slots `name` and `age` could be implemented by an access to the variables of the class `Person`. In practice, the following operator definition should work fine:

``` tom
%op TomPerson person(name:String, age:int) {
  is_fsym(t)  { $t instanceof Person }
  make(t1,t2) { new Person($t1,$t2) }
  get_slot(name,t) { $t.name } // assuming that 'name' is public
  get_slot(age,t)  { $t.age  } // assuming that 'age' is public
  get_default(name) { "Doe" }
  get_default(age) { 42 }
}
```

When defining a new symbol with the construct, the user has to specify how the symbol is implemented. In addition, the user has to specify how a list can be built and accessed:

-   the construct should return an empty list.
-   the construct corresponds to a function parameterized by a list variable and a term variable. This function should return a new list `l’` where the element `e` has been inserted at the head of the list `l` (i.e. and should be `true`).
-   the function is parameterized by a list variable and should return the first element of the considered list.
-   the function is parameterized by a list variable and should return the tail of the considered list.
-   the constructs corresponds to a predicate parameterized by a list variable. This predicate should return `true` if the considered list contains no element.

Similarly, when defining a new symbol with the construct, the user has to specify how the symbol is implemented, how an array can be built, and accessed:

-   the construct should return a list such that `n` successive can be made.
-   the construct corresponds to a function parameterized by a list variable and a term variable.

'''Warning: ''' This function should return a list `l’` such that the element `e` is at the end of `l`.

-   the construct is parameterized by a list variable and an integer. This should correspond to a function that return the `n-th` element of the considered list `l`.
-   the constructs corresponds to a function that returns the size of the considered list (i.e. the number of elements of the list). The size of an empty list is `0`.

The or is complex but not difficult to use. Let us consider the `ArrayList` class, and let us define a mapping over this data-structure. The first thing to do consists in defining the sort for the elements and the sort for the list-structure:

``` tom
%typeterm Object {
  implement     { Object          }
  equals(l1,l2) { $l1.equals($l2) }
}
%typeterm TomList {
  implement     { ArrayList       }
  equals(l1,l2) { $l1.equals($l2) }
}
```

Once defined the sorts, it becomes possible to define the list-operator `TomList conc( Object* )`. This operator has a variadic arity: it takes several `Object` and returns a `TomList`.

``` tom
%oparray TomList conc( Object* ) {
  is_fsym(t)       { $t instanceof ArrayList }
  make_empty(n)    { new ArrayList($n)       }
  make_append(e,l) { myAdd($e,(ArrayList)$l) }
  get_element(l,n) { (Object)$l.get($n)      }
  get_size(l)      { $l.size()               }
}
private static ArrayList myAdd(Object e,ArrayList l) {
  l.add(e);
  return l;
}
```

An auxiliary function `myAdd` is used since the `make_append` construct should return a new list. The `get_element` should return an element whose sort belongs to the domain (`Object`) in this example. Although not needed in this example, in general, a cast (`(Object)$l.get($n)`) is needed.

The grammar for the mapping constructs is the following:

```java
Operator	::=	'%op' Type Name'(' [ SlotName ':' Type ( ',' SlotName ':' Type )* ] ')'
'{' ( KeywordIsFsym | KeywordMake ∣ KeywordGetSlot ∣ KeywordGetDefault )* '}'
OperatorList	::=	'%oplist' Type Name '(' Type '*' ')'
'{' KeywordIsFsym ( KeywordMakeEmptyList
∣ KeywordMakeInsert ∣ KeywordGetHead
∣ KeywordGetTail ∣ KeywordIsEmpty )* '}'
OperatorArray	::=	'%oparray' Type Name '(' Type '*' ')'
'{' KeywordIsFsym ( KeywordMakeEmptyArray ∣
KeywordMakeAppend ∣ KeywordElement
∣ KeywordGetSize )* '}'
KeywordIsFsym	::=	'is_fsym' '(' Name ')' GoalLanguageBlock
KeywordGetSlot	::=	'get_slot' '(' Name ',' Name ')' GoalLanguageBlock
KeywordGetDefault	::=	'get_default' '(' Name ')' GoalLanguageBlock
KeywordMake	::=	'make' [ '(' Name ( ',' Name )* ')' ] GoalLanguageBlock
KeywordGetHead	::=	'get_head' '(' Name ')' GoalLanguageBlock
KeywordGetTail	::=	'get_tail' '(' Name ')' GoalLanguageBlock
KeywordIsEmpty	::=	'is_empty' '(' Name ')' GoalLanguageBlock
KeywordMakeEmptyList	::=	'make_empty' [ '(' ')' ] GoalLanguageBlock
KeywordMakeInsert	::=	'make_insert' '(' Name ',' Name ')' GoalLanguageBlock
KeywordGetElement	::=	'get_element' '(' Name ',' Name ')' GoalLanguageBlock
KeywordGetSize	::=	'get_size' '(' Name ')' GoalLanguageBlock
KeywordMakeEmptyArray	::=	'make_empty' '(' Name ')' GoalLanguageBlock
KeywordMakeAppend	::=	'make_append' '(' Name ',' Name ')' GoalLanguageBlock
```

### Predefined sorts and operators

See Section [Predefined mappings](/Documentation:Runtime_Library#Predefined_mappings "wikilink") in the Runtime Library Chapter.

### Gom construct

The grammar for the construct is as follows:

```java
GomConstruct	::=	'%gom' ['(' OptionString ')'] '{' GomGrammar '}'
```

It allows to define a signature (for more details about see Chapter [Gom](/Documentation:Gom "wikilink")). The compiler is called on the , and the construct is replaced by the produced mapping. The is composed by a list of command line options to pass to the underlying compiler, as described in Section [Using Gom](/Documentation:Using_Gom#Command_line_tool "wikilink").
