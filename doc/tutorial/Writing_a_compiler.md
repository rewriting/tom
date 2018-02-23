---
title: Documentation:Writing a compiler
permalink: /Documentation:Writing_a_compiler/
---

In this tutorial we will show how , Gom, and GomAntlrAdapter can be used together to easily write a Compiler, a Parser or an Interpreter.

Defining a data-structure: the Abstract Syntax Tree
===================================================

The first thing to do consists in defining the Abstract Syntax Tree that will be used to represent a program.

In the following we consider a tiny imperative programming language whose grammar is defined by:

|         |     |                       |             |
|---------|-----|-----------------------|-------------|
| Stm     | →   | Stm ; Stm             | (SeqStm)    |
| Stm     | →   | id := Exp             | (AssignStm) |
| Stm     | →   | print ( ExpList )     | (PrintStm)  |
| Exp     | →   | id                    | (IdExp)     |
| Exp     | →   | num                   | (NumExp)    |
| Exp     | →   | Exp Binop Exp         | (OpExp)     |
| Exp     | →   | ( Stm , Exp )         | (SeqExp)    |
| ExpList | →   | Exp , ExpList | Exp\* | (Cons, Nil) |
| Binop   | →   | +                     | (Plus)      |
| Binop   | →   | x                     | (Times)     |
| Binop   | →   | -                     | (Minus)     |
| Binop   | →   | /                     | (Div)       |

The AST can be implemented as follows in a `parser/Rec.gom` file:

``` tom
module parser.Rec
imports int String
abstract syntax
  Stm = SeqStm(Stm*)
      | AssignStm(Name:String, Exp:Exp)
      | PrintStm(List:ExpList)

  Exp = IdExp(Name:String)
      | NumExp(Value:int)
      | OpExp(Exp1:Exp,Op:Op,Exp2:Exp)
      | SeqExp(Stm:Stm,Exp:Exp)

  ExpList = ExpList(Exp*)

  Op = Plus() | Minus() | Times() | Div()

  Table = Table(Name:String,Value:int,Tail:Table)
        | EmptyTable()

  Pair = Pair(Value:int,Table:Table)
```

Parsing
=======

Using ANTLR, the concrete syntax can be easily defined in `parser/Rec.g`:

``` java
grammar Rec;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=RecTokens;
}

@header {
  package parser;
}
@lexer::header {
  package parser;
}

program:
  stm (';' stm)* -> ^(SeqStm stm*)
;

stm:
  ( ID ':=' exp -> ^(AssignStm ID exp)
  | 'print' '(' explist ')' -> ^(PrintStm explist)
  )
;

exp:
    e1=multexp ('+' e2=multexp -> ^(OpExp $e1 ^(Plus) $e2)
               |'-' e2=multexp -> ^(OpExp $e1 ^(Minus) $e2)
               |               -> $e1)
;

multexp:
    e1=atom ('*' e2=atom -> ^(OpExp $e1 ^(Times) $e2)
            |'/' e2=atom -> ^(OpExp $e1 ^(Div) $e2)
            |            -> $e1)
;

atom:
  ( INT -> ^(NumExp INT)
  | ID -> ^(IdExp ID)
  | '(' stm ',' exp ')' -> ^(SeqExp stm exp)
  )
;

explist:
  exp (',' exp)* -> ^(ExpList exp*)
;

INT : ('0'..'9')('0'..'9')*;
ID : ('a'..'z'|'A'..'Z')+;
WS : (' ' | '\t' | ( '\r\n' | '\n' | '\r')) {$channel=HIDDEN;} ;
SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?  {$channel=HIDDEN;} ;
MLCOMMENT : '/*' .* '*/' {$channel=HIDDEN;} ;
```

Interpreting
============

Using a functional programming style and an environment to store values, an interpreter can be defined:

