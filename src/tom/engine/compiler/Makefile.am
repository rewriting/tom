#
#    TOM - To One Matching Compiler
#

TSADT_SRC	= ${srcdir}/src/adt/TomSignature-${VERSION}.jar:${prefix}/share/TomSignature-${VERSION}.jar:${srcdir}/stable/adt/TomSignature-${VERSION}.jar

javadir         = ${prefix}/classes/jtom/compiler
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(JAVAROOT)/adt:$(srcdir):$(JAVA_ATERM):$(SHARED_OBJECTS):$(TSADT_SRC):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/compiler
BOOTSOURCES	= TomKernelCompiler.java TomKernelExpander.java\
                  TomCompiler.java TomExpander.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src

CLEANFILES 	= TomKernelCompiler.java TomKernelExpander.java \
		  TomCompiler.java TomExpander.java

EXTRA_DIST 	= $(BOOTSOURCES)\
		  TomKernelCompiler.t TomKernelExpander.t\
		  TomCompiler.t TomExpander.t

BUILT_SOURCES   =  TomKernelCompiler.java TomKernelExpander.java\
		   TomCompiler.java TomExpander.java

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noDeclaration
TOM = ${top_builddir}/utils/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*

%.tom.c : %.t
	$(TOM) --cCode $(TOMFLAGS) $*

TomKernelCompiler.java: TomKernelCompiler.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<

TomKernelExpander.java: TomKernelExpander.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<

TomCompiler.java: TomCompiler.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<

TomExpander.java: TomExpander.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<
