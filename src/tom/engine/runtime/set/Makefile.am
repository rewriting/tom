#
#    TOM - To One Matching Compiler
#

SUBDIRS		= jgtreeset
javadir         = ${prefix}/classes/jtom/runtime/set
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/runtime/set
BOOTSOURCES	= SharedSet.java SharedMultiSet.java ATermSet.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= SharedSet.java SharedMultiSet.java ATermSet.java
EXTRA_DIST 	= $(BOOTSOURCES) SharedSet.t SharedMultiSet.t ATermSet.t

BUILT_SOURCES   = SharedSet.java SharedMultiSet.java ATermSet.java

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

api:
	adt-to-java -v -i set.adt -j -p jtom.runtime.set.jgtreeset -o jgtreeset -m 'aterm.*'

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noWarning
TOM = ${prefix}/bin/jtom

SharedSet.java : SharedSet.t
	$(TOM) $(TOMFLAGS) --noDeclaration $<
SharedMultiSet.java : SharedMultiSet.t
	$(TOM) $(TOMFLAGS) --noDeclaration $<

%.java : %.t
	$(TOM) $(TOMFLAGS) $*

%.tom.c : %.t
	$(TOM) --cCode $(TOMFLAGS) $*

SharedSet.t: jgtreeset/set.tom
	@touch SharedSet.t

SharedMultiSet.t: jgtreeset/set.tom
	@touch SharedMultiSet.t

ATermSet.t: jgtreeset/set.tom
	@touch ATermSet.t

jgtreeset/set.tom: set.adt
	${ADTTOJAVA} -v -i set.adt -j -p jtom.runtime.set.jgtreeset --output jgtreeset
