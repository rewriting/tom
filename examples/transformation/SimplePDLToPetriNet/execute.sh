#!/bin/sh

LOCAL_CLASSPATH=.:petrinetsemantics_updated_1.2.jar:simplepdlsemantics_updated_1.2.jar:$CLASSPATH
TOM_OPTS="-nt -p" #-i"
FILE="SimplePDLToPetriNoHash"

echo "tom ${TOM_OPTS} ${FILE}.t…"
tom ${TOM_OPTS} ${FILE}.t

echo "javac -cp … ${FILE}.java…"
javac -cp $LOCAL_CLASSPATH ${FILE}.java

echo "java -cp … ${FILE}…"
java -cp $LOCAL_CLASSPATH ${FILE} $1
