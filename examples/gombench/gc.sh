JAVA="java -classpath ..:${CLASSPATH}"

DATFILE=gc.dat

echo -n "" > ${DATFILE}

for i in `jot 5 0 4`; do
	${JAVA} gombench.GomGC $i >> ${DATFILE}
done

echo "" > ${DATFILE}

for i in `jot 5 0 4`; do
	${JAVA} gombench.ApiGC $i >> ${DATFILE}
done
