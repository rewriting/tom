#
#    TOM - To One Matching Compiler
#
SUBDIRS		= strategies set xml mapping

javadir         = ${prefix}/classes/jtom/runtime
stabledir 	= $(top_srcdir)/stable/jtom/runtime

BOOTSOURCES	= GenericTraversal.java\
		  CollectReach.java\
		  Collect.java Collect1.java Collect2.java Collect3.java Collect4.java\
		  Replace.java Replace1.java Replace2.java Replace3.java

java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES)

include $(top_srcdir)/src/make_rules
