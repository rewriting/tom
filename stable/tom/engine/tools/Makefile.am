#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/tools
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = Tools.java OutputCode.java\
		  SingleLineOutputCode.java SymbolTable.java\
                  ASTFactory.java TomTask.java TomTaskInput.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

