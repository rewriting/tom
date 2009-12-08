#!/bin/sh
if [ $# != 2 ] ; then 
  echo "usage: $0 <nb_hierarchies> <output_dir>" 
  return 1
fi
testgendir=`mktemp -d /tmp/testgen.XXXXX`
outputdir=`mktemp -d /tmp/testoutput.XXXXX`
failuredir=$outputdir/failure
currentdir=$PWD
mkdir $failuredir
cd $testgendir
i=0 ;
j=0 ;
while [ $i -lt $1 ] ; do
  echo "Step $i"
  #generate a class hierarchy
  java testgen/Generator > log 2>&1
  if [ $? = 0 ] ; then
    #try to compile with javac
    find . -name '*.java' | xargs javac >> log 2>&1
    if [ $? != 0 ] ; then 
      echo "Failure(s) during java compilation"
      mv log $failuredir/log${i}javac
      rm -rf $testgendir/*
    else 
      #try to compile with jlc
      for FILE in `find "${testgendir}" -name '*.java' | awk -F ".java" '{print $1}'` ; do
        cp $FILE.java $FILE.jl
      done
      find . -name '*.jl' | xargs jlc -nooutput >> log 2>&1
      if [ $? != 0 ] ; then 
        echo "Unexpected Failure(s) during jlc compilation"
        mv log $failuredir/log${i}jlc
      else
        mkdir $outputdir/test$j
        mv $testgendir/* $outputdir/test$j
        j=$((j+1))
      fi
    fi
  else 
    echo "Failure(s) during hierarchy generation"
    mv log $failuredir/log${i}generation
    rm -rf $testgendir/*
  fi
  i=$((i+1))
done
echo "Success rate : $j/$i"
cd $currentdir
mv $outputdir $2
