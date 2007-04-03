JAVA="java -classpath ..:${CLASSPATH}"

TEXFILE=nspk.tex
DATFILE=nspk.dat

echo "#nbAgent maxMsg apigen gom gomv2" > ${DATFILE}
echo "\\multicolumn{1}{c}{\\makebox[0cm][l]{\\rotatebox{45}{Agents}}}" >> ${TEXFILE}
echo "  & \\multicolumn{1}{c}{\\makebox[0cm][l]{\\rotatebox{45}{Messages}}}" >> ${TEXFILE}
echo "      & \\multicolumn{1}{c}{\\makebox[0cm][l]{\\rotatebox{45}{\\apigen}}}" >> ${TEXFILE}
echo "            & \\multicolumn{1}{c}{\\makebox[0cm][l]{\\rotatebox{45}{\\gom}}}" >> ${TEXFILE}
echo "	            & \\multicolumn{1}{c}{\\makebox[0cm][l]{\\rotatebox{45}{{\\gom} v.2}}} \\\\" >> ${TEXFILE}
echo "\hline" >> ${TEXFILE}

for i in `jot 10 1 10`; do
	echo -n "1 & $i   & " >> ${TEXFILE}
	echo -n "1 $i " >> ${DATFILE}
	APITIME=`${JAVA} gombench.ApiNsh 1 $i | awk -F'&' '{printf $4}'`
	echo -n "${APITIME} & " >> ${TEXFILE}
	echo -n "${APITIME} " >> ${DATFILE}
	GOMTIME=`${JAVA} gombench.GomNsh 1 $i | awk -F'&' '{printf $4}'`
	echo -n "${GOMTIME} & " >> ${TEXFILE}
	echo -n "${GOMTIME} " >> ${DATFILE}
	GOMCMPTIME=`${JAVA} gombench.GomNshWithCompare 1 $i | awk -F'&' '{printf $4}'`
	echo "${GOMCMPTIME} \\\\" >> ${TEXFILE}
	echo "${GOMCMPTIME} " >> ${DATFILE}
done

for i in `jot 2 1 2`; do
	echo -n "2 & $i  & " >> ${TEXFILE}
	echo -n "2 $i " >> ${DATFILE}
	APITIME=`${JAVA} -Xmx256m gombench.ApiNsh 2 $i | awk -F'&' '{printf $4}'`
	echo -n "${APITIME} & " >> ${TEXFILE}
	echo -n "${APITIME} " >> ${DATFILE}
	GOMTIME=`${JAVA} -Xmx256m gombench.GomNsh 2 $i | awk -F'&' '{printf $4}'`
	echo -n "${GOMTIME} & " >> ${TEXFILE}
	echo -n "${GOMTIME} " >> ${DATFILE}
	GOMCMPTIME=`${JAVA} -Xmx256m gombench.GomNshWithCompare 2 $i | awk -F'&' '{printf $4}'`
	echo "${GOMCMPTIME} \\\\" >> ${TEXFILE}
	echo "${GOMCMPTIME} " >> ${DATFILE}
done
