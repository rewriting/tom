#
#    TOM - To One Matching Compiler
#

SUBDIRS         = c
javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/jtom/runtime/mapping

JAVAROOT        = ${top_builddir}/src/

MAPPING 	= dom.tom
EXTRA_DIST 	= ${MAPPING}
CLEANFILES 	= 

adtdir          = $(datadir)/jtom
adt_DATA        = ${MAPPING}

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f ${MAPPING} $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

