#!/bin/sh
testgendir=`mktemp -d /tmp/testgen.XXXXX`
outputdir=`mktemp -d /tmp/testoutput.XXXXX`
failuredir=$outputdir/failure
currentdir=$PWD
mkdir $failuredir
cd $testgendir
i=0 ;
j=0 ;
while [ $i -lt $1 ] ; do
  java testgen/Generator > log 2>&1
  if [ $? = 0 ] ; then
    find . -name '*.java' | xargs javac >> log 2>&1
    if [ $? != 0 ] ; then 
      echo "Failure(s) during java compilation (see log file)"
      mv log $failuredir/log${i}javac
      rm -rf $testgendir/*
    else 
      mkdir $outputdir/test$j
      mv $testgendir/* $outputdir/test$j
      j=$((j+1))
    fi
  else 
    echo "Failure(s) during hierarchy generation (see log file)"
    mv log $failuredir/log${i}generation
    rm -rf $testgendir/*
  fi
  i=$((i+1))
done
echo "Success rate : $j/$i"
mv $outputdir $currentdir/
