JAVA="java -classpath ..:${CLASSPATH}"
liststart=50
listmax=500

TEXFILE=crible.tex
DATFILE=crible.dat

echo "#size hand apigen gom" > ${DATFILE}
echo "\hline" > ${TEXFILE}
echo "Taille & Manuel & {\\apigen} & {\\tom} \\\\" >> ${TEXFILE}
echo "\hline" >> ${TEXFILE}
for i in `jot 10 $liststart $listmax`; do
	echo -n "$i   & " >> ${TEXFILE}
	echo -n "$i " >> ${DATFILE}
	HANDTIME=`${JAVA} gombench.HandList $i | awk -F'&' '{printf $3}'`
	echo -n "${HANDTIME} & " >> ${TEXFILE}
	echo -n "${HANDTIME} " >> ${DATFILE}
	APITIME=`${JAVA} gombench.ApiList $i | awk -F'&' '{printf $3}'`
	echo -n "${APITIME} & " >> ${TEXFILE}
	echo -n "${APITIME} " >> ${DATFILE}
	GOMTIME=`${JAVA} gombench.GomList $i | awk -F'&' '{printf $3}'`
	echo "${GOMTIME} \\\\" >> ${TEXFILE}
	echo "${GOMTIME} " >> ${DATFILE}
done
