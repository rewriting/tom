#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/compiler
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(TSADT_STABLE):$(srcdir):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = TomKernelExpander.java TomKernelCompiler.java\
                  TomExpander.java TomCompiler.java
java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

