#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/compiler
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/compiler
BOOTSOURCES	= TomCompiler.java TomGenerator.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= TomCompiler.java TomGenerator.java Tom.signature 
EXTRA_DIST 	= $(BOOTSOURCES) TomCompiler.t TomGenerator.t

BUILT_SOURCES   = TomCompiler.java TomGenerator.java

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noWarning --noDeclaration --import $(top_srcdir)/src/jtom/adt
TOM = ${prefix}/bin/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*

%.tom.c : %.t
	$(TOM) --cCode $(TOMFLAGS) $*
