FILE=SimplePDLToPetriNet
ARG=10
#ARG=models/DefaultSimplePDLInstance.xmi
#ARG=models/SimplePDLInstance2.xmi
LOCAL_CLASSPATH=.:../lib/petrinetsemantics_updated_1.2.jar:../lib/simplepdlsemantics_updated_1.2.jar:${CLASSPATH}
OPTS="-nt -p"

default: run

run: ${FILE}.class
	java -cp ${LOCAL_CLASSPATH} ${FILE} ${ARG}

${FILE}.java:
	tom ${OPTS} ${FILE} GenModel.t

${FILE}.class: ${FILE}.java
	javac -cp ${LOCAL_CLASSPATH} ${FILE}.java

clean:
	rm -f *class ${FILE}.java

