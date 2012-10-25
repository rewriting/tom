#!/bin/sh

EXAMPLES="arraytree recordtree"

for i in `echo $EXAMPLES`; do
	echo "### Ada example: $i/ ###"
	pushd $i
	sh ./build_and_run.sh
	popd
done

