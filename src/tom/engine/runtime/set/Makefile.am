#
#    TOM - To One Matching Compiler
#

#SUBDIRS		= jgtreeset
javadir         = ${prefix}/classes/jtom/runtime/set
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(SETADT):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/runtime/set
BOOTSOURCES	= SharedSet.java SharedMultiSet.java ATermSet.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= SharedSet.java SharedMultiSet.java ATermSet.java
EXTRA_DIST 	= $(BOOTSOURCES) SharedSet.t SharedMultiSet.t ATermSet.t set.adt

BUILT_SOURCES   = SharedSet.java SharedMultiSet.java ATermSet.java

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

# we use the last installed compiler to compile the current compiler
TOMFLAGS = 
TOM = ${prefix}/bin/jtom

ATermSet.java : ATermSet.t $(JAVAROOT)/adt/set.tom
	$(TOM) $(TOMFLAGS) $<

SharedSet.java : SharedSet.t $(JAVAROOT)/adt/set.tom
	$(TOM) $(TOMFLAGS) --noDeclaration $<

SharedMultiSet.java : SharedMultiSet.t $(JAVAROOT)/adt/set.tom
	$(TOM) $(TOMFLAGS) --noDeclaration $<

