#
#    TOM - To One Matching Compiler
#

MANUAL_PS = manual.ps manual.pdf
MANUAL_HTML = manual.html install.html language.html lib.html\
	      system.html tutorial.html manual1.html manual1001.html\
	      contents_motif.gif next_motif.gif previous_motif.gif depgraphjtom.pgn

EXTRA_DIST = $(MANUAL_PS) $(MANUAL_HTML)

WWWDIR = /local/www-equipes/protheo/SOFTWARES/tom
MANUALDIR = ${WWWDIR}/doc/manual

www:
	cp ${MANUAL_HTML} ${MANUAL_PS} ${MANUALDIR}
	chmod 666 ${MANUALDIR}/*.*
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
        done
