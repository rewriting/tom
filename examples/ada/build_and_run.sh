#!/bin/sh

EXAMPLES="car concString successeur vehicle"

mkdir -p ../gen/ada
for i in `echo $EXAMPLES`; do
	echo "### Ada example: $i/ ###"
	cp -R $i ../gen/ada/.
	pushd ../gen/ada/$i
	sh ./build_and_run.sh
	popd
done

