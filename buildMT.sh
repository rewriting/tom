#!/bin/sh

HERE=`pwd`
LOCALCLASSPATH=`echo ${HERE}/stable/lib/tools/junit.jar ${HERE}/stable/lib/tools/ant-antlr.jar | tr ' ' ':'`

#intial command line
#CLASSPATH=${LOCALCLASSPATH} ANT_OPTS="-Xmx512m -XX:PermSize=128m -XX:MaxPermSize=128m" ant -f MTbuild.xml $*

#let's try with a bit more memory
#CLASSPATH=${LOCALCLASSPATH} ANT_OPTS="-Xms512m -Xmx2048m -XX:PermSize=512m -XX:MaxPermSize=1024" ant -f MTbuild.xml $*
#CLASSPATH=${LOCALCLASSPATH} ANT_OPTS="-Xms1024m -Xmx4096m -XX:PermSize=512m -XX:MaxPermSize=2048" ant -f MTbuild.xml $*
#let's try with a lot of memory
CLASSPATH=${LOCALCLASSPATH} ANT_OPTS="-Xms1024m -Xmx8192m -XX:PermSize=512m -XX:MaxPermSize=2048" ant -f MTbuild.xml $*
