#
#    TOM - To One Matching Compiler
#

SUBDIRS         = strategies set xml

javadir         = ${prefix}/classes/jtom/runtime
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = GenericTraversal.java\
		  CollectReach.java\
                  Collect.java Collect1.java Collect2.java Collect3.java Collect4.java\
		  Replace.java Replace1.java Replace2.java Replace3.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

