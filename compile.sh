#!/bin/bash

if [[ -e compile.lock ]]; then
  echo "already building";
  exit 1;
else
  touch compile.lock
  ./build.sh $@ 2>1 | tee log
  rm compile.lock
fi;

