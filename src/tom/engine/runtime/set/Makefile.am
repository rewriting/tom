#
#    TOM - To One Matching Compiler
#

SUBDIRS		= jgtreeset
javadir         = ${prefix}/classes/jtom/runtime/set
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/runtime/set
BOOTSOURCES	= SharedSet.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= SharedSet.java
EXTRA_DIST 	= $(BOOTSOURCES) SharedSet.t

BUILT_SOURCES   = SharedSet.java

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noWarning --import $(top_srcdir)/src/jtom/runtime/set/jgtreeset
TOM = ${prefix}/bin/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*

%.tom.c : %.t
	$(TOM) --cCode $(TOMFLAGS) $*
