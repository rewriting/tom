#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/checker
stabledir 	= $(top_srcdir)/stable/jtom/checker

TOMSOURCES      = TomChecker.t
JAVASOURCES     = TomSyntaxChecker.java TomTypeChecker.java TomCheckerMessage.java
BOOTSOURCES	= $(TOMSOURCES:.t=.java) $(JAVASOURCES)
java_JAVA   = $(BOOTSOURCES)
CLEANFILES 	= $(TOMSOURCES:.t=.java) tomjava.stamp
EXTRA_DIST 	= $(BOOTSOURCES) $(TOMSOURCES)
BUILT_SOURCES   = tomjava.stamp $(BOOTSOURCES)

LOCALFLAGS = --noDeclaration 
include $(top_srcdir)/src/make_rules
