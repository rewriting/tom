#
#    TOM - To One Matching Compiler
#

SUBDIRS         = c caml

javadir         = ${prefix}/classes/jtom
stabledir 	= $(top_srcdir)/stable/jtom/runtime/mapping

BOOTSOURCES 	= dom.tom charlist.tom int.tom double.tom string.tom char.tom aterm.tom atermlist.tom
EXTRA_DIST 	= ${BOOTSOURCES}
CLEANFILES 	= 

adtdir          = $(datadir)/jtom
adt_DATA        = ${BOOTSOURCES}

include $(top_srcdir)/src/make_rules
