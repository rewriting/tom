#
#    TOM - To One Matching Compiler
#

TSADT_STABLE=$(top_srcdir)/stable/adt/TomSignature-${VERSION}.jar
SETADT_STABLE=$(top_srcdir)/stable/adt/set-${VERSION}.jar
TNODEADT_STABLE=$(top_srcdir)/stable/adt/TNode-${VERSION}.jar

javadir         = ${prefix}/classes/jtom/tools
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(TSADT_STABLE):$(TNODEADT_STABLE):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = Tools.java OutputCode.java\
		  SingleLineOutputCode.java SymbolTable.java\
                  ASTFactory.java TomTask.java TomTaskInput.java TomFactory.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

