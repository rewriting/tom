#
#    TOM - To One Matching Compiler
#

SUBDIRS = adt tools exception parser checker compiler backend runtime debug xml
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

javadir         = ${prefix}/classes/jtom

JAVASOURCES     = Tom.java TomBase.java TomEnvironment.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)
JAVAROOT        = ${top_builddir}/stable/
