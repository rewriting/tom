#
#    TOM - To One Matching Compiler
#

TSADT_SRC	= ${srcdir}/src/adt/TomSignature-${VERSION}.jar:${prefix}/share/TomSignature-${VERSION}.jar:${srcdir}/stable/adt/TomSignature-${VERSION}.jar

javadir         = ${prefix}/classes/jtom/checker
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(JAVAROOT)/adt:$(srcdir):$(JAVA_ATERM):$(SHARED_OBJECTS):$(TSADT_SRC):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/checker
BOOTSOURCES	= TomChecker.java TomSyntaxChecker.java TomTypeChecker.java TomCheckerMessage.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src

CLEANFILES 	= TomChecker.java
EXTRA_DIST 	= $(BOOTSOURCES) TomChecker.t

BUILT_SOURCES   = TomChecker.java

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noDeclaration
TOM = ${prefix}/bin/jtom
TomChecker.java: TomChecker.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<

