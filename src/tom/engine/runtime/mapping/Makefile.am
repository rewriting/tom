#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/adt

JAVAROOT        = ${top_builddir}/src/

EXTRA_DIST 	= dom.tom
CLEANFILES 	= 

adtdir          = $(datadir)/jtom
adt_DATA        = dom.tom

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f dom.tom $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

