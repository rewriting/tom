#!/bin/sh

EXAMPLES="car concString successeur vehicle"

for i in `echo $EXAMPLES`; do
	echo "### Ada example: $i/ ###"
	cd $i
	sh ./build_and_run.sh
	cd ..
done

