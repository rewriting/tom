#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/debug
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = TomDebugger.java TomDebugEnvironment.java TermHandler.java\
		  TomDebugStructure.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

