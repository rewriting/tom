#
#    TOM - To One Matching Compiler
#

include NodeTermMakeRules.am

javadir         = ${prefix}/classes/jtom/runtime/xml/adt
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/adt

${NodeTermAPI}: NodeTerm.tom

BOOTSOURCES	= ${NodeTermAPI}

java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES) NodeTermMakeRules.am NodeTerm.tom
JAVAROOT        = ${top_builddir}/src/

adtdir          = $(datadir)/jtom
adt_DATA        = NodeTerm.tom

CLEANFILES 	= $(BOOTSOURCES) NodeTerm.tom

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f NodeTerm.tom NodeTermMakeRules.am $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done
