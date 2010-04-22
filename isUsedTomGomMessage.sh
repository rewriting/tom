#!/bin/sh

tommsg=0;
gommsg=0;

echo "These TomMessage are not used :"
echo
for m in `grep "public\ static\ final\ TomMessage.*=" src/tom/engine/TomMessage.java | cut -d" " -f7 | sed -e 's/=//g'`; do
	grep -nR $m src/tom/* | grep -v TomMessage.java | grep -v Binary >/dev/null;
	if [ $? == 1 ]
	then
		echo "$m" # TomMessage is not used."
		((tommsg=$tommsg+1))
	fi
done
echo
echo "These GomMessage are not used :"
echo
for m in `grep "public\ static\ final\ GomMessage.*=" src/tom/gom/GomMessage.java | cut -d" " -f7 | sed -e 's/=//g'`; do
	grep -nR $m src/tom/* | grep -v GomMessage.java | grep -v Binary >/dev/null;
	if [ $? == 1 ]
	then
		echo "$m" # GomMessage is not used."
		((gommsg=$gommsg+1))
	fi
done

echo 
echo "$tommsg TOMMESSAGE ARE NOT USED IN SRC/TOM/"
echo "$gommsg GOMMESSAGE ARE NOT USED IN SRC/TOM/"
