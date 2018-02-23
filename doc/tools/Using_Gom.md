---
title: Documentation:Using Gom
permalink: /Documentation:Using_Gom/
---

Command line tool
=================

People interested in using as a standalone tool can use the `gom` command line interface.

**gom \[options\] filename \[... filename\]**

**Options:**

|                                                    |
|----------------------------------------------------|
|                                                    |
| `--config `<file>` | -X`                           |
|                                                    |
| `--debug | -vv`                                    |
|                                                    |
| <code>--destdir

 <dir>
 | -d</code>                                         |
|                                                    |
| <code>--fresh

 <dir>
 | -f</code>                                         |
|                                                    |
| `--generator `<type>` | -g`                        |
|                                                    |
| `--help | -h`                                      |
|                                                    |
| `--import `<path>` | -I`                           |
|                                                    |
| `--inlineplus`                                     |
|                                                    |
| `--intermediate | -i`                              |
|                                                    |
| `--intermediateName `<intermediateName>` | -iname` |
|                                                    |
| `--multithread | -mt`                              |
|                                                    |
| `--nosharing | -ns`                                |
|                                                    |
| `--optimize | -O`                                  |
|                                                    |
| `--optimize2 | -O2`                                |
|                                                    |
| `--package `<packageName>` | -p`                   |
|                                                    |
| `--withCongruenceStrategies | -wcs`                |
|                                                    |
| `--termgraph | -tg`                                |
|                                                    |
| `--termpointer | -tp`                              |
|                                                    |
| `--verbose | -v`                                   |
|                                                    |
| `--verbosedebug | -vvv`                            |
|                                                    |
| `--version | -V`                                   |
|                                                    |
| `--wall | -W`                                      |
|                                                    |

Ant task
========

Gom can also be used within ant, and has its own ant tasks.

To use the Gom ant task, you have to first declare it the same way as the Tom ant task.

``` xml
<taskdef name="gom"
         classname="tom.gom.tools.ant.GomTask"
         classpathref="tom.classpath"/>
```

Once the task is defined, you can use them to compile a Gom file.

The Gom ant task has to be able to find a configuration file for Gom. It can either use the `tom.home` property to find its configuration file, or use the value of the `config` attribute. Since the `tom.home` property is also used when Gom has to process hooks, it is a good practice to always define the `tom.home` property to the installation path of the Tom system.

The default configuration file for Gom is included in the Tom distribution as `Gom.xml`.

``` xml
<gom config="${gom.configfile}"
     srcdir="${src.dir}"
     package="example"
     destdir="${gen.dir}">
  <include name="**/*.gom"/>
</gom>
```

Like in Tom example, in this Gom ant task we want to compile all Gom source files in `{src.dir}` and to configure the compiler to create a package called `example` in `{gen.dir}` for the generated code, just as we do for Gom in command line.

The Gom task takes the following arguments:

| Attribute | Description                                 | Required                                      |
|-----------|---------------------------------------------|-----------------------------------------------|
| config    | Location of the Gom configuration file.     | No                                            |
| destdir   | Location of the generated files.            | No                                            |
| options   | Custom options to pass to the Gom compiler. | No                                            |
| package   | Package for the generated files.            | No                                            |
| srcdir    | Location of the Gom files                   | Yes, unless nested <src> elements are present |

The set of `.gom` files to compile is specified with a nested `include` element.

Gom Antlr adaptor
=================

GomAntlrAdaptor is a tool that takes a Gom signature and generates an adaptor for a given Antlr grammar file. This way, Gom constructors can be directly used in the rewrite rule mechanism offered by Antlr (version 3). The example `examples/parser` shows how to combine Tom, Gom and Antlr.

Command line tool
-----------------

**gomantlradaptor \[options\] filename \[... filename\]**

**Options:**

|                                                    |
|----------------------------------------------------|
|                                                    |
| `--config `<file>` | -X`                           |
|                                                    |
| `--debug | -vv`                                    |
|                                                    |
| <code>--destdir

 <dir>
 | -d</code>                                         |
|                                                    |
| `--fresh | -f`                                     |
|                                                    |
| `--help | -h`                                      |
|                                                    |
| `--import `<path>` | -I`                           |
|                                                    |
| `--intermediate | -i`                              |
|                                                    |
| `--intermediateName `<intermediateName>` | -iname` |
|                                                    |
| `--package `<packageName>` | -p`                   |
|                                                    |
| `--termgraph | -tg`                                |
|                                                    |
| `--termpointer | -tp`                              |
|                                                    |
| `--verbose | -v`                                   |
|                                                    |
| `--verbosedebug | -vvv`                            |
|                                                    |
| `--version | -V`                                   |
|                                                    |
| `--wall | -W`                                      |
|                                                    |

Ant task
--------

The GomAntlrAdaptor task takes the following arguments:

| Attribute | Description                                         | Required                                      |
|-----------|-----------------------------------------------------|-----------------------------------------------|
| config    | Location of the GomAntlrAdaptor configuration file. | No                                            |
| destdir   | Location of the generated files.                    | No                                            |
| options   | Custom options to pass to the compiler.             | No                                            |
| package   | Package for the generated files.                    | No                                            |
| srcdir    | Location of the GomAntlrAdaptor files               | Yes, unless nested <src> elements are present |

[Category:Documentation](/Category:Documentation "wikilink")