#
#    TOM - To One Matching Compiler
#

SUBDIRS         = adt

javadir         = ${prefix}/classes/jtom/runtime/xml
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = ATermToXML.java XMLToATerm.java XmlTools.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

