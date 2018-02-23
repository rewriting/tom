---
title: Documentation:Configuring your environment
permalink: /Documentation:Configuring_your_environment/
---

Editor
======

Vim
---

The Tom distribution contains some filetype plugins for Tom and Gom support in Vim. They are located in `${TOM_HOME}/share/contrib/vim`.

To install them, you should put the content of the `contrib/vim` directory in `${HOME}/.vim/`. Then in order for these plugins to be automatically used whenever it is necessary, you can put in your `${HOME}/.vimrc`:

`" indentation depends on the filetype`
`filetype indent on`
`filetype plugin on`

Also, to have vim automatically loading Tom and Gom plugins when editing Tom and Gom files, you can edit (or create) the file `${HOME}/.vim/filetype.vim`:

`if exists(`“`did_load_filetypes`”`)`
`  finish`
`endif`
`augroup filetypedetect`
`  au! BufNewFile,BufRead *.t   setfiletype tom`
`  au! BufNewFile,BufRead *.tom setfiletype tom`
`  au! BufNewFile,BufRead *.gom setfiletype gom`
`augroup END`

For Tom developers, the preferred setup for indenting Tom and <font color="purple">Java</font> code is as follows, to be placed in `${HOME}/.vimrc`:

`autocmd FileType ant   set expandtab`
`autocmd FileType ant   set sw=2`
`" automatically indent tom and java code`
`autocmd FileType java,tom,gom set cindent autoindent`
`autocmd FileType java,tom,gom set encoding=utf-8`
`autocmd FileType java,tom,gom set fileencoding=utf-8`
`" how to indent: in java and tom, 2 spaces, no tabs`
`autocmd FileType java,tom,gom set expandtab`
`autocmd FileType java,tom,gom set sw=2`
`autocmd FileType java,tom,gom set tabstop=2`
`autocmd FileType java,tom,gom set nosmarttab`

### Compiling Tom under Vim

To compile Tom programs (like Tom) under Vim, and get a correct error reporting, you will have to set the `${CLASSPATH}` environment value to the path of `junit.jar`, and use the `makeprg` variable to have `:make` call ant through a script maintaining the link between Tom and the generated <font color="purple">Java</font> files. We suppose here that Tom was installed in `${HOME}`, i.e. `${TOM_HOME}` is `${HOME}/workspace/jtom`.

`let $CLASSPATH = `“`${HOME}/workspace/jtom/src/lib/tools/junit.jar`”
`set makeprg=ant\ -find\ build.xml\ -emacs\ $*\\\|\`
`            \ awk\ -f\ \`“`${TOM_HOME}/../../utils/ant-tom.awk\`”

The provided awk script changes `.java` extensions into `.t` ones and removes `gen/` from the file paths mentioned in the error messages. You may have to tune it to fit your needs. Also note this is meant to be used with the POSIX version of awk (not the GNU one), although it may work out of the box on many configurations.

Shell
=====

Zsh
---

Zsh completion functions for Tom and Gom are available in the `share/contrib/zsh` directory. To install them, you only have to add those files to zsh’s `fpath` variable.

For example, assuming you already set up the `${TOM_HOME}` environment variable to the Tom installation directory, you can add to your `${HOME}/.zshrc`:

`fpath=(${TOM_HOME} $fpath)`

Build Tom projects using Ant
============================

To build complex projects using Tom, it is useful to use [Ant](http://ant.apache.org) as build system, to manage generation of data structure using Gom and the compilation of Tom and <font color="purple">Java</font> code.

To ease the use of [Ant](http://ant.apache.org), the file `${TOM_HOME}/lib/tom-common.xml` is provided in the Tom distribution. This file provide initialization for Tom and Gom Ant tasks. To load `tom-common.xml`, you just have to put in your Ant script:

``` xml
<property environment="env"/>
<property name="tom.home" value="${env.TOM_HOME}"/>
<import file="${tom.home}/lib/tom-common.xml"/>
```

Then, each target depending on the `tom.init` task will allow the use of the Gom and Tom ant tasks, as well as `tom.preset` and `javac.preset`, providing usual values for the attributes of those tasks. Also, `tom-common.xml` provides several properties, as `tomconfigfile` and `gomconfigfile`, providing the location of the configuration files for Tom and Gom, and `tom.classpath`, containing all classes related to the Tom installation.

For example, if you have a directory called `tom_example` with Tom source files and Gom source files which generate the Tom mapping required by `tom_example`, you can create the following Ant script to compile all Gom, Tom and <font color="purple">Java</font> code.:

``` xml
<?xml version="1.0" encoding="ISO-8859-1"?>
<project name="Example Ant for TOM" basedir="." default="compile">
  <description>
    A simple example of build script using Ant offering compilation of TOM programs.
  </description>

  <!-- Initializing Tom and Gom -->
  <property environment="env"/>
  <property name="tom.home" value="${env.TOM_HOME}"/>
  <import file="${tom.home}/lib/tom-common.xml"/>

  <!-- Defining folders -->
  <property name="my_example" value="tom_example"/>
  <property name="src.dir" value="."/>
  <property name="gen.dir" value="gen"/>
  <property name="build.dir" value="build"/>
  <property name="mapping.dir" value="${gen.dir}/${my_example}/${my_example}"/>

  <!-- Declaring Tom task -->
  <taskdef name="tom" classname="tom.engine.tools.ant.TomTask">
    <classpath refid="tom.classpath"/>
  </taskdef>

  <!-- Declaring Gom task -->
  <taskdef name="gom"
           classname="tom.gom.tools.ant.GomTask"
           classpathref="tom.classpath"/>

  <target name="init" depends="tom.init" description="To realize initialization">
    <mkdir dir="${gen.dir}"/>
    <mkdir dir="${build.dir}"/>
  </target>

  <target name="compile" depends="init" description="To compile all programs">
    <!-- Compiling Gom programs -->
    <gom config="${gomconfigfile}"
         srcdir="${src.dir}"
         package="${my_example}"
         destdir="${gen.dir}">
      <include name="**/*.gom"/>
    </gom>

    <!-- Compiling Tom programs -->
    <tom config="${tomconfigfile}"
         srcdir="${src.dir}"
         destdir="${gen.dir}"
         options="-I ${mapping.dir}">
      <include name="**/*.t"/>
    </tom>

    <!-- Compiling Java programs -->
    <javac srcdir="${gen.dir}" destdir="${build.dir}">
      <classpath path="${build.dir}"/>
      <include name="**/*.java"/>
    </javac>
  </target>

  <target name="clean" description="To remove generated code">
    <delete dir="${gen.dir}"/>
    <delete dir="${build.dir}"/>
  </target>
</project>
```

[Category:Documentation](/Category:Documentation "wikilink")