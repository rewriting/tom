#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/compiler
stabledir 	= $(top_srcdir)/stable/jtom/compiler

TOMSOURCES	= TomKernelCompiler.t TomKernelExpander.t\
                  TomCompiler.t TomExpander.t
BOOTSOURCES	= $(TOMSOURCES:.t=.java) 
java_JAVA       = $(BOOTSOURCES)
CLEANFILES 	= $(TOMSOURCES:.t=.java) tomjava.stamp
EXTRA_DIST 	= $(BOOTSOURCES) $(TOMSOURCES)
BUILT_SOURCES   = tomjava.stamp $(BOOTSOURCES)

TOMFLAGS = --noDeclaration -O
include $(top_srcdir)/src/make_rules
