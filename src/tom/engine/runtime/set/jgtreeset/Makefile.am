#
#    TOM - To One Matching Compiler
#

include SetMakeRules

javadir         = ${prefix}/classes/jtom/runtime/set/jgtreeset
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/runtime/set/jgtreeset

BOOTSOURCES	= ${SetAPI}

java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES) SetMakeRules set.t
JAVAROOT        = ${top_builddir}/src/

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f SetMakeRules $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done
