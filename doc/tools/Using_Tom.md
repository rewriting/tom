---
title: Documentation:Using Tom
permalink: /Documentation:Using_Tom/
---

The Tom compiler
================

The Tom compiler is a non-intrusive pattern matching compiler. It takes a Tom program, combination of a host programming language (<font color="purple">Java</font>, <font color="purple">C</font>, <font color="purple">Caml</font> or <font color="purple">Ada</font>) extended by Tom constructs. Each Tom program can define its own data structures and manipulate them in order to write pattern-matching based algorithms.

Development process
===================

As mentioned earlier, generates host language code. The command `tom` invokes the compiler on a source file and generates a new source file (with `.java`, `.tom.c`, `.tom.ml`, or `.adb` extension, depending on the compilation command-line). The generated code has to be compiled/interpreted by a tool which can handle the corresponding host language: `javac` for , `cc` for , `ocamlc` for and `gnatmake` for .

A typical development cycle is the following:

-   edit a program (by convention, a source code has the `.t` extension)
-   run the compiler on this file
-   compile the generated file (`javac file.java`, or `cc file.tom.c`,…)
-   execute the program (`java file`, or `a.out`,…)

As `cc` and `javac`, `tom` can compile several input files. By default, the generated files are saved in the same directory as the corresponding input files. When compiling a file (i.e. a Tom file where is the host language), is smart enough to parse the package definition and generate the pure- file in the same package architecture. Similarly to `javac`, `tom` supports a `-d` option which allows the user to indicate where the files have to be generated. To be compatible with `cc` and classical `Makefile`, Tom also supports a `-o` option which can be used to specify the name of the generated file.

Command line tool
=================

**tom \[options\] filename\[.t\]**

The command takes only one file argument, the name of the input file to be compiled. By convention the input file name is expected to be of the form *filename.t*, whatever the used host language is.

**Options:**

> <div class="center">
>
> ------------------------------------------------------------------------
>
> </div>
> |                               |
> |-------------------------------|
> |                               |
> | `--aCode | -a`                |
> |                               |
> | `--cCode | -c`                |
> |                               |
> |                               |
> | `--camlCode`                  |
> |                               |
> | `--camlSemantics`             |
> |                               |
> | `--compile`                   |
> |                               |
> | `--config | -X `<file>        |
> |                               |
> |                               |
> | `--csCode`                    |
> |                               |
> | `--destdir | -d`              |
> |                               |
> |                               |
> | `--eclipse`                   |
> |                               |
> | `--encoding `<charset>` | -e` |
> |                               |
> | `--expand`                    |
> |                               |
> | `--genIntrospector | -gi`     |
> |                               |
> | `--help | -h`                 |
> |                               |
> |                               |
> | `--import `<path>` | -I`      |
> |                               |
> |                               |
> | `--inline`                    |
> |                               |
> | `--inlineplus`                |
> |                               |
> | `--intermediate | -i`         |
> |                               |
> |                               |
> | `--jCode | -j`                |
> |                               |
> | `--lazyType | -l`             |
> |                               |
> |                               |
> | `--multithread | -mt`         |
> |                               |
> | `--newTyper | -nt`            |
> |                               |
> |                               |
> | `--noDeclaration | -D`        |
> |                               |
> |                               |
> | `--noOutput`                  |
> |                               |
> |                               |
> | `--noStatic`                  |
> |                               |
> | `--noSyntaxCheck`             |
> |                               |
> | `--noTypeCheck`               |
> |                               |
> | `--optimize | -O`             |
> |                               |
> |                               |
> | `--optimize2 | -O2`           |
> |                               |
> | `--output `<file>`| -o`       |
> |                               |
> |                               |
> | `--pCode`                     |
> |                               |
> | `--parse`                     |
> |                               |
> | `--pretty | -p`               |
> |                               |
> |                               |
> | `--prettyPIL | -pil`          |
> |                               |
> | `--protected`                 |
> |                               |
> |                               |
> | `--type`                      |
> |                               |
> | `--verbose | -v`              |
> |                               |
> |                               |
> | `--version | -V`              |
> |                               |
> | `--wall | -W`                 |
> |                               |
> |                               |
>
> <div class="center">
>
> ------------------------------------------------------------------------
>
> </div>

Ant task
========

provides an ant task for running the compiler within the [apache ant](http://ant.apache.org) build system.

The ant task is very close in use to the `javac` ant task. Since this task is not part of the official ant tasks, you have first to declare this task in your buildfile, in order to use it.

This is done by the following code:

``` xml
<taskdef name="tom" classname="tom.engine.tools.ant.TomTask">
  <classpath refid="tom.classpath"/>
</taskdef>
```

where `tom.classpath` is the path reference containing all the jar’s in ’s distribution predefined in the file `${TOM_HOME}/lib/tom-common.xml`.

This task is used to produce code from programs. A typical use of the ant task is:

``` xml
<tom config="${tom.configfile}"
     srcdir="${src.dir}"
     destdir="${gen.dir}"
     options="-I ${mapping.dir}">
  <include name="**/*.t"/>
</tom>
```

Here, we want to compile all source files in `{src.dir}`, having the generated code in `{gen.dir}`, and we configure the compiler to use the `{tom.configfile}` config file, and pass the options we want like, for example `{-I}` to indicate the mapping needed for compilation, just as we do for in command line.

Another use of this task is:

``` xml
<tom config="${tom.configfile}"
     srcdir="${src.dir}"
     outputfile="${gen.dir}/Example.java"
     options="-I ${mapping.dir}">
  <include name="**/Example.t"/>
</tom>
```

Here we compile only one file, and specify directly the output file name we want to use.

The main usecases for the ant task can be found in ’s own buildfile and the distributed examples.

The ant task takes the following arguments:

<div class="center">
| Attribute   | Description                                                                                                      | Required                                      |
|-------------|------------------------------------------------------------------------------------------------------------------|-----------------------------------------------|
| classpath   | The classpath to use, given as a reference to a path defined elsewhere. This variable is **not** used currently. | No                                            |
| config      | Location of the Tom configuration file.                                                                          | No                                            |
| destdir     | Location of the <font color="purple">Java</font> files.                                                          | No                                            |
| failonerror | Indicates whether the build will continue even if there are compilation errors; defaults to `true`               | No                                            |
| logfile     | Which log file to use.                                                                                           | No                                            |
| nowarn      | Asks the compiler not to report all warnings; defaults to `no`                                                   | No                                            |
| optimize    | Indicates whether source should be compiled with optimization; defaults to `off`                                 | No                                            |
| options     | Custom options to pass to the Tom compiler                                                                       | No                                            |
| outputfile  | Destination file to compile the source. This require to have only one source file.                               | No                                            |
| pretty      | Asks the compiler to generate more human readable code; defaults to `no`                                         | No                                            |
| srcdir      | Location of the Tom files.                                                                                       | Yes, unless nested <src> elements are present |
| verbose     | Asks the compiler for verbose output; defaults to `no`                                                           | No                                            |

</div>
[Category:Documentation](/Category:Documentation "wikilink")