#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom
stabledir 	= $(top_srcdir)/stable/jtom/runtime/mapping/c

MAPPING         = aterm.tom int.tom string.tom double.tom char.tom
EXTRA_DIST 	= ${MAPPING}
CLEANFILES 	= 

adtdir          = $(datadir)/jtom/c
adt_DATA        = ${MAPPING}

include $(top_srcdir)/src/make_rules
