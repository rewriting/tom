#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/jtom/runtime/mapping/c

JAVAROOT        = ${top_builddir}/src

MAPPING         = aterm.tom int.tom string.tom
EXTRA_DIST 	= ${MAPPING}
CLEANFILES 	= 

adtdir          = $(datadir)/jtom/c
adt_DATA        = ${MAPPING}

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f ${MAPPING} $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

