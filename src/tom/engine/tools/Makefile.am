#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/tools
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(TOMSIGNATURE):$$CLASSPATH

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

TOMFLAGS = --noDeclaration
TOM = ${prefix}/bin/jtom
TomFactory.java: TomFactory.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<
