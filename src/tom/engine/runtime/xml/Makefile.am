#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/runtime/xml
stabledir 	= $(top_srcdir)/stable/jtom/runtime/xml

TOMSOURCES	= XMLToATerm.t ATermToXML.t
JAVASOURCES	= XmlTools.java
BOOTSOURCES	= $(TOMSOURCES:.t=.java) $(JAVASOURCES)
java_JAVA   = $(BOOTSOURCES)
CLEANFILES 	= $(TOMSOURCES:.t=.java) tomjava.stamp
EXTRA_DIST 	= $(BOOTSOURCES) $(TOMSOURCES)
BUILT_SOURCES   = tomjava.stamp $(BOOTSOURCES)

LOCALFLAGS = 
include $(top_srcdir)/src/make_rules
