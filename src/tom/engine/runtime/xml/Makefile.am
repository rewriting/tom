#
#    TOM - To One Matching Compiler
#

SUBDIRS         = adt
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

javadir         = ${prefix}/classes/jtom/runtime/xml
stabledir 	= $(top_srcdir)/stable/jtom/runtime/xml

BOOTSOURCES	= XMLToATerm.java ATermToXML.java XmlTools.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= XMLToATerm.java ATermToXML.java
EXTRA_DIST 	= $(BOOTSOURCES) XMLToATerm.t ATermToXML.t NodeTerm.adt

BUILT_SOURCES   = XMLToATerm.java ATermToXML.java XmlTools.java

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

ATermToXML.t: adt/NodeTerm.tom
	@touch ATermToXML.t

XMLToATerm.t: adt/NodeTerm.tom
	@touch XMLToATerm.t

adt/NodeTerm.tom: NodeTerm.adt
	${ADTTOJAVA} -v -i NodeTerm.adt -j -p jtom.runtime.xml.adt --output adt 
	mv adt/NodeTermMakeRules adt/NodeTermMakeRules.am

TOMFLAGS = --noWarning 
TOM = ${prefix}/bin/jtom.src

%.java : %.t
	$(TOM) $(TOMFLAGS) $*
