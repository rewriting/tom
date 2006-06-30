JAVA="java -classpath ..:${CLASSPATH}"

TEXFILE=nspk.tex
DATFILE=nspk.dat

echo "#nbAgent maxMsg apigen gom" > ${DATFILE}
echo "\hline" > ${TEXFILE}
echo "Agents & Messages & {\\apigen} & {\\gom} \\\\" >> ${TEXFILE}
echo "\hline" >> ${TEXFILE}

for i in `jot 10 1 10`; do
	echo -n "1 & $i   & " >> ${TEXFILE}
	echo -n "1 $i " >> ${DATFILE}
	APITIME=`${JAVA} gombench.ApiNsh 1 $i | awk -F'&' '{printf $4}'`
	echo -n "${APITIME} & " >> ${TEXFILE}
	echo -n "${APITIME} " >> ${DATFILE}
	GOMTIME=`${JAVA} gombench.GomNsh 1 $i | awk -F'&' '{printf $4}'`
	echo "${GOMTIME} \\\\" >> ${TEXFILE}
	echo "${GOMTIME} " >> ${DATFILE}
done

for i in `jot 2 1 2`; do
	echo -n "2 & $i  & " >> ${TEXFILE}
	echo -n "2 $i " >> ${DATFILE}
	APITIME=`${JAVA} -Xmx256m gombench.ApiNsh 2 $i | awk -F'&' '{printf $4}'`
	echo -n "${APITIME} & " >> ${TEXFILE}
	echo -n "${APITIME} " >> ${DATFILE}
	GOMTIME=`${JAVA} -Xmx256m gombench.GomNsh 2 $i | awk -F'&' '{printf $4}'`
	echo "${GOMTIME} \\\\" >> ${TEXFILE}
	echo "${GOMTIME} " >> ${DATFILE}
done