``` tom
package parser;

import parser.rec.types.*;
import parser.rec.RecAdaptor;
import tom.library.sl.*;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import java.io.*;
import java.util.*;

public class Main {

  %include { rec/Rec.tom }
  %include { sl.tom }

  public static void main(String[] args) throws VisitFailure {
    try {
      if(args.length<=0) {
        System.out.println("usage: java Main <filename>");
      } else {
        // Initialize parser
        RecLexer lexer = new RecLexer(new ANTLRInputStream(new FileInputStream(args[0])));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RecParser parser = new RecParser(tokens);
        // Parse the input expression
        Tree b = (Tree) parser.program().getTree();
        Stm p = (Stm) RecAdaptor.getTerm(b);
        System.out.println(p);
        Main main = new Main();
        main.interp(p);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private int lookup(Table table, String key) {
    %match(table) {
      Table(name,value,tail) -> {
        if(key==`name) {
          return `value;
        } else {
          return lookup(`tail,key);
        }
      }
    }
    return 0;
  }

  private void interp(Stm s) {
    Table table = `EmptyTable();
    interpStm(s,table);
  }

  private Table interpStm(Stm s, Table table) {
    %match(s) {
      AssignStm(name,e1) -> {
        Pair p = interpExp(`e1,table);
        Table newTable = `Table(name,p.getValue(),p.getTable());
        return newTable;
      }

      SeqStm(s1,s2*) -> {
        Table newTable = `interpStm(s2,interpStm(s1,table));
        return newTable;
      }

      PrintStm(l) -> {
        Table t = interpPrint(`l,table);
        System.out.println();
        return t;
      }
    }
    return `EmptyTable();
  }

  private Table interpPrint(ExpList list, Table table) {
    %match(list) {
      ExpList(e1,tail*) -> {
        Pair p = `interpExp(e1,table);
        System.out.print(p.getValue());
        System.out.print(" ");
        return interpPrint(`tail*,p.getTable());
      }
    }
    return table;
  }

  private Pair interpExp(Exp e, Table table) {
    %match(e) {
      IdExp(n) -> { return `Pair(lookup(table,n),table); }
      NumExp(v) -> { return `Pair(v,table); }
      OpExp(e1,op,e2) -> {
        Pair p1 = `interpExp(e1,table);
        Pair p2 = `interpExp(e2,p1.getTable());
        %match(p1,op,p2) {
          Pair(i1,t1), Plus(), Pair(i2,t2) -> { return `Pair(i1 + i2,t2); }
          Pair(i1,t1), Minus(), Pair(i2,t2) -> { return `Pair(i1 - i2,t2); }
          Pair(i1,t1), Times(), Pair(i2,t2) -> { return `Pair(i1 * i2,t2); }
          Pair(i1,t1), Div(), Pair(i2,t2) -> { return `Pair(i1 / i2,t2); }
        }
      }

      SeqExp(s1,e1) -> {
        Table t = `interpStm(s1,table);
        Pair p = `interpExp(e1,t);
        return p;
      }

    }
    System.out.println("should not be there: " + e);
    return null;
  }

}
```

Building everything
===================

1.  generation of Java data structure to implement a Gom AST
2.  generation of a converter from ANTLR to Gom
3.  generation of the parser using ANTLR
4.  compilation of the program
5.  compilation of the Java generated code
6.  run the example

``` bash
gom -d gen parser/Rec.gom
gomantlradaptor -d gen -p parser parser/Rec.gom
java org.antlr.Tool -o gen -lib gen/parser/rec parser/Rec.g
tom -d gen parser/Main.t
cd gen
javac parser/Main.java
java parser.Main input.txt
```

Examples of inputs:

-   a := 5 + 3 ; b := 4
-   print ( 2,3,4 )
-   a := 5+3 ; b:= (print(a,a-1), 10\*a) ; print(b)

Using Ant
=========

Assuming that the sources are in a directory `src/parser`, you can use the following `ant` script (`build.xml`):

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="Build the parser" default="build" basedir=".">

  <property environment="env"/>
  <property name="tom.home"        value="${env.TOM_HOME}"/>
  <import file="${tom.home}/lib/tom-common.xml"/>

  <property name="src" location="src"/>
  <property name="gen" location="gen"/>
  <property name="build" location="build"/>
  <property name="parser" value="parser"/>

  <target name="clean" description="Cleans gen and build directory">
    <delete dir="${gen}"/>
    <delete dir="${build}"/>
  </target>

  <target name="build" depends="tom.init">
    <mkdir dir="${gen}"/>

    <gom.preset srcdir="${src}"
      destdir="${gen}">
      <include name="${parser}/Rec.gom"/>
    </gom.preset>

    <gomantlradapter.preset
      srcdir="${src}"
      destdir="${gen}"
      package="${parser}">
      <include name="${parser}/**/Rec.gom"/>
    </gomantlradapter.preset>

    <taskdef name="antlr3"
      classname="org.apache.tools.ant.antlr.ANTLR3">
      <classpath refid="tom.classpath"/>
    </taskdef>
    <antlr3 target="${src}/${parser}/Rec.g"
      outputdirectory="${gen}/${parser}"
      libdirectory="${gen}/${parser}/rec"
      multithreaded="true">
      <classpath refid="tom.classpath"/>
    </antlr3>

    <tom.preset srcdir="${src}"
      destdir="${gen}">
    </tom.preset>

    <mkdir dir="${build}"/>
    <javac.preset
      includeantruntime="false"
      destdir="${build}">
      <src path="${src}"/>
      <src path="${gen}"/>
      <classpath refid="tom.classpath"/>
    </javac.preset>
  </target>

</project>
```