#
#    TOM - To One Matching Compiler
#

TSADT_STABLE=$(top_srcdir)/stable/adt/TomSignature-${VERSION}.jar
SETADT_STABLE=$(top_srcdir)/stable/adt/set-${VERSION}.jar
TNODEADT_STABLE=$(top_srcdir)/stable/adt/TNode-${VERSION}.jar

javadir         = ${prefix}/classes/jtom/exception
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(TSADT_STABLE):$(srcdir):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = TomException.java CheckErrorException.java CheckWarningException.java\
		  TomIncludeException.java TomRuntimeException.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

