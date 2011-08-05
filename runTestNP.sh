#!/bin/sh

java -cp $CLASSPATH:$TOM_HOME/lib/tom-compiler-full.jar tom.engine.newparser.tester.Tester $@ $TOM_HOME/../../testNP
