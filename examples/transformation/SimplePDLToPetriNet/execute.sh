#!/bin/sh

LOCAL_CLASSPATH=.:petrinetsemantics_updated_1.2.jar:simplepdlsemantics_updated_1.2.jar:$CLASSPATH

echo "tom -nt -p SimplePDLToPetriNoHash.t…"
tom -nt -p SimplePDLToPetriNoHash.t
echo "javac -cp … SimplePDLToPetriNoHash.java…"
javac -cp $LOCAL_CLASSPATH SimplePDLToPetriNoHash.java
echo "java -cp … SimplePDLToPetriNoHash…"
java -cp $LOCAL_CLASSPATH SimplePDLToPetriNoHash $1
