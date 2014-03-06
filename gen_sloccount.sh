#!/bin/sh
# 
# TOM - To One Matching Compiler
# 
# Copyright (c) 2000-2014, Universite de Lorraine, Inria
# Nancy, France.
# 
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
# 
# Jean-Christophe Bach  e-mail: jeanchristophe.bach@inria.fr
#

SAVESLOCDIR=sloccounts
DATE=`date +%Y-%m-%d_%H%M%S`
DIRS="src/tom examples test"

#mkdir ${DATE}
mkdir ${SAVESLOCDIR}/${DATE}

for i in ${DIRS}; do
  SANITNAME=`echo $i | sed 's/\///g'`
  FILENAME=${SAVESLOCDIR}/${DATE}/"sloccount_$SANITNAME.txt"
  echo -e "=================\n${DATE}\n=================" >> ${FILENAME}
  echo -e "sloccount --filecount $i \n" >> ${FILENAME}
  sloccount --filecount $i >> ${FILENAME}
  echo -e "\n---------------------------\n" >> ${FILENAME}
  echo -e "sloccount $i \n" >> ${FILENAME}
  sloccount $i >> ${FILENAME}
  echo -e "\n---------------------------\n" >> ${FILENAME}
done;


