#
#    TOM - To One Matching Compiler
#

EXTRA_DIST = 

WWWDIR = /local/www-equipes/protheo/SOFTWARES/tom
STYLESDIR = ${WWWDIR}/styles
STYLES = mainstyle.css

www:
	chmod 666 ${STYLES}
	scp ${STYLES} ${LORIAUSER}@loria.loria.fr:${STYLESDIR}
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
        done

