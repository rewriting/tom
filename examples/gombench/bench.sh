JAVA="java -classpath ..:${CLASSPATH}"
liststart=50
listmax=500

for i in `jot 10 1 10`; do
	${JAVA} gombench.ApiNsh 1 $i
	${JAVA} gombench.GomNsh 1 $i
done

for i in `jot 2 1 2`; do
	${JAVA} -Xmx256m gombench.ApiNsh 2 $i
	${JAVA} -Xmx256m gombench.GomNsh 2 $i
done

for i in `jot 10 $liststart $listmax`; do
	${JAVA} gombench.HandList $i
	${JAVA} gombench.GomList $i
	${JAVA} gombench.ApiList $i
done	
