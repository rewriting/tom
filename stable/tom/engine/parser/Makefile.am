#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/parser

JAVASOURCES     = JavaCharStream.java\
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

include $(top_srcdir)/stable/make_rules