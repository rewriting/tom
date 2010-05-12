#!/bin/bash

export CLASSPATH=`echo ${ECLIPSE_HOME}/plugins/org.eclipse.emf*.jar | tr ' ' ':'`:${CLASSPATH}
export ECLIPSE_WORKSPACE="/home/jcb/workspace/eclipseProjects"
