---
title: Documentation:Installation
permalink: /Documentation:Installation/
---

Installing Tom
==============

Requirements
------------

is a application written with JDK 1.5. is platform independent and runs on various systems. It has been used successfully on many platforms, including GNU/Linux distributions (Debian, Gentoo, Mandrake, Ubuntu), MacOS X, FreeBSD, NetBSD, Windows XP, Windows Vista and Windows 7.

The only requirement to run and compile programs is to have a recent Development Kit installed (version 1.5 or newer):

-   is available for download at [java.sun.com](http://java.sun.com/javase/downloads/index.jsp). If you do not know which package to choose, select [Java SE Development Kit (JDK)](https://cds.sun.com/is-bin/INTERSHOP.enfinity/WFS/CDS-CDS_Developer-Site/en_US/-/USD/ViewProductDetail-Start?ProductRef=jdk-6u13-oth-JPR@CDS-CDS_Developer)

-   can also be successfully used with the [OpenJDK](http://openjdk.java.net/) instead of the Sun-Oracle JDK

Windows (simple install)
------------------------

For a painless process, run the [`tom-2.9_setup.exe`](https://gforge.inria.fr/frs/download.php/29258/tom-2.9_setup.exe) kit provided ([downloads](http://gforge.inria.fr/frs/?group_id=78&release_id=6495)). This will automatically update the environment as needed.

Windows (manual install)
------------------------

If for any reason you prefer to install manually, here is the procedure to follow:

We assume that Tom is installed in `C:\tom` (any other directory may be chosen). The following steps are needed for setting up the user variables of your environment:

-   set`JAVA_HOME` to where Java JDK is installed (for instance: `C:\Program Files\Java\jdk1.6.0_13`)
-   set `TOM_HOME` to `C:\tom`
-   set `CLASSPATH` to `.;%TOM_HOME%\lib\tom-runtime-full.jar`
-   set `PATH` to `%Path%;%TOM_HOME%\bin;%JAVA_HOME%\bin`

For a detailed description of setting environment variables in Windows, please refer to Windows documentation. For Windows XP, some information can be found [here](http://support.microsoft.com/default.aspx?scid=kb;en-us;310519&sd=tech)

Windows (with Cygwin)
---------------------

We assume that Tom is installed in `c:\tom`. Please issue the following for setting up Tom:

`  export TOM_HOME=/cygdrive/c/tom`
`  export PATH=${PATH}:${TOM_HOME}/bin`
`  export CLASSPATH=${TOM_HOME}/lib/tom-runtime-full.jar:${CLASSPATH}`

Unix
----

To install Tom, choose a directory (`${HOME}/tom` for instance) and copy the distribution files there. This directory will be called `TOM_HOME` in the following.

The following sets up the environment, for Bourne shell (sh) and C shell (csh) families:

`(sh)`
`  export TOM_HOME=${HOME}/tom`
`  export PATH=${TOM_HOME}/bin:${PATH}`
`  export CLASSPATH=${TOM_HOME}/lib/tom-runtime-full.jar:${CLASSPATH}`

`(csh)`
`  setenv TOM_HOME ${HOME}/tom`
`  set path=( ${TOM_HOME}/bin $path )`
`  setenv CLASSPATH ${TOM_HOME}/lib/tom-runtime-full.jar:${CLASSPATH}`

Getting some examples
=====================

Many examples have been written to test or illustrate some particular behaviors. This collection of examples is available: [tom-examples-2.9.zip](https://gforge.inria.fr/frs/download.php/29257/tom-examples-2.9.zip) (see also [downloads](http://gforge.inria.fr/frs/?group_id=78&release_id=6495)).

Unzip the archive and use the tool Ant to compile the examples:

-   Ant is available for download at [ant.apache.org](http://ant.apache.org).

Using Eclipse IDE
=================

The Eclipse platform (tested with versions 3.4 and 3.5) can be used to edit, compile, and run with program. Eclipse is available for download at [www.eclipse.org](http://www.eclipse.org).

Installation
------------

has to be installed using one of the previously described methods:

-   simple Windows install: run `tom-2.9_setup.exe`
-   manual install (Windows or Unix)

Use Ant to compile an example
-----------------------------

-   import the examples as a project (create a Java projet and use the `File->Import->General->Archive File`)
-   `Window->Open Perspective->Other->Java (default)`
-   expand `examples/addressbook`
-   `Window->Show View->Other->Ant`
-   move the `build.xml` file to the Ant view
-   click on the `build` target

To run an example
-----------------

-   in the package explorer: `right-click->refresh` (or press `F5`)
-   open the “Run Dialog” box (`Run->Run Configurations`)
-   select `Java Application`
-   enter `addressbook.AddressBookGom` as `Main class`
-   in the “Classpath” tab, select “User Entries”
    -   click `Add External JARs`, and select the JAR <TOM_HOME>`/lib/tom-runtime-full`
    -   click on the `Advanced` button, add `examples/build` using `Add Folder`

Configure the editor
--------------------

-   use `Windows->Preferences->General->Editors->File Associations` to associate a `Java Editor` to `*.t` files

Eclipse Plugin
==============

In parallel to , we also develop an Eclipse Plugin (for Eclipse 3.4 only) that integrates an editor and a compiler for .

The plugin is available directly from the Eclipse platform via the update link:

-   `Help->Software Updates` or `Help->Install New Software`
-   select `Available Software`, `Add Site`
-   enter:
        http://tom.loria.fr/eclipse-update/stable

-   select `Tom plugin` and click `Install`

To use the plugin:

-   create a Tom projet (`New->Project->Tom->Tom Project`)
-   in `src`, create a Tom file (`New->Other->Tom->Tom File`)

For more information, we invite the reader to use the integrated help (`Help->Help Contents->Tom Online Help`)

Contents
========

The binary distribution of Tom consists of one of the following directory layout:

` tom`
`  +--- bin (BSD Licence)`
`  |`
`  +--- lib `
`  |     +--- tom-runtime-full (BSD Licence)`
`  |     |`
`  |     +--- tom-compiler-full (GPL Licence)                                                                                  `
`  |     `
`  +--- share (BSD Licence)`
`        +--- contrib`
`        |`
`        +--- man`
`        |`
`        +--- tom `
`              +--- adt`
`              +--- c`
`              +--- caml`
`              +--- java`

or (version with separate jars):

`  tom`
`  +--- bin (BSD Licence)`
`  |`
`  +--- lib `
`  |     +--- compiletime (not developed by the Tom project)`
`  |     |     +--- ant-antlr          // ant task for antlr`
`  |     |     +--- ant-contrib        // ant task for running regress tests`
`  |     |     +--- ant-junit          `
`  |     |     +--- ant-launcher`
`  |     |     +--- ant                // to compile the project`
`  |     |     +--- antlr3             // ant plugin for v3`
`  |     |     +--- junit              // to test the system`
`  |     |`
`  |     +--- runtime (BSD Licence)`
`  |     |     +--- shared-objects     // Factory of shared objects`
`  |     |     +--- jjtraveler         // Visitable interface`
`  |     |     +--- aterm              // ATerm library`
`  |     |     +--- tom-library        // support for strategies, bytecode, viewer, and xml `
`  |     |     +--- emf                // support for Eclipse Modelling Framework`
`  |     |     +--- Bytecode           // ADT for ASM Bytecode library`
`  |     |     +--- TNode              // ADT for XML`
`  |     |`
`  |     +--- tom (GPL Licence)`
`  |     |     +--- tom                // Tom compiler`
`  |     |     +--- gom                // Gom compiler`
`  |     |     +--- GomADT             // ADT for Gom `
`  |     |     +--- TomSignature       // ADT for Tom`
`  |     |     +--- plugin-platform    // plugin platform`
`  |     |     +--- PlatformAlert      // platform error messages`
`  |     |     +--- PlatformOption     // platform options`
`  |     |`
`  |     +--- tools (not needed by the runtime, unless you use bytecode, term viewer, or xml supports)`
`  |     |     +--- antlr-2.7.7        // to compile the Tom parser`
`  |     |     +--- antlr-3.2          // to compile the Gom parser`
`  |     |     +--- asm-3.0            // to manipulate bytecode`
`  |     |     +--- grappa1_2          // to visualize terms`
`  |     |     +--- xercesImpl         // for XML`
`  |     |     +--- xml-apis           // for XML`
`  |     |     +--- args4j-2.0.10      // for command line parsing`
`  |     |     +--- commons-lang-2.4`
`  |     `
`  +--- share (BSD Licence)`
`        +--- contrib`
`        |`
`        +--- man`
`        |`
`        +--- tom `
`              +--- adt`
`              +--- c`
`              +--- caml`
`              +--- java`

[Category:Documentation](/Category:Documentation "wikilink")