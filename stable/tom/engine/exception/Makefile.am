#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/exception
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(TSADT_STABLE):$(srcdir):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = TomException.java CheckErrorException.java CheckWarningException.java\
		  TomIncludeException.java TomRuntimeException.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

