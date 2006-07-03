JAVA="java -classpath ..:${CLASSPATH}"
liststart=50
listmax=500

TEXFILE=crible.tex
DATFILE=crible.dat

echo "" > ${DATFILE}
echo "" > ${DATFILE}.1.tmp
echo "" > ${DATFILE}.2.tmp
echo "" > ${DATFILE}.3.tmp
echo "\hline" > ${TEXFILE}
echo "Taille & Manuel & {\\apigen} & {\\tom} \\\\" >> ${TEXFILE}
echo "\hline" >> ${TEXFILE}
for i in `jot 10 $liststart $listmax`; do
	echo -n "$i   & " >> ${TEXFILE}
	HANDTIME=`${JAVA} gombench.HandList $i | awk -F'&' '{printf $3}'`
	echo -n "${HANDTIME} & " >> ${TEXFILE}
	echo "$i  ${HANDTIME} " >> ${DATFILE}.1.tmp
	APITIME=`${JAVA} gombench.ApiList $i | awk -F'&' '{printf $3}'`
	echo -n "${APITIME} & " >> ${TEXFILE}
	echo "$i  ${APITIME} " >> ${DATFILE}.2.tmp
	GOMTIME=`${JAVA} gombench.GomList $i | awk -F'&' '{printf $3}'`
	echo "${GOMTIME} \\\\" >> ${TEXFILE}
	echo "$i  ${GOMTIME} " >> ${DATFILE}.3.tmp
done

cat ${DATFILE}.1.tmp ${DATFILE}.2.tmp ${DATFILE}.3.tmp >> ${DATFILE}
