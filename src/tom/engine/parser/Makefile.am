#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/parser
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

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

JAVAROOT        = ${top_builddir}/src/

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
TOMFLAGS = --noWarning
TOM = ${prefix}/bin/jtom

%.java : %.t
	$(TOM) $(TOMFLAGS) $*

%.tom.c : %.t
	$(TOM) --cCode $(TOMFLAGS) $*

TomBackQuoteParser.t: ../adt/TomSignature.tom
	@touch TomBackQuoteParser.t

../adt/TomSignature.tom: ../TomSignature.adt
	${ADTTOJAVA} -v -i ../TomSignature.adt -j -p jtom.adt --output ../adt 
	mv ../adt/TomSignatureMakeRules ../adt/TomSignatureMakeRules.am
