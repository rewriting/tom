#
#    TOM - To One Matching Compiler
#

SUBDIRS = adt jtom
JARFILE = jtom-devel.jar
JARVERSIONFILE = jtom-devel-${VERSION}.jar
data_DATA  = 
CLEANFILES = ${JARFILE} ${JARVERSIONFILE}

EXTRA_DIST = TODO ALGO 

bootstrap:
	@$(MAKE) clean
	@cd adt && $(MAKE) install
	@cd jtom && $(MAKE) bootstrap

bootinstall:
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

jar: ${JARFILE}

${JARFILE}: ${JARVERSIONFILE}
	cp ${JARVERSIONFILE} ${JARFILE}

${JARVERSIONFILE}:
	 jar cf ${JARVERSIONFILE} \
	`cd ${top_builddir}/src && find . -name '*.class'`
