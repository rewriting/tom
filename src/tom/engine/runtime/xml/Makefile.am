#
#    TOM - To One Matching Compiler
#

#SUBDIRS         = adt
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(JAVAROOT)/adt:$(srcdir):$(JAVA_ATERM):$(SHARED_OBJECTS):$(TNODEADT):$$CLASSPATH

javadir         = ${prefix}/classes/jtom/runtime/xml
stabledir 	= $(top_srcdir)/stable/jtom/runtime/xml

BOOTSOURCES	= XMLToATerm.java ATermToXML.java XmlTools.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= XMLToATerm.java ATermToXML.java
EXTRA_DIST 	= $(BOOTSOURCES) XMLToATerm.t ATermToXML.t

BUILT_SOURCES   = XMLToATerm.java ATermToXML.java XmlTools.java

bootinstall:
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

TOMFLAGS =
TOM = ${prefix}/bin/jtom

ATermToXML.java: ATermToXML.t $(JAVAROOT)/adt/TNode.tom
	$(TOM) $(TOMFLAGS) $<

XMLToATerm.java: XMLToATerm.t $(JAVAROOT)/adt/TNode.tom
	$(TOM) $(TOMFLAGS) $<
