#!/bin/sh

FILE=SimplePDLToPetriNet
ARG=$1

LOCAL_CLASSPATH=.:../lib/petrinetsemantics_updated_1.2.jar:../lib/simplepdlsemantics_updated_1.2.jar:./emflibs/org.eclipse.emf.common_2.5.0.v200906151043.jar:./emflibs/org.eclipse.emf.ecore.xmi_2.5.0.v200906151043.jar:./emflibs/org.eclipse.emf.ecore_2.5.0.v200906151043.jar:./emflibs/org.eclipse.uml2.uml_3.2.100.v201108110105.jar:${CLASSPATH}
#LOCAL_CLASSPATH=.:../lib/petrinetsemantics_updated_1.2.jar:../lib/simplepdlsemantics_updated_1.2.jar:${CLASSPATH}
OPTS="-Xmx20g -XX:-UseGCOverheadLimit"
RESULT=result${ARG}.txt
for i in `seq 1 10`
do
  echo "Test $i :" >> ${RESULT}
  java ${OPTS} -cp ${LOCAL_CLASSPATH} ${FILE} ${ARG} >> ${RESULT}
done
