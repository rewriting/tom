#
#    TOM - To One Matching Compiler
#

SUBDIRS         = adt runtime tools exception parser checker compiler backend debug 

CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/jtom
BOOTSOURCES	= Tom.java TomBase.java TomEnvironment.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

EXTRA_DIST 	= $(BOOTSOURCES) TomBase.t TomSignature.adt
CLEANFILES 	= TomBase.java
BUILT_SOURCES   = TomBase.java

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

api:
	adt-to-java -v -i TomSignature.adt -j -p jtom.adt -o adt -m 'aterm.*'
	mv adt/TomSignatureMakeRules adt/TomSignatureMakeRules.am

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noWarning
TOM = ${prefix}/bin/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*
