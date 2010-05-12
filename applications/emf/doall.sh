#!/bin/sh

if [ "$1" = "clean" ]
then
	rm mappings/* && ant clean
else
	ant compile && ./generateMappings.sh
fi	
