#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/tools
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/tools
BOOTSOURCES	= Tools.java OutputCode.java SingleLineOutputCode.java\
		  ASTFactory.java TomFactory.java\
                  SymbolTable.java TomTask.java TomTaskInput.java


java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES) TomFactory.t
JAVAROOT        = ${top_builddir}/src/

CLEANFILES      = TomFactory.java
BUILT_SOURCES   = TomFactory.java

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

TOMFLAGS = --noWarning --noDeclaration
TOM = ${prefix}/bin/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*

TomFactory.t: ../adt/TomSignature.tom
	@touch TomFActory.t

../adt/TomSignature.tom: ../TomSignature.adt
	${ADTTOJAVA} -v -i ../TomSignature.adt -j -p jtom.adt --output ../adt 
	mv ../adt/TomSignatureMakeRules ../adt/TomSignatureMakeRules.am
