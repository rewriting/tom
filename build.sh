#!/bin/sh

HERE=`pwd`
LOCALCLASSPATH=`echo ${HERE}/stable/lib/junit.jar | tr ' ' ':'` 
CLASSPATH=${LOCALCLASSPATH} ANT_OPTS="-XX:PermSize=128m -XX:MaxPermSize=128m" ant $*
