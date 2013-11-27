#!/bin/bash

SERIES="1 10 100 500 1000 2000 3000 4000 5000" # 10000 20000 50000 100000"
RES=" "
for n in $SERIES
do
  echo "result$n.txt:"
  echo "phase#1"
  grep phase#1 result$n.txt | cut -d " " -f 4
  echo ${RES}
  echo "phase#2"
  grep phase#2 result$n.txt | cut -d " " -f 4
done
