#
#    TOM - To One Matching Compiler
#
 
SUBDIRS = adt jtom
JARFILE = jtom-devel.jar
JARVERSIONFILE = jtom-devel-${VERSION}.jar
data_DATA  = 
CLEANFILES = ${JARFILE} ${JARVERSIONFILE}

EXTRA_DIST = TODO ALGO 

bootstep:
	@cd jtom && $(MAKE) clean
	@$(MAKE) 
	@$(MAKE) install


BOOTSTRAPDIR     = jtom
BOOTSTRAPDIRCOPY = tmp/jtom.bootstrap

bootstrap:
	@$(MAKE) clean
	@cd adt && $(MAKE) install

	@echo
	@echo "BOOTSTRAPPING PHASE 1"
	@echo
	$(MAKE) bootstep

	@echo
	@echo "BOOTSTRAPPING PHASE 2"
	@echo
	@$(MAKE) bootstep
	@rm -rf $(BOOTSTRAPDIRCOPY)
	@$(INSTALL) -d $(BOOTSTRAPDIR) $(BOOTSTRAPDIRCOPY)
	@cp -rp $(BOOTSTRAPDIR)/* $(BOOTSTRAPDIRCOPY)/

	@echo
	@echo "BOOTSTRAPPING PHASE 3"
	@echo
	@$(MAKE) bootstep

	@echo
	@echo "BOOTSTRAPPING CHECK"
	@echo "-------------------"
	@diff -r $(BOOTSTRAPDIR) $(BOOTSTRAPDIRCOPY)
	@rm -rf $(BOOTSTRAPDIRCOPY)

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
