#
#    TOM - To One Matching Compiler
#

CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(TOMSIGNATURE):$$CLASSPATH

javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/adt

JAVAROOT        = ${top_builddir}/src/

EXTRA_DIST 	= TomSignature.adt TNode.adt set.adt
CLEANFILES 	= ${JARFILE_TS} ${JARFILE_TNODE} ${JARFILE_SET} TomSignature.tom TNode.tom set.tom

JARFILE_TS      = TomSignature-$(VERSION).jar
JARFILE_TNODE   = TNode-$(VERSION).jar
JARFILE_SET     = set-$(VERSION).jar
data_DATA       = ${JARFILE_TS} ${JARFILE_TNODE} ${JARFILE_SET}

adtdir          = $(datadir)/jtom
adt_DATA        = TomSignature.tom TNode.tom set.tom

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f ${JARFILE_TS} ${JARFILE_TNODE} ${JARFILE_SET} $(stabledir)
	cp -f TomSignature.tom TNode.tom set.tom $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

api:
	$(MAKE) TomSignature.tom
	$(MAKE) TNode.tom
	$(MAKE) set.tom

TomSignature.tom: TomSignature.adt
	${ADTTOTOM}  -i TomSignature.adt -n TomSignature --javagen --version $(VERSION) -p jtom.adt

TNode.tom: TNode.adt
	${ADTTOTOM}  -i TNode.adt -n TNode --javagen -p jtom.runtime.xml.adt --version $(VERSION)

set.tom: set.adt
	${ADTTOTOM}  -i set.adt -n set --javagen --version $(VERSION) -p jtom.runtime.set.jgtreeset

$(JARFILE_TS): TomSignature.tom

$(JARFILE_TNODE): TNode.tom

$(JARFILE_SET): set.tom
