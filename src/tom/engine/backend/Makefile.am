#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/backend
stabledir 	= $(top_srcdir)/stable/jtom/backend

TOMSOURCES      = TomAbstractGenerator.t TomGenericGenerator.t\
                  TomImperativeGenerator.t TomJavaGenerator.t TomCGenerator.t\
                  TomCamlGenerator.t TomEiffelGenerator.t
BOOTSOURCES	= $(TOMSOURCES:.t=.java) TomBackend.java
java_JAVA       = $(BOOTSOURCES)
CLEANFILES 	= $(TOMSOURCES:.t=.java) tomjava.stamp
EXTRA_DIST 	= $(BOOTSOURCES) $(TOMSOURCES)
BUILT_SOURCES   = tomjava.stamp $(BOOTSOURCES)

TOMFLAGS = --noDeclaration -O
include $(top_srcdir)/src/make_rules
