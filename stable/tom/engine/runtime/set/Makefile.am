#
#    TOM - To One Matching Compiler
#

SUBDIRS         = jgtreeset

javadir         = ${prefix}/classes/jtom/runtime/set
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(TSADT_STABLE):$(SETADT_STABLE):$(srcdir):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = ATermSet.java SharedMultiSet.java SharedSet.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

