#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/exception
stabledir 	= $(top_srcdir)/stable/jtom/exception

BOOTSOURCES	= TomException.java TomRuntimeException.java\
                  CheckWarningException.java CheckErrorException.java\
                  TomIncludeException.java
java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES)

include $(top_srcdir)/src/make_rules
