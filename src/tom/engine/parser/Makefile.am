#
#    TOM - To One Matching Compiler
#

TSADT_SRC	= ${top_srcdir}/src/adt/TomSignature-${VERSION}.jar:${prefix}/share/TomSignature-${VERSION}.jar:${top_srcdir}/stable/adt/TomSignature-${VERSION}.jar

javadir         = ${prefix}/classes/jtom/parser
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(JAVAROOT)/adt:$(srcdir):$(JAVA_ATERM):$(TSADT_SRC):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/parser
BOOTSOURCES	= JavaCharStream.java\
		  TomBuffer.java\
		  TomBackQuoteParser.java

generated_java  = Token.java\
		  ParseException.java\
		  TokenMgrError.java\
		  TomParser.java\
		  TomParserConstants.java\
		  TomParserTokenManager.java

java_JAVA       = $(BOOTSOURCES) $(generated_java)

JAVAROOT        = ${top_builddir}/src

JAVACC          = @JAVACC@

CLEANFILES 	= TomBackQuoteParser.java 
#$(generated_java)

$(generated_java): TomParser.jj
	$(JAVACC) $<
	@touch Token.java ParseException.java TokenMgrError.java

EXTRA_DIST 	= $(BOOTSOURCES) $(generated_java) TomParser.jj TomBackQuoteParser.t 

BUILT_SOURCES   = TomBackQuoteParser.java 

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(generated_java) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done

# we use the last installed compiler to compile the current compiler
TOMFLAGS = 
TOM = ${top_builddir}/utils/jtom
TomBackQuoteParser.java: TomBackQuoteParser.t $(JAVAROOT)/adt/TomSignature.tom
	$(TOM) $(TOMFLAGS) $<
