#!/bin/sh

if [ "$1" = "clean" ]
then
	find ligneproduitstelephones/. -name ¨.*class¨ | xargs rm 
else
	echo "Compiling ClassUsingLPTMapping.t…"
	tom ClassUsingLPTMapping.t
	echo "Compiling ClassUsingLPTMapping.java…"
	javac -sourcepath . ClassUsingLPTMapping.java
	echo "Executing ClassUsingLPTMapping…"
	java ClassUsingLPTMapping
fi
