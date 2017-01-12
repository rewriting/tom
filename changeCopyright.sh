#!/bin/sh

#set manually the new year, or pass it as a parameter
#NEWYEAR="2018"
NEWYEAR=`date +%Y`
if [ -n "$1" ] ; then
  NEWYEAR=$1
else
  NEWYEAR=`date +%Y`
fi

DIR="./src/"

grep -rl "\(2[0-9]\{3\}\)-2[0-9]\{3\}" $DIR | xargs sed -i "s/\(2[0-9]\{3\}\)-2[0-9]\{3\}/\1-$NEWYEAR/g"

