#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/parser
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

JAVASOURCES     = ASCII_UCodeESC_CharStream.java\
		  JavaCharStream.java\
                  TomBuffer.java\
		  Token.java\
                  ParseException.java\
                  TokenMgrError.java\
                  TomParser.java\
                  TomParserConstants.java\
                  TomParserTokenManager.java\
		  TomBackQuoteParser.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/
