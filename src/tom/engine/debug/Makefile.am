#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/debug
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/debug
BOOTSOURCES	= TomDebugger.java TomDebugEnvironment.java TermHandler.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= TomDebugger.class  TomDebugEnvironment.class TermHandler.class
EXTRA_DIST 	= $(BOOTSOURCES)

BUILT_SOURCES   = TomDebugger.java TomDebugEnvironment.java TermHandler.java

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

