#
#    TOM - To One Matching Compiler
#


javadir         = ${prefix}/classes/jtom/checker
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(TOMSIGNATURE):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/checker
BOOTSOURCES	= TomChecker.java TomSyntaxChecker.java TomTypeChecker.java TomCheckerMessage.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= TomChecker.java
EXTRA_DIST 	= $(BOOTSOURCES) TomChecker.t

BUILT_SOURCES   = TomChecker.java

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

# we use the last installed compiler to compile the current compiler
TOMFLAGS = --noWarning --noDeclaration
TOM = ${prefix}/bin/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*

%.tom.c : %.t
	$(TOM) --cCode $(TOMFLAGS) $*

TomChecker.t: ../adt/TomSignature.tom
	@touch TomChecker.t

../adt/TomSignature.tom: ../TomSignature.adt
	${ADTTOJAVA} -v -i ../TomSignature.adt -j -p jtom.adt --output ../adt 
	mv ../adt/TomSignatureMakeRules ../adt/TomSignatureMakeRules.am
