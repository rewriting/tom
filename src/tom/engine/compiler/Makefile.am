#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/compiler
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/compiler
BOOTSOURCES	= TomKernelCompiler.java TomKernelExpander.java\
                  TomCompiler.java TomExpander.java
java_JAVA       = $(BOOTSOURCES)

JAVAROOT        = ${top_builddir}/src/

CLEANFILES 	= TomKernelCompiler.java TomKernelExpander.java \
		  TomCompiler.java TomExpander.java

EXTRA_DIST 	= $(BOOTSOURCES)\
		  TomKernelCompiler.t TomKernelExpander.t\
		  TomCompiler.t TomExpander.t

BUILT_SOURCES   =  TomKernelCompiler.java TomKernelExpander.java\
		   TomCompiler.java TomExpander.java

bootinstall:
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

TomKernelCompiler.t: ../adt/TomSignature.tom
	@touch TomKernelCompiler.t

TomKernelExpander.t: ../adt/TomSignature.tom
	@touch TomKernelExpander.t

TomCompiler.t: ../adt/TomSignature.tom
	@touch TomCompiler.t

TomExpander.t: ../adt/TomSignature.tom
	@touch TomExpander.t

../adt/TomSignature.tom: ../TomSignature.adt
	${ADTTOJAVA} -v -i ../TomSignature.adt -j -p jtom.adt --output ../adt 
	mv ../adt/TomSignatureMakeRules ../adt/TomSignatureMakeRules.am
