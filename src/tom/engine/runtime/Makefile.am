#
#    TOM - To One Matching Compiler
#
SUBDIRS		= strategies set xml
javadir         = ${prefix}/classes/jtom/runtime
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(TOMSIGNATURE):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/runtime
BOOTSOURCES	= GenericTraversal.java\
		  CollectReach.java\
		  Collect.java Collect1.java Collect2.java Collect3.java Collect4.java\
		  Replace.java Replace1.java Replace2.java Replace3.java

JARFILE         = concurrent.jar
java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES) ${JARFILE}
data_DATA       = ${JARFILE}

JAVAROOT        = ${top_builddir}/src/

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done
