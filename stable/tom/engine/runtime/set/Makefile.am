#
#    TOM - To One Matching Compiler
#

TSADT_STABLE=$(top_srcdir)/stable/adt/TomSignature-${VERSION}.jar
SETADT_STABLE=$(top_srcdir)/stable/adt/set-${VERSION}.jar
TNODEADT_STABLE=$(top_srcdir)/stable/adt/TNode-${VERSION}.jar

#SUBDIRS         = jgtreeset

javadir         = ${prefix}/classes/jtom/runtime/set
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(TSADT_STABLE):$(SETADT_STABLE):$(srcdir):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = ATermSet.java SharedMultiSet.java SharedSet.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

