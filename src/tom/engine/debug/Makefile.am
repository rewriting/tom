#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/debug
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(TOMSIGNATURE):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/debug
BOOTSOURCES	= TomDebugger.java TomDebugEnvironment.java TermHandler.java TomDebugStructure.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/
EXTRA_DIST 	= $(BOOTSOURCES)

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

