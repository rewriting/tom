#
#    TOM - To One Matching Compiler
#

SUBDIRS         = runtime adt tools exception parser checker compiler backend debug buttons 
# buttons 
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

BUTTONSDIR = buttons

javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/jtom
BOOTSOURCES	= Tom.java TomBase.java  TomDemo.java TomEnvironment.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

EXTRA_DIST 	= $(BOOTSOURCES) TomBase.t TomSignature.adt
CLEANFILES 	= TomBase.java
BUILT_SOURCES   = TomBase.java

install:
	cp -rf $(BUTTONSDIR) ${javadir}

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	cp -rf $(BUTTONSDIR) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

api:
	adt-to-java -v -i TomSignature.adt -j -p jtom.adt -o adt -m 'aterm.*'
	mv adt/TomSignatureMakeRules adt/TomSignatureMakeRules.am
	mv adt/TomSignature.t adt/Tom.signature

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noWarning --import $(top_srcdir)/src/jtom/adt
TOM = ${prefix}/bin/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*
