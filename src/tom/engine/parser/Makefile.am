#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/parser
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/parser
BOOTSOURCES	= JavaCharStream.java\
		  TomBuffer.java

generated_java  = Token.java\
		  ParseException.java\
		  TokenMgrError.java\
		  TomParser.java\
		  TomParserConstants.java\
		  TomParserTokenManager.java

java_JAVA       = $(BOOTSOURCES) $(generated_java)

JAVAROOT        = ${top_builddir}/src/

JAVACC          = @JAVACC@

CLEANFILES 	= 
#$(generated_java)

$(generated_java): TomParser.jj
	$(JAVACC) $<
	touch Token.java ParseException.java TokenMgrError.java

EXTRA_DIST 	= $(BOOTSOURCES) $(generated_java) TomParser.jj

#	          ASCII_UCodeESC_CharStream.static.java\
#                  ASCII_UCodeESC_CharStream.nonstatic.java

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(generated_java) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done
