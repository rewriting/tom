#!/bin/sh

FILE=SimplePDLToPetriNet
ARG=$1
LOCAL_CLASSPATH=.:../lib/petrinetsemantics_updated_1.2.jar:../lib/simplepdlsemantics_updated_1.2.jar:${CLASSPATH}
OPTS="-Xmx2048m -XX:-UseGCOverheadLimit"
RESULT=result${ARG}.txt
for i in `seq 1 10`
do
  echo "Test $i :" >> ${RESULT}
  java ${OPTS} -cp ${LOCAL_CLASSPATH} ${FILE} ${ARG} >> ${RESULT}
done
