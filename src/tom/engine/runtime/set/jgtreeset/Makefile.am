#
#    TOM - To One Matching Compiler
#

include SetMakeRules.am

javadir         = ${prefix}/classes/jtom/runtime/set/jgtreeset
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/runtime/set/jgtreeset

${SetAPI}: set.tom
BOOTSOURCES	= ${SetAPI}

java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES) SetMakeRules.am set.tom
JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= $(BOOTSOURCES) set.tom

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f SetMakeRules.am $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done
