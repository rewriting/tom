#
#    TOM - To One Matching Compiler
#

include TNodeMakeRules.am

javadir         = ${prefix}/classes/jtom/runtime/xml/adt
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = ${TNodeAPI}

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES) TNode.tom TNodeMakeRules.am

JAVAROOT        = ${top_builddir}/stable/

adtdir          = $(datadir)/jtom
adt_DATA        = TNode.tom
