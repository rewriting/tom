FILE=SimplePDLToPetriNoHash
LOCAL_CLASSPATH=.:petrinetsemantics_updated_1.2.jar:simplepdlsemantics_updated_1.2.jar:${CLASSPATH}
OPTS="-nt -p"

default: run

run: ${FILE}.class
	echo "java -cp … ${FILE}…"
	java -cp ${LOCAL_CLASSPATH} ${FILE} $1

${FILE}.java:
	echo "tom -nt -p i${FILE}.t…"
	tom ${OPTS} ${FILE}

${FILE}.class: ${FILE}.java
	echo "javac -cp … ${FILE}.java…"
	javac -cp ${LOCAL_CLASSPATH} ${FILE}.java

clean:
	rm -f *class ${FILE}.java

