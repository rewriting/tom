#
#    TOM - To One Matching Compiler
#

JAVACC          = @JAVACC@
javadir         = ${prefix}/classes/jtom/parser
stabledir 	= $(top_srcdir)/stable/jtom/parser

TOMSOURCES      = TomBackQuoteParser.t 
BOOTSOURCES	= $(TOMSOURCES:.t=.java)\
		  JavaCharStream.java\
		  TomBuffer.java\
		  $(generated_java)

generated_java  = Token.java\
		  ParseException.java\
		  TokenMgrError.java\
		  TomParser.java\
		  TomParserConstants.java\
		  TomParserTokenManager.java

java_JAVA       = $(BOOTSOURCES)

CLEANFILES 	= $(TOMSOURCES:.t=.java) tomjava.stamp

$(generated_java): TomParser.jj
	$(JAVACC) $<
	@touch Token.java ParseException.java TokenMgrError.java

EXTRA_DIST 	= $(BOOTSOURCES) $(TOMSOURCES) TomParser.jj

BUILT_SOURCES   = tomjava.stamp $(TOMSOURCES:.t=.java)

LOCALFLAGS = 

include $(top_srcdir)/src/make_rules
