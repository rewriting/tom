MAINFILE=SimplePDLToPetriNet
WD=10
HASWS=true #false #
ARGS=${WD} ${HASWS}
ARG=models/DefaultSimplePDLInstance.xmi
#ARG=models/SimplePDLInstance2.xmi
LOCAL_CLASSPATH=.:lib/petrinetsemantics_updated_1.2.jar:lib/simplepdlsemantics_updated_1.2.jar:${CLASSPATH}
OPTS="-nt -p"

default: run

#%.class: %.java
#	javac -cp ${LOCAL_CLASSPATH} $< 

#%.java: %.t
#	tom ${OPTS} $< 

run: ${MAINFILE}.class
	java -cp ${LOCAL_CLASSPATH} ${MAINFILE} ${ARG}
#	java -cp ${LOCAL_CLASSPATH} ${MAINFILE} ${ARGS}

GenModel.java: GenModel.t
	tom ${OPTS} GenModel.t

${MAINFILE}.java: ${MAINFILE}.t
	tom ${OPTS} ${MAINFILE}

${MAINFILE}.class: ${MAINFILE}.java GenModel.java
	javac -cp ${LOCAL_CLASSPATH} ${MAINFILE}.java

clean:
	#rm -f *class ${MAINFILE}.java GenModel.java
	rm -f *class GenModel.java

