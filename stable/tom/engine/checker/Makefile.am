#
#    TOM - To One Matching Compiler
#

TSADT_STABLE=$(top_srcdir)/stable/adt/TomSignature-${VERSION}.jar
SETADT_STABLE=$(top_srcdir)/stable/adt/set-${VERSION}.jar
TNODEADT_STABLE=$(top_srcdir)/stable/adt/TNode-${VERSION}.jar

javadir         = ${prefix}/classes/jtom/checker
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(TSADT_STABLE):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = TomChecker.java TomSyntaxChecker.java TomTypeChecker.java TomCheckerMessage.java
java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/
