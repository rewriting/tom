#
#    TOM - To One Matching Compiler
#

TSADT_SRC	= ${top_srcdir}/src/adt/TomSignature-${VERSION}.jar:${prefix}/share/TomSignature-${VERSION}.jar:${top_srcdir}/stable/adt/TomSignature-${VERSION}.jar

javadir         = ${prefix}/classes/jtom/debug
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(JAVAROOT)/adt:$(srcdir):$(JAVA_ATERM):$(TSADT_SRC):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/debug
BOOTSOURCES	= TomDebugger.java TomDebugEnvironment.java TermHandler.java TomDebugStructure.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src
EXTRA_DIST 	= $(BOOTSOURCES)

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

