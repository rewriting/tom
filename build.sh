#!/bin/sh

HERE=`pwd`
LOCALCLASSPATH=`echo ${HERE}/stable/lib/compiletime/junit.jar ${HERE}/stable/lib/compiletime/ant-launcher.jar ${HERE}/stable/lib/compiletime/ant.jar ${HERE}/stable/lib/compiletime/ant-antlr.jar ${HERE}/stable/lib/compiletime/antlr3.jar ${HERE}/stable/lib/compiletime/ant-junit.jar| tr ' ' ':'`

#CLASSPATH=${LOCALCLASSPATH} ANT_OPTS="-Xmx512m -XX:PermSize=128m -XX:MaxPermSize=128m" ant $*

ANT_OPTS="-Xmx512m"
ant_exec_command="exec java $ANT_OPTS -classpath \"$LOCALCLASSPATH\" -Dant.library.dir=\"${HERE}/stable/lib/compiletime\" org.apache.tools.ant.launch.Launcher $ANT_ARGS -cp \"$CLASSPATH\""

eval $ant_exec_command $*

