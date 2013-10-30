FILE=SimplePDLToPetriNet
FILE_NOR=SimplePDLToPetriNet_noresolve
WD=10
HASWS=true #false#
#ARG=models/DefaultSimplePDLInstance.xmi
#ARG=models/SimplePDLInstance2.xmi
LOCAL_CLASSPATH=.:../lib/petrinetsemantics_updated_1.2.jar:../lib/simplepdlsemantics_updated_1.2.jar:${CLASSPATH}
OPTS="-nt -p"
#LOCAL_CLASSPATH_NOR=.:../lib/petrinetsemantics_updated_1.2.jar:../lib/simplified_simplepdlsemantics_updated_1.2.jar:${CLASSPATH}

default: run

%.class: %.java
	javac -cp ${LOCAL_CLASSPATH} $< 

%.java: %.t
	tom ${OPTS} $< 

noresolve: ${FILE_NOR}.class
	java -cp ${LOCAL_CLASSPATH} ${FILE_NOR} ${WD} ${HASWS}

run: ${FILE}.class
	java -cp ${LOCAL_CLASSPATH} ${FILE} ${WD} ${HASWS}

${FILE}.java:
	tom ${OPTS} ${FILE} GenModel.t

${FILE_NOR}.java:
	tom ${OPTS} ${FILE_NOR} GenModel.t

${FILE}.class: ${FILE}.java
	javac -cp ${LOCAL_CLASSPATH} ${FILE}.java

clean:
	rm -f *class ${FILE}.java ${FILE_NOR}.java GenModel.java

