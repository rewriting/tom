#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/tools
stabledir 	= $(top_srcdir)/stable/jtom/tools

TOMSOURCES      = TomFactory.t
BOOTSOURCES	= $(TOMSOURCES:.t=.java)\
		  Tools.java OutputCode.java ASTFactory.java \
                  SymbolTable.java TomTask.java TomTaskInput.java
java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES) $(TOMSOURCES)
CLEANFILES      = $(TOMSOURCES:.t=.java) tomjava.stamp
BUILT_SOURCES   = tomjava.stamp $(BOOTSOURCES)

LOCALFLAGS = --noDeclaration
include $(top_srcdir)/src/make_rules
