#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/runtime/xml
stabledir 	= $(top_srcdir)/stable/jtom/runtime/xml

TOMSOURCES	= XMLToATerm.t ATermToXML.t
BOOTSOURCES	= XMLToATerm.java ATermToXML.java XmlTools.java
java_JAVA       = $(BOOTSOURCES)
CLEANFILES 	= $(TOMSOURCES:.t=.java) tomjava.stamp
EXTRA_DIST 	= $(BOOTSOURCES) $(TOMSOURCES)
BUILT_SOURCES   = tomjava.stamp $(BOOTSOURCES)

TOMFLAGS =
include $(top_srcdir)/src/make_rules
