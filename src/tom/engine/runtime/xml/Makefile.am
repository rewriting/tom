#
#    TOM - To One Matching Compiler
#

#SUBDIRS         = adt
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

javadir         = ${prefix}/classes/jtom/runtime/xml
stabledir 	= $(top_srcdir)/stable/jtom/runtime/xml

BOOTSOURCES	= XMLToATerm.java ATermToXML.java XmlTools.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= XMLToATerm.java ATermToXML.java
EXTRA_DIST 	= $(BOOTSOURCES) XMLToATerm.t ATermToXML.t TNode.adt

BUILT_SOURCES   = XMLToATerm.java ATermToXML.java XmlTools.java

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

ATermToXML.java: ATermToXML.t adt/TNode.tom
	$(TOM) $(TOMFLAGS) $<

XMLToATerm.java: XMLToATerm.t adt/TNode.tom
	$(TOM) $(TOMFLAGS) $<

api:
	${ADTTOTOM} --javagen -i TNode.adt -n TNode -p jtom.runtime.xml.adt --output $(JAVAROOT)

adt/TNode.tom: TNode.adt
	${ADTTOTOM} --javagen -i TNode.adt -n TNode -p jtom.runtime.xml.adt --output $(JAVAROOT)

TOMFLAGS = --noWarning 
TOM = ${prefix}/bin/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*
