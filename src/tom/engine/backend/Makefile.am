#
#    TOM - To One Matching Compiler
#

TSADT_SRC	= ${top_srcdir}/src/adt/TomSignature-${VERSION}.jar:${prefix}/share/TomSignature-${VERSION}.jar:${top_srcdir}/stable/adt/TomSignature-${VERSION}.jar

javadir         = ${prefix}/classes/jtom/backend
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(JAVAROOT)/adt:$(srcdir):$(JAVA_ATERM):$(TSADT_SRC):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/backend
BOOTSOURCES	= TomGenerator.java TomOptimizer.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src

CLEANFILES 	= TomGenerator.java TomOptimizer.java
EXTRA_DIST 	= $(BOOTSOURCES) TomGenerator.t TomOptimizer.t

BUILT_SOURCES   = TomGenerator.java TomOptimizer.java

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noDeclaration
TOM = ${top_builddir}/utils/jtom

TomOptimizer.java: TomOptimizer.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<

TomGenerator.java: TomGenerator.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<
