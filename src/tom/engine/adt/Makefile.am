#
#    TOM - To One Matching Compiler
#

include TomSignatureMakeRules.am

javadir         = ${prefix}/classes/jtom/adt
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/adt

${TomSignatureAPI}: TomSignature.tom
BOOTSOURCES	= ${TomSignatureAPI}

java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES) TomSignatureMakeRules.am TomSignature.tom
JAVAROOT        = ${top_builddir}/src/

adtdir          = $(datadir)/jtom/
adt_DATA        = TomSignature.tom

TomSignature.tom: ../TomSignature.adt
	${ADTTOJAVA} -v -i ../TomSignature.adt -j -p jtom.adt 
	mv TomSignatureMakeRules TomSignatureMakeRules.am

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f TomSignature.tom TomSignatureMakeRules.am $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done
