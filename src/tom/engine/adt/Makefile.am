#
#    TOM - To One Matching Compiler
#

include TomSignatureMakeRules.am

javadir         = ${prefix}/classes/jtom/adt
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/adt

BOOTSOURCES	= ${TomSignatureAPI}

java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES) TomSignatureMakeRules.am Tom.signature
JAVAROOT        = ${top_builddir}/src/

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f TomSignatureMakeRules.am $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done
