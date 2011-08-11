#!/bin/sh

if [ $# -eq 0 ]
then
  java -cp $CLASSPATH:$TOM_HOME/lib/tom-compiler-full.jar tom.engine.newparser.debug.tester.Tester $TOM_HOME/../../testNP
else
  java -cp $CLASSPATH:$TOM_HOME/lib/tom-compiler-full.jar tom.engine.newparser.debug.tester.Tester $@ 
fi
