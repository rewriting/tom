#
#    TOM - To One Matching Compiler
#


javadir         = ${prefix}/classes/jtom/checker
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/checker
BOOTSOURCES	= TomChecker.java TomSyntaxChecker.java TomTypeChecker.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= TomChecker.java
EXTRA_DIST 	= $(BOOTSOURCES) TomChecker.t

BUILT_SOURCES   = TomChecker.java TomSyntaxChecker.java TomTypeChecker.java

bootinstall :
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
