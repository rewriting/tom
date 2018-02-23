---
title: Documentation:Language Basics – Level 2
permalink: /Documentation:Language_Basics_–_Level_2/
---

The “Hello World” example
=========================

A very simple program is the following:

``` tom
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

The `%include { string.tom }` construct imports the predefined () mapping which defines the Tom `String` algebraic sort. Thus, the sort `String` can be used in the `%match` construct.

This examples shows that pattern matching can be performed against built-in data-types. In this example, the `%match` construct is equivalent to a `switch/case` construct: as expected the first print statement is executed, but the second one is also executed since no `break` statement has been executed.

To use a `break` statement in a `%match` construct, we recommend to use the notion of labeled block:

``` tom
public class HelloWorld {
  %include { string.tom }
    public final static void main(String[] args) {
    String who = "World";
    matchblock: {
      %match(who) {
        "World" -> { System.out.println("Hello " + who);
                     break matchblock;
                   }
        _       -> { System.out.println("Don't panic"); }
      }
    }
  }
}
```

“Hello World” revisited: introduction to list-matching
======================================================

One particularity of Tom is to simplify the manipulation of lists.

It is natural to consider a string as a list of characters. Characters can be concatenated to from a string, and string can also be concatenated. In Tom, we have to give a name to this concatenation operator. Let us call it `concString`.

Thus, the string `“Hello”` is equivalent to the list of characters `concString(’H’,’e’,’l’,’l’,’o’)`.

Given a string, to check if the string contains the character <font color="blue">‘</font><font color="blue">`e`</font><font color="blue">’</font>, we can use the following matching construct:

``` tom
public class HelloWorld {
  %include { string.tom }
  public final static void main(String[] args) {
    %match("Hello") {
      concString(_*,'e',_*) -> { System.out.println("we have found a 'e'"); }
    }
  }
}
```

In this example, <font color="blue">‘</font><font color="blue">`_*`</font><font color="blue">’</font> corresponds to an anonymous variable which can be instantiated by any list of characters (possibly reduced to the empty list).

In order to capture the context (i.e. what is before and after the <font color="blue">‘</font><font color="blue">`e`</font><font color="blue">’</font>), it is possible to use named variables:

``` tom
String t = "Hello";
%match(t) {
  concString(before*,'e',after*) -> {
    System.out.println("we have found a 'e'" +
                       " after " + `before* +
                       " but before " + `after*);
  }
}
```

In this example, we have introduced two new variables (`before*` and `after*`), called “variable-star”, which are instantiated by a list of characters, instead of a single character (if the <font color="blue">‘</font><font color="blue">`*`</font><font color="blue">’</font> was not there).

Suppose now that we look for a word whose last letter is a <font color="blue">‘</font><font color="blue">`o`</font><font color="blue">’</font>:

``` tom
String t = "Hello";
%match(t) {
  concString(before*,'o') -> { ... }
}
```

Using this mechanism, it also becomes possible to look for a word which contains an <font color="blue">‘</font><font color="blue">`e`</font><font color="blue">’</font> somewhere and an <font color="blue">‘</font><font color="blue">`o`</font><font color="blue">’</font> in last position:

``` tom
String t = "Hello";
%match(t) {
  concString(before*,'e',_*,'o') -> { ... }
}
```

Note that a single pattern could provide several outcomes:

``` tom
String t = "Hello";
%match(t) {
  concString(before*,'l',after*) -> {
    System.out.println("we have found " + `before* +
                       " before 'l' and " + `after* + " after");
  }
}
```

When applied to `“Hello”`, there are two possible solutions for `before*` and `after*`:

`we have found he before 'l' and lo after`
`we have found hel before 'l' and o after`

Let us suppose that we look for two consecutive <font color="blue">‘</font><font color="blue">`l`</font><font color="blue">’</font> anywhere in the matched expression. This could be expressed by:

``` tom
String t = "Hello";
%match(t) {
  concString(_*,'l','l',_*) -> { ... }
}
```

Since this syntax is error prone when looking for long/complex substrings, Tom provides an abbreviated notation: <font color="blue">‘</font><font color="blue">`ll`</font><font color="blue">’</font>:

``` tom
String t = "Hello";
%match(t) {
  concString(_*,'ll',_*) -> { ... }
}
```

This notation is fully equivalent to the previous one.

String matching – continued
===========================

In the following, we consider the program:

``` tom
public class StringMatching {
  %include { string.tom }
    public final static void main(String[] args) {
    String s = "abcabc";
    %match(s) {
      concString(_*,x,_*,x,_*)    -> { System.out.println("x = " + `x); }
      concString('a',_*,y,_*,'c') -> { System.out.println("y = " + `y); }
      concString(C1*,'abc',C2*)   -> { System.out.println("C1 = " + `C1*); }
      concString(_*,z@'bc',_*)    -> { System.out.println("z = " + `z); }
      concString(_*,L*,_*,L*,_*)  -> { if(`L*.length() > 0) {
                                        System.out.println("L = " + `L*); } }
    }
  }
}
```

As illustrated previously, we can use variables to capture contexts. In fact, this mechanism is more general and we can use a variable anywhere to match something which is not statically known. The following pattern looks for two characters which are identical:

``` tom
concString(_*,x,_*,x,_*) -> { System.out.println("x = " + `x); }
```

Since list matching is not unitary, there may be several results. In this case, we obtain:

``` tom
x = a
x = b
x = c
```

The second pattern looks for a character in a string which should begin with a <font color="blue">‘</font><font color="blue">`a`</font><font color="blue">’</font> and end with a <font color="blue">‘</font><font color="blue">`c`</font><font color="blue">’</font>:

``` tom
concString('a',_*,y,_*,'c') -> { System.out.println("y = " + `y); }
```

The results are:

``` tom
y = b
y = c
y = a
y = b
```

The third pattern look for the substring <font color="blue">‘</font><font color="blue">`abc`</font><font color="blue">’</font> anywhere in the string:

``` tom
concString(C1*,'abc',C2*) -> { System.out.println("C1 = " + `C1*); }
```

When the substring is found, the prefix `C1*` is:

``` tom
C1 =
C1 = abc
```

The last pattern illustrates the search of two identical substrings:

``` tom
concString(_*,L*,_*,L*,_*) -> { if(`L*.length() > 0) {
                                System.out.println("L = " + `L*); } }
```

The results are:

``` tom
L = a
L = ab
L = abc
L = b
L = bc
L = c
```

To look for a palindrome, you can use the following method:

``` tom
public static boolean pal(String s){
        %match(s){
            concString() -> {return true;}
            concString(x) -> {return true;}
            concString(x,y*,x) -> {return pal(`y);}
        }
        return false;
}
```

List matching – Associative matching
====================================

As illustrated previously, supports a generalized form of string matching, called *list matching*, also known as *associative matching with neutral element*. In , some operators are special and do not have a fixed number of arguments. This intuitively corresponds to the idea of list where elements are stored in a given order. A list of elements may be written `(a,b,c)` or `[a,b,c]`. In Tom, it is written `f(a(),b(),c())`, where <font color="blue">‘</font><font color="blue">`f`</font><font color="blue">’</font> is this special operator. In some sense, <font color="blue">‘</font><font color="blue">`f`</font><font color="blue">’</font> is the name of the list. This allows to distinguish list of apples, from list of oranges for examples.

The definition of such operators can be done using :

``` tom
import list1.list.types.*;
public class List1 {
  %gom {
    module List
    abstract syntax
    E = a()
      | b()
      | c()
    L = f( E* )
   }
   ...
}
```

Once this signature defined, it becomes possible to define patterns and `‘` expressions. For example, the function that removes identical consecutive elements can be expressed as follows:

``` tom
public static L removeDouble(L l) {
  %match(l) {
    f(X1*,x,x,X2*) -> {
      return removeDouble(`f(X1*,x,X2*));
    }
  }
  return l;
}
```

This example is interesting since it expresses a complex operation in a concise way: given a list `l`, the `%match` construct looks for two identical consecutive elements (`f(X1*,x,x,X2*)` is called a non-linear pattern since `x` appears twice). If there exists such two elements, the term `f(X1*,x,X2*)` is built (an `x` has been removed), and the `removeDouble` function is called recursively. When there is no such two elements, the pattern does not match, this means that the list does not contain any consecutive identical elements. Therefore, the instruction `return l` is executed and the function returns.

Similarly, a sorting algorithm can be implemented: if a list contains two elements in the wrong order, just swap them. This can be expressed as follows:

``` tom
public static L swapSort(L l) {
  %match(l) {
    f(X*,e1,Z*,e2,Y*) -> {
      if(`gt(e1,e2)) {
        return `swapSort(f(X*,e2,Z*,e1,Y*));
      }
    }
  }
  return l;
}
private static boolean gt(E e1, E e2) {
  return e1.toString().compareTo(e2.toString()) > 0;
}
```

In this example, the order is implemented by the `gt` function, using the lexicographical ordering provided by <font color="purple">Java</font>.

The following code shows how to build a list and call the function defined previously. This results in sorted lists where elements only occur once.

``` tom
public final static void main(String[] args) {
  L l = `f(a(),b(),c(),a(),b(),c(),a());
  L res1 = swapSort(l);
  L res2 = removeDouble(res1);
  System.out.println(" l       = " + l);
  System.out.println("sorted l = " + res1);
  System.out.println("single l = " + res2);
}
```

[Category:Documentation](/Category:Documentation "wikilink")