#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/runtime/strategies

runtimedir 	= $(top_srcdir)/src/jtom/runtime
CONCURRENT_JAR  = $(runtimedir)/concurrent.jar:${prefix}/share/concurrent.jar
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):${CONCURRENT_JAR}:$$CLASSPATH

BOOTSOURCES	= Strat.java StratList.java RunnerGroup.java\
		  All.java Pick.java One.java First.java\
		  Seq.java Rule.java\
		  EvalGetAll.java EvalGetOne.java

java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES)
JAVAROOT        = ${top_builddir}/stable

