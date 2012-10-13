FILE=SimplePDLToPetriNet
LOCAL_CLASSPATH=.:lib/petrinetsemantics_updated_1.2.jar:lib/simplepdlsemantics_updated_1.2.jar:${CLASSPATH}
OPTS="-nt -p"

default: run

run: ${FILE}.class
	echo "java -cp … ${FILE}…"
	java -cp ${LOCAL_CLASSPATH} ${FILE} $1

${FILE}.java:
	echo "tom -nt -p ${FILE}.t…"
	tom ${OPTS} ${FILE}

${FILE}.class: ${FILE}.java
	echo "javac -cp … ${FILE}.java…"
	javac -cp ${LOCAL_CLASSPATH} ${FILE}.java

clean:
	rm -f *class ${FILE}.java

