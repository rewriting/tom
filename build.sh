#!/bin/sh

LOCALCLASSPATH=`echo stable/lib/junit.jar | tr ' ' ':'` 

CLASSPATH=${LOCALCLASSPATH} ant $*
