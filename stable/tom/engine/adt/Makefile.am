#
#    TOM - To One Matching Compiler
#
 
include TomSignatureMakeRules.am

javadir         = ${prefix}/classes/jtom/adt
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = ${TomSignatureAPI}
java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES) TomSignatureMakeRules.am

JAVAROOT        = ${top_builddir}/stable/

