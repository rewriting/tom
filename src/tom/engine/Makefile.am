#
#    TOM - To One Matching Compiler
#

SUBDIRS         = runtime tools exception parser checker compiler backend debug xml

TSADT_SRC	= ${top_srcdir}/src/adt/TomSignature-${VERSION}.jar:${prefix}/share/TomSignature-${VERSION}.jar:${top_srcdir}/stable/adt/TomSignature-${VERSION}.jar

CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(JAVAROOT)/adt:$(srcdir):$(JAVA_ATERM):$(TSADT_SRC):$$CLASSPATH

javadir         = ${prefix}/classes/jtom

stabledir 	= $(top_srcdir)/stable/jtom
BOOTSOURCES	= Tom.java TomBase.java TomEnvironment.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src
EXTRA_DIST 	= $(BOOTSOURCES) TomBase.t
CLEANFILES 	= TomBase.java
BUILT_SOURCES   = TomBase.java


bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

TomBase.java: TomBase.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noWarning
TOM = ${top_builddir}/utils/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*
