#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/backend
stabledir 	= $(top_srcdir)/stable/jtom/backend

TOMSOURCES      = TomAbstractGenerator.t TomGenericGenerator.t\
                  TomImperativeGenerator.t TomJavaGenerator.t TomCGenerator.t\
                  TomCamlGenerator.t TomEiffelGenerator.t
JAVASOURCES = TomBackend.java
BOOTSOURCES	= $(TOMSOURCES:.t=.java) $(JAVASOURCES)
java_JAVA       = $(BOOTSOURCES)
CLEANFILES 	= $(TOMSOURCES:.t=.java) tomjava.stamp
EXTRA_DIST 	= $(BOOTSOURCES) $(TOMSOURCES)
BUILT_SOURCES   = tomjava.stamp $(BOOTSOURCES)

LOCALFLAGS = --noDeclaration 
include $(top_srcdir)/src/make_rules
