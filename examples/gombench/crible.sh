JAVA="java -classpath ..:${CLASSPATH}"
liststart=50
listmax=500

echo "\hline"
echo "Taille & Manuel & {\\apigen} & {\\tom} \\\\"
echo "\hline"
for i in `jot 10 $liststart $listmax`; do
	echo -n "$i   & "
	${JAVA} gombench.HandList $i | awk -F'&' '{printf $3}'
	echo -n " & "
	${JAVA} gombench.ApiList $i | awk -F'&' '{printf $3}'
	echo -n " & "
	${JAVA} gombench.GomList $i | awk -F'&' '{printf $3}'
	echo " \\\\"
done	
