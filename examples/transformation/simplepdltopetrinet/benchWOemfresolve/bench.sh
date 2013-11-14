#!/bin/sh

FILE=SimplePDLToPetriNet
#ARG=$1
HASWS=true #false #
SERIES="1 10 100 500 1000 2000 3000 4000 5000 10000"
LOCAL_CLASSPATH=.:../lib/petrinetsemantics_updated_1.2.jar:../lib/simplepdlsemantics_updated_1.2.jar:${CLASSPATH}
OPTS="-Xmx2048m -XX:-UseGCOverheadLimit"
#RESULT=result${ARG}.txt
ITERATIONS="1 2 3 4 5 6 7 8 9 10"

for n in $SERIES
do
  RESULT=result$n.txt
  echo "===============" >> ${RESULT}
  echo "Test with $n WD" >> ${RESULT}
  echo "===============" >> ${RESULT}
  echo "" >> ${RESULT}
  for i in $ITERATIONS
  do
    echo "Test $n.$i :" >> ${RESULT}
    #java ${OPTS} -cp ${LOCAL_CLASSPATH} ${FILE} ${ARG} >> ${RESULT}
    java ${OPTS} -cp ${LOCAL_CLASSPATH} ${FILE} $n ${HASWS} >> ${RESULT}
  done
done
