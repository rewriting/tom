#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom
stabledir 	= $(top_srcdir)/stable/jtom/runtime/mapping/c

BOOTSOURCES         = aterm.tom atermlist.tom int.tom string.tom double.tom char.tom
EXTRA_DIST 	= ${BOOTSOURCES}
CLEANFILES 	= 

adtdir          = $(datadir)/jtom/c
adt_DATA        = ${BOOTSOURCES}

include $(top_srcdir)/src/make_rules
