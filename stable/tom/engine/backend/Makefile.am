#
#    TOM - To One Matching Compiler
#

TSADT_STABLE=$(top_srcdir)/stable/adt/TomSignature-${VERSION}.jar
SETADT_STABLE=$(top_srcdir)/stable/adt/Set-${VERSION}.jar
TNODEADT_STABLE=$(top_srcdir)/stable/adt/TNode-${VERSION}.jar

javadir         = ${prefix}/classes/jtom/backend
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir):$(TSADT_STABLE):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = TomAbstractGenerator.java TomGenericGenerator.java TomBackend.java TomCGenerator.java TomCamlGenerator.java TomEiffelGenerator.java TomImperativeGenerator.java TomJavaGenerator.java
java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

