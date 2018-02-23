---
title: Documentation:Compiling trunk
permalink: /Documentation:Compiling_trunk/
---

Compiling Tom from sources (**\***)
===================================

The Tom and Gom compilers are written in Tom itself. The compiler is certainly the most complex program written in Tom. Most of the constructs provided by Tom are used and tested in the project itself.

Getting the sources
===================

Command line
------------

The Tom and Gom sources are available through anonymous Source Code Managment system (SCM). We used subversion but we switched to git. We try to keep both repositories up-to-date but jtom on **svn** may not be always synchronized. To get sources, prefer the git command :

`git clone `[`git://scm.gforge.inria.fr/tom/tom.git`](git://scm.gforge.inria.fr/tom/tom.git)

Developer access is possible via `ssh` or `DAV`. See [Gforge](http://gforge.inria.fr/scm/?group_id=78) for detailed instructions.

The `git` (or `svn`) checkout will get a new repertory in your current working directory, called `jtom`. We will call this directory `${TOM_BASE}`.

For GNU/Linux or Mac users, it is very simple to install and use Git (available through packages management systems), but it might be a bit difficult to use under Windows. We advise Windows users to install [msysgit](http://code.google.com/p/msysgit/): after few tests, it seems to run correctly and we did not encounter any issue during Tom installation.

Using Eclipse
-------------

-   install [subclipse](http://subclipse.tigris.org/), `1.4.x` for instance (see [detailed instructions](http://subclipse.tigris.org/install.html)), providing support for Subversion within Eclipse
-   `Window->Open Perspective->Other->SVN Repository Exploring`
-   `New->Repository Location`
-   [`svn://scm.gforge.inria.fr/svn/tom`](svn://scm.gforge.inria.fr/svn/tom) (anonymous access)
-   [`https://scm.gforge.inria.fr/svn/tom`](https://scm.gforge.inria.fr/svn/tom) (registered users)
-   expand `jtom` and do a checkout on the `trunk`
-   “checkout as a project in the workspace”, call it `jtom`, click `Finish`

Prepare for the build
=====================

First of all, please read `${TOM_BASE}/INSTALL_DEV`. Even if the current guide should be enough, this file may contain useful information.

Optionally, to customize the build environment, you will have to copy the template configuration file `${TOM_BASE}/local.properties_template` to `${TOM_BASE}/local.properties`, and edit the latter it to reflect your setup. The most common is to set `build.compiler=jikes` to use the `jikes` compiler, and `build.compiler.emacs=true` to get errors in a format the `emacs` editor can handle.

Configure user environment variables
====================================

-   declare the `TOM_HOME` environment variable and assign it to <eclipse workspace>`/jtom/src/dist` (<eclipse workspace>`\jtom\src\dist` for Windows)
-   set `JAVA_HOME` environment variable. This should be set to the directory where your JDK is installed, for instance: `C:\Program Files\Java\jdk1.6.0_13`
-   set `PATH` to `JAVA_HOME/bin` (`%JAVA_HOME%\bin` for Windows)
-   do not forget to relaunch Eclipse if you use it

Build the Tom distribution
==========================

The build process uses [apache ant](http://ant.apache.org) to automate the all parts of the process. This build process is specified by the `${TOM_BASE}/build.xml` file.

Command line
------------

You need to have apache [apache ant](http://ant.apache.org) (1.7 or earlier) installed in order to compile .

To compile the stable distribution of Tom, you have to use the `${TOM_BASE}/build.sh` script. To use it, first make sure to **cd(1)** to `${TOM_BASE}`. Then you can build the stable distribution.

`$ ./build.sh stable`

This creates the `stable` distribution in the directory `${TOM_BASE}/stable/dist`.

To build and install the source distribution, you have to do the following:

`$ ./build.sh stable`
`$ ./build.sh src.dist`

This creates the `src` distribution in the directory `${TOM_BASE}/src/dist`.

To list all the available targets for the build:

`$ ./build.sh -projecthelp`

Using Eclipse
-------------

-   `Window->Open Perspective->Other->Java (default)`
-   `Window->Show View->Other->Ant`
-   expand `jtom` and move the `build.xml` file to the Ant view
-   in the Ant view, expand `Ant for TOM` and click on the `stable` target
-   click on the `src.dist` target

Git guide for the beginner
==========================

Here you will find some useful documentation for git :

-   <http://www.unixgarden.com/index.php/administration-systeme/git-pour-les-futurs-barbus> \[fr\]
-   <http://alx.github.com/gitbook/3_usage_basique_des_branches_et_des_merges.html> \[fr\]
-   <http://github.com/guides/git-cheat-sheet> \[en\]
-   <http://marklodato.github.com/visual-git-guide/> \[en\] (a good explanation to understand how git works)
-   <http://gitref.org/remotes/> \[en\]
-   <http://stackoverflow.com/questions/67699/how-do-i-clone-all-remote-branches-with-git> \[en\]
-   <http://progit.org/book/> \[en\]
-   <http://oreilly.com/catalog/9780596520137> \[en\]

[Category:Documentation](/Category:Documentation "wikilink")