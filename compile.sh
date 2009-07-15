#!/bin/bash

if [[ -e compile.lock ]]; then
  echo "already building"
  exit
else
  touch compile.lock
  ./build.sh $@ 2>&1 | tee log
  grep '\[javac\] [0-9]* errors' log | grep -o '[0-9]*' >> stats
  rm compile.lock
fi;

