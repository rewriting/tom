FILE=SimplePDLToPetriNet
LOCAL_CLASSPATH=.:lib/petrinetsemantics_updated_1.2.jar:lib/simplepdlsemantics_updated_1.2.jar:${CLASSPATH}
OPTS=-p
#OPTS=-nt -p -i

default: ${FILE}.class

run: ${FILE}.class
	java -cp ${LOCAL_CLASSPATH} ${FILE} $1

${FILE}.java:
	tom ${OPTS} ${FILE}

${FILE}.class: ${FILE}.java
	javac -cp ${LOCAL_CLASSPATH} ${FILE}.java

clean:
	rm -f *class ${FILE}.java

