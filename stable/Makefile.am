#
#    TOM - To One Matching Compiler

SUBDIRS = jtom

JARFILE    = jtom-${VERSION}.jar
data_DATA  = ${JARFILE} 
CLEANFILES = ${JARFILE}

${JARFILE}:
	jar cf ${JARFILE} \
	`cd ${top_builddir}/stable && find . -name '*.class'`
