#
#    TOM - To One Matching Compiler

SUBDIRS = jtom

JARFILE        = jtom.jar
JARVERSIONFILE = jtom${VERSION}.jar
data_DATA  = $(JARFILE) $(JARVERSIONFILE)
CLEANFILES = ${JARFILE} ${JARVERSIONFILE}

${JARFILE}: ${JARVERSIONFILE}
	cp ${JARVERSIONFILE} ${JARFILE}

${JARVERSIONFILE}:
	 jar cf ${JARVERSIONFILE} \
	`cd ${top_builddir}/stable/ && find . -name '*.class'`
