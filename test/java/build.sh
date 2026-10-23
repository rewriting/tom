#!/bin/sh

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$TOM_HOME" ] &&
    TOM_HOME=`cygpath --windows "$TOM_HOME"`
fi

LOCALCLASSPATH=`echo ${TOM_HOME}/lib/compiletime/junit.jar ${TOM_HOME}/lib/compiletime/ant-launcher.jar ${TOM_HOME}/lib/compiletime/ant.jar ${TOM_HOME}/lib/compiletime/ant-antlr.jar ${TOM_HOME}/lib/compiletime/antlr3.jar ${TOM_HOME}/lib/compiletime/ant-junit.jar| tr ' ' ':'`

ANT_OPTS=""
ant_exec_command="exec java $ANT_OPTS -classpath \"$LOCALCLASSPATH\" -Dant.library.dir=\"${TOM_HOME}/lib/compiletime\" org.apache.tools.ant.launch.Launcher $ANT_ARGS -cp \"$CLASSPATH\""

ARGS=$*

eval $ant_exec_command $ARGS

