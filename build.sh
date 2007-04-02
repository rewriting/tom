#!/bin/sh

HERE=`pwd`
LOCALCLASSPATH=`echo ${HERE}/stable/lib/junit.jar | tr ' ' ':'`
#CLASSPATH=${LOCALCLASSPATH} ANT_OPTS="-Xmx1024m -XX:PermSize=128m -XX:MaxPermSize=128m" ant with.clover $*
CLASSPATH=${LOCALCLASSPATH} ANT_OPTS="-Xmx512m -XX:PermSize=128m -XX:MaxPermSize=128m" ant $*
