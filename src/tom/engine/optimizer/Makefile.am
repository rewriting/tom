#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/optimizer
stabledir 	= $(top_srcdir)/stable/jtom/optimizer

TOMSOURCES  = TomOptimizer.t
BOOTSOURCES	= $(TOMSOURCES:.t=.java) 
java_JAVA   = $(BOOTSOURCES)
CLEANFILES 	= $(TOMSOURCES:.t=.java) tomjava.stamp
EXTRA_DIST 	= $(BOOTSOURCES) $(TOMSOURCES)
BUILT_SOURCES   = tomjava.stamp $(BOOTSOURCES)

LOCALFLAGS = --noDeclaration 
include $(top_srcdir)/src/make_rules
