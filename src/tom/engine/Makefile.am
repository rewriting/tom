#
#    TOM - To One Matching Compiler
#

SUBDIRS         = runtime tools exception parser checker compiler backend debug xml

CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(TOMSIGNATURE):$$CLASSPATH

javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/jtom
BOOTSOURCES	= Tom.java TomBase.java TomEnvironment.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

EXTRA_DIST 	= $(BOOTSOURCES) TomBase.t TomSignature.adt
CLEANFILES 	= TomBase.java ${JARFILE}
BUILT_SOURCES   = TomBase.java

JARFILE         = TomSignature-$(VERSION).jar
data_DATA       = ${JARFILE}

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

TomBase.java: TomBase.t adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<

api:
	${ADTTOTOM}  -i TomSignature.adt -n TomSignature --output adt
	${ADTTOJAVA} --version $(VERSION) -i TomSignature.adt -n TomSignature -p jtom.adt --output $(JAVAROOT)

adt/TomSignature.tom: TomSignature.adt
	$(MAKE) api

$(JARFILE): adt/TomSignature.tom
	$(MAKE) api

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noWarning
TOM = ${prefix}/bin/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*
