#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/jtom/runtime/mapping/caml

JAVAROOT        = ${top_builddir}/src

MAPPING         = int.tom list.tom
EXTRA_DIST 	= ${MAPPING}
CLEANFILES 	= 

adtdir          = $(datadir)/jtom/caml
adt_DATA        = ${MAPPING}

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f ${MAPPING} $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

