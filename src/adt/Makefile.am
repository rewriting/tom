#
#    TOM - To One Matching Compiler
#

CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(TOMSIGNATURE):$$CLASSPATH

javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/jtom

JAVAROOT        = ${top_builddir}/src/

EXTRA_DIST 	= TomSignature.adt
CLEANFILES 	= ${JARFILE_TS} ${JARFILE_TNODE} ${JARFILE_SET} TomSignature.tom TNode.tom set.tom

JARFILE_TS      = TomSignature-$(VERSION).jar
JARFILE_TNODE   = TNode-$(VERSION).jar
JARFILE_SET     = set-$(VERSION).jar
data_DATA       = ${JARFILE_TS} ${JARFILE_TNODE} ${JARFILE_SET}

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

api:
	$(MAKE) TomSignature.tom
	$(MAKE) TNode.tom
	$(MAKE) set.tom


TomSignature.tom: TomSignature.adt
	${ADTTOJAVA} --version $(VERSION) -i TomSignature.adt -n TomSignature -p jtom.adt
	${ADTTOTOM}  -i TomSignature.adt -n TomSignature

TNode.tom: TNode.adt
	${ADTTOJAVA} --version $(VERSION) -i TNode.adt -n TNode -p jtom.runtime.xml.adt
	${ADTTOTOM}  -i TNode.adt -n TNode

set.tom: set.adt
	${ADTTOJAVA} --version $(VERSION) -i set.adt -n set -p jtom.runtime.set.jgtreeset
	${ADTTOTOM}  -i set.adt -n set

$(JARFILE_TS): TomSignature.tom

$(JARFILE_TNODE): TNode.tom

$(JARFILE_SET): set.tom
