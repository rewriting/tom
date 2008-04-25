#!/bin/sh

mkdir -p /tmp/testgen
mkdir -p /tmp/output/failure
cd /tmp/testgen
i = 0 ;
j = 0 ;
for   [ $i -lt 10 ] ; do
  java testgen/Generator > log
  if [ $? = 0 ] ; then
    find . -name '*.java' | xargs javac >> log 2>&1
    if [ $? != 0 ] ; then 
      echo "Failure(s) during java compilation (see log file)"
      mv log /tmp/output/failure/log$i
      rm -rf *
    else 
      mkdir /tmp/output/test$j
      mv * /tmp/output/test$j
      j = $((j+1))
    fi
  else 
    echo "Failure(s) during hierarchy generation (see log file)"
    mv log /tmp/output/failure/log$i
    rm -rf *
  fi
  i = $((i+1))
done
