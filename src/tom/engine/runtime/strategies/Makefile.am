#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/runtime/strategies
stabledir 	= $(top_srcdir)/stable/jtom/runtime/strategies

BOOTSOURCES	= Strat.java StratList.java RunnerGroup.java\
		  All.java Pick.java One.java First.java\
		  Seq.java Rule.java\
		  EvalGetAll.java EvalGetOne.java

java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES)

include $(top_srcdir)/src/make_rules
