#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/runtime/set
stabledir 	= $(top_srcdir)/stable/jtom/runtime/set

TOMSOURCES  = SharedSet.t SharedMultiSet.t ATermSet.t
JAVASOURCES =
BOOTSOURCES	= $(TOMSOURCES:.t=.java) $(JAVASOURCES)
java_JAVA   = $(BOOTSOURCES)
CLEANFILES 	= $(TOMSOURCES:.t=.java) tomjava.stamp
EXTRA_DIST 	= $(BOOTSOURCES) $(TOMSOURCES)
BUILT_SOURCES   = tomjava.stamp $(BOOTSOURCES)

LOCALFLAGS = 
include $(top_srcdir)/src/make_rules
