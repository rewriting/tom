#
#    TOM - To One Matching Compiler
#

SUBDIRS = tools exception parser checker compiler backend runtime debug xml
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(TSADT_STABLE):$(srcdir):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

javadir         = ${prefix}/classes/jtom

JAVASOURCES     = Tom.java TomBase.java TomEnvironment.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)
JAVAROOT        = ${top_builddir}/stable/
