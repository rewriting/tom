#
#    TOM - To One Matching Compiler
#

TSADT_SRC	= ${top_srcdir}/src/adt/TomSignature-${VERSION}.jar:${prefix}/share/TomSignature-${VERSION}.jar:${top_srcdir}/stable/adt/TomSignature-${VERSION}.jar

javadir         = ${prefix}/classes/jtom/tools
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(JAVAROOT)/adt:$(srcdir):$(JAVA_ATERM):$(TSADT_SRC):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/tools
BOOTSOURCES	= Tools.java OutputCode.java SingleLineOutputCode.java\
		  ASTFactory.java TomFactory.java\
                  SymbolTable.java TomTask.java TomTaskInput.java


java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES) TomFactory.t
JAVAROOT        = ${top_builddir}/src

CLEANFILES      = TomFactory.java
BUILT_SOURCES   = TomFactory.java

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

TOMFLAGS = --noDeclaration
TOM = ${top_builddir}/utils/jtom
TomFactory.java: TomFactory.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<
