#
#    TOM - To One Matching Compiler
#

SUBDIRS         = c caml

javadir         = ${prefix}/classes/jtom
stabledir 	= $(top_srcdir)/stable/jtom/runtime/mapping

MAPPING 	= dom.tom charlist.tom int.tom double.tom string.tom char.tom
EXTRA_DIST 	= ${MAPPING}
CLEANFILES 	= 

adtdir          = $(datadir)/jtom
adt_DATA        = ${MAPPING}

include $(top_srcdir)/src/make_rules
