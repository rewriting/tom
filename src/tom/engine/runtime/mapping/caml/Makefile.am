#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom
stabledir 	= $(top_srcdir)/stable/jtom/runtime/mapping/caml

MAPPING         = int.tom list.tom
EXTRA_DIST 	= ${MAPPING}
CLEANFILES 	= 

adtdir          = $(datadir)/jtom/caml
adt_DATA        = ${MAPPING}

include $(top_srcdir)/src/make_rules
