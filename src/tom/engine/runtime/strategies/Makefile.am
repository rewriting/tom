#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/runtime/strategies
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/runtime/strategies

BOOTSOURCES	= Strat.java StratList.java RunnerGroup.java\
		  All.java Pick.java One.java First.java\
		  Seq.java Rule.java\
		  EvalGetAll.java EvalGetOne.java

java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES)
JAVAROOT        = ${top_builddir}/src/


bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done
