#
#    TOM - To One Matching Compiler
#

include TNodeMakeRules.am

javadir         = ${prefix}/classes/jtom/runtime/xml/adt
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/runtime/xml/adt

${TNodeAPI}: TNode.tom

BOOTSOURCES	= ${TNodeAPI}

java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES) TNodeMakeRules.am TNode.tom
JAVAROOT        = ${top_builddir}/src/

adtdir          = $(datadir)/jtom
adt_DATA        = TNode.tom

CLEANFILES 	= $(BOOTSOURCES) TNode.tom

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f TNode.tom TNodeMakeRules.am $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done
