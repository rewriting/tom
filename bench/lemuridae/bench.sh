#!/bin/bash

function repeat()
{
	local i max
	max=$1; shift;
	for ((i=1; i <= max ; i++)); do 
		eval "$@";
	done
}

time repeat 10 './lemu < tests/bench.lem > /dev/null'
