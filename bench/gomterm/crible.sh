JAVA="java -classpath ..:${CLASSPATH}"
liststart=50
listmax=500

TEXFILE=crible.tex
DATFILE=crible.dat

echo -n "" > ${DATFILE}
echo -n "" > ${DATFILE}.1.tmp
echo "" > ${DATFILE}.2.tmp
echo "" > ${DATFILE}.3.tmp
echo "\\begin{tabular}{|l|rrr|}" > ${TEXFILE}
echo "\hline" >> ${TEXFILE}
echo "Taille & \\multicolumn{1}{c}{\\aterm} & \\multicolumn{1}{c}{\\apigen} & \\multicolumn{1}{c}{\\gom} \\\\" >> ${TEXFILE}
echo "\hline" >> ${TEXFILE}
for i in `jot 10 $liststart $listmax`; do
	echo -n "$i   & " >> ${TEXFILE}
	ATTIME=`${JAVA} gombench.AtList $i | awk -F'&' '{printf $3}'`
	echo -n "\\nombre[,]{${ATTIME/./,}} & " >> ${TEXFILE}
	echo "$i  ${ATTIME} " >> ${DATFILE}.1.tmp
	APITIME=`${JAVA} gombench.ApiList $i | awk -F'&' '{printf $3}'`
	echo -n "\\nombre[,]{${APITIME/./,}} & " >> ${TEXFILE}
	echo "$i  ${APITIME} " >> ${DATFILE}.2.tmp
	GOMTIME=`${JAVA} gombench.GomList $i | awk -F'&' '{printf $3}'`
	echo "\\nombre[,]{${GOMTIME/./,}} \\\\" >> ${TEXFILE}
	echo "$i  ${GOMTIME} " >> ${DATFILE}.3.tmp
done

echo "\hline" >> ${TEXFILE}
echo "\end{tabular}" >> ${TEXFILE}
cat ${DATFILE}.1.tmp ${DATFILE}.2.tmp ${DATFILE}.3.tmp >> ${DATFILE}
