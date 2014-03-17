#!/bin/sh

FILE=SimplePDLToPetriNet
#ARG=$1
HASWS=true #false #
#SERIES="1 10 100 500 1000 2000 3000 4000 5000 10000 20000 50000 100000"
SERIES="1000000"
LOCAL_CLASSPATH=.:../lib/petrinetsemantics_updated_1.2.jar:../lib/simplepdlsemantics_updated_1.2.jar:${CLASSPATH}
OPTS="-Xmx20000m -XX:-UseGCOverheadLimit"
#RESULT=result${ARG}.txt
#ITERATIONS="1 2 3 4 5 6 7 8 9 10"
ITERATIONS="1"

for n in $SERIES
do
  RESULT=result$n.txt
  echo "===============" >> encours/${RESULT}
  echo "Test with $n WD" >> encours/${RESULT}
  echo "===============" >> encours/${RESULT}
  echo "" >> encours/${RESULT}
  for i in $ITERATIONS
  do
    echo "Test $n.$i :" >> encours/${RESULT}
    #java ${OPTS} -cp ${LOCAL_CLASSPATH} ${FILE} ${ARG} >> ${RESULT}
    java ${OPTS} -cp ${LOCAL_CLASSPATH} ${FILE} $n ${HASWS} >> encours/${RESULT}
  done
done
