#
#    TOM - To One Matching Compiler
#

include SetMakeRules.am

javadir         = ${prefix}/classes/jtom/runtime/set/jgtreeset
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH


JAVASOURCES     = ${SetAPI}
java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

