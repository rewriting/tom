#!/bin/sh

LOCALCLASSPATH=`echo lib/junit.jar | tr ' ' ':'` 

CLASSPATH=${LOCALCLASSPATH} ant $*
