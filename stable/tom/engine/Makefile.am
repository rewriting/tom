#
#    TOM - To One Matching Compiler
#


SUBDIRS = tools exception parser checker compiler optimizer backend runtime debug xml verifier

javadir         = ${prefix}/classes/jtom

JAVASOURCES     = Tom.java TomBase.java TomEnvironment.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

include $(top_srcdir)/stable/make_rules
