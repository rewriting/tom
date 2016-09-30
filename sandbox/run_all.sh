#!/bin/bash

for F in *.t; do
  pn=${F/.t/}
  bn=$(basename ${pn})
  echo $bn
  ./run.sh $bn
done
