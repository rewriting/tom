#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/adt

JAVAROOT        = ${top_builddir}/src/

EXTRA_DIST 	= TomSignature.adt TNode.adt Set.adt TomSignature.tom TNode.tom Set.tom
CLEANFILES 	= ${JARFILE_TS} ${JARFILE_TNODE} ${JARFILE_SET} TomSignature.tom TNode.tom Set.tom

JARFILE_TS      = TomSignature-$(VERSION).jar
JARFILE_TNODE   = TNode-$(VERSION).jar
JARFILE_SET     = Set-$(VERSION).jar
data_DATA       = ${JARFILE_TS} ${JARFILE_TNODE} ${JARFILE_SET}

adtdir          = $(datadir)/jtom
adt_DATA        = TomSignature.tom TNode.tom Set.tom

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f ${JARFILE_TS} ${JARFILE_TNODE} ${JARFILE_SET} $(stabledir)
	cp -f TomSignature.tom TNode.tom Set.tom $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

api:
	$(MAKE) TomSignature.tom
	$(MAKE) TNode.tom
	$(MAKE) Set.tom

TomSignature.tom: TomSignature.adt
	${ADTTOTOM}  -i TomSignature.adt -n TomSignature -p jtom.adt --javagen --version $(VERSION) 

TNode.tom: TNode.adt
	${ADTTOTOM}  -i TNode.adt -n TNode -p jtom.adt --javagen --version $(VERSION)

Set.tom: Set.adt
	${ADTTOTOM}  -i Set.adt -n Set -p jtom.adt --javagen --version $(VERSION) 

$(JARFILE_TS): TomSignature.tom

$(JARFILE_TNODE): TNode.tom

$(JARFILE_SET): Set.tom
